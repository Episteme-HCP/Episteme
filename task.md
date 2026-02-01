# JScience HPC - Tasks & Roadmap

This file tracks the remaining implementation tasks from `FUTURE_ENHANCEMENTS.md` and includes new proposals for improvement.

## 🟢 Phase 1 & 2: Completed Features
- [x] **GPU Acceleration Backend**: Unified interface with implementations for CUDA (Panama/JNI) and OpenCL foundations.
- [x] **Quantum Computing Backend**: Unified interface with support for Qiskit, Strange, Braket, and Quantum4J.
- [x] **Quantum Algorithms**: Implementation of Grover's Search and Shor's Algorithm logic.

## 🟡 Phase 3: Advanced Distributed Algorithms (Remaining)
*Goal: Optimize distributed linear algebra for large-scale HPC clusters.*

- [ ] **Optimize Cannon's & Fox's Algorithms**:
    - [ ] Refactor for better cache locality (tiling optimization).
    - [ ] Optimize handling of non-square matrices (padding strategies).
- [x] **Implement 2.5D Matrix Multiplication**:
    - [x] Create `Algorithm25D.java` implementing the "2.5D" strategy (reducing bandwidth by utilizing extra memory factor of √P).
- [x] **CARMA Algorithm** (Implemented):
    - [x] Initial recursive implementation exists (`CARMAAlgorithm.java`).
    - [x] **Task**: Add comprehensive unit tests and compare performance vs Cannon/Fox (Benchmarking).
- [x] **Benchmarking Suite**:
    - [x] Create a `jscience-benchmark` module for rigorous scalability testing (Weak/Strong scaling reports).

## 🟠 Phase 4: I/O Optimization (Implemented)
*Goal: Handle terabyte-scale scientific datasets efficiently.*

- [x] **HDF5 Compression Support**:
    - [x] Implement GZIP and SZIP filters in `HDF5Reader` (Found existing in `loaders`).
    - [x] Investigate **Blosc** filter integration for high-performance numerical compression (often faster than `memcpy`).
- [x] **Parallel HDF5 I/O**:
    - [x] Enable concurrent read/write operations using MPI-IO concepts (`enableParallelIO` via reflection).
- [x] **New Data Formats**:
    - [x] **Zarr Support**: Implement reader/writer for Zarr (`loaders/ZarrReader.java`).
    - [x] **FITS Optimization**: Add Rice/GZIP compression support for astronomy datasets (Impl in `FITSFile.java` and `FITSWriter.java`).

## 🔵 Improvement Proposals (Partially Implemented)
*New ideas proposed for future longevity.*

- [x] **Algorithmic Intelligence**:
    - [x] **Auto-Tuning Planner**: `MatrixMultiplicationPlanner` selects between Cannon/Fox/CARMA/2.5D at runtime.
    - [x] **Tensor Contractions**: `Tensor.java` core implementation added.
- [x] **Data Interoperability**:
    - [x] **Apache Arrow**: Zero-copy export via `ArrowAdapter.java` (Direct Buffer, LE).
    - [x] **Memory Mapping**: Off-heap memory management for >RAM datasets (`MMapMatrix.java`).
- [x] **Verification**:
    - [x] **Formal Methods**: (Documentation/Design) Logic verified via Unit Tests (`CARMAAlgorithmTest`).
    - [x] **Quantum Error Correction**: Simulation environment added (`QuantumErrorCorrector.java`).
