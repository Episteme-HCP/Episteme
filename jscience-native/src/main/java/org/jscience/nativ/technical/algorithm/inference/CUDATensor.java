/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.algorithm.inference;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import jcuda.Pointer;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaMemcpyKind;
import jcuda.Sizeof;
import jcuda.jcublas.JCublas2;
import jcuda.jcublas.cublasHandle;
import java.util.Arrays;
import java.util.function.Function;

/**
 * CUDA implementation of Tensor backed by device memory.
 * 
 * @param <T> the element type (currently supports Float, Double)
 */
public class CUDATensor<T> implements Tensor<T> {

    private final Pointer devicePointer;
    private final int[] shape;
    private final int size;
    private final Class<T> explicitType; // Renamed to avoid hiding field/param issues

    public CUDATensor(Pointer devicePointer, int[] shape, Class<T> type) {
        this.devicePointer = devicePointer;
        this.shape = shape.clone();
        this.explicitType = type;
        this.size = Arrays.stream(shape).reduce(1, (a, b) -> a * b);
    }
    
    public Pointer getDevicePointer() {
        return devicePointer;
    }

    public Class<T> getElementType() {
        return explicitType;
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
        // Very slow! Should be avoided. Copies 1 element from GPU.
        long offset = calculateOffset(indices);
        if (explicitType == Float.class) {
            float[] hostData = new float[1];
            JCuda.cudaMemcpy(Pointer.to(hostData), devicePointer.withByteOffset(offset * Sizeof.FLOAT), 
                             Sizeof.FLOAT, cudaMemcpyKind.cudaMemcpyDeviceToHost);
            return explicitType.cast(hostData[0]);
        } else if (explicitType == Double.class) {
            double[] hostData = new double[1];
            JCuda.cudaMemcpy(Pointer.to(hostData), devicePointer.withByteOffset(offset * Sizeof.DOUBLE), 
                             Sizeof.DOUBLE, cudaMemcpyKind.cudaMemcpyDeviceToHost);
            return explicitType.cast(hostData[0]);
        }
        throw new UnsupportedOperationException("Type not supported: " + explicitType);
    }

    @Override
    public void set(T value, int... indices) {
        // Very slow!
        long offset = calculateOffset(indices);
        if (explicitType == Float.class) {
            float[] hostData = { ((Number) value).floatValue() };
            JCuda.cudaMemcpy(devicePointer.withByteOffset(offset * Sizeof.FLOAT), Pointer.to(hostData),
                             Sizeof.FLOAT, cudaMemcpyKind.cudaMemcpyHostToDevice);
        } else if (explicitType == Double.class) {
            double[] hostData = { ((Number) value).doubleValue() };
            JCuda.cudaMemcpy(devicePointer.withByteOffset(offset * Sizeof.DOUBLE), Pointer.to(hostData),
                             Sizeof.DOUBLE, cudaMemcpyKind.cudaMemcpyHostToDevice);
        } else {
             throw new UnsupportedOperationException("Type not supported: " + explicitType);
        }
    }

    private long calculateOffset(int... indices) {
        if (indices.length != shape.length) throw new IllegalArgumentException("Rank mismatch");
        long offset = 0;
        int stride = 1;
        for (int i = shape.length - 1; i >= 0; i--) {
            if (indices[i] < 0 || indices[i] >= shape[i]) throw new IndexOutOfBoundsException();
            offset += indices[i] * stride;
            stride *= shape[i];
        }
        return offset;
    }
    
    // -- Operations to be implemented --
    
    @Override
    public Tensor<T> add(Tensor<T> other) {
         if (!(other instanceof CUDATensor)) throw new IllegalArgumentException("Operand must be a CUDA Tensor");
         CUDATensor<T> o = (CUDATensor<T>) other;
         if (!Arrays.equals(shape, o.shape)) throw new IllegalArgumentException("Shapes mismatch");
         
         CUDATensor<T> result = (CUDATensor<T>) this.copy();
         
         cublasHandle handle = new cublasHandle();
         JCublas2.cublasCreate(handle);
         
         try {
             if (explicitType == Float.class) {
                 JCublas2.cublasSaxpy(handle, size, Pointer.to(new float[]{1.0f}), o.devicePointer, 1, result.devicePointer, 1);
             } else {
                 JCublas2.cublasDaxpy(handle, size, Pointer.to(new double[]{1.0d}), o.devicePointer, 1, result.devicePointer, 1);
             }
         } finally {
             JCublas2.cublasDestroy(handle);
         }
         return result;
    }

    @Override
    public Tensor<T> subtract(Tensor<T> other) {
         if (!(other instanceof CUDATensor)) throw new IllegalArgumentException("Operand must be a CUDA Tensor");
         CUDATensor<T> o = (CUDATensor<T>) other;
         if (!Arrays.equals(shape, o.shape)) throw new IllegalArgumentException("Shapes mismatch");
         
         CUDATensor<T> result = (CUDATensor<T>) this.copy();
         
         cublasHandle handle = new cublasHandle();
         JCublas2.cublasCreate(handle);
         
         try {
             if (explicitType == Float.class) {
                 JCublas2.cublasSaxpy(handle, size, Pointer.to(new float[]{-1.0f}), o.devicePointer, 1, result.devicePointer, 1);
             } else {
                 JCublas2.cublasDaxpy(handle, size, Pointer.to(new double[]{-1.0d}), o.devicePointer, 1, result.devicePointer, 1);
             }
         } finally {
             JCublas2.cublasDestroy(handle);
         }
         return result;
    }

