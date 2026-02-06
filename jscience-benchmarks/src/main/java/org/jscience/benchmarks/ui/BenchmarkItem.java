package org.jscience.benchmarks.ui;

import javafx.beans.property.*;
import org.jscience.benchmarks.benchmark.RunnableBenchmark;

/**
 * Model class for the Benchmark TreeTableView.
 */
public class BenchmarkItem {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty domain = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty("Pending");
    private final StringProperty result = new SimpleStringProperty("-");
    private final RunnableBenchmark benchmark;

    public BenchmarkItem(String name, String domain, RunnableBenchmark benchmark) {
        this.name.set(name);
        this.domain.set(domain);
        this.benchmark = benchmark;
    }

    // Standard getters for properties to allow binding
    public StringProperty nameProperty() { return name; }
    public StringProperty domainProperty() { return domain; }
    public StringProperty statusProperty() { return status; }
    public StringProperty resultProperty() { return result; }
    
    public RunnableBenchmark getBenchmark() { return benchmark; }
    public String getName() { return name.get(); }
}
