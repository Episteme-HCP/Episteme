/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

import org.jscience.util.UniversalDataModel;

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
public class ScientificExperiment<I, R> implements Experiment<I, R>, UniversalDataModel {


    @Override
    public java.util.concurrent.CompletableFuture<R> run(I input) {
        return java.util.concurrent.CompletableFuture.completedFuture(null);
    }

    @Override
    public Hypothesis<R> getHypothesis() {
        return null; // Can be specified by subclasses or via traits
    }

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
    public void record(Observation<R> observation) {
        if (observation != null) {
            observations.add(observation);
        }
    }

    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }

    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }

    // --- UniversalDataModel implementation ---

    @Override
    public String getModelType() {
        return "SCIENTIFIC_EXPERIMENT";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>(traits);
        meta.put("id", id.toString());
        meta.put("name", name);
        meta.put("author_count", authors.size());
        meta.put("start_date", startDate != null ? startDate.toString() : null);
        return meta;
    }

    @Override
    public Map<String, org.jscience.measure.Quantity<?>> getQuantities() {
        Map<String, org.jscience.measure.Quantity<?>> quantities = new HashMap<>();
        quantities.put("observation_count", org.jscience.measure.Quantities.create(observations.size(), org.jscience.measure.Units.ONE));
        if (startDate != null && endDate != null) {
            long durationSec = endDate.getEpochSecond() - startDate.getEpochSecond();
            quantities.put("duration", org.jscience.measure.Quantities.create(durationSec, org.jscience.measure.Units.SECOND));
        }
        return quantities;
    }
}

