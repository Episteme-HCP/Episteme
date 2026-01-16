# JScience Refactoring Tasks: Creamy Theme Standardization

This file tracks the progress of refactoring JScience viewers and applications to use the new consistent "creamy" theme, replacing all legacy dark-themed CSS classes and hardcoded background styles.

## Status: ✅ COMPLETE

All major viewers and applications have been refactored to use the standardized theme classes.

## Completed Tasks

### CSS Framework

- [x] `theme.css`: Removed all legacy `dark-` prefixed label and header styles.
- [x] `theme.css`: Standardized `.viewer-root`, `.viewer-sidebar`, `.viewer-controls`, and `.status-bar` classes.
- [x] Added accent button classes: `.accent-button-blue`, `.accent-button-green`, `.accent-button-orange`.

### Featured Applications (`jscience-featured-apps`)

- [x] `FeaturedAppBase.java`: Refactored status bar styling.
- [x] `CivilizationApp.java`: Refactored controls and headers.
- [x] `PandemicForecasterApp.java`: Standardized UI classes.
- [x] `MarketCrashApp.java`: Standardized UI classes and alerts.
- [x] `SmartGridApp.java`: Refactored control room and visualization panels.
- [x] `TitrationApp.java`: Standardized laboratory and data panels.
- [x] `QuantumCircuitApp.java`: Refactored gate toolbar and results display.

### Natural Sciences Viewers (`jscience-natural`)

- [x] `MechanicsDemo.java`: Replaced hardcoded dark backgrounds.
- [x] `InterplanetaryTrajectoryDemo.java`: Standardized sidebar and buttons.
- [x] `RelativisticFlightDemo.java`: Refactored overlay controls.
- [x] `HumanBodyViewer.java`: Full refactor of 3D scene background and sidebars.
- [x] `SpectrographViewer.java`: Standardized root and label classes.
- [x] `BioMotionViewer.java`: Refactored styling and sidebar.
- [x] `StellarSkyViewer.java`: Refactored labels and controls (while keeping dark content area).
- [x] `RigidBodyViewer.java`: Standardized labels.
- [x] `SequenceAlignmentDemo.java`: Standardized headers, panels, and result cells.
- [x] `PhylogeneticTreeViewer.java`: Already using correct classes.
- [x] `GeneticAlgorithmDemo.java`: Already using correct classes.
- [x] `GeneticsViewer.java`: Already using correct classes.
- [x] `PeriodicTableViewer.java`: Already using correct classes.
- [x] `ChemicalReactionViewer.java`: Already using correct classes.
- [x] `LSystemViewer.java`: Already using correct classes.
- [x] `MagneticFieldViewer.java`: Refactored 3D scene and root styles.
- [x] `MolecularViewer.java`: Refactored sidebar styles.

### Core Mathematics Viewers (`jscience-core`)

- [x] `DistributionsViewer.java`: Standardized layout and headers.
- [x] `GeometryBoardViewer.java`: Refactored root, toolbar, and sidebar.
- [x] `MetamathViewer.java`: Refactored header, theorem label, and sidebar.
- [x] `CSGViewer.java`: Refactored 3D scene, sidebar, and title.
- [x] `NetworkViewer.java`: Refactored container style.
- [x] `FunctionExplorer2DViewer.java`: Refactored sidebar, button, and analysis labels.
- [x] `FunctionExplorer3DViewer.java`: Refactored sidebar, button, and chart container.

### Social Sciences Viewers (`jscience-social`)

- [x] `KurzweilDemo.java`: Standardized sidebar and root panels.
- [x] `MapViewer.java`: Refactored root style.
- [x] `CarTrafficViewer.java`: Refactored root, sidebar, and description labels.
- [x] `EconomicsMarketDemo.java`: Refactored controls panel.

## Intentionally Preserved Dark Styles

The following components intentionally use dark backgrounds to simulate real-world hardware or visualization requirements:

- **Device Viewers** (`VitalMonitorViewer`, `OscilloscopeViewer`, `PressureGaugeViewer`, `ThermometerViewer`): Simulating real medical/scientific instrument displays.
- **Sky Viewers** (`StellarSkyViewer`, `PlanetariumViewer`): Using `.content-dark` for astronomical visualization areas.
- **Periodic Table Cell Styles**: Individual element cells use category-based colors dynamically.

## Theme Class Reference

| Class | Purpose |
| :--- | :--- |
| `.viewer-root` | Main viewer background (creamy #fdfbf7) |
| `.viewer-sidebar` | Sidebar panels with subtle border |
| `.viewer-controls` | Control panels and toolbars |
| `.header-label` | Bold section headers |
| `.description-label` | Secondary descriptive text |
| `.info-panel` | Information boxes with padding |
| `.status-bar` | Application status bars |
| `.content-dark` | Dark areas for sky/space visualization |
| `.accent-button-blue` | Blue action buttons |
| `.accent-button-green` | Green action buttons |
| `.accent-button-orange` | Orange action buttons |
