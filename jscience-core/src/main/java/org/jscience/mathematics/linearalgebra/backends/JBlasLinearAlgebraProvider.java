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

package org.jscience.mathematics.linearalgebra.backends;

import java.lang.reflect.Constructor;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.cpu.CPUExecutionContext;

/**
 * JBlas Linear Algebra Provider.
 * <p>
 * Uses JBlas (native BLAS/LAPACK) for maximum performance linear algebra
 * operations. Falls back to CPU provider for non-Real types or when JBlas
 * is not available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JBlasLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private static boolean jblasAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private LinearAlgebraProvider<E> jblasImpl;

    static {
        try {
            Class.forName("org.jblas.DoubleMatrix");
            Class.forName("org.jblas.Solve");
            Class.forName("org.jscience.mathematics.linearalgebra.backends.JBlasSupport");
            jblasAvailable = true;
        } catch (ClassNotFoundException e) {
            jblasAvailable = false;
        } catch (UnsatisfiedLinkError e) {
            jblasAvailable = false;
        } catch (Throwable t) {
            jblasAvailable = false;
        }
    }

    public JBlasLinearAlgebraProvider() {
        this(null);
    }

    public JBlasLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
        
        if (jblasAvailable && field != null) {
            try {
                Class<?> clazz = Class.forName("org.jscience.mathematics.linearalgebra.backends.JBlasSupport");
                @SuppressWarnings("unchecked")
                Constructor<LinearAlgebraProvider<E>> ctor = (Constructor<LinearAlgebraProvider<E>>) clazz.getConstructor(Field.class);
                this.jblasImpl = ctor.newInstance(field);
            } catch (Throwable t) {
                // Creation probably failed due to linkage error or missing implementation
                this.jblasImpl = null;
            }
        }
    }

    @Override
    public String getName() {
        return "JBlas";
    }

    @Override
    public boolean isAvailable() {
        // Double check runtime availability via impl check?
        return jblasAvailable;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public int getPriority() {
        return jblasAvailable ? 90 : 0;
    }

    private boolean canUseJBlas() {
        return jblasImpl != null && field != null && 
               (field instanceof org.jscience.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!canUseJBlas()) return cpuProvider.add(a, b);
        return jblasImpl.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!canUseJBlas()) return cpuProvider.subtract(a, b);
        return jblasImpl.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!canUseJBlas()) return cpuProvider.multiply(vector, scalar);
        return jblasImpl.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!canUseJBlas()) return cpuProvider.dot(a, b);
        return jblasImpl.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!canUseJBlas()) return cpuProvider.add(a, b);
        return jblasImpl.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!canUseJBlas()) return cpuProvider.subtract(a, b);
        return jblasImpl.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!canUseJBlas()) return cpuProvider.multiply(a, b);
        return jblasImpl.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!canUseJBlas()) return cpuProvider.multiply(a, b);
        return jblasImpl.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!canUseJBlas()) return cpuProvider.inverse(a);
        return jblasImpl.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        // Explicit fallback because JBlasSupport doesn't support it
        return cpuProvider.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!canUseJBlas()) return cpuProvider.solve(a, b);
        return jblasImpl.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!canUseJBlas()) return cpuProvider.transpose(a);
        return jblasImpl.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!canUseJBlas()) return cpuProvider.scale(scalar, a);
        return jblasImpl.scale(scalar, a);
    }

    @Override
    public E norm(Vector<E> a) {
        if (!canUseJBlas()) return cpuProvider.norm(a);
        return jblasImpl.norm(a);
    }
}
