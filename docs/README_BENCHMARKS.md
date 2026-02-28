# Episteme Benchmarking Suite

This module provides a comprehensive suite of benchmarks to evaluate the performance of Episteme algorithms and their external providers (Native, GPU, Distributed).

For detailed performance comparisons with other libraries (Commons Math, EJML, Colt, JBlas), see [BENCHMARK_COMPARISON.md](../docs/BENCHMARK_COMPARISON.md).

## Covered Domains

The following benchmarks are implemented and systematic (they test all discovered providers):
- **Linear Algebra**: Matrix operations (Dense/Sparse/Complex), Solvers.
- **Signal Processing**: Fast Fourier Transform (FFT).
- **Physics Simulation**:
  - **Finite Element Method (FEM)**: Poisson equation solver on 2D mesh.
  - **Fluid Dynamics**: Lattice Boltzmann Method (LBM).
  - **Electromagnetics**: Maxwell equations field tensor.
  - **Molecular Dynamics**: N-body simulation with Lennard-Jones potential.
  - **Quantum Computing**: Quantum Fourier Transform (QFT) circuit simulation.
- **Machine Learning**: K-Means clustering and PCA.
- **Distributed Computing**: Task latency simulation.
- **Computer Vision**: Image processing throughput.
- **Audio Processing**: Audio signal processing throughput.
- **N-Body Simulation**: Large scale particle simulation.
- **Geographic Information Systems (GIS)**: Map backend discovery.

## Running Benchmarks

### Command Line (CLI)
To run all benchmarks in CLI mode:
```bash
./run-benchmarks.bat --cli
```

### Graphical User Interface (JavaFX)
To launch the Benchmark Studio:
```bash
./run-benchmarks.bat
```

## Adding New Benchmarks

To add a new systematic benchmark:
1. Create a class implementing `SystematicBenchmark<YourProvider>`.
2. Annotate with `@AutoService(RunnableBenchmark.class)`.
3. Implement `setup()`, `run()`, and `teardown()`.
4. Ensure your provider interface extends `AlgorithmProvider`.

## Status
- **HDF5 I/O**: Benchmarks for native Read/Write (via Bytedeco HDF5) are implemented.
- **Vector API (SIMD)**: Implemented. Leverage JDK Vector instructions (AVX-512/NEON) for matrix operations via SIMDRealLinearAlgebraProvider.
- **Native Providers**: Supported via `episteme-native` module.
