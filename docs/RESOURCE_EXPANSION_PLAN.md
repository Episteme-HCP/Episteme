# Resource Expansion & Caching Plan

**Status**: IMPLEMENTED
**Date**: 2025-11-28

## 1. Vision: "The World's Data, On Demand"

We are transforming JScience from a standalone library into a gateway for global scientific data.
**Strategy**: API Connectors + Local Caching + Future Object Mapping.

## 2. The Cache System

To prevent excessive API calls and enable offline work, we have implemented a robust caching layer.

### Implementation
*   **Package**: `org.jscience.resources.cache`
*   **Interface**: `ResourceCache`
*   **Implementation**: `FileResourceCache`
    *   **Storage**: `~/.jscience/cache/{hash}.cache`
    *   **Format**: Raw string storage (JSON/XML).

### Usage
```java
// In a connector
String cacheKey = "pubchem_aspirin";
Optional<String> cached = ResourceCache.global().get(cacheKey);
if (cached.isPresent()) return cached.get();

String data = fetchFromApi(...);
ResourceCache.global().put(cacheKey, data);
```

## 3. Implemented Connectors (By Science)

We have implemented "Propositions" (connectors) for these domains:

| Science | Connector Class | Data Source | Data Type |
|---------|-----------------|-------------|-----------|
| **Astronomy** | `SimbadCatalog` | CDS Strasbourg | Stars, Galaxies, Objects |
| **Biology** | `GbifTaxonomy` | GBIF | Species, Occurrences |
| **Chemistry** | `PubChem` | NIH | Compounds, Properties |
| **Economics** | `WorldBank` | World Bank API | Indicators (GDP, Inflation) |
| **Geology** | `UsgsEarthquakes` | USGS | Seismic Data (Real-time) |
| **Genetics** | `NcbiTaxonomy` | NCBI | Organism Classification |

## 4. Mapping Strategy (The "Bridge")

As domain classes (`Star`, `Molecule`, `Atom`) are not yet designed, we use a placeholder strategy.

**Strategy**:
1.  **Phase 1 (Now)**: Connectors return **Raw Data** (String JSON/XML).
2.  **Phase 2 (Future)**: Implement `map()` method.

**Example**:
```java
// Placeholder for future mapping
public static <T> T map(String json, Class<T> target) {
    throw new UnsupportedOperationException("Domain classes (e.g. " + target.getSimpleName() + ") not yet designed.");
}
```

## 5. Next Steps

1.  **Design Domain Classes**: Create `org.jscience.astronomy.Star`, `org.jscience.chemistry.Molecule`.
2.  **Implement Mappers**: Use Jackson or Gson to parse JSON into these objects.
3.  **Advanced Caching**: Add expiration policies (TTL) for real-time data like earthquakes.
