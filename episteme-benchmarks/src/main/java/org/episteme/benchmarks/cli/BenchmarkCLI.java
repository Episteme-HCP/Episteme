package org.episteme.benchmarks.cli;

import org.episteme.benchmarks.reporting.BenchmarkReporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Command Line Interface for running benchmarks without UI.
 * Facilitates CI integration and automated performance testing.
 */
public class BenchmarkCLI {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        boolean runAll = false;
        boolean dryRun = false;
        boolean generatePdf = false;
        String exportFile = null;
        String domainFilter = null;
        String runId = null;
        List<String> excludedProviders = new ArrayList<>();

        // Parse arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--run-all".equals(arg)) {
                runAll = true;
            } else if ("--dry-run".equals(arg)) {
                dryRun = true;
            } else if ("--pdf".equals(arg)) {
                generatePdf = true;
            } else if ("--export-file".equals(arg) && i + 1 < args.length) {
                exportFile = args[++i];
            } else if (arg.startsWith("--export-file=")) {
                exportFile = arg.substring("--export-file=".length());
            } else if ("--domain".equals(arg) && i + 1 < args.length) {
                domainFilter = args[++i].replaceAll("^[\"']|[\"']$", "");
            } else if (arg.startsWith("--domain=")) {
                domainFilter = arg.substring("--domain=".length()).replaceAll("^[\"']|[\"']$", "");
            } else if ("--run".equals(arg) && i + 1 < args.length) {
                runId = args[++i];
            } else if (arg.startsWith("--run=")) {
                runId = arg.substring("--run=".length());
            } else if ("--exclude-provider".equals(arg) && i + 1 < args.length) {
                excludedProviders.add(args[++i].replaceAll("^[\"']|[\"']$", ""));
            } else if (arg.startsWith("--exclude-provider=")) {
                excludedProviders.add(arg.substring("--exclude-provider=".length()).replaceAll("^[\"']|[\"']$", ""));
            } else if ("--help".equals(arg)) {
                printHelp();
                return;
            }
        }

        // Default export logic: specialized directory and timestamped filename in docs/
        if (exportFile == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String dirPath = "docs/benchmark-results";
            new File(dirPath).mkdirs();
            
            exportFile = dirPath + "/High-Precision_Benchmark_Result_" + timestamp + ".json";
        } else {
            // If user provided a path, ensure parent directory exists
            File f = new File(exportFile);
            if (f.getParentFile() != null && !f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
        }

        if (!runAll && runId == null) {
            System.out.println("Nothing to run. Use --run-all or --run <id> to execute benchmarks.");
            printHelp();
            return;
        }

        System.out.println("Starting Episteme Benchmarks (CLI Mode)...");
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        System.out.println("Java: " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")");
        System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());

        // Discover Benchmarks
        List<org.episteme.benchmarks.benchmark.RunnableBenchmark> allBenchmarks = org.episteme.benchmarks.benchmark.BenchmarkRegistry.discover();
        System.out.println("Discovered " + allBenchmarks.size() + " benchmarks. IDs:");
        for (org.episteme.benchmarks.benchmark.RunnableBenchmark b : allBenchmarks) {
            System.out.println("  - " + b.getId() + " [Domain: " + b.getDomain() + "]");
        }
        List<org.episteme.benchmarks.benchmark.RunnableBenchmark> benchmarks = new ArrayList<>();
        
        for (org.episteme.benchmarks.benchmark.RunnableBenchmark b : allBenchmarks) {
            String domain = b.getDomain();
            if (domainFilter != null) {
                String dClean = domain.toLowerCase().replaceAll("[^a-z0-9]", "");
                String fClean = domainFilter.toLowerCase().replaceAll("[^a-z0-9]", "");
                boolean match = dClean.contains(fClean) || fClean.contains(dClean);
                
                System.out.println("[FILTER] Domain Check: '" + domain + "' vs '" + domainFilter + "' -> Clean: '" + dClean + "' vs '" + fClean + "' -> Match: " + match);
                
                if (!match) {
                    continue;
                }
            }
            
            if (runId != null) {
                String id = b.getId();
                boolean match = id.equalsIgnoreCase(runId) || id.contains(runId);
                System.out.println("[FILTER] ID Check: '" + id + "' vs '" + runId + "' -> Match: " + match);
                if (!match) {
                    continue;
                }
            }
            
            // Provider Filter (only High-Precision for this specialized report)
            if ("Linear Algebra (High-Precision Audit)".equals(b.getDomain())) {
                String name = b.getAlgorithmProvider();
                if (excludedProviders.contains(name)) continue;
                // Consistent exclusion list from HighPrecisionComplianceTest
                if (name.contains("EJML") || name.contains("Colt") || name.contains("Commons Math") || 
                    name.contains("JBlas") || name.contains("ND4J") || name.contains("CUDA") || 
                    name.contains("OpenCL") || name.contains("SIMD") || name.contains("Unified") || 
                    name.contains("FFMBLAS") || name.contains("Native BLAS Provider FFM") || 
                    name.contains("Native CPU-BLAS")) continue;
            }
            
            b.setDryRun(dryRun);
            benchmarks.add(b);
        }
        
        System.out.println("Discovered " + allBenchmarks.size() + " benchmarks. Filtered to " + benchmarks.size() + ".");

        Map<String, org.episteme.benchmarks.benchmark.BenchmarkResult> results = new LinkedHashMap<>();

        // Warmup & Run
        int success = 0;
        int fail = 0;
        int skipped = 0;

        for (org.episteme.benchmarks.benchmark.RunnableBenchmark benchmark : benchmarks) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("Running: " + benchmark.getName() + " [" + benchmark.getDomain() + "]");
            if (dryRun) System.out.println("Mode: DRY RUN (Small Dataset)");

            if (!benchmark.isAvailable()) {
                System.out.println("Status: SKIPPED (Unavailable)");
                skipped++;
                results.put(benchmark.getId(), new org.episteme.benchmarks.benchmark.BenchmarkResult(benchmark.getId(), benchmark.getName(), 
                    benchmark.getAlgorithmProvider(), benchmark.getDomain(), 0, 0, 0, 0, 0, new java.util.HashMap<>()));
                continue;
            }

            try {
                // Setup
                System.out.print("Status: Setup... ");
                
                // Enforce Isolation for benchmarks
                org.episteme.core.technical.algorithm.AlgorithmService oldService = 
                    org.episteme.core.technical.algorithm.AlgorithmManager.getService();
                org.episteme.core.technical.algorithm.AlgorithmProvider providerInstance = benchmark.getAlgorithmProviderInstance();
                
                if (providerInstance != null) {
                    org.episteme.core.technical.algorithm.AlgorithmManager.setService(
                        new org.episteme.core.technical.algorithm.TestingAlgorithmService(providerInstance));
                } else if (oldService instanceof org.episteme.core.technical.algorithm.StandardAlgorithmService) {
                    // If no explicit provider but was production service, restrict to empty testing service
                    // This forces all benchmarks to be explicit if they use AlgorithmManager
                    org.episteme.core.technical.algorithm.AlgorithmManager.setService(
                        new org.episteme.core.technical.algorithm.TestingAlgorithmService());
                }

                try {
                    benchmark.setup();
                    
                    // Warmup
                    System.out.print("Warmup... ");
                    long warmupStart = System.nanoTime();
                    while (System.nanoTime() - warmupStart < 500_000_000L) { // 500ms
                        benchmark.run();
                    }

                    // Measure
                    System.out.print("Measuring... ");
                    System.gc();
                    try { Thread.sleep(100); } catch (InterruptedException e) {}

                    List<Long> iterNanos = new ArrayList<>();
                    long totalStart = System.nanoTime();
                    while (System.nanoTime() - totalStart < 2_000_000_000L) { // 2s
                        long iterStart = System.nanoTime();
                        benchmark.run();
                        iterNanos.add(System.nanoTime() - iterStart);
                    }
                    long totalEnd = System.nanoTime();

                    // Stats
                    double durationSec = (totalEnd - totalStart) / 1_000_000_000.0;
                    double opsSec = iterNanos.size() / durationSec;
                    
                    // P99
                    iterNanos.sort(Long::compareTo);
                    int p99Index = Math.max(0, (int) Math.ceil(iterNanos.size() * 0.99) - 1);
                    double p99Ms = iterNanos.size() > 0 ? iterNanos.get(p99Index) / 1_000_000.0 : 0;

                    System.out.println("DONE");
                    System.out.printf(Locale.US, "Result: %.2f ops/s, P99: %.3f ms%n", opsSec, p99Ms);
                    
                    org.episteme.benchmarks.benchmark.BenchmarkResult res = new org.episteme.benchmarks.benchmark.BenchmarkResult(
                        benchmark.getId(), benchmark.getName(), benchmark.getAlgorithmProvider(), benchmark.getDomain(),
                        (long)(durationSec * 1000), iterNanos.size(), p99Ms, opsSec, 0, benchmark.getMetadata()
                    );
                    results.put(benchmark.getId(), res);
                    success++;
                } catch (Throwable t) {
                    System.out.println("FAILED during execution");
                    System.out.println("Error: " + t.getClass().getSimpleName() + " - " + t.getMessage());
                    results.put(benchmark.getId(), new org.episteme.benchmarks.benchmark.BenchmarkResult(benchmark.getId(), benchmark.getName(), 
                        benchmark.getAlgorithmProvider(), benchmark.getDomain(), 0, 0, 0, 0, 0, new java.util.HashMap<>()));
                    fail++;
                } finally {
                    benchmark.teardown();
                    org.episteme.core.technical.algorithm.AlgorithmManager.setService(oldService);
                }

            } catch (Throwable t) {
                System.out.println("FAILED during setup/isolation");
                System.out.println("Error: " + t.getClass().getSimpleName() + " - " + t.getMessage());
                results.put(benchmark.getId(), new org.episteme.benchmarks.benchmark.BenchmarkResult(benchmark.getId(), benchmark.getName(), 
                    benchmark.getAlgorithmProvider(), benchmark.getDomain(), 0, 0, 0, 0, 0, new java.util.HashMap<>()));
                fail++;
            }
        }

        System.out.println("================================================================");
        System.out.println("Summary: " + success + " Success, " + fail + " Failed, " + skipped + " Skipped.");
        System.out.println("================================================================");

        // Print Performance Report
        System.out.println(org.episteme.core.util.PerformanceLogger.getReport());

        // Generate Report
        String reporterTitle = domainFilter != null && domainFilter.contains("High-Precision") ? 
            "High-Precision Benchmark Result" : "Episteme Performance Audit";
            
        BenchmarkReporter reporter = new BenchmarkReporter(reporterTitle);
        for (org.episteme.benchmarks.benchmark.BenchmarkResult r : results.values()) {
            reporter.addResult(r);
        }

        // Expand high-precision results for JSON export (one entry per operation)
        List<org.episteme.benchmarks.benchmark.BenchmarkResult> expandedResults = new ArrayList<>();
        for (org.episteme.benchmarks.benchmark.BenchmarkResult r : results.values()) {
            if (r.domain().contains("High-Precision") && r.extraMetrics() != null && !r.extraMetrics().isEmpty()) {
                for (java.util.Map.Entry<String, Object> metric : r.extraMetrics().entrySet()) {
                    expandedResults.add(new org.episteme.benchmarks.benchmark.BenchmarkResult(
                        r.benchmarkName() + " (" + metric.getKey() + ")",
                        r.provider(),
                        r.domain(),
                        (Double)metric.getValue()
                    ));
                }
            } else {
                expandedResults.add(r);
            }
        }

        // Generate Files
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String baseName = "docs/benchmark-results/High-Precision_Benchmark_Result_" + timestamp;
        
        if (generatePdf) {
            System.out.println("[INFO] Generating PDF: " + baseName + ".pdf");
            reporter.generateReport(baseName + ".pdf");
        }
        exportJson(baseName + ".json", expandedResults);
    }

    private static void printHelp() {
        System.out.println("Usage: java org.episteme.benchmarks.cli.BenchmarkCLI [options]");
        System.out.println("Options:");
        System.out.println("  --run-all         Run all discovered benchmarks.");
        System.out.println("  --dry-run         Run with minimal datasets for functional verification.");
        System.out.println("  --export-file <f> Save results to JSON file.");
        System.out.println("  --pdf             Generate PDF Report (requires --export-file).");
        System.out.println("  --domain <d>      Only run benchmarks in this domain (e.g. 'Linear Algebra').");
        System.out.println("  --run <id>        Only run the benchmark with this ID.");
        System.out.println("  --exclude-provider <p> Skip benchmarks from this provider (e.g. 'ND4J').");
        System.out.println("  --help            Show this message.");
    }

    private static void exportJson(String path, List<org.episteme.benchmarks.benchmark.BenchmarkResult> results) {
        try {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            
            // Metadata -> context
            json.append("  \"context\": {\n");
            json.append(String.format("    \"java_version\": \"%s\",\n", escape(System.getProperty("java.version"))));
            json.append(String.format("    \"os_name\": \"%s\",\n", escape(System.getProperty("os.name"))));
            json.append(String.format("    \"os_arch\": \"%s\",\n", escape(System.getProperty("os.arch"))));
            json.append(String.format("    \"processors\": %d,\n", Runtime.getRuntime().availableProcessors()));
            json.append(String.format("    \"timestamp\": \"%s\"\n", java.time.Instant.now().toString()));
            json.append("  },\n");

            // Results -> runs
            json.append("  \"runs\": [\n");
            for (int i = 0; i < results.size(); i++) {
                org.episteme.benchmarks.benchmark.BenchmarkResult r = results.get(i);
                
                String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String resultText;
                if ("SUCCESS".equals(r.status())) {
                    if (r.operationsPerSecond() < 1.0) resultText = String.format(Locale.US, "%.5f ops/s", r.operationsPerSecond());
                    else if (r.operationsPerSecond() < 100.0) resultText = String.format(Locale.US, "%.3f ops/s", r.operationsPerSecond());
                    else resultText = String.format(Locale.US, "%.2f ops/s", r.operationsPerSecond());
                } else {
                    resultText = r.status();
                }
                
                json.append("    {");
                json.append(String.format("\"date\":\"%s\",", escape(dateStr)));
                json.append(String.format("\"name\":\"%s\",", escape(r.benchmarkName())));
                json.append(String.format("\"provider\":\"%s\",", escape(r.provider())));
                json.append(String.format("\"domain\":\"%s\",", escape(r.domain())));
                json.append(String.format("\"result\":\"%s\",", escape(resultText)));
                
                // Add extra metrics
                json.append("\"metrics\":{");
                List<String> mKeys = new ArrayList<>(r.extraMetrics().keySet());
                for (int j = 0; j < mKeys.size(); j++) {
                    String k = mKeys.get(j);
                    Object v = r.extraMetrics().get(k);
                    json.append(String.format("\"%s\":%s", escape(k), v instanceof Number ? v : "\"" + escape(String.valueOf(v)) + "\""));
                    if (j < mKeys.size() - 1) json.append(",");
                }
                json.append("}");
                json.append("}");
                
                if (i < results.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ]\n");
            json.append("}");

            File f = new File(path);
            FileWriter fw = new FileWriter(f);
            fw.write(json.toString());
            fw.close();
            System.out.println("Results exported to " + f.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error exporting JSON: " + e.getMessage());
        }
    }

    private static String escape(String s) {
        if (s == null) return "unknown";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
