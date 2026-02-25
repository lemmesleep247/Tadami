package eu.kanade.tachiyomi.ui.reader.novel

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NovelRichContentParserTest {

    @Test
    fun `rich paragraph model stores text and spans`() {
        val block = NovelRichContentBlock.Paragraph(
            segments = listOf(
                NovelRichTextSegment(
                    text = "Hello",
                    style = NovelRichTextStyle(bold = true),
                ),
            ),
        )

        (block as NovelRichContentBlock.Paragraph).segments.first().style.bold shouldBe true
    }

    @Test
    fun `parser extracts inline tags and links`() {
        val html = """
            <html><body>
            <p><strong>Bold</strong> <em>Italic</em> <a href="https://example.com">Link</a></p>
            </body></html>
        """.trimIndent()

        val result = parseNovelRichContent(html)

        result.blocks shouldHaveSize 1
        assertFalse(result.unsupportedFeaturesDetected)
        val paragraph = result.blocks.first() as NovelRichContentBlock.Paragraph
        paragraph.segments.map { it.text.trim() }.filter { it.isNotEmpty() } shouldBe listOf("Bold", "Italic", "Link")
        paragraph.segments[0].style.bold shouldBe true
        paragraph.segments[1].style.italic shouldBe true
        paragraph.segments[2].linkUrl shouldBe "https://example.com"
    }

    @Test
    fun `parser extracts headings blockquotes and images`() {
        val html = """
            <h2>Chapter Header</h2>
            <blockquote>Quote text</blockquote>
            <img src="https://example.com/image.jpg" alt="preview" />
        """.trimIndent()

        val result = parseNovelRichContent(html)

        result.blocks shouldHaveSize 3
        assertTrue(result.blocks[0] is NovelRichContentBlock.Heading)
        assertTrue(result.blocks[1] is NovelRichContentBlock.BlockQuote)
        assertTrue(result.blocks[2] is NovelRichContentBlock.Image)
    }

    @Test
    fun `parser keeps plugin image inside paragraph container`() {
        val html = """
            <p><img src="heximg://hexnovels?ref=test-image" alt="hex" /></p>
        """.trimIndent()

        val result = parseNovelRichContent(html)

        result.blocks shouldHaveSize 1
        val image = result.blocks.first() as NovelRichContentBlock.Image
        image.url shouldBe "heximg://hexnovels?ref=test-image"
        image.alt shouldBe "hex"
    }

    @Test
    fun `parser preserves block text alignment from inline style`() {
        val html = """
            <p style="text-align: center">Centered text</p>
        """.trimIndent()

        val result = parseNovelRichContent(html)

        val paragraph = result.blocks.first() as NovelRichContentBlock.Paragraph
        paragraph.textAlign shouldBe NovelRichBlockTextAlign.CENTER
    }

    @Test
    fun `parser flags unsupported structures for webview fallback`() {
        val html = """
            <table><tr><td>Complex layout</td></tr></table>
        """.trimIndent()

        val result = parseNovelRichContent(html)

        assertTrue(result.unsupportedFeaturesDetected)
    }
}
