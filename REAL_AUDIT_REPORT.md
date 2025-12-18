# Real vs Double Usage Audit Report

## Executive Summary

A comprehensive scan of the codebase reveals significant usage of primitive `double` and wrapper `Double` in areas where `org.jscience.mathematics.numbers.real.Real` should be used to support arbitrary precision and field operations.

## Key Findings

### 1. Statistical Distributions (`jscience-core`)

The package `org.jscience.mathematics.statistics.distributions` relies almost exclusively on `java.lang.Math` (and thus `double`) for probability density functions (PDF) and cumulative distribution functions (CDF).

**Affected Classes:**

- `BetaDistribution.java`
- `BinomialDistribution.java`
- `CauchyDistribution.java`
- `ExponentialDistribution.java`
- `GammaDistribution.java`
- `GeometricDistribution.java`
- `LogNormalDistribution.java`
- `NormalDistribution.java`
- `PoissonDistribution.java`
- `StudentTDistribution.java`
- `WeibullDistribution.java`

**Issues:**

- Mathematical functions (`pow`, `exp`, `log`, `gamma`) use standard library double-precision implementations.
- Intermediate calculations lose precision before being wrapped in `Real.of()` at the end.
- Field operations (Generic `Field<T>`) are effectively bypassed for concrete implementations.

### 2. Seismology (`jscience-natural`)

The newly added `Seismology` class is entirely based on primitive `double`.

**Affected Class:** `org.jscience.earth.seismology.Seismology`

**Usage:**

- Constants: `VP_CRUST`, `VS_CRUST`, `EARTH_RADIUS_KM` are `double`.
- Methods: `magnitudeToMoment`, `energyReleased`, `pWaveTravelTime` all operate on primitives.
- **Recommendation:** Convert constants to `Real` and methods to accept/return `Real` or `Quantity`.

### 3. Nuclear Physics (`jscience-natural`)

`RadioactiveDecay` shows mixed usage. While fields like `halfLife` are `Real`, many calculation methods degrade to `double`.

**Affected Class:** `org.jscience.physics.nuclear.RadioactiveDecay`

**Usage:**

- Constant: `AVOGADRO` is `double` (should be `Real` or `Quantity`).
- Methods: `fractionRemaining`, `activityFromMass`, `carbonDate`, `uraniumLeadDate` use `Math.exp` and `Math.log`.

### 4. Other Potential Candidates

- `NBodyNaiveBenchmark` (Intentionally primitive for performance baseline - Valid Exception)
- `NBodySimulation` (Uses `Real`, but check for hidden `double` math)

## Recommendations

1. **Refactor Distributions**: Create a `RealMath` utility or extend `Real` to provide arbitrary-precision implementations of special functions (Gamma, Erf, Beta) or strictly use `Real` methods.
2. **Update Seismology**: Rewrite `Seismology` to use `Real` for all calculations to allow high-precision earthquake modeling.
3. **Standardize Constants**: Move physical constants (Avogadro, Earth Radius) to a centralized `PhysicsConstants` class using `Real`.
4. **Strict Mode**: Consider adding a simplified "Strict Real" mode in the future that warns when `double` is used in scientific calculations.
