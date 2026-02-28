/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.tensors;

import jcuda.Pointer;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaMemcpyKind;
import jcuda.Sizeof;
import jcuda.jcublas.JCublas2;
import jcuda.jcublas.cublasHandle;
import jcuda.jcublas.cublasOperation;
import org.episteme.core.mathematics.linearalgebra.Tensor;
import java.util.Arrays;
import java.util.function.Function;

/**
 * CUDA implementation of Tensor backed by device (GPU) memory.
 * <p>
 * Supports Float and Double element types. Operations use cuBLAS where
 * possible, with GPU↔CPU roundtrip fallbacks for operations that require
 * custom CUDA kernels not yet available.
 * </p>
 *
 * @param <T> the element type (currently supports Float, Double)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class CUDATensor<T> implements Tensor<T> {

    private static final long serialVersionUID = 1L;

    private final Pointer devicePointer;
    private final int[] shape;
    private final int size;
    private final Class<T> explicitType;

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

    // ========== BLAS-backed Operations ==========

    @Override
    public Tensor<T> add(Tensor<T> other) {
         if (!(other instanceof CUDATensor)) throw new IllegalArgumentException("Operand must be a CUDATensor");
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
         if (!(other instanceof CUDATensor)) throw new IllegalArgumentException("Operand must be a CUDATensor");
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
         if (!(other instanceof CUDATensor)) throw new IllegalArgumentException("Operand must be a CUDATensor");
         CUDATensor<T> o = (CUDATensor<T>) other;
         if (!Arrays.equals(shape, o.shape)) throw new IllegalArgumentException("Shapes mismatch");
         if (explicitType == Float.class) {
             float[] a = copyToHostFloat();
             float[] b = o.copyToHostFloat();
             float[] result = new float[size];
             for (int i = 0; i < size; i++) result[i] = a[i] * b[i];
             return fromHostFloat(result, shape, explicitType);
         } else {
             double[] a = copyToHostDouble();
             double[] b = o.copyToHostDouble();
             double[] result = new double[size];
             for (int i = 0; i < size; i++) result[i] = a[i] * b[i];
             return fromHostDouble(result, shape, explicitType);
         }
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
         long newSize = Arrays.stream(newShape).asLongStream().reduce(1, (a, b) -> a * b);
         if (newSize != size) throw new IllegalArgumentException("Size mismatch for reshape");
         return new CUDATensor<>(devicePointer, newShape, explicitType);
    }

    @Override
    public Tensor<T> broadcast(int... newShape) {
         if (newShape.length < rank()) throw new IllegalArgumentException("Target shape must have at least equal rank");
         int dimOffset = newShape.length - rank();
         for (int i = newShape.length - 1; i >= 0; i--) {
             int oldIdx = i - dimOffset;
             if (oldIdx >= 0) {
                 int oldDim = shape[oldIdx];
                 int newDim = newShape[i];
                 if (oldDim != newDim && oldDim != 1) {
                     throw new IllegalArgumentException("Incompatible shapes for broadcast: " +
                             Arrays.toString(shape) + " -> " + Arrays.toString(newShape));
                 }
             }
         }
         long newSize = Arrays.stream(newShape).asLongStream().reduce(1, (a, b) -> a * b);
         if (explicitType == Float.class) {
             float[] src = copyToHostFloat();
             float[] dst = new float[(int)newSize];
             broadcastCopy(src, dst, shape, newShape, dimOffset);
             return fromHostFloat(dst, newShape, explicitType);
         } else {
             double[] src = copyToHostDouble();
             double[] dst = new double[(int)newSize];
             broadcastCopy(src, dst, shape, newShape, dimOffset);
             return fromHostDouble(dst, newShape, explicitType);
         }
    }

    @Override
    public Tensor<T> transpose(int... permutation) {
         if (permutation.length != rank()) throw new IllegalArgumentException("Permutation length must match rank");
         if (rank() == 2 && permutation[0] == 1 && permutation[1] == 0) {
             int rows = shape[0];
             int cols = shape[1];
             int[] newShape = { cols, rows };
             cublasHandle handle = new cublasHandle();
             JCublas2.cublasCreate(handle);
             try {
                 long byteSize = (long)size * ((explicitType == Float.class) ? Sizeof.FLOAT : Sizeof.DOUBLE);
                 Pointer resultPtr = new Pointer();
                 JCuda.cudaMalloc(resultPtr, byteSize);
                 if (explicitType == Float.class) {
                     float[] alpha = {1.0f};
                     float[] beta = {0.0f};
                     JCublas2.cublasSgeam(handle, cublasOperation.CUBLAS_OP_T, cublasOperation.CUBLAS_OP_N,
                             rows, cols, Pointer.to(alpha), devicePointer, cols, Pointer.to(beta), resultPtr, rows, resultPtr, rows);
                 } else {
                     double[] alpha = {1.0};
                     double[] beta = {0.0};
                     JCublas2.cublasDgeam(handle, cublasOperation.CUBLAS_OP_T, cublasOperation.CUBLAS_OP_N,
                             rows, cols, Pointer.to(alpha), devicePointer, cols, Pointer.to(beta), resultPtr, rows, resultPtr, rows);
                 }
                 return new CUDATensor<>(resultPtr, newShape, explicitType);
             } finally {
                 JCublas2.cublasDestroy(handle);
             }
         }
         if (explicitType == Float.class) {
             float[] src = copyToHostFloat();
             int[] newShape = new int[rank()];
             for (int i = 0; i < rank(); i++) newShape[i] = shape[permutation[i]];
             float[] dst = new float[size];
             transposeGeneric(src, dst, shape, newShape, permutation);
             return fromHostFloat(dst, newShape, explicitType);
         } else {
             double[] src = copyToHostDouble();
             int[] newShape = new int[rank()];
             for (int i = 0; i < rank(); i++) newShape[i] = shape[permutation[i]];
             double[] dst = new double[size];
             transposeGeneric(src, dst, shape, newShape, permutation);
             return fromHostDouble(dst, newShape, explicitType);
         }
    }

    @Override
    public Tensor<T> slice(int[] starts, int[] sizes) {
         if (starts.length != rank() || sizes.length != rank()) throw new IllegalArgumentException("Slice args must match rank");
         for (int i = 0; i < rank(); i++) {
             if (starts[i] < 0 || starts[i] + sizes[i] > shape[i]) throw new IndexOutOfBoundsException("Slice out of bounds at dim " + i);
         }
         int sliceSize = Arrays.stream(sizes).reduce(1, (a, b) -> a * b);
         if (explicitType == Float.class) {
             float[] src = copyToHostFloat();
             float[] dst = new float[sliceSize];
             sliceCopy(src, dst, shape, starts, sizes, 0, 0, 0);
             return fromHostFloat(dst, sizes, explicitType);
         } else {
             double[] src = copyToHostDouble();
             double[] dst = new double[sliceSize];
             sliceCopy(src, dst, shape, starts, sizes, 0, 0, 0);
             return fromHostDouble(dst, sizes, explicitType);
         }
    }

    @Override
    public Tensor<T> map(Function<T, T> function) {
         if (explicitType == Float.class) {
             float[] data = copyToHostFloat();
             float[] result = new float[size];
             for (int i = 0; i < size; i++) {
                 T val = explicitType.cast(Float.valueOf(data[i]));
                 result[i] = ((Number) function.apply(val)).floatValue();
             }
             return fromHostFloat(result, shape, explicitType);
         } else {
             double[] data = copyToHostDouble();
             double[] result = new double[size];
             for (int i = 0; i < size; i++) {
                 T val = explicitType.cast(Double.valueOf(data[i]));
                 result[i] = ((Number) function.apply(val)).doubleValue();
             }
             return fromHostDouble(result, shape, explicitType);
         }
    }

    @Override
    public T sum() {
         if (explicitType == Float.class) {
             float[] data = copyToHostFloat();
             float s = 0;
             for (float f : data) s += f;
             return explicitType.cast(s);
         } else {
             double[] data = copyToHostDouble();
             double s = 0;
             for (double d : data) s += d;
             return explicitType.cast(s);
         }
    }

    @Override
    public Tensor<T> sum(int axis) {
         if (axis < 0 || axis >= rank()) throw new IllegalArgumentException("Invalid axis: " + axis);
         int[] newShape = new int[rank() - 1];
         int idx = 0;
         for (int i = 0; i < rank(); i++) {
             if (i != axis) newShape[idx++] = shape[i];
         }
         if (newShape.length == 0) newShape = new int[]{1};
         int newSize = Arrays.stream(newShape).reduce(1, (a, b) -> a * b);
         if (explicitType == Float.class) {
             float[] src = copyToHostFloat();
             float[] dst = new float[newSize];
             sumAlongAxis(src, dst, shape, axis);
             return fromHostFloat(dst, newShape, explicitType);
         } else {
             double[] src = copyToHostDouble();
             double[] dst = new double[newSize];
             sumAlongAxis(src, dst, shape, axis);
             return fromHostDouble(dst, newShape, explicitType);
         }
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
         if (explicitType == Float.class) {
             float[] flat = copyToHostFloat();
             if (rank() == 1) return flat;
             return buildNestedArrayFloat(flat, shape, 0, 0);
         } else {
             double[] flat = copyToHostDouble();
             if (rank() == 1) return flat;
             return buildNestedArrayDouble(flat, shape, 0, 0);
         }
    }

    // ========== GPU↔CPU Transfer Helpers ==========

    private float[] copyToHostFloat() {
        float[] data = new float[size];
        JCuda.cudaMemcpy(Pointer.to(data), devicePointer, (long)size * Sizeof.FLOAT, cudaMemcpyKind.cudaMemcpyDeviceToHost);
        return data;
    }

    private double[] copyToHostDouble() {
        double[] data = new double[size];
        JCuda.cudaMemcpy(Pointer.to(data), devicePointer, (long)size * Sizeof.DOUBLE, cudaMemcpyKind.cudaMemcpyDeviceToHost);
        return data;
    }

    private static <T> CUDATensor<T> fromHostFloat(float[] data, int[] shape, Class<T> type) {
        long byteSize = (long)data.length * Sizeof.FLOAT;
        Pointer ptr = new Pointer();
        JCuda.cudaMalloc(ptr, byteSize);
        JCuda.cudaMemcpy(ptr, Pointer.to(data), byteSize, cudaMemcpyKind.cudaMemcpyHostToDevice);
        return new CUDATensor<>(ptr, shape, type);
    }

    private static <T> CUDATensor<T> fromHostDouble(double[] data, int[] shape, Class<T> type) {
        long byteSize = (long)data.length * Sizeof.DOUBLE;
        Pointer ptr = new Pointer();
        JCuda.cudaMalloc(ptr, byteSize);
        JCuda.cudaMemcpy(ptr, Pointer.to(data), byteSize, cudaMemcpyKind.cudaMemcpyHostToDevice);
        return new CUDATensor<>(ptr, shape, type);
    }

    // ========== CPU-side Helpers ==========

    private static void broadcastCopy(Object src, Object dst, int[] srcShape, int[] dstShape, int dimOffset) {
        int dstSize = Arrays.stream(dstShape).reduce(1, (a, b) -> a * b);
        int srcRank = srcShape.length;
        int[] srcStrides = computeStrides(srcShape);
        int[] dstStrides = computeStrides(dstShape);
        for (int i = 0; i < dstSize; i++) {
            int[] dstIndices = flatToIndices(i, dstShape, dstStrides);
            int srcIdx = 0;
            for (int d = srcRank - 1; d >= 0; d--) {
                int coord = (srcShape[d] == 1) ? 0 : dstIndices[d + dimOffset];
                srcIdx += coord * srcStrides[d];
            }
            if (src instanceof float[]) ((float[])dst)[i] = ((float[])src)[srcIdx];
            else ((double[])dst)[i] = ((double[])src)[srcIdx];
        }
    }

    private static void transposeGeneric(Object src, Object dst, int[] srcShape, int[] dstShape, int[] perm) {
        int sz = Arrays.stream(srcShape).reduce(1, (a, b) -> a * b);
        int rank = srcShape.length;
        int[] srcStrides = computeStrides(srcShape);
        int[] dstStrides = computeStrides(dstShape);
        for (int i = 0; i < sz; i++) {
            int[] srcIndices = flatToIndices(i, srcShape, srcStrides);
            int dstIdx = 0;
            for (int d = 0; d < rank; d++) dstIdx += srcIndices[perm[d]] * dstStrides[d];
            if (src instanceof float[]) ((float[])dst)[dstIdx] = ((float[])src)[i];
            else ((double[])dst)[dstIdx] = ((double[])src)[i];
        }
    }

    private static void sliceCopy(Object src, Object dst, int[] srcShape, int[] starts, int[] sizes, int dim, int srcOffset, int dstOffset) {
        if (dim == srcShape.length) {
            if (src instanceof float[]) ((float[])dst)[dstOffset] = ((float[])src)[srcOffset];
            else ((double[])dst)[dstOffset] = ((double[])src)[srcOffset];
            return;
        }
        int srcStride = 1; for (int d = dim+1; d < srcShape.length; d++) srcStride *= srcShape[d];
        int dstStride = 1; for (int d = dim+1; d < sizes.length; d++) dstStride *= sizes[d];
        for (int i = 0; i < sizes[dim]; i++) {
            sliceCopy(src, dst, srcShape, starts, sizes, dim+1, srcOffset+(starts[dim]+i)*srcStride, dstOffset+i*dstStride);
        }
    }

    private static void sumAlongAxis(Object src, Object dst, int[] shape, int axis) {
        int sz = Arrays.stream(shape).reduce(1, (a, b) -> a * b);
        int[] strides = computeStrides(shape);
        int[] reducedShape = new int[shape.length - 1];
        int idx = 0;
        for (int i = 0; i < shape.length; i++) if (i != axis) reducedShape[idx++] = shape[i];
        int[] reducedStrides = computeStrides(reducedShape.length > 0 ? reducedShape : new int[]{1});
        for (int i = 0; i < sz; i++) {
            int[] indices = flatToIndices(i, shape, strides);
            int dstIdx = 0; int dIdx = 0;
            for (int d = 0; d < shape.length; d++) {
                if (d != axis) { dstIdx += indices[d] * reducedStrides[dIdx]; dIdx++; }
            }
            if (src instanceof float[]) ((float[])dst)[dstIdx] += ((float[])src)[i];
            else ((double[])dst)[dstIdx] += ((double[])src)[i];
        }
    }

    private static int[] computeStrides(int[] shape) {
        int[] strides = new int[shape.length];
        int stride = 1;
        for (int i = shape.length - 1; i >= 0; i--) { strides[i] = stride; stride *= shape[i]; }
        return strides;
    }

    private static int[] flatToIndices(int flat, int[] shape, int[] strides) {
        int[] indices = new int[shape.length];
        for (int d = 0; d < shape.length; d++) { indices[d] = flat / strides[d]; flat %= strides[d]; }
        return indices;
    }

    private static Object buildNestedArrayFloat(float[] flat, int[] shape, int dim, int offset) {
        int length = shape[dim];
        if (dim == shape.length - 1) return Arrays.copyOfRange(flat, offset, offset + length);
        Object[] result = new Object[length];
        int stride = 1;
        for (int d = dim+1; d < shape.length; d++) stride *= shape[d];
        for (int i = 0; i < length; i++) result[i] = buildNestedArrayFloat(flat, shape, dim+1, offset+i*stride);
        return result;
    }

    private static Object buildNestedArrayDouble(double[] flat, int[] shape, int dim, int offset) {
        int length = shape[dim];
        if (dim == shape.length - 1) return Arrays.copyOfRange(flat, offset, offset + length);
        Object[] result = new Object[length];
        int stride = 1;
        for (int d = dim+1; d < shape.length; d++) stride *= shape[d];
        for (int i = 0; i < length; i++) result[i] = buildNestedArrayDouble(flat, shape, dim+1, offset+i*stride);
        return result;
    }
}
