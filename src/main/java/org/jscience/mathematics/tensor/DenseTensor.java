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
import java.util.Arrays;

/**
 * Dense tensor implementation backed by a flat array.
 * <p>
 * Uses row-major (C-style) ordering for multi-dimensional indexing.
 * Memory layout is similar to NumPy's ndarray.
 * </p>
 * 
 * @param <T> the element type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class DenseTensor<T extends Field<T>> implements Tensor<T> {

    private final Object data; // Flat array
    private final int[] shape;
    private final int[] strides; // For efficient indexing
    private final int size;

    /**
     * Creates a tensor with given shape and data.
     * 
     * @param data  flat array of elements
     * @param shape dimension sizes
     */
    @SuppressWarnings("unchecked")
    public DenseTensor(T[] data, int... shape) {
        if (shape.length == 0) {
            throw new IllegalArgumentException("Tensor must have at least one dimension");
        }

        this.shape = shape.clone();
        this.size = computeSize(shape);

        if (data.length != size) {
            throw new IllegalArgumentException("Data length must match shape: " + size);
        }

        this.data = data;
        this.strides = computeStrides(shape);
    }

    private static int computeSize(int[] shape) {
        int size = 1;
        for (int dim : shape) {
            if (dim <= 0) {
                throw new IllegalArgumentException("Dimensions must be positive");
            }
            size *= dim;
        }
        return size;
    }

    private static int[] computeStrides(int[] shape) {
        int[] strides = new int[shape.length];
        int stride = 1;
        for (int i = shape.length - 1; i >= 0; i--) {
            strides[i] = stride;
            stride *= shape[i];
        }
        return strides;
    }

    private int flatIndex(int... indices) {
        if (indices.length != shape.length) {
            throw new IllegalArgumentException("Indices length must match rank: " + shape.length);
        }

        int index = 0;
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] < 0 || indices[i] >= shape[i]) {
                throw new IndexOutOfBoundsException("Index " + indices[i] + " out of bounds for dimension " + i);
            }
            index += indices[i] * strides[i];
        }
        return index;
    }

    @Override
    public int[] shape() {
        return shape.clone();
    }

    @Override
    public int rank() {
        return shape.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int... indices) {
        int idx = flatIndex(indices);
        return ((T[]) data)[idx];
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(T value, int... indices) {
        int idx = flatIndex(indices);
        ((T[]) data)[idx] = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> add(Tensor<T> other) {
        if (!Arrays.equals(shape, other.shape())) {
            throw new IllegalArgumentException("Shapes must match for addition");
        }

        T[] result = (T[]) new Field[size];
        for (int i = 0; i < size; i++) {
            result[i] = ((T[]) data)[i].add(((T[]) data)[i], ((DenseTensor<T>) other).getDataArray()[i]);
        }
        return new DenseTensor<>(result, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> subtract(Tensor<T> other) {
        if (!Arrays.equals(shape, other.shape())) {
            throw new IllegalArgumentException("Shapes must match for subtraction");
        }

        T[] result = (T[]) new Field[size];
        for (int i = 0; i < size; i++) {
            result[i] = ((T[]) data)[i].subtract(((T[]) data)[i], ((DenseTensor<T>) other).getDataArray()[i]);
        }
        return new DenseTensor<>(result, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> multiply(Tensor<T> other) {
        if (!Arrays.equals(shape, other.shape())) {
            throw new IllegalArgumentException("Shapes must match for element-wise multiplication");
        }

        T[] result = (T[]) new Field[size];
        for (int i = 0; i < size; i++) {
            result[i] = ((T[]) data)[i].multiply(((T[]) data)[i], ((DenseTensor<T>) other).getDataArray()[i]);
        }
        return new DenseTensor<>(result, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> scale(T scalar) {
        T[] result = (T[]) new Field[size];
        for (int i = 0; i < size; i++) {
            result[i] = ((T[]) data)[i].multiply(((T[]) data)[i], scalar);
        }
        return new DenseTensor<>(result, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> reshape(int... newShape) {
        int newSize = computeSize(newShape);
        if (newSize != size) {
            throw new IllegalArgumentException("New shape must have same total size");
        }
        return new DenseTensor<>((T[]) data, newShape);
    }

    @Override
    public Tensor<T> transpose(int... permutation) {
        if (permutation.length != rank()) {
            throw new IllegalArgumentException("Permutation length must match rank");
        }

        // Create new shape
        int[] newShape = new int[rank()];
        for (int i = 0; i < rank(); i++) {
            newShape[i] = shape[permutation[i]];
        }

        // Copy data with transposed indices
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Field[size];

        int[] indices = new int[rank()];
        for (int i = 0; i < size; i++) {
            // Compute multi-dimensional indices from flat index
            int temp = i;
            for (int j = rank() - 1; j >= 0; j--) {
                indices[j] = temp % shape[j];
                temp /= shape[j];
            }

            // Permute indices
            int[] newIndices = new int[rank()];
            for (int j = 0; j < rank(); j++) {
                newIndices[j] = indices[permutation[j]];
            }

            // Compute new flat index
            int newIdx = 0;
            int stride = 1;
            for (int j = rank() - 1; j >= 0; j--) {
                newIdx += newIndices[j] * stride;
                stride *= newShape[j];
            }

            result[newIdx] = get(indices);
        }

        return new DenseTensor<>(result, newShape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T sum() {
        T result = ((T[]) data)[0];
        for (int i = 1; i < size; i++) {
            result = result.add(result, ((T[]) data)[i]);
        }
        return result;
    }

    @Override
    public Tensor<T> sum(int axis) {
        if (axis < 0 || axis >= rank()) {
            throw new IllegalArgumentException("Invalid axis: " + axis);
        }

        // Compute new shape (remove axis)
        int[] newShape = new int[rank() - 1];
        int idx = 0;
        for (int i = 0; i < rank(); i++) {
            if (i != axis) {
                newShape[idx++] = shape[i];
            }
        }

        if (newShape.length == 0) {
            // Result is scalar, wrap in rank-1 tensor
            T scalar = sum();
            @SuppressWarnings("unchecked")
            T[] resultData = (T[]) new Field[] { scalar };
            return new DenseTensor<>(resultData, 1);
        }

        int newSize = computeSize(newShape);
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Field[newSize];

        // Sum along specified axis
        int[] strides = computeStrides(shape);
        int axisStride = strides[axis];
        int axisSize = shape[axis];

        // Initialize result with zeros
        T zero = (size > 0) ? ((T) ((Object[]) data)[0]).zero() : null; // Should handle empty case better if needed
        for (int i = 0; i < newSize; i++) {
            result[i] = zero;
        }

        // Sum elements along axis
        Object[] dataArray = (Object[]) data;
        for (int i = 0; i < dataArray.length; i++) {
            // Convert flat index to result index (skip the summed axis)
            int resultIdx = 0;
            int temp = i;
            int newAxisIdx = 0;

            for (int d = shape.length - 1; d >= 0; d--) {
                int coord = temp % shape[d];
                temp /= shape[d];

                if (d != axis) {
                    int newStride = (d > axis) ? newShape[d - 1] : newShape[d];
                    if (newAxisIdx < newShape.length) {
                        resultIdx += coord * newStride;
                        newAxisIdx++;
                    }
                }
            }

            if (resultIdx < result.length) {
                if (result[resultIdx] == null) {
                    result[resultIdx] = (T) dataArray[i];
                } else {
                    // Use functional style: a.add(a, b) because T extends Field<T>
                    result[resultIdx] = result[resultIdx].add(result[resultIdx], (T) dataArray[i]);
                }
            }
        }

        return new DenseTensor<>(result, newShape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> copy() {
        T[] dataCopy = Arrays.copyOf((T[]) data, size);
        return new DenseTensor<>(dataCopy, shape);
    }

    @Override
    public Object toArray() {
        return buildNestedArray(0, 0);
    }

    @SuppressWarnings("unchecked")
    private Object buildNestedArray(int dimIndex, int offset) {
        int length = shape[dimIndex];
        if (dimIndex == shape.length - 1) {
            // Last dimension: return T[]
            return Arrays.copyOfRange((T[]) data, offset, offset + length);
        } else {
            // Intermediate dimension: return Object[] (which will hold sub-arrays)
            Object[] array = new Object[length];
            int stride = strides[dimIndex];
            for (int i = 0; i < length; i++) {
                array[i] = buildNestedArray(dimIndex + 1, offset + i * stride);
            }
            return array;
        }
    }

    @SuppressWarnings("unchecked")
    private T[] getDataArray() {
        return (T[]) data;
    }

    @Override
    public String toString() {
        return "DenseTensor(shape=" + Arrays.toString(shape) + ", size=" + size + ")";
    }
}

