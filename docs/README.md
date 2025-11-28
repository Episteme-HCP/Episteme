# JScience Documentation

This directory contains comprehensive documentation for the JScience library.

## Contents

### [mathematics_review.md](mathematics_review.md)
Complete review of mathematics package:
- **87 total features** (40 V1 + 47 V2)
- **100% coverage** of common mathematical computing
- Feature-by-feature comparison and implementation status
- Detailed capability matrix across all domains

### [ai_documentation_plan.md](ai_documentation_plan.md)
AI-friendly documentation strategy:
- Best practices for AI-readable Javadoc
- Human + AI dual-friendly approach
- Resource modernization plan (104 V1 resources)
- Modern data source recommendations (CODATA 2022, GBIF, NCBI)

## Quick Links

- [Main README](../README.md)
- [API Documentation](../target/site/apidocs/) (generated)
- [Source Code](../src/main/java/)

## Mathematics Package Highlights

**Linear Algebra**: LU, QR, SVD, Eigen, Cholesky, CG, GMRES, BiCGSTAB  
**Optimization**: Gradient descent, Newton, Simplex, Genetic, PSO, Simulated annealing  
**Statistics & ML**: Distributions, tests, ANOVA, k-means, PCA, Logistic regression  
**Graph Algorithms**: Dijkstra, A*, Bellman-Ford, Floyd-Warshall, Prim, Kruskal, MaxFlow, Hungarian  
**Numerical Analysis**: Integration, interpolation, root finding, ODE/PDE solvers  
**Special Functions**: Gamma, Beta, Bessel, Erf  
**Signal Processing**: FFT, DFT, Wavelets, Digital filters  
**Computational Geometry**: Convex hull, Delaunay/Voronoi, closest pair  
**Cryptography**: RSA, ECC, Hash functions  
**Symbolic Math**: Expression trees, polynomial algebra, factorization  
**Time Series**: MA/EMA, AR/MA models, seasonal decomposition  

## Contributing

When adding new features:
1. Follow AI-friendly documentation practices (see [ai_documentation_plan.md](ai_documentation_plan.md))
2. Use `Real` types (no primitives)
3. Add comprehensive Javadoc with examples
4. Update `mathematics_review.md` with new capabilities
5. Write unit tests
6. Commit with descriptive messages

## Resources

- **Physical Constants**: CODATA 2022 values in `org.jscience.physics.constants.PhysicalConstants`
- **Science Taxonomy**: 600+ sciences in `resources/org/jscience/sciences.xml`
- **Historical Data**: Nobel/Fields winners, discoveries, chronologies
- **Geographic Data**: Countries, timezones, world heritage sites
