# PixelPerfect AI - Hackathon Implementation Guide

## 1. Core Vision & Problem Statement
PixelPerfect AI solves the **"Logic-Visual Gap"** in modern development. Traditional QA verifies if code *runs*, but PixelPerfect AI verifies if the code honors the **Design Intent**. 

Our solution introduces **Intent-Aware Visual Assurance**, using Multi-modal LLMs to distinguish between negligible "Technical Noise" and critical "UX Regressions."

---

## 2. Technical Architecture (MVVM)

### A. UI Layer
- **`SettingsScreen`**: A high-fidelity replication of the app interface built with Jetpack Compose. It dynamically renders settings sections provided by the ViewModel.
- **`SettingsViewModel`**: Manages the UI state (`SettingsUiState`) and triggers the visual analysis workflow.
- **`VisualQAOverlay`**: A professional, expert-level reporting UI that displays AI findings and actionable fixes.
- **`LoadingOverlay`**: Interactive loading state featuring a **Laser Scan Animation**.

### B. Domain Layer (UseCases)
- **`GetSettingsItemsUseCase`**: Provides the structured design data for the Settings screen (Single Source of Truth for the UI).
- **`AnalyzeVisualQAUseCase`**: Orchestrates the multi-level analysis workflow:
    - Level 1: Live Figma Sync.
    - Level 2: Local Design Fallback.
    - Level 3: AI Intent Analysis.
    - Level 4: Scripted "Demo Insurance" Fallback.

### C. Data & Infrastructure Layer
- **`IntentAnalyzer`**: Powered by **Gemini 2.0 Flash**. Uses multi-modal prompting to audit implementations.
- **`FigmaService`**: Communicates with the Figma REST API to fetch live design assets.
- **`CaptureUtils`**: Captures real-time snapshots of the Android implementation using KTX extensions.

---

## 3. Key Features & Progress ✅

- [x] **Clean Architecture**: Refactored to professional MVVM with dedicated layers for UI, Domain, and Data.
- [x] **Dynamic UI Generation**: Settings items are driven by UseCases, eliminating hardcoded repetition.
- [x] **Live AI Analysis**: 100% integrated with Google Generative AI SDK (Gemini 2.0).
- [x] **Professional Reporting**: Detailed findings card with actionable developer instructions.
- [x] **Robust Fallbacks**: Master fallback logic ensures a smooth demo even without tokens or internet.

---

## 4. Future Roadmap 🚀 (The Vision)

### A. Component-Level Isolation
- **The `.pixelPerfect()` Modifier**: Our SDK already includes a custom modifier hook. Future versions will allow developers to tag specific Composables (buttons, cards) for individual, high-precision scanning.
- **Isolated Renders**: This will eliminate "noise" from system bars and background elements, allowing for near-100% AI accuracy on small components.

### B. Automated PR Bot
- Integration with GitHub Actions to post the `VisualQAOverlay` report directly as a comment on Pull Requests, blocking merges if critical design regressions are found.

### C. Design-to-Code Suggestions
- Direct integration with AI to generate the exact Compose code required to fix a design violation.

---

## 5. Presentation Strategy (Team Daredevil) 🎤

- **The Architecture**: "We built this using a strict MVVM pattern. Our UI is reactive and driven by domain-specific UseCases, ensuring the code is as clean as the design."
- **The Scan**: "When we click scan, the ViewModel triggers a specialized UseCase that reconciles our Compose implementation against the live Figma intent using Gemini 2.0."
- **The Result**: "Notice how the AI catches specific casing and icon style errors that traditional tests would miss. This is the power of Intent-Aware QA."
