/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.backends;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.technical.backend.ExecutionContext;

/**
 * Colt Linear Algebra Provider.
 * Delegate to CPU for now as fallback.
 */
public class ColtLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private CPUDenseLinearAlgebraProvider<E> cpuProvider;

    public ColtLinearAlgebraProvider() {
        this.cpuProvider = null;
    }

    public ColtLinearAlgebraProvider(Field<E> field) {
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
    }
    
    @Override
    public String getName() {
        return "Colt";
    }

    @Override
    public boolean isAvailable() {
        return false; // Not yet implemented
    }

    @Override
    public ExecutionContext createContext() {
         return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

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
        return cpuProvider.solve(a, b);
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

     @Override
    public String getId() {
        return "colt";
    }

    @Override
    public String getDescription() {
        return "ColtLinearAlgebraProvider";
    }
}
