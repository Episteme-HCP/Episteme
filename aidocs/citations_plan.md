# Comprehensive Academic Citations Plan

**Created**: 2025-11-27  
**Purpose**: Add scholarly references to ALL relevant JScience classes  
**Scope**: Mathematics, algorithms, algebras, transforms, geometry, logic, physics

---

## Citation Format Standard

Use Javadoc `@see` tags with full citations:

```java
/**
 * [Description of the class/algorithm]
 * 
 * @see Author Name, "Title", Journal/Book, Year
 * @see Author et al., "Title", Conference/Publisher, Year, DOI: xxx
 * 
 * @author [Implementer]
 * @since 1.0
 */
```

---

## 1. Algebra Structures

### Boolean Algebra
- **Class**: `BooleanAlgebra.java`
- **Citation**: George Boole, "The Mathematical Analysis of Logic", 1847
- **Citation**: Claude Shannon, "A Symbolic Analysis of Relay and Switching Circuits", MIT Master's Thesis, 1937

### Kleene Algebra
- **Class**: `KleeneAlgebra.java`
- **Citation**: Stephen Cole Kleene, "Representation of Events in Nerve Nets and Finite Automata", 1956
- **Citation**: Dexter Kozen, "A Completeness Theorem for Kleene Algebras and the Algebra of Regular Events", Information and Computation, 1994

### Lattices
- **Class**: `Lattice.java`, `SemiLattice.java`
- **Citation**: Garrett Birkhoff, "Lattice Theory", American Mathematical Society, 1940

### Group Theory
- **Classes**: Various group implementations
- **Citation**: Évariste Galois, "Mémoire sur les conditions de résolubilité des équations par radicaux", 1832
- **Citation**: Emmy Noether, "Idealtheorie in Ringbereichen", 1921

---

## 2. Transforms & Signal Processing

### Fast Fourier Transform (FFT)
- **Class**: `FastFourierTransform.java`
- **Citation**: James W. Cooley and John W. Tukey, "An Algorithm for the Machine Calculation of Complex Fourier Series", Mathematics of Computation, Vol. 19, No. 90, April 1965, pp. 297-301
- **Citation**: Carl Friedrich Gauss, Original work circa 1805 (rediscovered)

### Discrete Cosine Transform (DCT)
- **Class**: `DiscreteCosineTransform.java`
- **Citation**: Nasir Ahmed, T. Natarajan, and K. R. Rao, "Discrete Cosine Transform", IEEE Transactions on Computers, 1974

### Wavelet Transform
- **Class**: `WaveletTransform.java`
- **Citation**: Ingrid Daubechies, "Ten Lectures on Wavelets", SIAM, 1992
- **Citation**: Stéphane Mallat, "A Theory for Multiresolution Signal Decomposition", IEEE PAMI, 1989

### Laplace Transform
- **Class**: `LaplaceTransform.java`
- **Citation**: Pierre-Simon Laplace, "Théorie Analytique des Probabilités", 1812

---

## 3. Graph Algorithms

### Dijkstra's Algorithm
- **Class**: `GraphAlgorithms.java` (dijkstra method)
- **Citation**: Edsger W. Dijkstra, "A Note on Two Problems in Connexion with Graphs", Numerische Mathematik, Vol. 1, 1959, pp. 269-271

### Kruskal's Algorithm
- **Class**: `GraphAlgorithms.java` (kruskal method)
- **Citation**: Joseph Kruskal, "On the Shortest Spanning Subtree of a Graph", Proceedings of the AMS, Vol. 7, No. 1, 1956, pp. 48-50

### Prim's Algorithm
- **Class**: `GraphAlgorithms.java` (prim method)
- **Citation**: Robert C. Prim, "Shortest Connection Networks and Some Generalizations", Bell System Technical Journal, Vol. 36, 1957, pp. 1389-1401

### Floyd-Warshall
- **Class**: `GraphAlgorithms.java` (floydWarshall method)
- **Citation**: Robert W. Floyd, "Algorithm 97: Shortest Path", Communications of the ACM, Vol. 5, No. 6, 1962, p. 345

### Bellman-Ford
- **Class**: `GraphAlgorithms.java` (bellmanFord method)
- **Citation**: Richard Bellman, "On a Routing Problem", Quarterly of Applied Mathematics, Vol. 16, No. 1, 1958, pp. 87-90

---

## 4. Number Theory

### Primality Testing (Miller-Rabin)
- **Class**: `Primes.java` (isProbablePrime method)
- **Citation**: Gary L. Miller, "Riemann's Hypothesis and Tests for Primality", Journal of Computer and System Sciences, Vol. 13, No. 3, 1976, pp. 300-317
- **Citation**: Michael O. Rabin, "Probabilistic Algorithm for Testing Primality", Journal of Number Theory, Vol. 12, No. 1, 1980, pp. 128-138

### Sieve of Eratosthenes
- **Class**: `Primes.java` (sieve method)
- **Citation**: Eratosthenes of Cyrene, circa 240 BCE (ancient algorithm)

### Euclidean Algorithm (GCD)
- **Class**: `IntegerMath.java` or `NumberTheory.java`
- **Citation**: Euclid, "Elements", Book VII, circa 300 BCE

### Fermat's Little Theorem
- **Class**: Number theory utilities
- **Citation**: Pierre de Fermat, Letter to Frénicle de Bessy, 1640

---

## 5. Statistics & Probability

### Box-Muller Transform
- **Class**: `RandomDistributions.java` or `Statistics.java`
- **Citation**: George E. P. Box and Mervin E. Muller, "A Note on the Generation of Random Normal Deviates", The Annals of Mathematical Statistics, Vol. 29, No. 2, 1958, pp. 610-611

