/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.linearalgebra.Tensor;

/**
 * Service provider interface for tensor operations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface TensorProvider extends AlgorithmProvider {

    <T> Tensor<T> zeros(Class<T> elementType, int... shape);
    
    <T> Tensor<T> ones(Class<T> elementType, int... shape);
    
    <T> Tensor<T> create(T[] data, int... shape);

    @Override
    default String getName() {
        return "Tensor Provider";
    }
}
