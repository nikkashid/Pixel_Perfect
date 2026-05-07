# PixelPerfect AI - Hackathon Implementation Guide

## 1. Core Vision & Problem Statement
PixelPerfect AI solves the **"Logic-Visual Gap"** in modern development. Traditional QA verifies if code *runs*, but PixelPerfect AI verifies if the code honors the **Design Intent**. 

Our solution introduces **Intent-Aware Visual Assurance**, using Multi-modal LLMs to distinguish between negligible "Technical Noise" and critical "UX Regressions."

---

## 2. Technical Architecture

### A. Android Client (SDK)
- **High-Fidelity UI**: A professional Android Settings screen built with Jetpack Compose.
- **Scan & Match Workflow**: Integrated "Eye" icon in the Top Bar to trigger real-time analysis.
- **Laser Scan Animation**: A visual horizontal beam that scans the UI, providing a high-tech "AI feel."
- **Capture Utility**: `CaptureUtils` takes real-time, high-resolution snapshots of the phone screen.

### B. AI Engine (Gemini 2.0 Flash)
- **Cutting-Edge Intelligence**: Powered by **Gemini 2.0 Flash**, the latest multimodal model from Google, capable of sub-pixel visual reasoning.
- **Intent-Aware Prompting**: Instructions ensure the AI ignores rendering shifts and focuses on meaningful design violations.
- **Clean Reporting**: Automatically cleans markdown formatting for a professional, plain-text developer report.

---

## 3. Key Features & Progress ✅

- [x] **Settings Implementation**: Full Material 3 screen matching Figma design.
- [x] **Live AI Analysis**: 100% integrated with Google Generative AI SDK.
- [x] **Triple-Check Strategy**: 
    - **Level 1**: Live Figma Fetch via REST API.
    - **Level 2**: Local PNG Fallback (`figma_reference.png`).
    - **Level 3**: General AI UI Audit (Expert Mode).
- [x] **Expert QA Reporting**: Clean, iOS/Android hybrid "Findings" overlay with actionable fix suggestions.

---

## 4. Presentation Script (Team Daredevil) 🎤

- **The Hook**: "Our UI looks perfect to the human eye, but let's see what Gemini 2.0 thinks."
- **The Scan**: Click the icon. "The laser is scanning the implementation and comparing it live against our Figma design intent."
- **The Reveal**: Show the report. "The AI caught the lowercase 'p' in 'Edit profile' and the missing disclosure arrows. It even recognized that our icons are 'solid' while the design intent was 'outline'."
- **The Value**: "This isn't just pixel-diffing; it's **Design Intelligence** integrated directly into the developer workflow."
