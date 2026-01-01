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

package org.jscience.mathematics.linearalgebra.vectors;

import org.jscience.ComputeContext;
import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;
import org.jscience.mathematics.linearalgebra.matrices.GenericMatrix;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.jscience.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * A generic vector implementation using abstract storage and provider
 * delegation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GenericVector<E> implements Vector<E> {

    protected final VectorStorage<E> storage;
    protected LinearAlgebraProvider<E> provider;
    protected final Field<E> field;

    public GenericVector(VectorStorage<E> storage, LinearAlgebraProvider<E> provider, Field<E> field) {
        this.storage = storage;
        this.provider = provider;
        this.field = field;
    }

    /**
     * Creates a GenericVector with automatic storage selection.
     */
    public GenericVector(java.util.List<E> data, Field<E> field) {
        this(VectorFactory.createAutomaticStorage(data, field),
                ComputeContext.current().getDenseLinearAlgebraProvider(field),
                field);
    }

    public static <T> GenericVector<T> of(T[] data, Field<T> field) {
        // Default to Dense Storage
        VectorStorage<T> storage = new DenseVectorStorage<>(data);

        // Select Provider based on Context
        LinearAlgebraProvider<T> provider = ComputeContext.current().getDenseLinearAlgebraProvider(field);

        return new GenericVector<>(storage, provider, field);
    }

    // ================= Conversions =================

    public Matrix<E> toMatrix() {
        // Convert vector to Column Matrix (n x 1)
        @SuppressWarnings("unchecked")
        E[][] matrixData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), storage.dimension(), 1);
        for (int i = 0; i < storage.dimension(); i++) {
            @SuppressWarnings("unchecked")
            E[] row = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), 1);
            row[0] = storage.get(i);
            matrixData[i] = row;
        }
        return GenericMatrix.of(matrixData, field);
    }

    public Tensor<E> toTensor() {
        @SuppressWarnings("unchecked")
        E[] data = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), dimension());
        for (int i = 0; i < dimension(); i++) {
            data[i] = get(i);
        }
        return org.jscience.mathematics.linearalgebra.tensors.TensorFactory.of(data, dimension());
    }

    // ================= Vector<E> Implementation =================

    @Override
    public int dimension() {
        return storage.dimension();
    }

    public void set(int index, E value) {
        storage.set(index, value);
    }

    @Override
    public E get(int index) {
        return storage.get(index);
    }

    @Override
    public Vector<E> add(Vector<E> other) {
        if (other instanceof GenericVector) {
            return provider.add(this, (GenericVector<E>) other);
        }
        // Fallback
        if (other.dimension() != dimension())
            throw new IllegalArgumentException("Dim mismatch");

        // Manually create dense result for fallback
        // We need reflection for array creation if using DenseVectorStorage directly
        // with array,
        // or just use setters.
        DenseVectorStorage<E> newStorage = new DenseVectorStorage<>(dimension());
        for (int i = 0; i < dimension(); ++i) {
            newStorage.set(i, field.add(storage.get(i), other.get(i)));
        }
        return new GenericVector<>(newStorage, provider, field);
    }

    @Override
    public Vector<E> subtract(Vector<E> other) {
        if (other instanceof GenericVector) {
            return provider.subtract(this, (GenericVector<E>) other);
        }
        if (other.dimension() != dimension())
            throw new IllegalArgumentException("Dim mismatch");

        DenseVectorStorage<E> newStorage = new DenseVectorStorage<>(dimension());
        for (int i = 0; i < dimension(); ++i) {
            E neg = field.negate(other.get(i));
            newStorage.set(i, field.add(storage.get(i), neg));
        }
        return new GenericVector<>(newStorage, provider, field);
    }

    @Override
    public Vector<E> multiply(E scalar) {
        return provider.multiply(this, scalar);
    }

    public Vector<E> scale(E scalar) {
        return multiply(scalar);
    }

    @Override
    public Vector<E> negate() {
        return multiply(field.negate(field.one()));
    }

    @Override
    public E dot(Vector<E> other) {
        if (other instanceof GenericVector) {
            return provider.dot(this, (GenericVector<E>) other);
        }
        E sum = field.zero();
        for (int i = 0; i < dimension(); ++i) {
            sum = field.add(sum, field.multiply(get(i), other.get(i)));
        }
        return sum;
    }

    @Override
    public E norm() {
        return provider.norm(this);
    }

    @Override
    public Ring<E> getScalarRing() {
        return field;
    }

    @Override
    public Vector<E> scale(E scalar, Vector<E> element) {
        return element.multiply(scalar);
    }

    public VectorStorage<E> getStorage() {
        return storage;
    }

    public Field<E> getField() {
        return field;
    }

    // --- Default implementations for Ring/Module ---

    @Override
    public boolean contains(Vector<E> element) {
        return element.dimension() == this.dimension();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Vector<E> inverse(Vector<E> element) {
        return element.negate();
    }

    @Override
    public Vector<E> operate(Vector<E> left, Vector<E> right) {
        return left.add(right);
    }

    @Override
    public String description() {
        return "Generic Vector Space V^" + dimension();
    }
}
