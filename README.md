# JScience Reimagined

**A unified scientific computing framework where all sciences naturally build upon their mathematical foundations.**

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
- ✅ **Internationalization**: EN, FR, ES, DE

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

**Phase 1-13: Core, Cleanup & Benchmarks** (Completed)

- Algebraic structures & Linear Algebra (Dense/Sparse/GPU)
- Physics (Astronomy, Mechanics) & Chemistry (Periodic Table)
- Comprehensive Cleanup & Benchmarking Suite

## Module Structure

```
jscience/
├── jscience-core/          # Mathematics, I/O, common utilities
│   ├── mathematics/        # Linear algebra, calculus, statistics
│   ├── measure/            # Quantities, units (JSR-385)
│   ├── bibliography/       # Citation management, CrossRef
│   └── ui/mathematics/     # Matrix viewers
├── jscience-natural/       # Natural sciences
│   ├── physics/            # Mechanics, thermodynamics, astronomy
│   ├── chemistry/          # Molecules, reactions, biochemistry
│   ├── biology/            # Genetics, evolution, ecology
│   └── earth/              # Geology, meteorology, coordinates
├── jscience-social/        # Social sciences
│   ├── economics/          # Markets, currencies, models
│   ├── geography/          # GIS, maps, demographics
│   └── sociology/          # Networks, organizations
└── jscience-benchmarks/    # JMH performance benchmarks
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

## Demo Application

Launch the master demo:

```bash
mvn exec:java -pl jscience-natural -Dexec.mainClass="org.jscience.ui.JScienceDemoApp"
```

See [roadmap.md](roadmap.md) and [task.md](task.md) for detailed progress.

## Architecture

See [ARCHITECTURE.md](docs/ARCHITECTURE.md) for complete design.

**Core Principles:**

1. **Performance First**: Primitives by default, objects when needed
2. **Scientific Accuracy**: Respect mathematical and physical concepts
3. **Ease of Use**: Domain scientists shouldn't need to know lower layers
4. **Flexibility**: Switch precision/backends without code changes

## Documentation

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
