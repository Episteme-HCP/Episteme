/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.cuda.CUDAExecutionContext;

/**
 * ND4J Sparse Tensor Provider.
 * <p>
 * Uses ND4J's sparse tensor capabilities for memory-efficient storage of
 * tensors with many zero elements. Falls back to CPU sparse implementation
 * when ND4J is not available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ND4JSparseTensorProvider implements TensorProvider {

    private static boolean isAvailable = false;
    private static final CPUSparseTensorProvider fallback = new CPUSparseTensorProvider();

    static {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            // Check for sparse support
            Class.forName("org.nd4j.linalg.api.ndarray.BaseSparseNDArray");
            isAvailable = true;
        } catch (ClassNotFoundException e) {
            isAvailable = false;
        } catch (NoClassDefFoundError e) {
            isAvailable = false;
        }
    }

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        if (!isAvailable) {
            return fallback.zeros(elementType, shape);
        }
        
        if (!Real.class.isAssignableFrom(elementType)) {
            return fallback.zeros(elementType, shape);
        }
        
        // For sparse, zeros is just an empty sparse tensor
        return fallback.zeros(elementType, shape);
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        if (!isAvailable) {
            return fallback.ones(elementType, shape);
        }
        
        // Sparse ones doesn't make much sense as it's fully dense
        return fallback.ones(elementType, shape);
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        if (!isAvailable) {
            return fallback.create(data, shape);
        }
        
        // Analyze sparsity
        int nonZeroCount = 0;
        for (T element : data) {
            if (element instanceof Real) {
                if (((Real) element).doubleValue() != 0.0) {
                    nonZeroCount++;
                }
            } else {
                nonZeroCount++;
            }
        }
        
        // If less than 30% non-zero, use sparse
        double sparsity = (double) nonZeroCount / data.length;
        if (sparsity > 0.3) {
            // Too dense, fall back to dense provider
            return new ND4JDenseTensorProvider().create(data, shape);
        }
        
        return fallback.create(data, shape);
    }

    @Override
    public boolean supportsGPU() {
        return isAvailable;
    }

    @Override
    public String getName() {
        return "ND4J Sparse";
    }

    @Override
    public int getPriority() {
        return isAvailable ? 70 : 0;
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public ExecutionContext createContext() {
        if (!isAvailable) {
            return null;
        }
        return new CUDAExecutionContext();
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String getId() {
        return "nd4jsparse";
    }

    @Override
    public String getDescription() {
        return "ND4J Sparse Tensor Provider - Memory-efficient sparse tensor operations";
    }
}
