# AI-Friendly Documentation & Resource Modernization Plan

**Date**: 2025-11-28  
**Status**: PLANNING  
**Impact**: Foundation for AI code navigation & data accessibility

---

## 🎯 Objectives

1. **AI-Friendly Code Documentation** - Custom Javadoc tags for AI navigation
2. **Resource Modernization** - Convert 104 V1 resources to modern formats
3. **Metadata Enrichment** - Add structured data for AI processing
4. **API Elevation** - Make resources programmatically accessible

---

## Part 1: AI-Friendly Documentation Patterns

### **Philosophy**: Make code "readable" for AI assistants

### 1.1 Custom Javadoc Tags

```java
/**
 * Matrix decomposition for solving linear systems.
 * 
 * @ai.category Linear Algebra / Matrix Decomposition
 * @ai.complexity O(n³/3) for decomposition, O(n²) for solving
 * @ai.usecase Solve Ax=b, compute determinant, matrix inverse
 * @ai.prerequisites Matrix must be symmetric positive definite
 * @ai.seealso LUDecomposition, QRDecomposition
 * @ai.example
 * <pre>
 * Matrix<Real> A = ...; // SPD matrix
 * CholeskyDecomposition chol = CholeskyDecomposition.decompose(A);
 * Real[] x = chol.solve(b);
 * </pre>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class CholeskyDecomposition {
```

#### Proposed Custom Tags:

| Tag | Purpose | Example |
|-----|---------|---------|
| `@ai.category` | Classify algorithmic domain | `Linear Algebra / Sparse Solvers` |
| `@ai.complexity` | Algorithm complexity | `O(n log n)`, `O(E + V log V)` |
| `@ai.usecase` | Primary applications | `Image compression, noise reduction` |
| `@ai.prerequisites` | Input requirements | `Matrix must be square and invertible` |
| `@ai.alternatives` | Related approaches | `Use SVD for rank-deficient matrices` |
| `@ai.performance` | Runtime characteristics | `Fast for n<1000, use iterative for n>10000` |
| `@ai.numerics` | Numerical stability notes | `Prone to roundoff for ill-conditioned matrices` |
| `@ai.parallelizable` | Concurrency potential | `Yes - embarrassingly parallel` |
| `@ai.gpu` | GPU implementation status | `Available in CudaLinearAlgebraProvider` |

### 1.2 Method-Level AI Hints

```java
/**
 * Solves Ax = b using Cholesky factorization.
 * 
 * @ai.steps
 * 1. Forward substitution: Ly = b
 * 2. Backward substitution: L^T x = y
 * 
 * @ai.invariants
 * - Matrix must remain SPD throughout
 * - Solution exists iff b is in column space of A
 * 
 * @param b right-hand side vector
 * @return solution vector x
 */
public Real[] solve(Real[] b)
```

### 1.3 Package-Level AI Metadata

Create `package-info.java` with AI hints:

```java
/**
 * Linear algebra decompositions and solvers.
 * 
 * @ai.domain Numerical Linear Algebra
 * @ai.dependencies org.jscience.mathematics.vector, org.jscience.mathematics.number
 * @ai.features
 * - Matrix decompositions (LU, QR, SVD, Eigen, Cholesky)
 * - Direct solvers (Gaussian elimination, decomposition-based)
 * - Iterative solvers (CG, GMRES, BiCGSTAB)
 * 
 * @ai.gpu Some features GPU-accelerated via CudaLinearAlgebraProvider
 */
package org.jscience.mathematics.linear;
```

---

## Part 2: Resource Modernization

### 2.1 Current Resource Inventory (104 files)

**Found Resources**:
- **Sciences**: `sciences.xml` (2437 lines, 600+ sciences)
- **History**: Nobel/Fields winners, discoveries, chronology, biographies
- **Geography**: World heritage sites, paleomaps (600Ma-present)
- **Psychology**: DSM-IV classifications
- **Politics**: ISO 3166 countries, timezones, IDC codes
- **Chemistry**: (Mentioned in todo.txt - needs periodic table update)
- **Documentation**: Coding recommendations, TODO lists

### 2.2 Modernization Strategy

#### A. Convert XML to JSON (Better for AI parsing)

**Before (sciences.xml)**:
```xml
<science>
    <name>acarology</name>
    <description>study of mites</description>
</science>
```

**After (sciences.json)**:
```json
{
  "sciences": [
    {
      "id": "acarology",
      "name": "Acarology",
      "description": "Study of mites",
      "category": "Zoology / Arachnology",
      "related": ["entomology", "ecology"],
      "ai_tags": ["biology", "arthropods", "pest_control"]
    }
  ]
}
```

#### B. Add Structured Metadata

**Enhanced Resource Structure**:
```json
{
  "metadata": {
    "version": "2.0",
    "last_updated": "2025-11-28",
    "source": "Stephen Chrisomalis + JScience extensions",
    "license": "CC-BY-4.0",
    "ai_indexed": true,
    "categories": ["biology", "physics", "chemistry"...]
  },
  "data": [...]
}
```

#### C. Create Java API for Resources

