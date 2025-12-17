package org.jscience.mathematics.linearalgebra.vectors.storage;

import java.util.Arrays;

/**
 * Dense storage for vectors using a standard array.
 *
 * @param <E> the element type
 */
public class DenseVectorStorage<E> implements VectorStorage<E> {
    private final E[] data;
    private final int dimension;

    public DenseVectorStorage(int dimension) {
        this.dimension = dimension;
        // Generic array creation requires reflection or Object[] cast.
        // For storage, we might just store Object[] if E is not reified, but here we
        // assume caller might provide array or we cast.
        // Actually, to be type-safe without passing Class<E>, we might have to use
        // Object[] and cast on get.
        // However, GenericVector usually knows the class.
        // For simplicity in this refactor, we'll use Object[] internally and cast.
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) new Object[dimension];
        this.data = arr;
    }

    public DenseVectorStorage(E[] data) {
        this.dimension = data.length;
        this.data = data;
    }

    public DenseVectorStorage(java.util.List<E> data) {
        this.dimension = data.size();
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) data.toArray();
        this.data = arr;
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public E get(int index) {
        return data[index];
    }

    @Override
    public void set(int index, E value) {
        data[index] = value;
    }

    @Override
    public VectorStorage<E> copy() {
        return new DenseVectorStorage<>(Arrays.copyOf(data, dimension));
    }
}
