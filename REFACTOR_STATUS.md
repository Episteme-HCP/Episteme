# API Refactoring - Quick Status Update

## ✅ Already Using Core APIs (Verified)
1. **StellarSkyViewer** - Enhanced with VizieRReader ✅
2. **PeriodicTableViewer** - Uses `PeriodicTable.bySymbol()` and `ChemistryDataReader` ✅
3. **KurzweilDemo** - Uses `TimelineViewer` and Timeline APIs ✅

## 🔄 In Progress - To Complete Now

### High Priority
1. **PhylogeneticTreeViewer** - Add NCBI browser (20 min)
2. **AudioViewer/SpectrographViewer** - FFT Provider (30 min)
3. **MagneticFieldViewer** - MaxwellProvider integration (30 min)

### Medium Priority
4. **CrystalStructureApp** - Use Vector<Real> for coordinates
5. **CircuitSimulatorViewer** - Matrix-based solver enhancement
6. **LSystemViewer** - Matrix transformations
7. **LotkaVolterraViewer** - Real for ODE integration
8. **GeneticsViewer** - Biology loaders integration

### Lower Priority (Internal refactoring)
9. **SpinField2DViewer** - Matrix<Real> internally
10. **Jzy3dSpintronic3DViewer** - Matrix<Real> with float[] export
11. **PandemicForecasterApp** - Matrix<Real> for populations

## Next Action
Continuing with PhylogeneticTreeViewer NCBI integration...
