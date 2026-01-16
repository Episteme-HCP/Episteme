# JScience Refactor Tasks

## 1. Architecture of Viewers & Demos
 
- [x] Implement automatic UI generation in `AbstractDemo` from `Viewer.getViewerParameters()`.
- [x] Move Min/Max/Step/Default values for parameters to `jscience.properties`.
- [x] Ensure all Demos are simple wrappers of Viewers.
- [x] Refactor `MandelbrotDemo` and others to be "wrapper-only".

## 2. Aesthetics & Themes (Done)
 
- [x] Remove dark blue panels (`.dark-viewer-sidebar`, `.dark-viewer-root`) from `theme.css`.
- [x] Standardize background colors to the default Cream/Orange theme.
- [x] Remove hardcoded styles (`-fx-background-color`, etc.) from Viewers (e.g., `StellarSkyViewer`).
- [x] Ensure all text remain legible (no white on light background or yellow on white).

## 3. Real vs Double Support
 
- [x] Convert `jscience-natural` data models to use `Real` (e.g., `Star`, `Coordinates`).
- [x] Convert `jscience-social` data models to use `Real`.
- [x] Enforce pattern: Input (double) -> Internal (Real) -> Output (double).
- [x] Replace unit-less doubles with `Quantities` where applicable.

## 4. Externalization (jscience.properties)
 
- [x] Move all hardcoded default values (fallback values) from Java code to `jscience.properties`.
- [x] Ensure every Viewer loads its default parameters via `Configuration.get(...)`.
- [x] Add all Viewer default parameters to `jscience.properties`.

## 5. IO Components (Fixing Now)
 
- [x] Review all Readers/Writers (except `jscience-old-v1`) for I18n support.
- [x] Ensure `getCategory()`, `getName()`, `getDescription()` are properly internationalized.
- [x] Add missing translation keys for the 5 supported languages (EN, FR, DE, ES, IT/ZH).

## 6. Internationalization (I18n) (Fixing Now)
 
- [x] Replace string concatenation in UI labels with templates or proper I18n keys.
- [x] Audit all `.setText("...")` and `new Label("...")` calls to ensure no hardcoded strings remain.
- [x] Verify translation coverage for all new UI components.
