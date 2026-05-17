---
title: Episteme Scientific Kernel
emoji: 🌌
colorFrom: blue
colorTo: indigo
sdk: docker
app_port: 8080
dockerfile: docker/Dockerfile.huggingface
---

# 🌌 Episteme: The Unified Scientific Computing Framework

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/Episteme-HCP/Episteme)
[![Java Version](https://img.shields.io/badge/Java-21%2B%20%2F%2025-blue)](https://www.oracle.com/java/technologies/downloads/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Built with Antigravity](https://img.shields.io/badge/Built%20with-Antigravity-6f42c1)](https://deepmind.google/technologies/antigravity/)

**Episteme** is a high-performance, modular, and comprehensive scientific computing library for Java. It reimagines JVM-based science by bridging the gap between low-level performance (C/C++) and high-level architectural elegance.

---

## 🚀 The Achievement
Developed over a relentless **4-month** sprint, Episteme comprises over **450,000+ lines** of production-ready code. This massive engineering undertaking was **built entirely with Antigravity**, demonstrating the power of agentic AI in scaling complex, science-first architectures.

---

## 🔭 The Concept: Science-First Engineering
Most libraries are "computer-oriented"—built around arrays and pointers. Episteme is **"science-oriented"**.
*   **Natural Hierarchy**: Our object model mirrors the real world. Mathematics is the base for Physics, which in turn powers Biology and the Social Sciences.
*   **Semantic Reusability**: Complex scientific concepts are readily available via deep object hierarchies, allowing you to build entire domain-specific applications in just a few prompts.
*   **HPC on par with C**: Leverages Java Panama (21+) and the Vector API for direct native performance with zero deployment overhead.

---

## ✨ Key Features
*   🏎️ **Blazing Fast**: Up to **15x faster** on double-precision operations than EJML or Apache Commons Math.
*   ♾️ **Infinite Precision**: Arbitrary-precision numbers (MPFR) and complex domains supported natively.
*   📦 **Modular & Thin**: Release modules are ~1MB; add only the dependencies and compute backends you need.
*   🧠 **Autotuning Backends**: Plug-and-play support for CUDA, OpenCL, SIMD, and OpenBLAS. Backends are put into "competition" to ensure the fastest execution for your specific hardware.
*   🌐 **Distributed Grid**: Integrated worker nodes and gRPC-ready client/server architecture for scaling jobs across entire clusters.
*   🛠️ **Ready-to-Use**: Includes tens of loaders, viewers, external device drivers, and a suite of demo apps.
*   🌍 **I18n Supported**: Fully localized in English, French, German, Spanish, and Chinese (ZN).

---

## 💼 Career Note
**I am currently looking for a full-time job.**  
If you are impressed by the scale and quality of Episteme and are looking for a dedicated software engineer with experience in high-performance computing and AI-driven development, please reach out via GitHub or [LinkedIn](https://www.linkedin.com/in/silv%C3%A8re-martin-michiellot-65b6a95/).

---

## 🛠️ Getting Started

### Installation
Add Episteme to your `pom.xml`:
```xml
<dependency>
    <groupId>io.github.episteme-hcp</groupId>
    <artifactId>episteme-core</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

### High-Precision Linear Algebra
```java
// QR Decomposition with 128-bit precision
Matrix<RealBig> A = Matrix.rand(100, 100, RealBig.RING);
QRResult<RealBig> qr = A.qr();
System.out.println("Residual: " + A.subtract(qr.Q().multiply(qr.R())).norm());
```

 
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
| episteme-featured-apps | 10 | CRISPR, Quantum Circuits, Pandemic Forecaster |

### Launch Demo Launcher

```bash
# From project root
mvn exec:java -pl episteme-core -Dexec.mainClass="org.episteme.core.ui.EpistemeDemosApp"

# Or use batch script
run_demos.bat
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

## Architecture

See [ARCHITECTURE.md](docs/ARCHITECTURE.md) for complete design.
[Architecture Diagrams (Mermaid)](docs/mermaid/README.md)

**Core Principles:**

1. **Performance First**: Primitives by default, objects when needed
2. **Scientific Accuracy**: Respect mathematical and physical concepts
3. **Ease of Use**: Domain scientists shouldn't need to know lower layers
4. **Flexibility**: Switch precision/backends without code changes

## Documentation

- 📚 **Online API Javadoc**: [https://episteme-hcp.github.io/Episteme/javadoc/index.html](https://episteme-hcp.github.io/Episteme/javadoc/index.html)
- [Architecture Guide](docs/ARCHITECTURE.md)
- [Mathematical Concepts](docs/VISION.md)
- [API Reference](docs/REST_API.md)
- [Examples](docs/EXAMPLES.md)
- [Contributing Guide](docs/CONTRIBUTING.md)

## Requirements

- **Java 25** (Required to compile the high-performance native backend module `episteme-native` due to finalized modern Panama FFM APIs).
- **Java 21+** (Compatible for building and running all core mathematical and social modules: `episteme-core`, `episteme-natural`, `episteme-social`, `episteme-database`, by excluding `episteme-native` via Maven reactor exclusions `-pl '!episteme-native,!episteme-server,!episteme-client,!episteme-worker,!episteme-demos,!episteme-featured-apps,!episteme-benchmarks'`).
- Maven 3.8+
- (Optional) CUDA Toolkit 12.0+ for GPU support

## License

MIT License - see [LICENSE](LICENSE) file.

## Credits

- **Original Vision**: Silvere Martin-Michiellot
- **Implementation**: Gemini AI (Google DeepMind)

## Contributing

We welcome contributions!---

*© 2025-2026 Silvere Martin-Michiellot. Built with Antigravity.*
