/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.inference;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.TensorProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

import java.lang.reflect.Array;

/**
 * CPU Dense implementation of Neural Network tensor operations.
 * <p>
 * Uses {@link DenseTensor} (the standard CPU-backed tensor) to provide
 * full implementations of zeros, ones, and create. This provider works
 * on any system without GPU requirements.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService(AlgorithmProvider.class)
public class DenseNeuralProvider implements TensorProvider {

    @Override
    public boolean isAvailable() {
        return true; // Always available — pure CPU implementation
    }

    @Override
    public int getPriority() {
        return 10; // Low priority; GPU providers should take precedence
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        int size = computeSize(shape);
        T[] data = createFilledArray(elementType, size, zeroValue(elementType));
        return new DenseTensor<>(data, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        int size = computeSize(shape);
        T[] data = createFilledArray(elementType, size, oneValue(elementType));
        return new DenseTensor<>(data, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> create(T[] data, int... shape) {
        int expectedSize = computeSize(shape);
        if (data.length != expectedSize) {
            throw new IllegalArgumentException(
                "Data length " + data.length + " does not match shape size " + expectedSize);
        }
        T[] copy = (T[]) Array.newInstance(data.getClass().getComponentType(), data.length);
        System.arraycopy(data, 0, copy, 0, data.length);
        return new DenseTensor<>(copy, shape);
    }

    @Override
    public String getName() {
        return "CPU Dense Neural";
    }

    // ========== Helpers ==========

    private static int computeSize(int[] shape) {
        int size = 1;
        for (int dim : shape) {
            if (dim <= 0) throw new IllegalArgumentException("Dimensions must be positive");
            size *= dim;
        }
        return size;
    }

    @SuppressWarnings("unchecked")
    private static <T> T zeroValue(Class<T> type) {
        if (type == Float.class) return (T) Float.valueOf(0.0f);
        if (type == Double.class) return (T) Double.valueOf(0.0);
        if (type == Integer.class) return (T) Integer.valueOf(0);
        if (type == Long.class) return (T) Long.valueOf(0L);
        if (Real.class.isAssignableFrom(type)) return (T) Real.ZERO;
        throw new UnsupportedOperationException("Unsupported type: " + type.getName());
    }

    @SuppressWarnings("unchecked")
    private static <T> T oneValue(Class<T> type) {
        if (type == Float.class) return (T) Float.valueOf(1.0f);
        if (type == Double.class) return (T) Double.valueOf(1.0);
        if (type == Integer.class) return (T) Integer.valueOf(1);
        if (type == Long.class) return (T) Long.valueOf(1L);
        if (Real.class.isAssignableFrom(type)) return (T) Real.ONE;
        throw new UnsupportedOperationException("Unsupported type: " + type.getName());
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] createFilledArray(Class<T> type, int size, T fillValue) {
        // For boxed types, use Object[] since Array.newInstance for generics
        // We use the wrapper class to create the array
        T[] array;
        if (Real.class.isAssignableFrom(type)) {
            array = (T[]) Array.newInstance(Real.class, size);
        } else {
            array = (T[]) Array.newInstance(type, size);
        }
        java.util.Arrays.fill(array, fillValue);
        return array;
    }
}
