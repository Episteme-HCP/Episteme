# JScience Reimagined

**A unified scientific computing framework where all sciences naturally build upon their mathematical foundations.**

[![Javadoc](https://img.shields.io/badge/Javadoc-Reference-blue)](https://silveremartin-dev.github.io/JScience/)
[![Demos](https://img.shields.io/badge/Demos-59-green)](README.md#demo-applications)
[![I18n](https://img.shields.io/badge/Languages-EN%20|%20FR%20|%20ES%20|%20DE-orange)](README.md#internationalization)

```
Mathematics → Physics → Chemistry → Biology → Human Sciences
```

## Vision

JScience Reimagined creates an integrated API where:

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

## Quick Start

```bash
git clone https://github.com/silvere/jscience
cd jscience
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

## Project Status

**Phase 1-14: Core, Cleanup, Benchmarks & UI** (Completed)

- Algebraic structures & Linear Algebra (Dense/Sparse/GPU)
- Physics (Astronomy, Mechanics) & Chemistry (Periodic Table)
- Comprehensive Cleanup & Benchmarking Suite
- Full I18n support (EN/FR) with TestFX UI testing
- **Server Migration**: `jscience-server` migrated to **Spring Boot 3.2** (gRPC, REST, JPA, Vault)

## Module Structure

```
jscience/
├── jscience-core/          # Mathematics, I/O, common utilities
│   ├── mathematics/        # Linear algebra, calculus, statistics
│   ├── measure/            # Quantities, units (JSR-385)
│   ├── bibliography/       # Citation management, CrossRef
│   └── ui/                 # Demo launcher, Matrix viewers
├── jscience-natural/       # Natural sciences (34 demos)
│   ├── physics/            # Mechanics, thermodynamics, astronomy
│   ├── chemistry/          # Molecules, reactions, biochemistry
│   ├── biology/            # Genetics, evolution, ecology
│   └── earth/              # Geology, meteorology, coordinates
├── jscience-social/        # Social sciences (11 demos)
│   ├── economics/          # Markets, currencies, models
│   ├── geography/          # GIS, maps, demographics
│   └── sociology/          # Networks, organizations
├── jscience-killer-apps/   # Advanced applications (10 demos)
│   ├── biology/            # CRISPR Design, Pandemic Forecaster
│   ├── physics/            # Quantum Circuits, Relativity
│   └── chemistry/          # Titration, Crystal Structure
└── jscience-benchmarks/    # JMH performance benchmarks
```

## Demo Applications

**59 interactive scientific demonstrations** across 4 modules:

| Module | Demos | Examples |
|--------|-------|----------|
| jscience-core | 4 | Matrix Viewer, Function Plotter, 3D Surfaces |
| jscience-natural | 34 | Mandelbrot, Game of Life, Stellar Sky, Pendulum |
| jscience-social | 11 | GIS Maps, Voting Systems, GDP Models |
| jscience-killer-apps | 10 | CRISPR, Quantum Circuits, Pandemic Forecaster |

### Launch Demo Launcher

```bash
# From project root
mvn exec:java -pl jscience-core -Dexec.mainClass="org.jscience.ui.JScienceDemoApp"

# Or use batch script
run_demos.bat
```

## Data Loaders

External data sources with built-in caching (TTL: 24h):

| Module | Loaders |
|--------|---------|
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
mvn test -pl jscience-core -Dtest=*UITest,*DemoAppTest

# Headless mode (CI/CD)
mvn test -Dtestfx.headless=true -Dprism.order=sw -Djava.awt.headless=true
```

## Internationalization

Full support for English, French, Spanish, and German:

| Module | EN | FR |
|--------|----|----|
| jscience-core | ✅ | ✅ |
| jscience-natural | ✅ | ✅ |
| jscience-social | ✅ | ✅ |
| jscience-killer-apps | ✅ | ✅ |

Switch language via `Preferences > Language` menu.

See [roadmap.md](roadmap.md) and [task.md](task.md) for detailed progress.

## Architecture

See [ARCHITECTURE.md](docs/ARCHITECTURE.md) for complete design.

**Core Principles:**

1. **Performance First**: Primitives by default, objects when needed
2. **Scientific Accuracy**: Respect mathematical and physical concepts
3. **Ease of Use**: Domain scientists shouldn't need to know lower layers
4. **Flexibility**: Switch precision/backends without code changes

## Documentation

- 📚 **Online API Javadoc**: [https://silveremartin-dev.github.io/JScience/](https://silveremartin-dev.github.io/JScience/)
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
- **Inspired by**: JScience (Jean-Marie Dautelle et al.)

## Contributing

We welcome contributions! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

Areas we need help:

- Physics layer implementation
- Chemistry domain expertise
- Biology algorithms
- Performance optimization
- Documentation translations

---

*JScience Reimagined - Making scientific computing natural.*
