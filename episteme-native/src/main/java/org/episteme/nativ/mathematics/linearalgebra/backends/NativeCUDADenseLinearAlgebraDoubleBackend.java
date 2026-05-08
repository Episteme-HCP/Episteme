/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.FieldElement;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.technical.backend.gpu.cuda.CUDAManager;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Native CUDA Dense Linear Algebra Backend for Double precision.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDADenseLinearAlgebraDoubleBackend<E extends FieldElement<E>> implements LinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeCUDADenseLinearAlgebraDoubleBackend.class);

    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "cuda"; }
    
    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        return CUDAManager.isAvailable();
    }

    public boolean isExplicitlyDisabled() {
        return Boolean.getBoolean("episteme.backend.native.disabled") ||
               Boolean.getBoolean("episteme.backend.cuda.disabled") || 
               Boolean.getBoolean("episteme.backend.gpu.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra-cuda.disabled");
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        Object zero = ring.zero();
        return zero instanceof RealDouble || zero instanceof Complex || (zero instanceof Real r && !r.isFast());
    }

    @Override public String getId() { return "cuda-dense-double"; }
    @Override public String getName() { return "Native CUDA Dense Linear Algebra Double Backend"; }
    @Override public int getPriority() { return 105; }
    @Override public void shutdown() {}

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return multiplyComplex(a, b);
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 8, tracker);
            MemorySegment d_B = malloc((long) k * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) m * k * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) k * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DGEMM, handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_C, n));
            
            double[] result = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segC, d_C, (long) m * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n);
            return toMatrix(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA multiply failed", t); }
    }

    private Matrix<E> multiplyComplex(Matrix<E> a, Matrix<E> b) {
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 16, tracker);
            MemorySegment d_B = malloc((long) k * n * 16, tracker);
            MemorySegment d_C = malloc((long) m * n * 16, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) m * k * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(b)), (long) k * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_ZGEMM, handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_C, n));
            
            double[] result = new double[m * n * 2];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segC, d_C, (long) m * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, result, 0, m * n * 2);
            return toMatrixComplex(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA complex multiply failed", t); }
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (isComplex(a)) return transposeComplex(a);
        int rows = a.rows(); int cols = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rows * cols * 8, tracker);
            MemorySegment d_C = malloc((long) rows * cols * 8, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) rows * cols * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DGEAM, handle, 1, 0, rows, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_A, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_A, cols, 
                d_C, rows));
            
            double[] result = new double[rows * cols];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) rows * cols);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, host, d_C, (long) rows * cols * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, result, 0, rows * cols);
            return toMatrix(result, cols, rows, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA transpose failed", t); }
    }

    private Matrix<E> transposeComplex(Matrix<E> a) {
        int rows = a.rows(); int cols = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rows * cols * 16, tracker);
            MemorySegment d_C = malloc((long) rows * cols * 16, tracker);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) rows * cols * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_ZGEAM, handle, 1, 0, rows, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_A, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_A, cols, 
                d_C, rows));
            
            double[] result = new double[rows * cols * 2];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) rows * cols * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, host, d_C, (long) rows * cols * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, result, 0, rows * cols * 2);
            return toMatrixComplex(result, cols, rows, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA complex transpose failed", t); }
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (isComplex(a)) return inverseComplex(a);
        if (!CUDAManager.isUseCusolver()) throw new UnsupportedOperationException("cuSolver not available");
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segB = malloc((long) n * n * 8, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            
            double[] h_A = toDoubleArray(a); 
            double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            
            double[] identity = new double[n * n]; 
            for (int i = 0; i < n; i++) identity[i * n + i] = 1.0;
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity), (long) n * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF_BUFFER_SIZE, handle, n, n, segA, n, p_Lwork));
            
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRF, handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_DGETRS, handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));
            
            double[] resData = new double[n * n];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostB, segB, (long) n * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * n);
            
            double[] h_Res = new double[n * n]; 
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_Res[r * n + c] = resData[c * n + r];
            return toMatrix(h_Res, n, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA inverse failed", t); }
    }

    private Matrix<E> inverseComplex(Matrix<E> a) {
        if (!CUDAManager.isUseCusolver()) throw new UnsupportedOperationException("cuSolver not available");
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = CUDAManager.getCusolverHandle();
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segB = malloc((long) n * n * 16, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            
            double[] h_A = toComplexDoubleArray(a);
            double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2];
                    h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                }
            }
            
            double[] identity = new double[n * n * 2];
            for (int i = 0; i < n; i++) identity[(i * n + i) * 2] = 1.0;
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity), (long) n * n * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF_BUFFER_SIZE, handle, n, n, segA, n, p_Lwork));
            
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRF, handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCusolver((int) NativeSafe.invoke(CUDAManager.CUSOLVER_ZGETRS, handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));
            
            double[] resData = new double[n * n * 2];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostB, segB, (long) n * n * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * n * 2);
            
            double[] h_Res = new double[n * n * 2];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    h_Res[(r * n + c) * 2] = resData[(c * n + r) * 2];
                    h_Res[(r * n + c) * 2 + 1] = resData[(c * n + r) * 2 + 1];
                }
            }
            return toMatrixComplex(h_Res, n, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA complex inverse failed", t); }
    }

    private MemorySegment malloc(long size, ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MALLOC, p, size));
            MemorySegment d = p.get(ValueLayout.ADDRESS, 0);
            return tracker.track(d, ptr -> {
                try { NativeSafe.invoke(CUDAManager.CUDA_FREE, ptr); } catch (Throwable t) {}
            });
        } catch (Throwable t) { throw new RuntimeException("CUDA malloc failed", t); }
    }

    private void checkCuda(int status) {
        if (status != 0) throw new RuntimeException("CUDA error: " + status);
    }

    private void checkCublas(int status) {
        if (status != 0) throw new RuntimeException("cuBLAS error: " + status);
    }

    private void checkCusolver(int status) {
        if (status != 0) throw new RuntimeException("cuSolver error: " + status);
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        double[] data = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = ((Number) m.get(i, j)).doubleValue();
            }
        }
        return data;
    }

    private double[] toComplexDoubleArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        double[] data = new double[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex c = (Complex) m.get(i, j);
                data[(i * cols + j) * 2] = ((Number) c.real()).doubleValue();
                data[(i * cols + j) * 2 + 1] = ((Number) c.imaginary()).doubleValue();
            }
        }
        return data;
    }

    private Matrix<E> toMatrix(double[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) values[i] = (E) RealDouble.of(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(values, rows, cols, ring);
    }

    private Matrix<E> toMatrixComplex(double[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), rows * cols);
        for (int i = 0; i < rows * cols; i++) values[i] = (E) Complex.of(data[i * 2], data[i * 2 + 1]);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(values, rows, cols, ring);
    }

    private boolean isComplex(Matrix<E> a) { return a.getScalarRing().zero() instanceof Complex; }

    @Override public void close() {}
    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext();
    }

    @Override
    public org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[] getDevices() {
        if (!isAvailable()) return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[0];
        return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[]{
            new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo(
                "CUDA Device", 0, 0, "CUDA")
        };
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MALLOC, p, sizeBytes));
            return p.get(ValueLayout.ADDRESS, 0).address();
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public void copyToGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, MemorySegment.ofAddress(gpuHandle), MemorySegment.ofBuffer(hostBuffer), sizeBytes, CUDAManager.CUDA_MEMCPY_H_TO_D));
    }

    @Override
    public void copyFromGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, MemorySegment.ofBuffer(hostBuffer), MemorySegment.ofAddress(gpuHandle), sizeBytes, CUDAManager.CUDA_MEMCPY_D_TO_H));
    }

    @Override
    public void freeGPUMemory(long gpuHandle) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_FREE, MemorySegment.ofAddress(gpuHandle)));
    }

    @Override
    public void synchronize() {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_DEVICE_SYNCHRONIZE));
    }

    @Override
    public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) {
        CUDAManager.ensureInitialized();
        MemorySegment handle = CUDAManager.getCublasHandle();
        try (Arena arena = Arena.ofConfined()) {
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_DGEMM, handle, 0, 0, n, m, k, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), MemorySegment.ofBuffer(B), n, MemorySegment.ofBuffer(A), k, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), MemorySegment.ofBuffer(C), n));
        } catch (Throwable t) { throw new RuntimeException(t); }
    }
}
