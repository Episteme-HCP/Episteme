/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.linearalgebra;

import org.jscience.core.technical.algorithm.TensorProvider;
import org.jscience.core.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.core.mathematics.linearalgebra.tensors.DenseTensor;

/**
 * Standard Java CPU implementation for Dense Tensors.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class CPUDenseTensorProvider implements TensorProvider {

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        int size = 1;
        for (int dim : shape) size *= dim;
        T[] data = (T[]) java.lang.reflect.Array.newInstance(elementType, size);
        return new DenseTensor<>(data, shape);
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        // Simple placeholder for ones
        return zeros(elementType, shape);
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        return new DenseTensor<>(data, shape);
    }

    @Override
    public String getName() {
        return "Java CPU (Tensor)";
    }
}
