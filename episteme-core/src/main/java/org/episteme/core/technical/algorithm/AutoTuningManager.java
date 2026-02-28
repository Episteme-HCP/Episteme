package org.episteme.core.technical.algorithm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.episteme.core.io.UserPreferences;

/**
 * Manages performance-based tuning for providers.
 * Loads benchmark results and provides scoring logic.
 */
public class AutoTuningManager {

    public enum Mode {
        ON, OFF, AUTO
    }
    private static final Logger logger = LoggerFactory.getLogger(AutoTuningManager.class);
    private static final Map<String, BenchmarkResult> RESULTS = new ConcurrentHashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        loadResults();
    }

    public static Mode getMode() {
        return Mode.valueOf(UserPreferences.getInstance().getAutoTuningMode());
    }

    public static void loadResults() {
        try {
            Path dir = Paths.get(System.getProperty("user.home"), ".episteme");
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Path path = dir.resolve("benchmarks.json");
            if (Files.exists(path)) {
                List<BenchmarkResult> list = mapper.readValue(path.toFile(), new TypeReference<List<BenchmarkResult>>() {});
                for (BenchmarkResult res : list) {
                    RESULTS.put(res.getProviderName(), res);
                }
                logger.debug("Loaded {} benchmark results for auto-tuning", RESULTS.size());
            }
        } catch (Exception e) {
            logger.warn("Failed to load benchmark results: {}", e.getMessage());
        }
    }

    public static void saveResults() {
        try {
            Path path = Paths.get(System.getProperty("user.home"), ".episteme", "benchmarks.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), new ArrayList<>(RESULTS.values()));
        } catch (Exception e) {
            logger.error("Failed to save benchmark results: {}", e.getMessage());
        }
    }

    /**
     * Records a live performance sample from an operation.
     */
    public static void recordSample(String providerName, int dim, double durationMs, long ops) {
        if (getMode() == Mode.OFF) return;
        
        double gflops = (ops / (durationMs / 1000.0)) / 1e9;
        
        BenchmarkResult res = RESULTS.computeIfAbsent(providerName, k -> new BenchmarkResult(k, "Unknown", new java.util.concurrent.ConcurrentHashMap<>()));
        res.getGflops().put(dim, gflops);
        
        // Background save periodically or on certain events? For now, save every 10 samples for simplicity.
        // Actually, maybe just keep in memory until app close or manual save.
        // For JIT, we probably want it to persist.
    }

    /**
     * Calculates a dynamic score based on benchmark data.
     * 
     * @param providerName name of the provider
     * @param dim dimensionality (e.g. matrix rows)
     * @param defaultPriority the hardcoded priority
     * @return dynamic score
     */
    public static double getDynamicScore(String providerName, int dim, int defaultPriority) {
        Mode mode = getMode();
        if (mode == Mode.OFF) return defaultPriority;
        
        BenchmarkResult res = RESULTS.get(providerName);
        if (res == null || res.getGflops() == null || res.getGflops().isEmpty()) {
            return defaultPriority;
        }

        // Find nearest benchmarked size
        Map<Integer, Double> gflops = res.getGflops();
        int nearestSize = -1;
        int minDiff = Integer.MAX_VALUE;
        for (int size : gflops.keySet()) {
            int diff = Math.abs(size - dim);
            if (diff < minDiff) {
                minDiff = diff;
                nearestSize = size;
            }
        }

        if (nearestSize == -1) return defaultPriority;

        // Convert GFLOPS to a priority-relative score.
        // We use a logarithmic scale to keep scores within reasonable bounds.
        // Base priority is shifted by GFLOPS * multiplier.
        double perf = gflops.get(nearestSize);
        return defaultPriority + (perf * 10.0); 
    }
}
