package eu.kanade.tachiyomi.ui.reader.novel

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.util.Locale

internal fun parseNovelRichContent(rawHtml: String): NovelRichContentParseResult {
    val doc = Jsoup.parse(rawHtml)
    val body = doc.body()
    val unsupported = detectUnsupportedRichFeatures(doc)
    val blocks = mutableListOf<NovelRichContentBlock>()

    val blockRoots = body.children().ifEmpty {
        doc.children().filterIsInstance<Element>()
    }

    blockRoots.forEach { element ->
        blocks += parseBlockElement(element)
    }

    if (blocks.isEmpty()) {
        val segments = parseInlineSegments(body)
        if (segments.isNotEmpty()) {
            blocks += NovelRichContentBlock.Paragraph(segments)
        }
    }

    return NovelRichContentParseResult(
        blocks = blocks,
        unsupportedFeaturesDetected = unsupported,
    )
}

private fun parseBlockElement(element: Element): List<NovelRichContentBlock> {
    val tag = element.tagName().lowercase(Locale.US)
    return when (tag) {
        "p", "div", "article", "section", "main" -> {
            parseParagraphLikeOrContainerBlocks(element, tag)
        }
        "h1", "h2", "h3", "h4", "h5", "h6" -> {
            val segments = parseInlineSegments(element)
            if (segments.isEmpty()) {
                emptyList()
            } else {
                val level = tag.removePrefix("h").toIntOrNull()?.coerceIn(1, 6) ?: 1
                listOf(
                    NovelRichContentBlock.Heading(
                        level = level,
                        segments = segments,
                        textAlign = parseBlockTextAlign(element.attr("style")),
                    ),
                )
            }
        }
        "blockquote" -> {
            val segments = parseInlineSegments(element)
            if (segments.isEmpty()) {
                emptyList()
            } else {
                listOf(
                    NovelRichContentBlock.BlockQuote(
                        segments = segments,
                        textAlign = parseBlockTextAlign(element.attr("style")),
                    ),
                )
            }
        }
        "hr" -> listOf(NovelRichContentBlock.HorizontalRule)
        "img" -> {
            val src = element.attr("src").trim()
            if (src.isBlank()) {
                emptyList()
            } else {
                listOf(NovelRichContentBlock.Image(url = src, alt = element.attr("alt").ifBlank { null }))
            }
        }
        else -> {
            // Try block-like descendants first (e.g. body/html wrappers), else flatten text.
            val childBlocks = element.children().flatMap(::parseBlockElement)
            if (childBlocks.isNotEmpty()) {
                childBlocks
            } else {
                val segments = parseInlineSegments(element)
                if (segments.isEmpty()) emptyList() else listOf(NovelRichContentBlock.Paragraph(segments))
            }
        }
    }
}

private fun parseParagraphLikeOrContainerBlocks(
    element: Element,
    tag: String,
): List<NovelRichContentBlock> {
    val blocks = mutableListOf<NovelRichContentBlock>()
    val pendingSegments = mutableListOf<NovelRichTextSegment>()
    val blockTextAlign = parseBlockTextAlign(element.attr("style"))

    fun flushParagraph() {
        val merged = mergeAdjacentRichSegments(pendingSegments)
        if (merged.isNotEmpty()) {
            blocks += NovelRichContentBlock.Paragraph(
                segments = merged,
                textAlign = blockTextAlign,
            )
        }
        pendingSegments.clear()
    }

    element.childNodes().forEach { node ->
        when (node) {
            is Element -> {
                val childTag = node.tagName().lowercase(Locale.US)
                if (childTag == "img") {
                    flushParagraph()
                    val src = node.attr("src").trim()
                    if (src.isNotBlank()) {
                        blocks += NovelRichContentBlock.Image(
                            url = src,
                            alt = node.attr("alt").ifBlank { null },
                        )
                    }
                    return@forEach
                }

                // Container-like nodes should preserve nested block ordering instead of flattening.
                if (tag != "p" && childTag in richContainerBlockTags) {
                    flushParagraph()
                    blocks += parseBlockElement(node)
                    return@forEach
                }
            }
        }

        when (node) {
            is TextNode -> parseInlineNode(node, NovelRichTextStyle(), null, pendingSegments)
            is Element -> parseInlineNode(node, NovelRichTextStyle(), null, pendingSegments)
        }
    }

    flushParagraph()
    return blocks
}

private fun parseInlineSegments(root: Element): List<NovelRichTextSegment> {
    val out = mutableListOf<NovelRichTextSegment>()
    root.childNodes().forEach { node ->
        parseInlineNode(
            node = node,
            inheritedStyle = NovelRichTextStyle(),
            inheritedLink = null,
            out = out,
        )
    }
    return mergeAdjacentRichSegments(out)
}

