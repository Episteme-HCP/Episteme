/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * Apache Commons Math Linear Algebra Provider.
 * <p>
 * Uses Apache Commons Math library for linear algebra operations.
 * Falls back to CPU provider for non-Real types or when Commons Math
 * is not available.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CommonsMathLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private static boolean commonsMathAvailable = false;
    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private LinearAlgebraProvider<E> cmImpl;

    static {
        try {
            Class.forName("org.apache.commons.math3.linear.RealMatrix");
            Class.forName("org.apache.commons.math3.linear.LUDecomposition");
            Class.forName("org.jscience.mathematics.linearalgebra.backends.CommonsMathSupport");
            commonsMathAvailable = true;
        } catch (ClassNotFoundException e) {
            commonsMathAvailable = false;
        }
    }

    public CommonsMathLinearAlgebraProvider() {
        this(null);
    }

    public CommonsMathLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
        
        if (commonsMathAvailable && field != null) {
            try {
                Class<?> clazz = Class.forName("org.jscience.mathematics.linearalgebra.backends.CommonsMathSupport");
                @SuppressWarnings("unchecked")
                Constructor<LinearAlgebraProvider<E>> ctor = (Constructor<LinearAlgebraProvider<E>>) clazz.getConstructor(Field.class);
                this.cmImpl = ctor.newInstance(field);
            } catch (Throwable t) {
                this.cmImpl = null;
            }
        }
    }

    @Override
    public String getName() {
        return "Commons Math";
    }

    @Override
    public boolean isAvailable() {
        return commonsMathAvailable;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public int getPriority() {
        return commonsMathAvailable ? 50 : 0;
    }

    private boolean canUseCommonsMath() {
        return cmImpl != null && field != null && 
               (field instanceof org.jscience.mathematics.sets.Reals ||
                Real.class.isAssignableFrom(field.zero().getClass()));
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) return cpuProvider.add(a, b);
        return cmImpl.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) return cpuProvider.subtract(a, b);
        return cmImpl.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        if (!canUseCommonsMath()) return cpuProvider.multiply(vector, scalar);
        return cmImpl.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) return cpuProvider.dot(a, b);
        return cmImpl.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!canUseCommonsMath()) return cpuProvider.add(a, b);
        return cmImpl.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!canUseCommonsMath()) return cpuProvider.subtract(a, b);
        return cmImpl.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!canUseCommonsMath()) return cpuProvider.multiply(a, b);
        return cmImpl.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) return cpuProvider.multiply(a, b);
        return cmImpl.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!canUseCommonsMath()) return cpuProvider.inverse(a);
        return cmImpl.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (!canUseCommonsMath()) return cpuProvider.determinant(a);
        return cmImpl.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!canUseCommonsMath()) return cpuProvider.solve(a, b);
        return cmImpl.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!canUseCommonsMath()) return cpuProvider.transpose(a);
        return cmImpl.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!canUseCommonsMath()) return cpuProvider.scale(scalar, a);
        return cmImpl.scale(scalar, a);
    }

    @Override
    public E norm(Vector<E> a) {
        if (!canUseCommonsMath()) return cpuProvider.norm(a);
        return cmImpl.norm(a);
    }
}
