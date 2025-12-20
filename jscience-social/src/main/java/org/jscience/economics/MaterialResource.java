package org.jscience.economics;

import org.jscience.geography.Place;

import java.util.UUID;

/**
 * A class representing a modern (material) resource.
 */
public class MaterialResource<Q extends javax.measure.Quantity<Q>> extends Resource<Q> {

    // Using UUID string for identification instead of legacy Identification class
    private final String identification;

    // Value of the single unit of this resource? Or the total pile?
    // Usually price * quantity. Let's keep it simple: "Value" is the estimated
    // monetary worth.
    private Money value;

    public MaterialResource(String name, String description, double quantity, Organization producer, Place place,
            Money value) {
        super(name, description, quantity, producer, place);
        this.identification = UUID.randomUUID().toString();
        this.value = value;
    }

    public String getIdentification() {
        return identification;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MaterialResource))
            return false;
        MaterialResource<?> that = (MaterialResource<?>) o;
        return identification.equals(that.identification);
    }

    @Override
    public int hashCode() {
        return identification.hashCode();
    }
}
