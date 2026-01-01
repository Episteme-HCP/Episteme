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

package org.jscience.mathematics.linearalgebra.tensors;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.jscience.mathematics.structures.rings.Ring;

/**
 * Sparse tensor implementation using a map for non-zero elements.
 * <p>
 * Efficient for tensors with many zero elements.
 * Uses flat index mapping: Map&lt;Integer, T&gt;, where key is the flat index.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SparseTensor<T> implements Tensor<T> {

    private final Map<Integer, T> data;
    private final int[] shape;
    private final int[] strides;
    private final int size;
    private final T zero; // We need to know what "zero" is to return it

    public SparseTensor(int[] shape, T zero) {
        if (shape.length == 0) {
            throw new IllegalArgumentException("Tensor must have at least one dimension");
        }
        this.shape = shape.clone();
        this.size = computeSize(shape);
        this.strides = computeStrides(shape);
        this.data = new HashMap<>();
        this.zero = zero;
    }

    public SparseTensor(Map<Integer, T> data, int[] shape, T zero) {
        this(shape, zero);
        this.data.putAll(data);
    }

    private static int computeSize(int[] shape) {
        int size = 1;
        for (int dim : shape) {
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
            throw new IllegalArgumentException("Indices length must match rank");
        }
        int index = 0;
        for (int i = 0; i < indices.length; i++) {
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
    public T get(int... indices) {
        int idx = flatIndex(indices);
        return data.getOrDefault(idx, zero);
    }

    @Override
    public void set(T value, int... indices) {
        int idx = flatIndex(indices);
        if (value.equals(zero)) {
            data.remove(idx);
        } else {
            data.put(idx, value);
        }
    }

    @Override
    public Tensor<T> add(Tensor<T> other) {
        if (other instanceof SparseTensor) {
            SparseTensor<T> o = (SparseTensor<T>) other;
            // Thread-safe map for parallel execution
            final ConcurrentHashMap<Integer, T> resultData = new ConcurrentHashMap<>(this.data);

            if (o.data.size() >= 1000) {
                o.data.entrySet().stream().parallel().forEach(entry -> {
                    resultData.merge(entry.getKey(), entry.getValue(), (v1, v2) -> {
                        @SuppressWarnings("unchecked")
                        Ring<T> r = (Ring<T>) v1;
                        return r.add(v1, v2);
                    });
                });
            } else {
                o.data.forEach((k, v) -> resultData.merge(k, v, (v1, v2) -> {
                    @SuppressWarnings("unchecked")
                    Ring<T> r = (Ring<T>) v1;
                    return r.add(v1, v2);
                }));
            }

            // Remove zeros
            resultData.values().removeIf(v -> v.equals(zero));
            return new SparseTensor<>(resultData, this.shape, this.zero);
        } else

        {
            // Fallback for dense (Sparse + Dense = Dense)
            Tensor<T> result = other.copy();
            for (Map.Entry<Integer, T> entry : this.data.entrySet()) {
                int[] indices = indicesFromFlat(entry.getKey());
                T current = result.get(indices);
                @SuppressWarnings("unchecked")
                T sum = ((Ring<T>) current).add(current, entry.getValue());
                result.set(sum, indices);
            }
            return result;
        }
    }

    @Override

    public Tensor<T> subtract(Tensor<T> other) {
        if (other instanceof SparseTensor) {
            SparseTensor<T> o = (SparseTensor<T>) other;
            ConcurrentHashMap<Integer, T> resultData = new ConcurrentHashMap<>(this.data);

            o.data.forEach((k, v) -> resultData.merge(k, v, (v1, v2) -> {
                @SuppressWarnings("unchecked")
                Ring<T> r = (Ring<T>) v1; // Assuming T implements Ring logic or similar structure
                return r.subtract(v1, v2);
            }));

            return new SparseTensor<>(resultData, this.shape, this.zero);
        }

        @SuppressWarnings("unchecked")
        Ring<T> ringStructure = (Ring<T>) this.zero;
        T minusOne = ringStructure.negate(ringStructure.one());
        Tensor<T> negOther = other.scale(minusOne);
        return this.add(negOther);
    }

    @Override
    public Tensor<T> multiply(Tensor<T> other) {
        ConcurrentHashMap<Integer, T> resultData = new ConcurrentHashMap<>();

        Stream<Map.Entry<Integer, T>> stream = this.data.entrySet().stream();
        if (this.data.size() >= 1000) {
            stream = stream.parallel();
        }

        stream.forEach(entry -> {
            int idx = entry.getKey();
            // Reconstruct indices logic ...
            // Ideally we shouldn't replicate code but for lambda usage we copy logic
            int[] indices = new int[rank()];
            int temp = idx;
            for (int j = rank() - 1; j >= 0; j--) {
                indices[j] = temp % shape[j];
                temp /= shape[j];
            }

            T val = entry.getValue();
            T otherVal = other.get(indices);
            @SuppressWarnings("unchecked")
            T prod = ((Ring<T>) val).multiply(val, otherVal);
            if (!prod.equals(zero)) {
                resultData.put(idx, prod);
            }
        });

        return new SparseTensor<>(resultData, this.shape, this.zero);
    }

    @Override
    public Tensor<T> scale(T scalar) {
        if (scalar.equals(zero)) {
            return new SparseTensor<>(this.shape, this.zero);
        }

        ConcurrentHashMap<Integer, T> resultData = new ConcurrentHashMap<>();
        Stream<Map.Entry<Integer, T>> stream = this.data.entrySet().stream();
        if (this.data.size() >= 1000)
            stream = stream.parallel();

        stream.forEach(entry -> {
            @SuppressWarnings("unchecked")
            T newVal = ((Ring<T>) entry.getValue()).multiply(entry.getValue(), scalar);
            if (!newVal.equals(zero)) {
                resultData.put(entry.getKey(), newVal);
            }
        });
        return new SparseTensor<>(resultData, this.shape, this.zero);
    }

    @Override
    public Tensor<T> reshape(int... newShape) {
        int newSize = computeSize(newShape);
        if (newSize != size) {
            throw new IllegalArgumentException("New shape must have same total size");
        }
        // Reshape preserves flat indices! So data map is same.
        return new SparseTensor<>(this.data, newShape, this.zero);
    }

    @Override
    public Tensor<T> broadcast(int... newShape) {
        if (newShape.length < rank()) {
            throw new IllegalArgumentException("Target shape must have at least equal rank");
        }

        // This is a naive copy-based implementation for SparseTensor.
        // It physically replicates non-zero elements.

        // Check standard broadcast compatibility
        int dimOffset = newShape.length - rank();
        for (int i = newShape.length - 1; i >= 0; i--) {
            int oldIdx = i - dimOffset;
            if (oldIdx >= 0) {
                int oldDim = shape[oldIdx];
                int newDim = newShape[i];
                if (oldDim != newDim && oldDim != 1) {
                    throw new IllegalArgumentException("Incompatible shapes for broadcast");
                }
            }
        }

        // New SparseTensor
        SparseTensor<T> result = new SparseTensor<>(newShape, zero);

        // Iterate current non-zeros
        for (Map.Entry<Integer, T> entry : data.entrySet()) {
            int oldFlat = entry.getKey();
            int[] oldIndices = indicesFromFlat(oldFlat);

            // This element might map to multiple new locations if we broadcast (dim 1 -> N)
            // or if we add new dimensions (broadcast from left)

            recurseBroadcast(result, entry.getValue(), oldIndices, newShape, 0, new int[newShape.length]);
        }

        return result;
    }

    private void recurseBroadcast(SparseTensor<T> result, T value, int[] oldIndices, int[] newShape, int dim,
            int[] currentIndices) {
        if (dim == newShape.length) {
            result.set(value, currentIndices);
            return;
        }

        int dimOffset = newShape.length - rank();
        int oldDimIdx = dim - dimOffset;

        if (oldDimIdx < 0) {
            // New dimension added on left (broadcast implicitly). Iterate all.
            for (int i = 0; i < newShape[dim]; i++) {
                currentIndices[dim] = i;
                recurseBroadcast(result, value, oldIndices, newShape, dim + 1, currentIndices);
            }
        } else {
            // Existing dimension
            if (shape[oldDimIdx] == 1 && newShape[dim] > 1) {
                // Broadcast 1 -> N. Iterate all.
                for (int i = 0; i < newShape[dim]; i++) {
                    currentIndices[dim] = i;
                    recurseBroadcast(result, value, oldIndices, newShape, dim + 1, currentIndices);
                }
            } else {
                // Matched dimension. Use old index.
                currentIndices[dim] = oldIndices[oldDimIdx];
                recurseBroadcast(result, value, oldIndices, newShape, dim + 1, currentIndices);
            }
        }
    }

    private int[] indicesFromFlat(int flatIndex) {
        int[] indices = new int[rank()];
        int temp = flatIndex;
        for (int j = rank() - 1; j >= 0; j--) {
            indices[j] = temp % shape[j];
            temp /= shape[j];
        }
        return indices;
    }

    @Override
    public Tensor<T> slice(int[] starts, int[] sizes) {
        if (starts.length != rank() || sizes.length != rank()) {
            throw new IllegalArgumentException("Slice args must match rank");
        }

        int[] resultShape = sizes.clone();
        SparseTensor<T> result = new SparseTensor<>(resultShape, zero);

        // Iterate original non-zeros and check if they fall in slice
        for (Map.Entry<Integer, T> entry : data.entrySet()) {
            int oldFlat = entry.getKey();
            int[] oldIndices = indicesFromFlat(oldFlat);

            boolean inBounds = true;
            int[] newIndices = new int[rank()];

            for (int i = 0; i < rank(); i++) {
                if (oldIndices[i] >= starts[i] && oldIndices[i] < starts[i] + sizes[i]) {
                    newIndices[i] = oldIndices[i] - starts[i];
                } else {
                    inBounds = false;
                    break;
                }
            }

            if (inBounds) {
                result.set(entry.getValue(), newIndices);
            }
        }

        return result;
    }

    @Override
    public Tensor<T> transpose(int... permutation) {
        // ... implementation needed ...
        // Recompute indices for every keys.
        int[] newShape = new int[rank()];
        for (int i = 0; i < rank(); i++)
            newShape[i] = shape[permutation[i]];

        SparseTensor<T> result = new SparseTensor<>(newShape, zero);

        for (Map.Entry<Integer, T> entry : data.entrySet()) {
            int idx = entry.getKey();
            int[] indices = new int[rank()];
            int temp = idx;
            for (int j = rank() - 1; j >= 0; j--) {
                indices[j] = temp % shape[j];
                temp /= shape[j];
            }

            int[] newIndices = new int[rank()];
            for (int j = 0; j < rank(); j++)
                newIndices[j] = indices[permutation[j]];

            // New flat index
            // Need new strides? No, strides computed in constructor.
            // But we can't use helper flatIndex here easily without instance.
            // Wait, result is instance.
            result.set(entry.getValue(), newIndices);
        }

        return result;
    }

    @Override
    public T sum() {
        T s = zero; // Start with zero? Or null?
        // sum of non-zeros.
        // Zeros don't contribute to sum.
        for (T val : data.values()) {
            @SuppressWarnings("unchecked")
            T nextS = ((Ring<T>) s).add(s, val);
            s = nextS;
        }
        return s;
    }

    @Override
    public Tensor<T> sum(int axis) {
        if (axis < 0 || axis >= rank()) {
            throw new IllegalArgumentException("Invalid axis: " + axis);
        }

        // Result shape: remove the summed axis
        int[] newShape = new int[rank() - 1];
        int idx = 0;
        for (int i = 0; i < rank(); i++) {
            if (i != axis) {
                newShape[idx++] = shape[i];
            }
        }

        SparseTensor<T> result = new SparseTensor<>(newShape.length > 0 ? newShape : new int[] { 1 }, zero);

        for (Map.Entry<Integer, T> entry : data.entrySet()) {
            int[] oldIndices = indicesFromFlat(entry.getKey());

            // Build new indices by removing the axis
            int[] newIndices = new int[newShape.length > 0 ? newShape.length : 1];
            idx = 0;
            for (int i = 0; i < rank(); i++) {
                if (i != axis) {
                    newIndices[idx++] = oldIndices[i];
                }
            }

            T current = result.get(newIndices);
            @SuppressWarnings("unchecked")
            T sum = ((Ring<T>) current).add(current, entry.getValue());
            result.set(sum, newIndices);
        }

        return result;
    }

    @Override
    public Tensor<T> copy() {
        return new SparseTensor<>(this.data, this.shape, this.zero);
    }

    @Override
    public Object toArray() {
        // Convert to dense multi-dimensional array
        // For simplicity, return a flat array for rank > 2
        if (rank() == 1) {
            @SuppressWarnings("unchecked")
            T[] arr = (T[]) java.lang.reflect.Array.newInstance(zero.getClass(), shape[0]);
            java.util.Arrays.fill(arr, zero);
            for (Map.Entry<Integer, T> e : data.entrySet()) {
                arr[e.getKey()] = e.getValue();
            }
            return arr;
        } else if (rank() == 2) {
            @SuppressWarnings("unchecked")
            T[][] arr = (T[][]) java.lang.reflect.Array.newInstance(zero.getClass(), shape[0], shape[1]);
            for (int i = 0; i < shape[0]; i++) {
                java.util.Arrays.fill(arr[i], zero);
            }
            for (Map.Entry<Integer, T> e : data.entrySet()) {
                int[] idx = indicesFromFlat(e.getKey());
                arr[idx[0]][idx[1]] = e.getValue();
            }
            return arr;
        }
        // For higher ranks, return flat array with elements
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) java.lang.reflect.Array.newInstance(zero.getClass(), size);
        java.util.Arrays.fill(arr, zero);
        for (Map.Entry<Integer, T> e : data.entrySet()) {
            arr[e.getKey()] = e.getValue();
        }
        return arr;
    }

    public T getZero() {
        return zero;
    }
}
