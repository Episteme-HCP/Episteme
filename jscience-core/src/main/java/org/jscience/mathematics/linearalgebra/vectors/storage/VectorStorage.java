package org.jscience.mathematics.linearalgebra.vectors.storage;

import java.io.Serializable;

/**
 * Interface for vector data storage.
 * Abstracts the underlying data structure (array, map, buffer, etc.).
 *
 * @param <E> the element type
 */
public interface VectorStorage<E> extends Serializable {
    /**
     * Returns the dimension of the vector.
     */
    int dimension();

    /**
     * Gets the element at the specified index.
     *
     * @param index the index (0 to dimension-1)
     * @return the element
     */
    E get(int index);

    /**
     * Sets the element at the specified index.
     *
     * @param index the index
     * @param value the value to set
     */
    void set(int index, E value);

    /**
     * Creates a deep copy of this storage.
     */
    VectorStorage<E> copy();
}
