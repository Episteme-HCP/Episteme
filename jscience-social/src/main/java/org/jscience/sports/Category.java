package org.jscience.sports;

import java.util.Objects;

/**
 * Represents a sports category (e.g., Extreme Sports, Team Sports, Combat Sports).
 */
public record Category(String name, String description) {
    public Category {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
    }
}
