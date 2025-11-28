# Architectural Analysis: v1 vs v2 Comparison

## 1. Package Structure: `number` vs `algebras`

### Current: `org.jscience.mathematics.number`
```
number/
  - Real.java
  - Complex.java
  - Integer.java
  - Rational.java
  - Natural.java
```

### Alternative: `org.jscience.mathematics.algebra.numbers`

### Pros of current (`number` separate):
✅ **Semantically clear** - Numbers are fundamental, not just algebraic structures
✅ **Independence** - Numbers can be used without algebra package
✅ **Intuit

ive imports** - `import org.jscience.mathematics.number.Real`
✅ **Parallel to Java** - `java.lang.Number` is separate from collections/utils

### Cons of current:
❌ **Algebraic disconnect** - These types implement `Field`, `Ring` (algebraic concepts)
❌ **Duplicate concepts** - Real is both a number and a Field

### Pros of under `algebras`:
✅ **Type hierarchy clarity** - Emphasizes that Real implements Field<Real>
✅ **Category theory alignment** - Numbers as instances of algebraic categories

### Cons of under `algebras`:
❌ **Over-categorization** - Not all number usage is algebraic
❌ **Verbose imports** - `algebra.numbers.Real` feels redundant

### **RECOMMENDATION: KEEP CURRENT**
Numbers are **fundamental mathematical objects** that *happen to form* algebraic structures. 
Like `java.lang.Number` is separate from `java.util`, our numbers should be independent.

---

## 2. Chaos/Dynamical Systems Comparison

### V1 Had (`analysis.chaos`):
```java
MandelbrotSet.java     - Set membership + escape time
MandelbrotMap.java     - z → z² + c mapping
JuliaSet.java          - Julia set
HenonMap.java          - Henon attractor (discrete 2D)
LogisticMap.java       - 1D chaotic map
CatMap.java            - Arnold's cat map (torus)
StandardMap.java       - Chirikov standard map
GingerbreadManMap.java - Fractal attractor
KochCurve.java         - Fractal curve
CantorDust.java        - Cantor set fractal
```

### V2 Has (`dynamical`):
```java
MandelbrotSet.java     - Set<Complex> implementation ✅
JuliaSet.java          - Set<Complex> implementation ✅
```

### **MISSING FROM V2:**
❌ **Discrete maps** (HenonMap, LogisticMap, CatMap, StandardMap)
❌ **Fractal curves** (KochCurve, CantorDust, GingerbreadMan)
❌ **Map interface** (`PrimitiveMappingND` - N-dimensional mappings)

### V1 Architecture (BETTER):
- **Separation**: Maps vs Sets
- **Interface**: `PrimitiveMappingND` with `map()`, `iterate()`, `hausdorffDimension()`
- **Optimization**: Direct `double[]` for performance (no Object overhead)

### V2 Architecture (BETTER):
- **Type safety**: `Set<Complex>` properly typed
- **Modern**: Uses our number system

### **BEST OF BOTH WORLDS:**
```java
// Interface for dynamical maps
interface DynamicalMap<T> {
    T map(T state);
    T iterate(int n, T initial);
}

// Discrete maps for performance
interface DiscreteMap extends DynamicalMap<double[]> {
    int dimensions();
    double hausdorffDimension(); // Fractal dimension
}

// Keep: MandelbrotSet, JuliaSet as Set<Complex>
// ADD: HenonMap, LogisticMap, StandardMap, CatMap
// ADD: EscapeTimeFractal interface (generalize Mandelbrot/Julia)
```

---

## 3. Series Package Comparison

### V1 Had (`analysis.series`):
```java
AbstractSeries.java           - Base class
PrimitiveSeries.java          - Interface
NumberSeries.java             - Generic number series
PrimeSeries.java              - Prime number sequence + Sieve
FibonacciSeries.java          - Fibonacci sequence
FactorialSeries.java          - n!
HarmonicSeries.java           - 1/n (divergent)
LnSeries.java                 - Natural log series
IntegerSumSeries.java         - Σn
IntegerSumOfSquaresSeries.java- Σn²
PochhammerSeries.java         - Pochhammer symbol (rising factorial)
SumOfSeriesSeries.java        - Sum of series
FourierMathUtils.java         - FFT, DFT utilities (9KB!)
```

### V2 Has:
```java
// In numbertheory:
Primes.java                   - sieveOfEratosthenes(), isPrime() ✅

// In signal:
FastFourierTransform.java     - FFT implementation ✅
```

### **MISSING FROM V2:**
❌ **Series abstraction** - No `Series<T>` interface
❌ **Infinite series** - Fibonacci, Harmonic, etc.
❌ **Series operations** - sum, product, composition
❌ **Convergence testing** - `isConvergent()`, `getValue()`

### V1 Architecture (BETTER):
- **Series as sequences**: `getValueAtRank(int n)`
- **Convergence**: `isConvergent()`, `getValue()` for limit
- **Composition**: `SumOfSeriesSeries` - meta-series

### V2 Missing:
- No series abstraction at all!

### **BEST OF BOTH WORLDS:**
```java
package org.jscience.mathematics.sequences;

interface Sequence<T> {
    T get(int index);  // a_n
}

interface InfiniteSequence<T> extends Sequence<T> {
    boolean isConvergent();
    T limit();  // If convergent
}

// Implementations:
class FibonacciSequence implements InfiniteSequence<Integer>
class PrimeSequence implements InfiniteSequence<Integer>  
class HarmonicSeries implements InfiniteSequence<Rational>  // Divergent
class GeometricSeries<T extends Field<T>> implements InfiniteSequence<T>
```

---

## Summary of Improvements Needed

### 1. Package Structure
**Decision**: KEEP `number` separate from `algebras`

### 2. Add Missing Dynamical Systems
```
org.jscience.mathematics.dynamical/
  - DynamicalMap<T> interface
  - DiscreteMap interface (extends DynamicalMap<double[]>)
  - HenonMap implements DiscreteMap
  - LogisticMap implements DiscreteMap  
  - ArnoldCatMap implements DiscreteMap
  - StandardMap (Chirikov) implements DiscreteMap
  - EscapeTimeFractal interface
  - Refactor: Mandelbrot/Julia extend EscapeTimeFractal
```

### 3. Add Sequences Package
```
org.jscience.mathematics.sequences/
  - Sequence<T> interface
  - InfiniteSequence<T> interface
  - FibonacciSequence
  - PrimeSequence (integrate with Primes.java)
  - HarmonicSeries
  - GeometricSeries<T>
  - PowerSeries<T> (for Taylor/Maclaurin)
```

### 4. Enhancements
- Combine V1's performance (`double[]`) with V2's type safety (`Set<Complex>`)
- Preserve V1's `hausdorffDimension()` for fractals
- Add modern convergence tests for series (ratio test, root test, integral test)
