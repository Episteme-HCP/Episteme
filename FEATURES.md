# JScience Features

JScience provides a comprehensive suite of tools for scientific computing, natural sciences, and social sciences. Below is a detailed list of features categorized by module and domain.

## Technical Foundation (jscience-core)

### Mathematics & Computation

- **Linear Algebra**: High-performance Matrix and Vector operations with automatic selection of dense or sparse backends.
- **Algebraic Structures**: Implementation of Rings, Fields, Groups, and Monoids for abstract mathematical modeling.
- **Symbolic Mathematics**: Support for polynomial expressions, differentiation, and integration.
- **Discrete Math**: Graph theory, rooted trees, and discrete structures.
- **Distributed Computing**: Framework for parallel and distributed scientific kernels.
- **Flexible Precision**: Seamless switching between `double`, `BigDecimal` (Exact), and `float` (Fast) arithmetic.
- **GPU Acceleration**: OpenCL and CUDA backends for hardware-accelerated computations.

### Units & Measurement

- **JSR-385 Integration**: Full support for the Units of Measurement API (Indriya).
- **Physical Quantities**: Standardized representation of Length, Mass, Time, Temperature, Velocity, Energy, Pressure, and more.
- **Type-Safe Conversions**: Compile-time check for dimensional consistency during unit conversions.

### Documentation & Research

- **Bibliography Management**: Integrated citation management with CrossRef support.
- **Scientific I/O**: Custom loaders for JSON, Properties, and Protobuf data formats.

---

## Natural Sciences (jscience-natural)

### Physics

- **Mechanics**: Classical dynamics, kinematics, and ballistics engines.
- **Thermodynamics**: Fluid dynamics, heat transfer, and state equation modeling.
- **Astronomy**: Ephemeris tracking, exoplanet catalogs, and Simbad/Simbad integration.
- **Quantum Computing**: Primitives for quantum state simulation.

### Chemistry

- **Molecular Modeling**: PDB/PubChem loaders and molecular graph representations.
- **Periodic Table**: Comprehensive data for elements and stoichiometry.
- **Bio-Chemistry**: Specialized models for enzymatic reactions and metabolic pathways.

### Biology

- **Genetics**: DNA/RNA sequence analysis and GenBank integration.
- **Taxonomy**: GBIF and NCBI taxonomy backbone connectors.
- **Ecology**: Lotka-Volterra population dynamics and niche modeling.

### Earth & Medicine

- **Earth Sciences**: Meteorology (OpenWeather), Seismology (USGS), and Geodetic coordinates.
- **Medicine**: Vital sign monitoring simulations and medical data loaders.

---

## Social Sciences (jscience-social)

### Economics & Geography

- **Economics**: Financial market simulations and WorldBank data integration.
- **Geography**: GIS primitives, map projections, and demographic analysis.

### Humanities & Behavioral Sciences

- **Sociology**: Social network analysis and organizational modeling.
- **Psychology**: Behavioral simulation models.
- **Linguistics**: Natural language processing helpers.
- **Other Domains**: Arts, History, Law, Philosophy, Politics, and even Sports ballistics.

---

## Developer Experience

- **Interactive Demos**: A comprehensive collection of JavaFX-based demos covering multiple domains.
- **Master Launcher**: Unified UI to explore all scientific demos (`JScienceDemoApp`).
- **Benchmarking Suite**: Integrated JMH benchmarks with automatic chart generation.
- **Modern Java**: Built on Java 21 using latest language features for cleaner, safer code.
