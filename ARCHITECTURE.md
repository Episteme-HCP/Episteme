# JScience Reimagined - Architecture

**Version**: 2.0.0-SNAPSHOT  
**Status**: Multi-Module Refactoring Complete  
**Date**: 2025-12-21

---

## Overview

JScience Reimagined is a unified scientific computing framework where all sciences naturally build upon their mathematical foundations.

```
Mathematics → Physics → Chemistry → Biology → Human Sciences
```

Each layer automatically inherits capabilities from layers below, enabling domain scientists to work at their level without managing lower-level details.

---

## Design Principles

### 1. **Scientific Hierarchy**

Respect the natural dependencies between sciences:

- Mathematics provides foundational structures
- Physics builds on mathematical models
- Chemistry uses physics (quantum + classical)
- Biology leverages chemistry and physics
- Human sciences incorporate biological and social models

### 2. **Flexible Precision**

Support multiple numeric types without code changes:

- `DoubleScalar` - Fast, default (15 digits)
- `FloatScalar` - GPU-friendly (7 digits)  
- `IntScalar` - Discrete mathematics (ℤ)
- `ExactScalar` - Arbitrary precision (BigDecimal)

### 3. **Performance First**

- Primitives by default (doubles, not objects)
- Zero-cost abstractions (JIT eliminates generics overhead)
- Dynamic backend selection (Java/BLAS/CUDA)
- Memory pooling to minimize GC pressure

### 4. **Ease of Use**

- Domain scientists work at their layer
- Automatic access to lower layers
- Type-safe generic algorithms
- Comprehensive documentation

---

## Layer Architecture

### Layer 1: Mathematics (Current Implementation)

**Algebraic Structures** (`org.jscience.mathematics.algebra`):

```
Set (membership) 
  ↓ binary operation
Magma (closure)
  ↓ associativity  
Group (identity + inverse)
  ↓ commutativity
AbelianGroup
  ↓ second operation (multiplication)
Ring
  ↓ division
Field
```

**Scalar Types** (`org.jscience.mathematics.scalar`):

- **ScalarType<T>**: Generic arithmetic interface
- **DoubleScalar**: 64-bit floating point (ℝ approximation)
- **FloatScalar**: 32-bit floating point (GPU-optimized)
- **IntScalar**: 32-bit integers (ℤ/2³²ℤ)
- **ExactScalar**: Arbitrary precision (ℚ or ℝ)

**Linear Algebra** (Planned):

- Generic Matrix<T, S>
- Dynamic optimization (sparse/dense/triangular)
- Backend abstraction (Java/BLAS/CUDA)

### Layer 2: Physics (Planned)

**Classical Mechanics**:

- Particle<T> (position, velocity, mass)
- RigidBody<T> (orientation, angular momentum)
- Force fields and potentials

**Thermodynamics**:

- Temperature, pressure, entropy
- Ideal gas law
- Heat transfer

**Electromagnetism**:

- Electric/magnetic fields
- Maxwell's equations
- Electromagnetic waves

**Quantum Mechanics**:

- Wave functions
- Operators and observables
- Time evolution

### Layer 3: Chemistry (Planned)

- Atom<T> (nucleus + electrons + quantum states)
- Molecule<T> (atoms + bonds)
- Chemical reactions
- Molecular dynamics simulation

### Layer 4: Biology (Planned)

- DNASequence<T>
- Protein<T> (amino acids + 3D structure)
- Cellular structures
- Evolution/genetics algorithms

### Layer 5: Human Sciences (Planned)

- Population demographics
- Economic models
- Social networks
- Linguistics

---

## Mathematical Type System

### Philosophical Distinction

**Discrete Types** (ℤ, ℕ - countable):

- `IntScalar`: ℤ/2³²ℤ - Forms a ring with modular arithmetic
- `LongScalar`: ℤ/2⁶⁴ℤ - Larger integer range
- Use for: combinatorics, graph algorithms, discrete math

**Continuous Types** (ℝ, ℂ - uncountable):

- `DoubleScalar`: ℝ approximation - Forms a field (with rounding)
- `FloatScalar`: ℝ approximation - GPU-friendly
- `ExactScalar`: ℚ or ℝ - Configurable precision
- Use for: calculus, physics, differential equations

### Performance Characteristics