private fun parseBlockTextAlign(inlineStyle: String): NovelRichBlockTextAlign? {
    if (inlineStyle.isBlank()) return null
    return when (parseInlineCssMap(inlineStyle)["text-align"]?.trim()?.lowercase(Locale.US)) {
        "left", "start" -> NovelRichBlockTextAlign.LEFT
        "center" -> NovelRichBlockTextAlign.CENTER
        "justify" -> NovelRichBlockTextAlign.JUSTIFY
        "right", "end" -> NovelRichBlockTextAlign.RIGHT
        else -> null
    }
}

private fun parseInlineNode(
    node: Node,
    inheritedStyle: NovelRichTextStyle,
    inheritedLink: String?,
    out: MutableList<NovelRichTextSegment>,
) {
    when (node) {
        is TextNode -> {
            val text = node.text()
            if (text.isEmpty() || text.isBlank()) return
            out += NovelRichTextSegment(
                text = text,
                style = inheritedStyle,
                linkUrl = inheritedLink,
            )
        }
        is Element -> {
            val tag = node.tagName().lowercase(Locale.US)
            if (tag == "br") {
                out += NovelRichTextSegment(text = "\n", style = inheritedStyle, linkUrl = inheritedLink)
                return
            }
            if (tag == "img") return

            val styled = applyRichStyleTagAndInlineCss(
                base = inheritedStyle,
                tag = tag,
                inlineStyle = node.attr("style"),
            )
            val link = node.attr("href").trim().takeIf { tag == "a" && it.isNotBlank() } ?: inheritedLink
            node.childNodes().forEach { child ->
                parseInlineNode(child, styled, link, out)
            }
        }
    }
}

private fun applyRichStyleTagAndInlineCss(
    base: NovelRichTextStyle,
    tag: String,
    inlineStyle: String,
): NovelRichTextStyle {
    var style = when (tag) {
        "b", "strong" -> base.copy(bold = true)
        "i", "em" -> base.copy(italic = true)
        "u" -> base.copy(underline = true)
        "s", "strike", "del" -> base.copy(strikeThrough = true)
        else -> base
    }

    if (inlineStyle.isBlank()) return style
    parseInlineCssMap(inlineStyle).forEach { (key, value) ->
        when (key) {
            "color" -> style = style.copy(colorCss = value)
            "background", "background-color" -> style = style.copy(backgroundColorCss = value)
            "font-weight" -> if (value == "bold" || value.toIntOrNull()?.let { it >= 600 } == true) {
                style = style.copy(bold = true)
            }
            "font-style" -> if (value == "italic" || value == "oblique") {
                style = style.copy(italic = true)
            }
            "text-decoration" -> {
                val decorations = value.split(' ').map { it.trim() }
                if ("underline" in decorations) style = style.copy(underline = true)
                if ("line-through" in decorations) style = style.copy(strikeThrough = true)
            }
        }
    }

    return style
}

private fun parseInlineCssMap(raw: String): Map<String, String> {
    return raw.split(';')
        .mapNotNull { entry ->
            val idx = entry.indexOf(':')
            if (idx <= 0) return@mapNotNull null
            val key = entry.substring(0, idx).trim().lowercase(Locale.US)
            val value = entry.substring(idx + 1).trim()
            if (key.isBlank() || value.isBlank()) null else key to value
        }
        .toMap()
}

private fun mergeAdjacentRichSegments(segments: List<NovelRichTextSegment>): List<NovelRichTextSegment> {
    if (segments.isEmpty()) return emptyList()
    val merged = ArrayList<NovelRichTextSegment>(segments.size)
    segments.forEach { segment ->
        val last = merged.lastOrNull()
        if (last != null && last.style == segment.style && last.linkUrl == segment.linkUrl) {
            merged[merged.lastIndex] = last.copy(text = last.text + segment.text)
        } else {
            merged += segment
        }
    }
    return merged
}

private fun detectUnsupportedRichFeatures(doc: Element): Boolean {
    if (doc.select("table, iframe, svg").isNotEmpty()) return true

    return doc.select("[style]").any { element ->
        val inlineStyle = element.attr("style").lowercase(Locale.US)
        inlineStyle.contains("position:") ||
            inlineStyle.contains("display:flex") ||
            inlineStyle.contains("display: grid") ||
            inlineStyle.contains("display:grid") ||
            inlineStyle.contains("float:")
    }
}

private val richContainerBlockTags = setOf(
    "p",
    "div",
    "article",
    "section",
    "main",
    "h1",
    "h2",
    "h3",
    "h4",
    "h5",
    "h6",
    "blockquote",
    "hr",
)
