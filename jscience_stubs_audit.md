# JScience — UnsupportedOperationException & Stubs Audit

Full project scan as of 2026-02-18.

## Summary

| Category | Count | Action |
| --- | --- | --- |
| **True stubs** (methods that should work but don't) | ~30 | Must implement |
| **Guard clauses** (lib-not-available checks) | ~40 | Correct — keep |
| **Legitimate UOE** (math/domain constraints) | ~20 | Correct — keep |

---

## 🔴 TRUE STUBS — Must Implement

### 1. ND4JLinearAlgebraProvider (14 stubs)
**File:** `jscience-core/.../providers/ND4JLinearAlgebraProvider.java`
All 14 methods throw `"Not implemented yet"`:
`add`, `subtract`, `multiply` (vector×scalar, matrix×matrix, matrix×vector), `dot`, `norm`, `inverse`, `determinant`, `solve`, `transpose`, `scale`

### 2. NativeMulticoreLinearAlgebraProvider (13 stubs)
**File:** `jscience-native/.../NativeMulticoreLinearAlgebraProvider.java`
All 13 LinearAlgebraProvider methods throw bare `UnsupportedOperationException()` (no message):
`add` (vector, matrix), `subtract` (vector, matrix), `multiply` (vector, matrix×matrix, matrix×vector), `dot`, `norm`, `inverse`, `determinant`, `solve`, `transpose`, `scale`

### 3. NativeTensor partial stubs (4 stubs)
**File:** `jscience-native/.../tensors/NativeTensor.java`
- `broadcast()` → "Broadcast not implemented for NativeTensor yet"
- `transpose()` → "Transpose not implemented for NativeTensor yet"
- `slice()` → "Slice not implemented for NativeTensor yet"
- `sum(axis)` → "Axis sum not implemented"

### 4. NativeFFMBLASBackend partial stubs (1 stub)
**File:** `jscience-native/.../blas/NativeFFMBLASBackend.java`
- `solve()` → "Not implemented yet in NativeBLASBackend"

### 5. SU2Group.inverse() (1 stub)
**File:** `jscience-core/.../groups/SU2Group.java`
- `inverse()` → "Matrix inversion not yet fully exposed"

### 6. OpenCLBackend (5 stubs)
**File:** `jscience-core/.../gpu/opencl/OpenCLBackend.java`
- `selectDevice()` → "Device selection not yet implemented"
- `matmul()` → "OpenCL matrix multiplication kernel needs to be implemented"
- `elementWise()` → "OpenCL element-wise operations require kernel loading"
- `fft()` → "FFT requires clFFT or custom kernels"
- `reduce()` → "Reduction not implemented"

### 7. OpenCLFFTProvider (8 stubs)
**File:** `jscience-core/.../gpu/opencl/OpenCLFFTProvider.java`
- 2D/3D FFT, inverse FFT, and Real variants (8 methods total)

### 8. NativeFFTProvider partial stubs (4 stubs)  
**File:** `jscience-native/.../fft/NativeFFTProvider.java`
- `fft2dReal`, `ifft2dReal`, `fft3dReal`, `ifft3dReal`

### 9. FunctionExpression (2 stubs)
**File:** `jscience-core/.../symbolic/FunctionExpression.java`
- `differentiate()` → "Differentiation not yet fully integrated with Ring interface"
- `integrate()` → "Integration not yet implemented"

### 10. Symbolic integration stubs (3 stubs)
- `ProductExpression.integrate()` → "Integration of products not yet implemented"
- `DivisionExpression.integrate()` → "Integration of quotients not yet implemented"
- `IntegralExpression.evaluate()` → "Numeric evaluation of symbolic integrals not supported"

---

## 🟡 GUARD CLAUSES — Correct Behavior (keep as-is)

These are runtime checks that throw when a native library or hardware is unavailable:

| File | Pattern |
| --- | --- |
| `NativeFFTProvider` | `if (!available) throw UOE("FFTW3 library not available")` |
| `NativeBulletBackend` | `if (!AVAILABLE) throw UOE("Bullet native library not found")` |
| `NativeHDF5Reader/Writer` | `if (!AVAILABLE) throw UOE("HDF5 native library not found")` |
| `NativeFITSReader/Writer` | `if (!AVAILABLE) throw UOE("cfitsio library not found")` |
| `NativeAudioBackend` | `if (!isAvailable()) throw UOE(...)` |
| `NativeVisionProvider` | `if (!available) throw UOE(...)` |
| `NativeCPULinearAlgebraBackend` | `if (!AVAILABLE) throw UOE("BLAS/LAPACK not found")` |
| `CUDABackend` | `if (cublasDgemm == null) throw UOE("CUBLAS not available")` |
| `CUDATensorProvider` | `if (!isAvailable()) throw UOE(...)` |
| `ND4JBaseTensorBackend` | `if (!isAvailable) throw UOE(...)` |
| `ONNXRuntimeBackend` | `if (!loaded) throw UOE(...)` |

---

## 🟢 LEGITIMATE UOE — Domain/Math Constraints (keep as-is)

| File | Reason |
| --- | --- |
| `CauchyDistribution.mean()` | Cauchy has no defined mean |
| `CauchyDistribution.variance()` | Cauchy has no defined variance |
| `BioSequence.complement()` | Only valid for DNA |
| `BioSequence.transcribe()` | Only DNA can be transcribed |
| `Isotope` | Requires Element reference |
| `QuantumGate.matrix()` | Gate has no matrix representation |
| `Integers.divideExact()` | Integer division constraints |
| `VectorFactory.of()` | Only supports Real.class currently |
| `JavaCVBackend.play()/pause()` | Vision backend, not audio |
| `DenseNeuralProvider` | Type guard for Float/Double only |
| `CUDATensor` | Type guard for Float/Double only |
| `DistributedNBodyProvider` | Only supports double precision |
| `FunctionCompiler` | Unsupported expression type |
| `Simplifier` | Requires ring context |
| `AudioEqualizer` | Unsupported eq type |

---

## Priority Order for Implementation

1. **ND4JLinearAlgebraProvider** — 14 stubs, high visibility
2. **NativeMulticoreLinearAlgebraProvider** — 13 stubs, critical for native perf
3. **OpenCLBackend + OpenCLFFTProvider** — 13 stubs, GPU acceleration
4. **NativeTensor operations** — 4 stubs, tensor completeness
5. **Symbolic math** — 5 stubs, CAS feature gap
6. **SU2Group.inverse** — 1 stub, group theory
7. **NativeFFTProvider real variants** — 4 stubs, FFT completeness
