package org.jscience.benchmarks.cli;

import org.jscience.benchmarks.benchmark.BenchmarkRegistry;
import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import org.jscience.benchmarks.ui.BenchmarkItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Command Line Interface for running benchmarks without UI.
 * Facilitates CI integration and automated performance testing.
 */
public class BenchmarkCLI {

    public static void main(String[] args) {
        boolean runAll = false;
        String exportFile = null;

        // Parse arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--run-all".equals(arg)) {
                runAll = true;
            } else if ("--export-file".equals(arg) && i + 1 < args.length) {
                exportFile = args[++i];
            } else if ("--help".equals(arg)) {
                printHelp();
                return;
            }
        }

        if (!runAll) {
            System.out.println("Nothing to run. Use --run-all to execute all benchmarks.");
            printHelp();
            return;
        }

        System.out.println("Starting JScience Benchmarks (CLI Mode)...");
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        System.out.println("Java: " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")");
        System.out.println("Cores: " + Runtime.getRuntime().availableProcessors());

        // Discover Benchmarks
        List<RunnableBenchmark> benchmarks = BenchmarkRegistry.discover();
        System.out.println("Discovered " + benchmarks.size() + " benchmarks.");

        List<BenchmarkResult> results = new ArrayList<>();

        // Warmup & Run
        int success = 0;
        int fail = 0;
        int skipped = 0;

        for (RunnableBenchmark benchmark : benchmarks) {
            BenchmarkItem item = new BenchmarkItem(benchmark);
            System.out.println("----------------------------------------------------------------");
            System.out.println("Running: " + item.getName() + " [" + item.getBackend() + "/" + item.getProvider() + "]");

            if (!benchmark.isAvailable()) {
                System.out.println("Status: SKIPPED (Unavailable)");
                skipped++;
                results.add(new BenchmarkResult(item, "SKIPPED", 0, 0));
                continue;
            }

            try {
                // Setup
                System.out.print("Status: Setup... ");
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

                // Teardown
                benchmark.teardown();
                
                // Stats
                double durationSec = (totalEnd - totalStart) / 1_000_000_000.0;
                double opsSec = iterNanos.size() / durationSec;
                
                // P99
                iterNanos.sort(Long::compareTo);
                int p99Index = Math.max(0, (int) Math.ceil(iterNanos.size() * 0.99) - 1);
                double p99Ms = iterNanos.get(p99Index) / 1_000_000.0;

                System.out.println("DONE");
                System.out.printf(Locale.US, "Result: %.2f ops/s, P99: %.3f ms%n", opsSec, p99Ms);
                
                results.add(new BenchmarkResult(item, "SUCCESS", opsSec, p99Ms));
                success++;

            } catch (Throwable t) {
                System.out.println("FAILED");
                System.out.println("Error: " + t.getMessage());
                // t.printStackTrace(); 
                results.add(new BenchmarkResult(item, "FAILED: " + t.getClass().getSimpleName(), 0, 0));
                fail++;
            }
        }

        System.out.println("================================================================");
        System.out.println("Summary: " + success + " Success, " + fail + " Failed, " + skipped + " Skipped.");
        System.out.println("================================================================");

        // Export JSON
        if (exportFile != null) {
            exportJson(exportFile, results);
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java org.jscience.benchmarks.cli.BenchmarkCLI [options]");
        System.out.println("Options:");
        System.out.println("  --run-all         Run all discovered benchmarks.");
        System.out.println("  --export-file <f> Save results to JSON file.");
        System.out.println("  --help            Show this message.");
    }

    private static void exportJson(String path, List<BenchmarkResult> results) {
        try {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            
            // Metadata
            json.append("  \"metadata\": {\n");
            json.append(String.format("    \"os_name\": \"%s\",\n", escape(System.getProperty("os.name"))));
            json.append(String.format("    \"os_arch\": \"%s\",\n", escape(System.getProperty("os.arch"))));
            json.append(String.format("    \"java_version\": \"%s\",\n", escape(System.getProperty("java.version"))));
            json.append(String.format("    \"processors\": %d,\n", Runtime.getRuntime().availableProcessors()));
            json.append(String.format("    \"timestamp\": \"%s\"\n", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            json.append("  },\n");

            // Results
            json.append("  \"results\": [\n");
            for (int i = 0; i < results.size(); i++) {
                BenchmarkResult r = results.get(i);
                json.append("    {\n");
                json.append(String.format("      \"name\": \"%s\",\n", escape(r.item.getName())));
                json.append(String.format("      \"backend\": \"%s\",\n", escape(r.item.backendProperty().get())));
                json.append(String.format("      \"provider\": \"%s\",\n", escape(r.item.providerProperty().get())));
                json.append(String.format("      \"library\": \"%s\",\n", escape(r.item.libraryProperty().get())));
                json.append(String.format("      \"domain\": \"%s\",\n", escape(r.item.getDomain())));
                json.append(String.format("      \"status\": \"%s\",\n", escape(r.status)));
                json.append(String.format(Locale.US, "      \"ops_per_sec\": %.4f,\n", r.score));
                json.append(String.format(Locale.US, "      \"p99_ms\": %.4f\n", r.p99));
                json.append("    }");
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

    private static class BenchmarkResult {
        BenchmarkItem item;
        String status;
        double score;
        double p99;

        public BenchmarkResult(BenchmarkItem item, String status, double score, double p99) {
            this.item = item;
            this.status = status;
            this.score = score;
            this.p99 = p99;
        }
    }
}
