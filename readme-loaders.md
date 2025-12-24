# JScience Data Sources & Integrations

A comprehensive list of file formats, APIs, and datasets that JScience should support to bridge the gap between theoretical models and real-world data.

## 1. Natural Sciences

### Physics & Astronomy

* **Ephemerides**:
  * **JPL Horizons**: Text/CSV export (implemented loader).
  * **SPICE Kernels (.bsp)**: NASA's navigation and ancillary information facility binary format for precise planetary positions.
* **Astrophysics Images**:
  * **FITS (Flexible Image Transport System)**: Standard for astronomical images and tables.
* **Climate & Meteorology**:
  * **NetCDF / HDF5**: Gridded climate data (temperature, pressure, wind fields).
  * **GRIB**: General regularly-distributed information in binary form (weather forecasts).

### Biology & Biochemistry

* **Molecular Structures**:
  * **PDB (.pdb, .cif)**: Protein Data Bank format for 3D structures (loader partially implemented).
  * **SDF / MOL**: Chemical table files for small molecules.
* **Genetics**:
  * **FASTA (.fasta)**: Nucleotide or peptide sequences.
  * **FASTQ**: Raw sequencing reads with quality scores.
  * **VCF (Variant Call Format)**: Genetic variations (SNPs, indels).
* **Taxonomy & Biodiversity**:
  * **GBIF API**: Global Biodiversity Information Facility (species occurrences).
  * **Newick / Nexus**: Phylogenetic tree formats.

### Chemistry

* **Elements & Isotopes**:
  * **CIAAW**: Commission on Isotopic Abundances and Atomic Weights datasets.
  * **PubChem API**: Access to chemical properties and structures.

## 2. Social Sciences

### Economics & Finance

* **Market Data**:
  * **OHLCV CSV**: Standard financial time series (Open, High, Low, Close, Volume).
  * **FIX Protocol**: Real-time trading data exchange (advanced).
* **Macroeconomics**:
  * **World Bank Open Data API**: GDP, inflation, development indicators.
  * **fredapi (St. Louis Fed)**: US economic time series.

### Geography & GIS

* **Vector Data**:
  * **GeoJSON**: Lightweight format for geographic features.
  * **Shapefile (.shp)**: ESRI standard for vector maps.
  * **KML/KMZ**: Keyhole Markup Language (Google Earth).
* **Raster Data**:
  * **GeoTIFF**: Satellite imagery and digital elevation models (DEM).

### Sociology & Demographics

* **Population**:
  * **UN Population Division**: CSV datasets for global population stats/projections.
  * **Census Bureau APIs**: Detailed demographic info.

## 3. Engineering & Measurement

### Metrology

* **Sensor Data**:
  * **MQTT / CoAP**: IoT protocols for live sensor feeds.
  * **LabVIEW / TDMS**: National Instruments standard for test and measurement data.

## 4. Prioritized Implementation List

1. **[High] CSV Time Series**: Generic loader for "Date, Value" pairs (Economics, Climate).
2. **[High] GeoJSON**: Basic map overlay support for `jscience-social`.
3. **[Medium] FASTA**: Reading DNA sequences for `jscience-biology`.
4. **[Medium] NetCDF**: Reading simplified climate grids.
5. **[Low] SPICE**: precise planetary ephemerides (complex binary format).