    @Override
    public Tensor<T> multiply(Tensor<T> other) {
         // Hadamard product is NOT supported by standard BLAS (Level 1/2/3).
         // Requires a custom CUDA Kernel.
         throw new UnsupportedOperationException("Hadamard product requires custom CUDA kernel (not available in simplified provider)");
    }

    @Override
    public Tensor<T> scale(T scalar) {
         CUDATensor<T> result = (CUDATensor<T>) this.copy();
         
         cublasHandle handle = new cublasHandle();
         JCublas2.cublasCreate(handle);
         
         try {
             if (explicitType == Float.class) {
                 float val = ((Number)scalar).floatValue();
                 JCublas2.cublasSscal(handle, size, Pointer.to(new float[]{val}), result.devicePointer, 1);
             } else {
                 double val = ((Number)scalar).doubleValue();
                 JCublas2.cublasDscal(handle, size, Pointer.to(new double[]{val}), result.devicePointer, 1);
             }
         } finally {
             JCublas2.cublasDestroy(handle);
         }
         return result;
    }

    @Override
    public Tensor<T> reshape(int... newShape) {
         // Reshape is just a view if total size matches.
         // We can return a new CUDATensor sharing the SAME pointer (View).
         // IMPORTANT: We need to manage lifecycle (who frees the pointer?).
         // For now, simpler: Create new object with SAME pointer.
         // BUT: If one closes/frees, the other dies.
         // JScience Tensor usually doesn't have explicit free().
         // Rely on GC + finalize? Or Unsafe?
         // Let's just create a new view sharing the pointer.
         long newSize = Arrays.stream(newShape).asLongStream().reduce(1, (a, b) -> a * b);
         if (newSize != size) throw new IllegalArgumentException("Size mismatch for reshape");
         
         // WARNING: Sharing pointer without refcount!
         // This is dangerous but standard for simple implementations.
         return new CUDATensor<>(devicePointer, newShape, explicitType);
    }

    @Override
    public Tensor<T> broadcast(int... newShape) {
         throw new UnsupportedOperationException("Broadcast requires complex indexing logic on GPU (not implemented)");
    }

    @Override
    public Tensor<T> transpose(int... permutation) {
         // Basic matrix transpose (2D) can be done with cublasGeam.
         if (rank() == 2 && permutation.length == 2 && permutation[0] == 1 && permutation[1] == 0) {
             // New shape: [cols, rows]
             
             // Allocate result
             // Note: cublas uses Column-Major !
             // If we assume Row-Major storage (Java standard), Transpose in Col-Major is equivalent to Transpose in Row-Major?
             // Actually, if we use C-style (Row Major), we need to be careful.
             // cublasGeam can do C = alpha * op(A) + beta * op(B).
             // C = transpose(A).
             
             // This is getting complex for a quick fix.
             throw new UnsupportedOperationException("Transpose not robustly implemented yet");
         }
         throw new UnsupportedOperationException("General transpose not implemented");
    }

    @Override
    public Tensor<T> slice(int[] starts, int[] sizes) {
         throw new UnsupportedOperationException("Slice not implemented yet");
    }

    @Override
    public Tensor<T> map(Function<T, T> function) {
         // Map is typically CPU lambda -> Cannot run on GPU easily.
         // Unless we transfer to CPU, map, transfer back.
         // That's valid but slow.
         throw new UnsupportedOperationException("Map with Java lambda cannot run on GPU");
    }

    @Override
    public T sum() {
         // cublasSasum / Dasum returns sum of absolute values? No.
         // cublasDot (dot product with vector of 1s).
         cublasHandle handle = new cublasHandle();
         JCublas2.cublasCreate(handle);
         try {
             if (explicitType == Float.class) {
                 // Create vector of 1s
                 Pointer ones = new Pointer();
                 JCuda.cudaMalloc(ones, size * Sizeof.FLOAT);
                 // ... this is expensive just for sum.
                 
                 // Fallback: Copy to host and sum.
                 float[] data = new float[size];
                 JCuda.cudaMemcpy(Pointer.to(data), devicePointer, size * Sizeof.FLOAT, cudaMemcpyKind.cudaMemcpyDeviceToHost);
                 float sum = 0;
                 for(float f : data) sum += f;
                 return explicitType.cast(sum);
             } else {
                 double[] data = new double[size];
                 JCuda.cudaMemcpy(Pointer.to(data), devicePointer, size * Sizeof.DOUBLE, cudaMemcpyKind.cudaMemcpyDeviceToHost);
                 double sum = 0;
                 for(double d : data) sum += d;
                 return explicitType.cast(sum);
             }
         } finally {
             JCublas2.cublasDestroy(handle);
         }
    }

    @Override
    public Tensor<T> sum(int axis) {
         throw new UnsupportedOperationException("Axis reduction not implemented");
    }

    @Override
    public Tensor<T> copy() {
         long byteSize = size * (long)((explicitType == Float.class) ? Sizeof.FLOAT : Sizeof.DOUBLE);
         Pointer newPtr = new Pointer();
         JCuda.cudaMalloc(newPtr, byteSize);
         JCuda.cudaMemcpy(newPtr, devicePointer, byteSize, cudaMemcpyKind.cudaMemcpyDeviceToDevice);
         return new CUDATensor<>(newPtr, shape, explicitType);
    }

    @Override
    public Object toArray() {
         throw new UnsupportedOperationException("ToArray not implemented");
    }
}

