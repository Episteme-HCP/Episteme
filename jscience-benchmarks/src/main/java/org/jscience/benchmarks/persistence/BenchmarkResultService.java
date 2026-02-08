package org.jscience.benchmarks.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jscience.benchmarks.ui.BenchmarkRunSummary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkResultService {

    private static final String FILE_path = System.getProperty("user.home") + "/.jscience/benchmark_history.json";
    private final ObjectMapper mapper;
    
    public BenchmarkResultService() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        File dir = new File(System.getProperty("user.home") + "/.jscience");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public void saveResults(List<BenchmarkRunSummary> results) {
        try {
            mapper.writeValue(new File(FILE_path), results);
        } catch (IOException e) {
            System.err.println("Failed to save benchmark history: " + e.getMessage());
        }
    }
    
    public List<BenchmarkRunSummary> loadResults() {
        File file = new File(FILE_path);
        if (!file.exists()) return new ArrayList<>();
        
        try {
            return mapper.readValue(file, new TypeReference<List<BenchmarkRunSummary>>() {});
        } catch (IOException e) {
            System.err.println("Failed to load benchmark history: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