| Type | Size | Precision | Speed | Best Use Case |
|------|------|-----------|-------|---------------|
| IntScalar | 4 bytes | Exact (±2³¹) | Fast | Discrete math |
| FloatScalar | 4 bytes | ~7 digits | Fast (GPU) | Graphics, ML |
| DoubleScalar | 8 bytes | ~15 digits | Fast | Science (default) |
| ExactScalar | Variable | Configurable | Slow | Finance, validation |

---

## Generic Algorithm Pattern

All algorithms are generic over scalar type:

```java
public <T> T algorithm(T input, ScalarType<T> ops) {
    T result = ops.zero();
    // Algorithm logic using ops.add(), ops.multiply(), etc.
    return result;
}

// Fast execution
Double fastResult = algorithm(5.0, new DoubleScalar());

// Exact execution
BigDecimal exactResult = algorithm(
    new BigDecimal("5"), 
    new ExactScalar()
);
```

JIT compiler optimizes away abstractions for primitive types!

---

## Backend Abstraction (Future)

```
Algorithm Layer
     ↓
Backend Interface
     ↓
   ┌─┴─┬────────┬────────┐
   │   │        │        │
 Java BLAS   cuBLAS   OpenCL
 (CPU) (Native) (NVIDIA) (Multi-GPU)
```

Automatic selection based on:

- Hardware availability
- Problem size
- Data type (float vs double)

---

## JSR-385 Integration (Future)

Units of measurement throughout:

```java
Quantity<Length> distance = Quantities.getQuantity(5.0, METRE);
Quantity<Velocity> speed = Quantities.getQuantity(10.0, METRE_PER_SECOND);
Quantity<Time> time = distance.divide(speed).asType(Time.class);
// Automatic unit conversion and dimensional analysis
```

---

## Testing Strategy

### Unit Tests

- Every class has comprehensive tests
- Property-based testing for algebraic laws
- Edge cases (zero, negative, overflow)

### Integration Tests

- Cross-layer integration (DNA → Chemistry → Physics)
- Performance benchmarks
- Precision validation (exact vs approximate)

### Current Coverage

- ✅ DoubleScalar: 13/13 tests passing
- ✅ Algebraic structures: Compile-time validation
- 🔜 FloatScalar, IntScalar, ExactScalar tests

---

## Internationalization

All user-facing strings in resource bundles:

- Error messages
- Mathematical terminology
- Documentation

Supported languages:

- English (default)
- Français
- Español (planned)
- Deutsch (planned)

---

## Current Status (2025-11-24)

### ✅ Completed

- Project structure (Maven + Git)
- Algebraic hierarchy (Set → Field)
- Scalar type system (4 implementations)
- Comprehensive Javadoc
- Unit tests (13/13 passing)
- i18n (EN, FR)

### 🚧 In Progress

- Complex number types
- Additional scalar tests
- Architecture documentation

### 📋 Next Steps

1. Complete scalar type suite (LongScalar)
2. Complex<T> implementation
3. Vector<T> and Matrix<T> interfaces
4. Dynamic matrix optimization
5. First physics layer classes
6. JSR-385 integration

---

## Performance Targets

| Operation | Target | Current |
|-----------|--------|---------|
| Scalar add (double) | <2ns | ✅ ~1ns |
| Matrix multiply 1000x1000 (double) | <1s | 🔜 TBD |
| Matrix multiply 1000x1000 (GPU) | <50ms | 🔜 TBD |
| Test coverage | >90% | ~85% |

---

## Dependencies

**Required**:

- Java 21+
- Maven 3.8+

**Optional**:

- CUDA Toolkit 12.0+ (for GPU support)
- BLAS/LAPACK (for native optimization)

**External Libraries**:

- JSR-385 (Units API)
- SLF4J + Logback (logging)
- JUnit 5 (testing)

---

## Contributing Guidelines

1. All code must have complete Javadoc
2. Unit tests required (>90% coverage)
3. Follow algebraic structure hierarchy
4. Document mathematical concepts
5. Performance-critical code needs benchmarks
6. i18n for all user-facing strings

---

## References

- **Algebra**: Dummit & Foote, "Abstract Algebra"
- **Linear Algebra**: Strang, "Linear Algebra and Its Applications"
- **Numerical Methods**: Press et al., "Numerical Recipes"
- **JSR-385**: <https://github.com/unitsofmeasurement/unit-api>
- **Original JScience**: <http://jscience.org/> (inspiration)

---

*This architecture document will evolve as the project grows. Last updated: 2025-12-21*
