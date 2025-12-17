# Benchmark Comparison Report

This document presents a structured comparison between JScience and other popular Java linear algebra libraries.

## Setup

- **Hardware**: (User Hardware)
- **Java Version**: (User Java Version)
- **Libraries**:
  - **JScience**: Current Build
  - **Apache Commons Math**: 3.6.1
  - **EJML**: 0.43

## Benchmarks

### 1. Matrix Multiplication

Measures the average time (ms) to multiply two square matrices of size $N \times N$.

| Size (N) | JScience (ms) | Commons Math (ms) | EJML (ms) |
| :--- | :--- | :--- | :--- |
| 128 | *Run Benchmark* | *Run Benchmark* | *Run Benchmark* |
| 256 | *Run Benchmark* | *Run Benchmark* | *Run Benchmark* |
| 512 | *Run Benchmark* | *Run Benchmark* | *Run Benchmark* |

### 2. N-Body Simulation

Measures the time (us) per simulation step for $N$ bodies.

| Bodies (N) | JScience (us) | Commons Math (us)* | EJML (us)* |
| :--- | :--- | :--- | :--- |
| 100 | *Run Benchmark* | N/A | N/A |
| 1000 | *Run Benchmark* | N/A | N/A |

*\*Note: N-Body benchmark is specific to physics logic, so direct comparison might not be applicable unless reimplemented using other libraries' vector classes.*

## How to Run

Windows:

```bat
.\run-benchmarks.bat
```

Linux/Mac:

```bash
./run-benchmarks.sh
```
