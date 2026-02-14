package org.jscience.benchmarks.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BenchmarkRunSummary {
    private final StringProperty date;
    private final StringProperty name;
    private final StringProperty result;

    public BenchmarkRunSummary(String name, String result) {
        this.date = new SimpleStringProperty(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        this.name = new SimpleStringProperty(name);
        this.result = new SimpleStringProperty(result);
    }
    
    // Constructor for loading from persistence if needed
     public BenchmarkRunSummary(String date, String name, String result) {
        this.date = new SimpleStringProperty(date);
        this.name = new SimpleStringProperty(name);
        this.result = new SimpleStringProperty(result);
    }

    public StringProperty dateProperty() { return date; }
    public StringProperty nameProperty() { return name; }
    public StringProperty resultProperty() { return result; }
    
    public String getDate() { return date.get(); }
    public String getName() { return name.get(); }
    public String getResult() { return result.get(); }
}
