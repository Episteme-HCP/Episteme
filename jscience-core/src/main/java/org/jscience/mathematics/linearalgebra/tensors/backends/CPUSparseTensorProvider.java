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

import org.jscience.technical.backend.ExecutionContext;

/**
 * Dummy CPUSparseTensorProvider.
 */
public class CPUSparseTensorProvider implements TensorProvider {

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        return null;
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        return null; 
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        return null;
    }

    @Override
    public boolean supportsGPU() {
        return false;
    }

    @Override
    public String getName() {
        return "Native Sparse";
    }

    @Override
    public ExecutionContext createContext() {
        return null;
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getId() {
        return "cpusparse";
    }

    @Override
    public String getDescription() {
        return "CPUSparseTensorProvider";
    }
}
