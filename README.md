# Episteme Reimagined

**A unified scientific computing framework where all sciences naturally build upon their mathematical foundations.**

[![Javadoc](https://img.shields.io/badge/Javadoc-Reference-blue)](https://silveremartin-dev.github.io/Episteme/)
[![Demos](https://img.shields.io/badge/Demos-59-green)](README.md#demo-applications)
[![I18n](https://img.shields.io/badge/Languages-EN%20|%20FR%20|%20ES%20|%20DE-orange)](README.md#internationalization)

```text
Mathematics → Physics → Chemistry → Biology → Human Sciences
```

## Vision

Episteme Reimagined creates an integrated API where:

- A **biologist** extends biology classes and automatically gets chemistry, physics, and mathematics
- A **chemist** works with molecules and automatically gets quantum mechanics and statistical analysis
- All sciences respect their natural hierarchy and interdependencies

### Example: DNA Analysis

```java
// Biologist writes simple code
DNASequence dna = new DNASequence("ACGTACGT");

// Automatically gets:
Molecule[] molecules = dna.getMolecules();        // Chemistry layer
dna.simulateFolding(temperature);                 // Physics layer (molecular dynamics)
Statistics stats = dna.computeAlignmentStats();   // Mathematics layer
```

## Key Features

- ✅ **Scientific Hierarchy**: Each science layer inherits capabilities from below
- ✅ **Flexible Precision**: Switch between double, BigDecimal, or GPU types
- ✅ **Dynamic Optimization**: Matrices auto-select sparse, dense, triangular, or GPU backends
- ✅ **JSR-385 Integration**: Full units of measurement support
- ✅ **Modern Java 21**: Records, pattern matching, value types (when available)
- ✅ **GPU Support**: CUDA backends for intensive computations
- ✅ **Data Processing**: Import/Export support for JSON (Chemistry, etc.)
- ✅ **Benchmarking**: Built-in JMH benchmarks and Graph generation
- ✅ **Complete Documentation**: Javadoc, architecture guides, examples
- ✅ **Internationalization**: EN, FR (100% translated)
- ✅ **59 Interactive Demos**: Physics, Chemistry, Biology, Social Sciences

## 🚀 High Performance & Distributed Computing (HPC)

Episteme now includes native optimizations for heavy workloads:
- **SIMD Acceleration**: Vector API (Incubator) utilization for AVX-512/NEON instructions in Matrix operations.
- **Native BLAS**: Automatic bonding with OpenBLAS / MKL via Project Panama (Foreign Function & Memory API) for maximum CPU throughput.
- **MPI Integration**: Distributed context capable of scaling from local simulation to true MPI clusters (OpenMPI/MPICH) without code changes.

## Quick Start

```bash
git clone https://github.com/silveremartin-dev/Episteme
cd episteme
mvn clean install
```

```java
// Fast doubles (default)
Matrix<Double> m = Matrix.create(data, new DoubleScalar());
Matrix<Double> result = m.multiply(other);  // Auto-optimized

// Exact precision when needed
Matrix<BigDecimal> exact = Matrix.create(data, new ExactScalar());

// GPU acceleration
Matrix<CudaFloat> gpu = Matrix.create(data, new CudaScalar());
// Same API, 100x faster!
```

## ☁️ Cloud Deployment (GCP GPU)

You can launch a completely pre-configured Google Cloud Shell environment to deploy Episteme on an NVIDIA GPU Virtual Machine with one click:

[![Open in Cloud Shell](https://gstatic.com/cloudssh/images/open-btn.svg)](https://console.cloud.google.com/cloudshell/editor?cloudshell_git_repo=https://github.com/silveremartin-dev/Episteme&cloudshell_run_command=echo%20%22Ready%20to%20deploy%20Episteme!%20See%20gcp_deployment_plan.md%20for%20the%20gcloud%20command.%22)

## Project Status

**Phase 1-14: Core, Cleanup, Benchmarks & UI** (Completed)

- Algebraic structures & Linear Algebra (Dense/Sparse/GPU)
- Physics (Astronomy, Mechanics) & Chemistry (Periodic Table)
- Comprehensive Cleanup & Benchmarking Suite
- Full I18n support (EN/FR) with TestFX UI testing
- **Server Migration**: `episteme-server` migrated to **Spring Boot 3.2** (gRPC, REST, JPA, Vault)

## Module Structure

```text
episteme/
├── episteme-core/          # Mathematics, I/O, common utilities
│   ├── mathematics/        # Linear algebra, calculus, statistics
│   ├── measure/            # Quantities, units (JSR-385)
│   ├── bibliography/       # Citation management, CrossRef
│   └── ui/                 # Demo launcher, Matrix viewers
├── episteme-natural/       # Natural sciences (34 demos)
│   ├── physics/            # Mechanics, thermodynamics, astronomy
│   ├── chemistry/          # Molecules, reactions, biochemistry
│   ├── biology/            # Genetics, evolution, ecology
│   └── earth/              # Geology, meteorology, coordinates
├── episteme-social/        # Social sciences (11 demos)
│   ├── economics/          # Markets, currencies, models
│   ├── geography/          # GIS, maps, demographics
│   └── sociology/          # Networks, organizations
├── episteme-killer-apps/   # Advanced applications (10 demos)
│   ├── biology/            # CRISPR Design, Pandemic Forecaster
│   ├── physics/            # Quantum Circuits, Relativity
│   └── chemistry/          # Titration, Crystal Structure
└── episteme-benchmarks/    # JMH performance benchmarks
```

## Demo Applications

**59 interactive scientific demonstrations** across 4 modules:

| Module | Demos | Examples |
| --- | --- | --- |
| episteme-core | 4 | Matrix Viewer, Function Plotter, 3D Surfaces |
| episteme-natural | 34 | Mandelbrot, Game of Life, Stellar Sky, Pendulum |
| episteme-social | 11 | GIS Maps, Voting Systems, GDP Models |
| episteme-killer-apps | 10 | CRISPR, Quantum Circuits, Pandemic Forecaster |

### Launch Demo Launcher

```bash
# From project root
mvn exec:java -pl episteme-core -Dexec.mainClass="org.episteme.core.ui.EpistemeDemosApp"

# Or use batch script
run_demos.bat
```


## Running Examples

### Linear Algebra (Smart Dispatch)
```bash
mvn exec:java -pl episteme-featured-apps -Dexec.mainClass="org.episteme.examples.linearalgebra.LinearAlgebraExample"
```

### Audio Analysis (TarsosDSP)
```bash
mvn exec:java -pl episteme-featured-apps -Dexec.mainClass="org.episteme.examples.audio.AudioAnalysisExample" -Dexec.args="path/to/audio.wav"
```

### Physics Simulation (Native Bullet/Jolt)
```bash
mvn exec:java -pl episteme-featured-apps -Dexec.mainClass="org.episteme.examples.physics.PhysicsSimulationExample"
```


## Running Examples

### Linear Algebra (Smart Dispatch)
```bash
mvn exec:java -pl episteme-featured-apps -Dexec.mainClass="org.episteme.examples.linearalgebra.LinearAlgebraExample"
```

### Audio Analysis (TarsosDSP)
```bash
mvn exec:java -pl episteme-featured-apps -Dexec.mainClass="org.episteme.examples.audio.AudioAnalysisExample" -Dexec.args="path/to/audio.wav"
```

### Physics Simulation (Native Bullet/Jolt)
```bash
mvn exec:java -pl episteme-featured-apps -Dexec.mainClass="org.episteme.examples.physics.PhysicsSimulationExample"
```

## Data Loaders

External data sources with built-in caching (TTL: 24h):

| Module | Loaders |
| --- | --- |
| Astronomy | `NasaExoplanets`, `SimbadLoader`, `SimbadCatalog` |
| Biology | `GbifTaxonomy`, `GenBank`, `NcbiTaxonomy` |
| Chemistry | `PubChem` |
| Earth | `OpenWeather`, `UsgsEarthquakes` |
| Economics | `WorldBank` |
| Bibliography | `CrossRef` |

## Testing

### Unit Tests

```bash
mvn test
```

### UI Tests (TestFX)

```bash
# With display
mvn test -pl episteme-core -Dtest=*UITest,*DemoAppTest

# Headless mode (CI/CD)
mvn test -Dtestfx.headless=true -Dprism.order=sw -Djava.awt.headless=true
```

## Internationalization

Full support for English, French, Spanish, and German:

| Module | EN | FR |
| --- | --- | --- |
| episteme-core | ✅ | ✅ |
| episteme-natural | ✅ | ✅ |
| episteme-social | ✅ | ✅ |
| episteme-killer-apps | ✅ | ✅ |

Switch language via `Preferences > Language` menu.

## Architecture

See [ARCHITECTURE.md](docs/ARCHITECTURE.md) for complete design.
[Architecture Diagrams (Mermaid)](docs/mermaid/README.md)

**Core Principles:**

1. **Performance First**: Primitives by default, objects when needed
2. **Scientific Accuracy**: Respect mathematical and physical concepts
3. **Ease of Use**: Domain scientists shouldn't need to know lower layers
4. **Flexibility**: Switch precision/backends without code changes

## Documentation

- 📚 **Online API Javadoc**: [https://silveremartin-dev.github.io/Episteme/](https://silveremartin-dev.github.io/Episteme/)
- [Architecture Guide](docs/ARCHITECTURE.md)
- [Mathematical Concepts](docs/MATH_CONCEPTS.md)
- [API Reference](docs/API.md)
- [Examples](docs/EXAMPLES.md)
- [Contributing Guide](CONTRIBUTING.md)

## Requirements

- Java 21+
- Maven 3.8+
- (Optional) CUDA Toolkit 12.0+ for GPU support

## License

MIT License - see [LICENSE](LICENSE) file.

## Credits

- **Original Vision**: Silvere Martin-Michiellot
- **Implementation**: Gemini AI (Google DeepMind)
- **Inspired by**: Episteme (Jean-Marie Dautelle et al.)

## Contributing

We welcome contributions! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

Areas we need help:

- Physics layer implementation
- Chemistry domain expertise
- Biology algorithms
- Performance optimization
- Documentation translations

---

*Episteme Reimagined - Making scientific computing natural.*
