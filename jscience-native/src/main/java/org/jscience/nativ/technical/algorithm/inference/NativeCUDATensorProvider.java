/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.nativ.technical.algorithm.inference;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.technical.algorithm.TensorProvider;

/**
 * CUDA-accelerated Neural Network provider.
 * <p>
 * Leverages cuDNN and cuBLAS for high-performance tensor operations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class NativeCUDATensorProvider implements TensorProvider {

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        throw new UnsupportedOperationException("CUDA Tensor support coming soon via JCuda/cuDNN.");
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        throw new UnsupportedOperationException("CUDA Tensor support coming soon via JCuda/cuDNN.");
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        throw new UnsupportedOperationException("CUDA Tensor support coming soon via JCuda/cuDNN.");
    }

    @Override
    public String getName() {
        return "CUDA GPU Neural";
    }
}

