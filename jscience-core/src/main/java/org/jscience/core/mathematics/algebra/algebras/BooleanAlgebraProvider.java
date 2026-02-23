/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.mathematics.algebra.algebras;

import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Service provider interface for Boolean Algebra operations.
 */
public interface BooleanAlgebraProvider extends AlgorithmProvider {
    
    Boolean and(Boolean a, Boolean b);
    Boolean or(Boolean a, Boolean b);
    Boolean not(Boolean a);
    Boolean xor(Boolean a, Boolean b);

    @Override
    default String getAlgorithmType() {
        return "Boolean Algebra";
    }
}
