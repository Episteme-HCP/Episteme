# Episteme – Modern Scientific API (2025)

**Silvère Martin-Michiellot**  
Senior Computer Programmer  
<silvere.martin@gmail.com>

**Keywords**: Numerical computing, scientific computing, Java, science library, framework, high-precision.

## Abstract

This paper describes **Episteme**, a comprehensive general science API written in Java. Unlike traditional scientific libraries that focus solely on numerical recipes, Episteme provides a semantic framework for modeling scientific concepts—from Quantum Mechanics to Sociology. Our goal is to cover the full spectrum of scientific knowledge, distributed freely under open-source licenses. In this 2025 edition, we present the revitalized architecture, featuring arbitrary-precision arithmetic, hardware acceleration, and a unified domain model.

## Introduction

Scientific computing has historically been dominated by Fortran and C++. However, Java's evolution—offering rigorous type safety, object-oriented design, and now competitive performance via Just-In-Time (JIT) compilation and foreign memory access—makes it an ideal platform for complex scientific systems. Episteme leverages these capabilities to prioritize **correctness** and **maintainability** without sacrificing performance.

## Architecture Overview

The Episteme project (2025) is organized into three primary modules:

1. **`episteme-core`**: The mathematical engine.
    * **Arbitrary Precision**: Built on `org.episteme.mathematics.numbers.real.Real`, enabling calculations where precision is limited only by memory, not hardware floating-point constraints.
    * **Linear Algebra**: Generic Matrix and Vector implementations supporting field-agnostic operations.
    * **Hardware Acceleration**: Service-provider interfaces (`CPUDenseLinearAlgebraProvider`, `CUDADenseLinearAlgebraProvider`) allow offloading heavy computations to GPUs seamlessly.
    * **Structure**: Categories, Groups, Rings, Fields vector spaces.

2. **`episteme-natural`**: The domain application layer.
    * **Physics**: Classical Mechanics, Relativity, Quantum Physics, Thermodynamics.
    * **Chemistry**: Periodic Table, Molecular Dynamics, Stoichiometry.
    * **Biology**: DNA/RNA sequencing, Codon mapping.
    * **Sociology/Economics**: Emerging modules for soft science modeling.

3. **`episteme-benchmarks`**: A standardized suite to measure performance against naive implementations and other libraries.

## The Semantic Difference

Most scientific libraries are collections of functions (e.g., `fft(double[] data)`). Episteme is a collection of **concepts**.

* **Type Safety**: You cannot accidentally add `Mass` to `Length`. The `Quantity<T>` system ensures dimensional consistency at compile time.
* **Arbitrary Precision**: While legacy parts of the system still utilize primitive `double` for performance trade-offs, the core architecture is moving towards ubiquitous `Real` usage, allowing users to define the required precision context dynamically.
* **Object-Oriented**: A `Particle` in Episteme isn't just a position vector; it's an entity with Mass, Charge, Spin, and interactions defined by interfaces like `Relativistic` or `QuantumMechanical`.

## Key Features

### 1. High-Performance Linear Algebra across Platforms

Episteme automatically detects available acceleration hardware. For small matrices, it uses optimized CPU algorithms. For large simulations (like N-Body problems), it can dispatch operations to CUDA-enabled GPUs, transparent to the user.

### 2. Signal Processing

The unified `SignalProcessing` framework provides Fast Fourier Transforms (FFT), filtering (High-pass, Low-pass), and spectral analysis using both primitive double precision (for speed) and `Real` precision (for accuracy).

### 3. Data Externalization

Hardcoded scientific constants are a thing of the past. Episteme loads data (Isotopes, Planets, Codon Tables) from external JSON resources, making the library extensible without recompilation.

## Weaknesses & Roadmap

While powerful, the current version (2025) acknowledges certain limitations:

* **Precision Gap**: Some "natural" science modules still rely on 64-bit floating point math (`double`), creating a precision gap with the "core" math engine. Phase 10 of our roadmap targets a comprehensive conversion to `Real`.
* **Soft Sciences**: Modules for Sociology and Economics are currently in early modeling stages.
* **Documentation**: We are actively improving Javadoc coverage to assist new adopters.

## Conclusion

Episteme represents the modern approach to scientific software: Strongly typed, semantically rich, and hardware-aware. It bridges the gap between theoretical correctness and computational efficiency, providing a robust foundation for the next generation of scientific applications.

## References

* Martin-Michiellot, S. (2025). *Episteme Refactoring Roadmap*.
* Dautelle, J.M. (2006). *Javolution: Real-Time Java Library*.
* Open Source Contributors (2000-2025).


