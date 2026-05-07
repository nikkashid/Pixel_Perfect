# PixelPerfect AI 🚀
### Intent-Aware Visual QA for Modern Android Development

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.02-green.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/compose)
[![Gemini](https://img.shields.io/badge/AI-Gemini%202.0%20Flash-purple.svg?style=flat&logo=google-gemini)](https://aistudio.google.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

## 👁️ The Vision
PixelPerfect AI bridges the **"Logic-Visual Gap"** in enterprise development. Traditional QA methodologies confirm if code *runs*, but often ignore whether it honors the **Design Intent**. 

Our solution moves beyond brittle pixel-matching. By using **Gemini 2.0 Flash**, PixelPerfect AI distinguishes between negligible technical artifacts (anti-aliasing) and genuine UX regressions, delivering actionable, context-aware feedback directly to the developer.

---

## ✨ Key Features
- **Live Figma Sync**: Real-time asset retrieval via Figma REST API—your design file is the live Single Source of Truth (SSoT).
- **Intent-Aware Analysis**: Powered by Gemini 2.0, providing semantic understanding of design vs. implementation.
- **Interactive Scan Workflow**: High-tech "Laser Scan" animation providing real-time visual feedback during audits.
- **Clean Architecture**: Built using a strict **MVVM + UseCase** pattern for scalability and testability.
- **Master Fallback System**: Robust three-level logic ensures the demo always works (Live Fetch -> Local Reference -> General Audit).

---

## 🛠️ Technical Stack
- **Language**: Kotlin 2.1
- **UI Framework**: Jetpack Compose (Material 3)
- **AI Engine**: Google Generative AI SDK (Gemini 2.0 Flash)
- **Networking**: OkHttp 4.12 + Kotlinx Serialization
- **Architecture**: MVVM + Clean Architecture (Domain-driven)
- **Secrets Management**: Maps Platform Secrets Gradle Plugin

---

## 🚀 Quick Start

### 1. Setup API Keys
Add your credentials to your `local.properties` file (Git-ignored):
```properties
GEMINI_API_KEY=your_gemini_key
FIGMA_TOKEN=your_figma_token
```

### 2. Run the Demo
1. Build and run the app on an Android device (API 24+).
2. Open the **Settings Screen**.
3. Click the **Eye Icon** in the top-right corner.
4. Watch the **Laser Scan** animation as PixelPerfect AI audits your UI.

---

## 📂 Project Structure
```text
com.nikkashid.pixelperfect
├── data                # Data Models (VisualIntent, AnalysisResult)
├── domain              # Business Logic
│   └── usecase         # UseCases (AnalyzeVisualQA, GetSettings)
├── qa                  # Infrastructure Services (Figma, AI, Capture)
└── ui                  # Presentation Layer
    ├── theme           # Design System (Color, Constants, Theme)
    └── settings        # Settings Feature (ViewModel, Screen)
```

---

> "PixelPerfect AI isn't just a testing tool; it's a fundamental shift toward design-aligned engineering."
