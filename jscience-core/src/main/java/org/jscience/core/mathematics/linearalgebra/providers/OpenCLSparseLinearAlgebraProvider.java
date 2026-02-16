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




package org.jscience.core.mathematics.linearalgebra.providers;


import org.jscience.core.mathematics.structures.rings.Field;

import org.jscience.core.mathematics.linearalgebra.Matrix;

import org.jscience.core.mathematics.linearalgebra.Vector;


import org.jscience.core.technical.backend.gpu.opencl.OpenCLBackend;

/**
 * OpenCL Linear Algebra Provider (Sparse).
 * <p>
 * Placeholder for sparse OpenCL implementation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
import org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import com.google.auto.service.AutoService;

import org.jscience.core.technical.algorithm.AlgorithmProvider;

@AutoService({SparseLinearAlgebraProvider.class, AlgorithmProvider.class})
public class OpenCLSparseLinearAlgebraProvider<E> implements SparseLinearAlgebraProvider<E> {

    private final CPUSparseLinearAlgebraProvider<E> cpuProvider;
    private static final OpenCLBackend backend = new OpenCLBackend();
    private final boolean initialized;

    /**
     * Public no-arg constructor required by ServiceLoader.
     * Defaults to Reals field.
     */
    @SuppressWarnings("unchecked")
    public OpenCLSparseLinearAlgebraProvider() {
        this((Field<E>) org.jscience.core.mathematics.sets.Reals.getInstance());
    }

    public OpenCLSparseLinearAlgebraProvider(Field<E> field) {
        this.cpuProvider = new CPUSparseLinearAlgebraProvider<>(field);
        this.initialized = true;
        if (isAvailable()) {
            java.util.logging.Logger.getLogger(getClass().getName()).info(
                    "OpenCLSparseLinearAlgebraProvider initialized (Warning: Sparse GPU ops delegated to CPU in this version)");
        }
    }

    @Override
    public boolean isAvailable() {
        return initialized && backend.isAvailable();
    }

    public String getName() {
        return "Native OpenCL (Sparse)";
    }


    @Override
    public int getPriority() {
        return 10;
    }

    // Delegate to CPU Sparse for now
    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        return cpuProvider.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        return cpuProvider.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return cpuProvider.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return cpuProvider.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        return cpuProvider.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        return cpuProvider.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return cpuProvider.solve(a, b); // Sparse solver?
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return cpuProvider.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return cpuProvider.scale(scalar, a);
    }


    @Override
    public E norm(Vector<E> a) {
        return cpuProvider.norm(a);
    }
}



