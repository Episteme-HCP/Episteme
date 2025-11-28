# Units and Measures Implementation Plan

**Package**: `org.jscience.measure`  
**Priority**: Phase 2 - Core Packages (Next in docs/task.md)  
**Status**: Planning

---

## Overview

Implement a comprehensive units and measures system for physical quantities, enabling type-safe dimensional analysis and automatic unit conversions.

### Goals

✅ **Type Safety** - Compile-time dimensional analysis  
✅ **SI Units** - Full metric system support  
✅ **Conversions** - Automatic unit conversions  
✅ **Custom Units** - User-defined units and quantities  
✅ **JSR-385 Compatible** - Standard Java units API patterns

---

## Architecture

### Core Interfaces

```java
package org.jscience.measure;

// Quantity with value and unit
interface Quantity<Q extends Quantity<Q>> {
    Real getValue();
    Unit<Q> getUnit();
    Quantity<Q> to(Unit<Q> unit);  // Convert
    Quantity<Q> add(Quantity<Q> other);
    Quantity<Q> subtract(Quantity<Q> other);
}

// Unit of measurement
interface Unit<Q extends Quantity<Q>> {
    String getSymbol();
    Dimension getDimension();
    UnitConverter getConverterTo(Unit<Q> target);
    Unit<Q> multiply(Unit<?> other);
    Unit<Q> divide(Unit<?> other);
}

// Dimensional analysis  
interface Dimension {
    int getLengthExponent();
    int getMassExponent();
    int getTimeExponent();
    // ... other SI base dimensions
}
```

### Standard Quantity Types

```java
// Length
interface Length extends Quantity<Length> {}

// Mass  
interface Mass extends Quantity<Mass> {}

// Time
interface Time extends Quantity<Time> {}

// Velocity = Length / Time
interface Velocity extends Quantity<Velocity> {}

// Force = Mass * Length / Time^2
interface Force extends Quantity<Force> {}
```

---

## Implementation Phases

### Phase 1: Core Framework ✅

