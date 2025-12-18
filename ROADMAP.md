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

- [ ] **Physics**: Mechanics, Electromagnetism, Optics, Acoustics
- [ ] **Chemistry**: Concentrations, Reaction rates, Enthalpies
- [ ] **Biology**: Population sizes, Growth rates, Enzyme kinetics
- [ ] **Earth Science**: Coordinates, Elevations, Seismic magnitudes
- [ ] **Medicine**: Dosages, Vital signs, Body measurements
- [ ] **Engineering**: Stress, Strain, Current, Voltage

### New Resource Loaders

- [ ] **ChEBI** - Chemical Entities of Biological Interest
- [ ] **CrossRef/DOI** - Bibliography and citations
- [ ] **UniProt** - Protein database
- [ ] **IUPAC Gold Book** - Chemistry nomenclature
- [ ] **PDB** - Protein Data Bank (3D structures)
- [ ] **OpenWeather** - Meteorological data
- [ ] **CDS/VizieR** - Astronomical catalogs

---

## New Scientific Features (By Domain)

### Chemistry

- [ ] ReactionBalancer - Stoichiometry equation balancing
- [ ] Electrochemistry - Nernst equation, cell potentials
- [ ] Thermochemistry - Hess's law, enthalpy calculations
- [ ] pH/Buffer calculations
- [ ] Molecular orbital theory basics

### Biology

- [ ] PopulationDynamics - Logistic growth, Lotka-Volterra
- [ ] GeneticAlgorithm - Evolution simulation
- [ ] SequenceAlignment - Needleman-Wunsch, BLAST-like
- [ ] PhylogeneticTree - Distance matrix, UPGMA
- [ ] EnzymeKinetics - Michaelis-Menten model

### Earth Science

- [ ] PlateMotion - GPS velocity vectors
- [ ] SeismicWaveTravel - P/S wave arrival times
- [ ] AtmosphericPressure - Barometric formula
- [ ] TidalPrediction - Harmonic analysis
- [ ] GeodeticDistance - Vincenty/Haversine formulas

### Engineering

- [ ] CircuitSolver - Ohm's law, Kirchhoff's rules
- [ ] BeamDeflection - Euler-Bernoulli beam theory
- [ ] HeatTransfer - Conduction, convection, radiation
- [ ] FluidFlow - Bernoulli, Reynolds number
- [ ] SignalProcessing - FFT, filters

### Medicine

- [ ] Pharmacokinetics - ADME models, half-life
- [ ] BMI/BSA calculators with Quantity
- [ ] ECG analysis basics
- [ ] DoseCalculator - Weight-based dosing
- [ ] EpidemiologyStats - Incidence, prevalence, odds ratios

### Physics/Astronomy

- [ ] OrbitalMechanics - Kepler's laws, vis-viva
- [ ] SpectralAnalysis - Blackbody, Wien's law
- [ ] QuantumHarmonicOscillator
- [ ] Pendulum (simple, damped, driven)
- [ ] OpticsSystems - Lenses, mirrors, ray tracing

### Computing/AI

- [ ] NeuralNetwork enhancements (activation functions)
- [ ] GeneticProgramming
- [ ] FuzzyLogic engine
- [ ] DataNormalization utilities

### Bibliography

- [ ] BibTeX parser/generator
- [ ] Citation formatter (APA, MLA, Chicago)
- [ ] DOI resolver and metadata fetcher
- [ ] Reference manager integration

---

## Known Issues / Technical Debt

- **Symbolic Math**: Integration/differentiation engines incomplete
- **gRPC Protobuf**: Generated code has type safety warnings (acceptable)
- **GPU Backends**: CUDA/OpenCL experimental, needs hardware testing
- **Vector.java**: Unchecked casts due to Java generic erasure

---

## Version Milestones

| Version | Focus |
|---------|-------|
| 2.0 | Multi-module, Quantity adoption, External loaders |
| 2.1 | Chemistry & Biology features |
| 2.2 | Engineering & Earth Science |
| 2.3 | Medicine & Physics expansion |
| 3.0 | Full symbolic math, GPU production-ready |
