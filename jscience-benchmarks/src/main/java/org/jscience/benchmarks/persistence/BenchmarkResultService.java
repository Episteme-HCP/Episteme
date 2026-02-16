package org.jscience.benchmarks.persistence;

import org.jscience.benchmarks.ui.BenchmarkRunSummary;
import java.util.List;
import java.util.ArrayList;

public class BenchmarkResultService {
    // Simple in-memory storage for now, or file-based later
    private static final List<BenchmarkRunSummary> savedResults = new ArrayList<>();

    public List<BenchmarkRunSummary> loadResults() {
        return new ArrayList<>(savedResults);
    }

    public void saveResults(List<BenchmarkRunSummary> results) {
        savedResults.clear();
        savedResults.addAll(results);
    }
}