### Central Limit Theorem
- **Class**: `Statistics.java`
- **Citation**: Pierre-Simon Laplace, "Théorie Analytique des Probabilités", 1812

### Gaussian Distribution
- **Class**: `GaussianDistribution.java`
- **Citation**: Carl Friedrich Gauss, "Theoria combinationis observationum erroribus minimis obnoxiae", 1809

---

## 6. Chaos Theory & Dynamical Systems

### Logistic Map (DONE ✓)
- **Class**: `LogisticMap.java`
- **Citation**: Robert M. May, "Simple mathematical models with very complicated dynamics", Nature, Vol. 261, 1976, pp. 459-467

### Hénon Map
- **Class**: `HenonMap.java`
- **Citation**: Michel Hénon, "A two-dimensional mapping with a strange attractor", Communications in Mathematical Physics, Vol. 50, 1976, pp. 69-77

### Mandelbrot Set
- **Class**: `MandelbrotSet.java`
- **Citation**: Benoit Mandelbrot, "The Fractal Geometry of Nature", W.H. Freeman, 1982
- **Citation**: Adrien Douady and John H. Hubbard, "Étude dynamique des polynômes complexes", 1984-1985

### Julia Sets
- **Class**: `JuliaSet.java`
- **Citation**: Gaston Julia, "Mémoire sur l'itération des fonctions rationnelles", Journal de Mathématiques Pures et Appliquées, Vol. 8, 1918, pp. 47-245

### Lorenz Attractor
- **Class**: `LorenzAttractor.java`
- **Citation**: Edward N. Lorenz, "Deterministic Nonperiodic Flow", Journal of the Atmospheric Sciences, Vol. 20, 1963, pp. 130-141

---

## 7. Logic Systems

### Propositional Logic
- **Class**: `BooleanLogic.java`
- **Citation**: Gottlob Frege, "Begriffsschrift", 1879
- **Citation**: Alfred North Whitehead and Bertrand Russell, "Principia Mathematica", 1910-1913

### Modal Logic
- **Class**: `ModalLogic.java`
- **Citation**: Saul Kripke, "Semantical Considerations on Modal Logic", Acta Philosophica Fennica, Vol. 16, 1963, pp. 83-94
- **Citation**: C. I. Lewis, "A Survey of Symbolic Logic", 1918

### Temporal Logic (LTL/CTL)
- **Class**: `TemporalLogic.java`
- **Citation**: Amir Pnueli, "The Temporal Logic of Programs", 18th Annual Symposium on Foundations of Computer Science, IEEE, 1977, pp. 46-57

### Fuzzy Logic
- **Class**: `FuzzyLogic.java`
- **Citation**: Lotfi A. Zadeh, "Fuzzy Sets", Information and Control, Vol. 8, No. 3, 1965, pp. 338-353

### Three-Valued Logic
- **Class**: `ThreeValuedLogic.java`
- **Citation**: Jan Łukasiewicz, "O logice trójwartościowej" (On Three-Valued Logic), Ruch Filozoficzny, Vol. 5, 1920, pp. 170-171

---

## 8. Combinatorics

### Catalan Numbers
- **Class**: `CatalanSequence.java`, `Combinatorics.catalan()`
- **Citation**: Eugène Charles Catalan, "Note sur une équation aux différences finies", Journal de Mathématiques Pures et Appliquées, Vol. 3, 1838, pp. 508-516

### Fibonacci Sequence
- **Class**: `FibonacciSequence.java`
- **Citation**: Leonardo Fibonacci (Leonardo of Pisa), "Liber Abaci", 1202

### Binomial Coefficients
- **Class**: `Combinatorics.binomial()`
- **Citation**: Blaise Pascal, "Traité du triangle arithmétique", 1665

---

## 9. Geometry

### Euclidean Geometry
- **Citation**: Euclid, "Elements", circa 300 BCE

### Computational Geometry Algorithms
- **Class**: Various geometric classes
- **Citation**: Franco P. Preparata and Michael Ian Shamos, "Computational Geometry: An Introduction", Springer-Verlag, 1985

---

## 10. Linear Algebra

### Matrix Decompositions
- **Classes**: LU, QR, SVD, Cholesky
- **Citation**: Carl Gustav Jacob Jacobi, "Über ein leichtes Verfahren", 1846 (eigenvalues)
- **Citation**: Alston Scott Householder, "Unitary Triangularization of a Nonsymmetric Matrix", Journal of the ACM, Vol. 5, No. 4, 1958

---

## Implementation Priority

1. ✅ Chaos: LogisticMap (DONE)
2. 🔄 Algebras: Kleene, Boolean, Lattice
3. 🔄 Transforms: FFT, DCT, Wavelet
4. 🔄 Graph Algorithms: Dijkstra, Kruskal, Floyd-Warshall
5. 🔄 Number Theory: Miller-Rabin, Euclidean Algorithm
6. 🔄 Logic: Modal, Temporal, Fuzzy
7. 🔄 Chaos: Hénon, Mandelbrot, Julia, Lorenz
8. 🔄 Statistics: Box-Muller, distributions
9. 🔄 Combinatorics: Catalan, binomial

---

## Citation Template

```java
 * <p>
 * <b>References:</b>
 * <ul>
 * <li>Author Name, "Paper Title", Journal/Conference, Vol. X, Year, pp. Y-Z</li>
 * <li>Author Name, "Book Title", Publisher, Year</li>
 * </ul>
 * </p>
```
