/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.numerical;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import java.nio.DoubleBuffer;

/**
 * Reduction operation algorithm provider.
 * <p>
 * Provides parallel reduction operations (sum, max, min, product)
 * with CPU and GPU implementations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface ReduceProvider extends AlgorithmProvider {

    /**
     * Reduces an array to a single value (sum, max, min, etc.).
     *
     * @param operation Reduction operation: "sum", "max", "min", "prod"
     * @param input Input buffer
     * @param size Number of elements
     * @return Reduced value
     */
    double reduce(String operation, DoubleBuffer input, int size);

    @Override
    default String getAlgorithmType() {
        return "reduce";
    }
}
