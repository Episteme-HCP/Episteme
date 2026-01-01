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

package org.jscience.mathematics.linearalgebra.matrices.storage;

/**
 * Internal storage abstraction for matrices.
 * <p>
 * Different implementations provide optimal storage for different
 * matrix types (dense, sparse, symmetric, etc.).
 * </p>
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


