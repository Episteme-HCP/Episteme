/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import java.util.Optional;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader;
import com.google.auto.service.AutoService;

/**
 * High-performance Dense CUDA Backend using Project Panama (FFM).
 * Binds directly to cuBLAS for matrix operations.
 * Implements {@link LinearAlgebraProvider} (Dense).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class, GPUBackend.class, NativeBackend.class, LinearAlgebraProvider.class})
public class NativeCUDADenseLinearAlgebraBackend implements NativeBackend, LinearAlgebraProvider<Real>, GPUBackend {

    private static boolean IS_AVAILABLE = false;
    private static final Linker LINKER = NativeLibraryLoader.getLinker();

    // CUDA/cuBLAS Handles
    private static MethodHandle CUBLAS_CREATE;
    private static MethodHandle CUBLAS_DESTROY;
    private static MethodHandle CUBLAS_DGEMM;
    private static MethodHandle CUDA_MALLOC;
    private static MethodHandle CUDA_FREE;
    private static MethodHandle CUDA_MEMCPY;
    private static MethodHandle CUDA_DEVICE_SYNCHRONIZE;

    // Constants
    private static final int CUBLAS_OP_N = 0;
    private static final int CUDA_MEMCPY_HOST_TO_DEVICE = 1;
    private static final int CUDA_MEMCPY_DEVICE_TO_HOST = 2;

    private static synchronized void ensureInitialized() {
        if (IS_AVAILABLE) return;

        SymbolLookup cuda = null;
        SymbolLookup cublas = null;

        try (Arena arena = Arena.ofConfined()) { // Test loading
            Optional<SymbolLookup> cudaOpt = NativeLibraryLoader.loadLibrary("cuda", Arena.global());
            Optional<SymbolLookup> cublasOpt = NativeLibraryLoader.loadLibrary("cublas", Arena.global());
            
            if (cudaOpt.isEmpty() || cublasOpt.isEmpty()) return;
            
            cuda = cudaOpt.get();
            cublas = cublasOpt.get();
            
            

            // CUDA Runtime Memory Management
            CUDA_MALLOC = LINKER.downcallHandle(
                cuda.find("cudaMalloc").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
            );
            CUDA_FREE = LINKER.downcallHandle(
                cuda.find("cudaFree").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
            );
            CUDA_MEMCPY = LINKER.downcallHandle(
                cuda.find("cudaMemcpy").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT)
            );
            CUDA_DEVICE_SYNCHRONIZE = LINKER.downcallHandle(
                cuda.find("cudaDeviceSynchronize").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT)
            );

            // cuBLAS
            CUBLAS_CREATE = LINKER.downcallHandle(
                cublas.find("cublasCreate_v2").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
            );
            CUBLAS_DESTROY = LINKER.downcallHandle(
                cublas.find("cublasDestroy_v2").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
            );
            CUBLAS_DGEMM = LINKER.downcallHandle(
                cublas.find("cublasDgemm_v2").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT
                )
            );

            IS_AVAILABLE = true;
        } catch (Throwable t) {
            IS_AVAILABLE = false;
            System.out.println("\n[WARNING] Native CUDA Backend initialization completely failed: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            t.printStackTrace(System.out);
        }
    }

    @Override
    public boolean isAvailable() { ensureInitialized(); return IS_AVAILABLE; }

    @Override
    public boolean isLoaded() { return IS_AVAILABLE; }

    @Override
    public String getEnvironmentInfo() {
        return IS_AVAILABLE ? "GPU (CUDA)" : "N/A";
    }

    @Override
    public String getName() { return "Native CUDA Dense Backend"; }

    @Override
    public int getPriority() { return 110; } // Higher than Native SIMD (90) and Native BLAS (100)

    @Override
    public boolean isCompatible(Ring<?> ring) { return ring instanceof Reals; }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA not available");

        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        if (k != b.rows()) throw new IllegalArgumentException("Dimension mismatch");

        try (Arena arena = Arena.ofConfined()) {
            // Allocate Host Buffers
            double[] h_A = toDoubleArray(a);
            double[] h_B = toDoubleArray(b);

            // Allocate Device Pointers
            MemorySegment p_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_B = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_C = arena.allocate(ValueLayout.ADDRESS);

            checkCuda(CUDA_MALLOC.invokeExact(p_A, (long) m * k * 8));
            checkCuda(CUDA_MALLOC.invokeExact(p_B, (long) k * n * 8));
            checkCuda(CUDA_MALLOC.invokeExact(p_C, (long) m * n * 8));

            MemorySegment d_A = p_A.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_B = p_B.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_C = p_C.get(ValueLayout.ADDRESS, 0);

            // Copy to Device
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A);
            MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B);

            checkCuda(CUDA_MEMCPY.invokeExact(d_A, segA, (long) m * k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda(CUDA_MEMCPY.invokeExact(d_B, segB, (long) k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            // cuBLAS Handle
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCublas(CUBLAS_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            // cuBLAS DGEMM Setup (Alpha, Beta)
            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            // Call DGEMM
            // Note: cuBLAS is Column Major by default. 
            // To compute C = A * B in Row Major, we can compute C_T = B_T * A_T.
            // Or just use cuBLAS's column-major logic with swapped arguments.
            checkCublas(CUBLAS_DGEMM.invokeExact(handle, CUBLAS_OP_N, CUBLAS_OP_N, 
                n, m, k, alpha, d_B, n, d_A, k, beta, d_C, n));

            // Copy back
            double[] h_C = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda(CUDA_MEMCPY.invokeExact(segC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);

            // Cleanup
            CUBLAS_DESTROY.invokeExact(handle);
            CUDA_FREE.invokeExact(d_A);
            CUDA_FREE.invokeExact(d_B);
            CUDA_FREE.invokeExact(d_C);

            return fromDoubleArray(h_C, m, n);

        } catch (Throwable t) {
            throw new RuntimeException("CUDA GPU execution error", t);
        }
    }

    private void checkCuda(Object result) {
        int code = (int) result;
        if (code != 0) throw new RuntimeException("CUDA Error: " + code);
    }

    private void checkCublas(Object result) {
        int code = (int) result;
        if (code != 0) throw new RuntimeException("cuBLAS Error: " + code);
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                data[i*cols + j] = m.get(i, j).doubleValue();
            }
        }
        return data;
    }

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[data.length];
        for(int i=0; i<data.length; i++) reals[i] = Real.of(data[i]);
        return new DenseMatrix<Real>(reals, rows, cols, Reals.getInstance());
    }

    @Override public String getNativeLibraryName() { return "cuda"; }
    @Override public DeviceInfo[] getDevices() { return new DeviceInfo[0]; }
    @Override public void selectDevice(int deviceId) { }
    @Override public long allocateGPUMemory(long size) { return 0; }
    @Override public void copyToGPU(long handle, DoubleBuffer buffer, long count) { }
    @Override public void copyFromGPU(long handle, DoubleBuffer buffer, long count) { }
    @Override public void freeGPUMemory(long handle) { }
    @Override public void synchronize() { try { CUDA_DEVICE_SYNCHRONIZE.invokeExact(); } catch (Throwable t) {} }
    @Override public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) { }

    @Override
    public double score(OperationContext context) {
        if (!IS_AVAILABLE) return -1;
        if (MathContext.getCurrent().getRealPrecision() == MathContext.RealPrecision.EXACT) {
            return -1.0; // Hardware Float/Double cannot handle Arbitrary Precision MathContext
        }
        double base = AutoTuningManager.getDynamicScore(getName(), context.getDimensionality(), getPriority());
        if (context.getDataSize() < 256) base -= 200; // Prefer CPU for small matrices
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 50;
        return base;
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
    }
}
