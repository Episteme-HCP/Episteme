# Batch API Refactoring - Implementation Notes

## Task 1: AudioViewer/SpectrographViewer - FFT Provider Integration

### Current State
- Custom primitive DSP code
- Manual FFT implementation

### Target
- Use FFTProvider from jscience-core
- Matrix<Real> for frequency domain
- Convert to double[] for visualization

### Implementation
```java
// Replace custom FFT with:
import org.jscience.technical.backend.algorithms.FFTProvider;
import org.jscience.technical.backend.algorithms.MulticoreFFTProvider;

FFTProvider fftProvider = new MulticoreFFTProvider();
Matrix<Real> spectrum = fftProvider.compute(audioSamples);

// For rendering:
double[] renderData = new double[spectrum.rows()];
for (int i = 0; i < spectrum.rows(); i++) {
    renderData[i] = spectrum.get(i, 0).doubleValue();
}
```

## Task 2: MagneticFieldViewer - Maxwell Provider

### Current State
- Hardcoded field calculations
- Primitive double arrays

### Target
- Use MaxwellProvider for electromagnetic field simulation
- Real-time field integration

### Implementation  
```java
import org.jscience.technical.backend.algorithms.MaxwellProvider;

MaxwellProvider maxwell = new MulticoreMaxwellProvider();
Vector<Real> fieldVector = maxwell.computeField(position, current);

// Extract for rendering
double fx = fieldVector.get(0).doubleValue();
double fy = fieldVector.get(1).doubleValue();
double fz = fieldVector.get(2).doubleValue();
```

## Task 3: CircuitSimulatorViewer - Matrix Solver

### Current State
- Custom SPICE-like solver
- Primitive arrays for node voltages

### Target
- Matrix<Real> for circuit equations (KCL/KVL)
- Linear solver for node voltages

### Implementation
```java
// Build circuit matrix A and vector b
Matrix<Real> A = buildCircuitMatrix(components);
Vector<Real> b = buildSourceVector(components);

// Solve Ax = b for node voltages
Vector<Real> nodeVoltages = A.solve(b);
```

## Task 4: LotkaVolterraViewer - Real ODE Integration

### Current State
- Primitive double for populations
- Euler integration

### Target
- Real for precision
- Proper ODE solver

### Implementation
```java
import org.jscience.mathematics.numbers.real.Real;

private Real preyPopulation = Real.of(100.0);
private Real predatorPopulation = Real.of(20.0);

// ODE integration with Real
Real dPrey = (alpha.multiply(prey).subtract(beta.multiply(prey).multiply(predator))).multiply(dt);
Real dPredator = (delta.multiply(prey).multiply(predator).subtract(gamma.multiply(predator))).multiply(dt);

preyPopulation = preyPopulation.add(dPrey);
predatorPopulation = predatorPopulation.add(dPredator);
```

## Summary
All tasks follow the same pattern:
1. Import core API (Provider, Real, Matrix, Vector)
2. Use API for computation
3. Extract primitives ONLY for final rendering
4. Maintain state in Real/Matrix internally
