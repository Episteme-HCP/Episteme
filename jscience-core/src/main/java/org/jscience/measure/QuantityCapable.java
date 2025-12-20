/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.measure;

/**
 * Marker interface for classes supporting Quantity-based APIs.
 * <p>
 * Classes implementing this interface guarantee type-safe physical
 * quantity parameters using {@link Quantity} instead of raw doubles.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface QuantityCapable {

    /**
     * Returns true if this object can work with Quantity parameters.
     * Default implementation returns true.
     */
    default boolean supportsQuantityAPI() {
        return true;
    }
}
