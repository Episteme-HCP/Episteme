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
    private final SimpleStringProperty suiteName = new SimpleStringProperty();
    private final SimpleIntegerProperty successfulCount = new SimpleIntegerProperty();

    public BenchmarkRunSummary(String suiteName, int successfulCount) {
        this.date.set(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.suiteName.set(suiteName);
        this.successfulCount.set(successfulCount);
    }

    public String getDate() { return date.get(); }
    public SimpleStringProperty dateProperty() { return date; }

    public String getSuiteName() { return suiteName.get(); }
    public SimpleStringProperty suiteNameProperty() { return suiteName; }

    public int getSuccessfulCount() { return successfulCount.get(); }
    public SimpleIntegerProperty successfulCountProperty() { return successfulCount; }
}
