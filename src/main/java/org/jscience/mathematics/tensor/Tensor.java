/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.tensor;

import org.jscience.mathematics.algebra.Field;

/**
 * Represents a multidimensional array (tensor).
 * <p>
 * Tensors generalize scalars (rank 0), vectors (rank 1), and matrices (rank 2)
 * to arbitrary dimensions. They are fundamental in physics, machine learning,
 * and numerical computing.
 * </p>
 * 
 * <h2>Rank and Shape</h2>
 * <ul>
 * <li><b>Rank</b>: Number of dimensions (e.g., rank 3 = 3D tensor)</li>
 * <li><b>Shape</b>: Size of each dimension (e.g., [2, 3, 4] = 2×3×4)</li>
 * <li><b>Size</b>: Total number of elements (product of shape)</li>
 * </ul>
 * 
 * <h2>Example</h2>
 * 
 * <pre>
 * // 2×3 matrix (rank 2 tensor)
 * Tensor&lt;Real&gt; matrix = Tensor.of(new Real[][] {
 *         { Real.of(1), Real.of(2), Real.of(3) },
 *         { Real.of(4), Real.of(5), Real.of(6) }
 * });
 * 
 * // 3D tensor (rank 3)
 * Tensor&lt;Real&gt; tensor3d = Tensor.zeros(Real.class, 2, 3, 4);
 * </pre>
 * 
 * @param <T> the type of elements (must be a Field)
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface Tensor<T extends Field<T>> {

    /**
     * Returns the shape of this tensor.
     * 
     * @return array of dimension sizes
     */
    int[] shape();

    /**
     * Returns the rank (number of dimensions) of this tensor.
     * 
     * @return rank ≥ 0
     */
    int rank();

    /**
     * Returns the total number of elements.
     * 
     * @return size = product of shape
     */
    int size();

    /**
     * Gets the element at the specified indices.
     * 
     * @param indices the indices (length must equal rank)
     * @return the element
     * @throws IndexOutOfBoundsException if indices are invalid
     */
    T get(int... indices);

    /**
     * Sets the element at the specified indices.
     * 
     * @param value   the value to set
     * @param indices the indices (length must equal rank)
     * @throws IndexOutOfBoundsException if indices are invalid
     */
    void set(T value, int... indices);

    /**
     * Element-wise addition.
     * 
     * @param other tensor with same shape
     * @return this + other
     */
    Tensor<T> add(Tensor<T> other);

    /**
     * Element-wise subtraction.
     * 
     * @param other tensor with same shape
     * @return this - other
     */
    Tensor<T> subtract(Tensor<T> other);

    /**
     * Element-wise multiplication (Hadamard product).
     * 
     * @param other tensor with same shape
     * @return this ⊙ other
     */
    Tensor<T> multiply(Tensor<T> other);

    /**
     * Scalar multiplication.
     * 
     * @param scalar the scalar value
     * @return scalar * this
     */
    Tensor<T> scale(T scalar);

    /**
     * Reshapes this tensor to a new shape.
     * <p>
     * The new shape must have the same total size.
     * </p>
     * 
     * @param newShape the new shape
     * @return reshaped tensor
     */
    Tensor<T> reshape(int... newShape);

    /**
     * Transposes dimensions according to the given permutation.
     * <p>
     * For a matrix (rank 2), transpose() swaps rows and columns.
     * For higher-rank tensors, specify the permutation explicitly.
     * </p>
     * 
     * @param permutation permutation of dimension indices
     * @return transposed tensor
     */
    Tensor<T> transpose(int... permutation);

    /**
     * Transposes a rank-2 tensor (matrix).
     * 
     * @return transposed matrix
     * @throws UnsupportedOperationException if rank != 2
     */
    default Tensor<T> transpose() {
        if (rank() != 2) {
            throw new UnsupportedOperationException("transpose() without arguments requires rank 2");
        }
        return transpose(1, 0);
    }

    /**
     * Sums all elements.
     * 
     * @return Σ elements
     */
    T sum();

    /**
     * Sums along the specified axis.
     * 
     * @param axis the axis to sum over (0 to rank-1)
     * @return tensor with rank reduced by 1
     */
    Tensor<T> sum(int axis);

    /**
     * Returns a copy of this tensor.
     * 
     * @return independent copy
     */
    Tensor<T> copy();

    /**
     * Converts this tensor to a multidimensional array.
     * 
     * @return nested array representation
     */
    Object toArray();
}

