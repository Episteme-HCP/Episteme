# JScience Data Sources & Integrations

A comprehensive list of file formats, APIs, and datasets that JScience supports to bridge the gap between theoretical models and real-world data.

## Loader Architecture

All JScience loaders implement the `ResourceReader<T>` interface (from `org.jscience.io`), enabling:

- **Dashboard Discovery**: Automatic registration with the JScience Dashboard
- **Caching**: Built-in resource caching via `AbstractResourceReader`
- **Fallback Data**: Sample data when external resources are unavailable

### Core Interfaces

```java
public interface ResourceIO<T> {
    String getResourcePath();
    Class<T> getResourceType();
    String getName();
    String getDescription();
    String getLongDescription();
    String getCategory();
    String[] getSupportedVersions();  // Format versions supported
}

public interface ResourceReader<T> extends ResourceIO<T> {
    T load(String resourceId) throws Exception;
}

public interface ResourceWriter<T> extends ResourceIO<T> {
    void save(T resource, String destination) throws Exception;
}
```

**Version Compatibility**: Each reader/writer declares which versions of its format are supported via `getSupportedVersions()`. For example:
- `MathMLReader.getSupportedVersions()` returns `["3.0", "2.0"]`
- `SBMLReader.getSupportedVersions()` returns `["Level 3 Version 2", "Level 3 Version 1"]`
- `OpenMathReader.getSupportedVersions()` returns `["2.0"]`

---

## XML-Based Scientific Data Formats

JScience provides comprehensive support for standard XML formats used across scientific disciplines. All XML readers extend `AbstractResourceReader<T>` and implement the `ResourceReader<T>` interface.

### Biology & Life Sciences

| Reader | Format | Package | Description |
|--------|--------|---------|-------------|
| `PhyloXMLReader` | PhyloXML | `org.jscience.biology.loaders.phyloxml` | Phylogenetic trees with clades, taxonomy, and evolutionary events |
| `SBMLReader` | SBML | `org.jscience.biology.loaders.sbml` | Systems biology models with reactions, species, and kinetics |
| `NeuroMLReader` | NeuroML v2 | `org.jscience.biology.loaders.neuroml` | Computational neuroscience models with morphology and ion channels |
| `PDBMLReader` | PDBML | `org.jscience.biology.loaders.pdbml` | Protein Data Bank structures with atomic coordinates |
| `BioPAXReader` | BioPAX Level 3 | `org.jscience.biology.loaders.biopax` | Biological pathways, reactions, and molecular interactions |

#### PhyloXML Reader
**Supported Elements:**
- Phylogeny metadata (name, description, rooted status)
- Clade hierarchy with branch lengths
- Taxonomy (scientific name, common name, rank)
- Sequence data associations
- Evolutionary events (speciation, duplication)
- Confidence values (bootstrap, posterior probability)

```java
PhyloXMLReader reader = new PhyloXMLReader();
PhyloXMLDocument doc = reader.load("/path/to/tree.xml");
for (Phylogeny phylo : doc.getPhylogenies()) {
    System.out.println("Tree: " + phylo.getName());
    Clade root = phylo.getRootClade();
    // Traverse tree structure
}
```

#### SBML Reader
**Supported Features (via JSBML):**
- Model metadata (ID, name, notes)
- Compartments with sizes and units
- Species (metabolites) with concentrations
- Reactions with kinetic laws and stoichiometry
- FBC extension for flux balance constraints
- Stoichiometric matrix generation

```java
SBMLReader reader = new SBMLReader();
SBMLModel model = reader.read(new File("ecoli_core.xml"));
System.out.println("Reactions: " + model.getReactionCount());
// Generate stoichiometric matrix
RealDoubleMatrix sMatrix = model.getStoichiometricMatrix();
```

#### NeuroML Reader
**Supported NeuroML v2 Elements:**
- Cell morphology (segments, segment groups)
- Ion channels and conductances
- Synapses (tauRise, tauDecay, gbase)
- Networks (populations, projections)
- Biophysical properties (membrane capacitance, channel densities)

```java
NeuroMLReader reader = new NeuroMLReader();
NeuroMLDocument doc = reader.load("/path/to/model.nml");
for (Cell cell : doc.getCells()) {
    Morphology morph = cell.getMorphology();
    System.out.println("Segments: " + morph.getSegments().size());
}
```

