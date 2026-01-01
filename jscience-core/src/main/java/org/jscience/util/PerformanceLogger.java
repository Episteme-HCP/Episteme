/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Lightweight utility for tracking high-performance computing metrics.
 * <p>
 * This logger tracks cumulative time execution counts for specific keys (e.g.
 * "OpenCL:Transfer", "OpenCL:Compute").
 * It is designed to be minimal overhead.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PerformanceLogger {

    // Key -> Total Time in Nanoseconds
    private static final Map<String, AtomicLong> timings = new ConcurrentHashMap<>();
    // Key -> Count of operations
    private static final Map<String, AtomicLong> counts = new ConcurrentHashMap<>();

    // Key -> Context (e.g. "Dense/OpenCL")
    private static final Map<String, String> contexts = new ConcurrentHashMap<>();

    private static boolean enabled = true;

    /**
     * Records a timing metric with context.
     * 
     * @param key           unique identifier
     * @param context       descriptive context (e.g. "Dense/OpenCL")
     * @param durationNanos duration in nanoseconds
     */
    public static void log(String key, String context, long durationNanos) {
        if (!enabled)
            return;
        log(key, durationNanos);
        contexts.putIfAbsent(key, context);
    }

    /**
     * Records a timing metric.
     * 
     * @param key           unique identifier for the operation
     * @param durationNanos duration in nanoseconds
     */
    public static void log(String key, long durationNanos) {
        if (!enabled)
            return;
        timings.computeIfAbsent(key, k -> new AtomicLong()).addAndGet(durationNanos);
        counts.computeIfAbsent(key, k -> new AtomicLong()).incrementAndGet();
    }

    /**
     * resets all metrics.
     */
    public static void reset() {
        timings.clear();
        counts.clear();
        contexts.clear();
    }

    public static void setEnabled(boolean e) {
        enabled = e;
    }

    /**
     * Returns a formatted report of all collected metrics.
     */
    public static String getReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== JScience Performance Report ===\n");
        sb.append(String.format("%-30s | %-20s | %-10s | %-15s | %-15s\n", "Operation", "Context", "Count",
                "Total (ms)", "Avg (ms)"));
        sb.append(
                "----------------------------------------------------------------------------------------------------\n");

        timings.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    String key = e.getKey();
                    String context = contexts.getOrDefault(key, "-");
                    long totalNanos = e.getValue().get();
                    long count = counts.get(key).get();
                    double totalMs = totalNanos / 1_000_000.0;
                    double avgMs = count > 0 ? totalMs / count : 0.0;

                    sb.append(String.format("%-30s | %-20s | %-10d | %-15.3f | %-15.3f\n", key, context, count, totalMs,
                            avgMs));
                });
        sb.append("===================================\n");
        return sb.toString();
    }
}

