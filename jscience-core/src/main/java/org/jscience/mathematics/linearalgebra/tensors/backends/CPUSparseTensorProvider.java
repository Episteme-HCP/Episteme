/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.mathematics.linearalgebra.tensors.backends;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.SparseTensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;

/**
 * CPU-based sparse tensor provider. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class CPUSparseTensorProvider implements TensorProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        // Create sparse tensor.
        try {
            T zero;
            if (org.jscience.mathematics.structures.rings.Ring.class.isAssignableFrom(elementType)) {
                org.jscience.mathematics.structures.rings.Ring<?> ringElem = (org.jscience.mathematics.structures.rings.Ring<?>) elementType
                        .getDeclaredConstructor().newInstance();
                zero = (T) ringElem.zero();
            } else if (Real.class.isAssignableFrom(elementType)) {
                zero = (T) Real.ZERO;
            } else {
                // Try default constructor generic fallback
                T instance = elementType.getDeclaredConstructor().newInstance();
                // Try reflection for zero()? Or assume instance is zero-like?
                // For structure-as-element, instance.zero() gives the additive identity.
                try {
                    zero = (T) elementType.getMethod("zero").invoke(instance);
                } catch (Exception e) {
                    // Fallback/Fail
                    throw new IllegalArgumentException("Cannot determine zero for type " + elementType);
                }
            }
            T z = zero;
            return new SparseTensor<>(shape, z);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create zero for " + elementType, e);
        }
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        // Ones is not sparse! But we can return a SparseTensor full of ones?
        // That defeats the purpose.
        // Usually providers return the most appropriate type.
        // If user asks Sparse provider for ones(), they get a generic Tensor (which
        // might be DenseTensor or SparseTensor with many entries).
        // Let's return SparseTensor with all entries filled? That's huge memory usage.
        // User likely wants SparseTensor structure.

        throw new UnsupportedOperationException(
                "Creating 'ones' with Sparse provider effectively creates a dense tensor. Use Dense provider instead.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> create(T[] data, int... shape) {
        // Create from array. Check for zeros.
        // Need zero value.
        if (data.length == 0) {
            Class<T> type = (Class<T>) data.getClass().getComponentType();
            return zeros(type, shape);
        }

        T zero;
        // Determine zero from data[0]?
        if (data[0] instanceof org.jscience.mathematics.structures.rings.Ring) {
            @SuppressWarnings("rawtypes")
            org.jscience.mathematics.structures.rings.Ring r = (org.jscience.mathematics.structures.rings.Ring) data[0];
            T z = (T) r.zero();
            zero = z;
        } else if (data[0] instanceof Real) {
            zero = (T) Real.ZERO;
        } else {
            // Fallback
            try {
                T z = (T) data[0].getClass().getMethod("zero").invoke(data[0]);
                zero = z;
            } catch (Exception e) {
                // Assuming null? No.
                throw new IllegalArgumentException("Cannot determine zero from data element");
            }
        }

        SparseTensor<T> tensor = new SparseTensor<>(shape, zero);

        // Use flat index logic
        // This assumes data is flat array in correct order.
        for (int i = 0; i < data.length; i++) {
            if (!data[i].equals(zero)) {
                // We need to access underlying map efficiently. The interface usage of set(val,
                // indices) is slow if we have to compute indices.
                // SparseTensor internal map uses flat index.
                // We can cast?
                // Let's trust constructor logic or add a helper.
                // But create() uses Tensor interface.
                // SparseTensor constructor that takes map is private/protected? No, public.
                // But we have array.
                // We can just iterate and put? Yes.
                // Ideally we expose a way to set by flat index.
            }
        }
        // Actually, SparseTensor doesn't expose setByFlatIndex.
        // Let's just create dense if data is provided? No, user explicitly wants Sparse
        // provider.

        // Let's implement slow fill for now.
        // Wait, data[] is flat array. SparseTensor uses flat index.
        // I can add a constructor or method in SparseTensor to populate from flat
        // array?
        // No, I shouldn't modify SparseTensor now.

        // I'll make SparseTensor(shape, zero) public.
        // And I'll assume I can just use get/set if I calculate indices.
        // Calculating indices is O(rank). Total O(size * rank).

        // Efficient index calculation using strides
        int[] strides = new int[shape.length];
        strides[shape.length - 1] = 1;
        for (int i = shape.length - 2; i >= 0; i--) {
            strides[i] = strides[i + 1] * shape[i + 1];
        }

        for (int i = 0; i < data.length; i++) {
            if (!data[i].equals(zero)) {
                // Compute indices from flat index 'i'
                int[] indices = new int[shape.length];
                int remaining = i;
                for (int j = 0; j < shape.length; j++) {
                    indices[j] = remaining / strides[j];
                    remaining %= strides[j];
                }
                tensor.set(data[i], indices);
            }
        }
        return tensor;
    }

    @Override
    public boolean supportsGPU() {
        return false;
    }

    @Override
    public String getName() {
        return "CPU Sparse";
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public ExecutionContext createContext() {
        return null; // Local execution
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