#### PDBML Reader
**Supported Elements:**
- Entry metadata (ID, title, deposition date)
- Atomic coordinates (atom_site with x, y, z)
- Polymer chains and sequences
- Secondary structure (helices, sheets)
- Experimental data (resolution, method)

```java
PDBMLReader reader = new PDBMLReader();
PDBMLStructure structure = reader.load("/path/to/1abc.xml");
System.out.println("Atoms: " + structure.getAtomCount());
System.out.println("Resolution: " + structure.getResolution() + " Å");
```

#### BioPAX Reader
**Supported BioPAX Level 3 Elements:**
- Pathways and their components
- Biochemical reactions and catalysis
- Proteins, small molecules, complexes
- Gene regulation relationships
- Cross-references to external databases

```java
BioPAXReader reader = new BioPAXReader();
BioPAXModel model = reader.load("/path/to/pathway.owl");
for (Pathway pathway : model.getPathways()) {
    System.out.println("Pathway: " + pathway.getDisplayName());
}
```

### Chemistry

| Reader | Format | Package | Description |
|--------|--------|---------|-------------|
| `CMLReader` | Chemical Markup Language | `org.jscience.chemistry.loaders.cml` | Molecules, atoms, bonds, and spectra |
| `AnIMLReader` | AnIML | `org.jscience.chemistry.loaders.animl` | Analytical instrument measurement data |

#### CML Reader
**Supported Elements:**
- Molecular structure (atoms, bonds)
- Crystal structures
- Reactions and mechanisms
- Spectroscopic data
- Property calculations

### Physics

| Reader | Format | Package | Description |
|--------|--------|---------|-------------|
| `ThermoMLReader` | ThermoML | `org.jscience.physics.loaders.thermoml` | Thermodynamic property measurements |

#### ThermoML Reader
**Supported Elements:**
- Compound identification and properties
- Phase equilibrium data
- Transport properties
- Thermodynamic measurements with uncertainties

### Earth Sciences & Geography

| Reader | Format | Package | Description |
|--------|--------|---------|-------------|
| `GMLReader` | Geography Markup Language | `org.jscience.earth.loaders.gml` | OGC standard for geographic features |

#### GML Reader
**Supported GML 3.2 Elements:**
- Feature collections and members
- Points, LineStrings, Polygons
- MultiPoint, MultiLineString, MultiPolygon
- Coordinate reference systems (srsName)
- Feature properties and attributes

```java
GMLReader reader = new GMLReader();
GMLDocument doc = reader.load("/path/to/features.gml");
for (GMLFeature feature : doc.getFeatures()) {
    GMLGeometry geom = feature.getGeometry();
    if (geom instanceof GMLPolygon) {
        GMLPolygon poly = (GMLPolygon) geom;
        System.out.println("Polygon with " + poly.getExteriorRing().size() + " points");
    }
}
```

### Computing & Machine Learning

| Reader | Format | Package | Description |
|--------|--------|---------|-------------|
| `PMMLReader` | PMML 4.4 | `org.jscience.computing.loaders.pmml` | Predictive models (ML/AI) |

#### PMML Reader
**Supported Model Types:**
- Regression models (linear, polynomial)
- Decision trees and rule sets
- Neural networks (layer structure)
- Clustering models (k-means, etc.)
- Support vector machines
- Naive Bayes classifiers

```java
PMMLReader reader = new PMMLReader();
PMMLModel model = reader.load("/path/to/model.pmml");
Model treeModel = model.getFirstModel();
if ("TreeModel".equals(treeModel.getType())) {
    TreeNode root = treeModel.getRootNode();
    // Traverse decision tree
}
```

### Mathematics

| Reader/Writer | Format | Package | Description |
|---------------|--------|---------|-------------|
| `OpenMathReader` | OpenMath | `org.jscience.mathematics.loaders.openmath` | Mathematical expressions and objects |
| `OpenMathWriter` | OpenMath | `org.jscience.mathematics.loaders.openmath` | Export mathematical objects |
| `MathMLReader` | MathML | `org.jscience.mathematics.loaders.mathml` | Mathematical notation and content |

#### OpenMath Reader/Writer
**Supported Elements:**
- Integer, Float, String, ByteArray
- Variables and Symbols
- Applications and Bindings
- Content Dictionaries

---

## Other File Formats

