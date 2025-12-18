# JScience Roadmap

## Project Status

**Current Version**: 1.0.0-SNAPSHOT
**Focus**: Cleanup, Stabilization, and Performance Benchmarking

## Completed Tasks (Done)

- [x] **Project Structure**: Maven-based architecture with modular package design (`org.jscience.*`).
- [x] **Core Mathematics**:
  - Implementation of `Field`, `Ring`, `Vector`, `Matrix` interfaces.
  - Basic linear algebra support (Dense/Sparse vectors).
- [x] **Physics Modules**:
  - Relativity (Lorentz factor, Time dilation, etc.).
  - Astrophysics (Celestial bodies basics).
  - Classical Mechanics (Kinematics basics).
- [x] **Compute Context**: logic for switching between CPU/GPU backends (Java CPU, CUDA, OpenCL).
- [x] **Benchmarking Infrastructure**: JMH setup with reports (`BENCHMARK_REPORT.md`).
- [x] **Build System**: Modern Java 21+ configuration.

## Active Tasks (In Progress - To Do Now)

- [ ] **Code Cleanup & Quality**:
  - Remove deprecated concepts (e.g., Relativistic Mass, `finalize()` methods).
  - Resolve `UnsupportedOperationException` in symbolic math and generic vector operations.
  - Fix all compiler warnings.
- [ ] **Documentation**:
  - Complete Javadoc for all classes.
  - Add `package-info.java` to all packages.
  - Standardize file headers.
- [ ] **Testing**:
  - Increase unit test coverage.
  - Add integration tests.
- [ ] **Benchmarking**:
  - Run and validate benchmarks.

## Future Features (Suggested to Go)

- [ ] **Advanced Symbolic Math**:
  - Full implementation of Integration and Differentiation engines.
  - Polynomial factorization and simplification rules.
- [ ] **High-Performance Computing**:
  - production-ready OpenCL and CUDA backends (currently experimental/partial).
  - Native BLAS integration (`NATIVE_BLAS` backend).
  - Tensor operations via ND4J or custom engines.
- [ ] **Machine Learning Support**:
  - Basic neural network primitives using the Tensor library.
- [ ] **Visualization**:
  - JavaFX-based plotting and simulation views (3D/2D).

## Known Issues / Bottlenecks

- **Symbolic Math**: Many methods throw `UnsupportedOperationException` (Integration, Division of functions).
- **GPU Backends**: Hardcoded paths or fragility in `CudaStorage` (deprecated `finalize`).
- **Performance**: Fallback to CPU in many generic cases where GPU could be used.
