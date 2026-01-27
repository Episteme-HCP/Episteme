package org.jscience.methodology;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a captured data point during an experiment.
 * 
 * @param <T> The data type of the observed value.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Observation<T> implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Instant timestamp;

    @Attribute
    private final T value;

    @Attribute
    private String tag;

    @Attribute
    private final Map<String, Object> traits;

    public Observation(T value) {
        this(Instant.now(), value, "default", new HashMap<>());
    }

    public Observation(Instant timestamp, T value, String tag, Map<String, Object> traits) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        this.timestamp = Objects.requireNonNull(timestamp);
        this.value = Objects.requireNonNull(value);
        this.tag = tag;
        this.traits = traits != null ? new HashMap<>(traits) : new HashMap<>();
    }

    @Override
    public Identification getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public T getValue() {
        return value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public static <T> Observation<T> of(T value) {
        return new Observation<>(value);
    }

    @Override
    public String toString() {
        return String.format("Observation{timestamp=%s, value=%s, tag='%s'}", timestamp, value, tag);
    }
}
