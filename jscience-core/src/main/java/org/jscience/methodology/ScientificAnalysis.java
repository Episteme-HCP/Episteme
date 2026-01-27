package org.jscience.methodology;


import java.time.Instant;
import java.util.Objects;

import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a scientific analysis linking a sample to experimental results.
 * <p>
 * An analysis is the process of examining a sample within the context of an experiment.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class ScientificAnalysis implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String name;

    @Attribute
    private Instant date;

    @Relation
    private Sample<?> sample;

    @Attribute
    private String result;

    @Attribute
    private String comments;

    /**
     * Creates a new Scientific Analysis.
     * @param name The name of the analysis.
     * @param sample The sample being analyzed.
     */
    public ScientificAnalysis(String name, Sample<?> sample) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(Objects.requireNonNull(name));
        this.sample = Objects.requireNonNull(sample);
        this.date = Instant.now();
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public Identification getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Sample<?> getSample() {
        return sample;
    }

    public void setSample(Sample<?> sample) {
        this.sample = sample;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return String.format("Analysis{name='%s', date=%s, sample=%s}", name, date, sample.getName());
    }
}