### Physics & Astronomy

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `HorizonsEphemerisLoader` | JPL Horizons Text/CSV | ✅ Implemented | `ResourceReader<List<EphemerisPoint>>` |
| `SimbadLoader` | SIMBAD/Sesame API | ✅ Implemented | `ResourceReader<AstronomicalObject>` |
| `StarLoader` | CSV Star Catalogs | ✅ Implemented | `ResourceReader<List<Star>>` |
| `VizieRLoader` | VizieR TAP/VOTable | ✅ Implemented | `ResourceReader<Map>` |
| `SolarSystemLoader` | JSON Configuration | ✅ Implemented | Static Loader |
| `NetCDFFile` | NetCDF (.nc) | ✅ Implemented | `ResourceReader<NetCDFFile>` |

**Planned:**
- **SPICE Kernels (.bsp)**: NASA binary format for precise planetary positions
- **FITS**: Flexible Image Transport System for astronomical images

### Biology & Biochemistry

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `FASTALoader` | FASTA Sequences | ✅ Implemented | `ResourceReader<List<Sequence>>` |
| `FASTAParser` | FASTA Files | ✅ Implemented | `ResourceReader<List<BioSequence>>` |
| `FASTQParser` | FASTQ with Quality | ✅ Implemented | `ResourceReader<List<BioSequence>>` |
| `NCBITaxonomy` | NCBI E-utilities API | ✅ Implemented | `ResourceReader<String>` |
| `GBIFTaxonomy` | GBIF Species API | ✅ Implemented | `ResourceReader<String>` |
| `UniProtLoader` | UniProt REST API | ✅ Implemented | `ResourceReader<Map>` |
| `PDBLoader` | Protein Data Bank | ✅ Implemented | `ResourceReader<List<PDBAtom>>` |
| `GenericPDBLoader` | PDB/mmCIF | ✅ Implemented | `ResourceReader<MeshView>` |
| `ObjMeshLoader` | Wavefront OBJ | ✅ Implemented | `ResourceReader<MeshView>` |
| `StlMeshLoader` | STL 3D Models | ✅ Implemented | `ResourceReader<MeshView>` |
| `VirusLoader` | Virus Database JSON | ✅ Implemented | `ResourceReader<List<Virus>>` |

**Planned:**
- **VCF**: Variant Call Format for genetic variations
- **Newick/Nexus**: Phylogenetic tree formats

### Chemistry

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `ChemistryDataLoader` | Elements/Molecules JSON | ✅ Implemented | `ResourceReader<Object>` |
| `CIFLoader` | Crystallographic CIF | ✅ Implemented | Static Loader |
| `PubChemLoader` | PubChem REST API | ✅ Implemented | Static Loader |
| `ChEBILoader` | ChEBI Ontology | ✅ Implemented | Static Loader |
| `IUPACGoldBookLoader` | IUPAC Terms | ✅ Implemented | Static Loader |
| `AcidBaseLoader` | pKa/pKb Data JSON | ✅ Implemented | Static Loader |

### Earth Sciences

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `OpenWeatherLoader` | OpenWeather API | ✅ Implemented | `ResourceReader<Map>` |
| `WeatherDataLoader` | Weather CSV | ✅ Implemented | `ResourceReader<List<WeatherRecord>>` |

**Planned:**
- **GRIB**: Weather forecast binary format
- **GeoTIFF**: Satellite imagery and DEMs

---

## Social Sciences

### Economics & Finance

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `FinancialMarketLoader` | OHLCV CSV | ✅ Implemented | `ResourceReader<List<Candle>>` |
| `CSVTimeSeriesLoader` | Time Series CSV | ✅ Implemented | `ResourceReader<Map<TimePoint, Real>>` |
| `WorldBankLoader` | World Bank API | ✅ Implemented | `ResourceReader<List<Region>>` |

### Geography & GIS

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `GeoJsonLoader` | GeoJSON Features | ✅ Implemented | `ResourceReader<List<Region>>` |
| `FactbookLoader` | CIA Factbook JSON | ✅ Implemented | `ResourceReader<Map<String, CountryProfile>>` |

**Planned:**
- **Shapefile (.shp)**: ESRI vector maps
- **KML/KMZ**: Google Earth format

### Device & Sensors