```java
/**
 * Programmatic access to sciences database.
 * 
 * @ai.category Knowledge Base / Sciences
 * @ai.datasize 600+ sciences from 100+ domains
 */
public class SciencesDatabase {
    
    /** @ai.lazy Loaded on first access */
    private static final Map<String, Science> SCIENCES = loadSciences();
    
    /**
     * Find science by name or keyword.
     * @ai.complexity O(1) for exact match, O(n) for search
     */
    public static Optional<Science> find(String keyword);
    
    /**
     * Get all sciences in category.
     * @ai.example findByCategory("physics") → [astrophysics, geophysics...]
     */
    public static List<Science> findByCategory(String category);
}
```

### 2.3 Priority Resources to Modernize

| Priority | Resource | Description | Impact |
|----------|----------|-------------|--------|
| **P0** | `sciences.xml` | 600+ sciences | High - foundational knowledge |
| **P0** | `PhysicalConstants` | Already done ✅ | Complete |
| **P1** | `nobelwinners.xml` | Historical data | Medium - AI training data |
| **P1** | `iso3166.txt` | Countries | Medium - geography |
| **P2** | `dsmiv.xml` | Psychology classifications | Low - specialized |
| **P2** | `paleomaps/*.jpg` | Geological history | Low - visualization |

---

## Part 3: Implementation Plan

### Phase 1: Documentation Enhancement (2-3 hours)

**Step 1.1**: Create AI-friendly Javadoc taglet
```java
// Custom taglet processor for @ai.* tags
public class AITaglet extends BaseTaglet {
    // Process and format AI hints
}
```

**Step 1.2**: Document 10 flagship classes
- [MODIFY] CholeskyDecomposition
- [MODIFY] BiCGSTAB  
- [MODIFY] LogisticRegression
- [MODIFY] DelaunayTriangulation
- [MODIFY] TimeSeries
- [MODIFY] PolynomialFactorization
- [MODIFY] DigitalFilters
- [MODIFY] SVD
- [MODIFY] EigenDecomposition
- [MODIFY] FFT

**Step 1.3**: Update package-info.java for top packages
- org.jscience.mathematics.linear
- org.jscience.mathematics.ml
- org.jscience.mathematics.optimization

### Phase 2: Resource Modernization (4-6 hours)

**Step 2.1**: Convert sciences.xml → sciences.json
- Parse 600+ sciences
- Add categories & AI tags
- Create SciencesDatabase Java API

**Step 2.2**: Modernize historical data
- Nobel winners JSON
- Timeline JSON with dates/categories

**Step 2.3**: Geography resources
- ISO 3166 countries → structured JSON
- Timezones with metadata

### Phase 3: API Access Layer (2-3 hours)

**Step 3.1**: Create resource package
```
org.jscience.resources/
├── SciencesDatabase.java
├── HistoricalEventsDatabase.java
├── CountriesDatabase.java
└── data/
    ├── sciences.json
    ├── nobel_winners.json
    └── countries.json
```

**Step 3.2**: Lazy loading & caching
- ResourceLoader utility
- JSON parsing with Gson/Jackson
- In-memory cache

---

## Part 4: AI-Friendly Principles Applied

### ✅ Principles from JScience coding_recommendations.txt:

1. **Use standards** → JSON (industry standard)
2. **XML support** → JSON for data, XML for legacy compat
3. **Standalone objects** → Resource databases are self-contained
4. **Think readability** → AI-parseable structure
5. **Think for others** → Extensible JSON schema
6. **Use long names** → Descriptive field names
7. **Provide deep documentation** → @ai.* tags

### 🆕 Additional AI-Friendly Principles:

8. **Searchability** → Tags, categories, keywords
9. **Discoverability** → Cross-references between related concepts
10. **Learnability** → Examples in every doc block
11. **Explainability** → Algorithm steps, invariants, complexity
12. **Toolability** → JSON schema for validation
13. **Queryability** → Programmatic API access
14. **Version clarity** → Metadata with versions/timestamps

---

## User Review Required

> [!IMPORTANT]  
> **Custom Javadoc Tags Decision**
> 
> Do you want custom `@ai.*` tags? This requires:
> - Custom taglet processor
> - Modified Javadoc generation
> - Benefits: AI-friendly hints, better navigation
> - Costs: ~4 hours implementation, maintains compatibility

> [!IMPORTANT]  
> **Resource Modernization Scope**
> 
> Which resources to prioritize?
> 1. **All 104 files** (~20 hours)
> 2. **Top 10 priority** (sciences, physics, history) (~6 hours)
> 3. **Just sciences.xml** (~2 hours)

---

## Verification Plan

### Phase 1 Verification:
- [ ] Javadoc builds without errors
- [ ] AI tags render in HTML
- [ ] Examples are executable

### Phase 2 Verification:
- [ ] JSON validates against schema
- [ ] All 600+ sciences parse correctly
- [ ] Database APIs work

### Phase 3 Verification:
- [ ] Resources load on demand
- [ ] Search performance < 10ms
- [ ] Memory usage reasonable

---

## Timeline

| Phase | Duration | Output |
|-------|----------|--------|
| Phase 1 | 2-3 hrs | 10 classes documented |
| Phase 2 | 4-6 hrs | JSON resources |
| Phase 3 | 2-3 hrs | Java APIs |
| **Total** | **8-12 hrs** | AI-friendly codebase |

---

## Next Steps

1. Get user approval on scope
2. Implement custom taglet (if approved)
3. Document flagship classes
4. Convert priority resources
5. Create database APIs
6. Test & verify