**[Units.java](file:///c:/Silvere/Encours/Developpement/JScience/src/main/java/org/jscience/measure/Units.java)** (Base units registry)
**[Dimension.java](file:///c:/Silvere/Encours/Developpement/JScience/src/main/java/org/jscience/measure/Dimension.java)** (SI dimensions)
**[UnitConverter.java](file:///c:/Silvere/Encours/Developpement/JScience/src/main/java/org/jscience/measure/UnitConverter.java)** (Conversion interface)

### Phase 2: SI Units

Create all metric (SI) units:

**Length**: meter, kilometer, centimeter, millimeter
**Mass**: kilogram, gram, tonne
**Time**: second, minute, hour, day
**Temperature**: kelvin, celsius
**Current**: ampere
**Amount**: mole
**Luminosity**: candela

### Phase 3: Derived Units

**Area** = Length²
**Volume** = Length³
**Speed** = Length / Time
**Acceleration** = Length / Time²  
**Force** = Mass × Acceleration (Newton)
**Pressure** = Force / Area (Pascal)
**Energy** = Force × Length (Joule)
**Power** = Energy / Time (Watt)

### Phase 4: Non-SI Units

**Imperial/US**: foot, inch, mile, pound, gallon, etc.
**Astronomical**: light-year, parsec, AU
**Atomic**: electron-volt, atomic mass unit

---

## Usage Examples

### Basic Quantities

```java
import org.jscience.measure.*;
import org.jscience.measure.quantity.*;
import static org.jscience.measure.Units.*;

// Create quantities
Quantity<Length> distance = Quantities.create(100, METER);
Quantity<Time> time = Quantities.create(10, SECOND);

// Unit conversion
Quantity<Length> km = distance.to(KILOMETER);  // 0.1 km
```

### Dimensional Analysis

```java
// Velocity = Length / Time (automatic)
Quantity<Velocity> speed = distance.divide(time);  // 10 m/s

// Convert to km/h
Quantity<Velocity> kmh = speed.to(KILOMETER_PER_HOUR);  // 36 km/h
```

### Type Safety

```java
// Compile error - cannot add length and mass
Quantity<Length> len = Quantities.create(10, METER);
Quantity<Mass> mass = Quantities.create(5, KILOGRAM);
len.add(mass);  // ❌ Type error!

// OK - same dimension
Quantity<Length> total = len.add(Quantities.create(5, METER));  // ✅
```

### Custom Units

```java
// Define custom unit
Unit<Length> FURLONG = METER.multiply(201.168);

// Use it
Quantity<Length> race = Quantities.create(8, FURLONG);  // 1 mile
Quantity<Length> meters = race.to(METER);  // 1609.344 m
```

---

## Design Decisions

### 1. Parameterized Types for Safety

Use generics to enforce dimensional correctness at compile time:

```java
interface Quantity<Q extends Quantity<Q>>  // Self-referential
```

This prevents:
```java
Quantity<Length> + Quantity<Mass>  // ❌ Won't compile
```

### 2. Real for Values

Use `org.jscience.mathematics.number.Real` for quantity values:
- Arbitrary precision when needed
- Exact arithmetic for rational conversions
- Consistent with JScience number system

### 3. Immutable Quantities

All quantities are immutable - operations return new instances:

```java
Quantity<Length> a = Quantities.create(10, METER);
Quantity<Length> b = a.add(Quantities.create(5, METER));
// a is unchanged, b = 15 m
```

### 4. SI-First Approach

SI units are primary, with converters for other systems:

```java
// Meter is base Length unit
FOOT = METER.multiply(0.3048);  // Defined in terms of meter
```

---

## File Structure

```
org.jscience.measure/
├── Quantity.java
├── Unit.java
├── Dimension.java
├── UnitConverter.java
├── Quantities.java (factory)
├── Units.java (constants)
│
├── quantity/ (quantity types)
│   ├── Length.java
│   ├── Mass.java
│   ├── Time.java
│   ├── Velocity.java
│   ├── Force.java
│   └── ... (30+ standard quantities)
│
├── unit/ (implementations)
│   ├── BaseUnit.java
│   ├── DerivedUnit.java  
│   ├── ProductUnit.java
│   └── TransformedUnit.java
│
└── converter/ (conversion implementations)
    ├── LinearConverter.java
    ├── RationalConverter.java
    └── CompoundConverter.java
```

---

## Integration with JScience

### Number Types

```java
// Quantities use Real
Quantity<Length> precise = Quantities.create(
    Real.valueOf("1.23456789012345678901234567890"),
    METER
);
```

### Linear Algebra

```java
// Vector of quantities
Vector<Quantity<Force>> forces = ...;
Quantity<Force> resultant = forces.sum();
```

### Physics Package

Units enable the physics package:

```java
// In org.jscience.physics
class Particle {
    Quantity<Mass> mass;
    Quantity<Velocity> velocity;
    
    Quantity<Energy> kineticEnergy() {
        return mass.multiply(velocity.pow(2)).divide(2);
    }
}
```

---

## Testing Strategy

### Unit Tests

- Dimensional analysis correctness
- Conversion accuracy (rational conversions exact)
- Edge cases (zero, infinity, very large/small)
- Custom unit definition

### Integration Tests

- Physics calculations
- Multi-step conversions
- Performance benchmarks

---

## Performance Considerations

### Conversion Caching

```java
// Cache frequently used converters
Map<Pair<Unit, Unit>, UnitConverter> converterCache;
```

### Lazy Evaluation

```java
// Don't convert until needed
Quantity<Length> km = meters.to(KILOMETER);  // Lazy
Real value = km.getValue();  // Conversion happens here
```

---

## Standards Compliance

### JSR-385 Compatibility

Follow Units of Measurement API patterns:
- Similar interface names and methods
- Compatible type hierarchy
- Standard unit symbols (UCUM)

### UCUM Symbols

Use Unified Code for Units of Measure:
- `m` for meter
- `kg` for kilogram  
- `m/s` for meter per second
- `N` for newton

---

## Migration from V1

JScience V1 had a measures package. Key improvements:

✅ **Better type safety** - Generic parameter prevents mixing dimensions  
✅ **Real instead of double** - Arbitrary precision  
✅ **Simplified API** - Fewer classes, clearer hierarchy  
✅ **Standard compliance** - JSR-385 patterns

---

## Next Steps

1. **Create core interfaces** (Quantity, Unit, Dimension)
2. **Implement base SI units** (meter, kilogram, second, etc.)
3. **Add conversion framework** (UnitConverter implementations)
4. **Define derived units** (velocity, force, energy, etc.)
5. **Add non-SI units** (imperial, astronomical, etc.)
6. **Write comprehensive tests**
7. **Document usage patterns**

---

## Success Criteria

✅ All 7 SI base units defined  
✅ 20+ derived SI units  
✅ Imperial/US customary units  
✅ Compile-time dimensional checking  
✅ Exact rational conversions  
✅ 95%+ test coverage  
✅ Comprehensive documentation

---

## User Review Required

> [!IMPORTANT]
> **Design Decision**: Use `Real` for all quantity values instead of `double`?
> - **Pro**: Arbitrary precision, exact arithmetic, consistent with JScience
> - **Con**: Potential performance overhead vs. primitive double

**Recommendation**: Use `Real` by default, with `doubleValue()` for performance-critical paths.

