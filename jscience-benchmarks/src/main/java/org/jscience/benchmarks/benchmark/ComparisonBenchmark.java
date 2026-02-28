package org.episteme.benchmarks.benchmark;

import java.util.Map;
import java.util.HashMap;

/**
 * A benchmark that compares multiple implementations of the same algorithm.
 */
public abstract class ComparisonBenchmark implements RunnableBenchmark {

    private final String id;
    private final String name;
    private final String description;
    private final String domain;
    private final Map<String, RunnableBenchmark> implementations = new java.util.LinkedHashMap<>();
    private RunnableBenchmark activeImplementation;

    public ComparisonBenchmark(String id, String name, String description, String domain) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.domain = domain;
    }

    @Override public String getId() { return id + (activeImplementation != null ? "-" + activeImplementation.getId() : ""); }
    @Override public String getName() { return name + (activeImplementation != null ? " [" + activeImplementation.getName() + "]" : ""); }
    @Override public String getDescription() { return description; }
    @Override public String getDomain() { return domain; }

    public void addImplementation(String name, RunnableBenchmark impl) {
        implementations.put(name, impl);
        if (activeImplementation == null) {
            activeImplementation = impl;
        }
    }

    public void setImplementation(String name) {
        RunnableBenchmark impl = implementations.get(name);
        if (impl != null) {
            this.activeImplementation = impl;
        }
    }

    public Map<String, RunnableBenchmark> getImplementations() {
        return java.util.Collections.unmodifiableMap(implementations);
    }

    @Override
    public void setup() {
        if (activeImplementation != null) {
            activeImplementation.setup();
        }
    }

    @Override
    public void run() {
        if (activeImplementation != null) {
            activeImplementation.run();
        }
    }

    @Override
    public void teardown() {
        if (activeImplementation != null) {
            activeImplementation.teardown();
        }
    }

    @Override
    public int getSuggestedIterations() {
        return activeImplementation != null ? activeImplementation.getSuggestedIterations() : 10;
    }

    @Override
    public Map<String, String> getMetadata() {
        Map<String, String> meta = new HashMap<>(RunnableBenchmark.super.getMetadata());
        meta.put("implementations", String.join(", ", implementations.keySet()));
        if (activeImplementation != null) {
            meta.putAll(activeImplementation.getMetadata());
            meta.put("active_implementation", activeImplementation.getName());
            meta.put("active_implementation_class", activeImplementation.getClass().getName());
        }
        return meta;
    }
}
