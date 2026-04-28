# Contributing to Episteme

Thank you for your interest in contributing to Episteme! As a high-performance scientific computing framework, we value contributions that improve numerical accuracy, performance, or developer experience.

## Ways to Contribute

1.  **Bug Reports**: If you find a numerical discrepancy or a crash, please open an issue with a minimal reproducible example.
2.  **Performance Improvements**: We are always looking for optimizations in our linear algebra backends (FFM, CUDA, OpenCL).
3.  **Documentation**: Help us improve our guides and API references.
4.  **New Backends**: Implementing new providers for the linear algebra SPI.

## Development Setup

### Prerequisites
*   **JDK 21 & 25**: We use a mixed-JDK approach. Core modules target JDK 21, while performance-critical native bridges target JDK 25.
*   **Maven 3.9+**: Required for the build system.
*   **Native Libraries**: To run native tests, you will need MPFR, OpenBLAS, and optionally CUDA/OpenCL. See [NATIVE_LIBS_SETUP.md](NATIVE_LIBS_SETUP.md) for details.

### Building the Project
```bash
mvn install -DskipTests
```
*Note: Full tests require specific native environments and are often skipped in standard CI.*

## Coding Standards

*   **Numerical Parity**: Any new algorithm must be validated against the `LinearAlgebraComplianceTest` suite to ensure parity between CPU and native backends.
*   **Performance Audits**: Substantial changes to the core engine should be accompanied by a benchmark report using the JMH-based benchmarking suite in `episteme-benchmarks`.
*   **Documentation**: All new public APIs must include Javadoc, especially noting precision and complexity characteristics.

## Pull Request Process

1.  Create a feature branch from `main`.
2.  Ensure your code compiles with both JDK 21 and 25.
3.  Verify that your changes do not break the "Mixed-JDK" build architecture.
4.  Submit a PR with a detailed description of the changes and any performance impact.

## License

By contributing to Episteme, you agree that your contributions will be licensed under the project's open-source license.
