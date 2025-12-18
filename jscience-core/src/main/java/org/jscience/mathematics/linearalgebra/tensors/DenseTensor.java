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
package org.jscience.mathematics.linearalgebra.tensors;

import org.jscience.mathematics.structures.rings.Ring;
import java.util.Arrays;

/**
 * Dense tensor implementation backed by a flat array.
 * <p>
 * Uses row-major (C-style) ordering for multi-dimensional indexing.
 * Memory layout is similar to NumPy's ndarray.
 * </p>
 * 
 * 
 * 
 * @param <T> the element type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class DenseTensor<T> implements Tensor<T> {

    protected final Object data; // Flat array
    protected final int[] shape;
    protected final int[] strides; // For efficient indexing
    protected final int size;
    protected final int offset; // Offset into the data array
    private static final int PARALLEL_THRESHOLD = 1000;

    /**
     * Creates a tensor with given shape and data.
     * 
     * @param data  flat array of elements
     * @param shape dimension sizes
     */
    public DenseTensor(T[] data, int... shape) {

        this.shape = shape.clone();
        this.size = computeSize(shape);

        if (data.length != size) {
            throw new IllegalArgumentException("Data length must match shape: " + size);
        }

        this.data = data;
        this.strides = computeStrides(shape);
        this.offset = 0;
    }

    /**
     * Protected constructor for subclasses managing their own data.
     */
    protected DenseTensor(int[] shape) {
        this.shape = shape.clone();
        this.size = computeSize(shape);
        this.strides = computeStrides(shape);
        this.offset = 0;
        this.data = null; // Subclass responsibility
    }

    /**
     * Internal constructor for views.
     */
    private DenseTensor(Object data, int[] shape, int[] strides, int offset) {
        this.data = data;
        this.shape = shape;
        this.strides = strides;
        this.offset = offset;
        this.size = computeSize(shape);
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

        int index = offset;
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
        T val = ((T[]) data)[idx];
        return val;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(T value, int... indices) {
        int idx = flatIndex(indices);
        T[] arr = ((T[]) data);
        arr[idx] = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> add(Tensor<T> other) {
        if (!Arrays.equals(shape, other.shape())) {
            throw new IllegalArgumentException("Shapes must match for addition");
        }

        T[] result = (T[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), size);
        T[] otherData = ((DenseTensor<T>) other).getDataArray();
        T[] thisData = (T[]) data;

        if (size >= PARALLEL_THRESHOLD) {
            java.util.stream.IntStream.range(0, size).parallel().forEach(i -> {
                result[i] = ((Ring<T>) thisData[i]).add(thisData[i], otherData[i]);
            });
        } else {
            for (int i = 0; i < size; i++) {
                result[i] = ((Ring<T>) thisData[i]).add(thisData[i], otherData[i]);
            }
        }
        return new DenseTensor<>(result, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> subtract(Tensor<T> other) {
        if (!Arrays.equals(shape, other.shape())) {
            throw new IllegalArgumentException("Shapes must match for subtraction");
        }

        T[] result = (T[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), size);
        T[] otherData = ((DenseTensor<T>) other).getDataArray();
        T[] thisData = (T[]) data;

        // Subtraction might require GroupAdditive or Ring
        if (size >= PARALLEL_THRESHOLD) {
            java.util.stream.IntStream.range(0, size).parallel().forEach(i -> {
                result[i] = ((Ring<T>) thisData[i]).subtract(thisData[i], otherData[i]);
            });
        } else {
            for (int i = 0; i < size; i++) {
                result[i] = ((Ring<T>) thisData[i]).subtract(thisData[i], otherData[i]);
            }
        }
        return new DenseTensor<>(result, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> multiply(Tensor<T> other) {
        if (!Arrays.equals(shape, other.shape())) {
            throw new IllegalArgumentException("Shapes must match for element-wise multiplication");
        }

        T[] result = (T[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), size);
        T[] otherData = ((DenseTensor<T>) other).getDataArray();
        T[] thisData = (T[]) data;

        if (size >= PARALLEL_THRESHOLD) {
            java.util.stream.IntStream.range(0, size).parallel().forEach(i -> {
                result[i] = ((Ring<T>) thisData[i]).multiply(thisData[i], otherData[i]);
            });
        } else {
            for (int i = 0; i < size; i++) {
                result[i] = ((Ring<T>) thisData[i]).multiply(thisData[i], otherData[i]);
            }
        }
        return new DenseTensor<>(result, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tensor<T> scale(T scalar) {
        T[] result = (T[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), size);
        T[] thisData = (T[]) data;

        if (size >= PARALLEL_THRESHOLD) {
            java.util.stream.IntStream.range(0, size).parallel().forEach(i -> {
                result[i] = ((Ring<T>) thisData[i]).multiply(thisData[i], scalar);
            });
        } else {
            for (int i = 0; i < size; i++) {
                result[i] = ((Ring<T>) thisData[i]).multiply(thisData[i], scalar);
            }
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
        // Reshape is only valid for views if the data is contiguous in memory for the
        // new shape.
        // For simplicity, we only strictly support reshape on non-strided (contiguous)
        // tensors or force copy.
        // Determine if contiguous:
        // Proper check is complex. For prototype, we'll try to reuse data if strides
        // are canonical.

        // MVP: If strides are canonical, reuse data with offset=0 (assuming contiguous
        // from 0).
        // If not, copy.

        // Check if contiguous
        if (isContiguous()) {
            Tensor<T> res = new DenseTensor<>((T[]) data, newShape); // Creates new canonical strides
            return res;
        } else {
            return copy().reshape(newShape);
        }
    }

    private boolean isContiguous() {
        int expectedStride = 1;
        for (int i = rank() - 1; i >= 0; i--) {
            if (strides[i] != expectedStride && shape[i] > 1) {
                // broadcasting (stride 0) or slicing can break contiguity
                return false;
            }
            expectedStride *= shape[i];
        }
        return true;
    }

    @Override
    public Tensor<T> broadcast(int... newShape) {
        if (newShape.length < rank()) {
            throw new IllegalArgumentException("Target shape must have at least equal rank");
        }

        int[] newStrides = new int[newShape.length];
        int dimOffset = newShape.length - rank();

        // Match dimensions from the right
        for (int i = newShape.length - 1; i >= 0; i--) {
            int oldIdx = i - dimOffset;
            if (oldIdx < 0) {
                // New dimension on the left (broadcast)
                newStrides[i] = 0; // Stride 0 repeats the data
            } else {
                int oldDim = shape[oldIdx];
                int newDim = newShape[i];

                if (oldDim == newDim) {
                    newStrides[i] = strides[oldIdx];
                } else if (oldDim == 1) {
                    newStrides[i] = 0; // Broadcast existing dim 1 -> N
                } else {
                    throw new IllegalArgumentException("Incompatible shapes for broadcast: " +
                            Arrays.toString(shape) + " -> " + Arrays.toString(newShape));
                }
            }
        }

        return new DenseTensor<>(this.data, newShape, newStrides, this.offset);
    }

    @Override
    public Tensor<T> slice(int[] starts, int[] sizes) {
        if (starts.length != rank() || sizes.length != rank()) {
            throw new IllegalArgumentException("Slice args must match rank");
        }

        int newOffset = this.offset;
        int[] newShape = new int[rank()];
        int[] newStrides = new int[rank()];

        for (int i = 0; i < rank(); i++) {
            if (starts[i] < 0 || starts[i] + sizes[i] > shape[i]) {
                throw new IndexOutOfBoundsException("Slice out of bounds");
            }
            newOffset += starts[i] * strides[i];
            newShape[i] = sizes[i];
            newStrides[i] = strides[i];
        }

        return new DenseTensor<>(this.data, newShape, newStrides, newOffset);
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
        T[] result = (T[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), size);

        if (size >= PARALLEL_THRESHOLD) {
            java.util.stream.IntStream.range(0, size).parallel().forEach(i -> {
                int[] indices = new int[rank()];
                int temp = i;
                for (int j = rank() - 1; j >= 0; j--) {
                    indices[j] = temp % shape[j];
                    temp /= shape[j];
                }

                int[] newIndices = new int[rank()];
                for (int j = 0; j < rank(); j++) {
                    newIndices[j] = indices[permutation[j]];
                }

                int newIdx = 0;
                int stride = 1;
                for (int j = rank() - 1; j >= 0; j--) {
                    newIdx += newIndices[j] * stride;
                    stride *= newShape[j];
                }
                result[newIdx] = get(indices);
            });
        } else {
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
        }

        return new DenseTensor<>(result, newShape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T sum() {
        if (size == 0)
            return null; // Or throw?

        T[] thisData = (T[]) data;

        if (size >= PARALLEL_THRESHOLD) {
            // Assuming T implements Ring<T> with add(T, T)
            return java.util.Arrays.stream(thisData).parallel().reduce((a, b) -> ((Ring<T>) a).add(a, b)).orElse(null);
        } else {
            T result = ((T[]) data)[0];
            for (int i = 1; i < size; i++) {
                T val = ((T[]) data)[i];
                result = ((Ring<T>) result).add(result, val);
            }
            return result;
        }
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
            T[] resultData = (T[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), 1);
            resultData[0] = scalar;
            return new DenseTensor<>(resultData, 1);
        }

        int newSize = computeSize(newShape);
        @SuppressWarnings("unchecked")
        T[] result = (T[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), newSize);

        // Sum along specified axis
        // Note: axisSize and axisStride are available if needed for optimization

        // Initialize result with zeros
        @SuppressWarnings("unchecked")
        T zero = (size > 0) ? ((Ring<T>) ((Object[]) data)[0]).zero() : null;
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
                    @SuppressWarnings("unchecked")
                    T value = (T) dataArray[i];
                    result[resultIdx] = value;
                } else {
                    // Use functional style: a.add(a, b) because T extends Field<T>
                    @SuppressWarnings("unchecked")
                    T addend = (T) dataArray[i];
                    @SuppressWarnings("unchecked")
                    Ring<T> accum = (Ring<T>) result[resultIdx];
                    result[resultIdx] = accum.add(result[resultIdx], addend);
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
