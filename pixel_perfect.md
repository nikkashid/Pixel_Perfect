# PixelPerfect AI - Implementation Plan

## 1. Core Vision
Bridge the "logic-visual gap" by moving from simple pixel-diffing to AI-driven **Intent-Aware Visual QA**.

## 2. Technical Architecture

### A. Android Client (SDK)
- **Capture Module**: Utility to capture Composable states as high-resolution Bitmaps.
- **Metadata Tagging**: Attach component names and Figma node IDs to screenshots.
- **Reporting Interface**: Display AI feedback directly in the app's debug builds.

### B. AI Engine (Gemini 1.5 Pro)
- **Multi-modal Analysis**: Compares {Figma Render} vs {App Screenshot}.
- **Intent Filtering**: Distinguishes between 'Technical Noise' and 'UX Regressions'.
- **Actionable Insights**: Provides specific CSS/Compose property suggestions.

## 3. Completed Milestones ✅
- [x] **Project Scoping**: Defined "Intent-Aware" vs "Pixel-based" strategy.
- [x] **AI Integration**: Integrated Google AI SDK (Gemini) with multi-modal prompting.
- [x] **SDK Hook**: Created `.pixelPerfect()` modifier for component registration.
- [x] **Visual Reporting**: Built full-screen `VisualQAOverlay` for side-by-side comparison.

## 4. Remaining Hackathon Tasks 🚀

### Step 1: Real Component Capture (High Priority)
- Finish the `CaptureController` to extract 1:1 Bitmaps from Composables using `GraphicsLayer`.
- Ensure transparency is handled correctly for isolated component comparison.

### Step 2: Figma Live Sync
- Implement OAuth2/Token-based authentication for Figma.
- Connect `FigmaService` to fetch live renders of tagged Node IDs.

### Step 3: Prompt Refinement
- Fine-tune the Gemini prompt to handle Android-specific rendering quirks (e.g., elevation shadows).

### Step 4: PR Bot (Final Stretch)
- Create a script/action to post the `QAAnalysisResult` as a comment on GitHub Pull Requests.

## 5. Key Components
1. `FigmaService`: Figma API wrapper.
2. `IntentAnalyzer`: AI logic using Gemini 1.5 Pro.
3. `PixelPerfectModifier`: The developer entry point.
4. `VisualQAOverlay`: The user-facing reporting tool.
