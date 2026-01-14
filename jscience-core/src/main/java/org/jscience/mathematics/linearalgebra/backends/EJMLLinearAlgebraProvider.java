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
 * EJML Linear Algebra Provider.
 * <p>
 * Uses EJML (Efficient Java Matrix Library) for high-performance linear algebra
 * operations. Falls back to CPU provider for non-Real types or when EJML
 * is not available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EJMLLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private static boolean ejmlAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private LinearAlgebraProvider<E> ejmlImpl;

    static {
        try {
            Class.forName("org.ejml.simple.SimpleMatrix");
            Class.forName("org.ejml.dense.row.CommonOps_DDRM");
            Class.forName("org.jscience.mathematics.linearalgebra.backends.EJMLSupport");
            ejmlAvailable = true;
        } catch (ClassNotFoundException e) {
            ejmlAvailable = false;
        }
    }

    public EJMLLinearAlgebraProvider() {
        this(null);
    }

    public EJMLLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
        
        if (ejmlAvailable && field != null) {
            try {
                Class<?> clazz = Class.forName("org.jscience.mathematics.linearalgebra.backends.EJMLSupport");
                @SuppressWarnings("unchecked")
                Constructor<LinearAlgebraProvider<E>> ctor = (Constructor<LinearAlgebraProvider<E>>) clazz.getConstructor(Field.class);
                this.ejmlImpl = ctor.newInstance(field);
            } catch (Throwable t) {
                // Creation failed, fallback
                this.ejmlImpl = null;
            }
        }
    }

    @Override
    public String getName() {
        return "EJML";
    }

    @Override
    public boolean isAvailable() {
        return ejmlAvailable;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public int getPriority() {
        return ejmlAvailable ? 80 : 0;
    }

    private boolean canUseEJML() {
        return ejmlImpl != null && field != null && 
               (field instanceof org.jscience.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!canUseEJML()) return cpuProvider.add(a, b);
        return ejmlImpl.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!canUseEJML()) return cpuProvider.subtract(a, b);
        return ejmlImpl.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!canUseEJML()) return cpuProvider.multiply(vector, scalar);
        return ejmlImpl.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!canUseEJML()) return cpuProvider.dot(a, b);
        return ejmlImpl.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!canUseEJML()) return cpuProvider.add(a, b);
        return ejmlImpl.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!canUseEJML()) return cpuProvider.subtract(a, b);
        return ejmlImpl.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!canUseEJML()) return cpuProvider.multiply(a, b);
        return ejmlImpl.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!canUseEJML()) return cpuProvider.multiply(a, b);
        return ejmlImpl.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!canUseEJML()) return cpuProvider.inverse(a);
        return ejmlImpl.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (!canUseEJML()) return cpuProvider.determinant(a);
        return ejmlImpl.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!canUseEJML()) return cpuProvider.solve(a, b);
        return ejmlImpl.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!canUseEJML()) return cpuProvider.transpose(a);
        return ejmlImpl.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!canUseEJML()) return cpuProvider.scale(scalar, a);
        return ejmlImpl.scale(scalar, a);
    }

    @Override
    public E norm(Vector<E> a) {
        if (!canUseEJML()) return cpuProvider.norm(a);
        return ejmlImpl.norm(a);
    }
}
