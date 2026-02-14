/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.geometry.Vector4D;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Interface for Maxwell's Equations providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MaxwellProvider extends AlgorithmProvider {

    /**
     * Computes the electromagnetic field tensor components at a given spacetime point using double precision.
     */
    double[][] computeTensor(Vector4D point);

    /**
     * Computes the electromagnetic field tensor components at a given spacetime point using Real precision.
     */
    Real[][] computeTensorReal(Vector4D point);

    @Override
    default String getName() {
        return "Maxwell Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Maxwell Equations";
    }
}
