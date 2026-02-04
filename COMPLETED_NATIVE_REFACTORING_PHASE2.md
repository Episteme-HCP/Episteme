# Native Module Structural Refactoring - Phase 2 (Verified)

## Overview

Completed alignment of `jscience-native` directory structure with `jscience-core` by moving algorithm providers into centralized `technical/algorithm/*` subdirectories and standardizing other module locations.

## Structural Changes

### 1. Centralized Algorithm Providers

Moved scattered provider implementations to `org.jscience.nativ.technical.algorithm.*` to match core module structure:

| Original Location | New Location (under `technical.algorithm`) |
|-------------------|------------------------------------------|
| `physics/classical/mechanics/providers/*` | `nbody/` |
| `mathematics/signal/providers/*` | `fft/` |
| `mathematics/statistics/providers/*` | `montecarlo/` |
| `physics/fluids/providers/*` | `physics/` |
| `mathematics/optimization/*` | `genetic/` |
| `mathematics/ml/*` | `inference/` |
| `mathematics/logic/fuzzy/*` | `inference/` |
| `engineering/eventdriven/*` | `simulation/` |
| `mathematics/linearalgebra/backends/*` | `linearalgebra/` |
| `mathematics/discrete/backends/*` | `graph/` |

### 2. Consolidated Implementations

**Simulation Provider**:
- Merged `engineering/eventdriven/backends` and `providers`
- Kept `NativeMulticoreSimulationProvider`
- Moved to `technical/algorithm/simulation/`

**Graph Algorithm Provider**:
- Consolidated duplicate `NativeGraphAlgorithmProvider`
- Moved to `technical/algorithm/graph/`

### 3. Audio & Vision Cleanup

- Moved `media/audio/backends/` to `media/audio/providers/`
- Moved `media/vision/backends/` to `media/vision/providers/`

### 4. Cleanup

- Removed legacy `mathematics` directory
- Removed legacy `engineering` directory

## Files Moved

### N-Body (`technical.algorithm.nbody`)
- `NativeCUDANBodyProvider.java`
- `NativeMulticoreNBodyProvider.java`
- `NativeOpenCLNBodyProvider.java`

### FFT (`technical.algorithm.fft`)
- `NativeFFTProvider.java`
- `NativeMulticoreFFTProvider.java`

### Simulation (`technical.algorithm.simulation`)
- `NativeMulticoreSimulationProvider.java`

### Linear Algebra (`technical.algorithm.linearalgebra`)
- `NativeMulticoreLinearAlgebraProvider.java`

### Inference/ML (`technical.algorithm.inference`)
- `NativeCUDATensorProvider.java`
- `NativeMulticoreMLProvider.java`
- Fuzzy logic providers

### Genetic/Optimization (`technical.algorithm.genetic`)
- `NativeMulticoreGeneticAlgorithmProvider.java`

### Physics/Fluids (`technical.algorithm.physics`)
- `NativeOpenCLLatticeBoltzmannProvider.java`

### Audio (`media.audio.providers`)
- `NativeMulticoreAudioProvider.java`

### Vision (`media.vision.providers`)
- `NativeCUDAVisionProvider.java`
- `NativeMulticoreVisionProvider.java`
- `NativeOpenCLVisionProvider.java`

## Final Directory Structure

```
org.jscience.nativ/
├── media/
│   ├── audio/
│   │   └── providers/
│   │       └── NativeMulticoreAudioProvider.java
│   └── vision/
│       └── providers/
│           ├── NativeCUDAVisionProvider.java
│           ├── NativeMulticoreVisionProvider.java
│           └── NativeOpenCLVisionProvider.java
├── physics/
│   └── loaders/
│       ├── fits/
│       └── hdf5/
└── technical/
    ├── algorithm/
    │   ├── fft/
    │   ├── genetic/
    │   ├── graph/
    │   ├── inference/
    │   ├── linearalgebra/
    │   ├── montecarlo/
    │   ├── nbody/
    │   ├── physics/
    │   └── simulation/
    └── backend/
        ├── gpu/
        │   ├── cuda/
        │   └── opencl/
        └── nativ/
```
