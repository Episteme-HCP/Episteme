package org.jscience.benchmarks.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a summary of a completed benchmarking suite run.
 */
public class BenchmarkRunSummary {
    private final SimpleStringProperty date = new SimpleStringProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty result = new SimpleStringProperty();

    public BenchmarkRunSummary(String name, String result) {
        this.date.set(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.name.set(name);
        this.result.set(result);
    }

    public String getDate() { return date.get(); }
    public SimpleStringProperty dateProperty() { return date; }

    public String getName() { return name.get(); }
    public SimpleStringProperty nameProperty() { return name; }

    public String getResult() { return result.get(); }
    public SimpleStringProperty resultProperty() { return result; }
}
