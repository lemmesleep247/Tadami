# Design Document: Naruto Running Progress Bar (Download Queue)

**Date:** 2026-06-05  
**Topic:** Implementation of a custom loading progress bar with a running Naruto animation in the Download Queue.  
**Target File:** [`DownloadQueueItem.kt`](../../../app/src/main/java/eu/kanade/presentation/download/DownloadQueueItem.kt)  
**New Component File:** `app/src/main/java/eu/kanade/presentation/download/components/NarutoProgressBar.kt` (or embedded in `DownloadQueueItem.kt`)  
**Assets Added:**  
- `app/src/main/res/drawable/naruto_run.gif` (transparent GIF)
- `app/src/main/res/drawable/naruto_run.webp` (transparent WebP animation for better performance)

---

## 1. Overview

This design replaces the standard Compose `LinearProgressIndicator` in the [DownloadQueueItem](file:///d:/lnreader/Tadami/ranobe-aniyomi/.worktrees/ranobe-novel/app/src/main/java/eu/kanade/presentation/download/DownloadQueueItem.kt) component with a custom themed progress bar. A miniature animated Naruto runs along the progress bar, matching the progress of the active download.

---

## 2. Architecture & UI Layout

### 2.1 Component Boundary

The new component `NarutoProgressBar` will be integrated inside `DownloadQueueItem`. It takes the current progress fraction and download status:

```kotlin
@Composable
fun NarutoProgressBar(
    progress: Float,
    status: DownloadQueueUiModel.QueueStatus,
    modifier: Modifier = Modifier
)
```

### 2.2 Layout Structure

A `BoxWithConstraints` structure is used to allow Naruto to run above the progress line without clipping or scaling issues:

```
+--------------------------------------------------------------+  <- BoxWithConstraints (height = 28.dp)
|               ____                                           |
|              /    \   <- Naruto Runner (AsyncImage, size=20.dp)
|              \____/                                          |
|  [===========================>----------------------------]  |  <- Progress Track (height = 4.dp)
+--------------------------------------------------------------+
```

1. **Progress Line:** Placed at `Alignment.BottomCenter` of the `Box`. It is drawn as a thin line (height `4.dp`) with a background track.
2. **Naruto Runner:** Placed above the line. Its horizontal position is calculated as:
   $$\text{translationX} = \text{progress} \times (\text{maxWidth} - \text{runnerWidth})$$
   We apply this translation using `Modifier.graphicsLayer { translationX = ... }` to avoid triggering recompositions during progress updates.

---

## 3. Detailed Specifications

### 3.1 Animation and Loading via Coil

To load the animated asset `R.drawable.naruto_run` (either GIF or WebP), we use Coil's `AsyncImage` with an explicit `GifDecoder` / `ImageDecoder` configured:

```kotlin
val context = LocalContext.current
val imageLoader = remember(context) {
    ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
}

AsyncImage(
    model = R.drawable.naruto_run,
    imageLoader = imageLoader,
    contentDescription = null,
    modifier = Modifier
        .size(20.dp)
        .graphicsLayer {
            translationX = (progress * (maxWidth.toPx() - 20.dp.toPx()))
        }
)
```

### 3.2 State Management

| Download Status | Animation Behavior | Position |
|---|---|---|
| **DOWNLOADING** | Animation plays (running) | Moves smoothly with `progress` (0.0 $\rightarrow$ 1.0) |
| **QUEUED** | Animation paused on frame 0 | Position frozen at current progress (typically 0.0) |
| **DOWNLOADED** | Fade out / Hide | Position at 1.0 |
| **FAILED / IDLE** | Fade out / Hide | Hidden |

We wrap the runner image in an `AnimatedVisibility` block with a fade-in/fade-out transition to ensure smooth entry and exit of the character when downloading starts or finishes.

---

## 4. Performance & Resource Constraints

1. **Recomposition Safety:** All translation offsets must be applied in the draw/layout stage via `graphicsLayer`. No layout-changing modifiers (`offset`, `padding`) should be updated dynamically on progress ticks.
2. **Coil Caching:** The animated asset is cached in memory. Since the file size is very small, memory footprint is negligible.
3. **Low-power / E-ink Mode:** If the system is in E-ink mode (as per `EInkProfile` configurations in Tadami), we fall back to a static, standard `LinearProgressIndicator` to avoid battery drain and screen ghosting.

---

## 5. Verification Plan

- **`compileDebugKotlin`** — Verify there are no compilation errors in the presentation module.
- **`assembleDebug`** — Build the APK.
- **Smoke Tests:**
  - Verify that Naruto runs smoothly from left to right as the download progress increases.
  - Verify that pausing the download stops the animation.
  - Verify that complete/failed/idle states hide the runner gracefully.
