package org.episteme.benchmarks.cli;

import org.episteme.benchmarks.ui.BenchmarkItem;

/**
 * Data class representing the result of a single benchmark run.
 */
public class BenchmarkResult {
    public BenchmarkItem item;
    public String status;
    public double score;
    public double p99;

    public BenchmarkResult(BenchmarkItem item, String status, double score, double p99) {
        this.item = item;
        this.status = status;
        this.score = score;
        this.p99 = p99;
    }
}
