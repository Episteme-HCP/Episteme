# JScience API Refactoring Progress

## Summary
Refactoring viewers and demos to use JScience Core, Natural, and Social APIs instead of hardcoded/primitive implementations.

## Completed ✅

### 1. StellarSkyViewer.java - VizieR Integration ✅
**Status**: COMPLETE  
**Changes**:
- Added real-time VizieR astronomical catalog browsing
- Users can now query Hipparcos, Tycho-2, Gaia DR3, USNO-B1 catalogs
- Implemented background thread querying with UI feedback
- Kept base StarReader CSV loading as fallback
- **API Used**: `VizieRReader.queryByCoordinates()`, `VizieRReader.HIPPARCOS/TYCHO2/GAIA_DR3`

**Impact**: Users can now browse REAL astronomical data from online catalogs instead of just static CSV files!

## In Progress 🔄

### Next Priority Files

#### 2. PhylogeneticTreeViewer.java - Taxonomy Loader Integration
**Current**: Uses hardcoded CSV phylogenetic data  
**Target**: Integrate `NCBITaxonomyReader` + `PhylogeneticTreeReader` for browsing real taxonomy databases  
**APIs to use**:
- `NCBITaxonomyReader` - Query NCBI Taxonomy database
- `PhylogeneticTreeReader` - Load phylogenetic tree formats (Newick, PhyloXML)
- `GBIFSpeciesReader` - Additional species data from GBIF
**Estimated effort**: Medium (similar to StellarSkyViewer)

#### 3. PeriodicTableViewer.java - Chemistry Data Integration  
**Current**: Hardcoded element properties  
**Target**: Use `PeriodicTableReader` + `ChEBIReader` for real chemistry databases  
**APIs to use**:
- `PeriodicTableReader` -Element data with isotopes
- `PubChemReader` - Compound lookups
- `ChEBIReader` - Chemical entities database
**Estimated effort**: Low-Medium (straightforward data replacement)

#### 4. AudioViewer/SpectrographViewer - FFT Provider
**Current**: Custom primitive DSP code  
**Target**: Use `FFTProvider` for spectral analysis  
**APIs to use**:
- `FFTProvider` / `MulticoreFFTProvider` - Fast Fourier Transform
- `Matrix<Real>` for frequency domain data
**Pattern**: Compute with Real, convert to double[] for rendering
**Estimated effort**: Medium (need to refactor DSP algorithms)

#### 5. MagneticFieldViewer - Maxwell Provider
**Current**: Hardcoded field calculations  
**Target**: Use `MaxwellProvider` for  electromagnetic field integration  
**APIs to use**:
- `MaxwellProvider` / `MulticoreMaxwellProvider`
- `Vector<Real>` for field vectors
**Estimated effort**: Medium-High (physics simulation)

#### 6. CircuitSimulatorViewer - Matrix/FEM Integration
**Current**: Custom SPICE-like solver  
**Target**: Enhance with `FEMProvider` or Matrix-based linear solvers  
**APIs to use**:
- `Matrix<Real>` for circuit equations (KCL/KVL)
- `FEMProvider` for advanced analysis
**Estimated effort**: High (complex circuit analysis)

## Lower Priority (Simpler Refactorings)

### Graphics/3D Files (Internal Real, extract for render)
- `Spin Field2DViewer.java` - Use `Matrix<Real>` internally
- `Jzy3dSpintronic3DViewer.java` - Use `Matrix<Real>`, convert to float[] for OpenGL
- `CrystalStructureApp.java` - Use `Vector<Real>` for atom positions
- `PandemicForecasterApp.java` - Use `Matrix<Real>` for SIR model populations

### Biological/Genetics
- `GeneticsViewer.java` - Integrate with `FASTAReader`, `UniProtReader`
- `LotkaVolterraViewer.java` - Use `Real` for ODE integration precision
- `LSystemViewer.java` - Use `Matrix<Real>` for L-system transformations

## Already Compliant ✅
- `KurzweilDemo.java` - Already uses `TimelineViewer` and `Timeline` API correctly
- `MandelbrotDemo.java`, `LorenzDemo.java`, `StarSystemDemo.java` etc. - Confirmed using providers

## Implementation Patterns Established

### Pattern 1: Data Loader Integration (StellarSkyViewer example)
```java
// Add UI section for querying
private VBox createDataQuerySection() {
    // ComboBox for catalog/database selection
    // Button for query with background thread
    // Status label for feedback
    // Call Reader.query*() methods
}

// Keep fallback to local data
stars = reader.loadFromSource("default");
```

### Pattern 2: Real for Computation, Primitives for Rendering
```java
// Computation
Matrix<Real> field = provider.compute(parameters);

// Rendering
for (int i = 0; i < size; i++) {
    double value = field.get(i).doubleValue();
    gc.drawPixel(x, y, colorFromValue(value));
}
```

### Pattern 3: Background Threading for Network
```java
new Thread(() -> {
    var result = Reader.queryOnline(...);
    Platform.runLater(() -> updateUI(result));
}).start();
```

## Next Steps

1. ✅ **StellarSkyViewer** - VizieRReader integration (DONE)
2. **PhylogeneticTreeViewer** - Taxonomy readers (NEXT)
3. **PeriodicTableViewer** - Chemistry readers
4. **Audio/Spectrograph** - FFT Provider integration
5. **MagneticField** - Maxwell Provider
6. **Circuit** - Matrix/FEM enhancement
7. Verify all compilations
8. Test key functionality
9. Document API usage examples
10. Commit and push

## Notes

- All refactorings maintain backward compatibility (local data as fallback)
- Network queries are non-blocking with user feedback
- Precision: Use `Real` for state/computation, `double` only for final rendering
- Thread safety: All UI updates via `Platform.runLater()`
