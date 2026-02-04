# JScience Benchmark & Visualization Task

## Status
- **Phase 1: Standardization** - IN PROGRESS
- **Phase 2: JScience Studio (UI)** - PLANNED
- **Phase 3: Systematic Provider Testing** - PLANNED
- **Phase 4: Reporting & Export** - PLANNED

## Detailed Steps
1.  **Refactor Benchmarks to RunnableBenchmark**
    - [ ] Update `FFTBenchmark` to implement `RunnableBenchmark`.
    - [ ] Update `MatrixBenchmark` to implement `RunnableBenchmark`.
    - [ ] Update `MonteCarloBenchmark` to implement `RunnableBenchmark`.
    - [ ] Ensure all are registered in `META-INF/services`.
2.  **Enhance Provider Selection**
    - [ ] Create a `ProviderDiscovery` utility to list all `FFTProvider`, `LinearAlgebraProvider`, etc.
    - [ ] Update benchmarks to run against *all* discovered Providers and external libraries.
3.  **Build JScience Studio (JavaFX)**
    - [ ] Update `jscience-benchmarks/pom.xml` with JavaFX dependencies.
    - [ ] Create `JScienceStudioApp.java`.
    - [ ] Implement Dashboard, Runner, and Chart views.
4.  **Reporting**
    - [ ] Export results to `benchmarks.md` with beautiful Markdown formatting.
    - [ ] Save charts as transparent PNGs for reports.

## Current Findings
- Multiple FFT providers exist (`Multicore`, `NativeMulticore`).
- Matrix multiplication benchmarks comparison: EJML (52ms) > Colt (160ms) > JBlas (170ms) > Commons Math (317ms).
- Need to fix `RealDoubleMatrix` runtime issues to include JScience Core in the comparison.
