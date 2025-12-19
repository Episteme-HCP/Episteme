package org.jscience.benchmark;

import java.util.Map;

/**
 * Captures the performance metrics of a single benchmark run.
 */
public record BenchmarkResult(
        String benchmarkName,
        String domain,
        long totalTimeMillis,
        long iterations,
        double averageTimePerOpMillis,
        double operationsPerSecond,
        long memoryUsedBytes,
        Map<String, Object> extraMetrics) {
    public String toSummaryString() {
        return String.format("%-30s | %-15s | %10.3f ms/op | %10.0f ops/sec | %6d MB",
                benchmarkName, domain, averageTimePerOpMillis, operationsPerSecond, memoryUsedBytes / (1024 * 1024));
    }
}
