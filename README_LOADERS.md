# JScience Data Sources & Integrations

A comprehensive list of file formats, APIs, and datasets that JScience supports to bridge the gap between theoretical models and real-world data.

## Loader Architecture

All JScience loaders implement the `InputLoader<T>` or `OutputLoader<T>` interfaces, enabling:

- **Dashboard Discovery**: Automatic registration with the JScience Dashboard
- **Caching**: Built-in resource caching via `AbstractLoader`
- **Fallback Data**: Sample data when external resources are unavailable

### Core Interfaces

```java
public interface ResourceLoader<T> {
    T load(String resourceId) throws Exception;
    String getResourcePath();
    Class<T> getResourceType();
}

public interface InputLoader<T> extends ResourceLoader<T> {}
public interface OutputLoader<T> extends ResourceLoader<T> {
    void save(T resource, String destination) throws Exception;
}
```

---

## 1. Natural Sciences

### Physics & Astronomy

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `HorizonsEphemerisLoader` | JPL Horizons Text/CSV | ✅ Implemented | `InputLoader<List<EphemerisPoint>>` |
| `SimbadLoader` | SIMBAD/Sesame API | ✅ Implemented | `InputLoader<AstronomicalObject>` |
| `StarLoader` | CSV Star Catalogs | ✅ Implemented | `InputLoader<List<Star>>` |
| `VizieRLoader` | VizieR TAP/VOTable | ✅ Implemented | `InputLoader<Map>` |
| `SolarSystemLoader` | JSON Configuration | ✅ Implemented | Static Loader |
| `NetCDFFile` | NetCDF (.nc) | ✅ Implemented | `InputLoader<NetCDFFile>` |

**Planned:**

- **SPICE Kernels (.bsp)**: NASA binary format for precise planetary positions
- **FITS**: Flexible Image Transport System for astronomical images

### Biology & Biochemistry

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `FASTALoader` | FASTA Sequences | ✅ Implemented | `InputLoader<List<Sequence>>` |
| `FASTAParser` | FASTA Files | ✅ Implemented | `InputLoader<List<BioSequence>>` |
| `FASTQParser` | FASTQ with Quality | ✅ Implemented | `InputLoader<List<BioSequence>>` |
| `NCBITaxonomy` | NCBI E-utilities API | ✅ Implemented | `InputLoader<String>` |
| `GBIFTaxonomy` | GBIF Species API | ✅ Implemented | `InputLoader<String>` |
| `UniProtLoader` | UniProt REST API | ✅ Implemented | `InputLoader<Map>` |
| `PDBLoader` | Protein Data Bank | ✅ Implemented | `InputLoader<List<PDBAtom>>` |
| `GenericPDBLoader` | PDB/mmCIF | ✅ Implemented | `InputLoader<MeshView>` |
| `ObjMeshLoader` | Wavefront OBJ | ✅ Implemented | `InputLoader<MeshView>` |
| `StlMeshLoader` | STL 3D Models | ✅ Implemented | `InputLoader<MeshView>` |
| `VirusLoader` | Virus Database JSON | ✅ Implemented | `InputLoader<List<Virus>>` |

**Planned:**

- **VCF**: Variant Call Format for genetic variations
- **Newick/Nexus**: Phylogenetic tree formats

### Chemistry

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `ChemistryDataLoader` | Elements/Molecules JSON | ✅ Implemented | `InputLoader<Object>` |
| `CIFLoader` | Crystallographic CIF | ✅ Implemented | Static Loader |
| `PubChemLoader` | PubChem REST API | ✅ Implemented | Static Loader |
| `ChEBILoader` | ChEBI Ontology | ✅ Implemented | Static Loader |
| `IUPACGoldBookLoader` | IUPAC Terms | ✅ Implemented | Static Loader |
| `AcidBaseLoader` | pKa/pKb Data JSON | ✅ Implemented | Static Loader |

### Earth Sciences

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `OpenWeatherLoader` | OpenWeather API | ✅ Implemented | `InputLoader<Map>` |
| `WeatherDataLoader` | Weather CSV | ✅ Implemented | `InputLoader<List<WeatherRecord>>` |

**Planned:**

- **GRIB**: Weather forecast binary format
- **GeoTIFF**: Satellite imagery and DEMs

---

## 2. Social Sciences

### Economics & Finance

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `FinancialMarketLoader` | OHLCV CSV | ✅ Implemented | `InputLoader<List<Candle>>` |
| `CSVTimeSeriesLoader` | Time Series CSV | ✅ Implemented | `InputLoader<Map<TimePoint, Real>>` |
| `WorldBankLoader` | World Bank API | ✅ Implemented | `InputLoader<List<Region>>` |

**Planned:**

- **FIX Protocol**: Real-time trading data
- **fredapi**: St. Louis Fed economic data

### Geography & GIS

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `GeoJsonLoader` | GeoJSON Features | ✅ Implemented | `InputLoader<List<Region>>` |
| `FactbookLoader` (geography) | CIA Factbook JSON | ✅ Implemented | `InputLoader<Map<String, CountryProfile>>` |

**Planned:**

- **Shapefile (.shp)**: ESRI vector maps
- **KML/KMZ**: Google Earth format

### Politics & History

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `FactbookLoader` (politics) | CIA Factbook XML | ✅ Implemented | `InputLoader<List<Country>>` |

---

## 3. Core & Mathematics

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `OpenMathReader` | OpenMath XML | ✅ Implemented | `InputLoader<Object>` |
| `OpenMathWriter` | OpenMath XML | ✅ Implemented | `OutputLoader<Object>` |
| `CrossRef` | CrossRef DOI API | ✅ Implemented | `InputLoader<CitationInfo>` |

---

## 4. Device & Sensors

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `NMEALoader` | NMEA GPS Sentences | ✅ Implemented | `InputLoader<List<NMEAMessage>>` |

**Planned:**

- **MQTT/CoAP**: IoT sensor protocols
- **TDMS**: LabVIEW measurement data

---

## Usage Example

```java
// Using a loader directly
SimbadLoader simbad = new SimbadLoader();
SimbadLoader.AstronomicalObject obj = simbad.load("Betelgeuse");
System.out.println(obj.getRaHMS() + " " + obj.getDecDMS());

// Using AbstractLoader caching
StarLoader starLoader = new StarLoader();
List<StarLoader.Star> stars = starLoader.load("/data/stars.csv");

// Dashboard discovery finds all InputLoader implementations
List<InputLoader<?>> loaders = dashboard.discoverLoaders();
```

---

## Naming Conventions

All loaders follow consistent naming:

- **Acronyms fully capitalized**: `FASTA`, `FASTQ`, `NCBI`, `GBIF`, `NMEA`, `CSV`, `PDB`
- **Class names use CamelCase**: `FASTALoader`, `NCBITaxonomy`, `GeoJsonLoader`
- **Loader suffix**: Classes that load data end with `Loader`, `Parser`, or `Reader`

---

## Adding New Loaders

1. Implement `InputLoader<T>` or extend `AbstractLoader<T>`
2. Implement required methods: `load()`, `getResourcePath()`, `getResourceType()`
3. For `AbstractLoader`, implement `loadFromSource(String resourceId)`
4. Register with the dashboard by placing in appropriate `loaders` package

```java
public class MyDataLoader extends AbstractLoader<MyData> {
    @Override
    protected MyData loadFromSource(String resourceId) throws Exception {
        // Load and parse data
    }
    
    @Override
    public String getResourcePath() {
        return "/data/myformat/";
    }
    
    @Override
    public Class<MyData> getResourceType() {
        return MyData.class;
    }
}
```
