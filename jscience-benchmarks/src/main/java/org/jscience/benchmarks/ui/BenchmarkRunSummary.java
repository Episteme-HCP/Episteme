package org.jscience.benchmarks.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BenchmarkRunSummary {
    private final StringProperty date;
    private final StringProperty name;
    private final StringProperty backend;
    private final StringProperty provider;
    private final StringProperty domain;
    private final StringProperty result;
    private final StringProperty library;

    public BenchmarkRunSummary(String name, String backend, String provider, String library, String domain, String result) {
        this(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
             name, backend, provider, library, domain, result);
    }

    // Constructor for persistence (7 args)
    public BenchmarkRunSummary(String date, String name, String backend, String provider, String library, String domain, String result) {
        this.date = new SimpleStringProperty(date);
        this.name = new SimpleStringProperty(name);
        this.backend = new SimpleStringProperty(backend);
        this.provider = new SimpleStringProperty(provider);
        this.library = new SimpleStringProperty(library);
        this.domain = new SimpleStringProperty(domain);
        this.result = new SimpleStringProperty(result);
    }

    public StringProperty dateProperty() { return date; }
    public StringProperty nameProperty() { return name; }
    public StringProperty backendProperty() { return backend; }
    public StringProperty providerProperty() { return provider; }
    public StringProperty libraryProperty() { return library; }
    public StringProperty domainProperty() { return domain; }
    public StringProperty resultProperty() { return result; }

    // Legacy compatibility
    public StringProperty suiteProperty() { return domain; }

    public String getDate() { return date.get(); }
    public String getName() { return name.get(); }
    public String getBackend() { return backend.get(); }
    public String getProvider() { return provider.get(); }
    public String getLibrary() { return library.get(); }
    public String getDomain() { return domain.get(); }
    public String getResult() { return result.get(); }
    public String getSuite() { return domain.get(); }

    /**
     * Serializes this summary entry to a JSON string.
     */
    public String toJson() {
        return String.format(
            "{\"date\":\"%s\",\"name\":\"%s\",\"backend\":\"%s\",\"provider\":\"%s\",\"library\":\"%s\",\"domain\":\"%s\",\"result\":\"%s\"}",
            escape(getDate()), escape(getName()), escape(getBackend()),
            escape(getProvider()), escape(getLibrary()), escape(getDomain()), escape(getResult()));
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
