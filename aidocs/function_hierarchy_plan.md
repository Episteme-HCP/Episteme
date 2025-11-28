# Function Hierarchy & Package Reorganization Plan

## 1. Function Hierarchy (Extending Function.java)

### Current State
- `Function.java` exists in `org.jscience.mathematics.analysis`
- `MathematicalFunction<D, C>` extends `java.util.function.Function`
- No connection between them

### Proposed Hierarchy
```
Function (existing JScience v1 interface)
  ↓
MathematicalFunction<D, C> (refactor to extend Function)
  ↓
├─ RealFunction (ℝ → ℝ)
├─ ComplexFunction (ℂ → ℂ)
├─ VectorFunction (ℝⁿ → ℝᵐ)
├─ Sequence<T> (ℕ → T)
└─ Series<T> (partial sums, infinite series)
```

### GPU/CPU Acceleration Strategy
```java
interface ComputeBackend {
    double[] evaluate(double[] inputs);
    boolean supportsGPU();
}

interface AcceleratedFunction extends Function {
    ComputeBackend getBackend();
    void setBackend(ComputeBackend backend); // CPU or GPU
}
```

**GPU Libraries:**
- **JOCL** (Java OpenCL) - industry standard
- **Aparapi** - automatic Java → GPU kernel translation
- **JCuda** - CUDA support

---

## 2. Package Reorganization (v1 → v2 Mapping)

### V1 Package Structure
```
org.jscience.mathematics/
  ├─ algebraic/           # Algebraic structures
  │   ├─ numbers/         # Complex, Real, Rational
  │   └─ vectors/         # Vector, Matrix
  ├─ analysis/            # Calculus, functions
  │   ├─ chaos/           # Fractals, dynamical systems
  │   ├─ series/          # Sequences, infinite series
  │   └─ (functions)      # Function, RealFunction
  ├─ geometry/            # Points, lines, shapes
  ├─ logic/               # (not in v1)
  ├─ number/              # (was algebraic/numbers)
  └─ wavelet/             # Signal processing
```

### V2 Current Structure
```
org.jscience.mathematics/
  ├─ algebra/             # ✅ Abstract algebra (done)
  │   ├─ algebras/        # Magma, KleeneAlgebra, etc.
  │   ├─ categories/      # Category theory
  │   ├─ group/           # CyclicGroup
  │   ├─ ring/            # PolynomialRing
  │   └─ space/           # HilbertSpace, BanachSpace
  ├─ analysis/            # ✅ Calculus (partial)
  │   ├─ Function.java
  │   ├─ RealFunction.java
  │   ├─ Differentiation.java
  │   └─ Integration.java
  ├─ discrete/            # ✅ NEW (graphs, combinatorics)
  ├─ dynamical/           # ✅ NEW (Mandelbrot, Julia)
  ├─ geometry/            # ✅ NEW (Point2D, Vector2D, Line2D, 3D)
  ├─ logic/               # ✅ NEW (Boolean, Fuzzy, Temporal, Modal)
  ├─ number/              # ✅ Core number types
  ├─ numbertheory/        # ✅ NEW (Primes)
  ├─ sequences/           # ✅ NEW (Fibonacci, Catalan, OEIS)
  ├─ signal/              # ✅ FFT, Wavelets
  └─ vector/              # Matrix, Vector
```

### Reorganization Needed

**Move to match v1:**
1. `sequences` → `analysis/series` (v1 had series here)
2. `dynamical` → `analysis/chaos` (v1 location)
3. Keep `discrete`, `logic`, `numbertheory` (new modules, no v1 equivalent)

**Final V2 Structure:**
```
org.jscience.mathematics/
  ├─ algebra/             # Abstract algebra
  ├─ analysis/            # Calculus & functions
  │   ├─ chaos/           # ← Move dynamical here
  │   ├─ series/          # ← Move sequences here
  │   ├─ Function.java
  │   ├─ RealFunction.java
  │   ├─ Differentiation.java
  │   └─ Integration.java
  ├─ discrete/            # Graph theory, combinatorics
  ├─ geometry/            # Euclidean geometry
  ├─ logic/               # Logic systems
  ├─ number/              # Core numbers
  ├─ numbertheory/        # Primes, modular arithmetic
  ├─ signal/              # FFT, wavelets
  └─ vector/              # Matrix, Vector
```

---

## 3. Implementation Steps

### Phase 1: Function Hierarchy
1. ✅ Check existing `Function.java`
2. Refactor `MathematicalFunction` to extend `Function`
3. Create `AcceleratedFunction` interface
4. Implement CPU backend (default)
5. Add GPU backend (JOCL/Aparapi)

### Phase 2: Package Reorganization
1. Move `sequences/*` → `analysis/series/*`
2. Move `dynamical/*` → `analysis/chaos/*`
3. Update all imports
4. Update documentation

### Phase 3: GPU Acceleration
1. Add JOCL dependency to `pom.xml`
2. Create `ComputeBackend` interface
3. Implement `CPUBackend` (default)
4. Implement `GPUBackend` (JOCL)
5. Add backend selection to functions

---

## 4. GPU Acceleration Design

### Maven Dependencies
```xml
<dependency>
    <groupId>org.jocl</groupId>
    <artifactId>jocl</artifactId>
    <version>2.0.4</version>
</dependency>
```

### Interface Design
```java
package org.jscience.mathematics.analysis.acceleration;

public interface ComputeBackend {
    String getName();
    boolean isAvailable();
    
    // Batch evaluation
    double[] evaluate(Function f, double[] inputs);
    
    // Matrix operations
    double[][] matrixMultiply(double[][] a, double[][] b);
}

public class CPUBackend implements ComputeBackend {
    // Standard Java implementation
}

public class GPUBackend implements ComputeBackend {
    // JOCL OpenCL implementation
    // Automatic kernel generation
}
```

### Usage
```java
RealFunction sin = RealFunction.sin();
sin.setBackend(new GPUBackend()); // Use GPU

double[] x = new double[1000000];
double[] y = sin.evaluate(x); // Computed on GPU
```

---

## 5. Benefits

✅ **Unified hierarchy** - MathematicalFunction extends existing Function
✅ **V1 compatibility** - Package structure matches v1
✅ **Performance** - GPU acceleration for batch operations
✅ **Flexibility** - Can switch CPU/GPU at runtime
✅ **Clean separation** - Backend abstraction isolates GPU code

---

## 6. Migration Checklist

- [ ] Analyze existing Function.java interface
- [ ] Refactor MathematicalFunction to extend Function
- [ ] Move sequences → analysis/series
- [ ] Move dynamical → analysis/chaos  
- [ ] Create ComputeBackend interface
- [ ] Implement CPUBackend
- [ ] Add JOCL dependency
- [ ] Implement GPUBackend
- [ ] Add benchmarks (CPU vs GPU)
- [ ] Update all documentation
