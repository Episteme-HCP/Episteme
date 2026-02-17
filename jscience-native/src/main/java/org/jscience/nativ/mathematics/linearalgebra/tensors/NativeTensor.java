/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.tensors;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import java.lang.foreign.*;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Tensor implementation backed by native memory (off-heap).
 * <p>
 * Supports hardware acceleration via FFM (Project Panama).
 * Currently optimized for Float and Double types.
 * </p>
 *
 * @param <T> the element type (must be Float or Double for native storage)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeTensor<T> implements Tensor<T> {

    private static final long serialVersionUID = 1L;

    private final MemorySegment segment;
    private final int[] shape;
    private final int[] strides;
    private final long size; // Element count
    private final Class<T> type;
    private final Arena arena; // Keep reference to prevent GC if using auto

    // Helper to calculate size
    private static long computeSize(int[] shape) {
        long size = 1;
        for (int s : shape) size *= s;
        return size;
    }

    private static int[] computeStrides(int[] shape) {
        int[] strides = new int[shape.length];
        int s = 1;
        for (int i = shape.length - 1; i >= 0; i--) {
            strides[i] = s;
            s *= shape[i];
        }
        return strides;
    }

    /**
     * Creates a new NativeTensor.
     *
     * @param shape the tensor shape
     * @param type  the element class (Float.class or Double.class)
     */
    public NativeTensor(Class<T> type, int... shape) {
        this.shape = shape.clone();
        this.strides = computeStrides(shape);
        this.size = computeSize(shape);
        this.type = type;
        
        // Use automatic arena for GC-based deallocation
        this.arena = Arena.ofAuto();
        
        long byteSize;
        if (type == Float.class) {
            byteSize = size * Float.BYTES;
            this.segment = arena.allocate(byteSize, Float.BYTES);
        } else if (type == Double.class) {
            byteSize = size * Double.BYTES;
            this.segment = arena.allocate(byteSize, Double.BYTES);
        } else {
            throw new IllegalArgumentException("NativeTensor only supports Float and Double, got: " + type.getSimpleName());
        }
    }

    /**
     * Internal constructor for sharing segment (views).
     */
    private NativeTensor(MemorySegment segment, int[] shape, int[] strides, Class<T> type, Arena arena) {
        this.segment = segment;
        this.shape = shape;
        this.strides = strides;
        this.size = computeSize(shape);
        this.type = type;
        this.arena = arena;
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
        return (int) size;
    }

    private long computeOffset(int... indices) {
        long offset = 0;
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] < 0 || indices[i] >= shape[i]) {
                throw new IndexOutOfBoundsException("Index " + indices[i] + " out of bounds for dim " + i);
            }
            offset += (long) indices[i] * strides[i];
        }
        return offset;
    }

    @Override
    public T get(int... indices) {
        long offset = computeOffset(indices);
        if (type == Float.class) {
            return type.cast(segment.getAtIndex(ValueLayout.JAVA_FLOAT, offset));
        } else {
            return type.cast(segment.getAtIndex(ValueLayout.JAVA_DOUBLE, offset));
        }
    }

    @Override
    public void set(T value, int... indices) {
        long offset = computeOffset(indices);
        if (type == Float.class) {
            segment.setAtIndex(ValueLayout.JAVA_FLOAT, offset, (Float) value);
        } else {
            segment.setAtIndex(ValueLayout.JAVA_DOUBLE, offset, (Double) value);
        }
    }

    // --- Bulk Operations (Hook for Native Provider) ---

    // For now, implement generic Java-loop fallback.
    // The Provider will intercept these or we can inject native ops later.
    // To keep this class self-contained but acceleratable:
    
    @Override
    public Tensor<T> add(Tensor<T> other) {
        if (!Arrays.equals(shape, other.shape())) throw new IllegalArgumentException("Shape mismatch");
        NativeTensor<T> result = new NativeTensor<>(type, shape);
        
        // TODO: Call BLAS SAXPY/DAXPY here if available
        // Fallback:
        for (int i = 0; i < size; i++) {
             // Basic implementation for verification
             if (type == Float.class) {
                 float a = segment.getAtIndex(ValueLayout.JAVA_FLOAT, i);
                 float b = (Float) other.get(unflatten(i)); // optimizing this requires direct access to other implementation
                 result.segment.setAtIndex(ValueLayout.JAVA_FLOAT, i, a + b);
             } else {
                 double a = segment.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                 double b = (Double) other.get(unflatten(i));
                 result.segment.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a + b);
             }
        }
        return result;
    }

    private int[] unflatten(int index) {
        int[] indices = new int[shape.length];
        for (int i = shape.length - 1; i >= 0; i--) {
            indices[i] = index % shape[i];
            index /= shape[i];
        }
        return indices;
    }

    @Override
    public Tensor<T> subtract(Tensor<T> other) {
        return add(other.scale(type == Float.class ? type.cast(-1.0f) : type.cast(-1.0)));
    }

    @Override
    public Tensor<T> multiply(Tensor<T> other) {
        // Hadamard product
        if (!Arrays.equals(shape, other.shape())) throw new IllegalArgumentException("Shape mismatch");
        NativeTensor<T> result = new NativeTensor<>(type, shape);
        
        for (int i = 0; i < size; i++) {
             if (type == Float.class) {
                 float a = segment.getAtIndex(ValueLayout.JAVA_FLOAT, i);
                 float b = (Float) other.get(unflatten(i)); 
                 result.segment.setAtIndex(ValueLayout.JAVA_FLOAT, i, a * b);
             } else {
                 double a = segment.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                 double b = (Double) other.get(unflatten(i));
                 result.segment.setAtIndex(ValueLayout.JAVA_DOUBLE, i, a * b);
             }
        }
        return result;
    }

    @Override
    public Tensor<T> scale(T scalar) {
        NativeTensor<T> result = new NativeTensor<>(type, shape);
         if (type == Float.class) {
             float s = (Float) scalar;
             for (long i = 0; i < size; i++) {
                 float val = segment.getAtIndex(ValueLayout.JAVA_FLOAT, i);
                 result.segment.setAtIndex(ValueLayout.JAVA_FLOAT, i, val * s);
             }
         } else {
             double s = (Double) scalar;
             for (long i = 0; i < size; i++) {
                 double val = segment.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                 result.segment.setAtIndex(ValueLayout.JAVA_DOUBLE, i, val * s);
             }
         }
         return result;
    }

    @Override
    public Tensor<T> reshape(int... newShape) {
        if (computeSize(newShape) != size) throw new IllegalArgumentException("Size mismatch");
        // View if contiguous, else copy. Assuming contiguous for simple constructor logic.
        return new NativeTensor<>(segment, newShape, computeStrides(newShape), type, arena);
    }

    @Override
    public Tensor<T> broadcast(int... newShape) {
        throw new UnsupportedOperationException("Broadcast not implemented for NativeTensor yet");
    }

    @Override
    public Tensor<T> transpose(int... permutation) {
         throw new UnsupportedOperationException("Transpose not implemented for NativeTensor yet");
    }
    
    @Override
    public Tensor<T> slice(int[] starts, int[] sizes) {
        throw new UnsupportedOperationException("Slice not implemented for NativeTensor yet");
    }

    @Override
    public Tensor<T> map(Function<T, T> function) {
        NativeTensor<T> result = new NativeTensor<>(type, shape);
        for (int i = 0; i < size; i++) {
            int[] idx = unflatten(i);
            T val = get(idx);
            result.set(function.apply(val), idx);
        }
        return result;
    }

    @Override
    public T sum() {
        if (type == Float.class) {
            float sum = 0;
            for (long i = 0; i < size; i++) sum += segment.getAtIndex(ValueLayout.JAVA_FLOAT, i);
            return type.cast(sum);
        } else {
            double sum = 0;
            for (long i = 0; i < size; i++) sum += segment.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
            return type.cast(sum);
        }
    }

    @Override
    public Tensor<T> sum(int axis) {
        throw new UnsupportedOperationException("Axis sum not implemented");
    }

    @Override
    public Tensor<T> copy() {
        NativeTensor<T> copy = new NativeTensor<>(type, shape);
        MemorySegment.copy(segment, 0, copy.segment, 0, segment.byteSize());
        return copy;
    }

    @Override
    public Object toArray() {
        if (type == Float.class) {
            return segment.toArray(ValueLayout.JAVA_FLOAT);
        } else {
            return segment.toArray(ValueLayout.JAVA_DOUBLE);
        }
    }
    
    // accessors for Provider
    public MemorySegment getSegment() { return segment; }
}
