/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.numbers.real;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Service Provider Interface (SPI) for high-precision transcendental functions.
 * Backends should implement this to provide arbitrary-precision math.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface TranscendentalProvider extends AlgorithmProvider {

    @Override
    default String getAlgorithmType() {
        return "transcendental";
    }

    /**
     * Computes the transcendental function with the given precision.
     * 
     * @param function the function name (e.g., "exp", "sin")
     * @param value the input value
     * @param mc the math context for precision and rounding
     * @return the result as a BigDecimal
     */
    BigDecimal compute(String function, BigDecimal value, MathContext mc);

    /**
     * Computes a two-argument function (e.g., "pow", "atan2", "hypot").
     * 
     * @param function the function name
     * @param v1 the first value
     * @param v2 the second value
     * @param mc the math context
     * @return the result
     */
    default BigDecimal compute(String function, BigDecimal v1, BigDecimal v2, MathContext mc) {
        throw new UnsupportedOperationException("Function " + function + " not supported by this provider");
    }
}
