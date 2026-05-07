# PixelPerfect AI - Hackathon Implementation Guide

## 1. Core Vision & Problem Statement
PixelPerfect AI solves the **"Logic-Visual Gap"** in modern development. While traditional tests confirm that code *runs*, they fail to verify if the code honors the **Design Intent**. 

Our solution moves the QA paradigm from brittle pixel-matching to **Intent-Aware Visual Assurance** using Multi-modal LLMs.

---

## 2. Technical Architecture

### A. Android Client (SDK)
- **Settings Dashboard**: A high-fidelity replication of the app interface (Settings UI) with an integrated "Scan & Match" workflow.
- **Component Capture**: `CaptureUtils` captures real-time snapshots of the implementation.
- **Figma Connector**: `FigmaService` uses the Figma REST API to fetch live, high-resolution renders of the "Source of Truth" design.
- **URL Parser**: `FigmaUrlParser` extracts File Keys and Node IDs from standard Figma prototype/design links.

### B. AI Engine (Gemini 1.5 Pro)
- **Multi-modal Analysis**: Uses `model.generate_content([design, reality, prompt])` to compare the two images.
- **Intent-Aware Prompting**: Instructed to ignore "Technical Noise" (anti-aliasing, rendering shifts) and focus on UX discrepancies.
- **Actionable Feedback**: Classifies results as `MATCH`, `TECHNICAL_NOISE`, or `VISUAL_REGRESSION` and provides specific fix suggestions.

---

## 3. Key Features & Progress ✅

- [x] **"Scan & Match" Workflow**: Top-right Eye icon triggers the end-to-end analysis.
- [x] **Live Figma Fetching**: Integrated OkHttp networking to pull live renders from Figma Node IDs.
- [x] **AI Integration**: Full Google AI SDK (Gemini) integration with custom prompts.
- [x] **Expert QA Reporting**: Full-screen `VisualQAOverlay` with side-by-side comparison and detailed "Findings" card.
- [x] **Secrets Management**: Integrated `secrets-gradle-plugin` to protect API keys.

---

## 4. Developer Setup (Your Next Steps) 🛠️

### 1. Configure Secrets
Open `local.properties` and add your real tokens:
```properties
GEMINI_API_KEY=AIzaSy... (from Google AI Studio)
FIGMA_TOKEN=figd_... (from Figma Settings)
```

### 2. Verify Connectivity
- Ensure the device/emulator has **Internet access**.
- Check `AndroidManifest.xml` for `<uses-permission android:name="android.permission.INTERNET" />`.

### 3. Test the "Break-Fix" Flow
1. Change a visual property in `MainActivity.kt` (e.g., change a font weight or color).
2. Run the app and click the **Eye icon** (Top Right).
3. Observe the AI identifying the regression in the **Findings** report.

---

## 5. Presentation Strategy 🎤

### The "Hook"
Show the **Settings Screen**. It looks great, but is it perfect? Most developers would say "Yes," but PixelPerfect AI knows better.

### The "Magic"
1. Paste the Figma Link.
2. Click **Scan**.
3. Show the **Loading State**: "Fetching Figma design and analyzing with AI..."
4. Reveal the **Findings Report**: Point out how the AI caught the "Regular" font weight when the design called for "Bold," and how it suggested the exact fix.

### The "Value"
Explain that this removes the "Velocity Tax" of manual design reviews and eliminates "Ghost Regressions."
