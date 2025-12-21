# JScience Roadmap

## Project Status

**Current Version**: 2.0.0-SNAPSHOT  
**Focus**: Multi-Module Architecture, Type-Safe Measurements, External Data Integration

---

## Completed Tasks ✓

### Project Structure

- [x] Maven multi-module: `jscience-core`, `jscience-natural`, `jscience-social`
- [x] Modern Java 21+ with incubator vector module support
- [x] Javadoc generation with `mvn javadoc:aggregate`

### Core Mathematics (`jscience-core`)

- [x] Algebraic structures: Field, Ring, Group, Vector, Matrix
- [x] Linear algebra: Dense/Sparse vectors, matrix operations
- [x] Symbolic math foundations (expression trees)
- [x] Geometry: Polyhedra, collision detection, transforms

### Measurement System

- [x] `Quantity<Q>` type-safe measurement class
- [x] `Units` class with SI and derived units
- [x] Thermodynamics converted to Quantity (IdealGas, ThermodynamicState)

### External Resource Loaders

- [x] PubChem (chemistry compounds)
- [x] NASA Exoplanets (habitable zone search, ESI)
- [x] GenBank (DNA/protein sequences, GC content)
- [x] SIMBAD (astronomical objects)
- [x] USGS Earthquakes (seismic data)
- [x] World Bank (economic indicators)
- [x] GBIF/NCBI Taxonomy (biological classification)

### Scientific Features

- [x] ProjectileMotion (trajectory, optimal angle)
- [x] SIR Epidemic Model (R₀, herd immunity, disease presets)
- [x] Kinematics (motion equations with Quantity)

---

## Active Tasks 🔄

### Quantity Adoption (All Science Domains)
>
> **Rule**: All physical values must use `Quantity<Q>` types - no raw doubles for temperature, mass, length, time, etc.

- [x] **Physics**: Mechanics, Electromagnetism, Optics, Acoustics
- [x] **Chemistry**: Concentrations, Reaction rates, Enthalpies (ArrheniusEquation, HessLaw, NernstEquation)
- [x] **Biology**: Population sizes, Growth rates, Enzyme kinetics
- [x] **Earth Science**: Coordinates, Elevations, Seismic magnitudes (PlateMotionCalculator, VincentyUtils)
- [x] **Medicine**: Dosages, Vital signs, Body measurements
- [x] **Engineering**: Stress, Strain, Current, Voltage (CircuitAnalysis, StructuralAnalysis)

### New Resource Loaders

- [x] **ChEBI** - Chemical Entities of Biological Interest
- [x] **CrossRef/DOI** - Bibliography and citations
- [x] **UniProt** - Protein database
- [x] **IUPAC Gold Book** - Chemistry nomenclature
- [x] **PDB** - Protein Data Bank (3D structures)
- [x] **OpenWeather** - Meteorological data
- [x] **CDS/VizieR** - Astronomical catalogs

---

## New Scientific Features (By Domain)

### Chemistry

- [x] ReactionBalancer - Stoichiometry equation balancing
- [x] Electrochemistry - Nernst equation, cell potentials
- [x] Thermochemistry - Hess's law, enthalpy calculations
- [x] pH/Buffer calculations
- [x] Molecular orbital theory basics

### Biology

- [x] PopulationDynamics - Logistic growth, Lotka-Volterra
- [x] GeneticAlgorithm - Evolution simulation
- [x] SequenceAlignment - Needleman-Wunsch, BLAST-like
- [x] PhylogeneticTree - Distance matrix, UPGMA
- [x] EnzymeKinetics - Michaelis-Menten model

### Earth Science

- [x] PlateMotion - GPS velocity vectors (Vincenty formulae)
- [x] SeismicWaveTravel - P/S wave arrival times, Magnitude, Energy
- [x] AtmosphericPressure - Barometric formula
- [x] TidalPrediction - Harmonic analysis
- [x] GeodeticDistance - Vincenty/Haversine formulas

### Engineering

- [x] CircuitSolver - Ohm's law, Kirchhoff's rules, Impedance
- [x] BeamDeflection - Euler-Bernoulli beam theory
- [x] HeatTransfer - Conduction, convection, radiation
- [x] FluidFlow - Bernoulli, Reynolds number
- [x] SignalProcessing - FFT, filters

### Medicine

- [x] Pharmacokinetics - ADME models, half-life, dosing
- [x] BMI/BSA calculators with Quantity
- [x] ECG analysis basics
- [x] DoseCalculator - Weight-based dosing
- [x] EpidemiologyStats - Incidence, prevalence, odds ratios

### Physics/Astronomy

- [x] OrbitalMechanics - Kepler's laws, vis-viva
- [x] SpectralAnalysis - Blackbody, Wien's law
- [x] QuantumHarmonicOscillator
- [x] Pendulum (simple, damped, driven)
- [x] OpticsSystems - Lenses, mirrors, ray tracing

### Computing/AI

- [x] NeuralNetwork enhancements (activation functions)
- [x] GeneticProgramming
- [x] FuzzyLogic engine
- [x] DataNormalization utilities

### Bibliography

- [x] BibTeX parser/generator
- [x] Citation formatter (APA, MLA, Chicago)
- [x] DOI resolver and metadata fetcher
- [x] Reference manager integration

### UI/Demos

- [x] JScienceDemoApp - Master demo launcher
- [x] DemoProvider interface with ServiceLoader
- [x] MatrixViewerDemo, PlottingDemo (jscience-natural)
- [x] EconomicsDemo - GDP growth model
- [x] GeneticsDemo - Genetic drift simulation
- [x] StarSystemViewer, MolecularViewer
- [x] L-System, Logic Gate, Oscilloscope viewers

### Architecture Improvements

- [x] Loader standardization (AbstractLoader base class)
- [x] TTL caching support (24h default)
- [x] Package alignment (electronics→engineering, matrix→mathematics)
- [x] Consolidated Javadoc in `/javadoc`

---

## Known Issues / Technical Debt

- **Symbolic Math**: Integration/differentiation engines ✓ (Expression interface, differentiate/integrate methods)
- **gRPC Protobuf**: Generated code has type safety warnings (acceptable)
- **GPU Backends**: ✓ GPUBackend interface with CUDA/OpenCL stubs and CPUFallbackBackend
- **Vector.java**: Unchecked casts handled with @SuppressWarnings (Java generic erasure limitation)

---

## Version Milestones

| Version | Focus |
|---------|-------|
| 2.0 | Multi-module, Quantity adoption, External loaders |
| 2.1 | Chemistry & Biology features |
| 2.2 | Engineering & Earth Science |
| 2.3 | Medicine & Physics expansion |
| 3.0 | Full symbolic math, GPU production-ready |