| Loader | Format | Status | Interface |
|--------|--------|--------|-----------|
| `NMEALoader` | NMEA GPS Sentences | ✅ Implemented | `ResourceReader<List<NMEAMessage>>` |

---

## Usage Examples

### XML Reader Pattern

All XML readers follow a consistent pattern:

```java
// Direct reading from file
PhyloXMLReader reader = new PhyloXMLReader();
PhyloXMLDocument doc = reader.read(new File("tree.xml"));

// Reading from InputStream
try (InputStream is = new FileInputStream("model.xml")) {
    NeuroMLDocument doc = neuroReader.read(is);
}

// Using ResourceReader interface (with caching)
GMLDocument doc = gmlReader.load("/path/to/features.gml");
```

### Dashboard Discovery

```java
// Dashboard discovery finds all ResourceReader implementations
List<ResourceReader<?>> readers = dashboard.discoverReaders();
for (ResourceReader<?> reader : readers) {
    System.out.println(reader.getName() + " - " + reader.getCategory());
}
```

---

## Naming Conventions

All loaders follow consistent naming:

- **Acronyms fully capitalized**: `FASTA`, `FASTQ`, `NCBI`, `GBIF`, `NMEA`, `CSV`, `PDB`, `SBML`, `GML`, `PMML`
- **Class names use CamelCase**: `FASTALoader`, `NCBITaxonomy`, `GeoJsonLoader`
- **Suffix conventions**:
  - `Reader`: XML and structured format parsers (preferred for new loaders)
  - `Loader`: General data loading
  - `Parser`: Text/line-based format parsing

---

## Adding New Loaders

1. Extend `AbstractResourceReader<T>` (from `org.jscience.io`)
2. Implement required methods for `ResourceIO<T>`:
   - `getResourcePath()`, `getResourceType()`
   - `getName()`, `getDescription()`, `getLongDescription()`, `getCategory()`
3. Implement `loadFromSource(String resourceId)` for main loading logic
4. Optionally override `loadFromInputStream(InputStream is, String id)`
5. Place in appropriate `loaders` package for dashboard discovery

```java
public class MyFormatReader extends AbstractResourceReader<MyDocument> {

    @Override
    public String getResourcePath() {
        return null; // File-based, path provided at load time
    }

    @Override
    public Class<MyDocument> getResourceType() {
        return MyDocument.class;
    }

    @Override
    public String getName() {
        return "MyFormat Reader";
    }

    @Override
    public String getDescription() {
        return "Reads data from MyFormat files";
    }

    @Override
    public String getLongDescription() {
        return "MyFormat is an XML-based format for representing scientific data...";
    }

    @Override
    public String getCategory() {
        return "Science";
    }

    @Override
    protected MyDocument loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) {
            return read(file);
        }
        throw new Exception("File not found: " + resourceId);
    }

    public MyDocument read(File file) throws MyFormatException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis);
        } catch (IOException e) {
            throw new MyFormatException("Failed to read file: " + file, e);
        }
    }

    public MyDocument read(InputStream input) throws MyFormatException {
        // Parse XML and return document
    }
}
```

---

## External Dependencies

Some XML readers require external libraries:

| Reader | Dependencies |
|--------|--------------|
| `SBMLReader` | JSBML (`org.sbml.jsbml:jsbml`) |
| `CMLReader` | JumboNG (included) |
| All XML readers | Java DOM (`javax.xml.parsers`) |

---

## Standards References

| Format | Standard | URL |
|--------|----------|-----|
| PhyloXML | PhyloXML 1.10 | http://www.phyloxml.org/ |
| SBML | SBML Level 3 | http://sbml.org/ |
| NeuroML | NeuroML 2.0 | https://neuroml.org/ |
| PDBML | PDB/mmCIF | https://www.wwpdb.org/ |
| BioPAX | BioPAX Level 3 | http://www.biopax.org/ |
| CML | Chemical Markup Language | http://www.xml-cml.org/ |
| ThermoML | IUPAC ThermoML | https://www.iupac.org/ |
| GML | OGC GML 3.2 | https://www.ogc.org/standards/gml |
| PMML | DMG PMML 4.4 | https://dmg.org/pmml/ |
| OpenMath | OpenMath 2.0 | https://openmath.org/ |
| MathML | W3C MathML 3.0 | https://www.w3.org/Math/ |
| AnIML | ASTM AnIML | https://www.animl.org/ |
