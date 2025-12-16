package org.jscience.mathematics.linearalgebra.matrices.storage;

/**
 * Internal storage abstraction for matrices.
 * <p>
 * Different implementations provide optimal storage for different
 * matrix types (dense, sparse, symmetric, etc.).
 * </p>
 * 
 * @param <E> the element type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MatrixStorage<E> {

    /**
     * Returns the element at the specified position.
     * 
     * @param row the row index
     * @param col the column index
     * @return the element at [row, col]
     */
    E get(int row, int col);

    /**
     * Sets the element at the specified position.
     * 
     * @param row   the row index
     * @param col   the column index
     * @param value the value to set
     */
    void set(int row, int col, E value);

    /**
     * Returns the number of rows.
     * 
     * @return the row count
     */
    int rows();

    /**
     * Returns the number of columns.
     * 
     * @return the column count
     */
    int cols();

    /**
     * Creates a deep copy of this storage.
     * 
     * @return a clone of this storage
     */
    MatrixStorage<E> clone();
}
