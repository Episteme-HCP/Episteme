/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.jocl.*;
import static org.jocl.CL.*;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import com.google.auto.service.AutoService;


import java.nio.DoubleBuffer;

/**
 * OpenCL implementation of Dense Linear Algebra Provider.
 * Uses JOCL to provide cross-platform GPU acceleration.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, GPUBackend.class})
public class NativeOpenCLDenseLinearAlgebraBackend implements LinearAlgebraProvider<Real>, NativeBackend, GPUBackend {

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLDenseLinearAlgebraBackend.class);
    private static cl_context context;
    private static cl_command_queue commandQueue;
    private static cl_kernel matMulKernel;
    private static cl_kernel vecAddKernel;
    private static cl_kernel vecSubKernel;
    private static cl_kernel vecScaleKernel;
    private static cl_kernel vecDotPartialKernel;
    private static cl_kernel transposeKernel;
    private static cl_kernel normalizeRowKernel;
    private static cl_kernel gaussJordanKernel;
    private static cl_kernel gaussElimPhase1Kernel;
    private static cl_program program;
    private static volatile boolean initialized = false;
    private static volatile boolean initAttempted = false;

    private static final String KERNEL_SOURCE =
        "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
        "__kernel void matrixMultiply(__global const double *a, __global const double *b, __global double *c, const int m, const int n, const int k) {\n" +
        "    int row = get_global_id(1); int col = get_global_id(0);\n" +
        "    if (row < m && col < n) {\n" +
        "        double sum = 0.0;\n" +
        "        for (int i = 0; i < k; i++) sum += a[row*k+i] * b[i*n+col];\n" +
        "        c[row*n+col] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void vec_add(__global const double *a, __global const double *b, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] + b[i];\n" +
        "}\n" +
        "__kernel void vec_sub(__global const double *a, __global const double *b, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] - b[i];\n" +
        "}\n" +
        "__kernel void vec_scale(__global const double *a, const double s, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] * s;\n" +
        "}\n" +
        "__kernel void vec_dot_partial(__global const double *a, __global const double *b, __global double *out, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) out[i] = a[i] * b[i];\n" +
        "}\n" +
        "__kernel void gaussElimPhase1(__global double *a, const int n, const int k) {\n" +
        "    int i = get_global_id(0) + k + 1;\n" +
        "    if (i < n) {\n" +
        "        double factor = a[i*n + k] / a[k*n + k];\n" +
        "        for (int j = k + 1; j < n; j++) a[i*n + j] -= factor * a[k*n + j];\n" +
        "        a[i*n + k] = 0.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void transpose(__global const double *a, __global double *b, const int rows, const int cols) {\n" +
        "    int r = get_global_id(1); int c = get_global_id(0);\n" +
        "    if (r < rows && c < cols) b[c * rows + r] = a[r * cols + c];\n" +
        "}\n" +
        "__kernel void normalizeRow(__global double *a, const int rows, const int cols, const int k) {\n" +
        "    int j = get_global_id(0) + k + 1;\n" +
        "    double pivot = a[k * cols + k];\n" +
        "    if (j < cols) a[k * cols + j] /= pivot;\n" +
        "    if (j == k + 1) a[k * cols + k] = 1.0;\n" + // Only one thread does this
        "}\n" +
        "__kernel void gaussJordan(__global double *a, const int rows, const int cols, const int k) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < rows && i != k) {\n" +
        "        double factor = a[i * cols + k];\n" +
        "        for (int j = k + 1; j < cols; j++) a[i * cols + j] -= factor * a[k * cols + j];\n" +
        "        a[i * cols + k] = 0.0;\n" +
        "    }\n" +
        "}\n";

    private static synchronized void init() {
        if (initAttempted) return;
        initAttempted = true;
        
        // Defensive check for JOCL classes to avoid NoClassDefFoundError if lib is missing
        try {
            Class.forName("org.jocl.CL");
        } catch (ClassNotFoundException e) {
            logger.warn("JOCL classes not found on classpath. OpenCL backend disabled.");
            return;
        }

        logger.info("Initializing Native OpenCL Dense Backend...");
        try {
            setExceptionsEnabled(true);
            int[] numPlatformsArray = new int[1];
            
            // This might still throw UnsatisfiedLinkError if JOCL native is missing
            clGetPlatformIDs(0, null, numPlatformsArray);
            
            if (numPlatformsArray[0] == 0) {
                logger.info("No OpenCL platforms found.");
                return;
            }

            cl_platform_id[] platforms = new cl_platform_id[numPlatformsArray[0]];
            clGetPlatformIDs(platforms.length, platforms, null);
            cl_platform_id platform = platforms[0];

            cl_device_id[] devices = new cl_device_id[1];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 1, devices, null);
            cl_device_id device = devices[0];

            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
            context = clCreateContext(contextProperties, 1, devices, null, null, null);
            
            cl_queue_properties queueProperties = new cl_queue_properties();
            commandQueue = clCreateCommandQueueWithProperties(context, device, queueProperties, null);

            program = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SOURCE}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            
            matMulKernel = clCreateKernel(program, "matrixMultiply", null);
            transposeKernel = clCreateKernel(program, "transpose", null);
            normalizeRowKernel = clCreateKernel(program, "normalizeRow", null);
            gaussJordanKernel = clCreateKernel(program, "gaussJordan", null);
            vecAddKernel = clCreateKernel(program, "vec_add", null);
            vecSubKernel = clCreateKernel(program, "vec_sub", null);
            vecScaleKernel = clCreateKernel(program, "vec_scale", null);
            vecDotPartialKernel = clCreateKernel(program, "vec_dot_partial", null);
            gaussElimPhase1Kernel = clCreateKernel(program, "gaussElimPhase1", null);

            initialized = true;
            logger.info("Native OpenCL Dense Backend initialized successfully.");
        } catch (org.jocl.CLException e) {
            initialized = false;
            String msg = e.getMessage() != null ? e.getMessage() : "Unknown CLException";
            if (msg.contains("CL_BUILD_PROGRAM_FAILURE")) {
                logger.warn("OpenCL program build failure (likely no fp64 support on this device).");
            } else {
                logger.warn("OpenCL initialization failed (JOCL): {}", msg);
            }
        } catch (UnsatisfiedLinkError e) {
            initialized = false;
            logger.warn("JOCL native library not found or could not be loaded: {}", e.getMessage());
        } catch (Throwable t) {
            initialized = false;
            logger.warn("Native OpenCL Backend initialization failed: {} - {}", t.getClass().getSimpleName(), t.getMessage());
        }
    }

    @Override public boolean isAvailable() { if (!initAttempted) init(); return initialized; }
    @Override public boolean isLoaded() { return initialized; }
    @Override public String getName() { return "Native OpenCL Dense Backend"; }
    @Override public int getPriority() { return 105; }
    @Override public boolean isCompatible(Ring<?> ring) { return ring instanceof Reals; }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        init();
        if (!initialized) return fallback().transpose(a);

        int rows = a.rows();
        int cols = a.cols();
        double[] srcData = toDoubleArray(a);
        double[] dstData = new double[rows * cols];

        try {
            Pointer pSrc = Pointer.to(srcData);
            Pointer pDst = Pointer.to(dstData);

            cl_mem memA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * rows * cols, pSrc, null);
            cl_mem memB = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * rows * cols, null, null);

            clSetKernelArg(transposeKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(transposeKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(transposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(transposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{cols}));

            long[] globalWorkSize = new long[]{cols, rows};
            clEnqueueNDRangeKernel(commandQueue, transposeKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memB, CL_TRUE, 0, rows * cols * Sizeof.cl_double, pDst, 0, null, null);

            clReleaseMemObject(memA);
            clReleaseMemObject(memB);

            return fromDoubleArray(dstData, cols, rows);
        } catch (Exception e) {
            logger.error("OpenCL Transpose failed, falling back", e);
            return fallback().transpose(a);
        }
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");

        logger.debug("Entering OpenCL multiply: [{}x{}] * [{}x{}]", a.rows(), a.cols(), b.rows(), b.cols());
        long start = System.nanoTime();
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        double[] h_A = toDoubleArray(a);
        double[] h_B = toDoubleArray(b);
        double[] h_C = new double[m * n];

        cl_mem memA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * k, Pointer.to(h_A), null);
        cl_mem memB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * k * n, Pointer.to(h_B), null);
        cl_mem memC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * m * n, null, null);

        try {
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));

            long[] globalWorkSize = new long[]{n, m};
            clEnqueueNDRangeKernel(commandQueue, matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(h_C), 0, null, null);
            
            Matrix<Real> result = fromDoubleArray(h_C, m, n);
            org.episteme.core.util.PerformanceLogger.log("MatrixMultiply", "Dense/OpenCL", System.nanoTime() - start);
            return result;
        } finally {
            clReleaseMemObject(memA);
            clReleaseMemObject(memB);
            clReleaseMemObject(memC);
        }
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

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        int n = a.rows();
        if (n != a.cols() || b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");

        double[] h_A = toDoubleArray(a);
        double[] h_B = toDoubleVec(b);

        cl_mem memA = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null);
        try {
            for (int k = 0; k < n; k++) {
                // Pivoting on CPU for simplicity (avoiding complex GPU sync)
                clEnqueueReadBuffer(commandQueue, memA, CL_TRUE, (long)k * n * Sizeof.cl_double, (long)(n - k) * Sizeof.cl_double, Pointer.to(h_A).withByteOffset((long)k * n * Sizeof.cl_double), 0, null, null);
                
                int max = k;
                for (int i = k + 1; i < n; i++) {
                    if (Math.abs(h_A[i * n + k]) > Math.abs(h_A[max * n + k])) max = i;
                }
                if (k != max) {
                    // Swap on CPU and write back or use a swap kernel. CPU swap for small n is fine.
                    for (int j = k; j < n; j++) { double t = h_A[k * n + j]; h_A[k * n + j] = h_A[max * n + j]; h_A[max * n + j] = t; }
                    double tb = h_B[k]; h_B[k] = h_B[max]; h_B[max] = tb;
                    clEnqueueWriteBuffer(commandQueue, memA, CL_TRUE, (long)k * n * Sizeof.cl_double, (long)Sizeof.cl_double * (n - k), Pointer.to(h_A).withByteOffset((long)k * n * Sizeof.cl_double), 0, null, null);
                    clEnqueueWriteBuffer(commandQueue, memA, CL_TRUE, (long)max * n * Sizeof.cl_double, (long)Sizeof.cl_double * (n - k), Pointer.to(h_A).withByteOffset((long)max * n * Sizeof.cl_double), 0, null, null);
                }

                double pivot = h_A[k * n + k];
                if (Math.abs(pivot) < 1e-15) throw new ArithmeticException("Singular matrix");

                // Vectorized elimination on GPU
                clSetKernelArg(gaussElimPhase1Kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(gaussElimPhase1Kernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(gaussElimPhase1Kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                
                if (n - k - 1 > 0) {
                    clEnqueueNDRangeKernel(commandQueue, gaussElimPhase1Kernel, 1, null, new long[]{n - k - 1}, null, 0, null, null);
                }
                
                // Update B on CPU (small enough)
                for (int i = k + 1; i < n; i++) {
                    h_B[i] -= (h_A[i * n + k] / pivot) * h_B[k];
                }
            }
            
            // Back substitution on CPU
            clEnqueueReadBuffer(commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), 0, null, null);
            double[] x = new double[n];
            for (int i = n - 1; i >= 0; i--) {
                double sum = 0;
                for (int j = i + 1; j < n; j++) sum += h_A[i * n + j] * x[j];
                x[i] = (h_B[i] - sum) / h_A[i * n + i];
            }
            return toRealVector(x);
        } finally {
            clReleaseMemObject(memA);
        }
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        // Identity matrix as B, solve AX = I
        double[] h_A = toDoubleArray(a);
        double[] inv = new double[n * n];
        for (int i = 0; i < n; i++) inv[i * n + i] = 1.0;

        cl_mem memA = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null);
        try {
            for (int k = 0; k < n; k++) {
                clEnqueueReadBuffer(commandQueue, memA, CL_TRUE, (long)k * n * Sizeof.cl_double, (long)(n - k) * Sizeof.cl_double, Pointer.to(h_A).withByteOffset((long)k * n * Sizeof.cl_double), 0, null, null);
                int max = k;
                for (int i = k + 1; i < n; i++) {
                    if (Math.abs(h_A[i * n + k]) > Math.abs(h_A[max * n + k])) max = i;
                }
                if (k != max) {
                    for (int j = 0; j < n; j++) { double t = h_A[k * n + j]; h_A[k * n + j] = h_A[max * n + j]; h_A[max * n + j] = t; }
                    for (int j = 0; j < n; j++) { double t = inv[k * n + j]; inv[k * n + j] = inv[max * n + j]; inv[max * n + j] = t; }
                    clEnqueueWriteBuffer(commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), 0, null, null);
                }
                // 1. Normalize pivot row
                clSetKernelArg(normalizeRowKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(normalizeRowKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(normalizeRowKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n})); // cols
                clSetKernelArg(normalizeRowKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(commandQueue, normalizeRowKernel, 1, null, new long[]{n - k}, null, 0, null, null);

                // 2. Eliminate other rows (Gauss-Jordan)
                clSetKernelArg(gaussJordanKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(gaussJordanKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(gaussJordanKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n})); // cols
                clSetKernelArg(gaussJordanKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(commandQueue, gaussJordanKernel, 1, null, new long[]{n}, null, 0, null, null);

                // For now, we still handle the inverse matrix updates in Java to keep it simple,
                // but A is being reduced to I on the GPU.
                // TODO: Fully move J's updates to GPU
                clEnqueueReadBuffer(commandQueue, memA, CL_TRUE, (long)k * n * Sizeof.cl_double, (long)n * Sizeof.cl_double, Pointer.to(h_A).withByteOffset((long)k * n * Sizeof.cl_double), 0, null, null);
                double pivotVal = h_A[k * n + k];
                
                for (int i = 0; i < n; i++) {
                    if (i != k) {
                        clEnqueueReadBuffer(commandQueue, memA, CL_TRUE, (long)i * n * Sizeof.cl_double, (long)Sizeof.cl_double, Pointer.to(h_A).withByteOffset((long)i * n * Sizeof.cl_double), 0, null, null);
                        double factor = h_A[i * n + k];
                        for (int j = 0; j < n; j++) inv[i * n + j] -= factor * inv[k * n + j];
                    }
                }
                for (int j = 0; j < n; j++) inv[k * n + j] /= pivotVal;
            }
            return fromDoubleArray(inv, n, n);
        } finally { clReleaseMemObject(memA); }
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        int n = a.rows();
        double[] h_A = toDoubleArray(a);
        double det = 1.0;
        cl_mem memA = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null);
        try {
            for (int k = 0; k < n; k++) {
                clEnqueueReadBuffer(commandQueue, memA, CL_TRUE, (long)k * n * Sizeof.cl_double, (long)(n - k) * Sizeof.cl_double, Pointer.to(h_A).withByteOffset((long)k * n * Sizeof.cl_double), 0, null, null);
                int max = k;
                for (int i = k + 1; i < n; i++) if (Math.abs(h_A[i * n + k]) > Math.abs(h_A[max * n + k])) max = i;
                if (k != max) {
                    for (int j = k; j < n; j++) { double t = h_A[k * n + j]; h_A[k * n + j] = h_A[max * n + j]; h_A[max * n + j] = t; }
                    det = -det;
                    clEnqueueWriteBuffer(commandQueue, memA, CL_TRUE, (long)k * n * Sizeof.cl_double, (long)Sizeof.cl_double * (n - k), Pointer.to(h_A).withByteOffset((long)k * n * Sizeof.cl_double), 0, null, null);
                    clEnqueueWriteBuffer(commandQueue, memA, CL_TRUE, (long)max * n * Sizeof.cl_double, (long)Sizeof.cl_double * (n - k), Pointer.to(h_A).withByteOffset((long)max * n * Sizeof.cl_double), 0, null, null);
                }
                det *= h_A[k * n + k];
                clSetKernelArg(gaussJordanKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(gaussJordanKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(gaussJordanKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(gaussJordanKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(commandQueue, gaussJordanKernel, 1, null, new long[]{n}, null, 0, null, null);
            }
            return Real.of(det);
        } finally { clReleaseMemObject(memA); }
    }

    @Override public String getNativeLibraryName() { return "opencl"; }
    @Override public DeviceInfo[] getDevices() { return new DeviceInfo[0]; }
    @Override public void selectDevice(int deviceId) { }
    @Override public long allocateGPUMemory(long size) { return 0; }
    @Override public void copyToGPU(long handle, DoubleBuffer buffer, long count) { }
    @Override public void copyFromGPU(long handle, DoubleBuffer buffer, long count) { }
    @Override public void freeGPUMemory(long handle) { }
    @Override public void synchronize() { if (commandQueue != null) clFinish(commandQueue); }
    @Override public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) { }

    /** Matrix add via element-wise OpenCL. */
    @Override public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        return elementWiseVec(toDoubleArray(a), toDoubleArray(b), vecAddKernel, a.rows(), a.cols());
    }
    @Override public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        return elementWiseVec(toDoubleArray(a), toDoubleArray(b), vecSubKernel, a.rows(), a.cols());
    }
    @Override public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        return fromDoubleArray(scaleVec(toDoubleArray(a), scalar.doubleValue()), a.rows(), a.cols());
    }
    @Override public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        // Mv = A * (b as column matrix) — reuse GPU matmul kernel
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        double[] av = toDoubleArray(a), bv = toDoubleVec(b);
        double[] result = matVecMul(av, bv, a.rows(), a.cols());
        return toRealVector(result);
    }
    @Override public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        return toRealVector(vecOp(toDoubleVec(a), toDoubleVec(b), vecAddKernel));
    }
    @Override public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        return toRealVector(vecOp(toDoubleVec(a), toDoubleVec(b), vecSubKernel));
    }
    @Override public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        return toRealVector(scaleVec(toDoubleVec(vector), scalar.doubleValue()));
    }
    @Override public Real dot(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        double[] products = vecOp(toDoubleVec(a), toDoubleVec(b), vecDotPartialKernel);
        double sum = 0; for (double v : products) sum += v;
        return Real.of(sum);
    }
    @Override public Real norm(Vector<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");
        double[] av = toDoubleVec(a);
        double[] sq = scaleVecElementWise(av, av); // a[i]*a[i] via dot kernel
        double sum = 0; for (double v : sq) sum += v;
        return Real.of(Math.sqrt(sum));
    }
    @Override public double score(OperationContext context) {
        if (!isAvailable()) return -1.0;
        double base = getPriority();

        // Check for unsupported operations
        if (context.hasHint(OperationContext.Hint.MAT_INV) ||
            context.hasHint(OperationContext.Hint.MAT_DET) ||
            context.hasHint(OperationContext.Hint.MAT_SOLVE)) {
            base += 5.0;
        }

        if (context.getDataSize() < 256) base -= 200;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 50;
        if (context.hasHint(OperationContext.Hint.MAT_MUL)) base += 20;
        return base;
    }

    // ---- helpers ----

    private Matrix<Real> elementWiseVec(double[] a, double[] b, cl_kernel k, int rows, int cols) {
        return fromDoubleArray(vecOp(a, b, k), rows, cols);
    }

    private double[] vecOp(double[] a, double[] b, cl_kernel k) {
        int n = a.length;
        double[] result = new double[n];
        cl_mem mA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(a), null);
        cl_mem mB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(b), null);
        cl_mem mC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null);
        try {
            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mB));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(commandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(commandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
        } finally { clReleaseMemObject(mA); clReleaseMemObject(mB); clReleaseMemObject(mC); }
        return result;
    }

    private double[] scaleVec(double[] a, double s) {
        int n = a.length;
        double[] result = new double[n];
        cl_mem mA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(a), null);
        cl_mem mC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null);
        try {
            clSetKernelArg(vecScaleKernel, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(vecScaleKernel, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
            clSetKernelArg(vecScaleKernel, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(vecScaleKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(commandQueue, vecScaleKernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(commandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
        } finally { clReleaseMemObject(mA); clReleaseMemObject(mC); }
        return result;
    }

    private double[] scaleVecElementWise(double[] a, double[] b) {
        return vecOp(a, b, vecDotPartialKernel);
    }

    private double[] toDoubleVec(Vector<Real> v) {
        double[] d = new double[v.dimension()];
        for (int i = 0; i < d.length; i++) d[i] = v.get(i).doubleValue();
        return d;
    }

    private Vector<Real> toRealVector(double[] d) {
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(d);
    }

    private double[] matVecMul(double[] a, double[] b, int rows, int cols) {
        // Mv = treat b as nx1 matrix and use GPU matmul
        double[] bMat = b; // already a flat column
        double[] c = new double[rows];
        cl_mem mA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * rows * cols, Pointer.to(a), null);
        cl_mem mB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * cols, Pointer.to(bMat), null);
        cl_mem mC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * rows, null, null);
        try {
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(mB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{1}));
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{cols}));
            long[] globalWorkSize = new long[]{1, rows};
            clEnqueueNDRangeKernel(commandQueue, matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(commandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * rows, Pointer.to(c), 0, null, null);
        } finally { clReleaseMemObject(mA); clReleaseMemObject(mB); clReleaseMemObject(mC); }
        return c;
    }

    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
    }
}
