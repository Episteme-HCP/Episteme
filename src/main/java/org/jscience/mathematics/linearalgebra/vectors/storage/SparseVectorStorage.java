package org.jscience.mathematics.linearalgebra.vectors.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Sparse storage for vectors using a Map.
 *
 * @param <E> the element type
 */
public class SparseVectorStorage<E> implements VectorStorage<E> {
    private final Map<Integer, E> data;
    private final int dimension;
    private final E zero;

    public SparseVectorStorage(int dimension, E zero) {
        this.dimension = dimension;
        this.data = new HashMap<>(); // Could use a more efficient primitive map if E was Double
        this.zero = zero;
    }

    // Copy constructor
    private SparseVectorStorage(int dimension, E zero, Map<Integer, E> data) {
        this.dimension = dimension;
        this.zero = zero;
        this.data = new HashMap<>(data);
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public E get(int index) {
        return data.getOrDefault(index, zero);
    }

    @Override
    public void set(int index, E value) {
        if (value.equals(zero)) {
            data.remove(index);
        } else {
            data.put(index, value);
        }
    }

    @Override
    public VectorStorage<E> copy() {
        return new SparseVectorStorage<>(dimension, zero, data);
    }
}
