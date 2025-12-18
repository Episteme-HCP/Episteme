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
 * @param <T> the element type
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
                for (Map.Entry<Integer, T> entry : o.data.entrySet()) {
                    resultData.merge(entry.getKey(), entry.getValue(), (v1, v2) -> {
                        @SuppressWarnings("unchecked")
                        Ring<T> r = (Ring<T>) v1;
                        return r.add(v1, v2);
                    });
                }
            }

            // Remove zeros
            resultData.values().removeIf(v -> v.equals(zero));
            return new SparseTensor<>(resultData, this.shape, this.zero);
        } else {
            // Fallback for dense
            throw new UnsupportedOperationException("Sparse + Dense not fully implemented yet");
        }
    }

    @Override
    public Tensor<T> subtract(Tensor<T> other) {
        if (other instanceof SparseTensor) {
            SparseTensor<T> result = new SparseTensor<>(this.shape, this.zero);
            result.data.putAll(this.data);

            SparseTensor<T> o = (SparseTensor<T>) other;
            for (Map.Entry<Integer, T> entry : o.data.entrySet()) {
                int idx = entry.getKey();
                T val = entry.getValue();
                T current = result.data.getOrDefault(idx, zero);
                @SuppressWarnings("unchecked")
                T newVal = ((Ring<T>) current).subtract(current, val);
                if (newVal.equals(zero)) {
                    result.data.remove(idx);
                } else {
                    result.data.put(idx, newVal);
                }
            }
            return result;
        }
        throw new UnsupportedOperationException("Sparse - Dense not fully implemented yet");
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
        // ... similar logic ...
        return null; // Placeholder
    }

    @Override
    public Tensor<T> copy() {
        return new SparseTensor<>(this.data, this.shape, this.zero);
    }

    @Override
    public Object toArray() {
        // Convert to dense array
        return null; // Placeholder
    }

    public T getZero() {
        return zero;
    }
}
