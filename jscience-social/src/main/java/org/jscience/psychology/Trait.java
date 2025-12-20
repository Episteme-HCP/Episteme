package org.jscience.psychology;

/**
 * Represents a personality trait (e.g., Openness, Conscientiousness).
 * Values are typically normalized between 0.0 and 1.0.
 */
public class Trait {

    private final String name;
    private final String description;
    private double value;

    public Trait(String name, double value) {
        this(name, null, value);
    }

    public Trait(String name, String description, double value) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException("Trait value must be between 0.0 and 1.0");
        }
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException("Trait value must be between 0.0 and 1.0");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "%s: %.2f", name, value);
    }
}
