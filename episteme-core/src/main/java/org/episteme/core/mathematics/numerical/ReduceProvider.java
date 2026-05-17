/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.numerical;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.nio.DoubleBuffer;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

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
     * Reduces an array to a single value.
     *
     * @param operation Reduction operation: "sum", "max", "min", "prod"
     * @param input Input array
     * @return Reduced value
     */
    float reduce(String operation, float[] input);

    /**
     * Reduces an array to a single value.
     *
     * @param operation Reduction operation: "sum", "max", "min", "prod"
     * @param input Input array
     * @return Reduced value
     */
    double reduce(String operation, double[] input);

    /**
     * Reduces an array to a single value.
     *
     * @param operation Reduction operation: "sum", "max", "min", "prod"
     * @param input Input array
     * @return Reduced value
     */
    Real reduce(String operation, Real[] input);
    
    /**
     * Reduces a MemorySegment to a single value.
     */
    float reduce(String operation, MemorySegment input, ValueLayout.OfFloat layout, long count);

    /**
     * Reduces a MemorySegment to a single value.
     */
    double reduce(String operation, MemorySegment input, ValueLayout.OfDouble layout, long count);

    /**
     * Reduces an array to a single value (sum, max, min, etc.).
     *
     * @param operation Reduction operation: "sum", "max", "min", "prod"
     * @param input Input buffer
     * @param size Number of elements
     * @return Reduced value
     * @deprecated Use primitive array versions or MemorySegment-based versions for better performance.
     */
    @Deprecated
    double reduce(String operation, DoubleBuffer input, int size);

    @Override
    default String getAlgorithmType() {
        return "reduce";
    }
}
