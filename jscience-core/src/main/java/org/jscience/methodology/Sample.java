package org.jscience.methodology;

import java.time.Instant;
import java.util.Optional;

import org.jscience.measure.Quantity;
 
import org.jscience.util.Positioned;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a physical or conceptual sample used in a scientific experiment.
 * <p>
 * A sample is the object of study, which can be material (e.g., a rock, a tissue),
 * biological (e.g., an individual), or even social.
 * </p>
 * 
 * @param <P> The type of the position (e.g., GeoCoordinates, Vector3D, String default).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Sample<P> implements ComprehensiveIdentification, Positioned<P> {

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
    private Instant samplingDate;

    @Attribute
    private String category; // e.g., "Rock", "Biological Tissue", "Individual"

    @Attribute
    private Quantity<?> amount; // Mass, Volume, Count, etc.

    @Attribute
    private String source; // Origin of the sample

    @Attribute
    private P position;

    @Attribute
    private String thermalProperties;

    /**
     * Creates a new Sample.
     * @param name The name of the sample.
     */
    public Sample(String name) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(name);
        this.samplingDate = Instant.now();
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

    public Instant getSamplingDate() {
        return samplingDate;
    }

    public void setSamplingDate(Instant samplingDate) {
        this.samplingDate = samplingDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Optional<Quantity<?>> getAmount() {
        return Optional.ofNullable(amount);
    }

    public void setAmount(Quantity<?> amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public P getPosition() {
        return position;
    }

    public void setPosition(P position) {
        this.position = position;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public String toString() {
        return String.format("Sample{name='%s', category='%s', date=%s}", name, category, samplingDate);
    }
}
