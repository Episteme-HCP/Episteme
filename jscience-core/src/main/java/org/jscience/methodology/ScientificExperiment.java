/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.methodology;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jscience.bibliography.Citation;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A robust, persistent implementation of the {@link Experiment} interface.
 * <p>
 * This class serves as a base for specific scientific experiments (e.g., in Psychology, Physics).
 * It includes support for authorship, bibliographic references, and observation tracking.
 * </p>
 *
 * @param <I> The input configuration type
 * @param <R> The result type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public abstract class ScientificExperiment<I, R> implements Experiment<I, R> {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String name;

    @Attribute
    private String description;

    @Attribute
    private final List<String> authors;

    @Attribute
    private final List<Citation> references;

    @Attribute
    private final List<Observation<R>> observations;

    @Attribute
    private Instant startDate;

    @Attribute
    private Instant endDate;

    /**
     * Creates a new Scientific Experiment.
     * @param name The name of the experiment.
     */
    public ScientificExperiment(String name) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        this.authors = new ArrayList<>();
        this.references = new ArrayList<>();
        this.observations = new CopyOnWriteArrayList<>();
        this.startDate = Instant.now();
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(String author) {
        if (author != null) authors.add(author);
    }

    public List<Citation> getReferences() {
        return Collections.unmodifiableList(references);
    }

    public void addReference(Citation citation) {
        if (citation != null) references.add(citation);
    }

    public List<Observation<R>> getObservations() {
        return Collections.unmodifiableList(observations);
    }

    /**
     * Records an observation derived from the experiment execution.
     */
    protected void addObservation(Observation<R> observation) {
        if (observation != null) {
            observations.add(observation);
        }
    }

    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }

    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }
}
