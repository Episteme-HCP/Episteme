package org.jscience.benchmarks.ui;

import javafx.beans.property.*;
import org.jscience.benchmarks.benchmark.RunnableBenchmark;

/**
 * Model class for the Benchmark TreeTableView.
 */
public class BenchmarkItem {
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty domain;
    private final StringProperty status;
    private final StringProperty result;
    private final StringProperty provider;
    private final StringProperty backend;
    private final StringProperty library;
    private final BooleanProperty selected;
    private final RunnableBenchmark benchmark;

    public BenchmarkItem(String name, String domain, RunnableBenchmark benchmark) {
        this(name, domain, "", "", "", benchmark);
    }

    public BenchmarkItem(RunnableBenchmark benchmark) {
        this(benchmark.getName(), benchmark.getDomain(), benchmark);
    }
    
    // For Categories/Folders
    public BenchmarkItem(String name, String domain, String backend, String provider, String description, boolean isLeaf) {
         this(name, domain, backend, provider, description, null);
    }

    // Main Constructor
    public BenchmarkItem(String name, String domain, String backend, String provider, String description, RunnableBenchmark benchmark) {
        // Sanitize name: remove (CPU), (Wrapper)
        String cleanName = name.replace("(CPU)", "").replace("(Wrapper)", "").trim();
        this.name = new SimpleStringProperty(cleanName);
        this.domain = new SimpleStringProperty(domain);
        this.status = new SimpleStringProperty(benchmark != null ? "Ready" : "");
        this.result = new SimpleStringProperty("");
        this.selected = new SimpleBooleanProperty(false);
        this.benchmark = benchmark;
        
        if (benchmark != null) {
            String fullPName = benchmark.getAlgorithmProvider();
            String libraryVal = determineLibrary(fullPName);
            String backendVal = determineBackend(fullPName, libraryVal);
            this.backend = new SimpleStringProperty(backendVal);
            this.library = new SimpleStringProperty(libraryVal);
            this.provider = new SimpleStringProperty(determineSimpleProvider(fullPName, backendVal, libraryVal));
            this.description = new SimpleStringProperty(benchmark.getDescription());
        } else {
            this.backend = new SimpleStringProperty(backend != null ? backend : "");
            this.library = new SimpleStringProperty("");
            this.provider = new SimpleStringProperty(provider != null ? provider : "");
            this.description = new SimpleStringProperty(description != null ? description : "");
        }
    }
    
    public boolean isBenchmark() {
        return benchmark != null;
    }

    private String determineBackend(String providerName, String library) {
        String pName = providerName.toUpperCase();
        
        // GPU / Distributed backends
        if (pName.contains("CUDA")) return "CUDA"; 
        if (pName.contains("OPENCL") || pName.contains("GPU")) return "OpenCL";
        if (pName.contains("MPI") || pName.contains("SPARK")) return "Distributed";
        
        // External libraries are generally single-threaded
        if (!library.equals("JScience") && !library.equals("JScience Native")) {
            // JBlas uses native BLAS which may be multi-threaded internally
            if (library.equals("JBlas")) return "CPU (Native)";
            return "Monocore";
        }
        
        // JScience CPU providers use parallel streams above PARALLEL_THRESHOLD
        return "Multicore";
    }

    private String determineLibrary(String providerName) {
        String pName = providerName.toUpperCase();
        if (pName.contains("EJML")) return "EJML";
        if (pName.contains("COMMONS MATH")) return "Commons Math";
        if (pName.contains("TARSOS")) return "TarsosDSP";
        if (pName.contains("MINIM")) return "Minim";
        if (pName.contains("VLCJ")) return "VLCJ";
        if (pName.contains("OPENCV") || pName.contains("JAVACV")) return "JavaCV";
        if (pName.contains("JBLAS")) return "JBlas";
        if (pName.contains("COLT")) return "Colt";
        if (pName.contains("ND4J")) return "ND4J";
        if (pName.contains("FFTW")) return "FFTW3";
        if (pName.contains("MPI")) return "MPJ Express";
        if (pName.contains("SPARK")) return "Apache Spark";
        if (pName.contains("NATIVE")) return "JScience Native";
        if (pName.contains("JAVA REFERENCE")) return "JScience";
        return "JScience"; 
    }

    private String determineSimpleProvider(String fullName, String backend, String library) {
        // For external libraries, the provider name IS the library name
        if (!library.equals("JScience") && !library.equals("JScience Native")) {
            return library;
        }
        
        // For JScience / JScience Native, detect meaningful algorithm qualifiers
        String upper = fullName.toUpperCase();
        if (upper.contains("SPARSE")) return "Sparse";
        if (upper.contains("SIMD") || upper.contains("VECTOR")) return "SIMD";
        if (upper.contains("REFERENCE")) return "Reference";
        if (upper.contains("MULTICORE")) return "Multicore";
        if (upper.contains("VARIABLE ELIMINATION")) return "Variable Elimination";
        if (upper.contains("MONTE CARLO")) return "Monte Carlo";
        if (upper.contains("LOUVAIN")) return "Louvain";
        if (upper.contains("BARNES")) return "Barnes-Hut";
        if (upper.contains("RUNGE") || upper.contains("RK4")) return "Runge-Kutta";
        if (upper.contains("EULER")) return "Euler";
        
        // Fallback: clean the full name to extract a meaningful qualifier
        String cleaned = fullName.replaceAll("\\s*\\([^)]*\\)", "") // Remove parenthesized qualifiers
                                 .replace("JScience", "")
                                 .replace("Native", "")
                                 .replace("CPU", "")
                                 .replace("GPU", "")
                                 .replaceAll("\\s+", " ")
                                 .trim();
        if (!cleaned.isEmpty()) return cleaned;
        
        return "Standard";
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    
    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }

    public String getDomain() { return domain.get(); }
    public StringProperty domainProperty() { return domain; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }

    public String getResult() { return result.get(); }
    public StringProperty resultProperty() { return result; }
    
    public String getProvider() { return provider.get(); }
    public StringProperty providerProperty() { return provider; }

    public String getLibrary() { return library.get(); }
    public StringProperty libraryProperty() { return library; }

    public String getBackend() { return backend.get(); }
    public StringProperty backendProperty() { return backend; }

    public boolean isSelected() { return selected.get(); }
    public BooleanProperty selectedProperty() { return selected; }

    public RunnableBenchmark getBenchmark() { return benchmark; }
    
    // Store raw throughput score for dynamic recalculation (Ops/sec)
    private double rawScore = 0.0;
    public void setScore(double score) { this.rawScore = score; }
    public double getScore() { return rawScore; }
    
    // P99 latency in milliseconds, computed from per-iteration measurements
    private double p99LatencyMs = 0.0;
    public void setP99LatencyMs(double p99) { this.p99LatencyMs = p99; }
    public double getP99LatencyMs() { return p99LatencyMs; }
    
    // Unique ID helper for consistent usage
    public String getUniqueId() {
        if (benchmark == null) return getName();
        // Include Provider to differentiate implementations within the same library
        return getName() + " [" + getProvider() + "] (" + getLibrary() + ")";
    }
}
