/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.waves.electromagnetism;

import org.episteme.core.mathematics.geometry.Vector4D;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Maxwell's Equations providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MaxwellProvider extends AlgorithmProvider {

    double[][] computeTensor(Vector4D point);
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
