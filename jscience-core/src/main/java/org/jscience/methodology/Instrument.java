package org.jscience.methodology;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a measuring instrument used in scientific experiments.
 * <p>
 * Instruments provide the means to capture observations and measurements.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Instrument implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String name;

    @Attribute
    private String model;

    @Attribute
    private String precisionDescription;

    @Attribute
    private Real sensitivity;

    @Attribute
    private String manufacturer;

    /**
     * Creates a new Instrument.
     * @param name The name of the instrument.
     */
    public Instrument(String name) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrecisionDescription() {
        return precisionDescription;
    }

    public void setPrecisionDescription(String precisionDescription) {
        this.precisionDescription = precisionDescription;
    }

    public Real getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(Real sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return String.format("Instrument{name='%s', model='%s'}", name, model);
    }
}
