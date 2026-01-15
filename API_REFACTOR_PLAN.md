# JScience API Refactoring Plan

## Goal
Replace hardcoded/primitive implementations with JScience Core, Natural, and Social APIs.

## Available Core APIs

### Backend Providers (in `jscience-core/.../algorithms/`)
- **Physics Simulation**: `NBodyProvider`, `MolecularDynamicsProvider`, `MaxwellProvider`, `NavierStokesProvider`, `WaveProvider`, `SPHFluidProvider`, `LatticeBoltzmann`
- **Mathematics**: `FFTProvider`, `FEMProvider`, `MandelbrotProvider`, `MonteCarloProvider`, `MonteCarloPiProvider`
- **Variants**: Multicore, CUDA, OpenCL versions available

### Data Loaders (in `jscience-natural/.../loaders/`)
**Astronomy**: `VizieRReader`, `SIMBADReader`, `StarReader`, `HorizonsEphemerisReader`, `NASAExoplanetsReader`, `SolarSystemReader`
**Biology**: `NCBITaxonomyReader`, `PhylogeneticTreeReader`, `FASTAReader`, `GBIFSpeciesReader`, `UniProtReader`
**Chemistry**: `PeriodicTableReader`, `PubChemReader`, `ChEBIReader`, `CIFReader`
**Other**: `OpenWeatherReader`, `USGSEarthquakesReader`, `DrugBankReader`

### Math Types
- `Real` instead of `double` (arbitrary precision)
- `Matrix`, `Vector` for linear algebra
- Complex numbers, quaternions

## Files to Refactor

### Priority 1: Data Loaders Integration
| File | Current State | Target API |
|------|--------------|------------|
| `StellarSkyViewer.java` | CSV hardcoded stars | Use `VizieRReader` for real astronomical data +  `StarReader` for base catalog |
| `PhylogeneticTreeViewer.java` | CSV custom data | Use `NCBITaxonomyReader` + `PhylogeneticTreeReader` |
| `PeriodicTableViewer.java` | Hardcoded element data | Use `PeriodicTableReader` |

### Priority 2: Simulation Providers
| File | Current State | Target API |
|------|--------------|------------|
| `AudioViewer.java` / `SpectrographViewer.java` | Primitive DSP | Use `FFTProvider` for spectral analysis |
| `MagneticFieldViewer.java` | Hardcoded field calc | Use `MaxwellProvider` for field integration |
| `CircuitSimulatorViewer.java` | Custom solver | Enhance with `FEMProvider` or Matrix/Real solvers |
| `LotkaVolterraViewer.java` | Primitive integration | Use Real-based ODE integration |
| `GeneticsViewer.java` | Hardcoded genetics | Integrate with biology loaders |
| `LSystemViewer.java` | Primitive recursion | Use Real for precision, Matrix for transformations |

### Priority 3: 3D/Graphics Viewers  
| File | Current State | Target API |
|------|--------------|------------|
| `SpinField2DViewer.java` | `double[][]` arrays | Internal `Matrix<Real>`, convert to primitives for rendering |
| `Jzy3dSpintronic3DViewer.java` | `float[]` buffers | Internal `Matrix<Real>`, convert for OpenGL |
| `CrystalStructureApp.java` | Primitive coords | Use `Vector<Real>` internally, extract for rendering |
| `PandemicForecasterApp.java` | Primitive SIR model | Use `Real` for precision, Matrix for population vectors |

### Already Compliant (checked items)
- ✅ `MandelbrotDemo.java` 
- ✅ `LorenzDemo.java` 
- ✅ `StarSystemDemo.java` 
- ✅ `InterplanetaryTrajectoryDemo.java` 
- ✅ `FluidDynamicsDemo.java` (has dual implementation)
- ✅ `ClothSimulationDemo.java`
- ✅ `KurzweilDemo.java` (already uses TimelineViewer correctly!)

## Implementation Strategy

### Phase 1: Data Loaders (StellarSkyViewer, PhylogeneticTreeViewer, PeriodicTableViewer)
1. Replace CSV loading with Reader APIs
2. Add "Browse/Query" functionality using real data sources
3. Keep rendering performance (convert Real to double only for final display)

### Phase 2: Simulation Backends (Audio, Magnetic, Circuit)
1. Wrap primitive arrays in Matrix/Vector when needed
2. Call appropriate Provider for computation
3. Extract results for rendering

### Phase 3: 3D/Graphics (Spin, Crystal, Pandemic)
1. Use Real/Matrix internally for state
2. Convert to primitives ONLY for final OpenGL/JavaFX rendering
3. Document the conversion pattern

## Key Patterns

### Pattern 1: Real for State, double for Rendering
```java
// State
private Real cellValue = Real.of(0.5);

// Rendering
gc.fillRect(x, y, w, h);  // primitives OK here
```

### Pattern 2: Matrix for Data, extract for Buffers
```java
// Computation
Matrix<Real> field = provider.compute(...);

// For OpenGL
float[] buffer = new float[size];
for (int i = 0; i < size; i++) {
    buffer[i] = field.get(i).floatValue();
}
```

### Pattern 3: Loader Integration
```java
// Before: CSV hardcoded
List<Star> stars = loadFromCSV();

// After: Use Reader with fallback
StarReader reader = new StarReader();
List<Star> stars = reader.loadFromSource("hipparcos");
// Can also query live: VizieRReader.queryByCoordinates(...)
```

## Timeline
1. Document current state of each file ✅
2. Refactor Priority 1 (3 files) - Data Loaders
3. Refactor Priority 2 (6 files) - Simulation Backends  
4. Refactor Priority 3 (4 files) - 3D/Graphics
5. Verify compilation and functionality
6. Update documentation
