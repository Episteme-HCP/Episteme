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

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;

import java.util.List;
import java.util.ArrayList;


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
    
    private static class OpenCLHolder {
        private static cl_context context;
        private static cl_command_queue commandQueue;
        // Kernels
        private static cl_kernel transposeKernel;
        private static cl_kernel vecAddKernel;
        private static cl_kernel vecSubKernel;
        private static cl_kernel vecScaleKernel;
        private static cl_kernel vecDotPartialKernel;
        private static cl_kernel matMulKernel;
        private static cl_kernel normalizeRowKernel;
        private static cl_kernel gaussJordanKernel;
        private static cl_kernel normalizeRowInvKernel;
        private static cl_kernel gaussJordanInvKernel;
        private static cl_kernel gaussElimPhase1Kernel;
        private static cl_kernel gaussElimPhase1WithBKernel;
        private static cl_kernel swapRowsKernel;
        private static cl_kernel luDecomposeStepKernel;
        private static cl_kernel choleskyDecomposeStepKernel;
        private static cl_kernel qrHouseholderApplyKernel;
        private static cl_kernel complexMatMulKernel;
        private static cl_kernel complexVecAddKernel;
        private static cl_kernel complexVecSubKernel;
        private static cl_kernel complexVecScaleKernel;
        private static cl_kernel complexVecDotPartialKernel;
        private static cl_kernel vecDotKernel;
        private static cl_kernel vecNormKernel;
        private static cl_kernel hestenesDotKernel;
        private static cl_kernel hestenesApplyKernel;
        private static cl_program program;
        private static cl_context_properties contextProperties;
    }

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
        "__kernel void saxpy(__global const double *x, __global double *y, const double alpha, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) y[i] = alpha * x[i] + y[i];\n" +
        "}\n\n" +
        "__kernel void vec_dot(__global const double *a, __global const double *b, __global double *result, const int n) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row == 0) {\n" +
        "        double sum = 0;\n" +
        "        for (int i = 0; i < n; i++) sum += a[i] * b[i];\n" +
        "        result[0] = sum;\n" +
        "    }\n" +
        "}\n\n" +
        "__kernel void vec_norm(__global const double *a, __global double *result, const int n) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row == 0) {\n" +
        "        double sum = 0;\n" +
        "        for (int i = 0; i < n; i++) sum += a[i] * a[i];\n" +
        "        result[0] = sqrt(sum);\n" +
        "    }\n" +
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
        "__kernel void matrixMultiplyFloat(__global const float *a, __global const float *b, __global float *c, const int m, const int n, const int k) {\n" +
        "    int row = get_global_id(1); int col = get_global_id(0);\n" +
        "    if (row < m && col < n) {\n" +
        "        float sum = 0.0f;\n" +
        "        for (int i = 0; i < k; i++) sum += a[row*k+i] * b[i*n+col];\n" +
        "        c[row*n+col] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void vec_add(__global const double *a, __global const double *b, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] + b[i];\n" +
        "}\n" +
        "__kernel void transposeFloat(__global const float *a, __global float *b, const int rows, const int cols) {\n" +
        "    int r = get_global_id(1); int c = get_global_id(0);\n" +
        "    if (r < rows && c < cols) b[c * rows + r] = a[r * cols + c];\n" +
        "}\n" +
        "__kernel void normalizeRow(__global double *a, const int rows, const int cols, const int k) {\n" +
        "    int j = get_global_id(0) + k + 1;\n" +
        "    double pivot = a[k * cols + k];\n" +
        "    if (j < cols) a[k * cols + j] /= pivot;\n" +
        "    if (j == k + 1) a[k * cols + k] = 1.0;\n" +
        "}\n" +
        "__kernel void gaussJordan(__global double *a, const int rows, const int cols, const int k) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < rows && i != k) {\n" +
        "        double pivot = a[k * cols + k];\n" +
        "        if (fabs(pivot) < 1e-15) return;\n" +
        "        double factor = a[i * cols + k] / pivot;\n" +
        "        for (int j = k + 1; j < cols; j++) a[i * cols + j] -= factor * a[k * cols + j];\n" +
        "        a[i * cols + k] = 0.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void normalizeRowInv(__global double *a, __global double *inv, const int n, const int k) {\n" +
        "    int j = get_global_id(0);\n" +
        "    if (j < n) {\n" +
        "        double pivot = a[k * n + k];\n" +
        "        if (j > k) a[k * n + j] /= pivot;\n" +
        "        inv[k * n + j] /= pivot;\n" +
        "        if (j == k) a[k * n + k] = 1.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void gaussJordanInv(__global double *a, __global double *inv, const int n, const int k) {\n" +
        "    int j = get_global_id(0); int i = get_global_id(1);\n" +
        "    if (i < n && j < n && i != k) {\n" +
        "        double factor = a[i * n + k];\n" +
        "        if (j > k) a[i * n + j] -= factor * a[k * n + j];\n" +
        "        inv[i * n + j] -= factor * inv[k * n + j];\n" +
        "        if (j == k) a[i * n + k] = 0.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void swapRows(__global double *a, const int n, const int r1, const int r2) {\n" +
        "    int j = get_global_id(0);\n" +
        "    if (j < n) {\n" +
        "        double temp = a[r1 * n + j];\n" +
        "        a[r1 * n + j] = a[r2 * n + j];\n" +
        "        a[r2 * n + j] = temp;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void gaussElimPhase1WithB(__global double *a, __global double *b, const int n, const int k) {\n" +
        "    int i = get_global_id(0) + k + 1;\n" +
        "    if (i < n) {\n" +
        "        double factor = a[i*n + k] / a[k*n + k];\n" +
        "        for (int j = k + 1; j < n; j++) a[i*n + j] -= factor * a[k*n + j];\n" +
        "        b[i] -= factor * b[k];\n" +
        "        a[i*n + k] = 0.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void mat_copy(__global const double *src, __global double *dst, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) dst[i] = src[i];\n" +
        "}\n" +
        "__kernel void lu_decompose_step(__global double *a, const int n, const int k) {\n" +
        "    int i = get_global_id(1) + k + 1;\n" +
        "    int j = get_global_id(0) + k + 1;\n" +
        "    if (i < n && j < n) {\n" +
        "        if (j == k + 1) a[i * n + k] /= a[k * n + k];\n" +
        "        a[i * n + j] -= a[i * n + k] * a[k * n + j];\n" +
        "    }\n" +
        "}\n" +
        "__kernel void cholesky_decompose_step(__global double *a, const int n, const int k) {\n" +
        "    int i = get_global_id(0) + k + 1;\n" +
        "    if (i < n) {\n" +
        "        double sum = 0.0;\n" +
        "        for (int j = 0; j < k; j++) sum += a[i * n + j] * a[k * n + j];\n" +
        "        a[i * n + k] = (a[i * n + k] - sum) / a[k * n + k];\n" +
        "    }\n" +
        "}\n" +
        "__kernel void qr_householder_apply(__global double *a, const int rows, const int cols, const int k, __global const double *v) {\n" +
        "    int j = get_global_id(0) + k;\n" +
        "    int i = get_global_id(1) + k;\n" +
        "    if (i < rows && j < cols) {\n" +
        "        double dot = 0.0;\n" +
        "        a[i * cols + j] -= 2.0 * v[i] * dot;\n" +
        "    }\n" +
        "}\n" +
        "typedef struct { double r; double i; } double2_custom;\n" +
        "__kernel void complexMatrixMultiply(__global const double2_custom *a, __global const double2_custom *b, __global double2_custom *c, const int m, const int n, const int k) {\n" +
        "    int row = get_global_id(1); int col = get_global_id(0);\n" +
        "    if (row < m && col < n) {\n" +
        "        double2_custom sum = {0.0, 0.0};\n" +
        "        for (int i = 0; i < k; i++) {\n" +
        "            double2_custom av = a[row*k+i];\n" +
        "            double2_custom bv = b[i*n+col];\n" +
        "            sum.r += av.r * bv.r - av.i * bv.i;\n" +
        "            sum.i += av.r * bv.i + av.i * bv.r;\n" +
        "        }\n" +
        "        c[row*n+col] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void complex_vec_add(__global const double2_custom *a, __global const double2_custom *b, __global double2_custom *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) { c[i].r = a[i].r + b[i].r; c[i].i = a[i].i + b[i].i; }\n" +
        "}\n" +
        "__kernel void complex_vec_sub(__global const double2_custom *a, __global const double2_custom *b, __global double2_custom *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) { c[i].r = a[i].r - b[i].r; c[i].i = a[i].i - b[i].i; }\n" +
        "}\n" +
        "__kernel void complex_vec_scale(__global const double2_custom *a, const double2_custom s, __global double2_custom *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) {\n" +
        "        double2_custom av = a[i];\n" +
        "        c[i].r = av.r * s.r - av.i * s.i;\n" +
        "        c[i].i = av.r * s.i + av.i * s.r;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void complexVecDotPartial(__global const double2_custom *a, __global const double2_custom *b, __global double2_custom *out, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) {\n" +
        "        double2_custom av = a[i];\n" +
        "        double2_custom bv = b[i];\n" +
        "        out[i].r = av.r * bv.r + av.i * bv.i;\n" +
        "        out[i].i = av.i * bv.r - av.r * bv.i;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void hestenes_jacobi_dot(__global const double *a, int m, int n, int i_col, int j_col, __global double *out_dot) {\n" +
        "    if (get_global_id(0) == 0) {\n" +
        "        double s_ii = 0; double s_jj = 0; double s_ij = 0;\n" +
        "        for (int k = 0; k < m; k++) {\n" +
        "            double val_i = a[k * n + i_col]; double val_j = a[k * n + j_col];\n" +
        "            s_ii += val_i * val_i; s_jj += val_j * val_j; s_ij += val_i * val_j;\n" +
        "        }\n" +
        "        out_dot[0] = s_ii; out_dot[1] = s_jj; out_dot[2] = s_ij;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void hestenes_jacobi_apply(__global double *a, __global double *v, int m, int n, int i_col, int j_col, double cos_v, double sin_v) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row < m) {\n" +
        "        double ai = a[row * n + i_col]; double aj = a[row * n + j_col];\n" +
        "        a[row * n + i_col] = cos_v * ai + sin_v * aj;\n" +
        "        a[row * n + j_col] = -sin_v * ai + cos_v * aj;\n" +
        "    }\n" +
        "    if (row < n) {\n" +
        "        double vi = v[row * n + i_col]; double vj = v[row * n + j_col];\n" +
        "        v[row * n + i_col] = cos_v * vi + sin_v * vj;\n" +
        "        v[row * n + j_col] = -sin_v * vi + cos_v * vj;\n" +
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
            
            if (!verifyExtensions(device)) {
                logger.warn("OpenCL device does not support cl_khr_fp64 extension. Backend will be disabled.");
                return;
            }

            OpenCLHolder.contextProperties = new cl_context_properties();
            OpenCLHolder.contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
            OpenCLHolder.context = clCreateContext(OpenCLHolder.contextProperties, 1, devices, null, null, null);
            
            cl_queue_properties queueProperties = new cl_queue_properties();
            OpenCLHolder.commandQueue = clCreateCommandQueueWithProperties(OpenCLHolder.context, device, queueProperties, null);

            OpenCLHolder.program = clCreateProgramWithSource(OpenCLHolder.context, 1, new String[]{KERNEL_SOURCE}, null, null);
            clBuildProgram(OpenCLHolder.program, 0, null, null, null, null);
            
            OpenCLHolder.matMulKernel = clCreateKernel(OpenCLHolder.program, "matrixMultiply", null);
            OpenCLHolder.vecAddKernel = clCreateKernel(OpenCLHolder.program, "vec_add", null);
            OpenCLHolder.vecSubKernel = clCreateKernel(OpenCLHolder.program, "vec_sub", null);
            OpenCLHolder.vecScaleKernel = clCreateKernel(OpenCLHolder.program, "vec_scale", null);
            OpenCLHolder.vecDotKernel = clCreateKernel(OpenCLHolder.program, "vec_dot", null);
            OpenCLHolder.vecNormKernel = clCreateKernel(OpenCLHolder.program, "vec_norm", null);
            OpenCLHolder.transposeKernel = clCreateKernel(OpenCLHolder.program, "transpose", null);
            OpenCLHolder.normalizeRowKernel = clCreateKernel(OpenCLHolder.program, "normalizeRow", null);
            OpenCLHolder.gaussJordanKernel = clCreateKernel(OpenCLHolder.program, "gaussJordan", null);
            OpenCLHolder.normalizeRowInvKernel = clCreateKernel(OpenCLHolder.program, "normalizeRowInv", null);
            OpenCLHolder.gaussJordanInvKernel = clCreateKernel(OpenCLHolder.program, "gaussJordanInv", null);
            OpenCLHolder.gaussElimPhase1Kernel = clCreateKernel(OpenCLHolder.program, "gaussElimPhase1", null);
            OpenCLHolder.gaussElimPhase1WithBKernel = clCreateKernel(OpenCLHolder.program, "gaussElimPhase1WithB", null);
            OpenCLHolder.swapRowsKernel = clCreateKernel(OpenCLHolder.program, "swapRows", null);
            OpenCLHolder.luDecomposeStepKernel = clCreateKernel(OpenCLHolder.program, "lu_decompose_step", null);
            OpenCLHolder.choleskyDecomposeStepKernel = clCreateKernel(OpenCLHolder.program, "cholesky_decompose_step", null);
            OpenCLHolder.qrHouseholderApplyKernel = clCreateKernel(OpenCLHolder.program, "qr_householder_apply", null);
            OpenCLHolder.complexMatMulKernel = clCreateKernel(OpenCLHolder.program, "complexMatrixMultiply", null);
            OpenCLHolder.complexVecAddKernel = clCreateKernel(OpenCLHolder.program, "complex_vec_add", null);
            OpenCLHolder.complexVecSubKernel = clCreateKernel(OpenCLHolder.program, "complex_vec_sub", null);
            OpenCLHolder.complexVecScaleKernel = clCreateKernel(OpenCLHolder.program, "complex_vec_scale", null);
            OpenCLHolder.complexVecDotPartialKernel = clCreateKernel(OpenCLHolder.program, "complexVecDotPartial", null);
            OpenCLHolder.hestenesDotKernel = clCreateKernel(OpenCLHolder.program, "hestenes_jacobi_dot", null);
            OpenCLHolder.hestenesApplyKernel = clCreateKernel(OpenCLHolder.program, "hestenes_jacobi_apply", null);


            initialized = true;
            logger.info("Native OpenCL Dense Backend initialized successfully.");
        } catch (org.jocl.CLException e) {
            initialized = false;
            String msg = e.getMessage() != null ? e.getMessage() : "Unknown CLException";
            if (msg.contains("CL_BUILD_PROGRAM_FAILURE")) {
                logger.warn("OpenCL OpenCLHolder.program build failure (likely no fp64 support on this device).");
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
    @Override
    public String getName() {
        return "Native OpenCL Dense Linear Algebra Backend";
    }
    @Override public int getPriority() { return 105; }
    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) return true;
        return ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        init();
        if (!initialized) throw new UnsupportedOperationException(getName() + ": OpenCL not initialized, cannot transpose()");

        int rows = a.rows();
        int cols = a.cols();
        double[] srcData = toDoubleArray(a);
        double[] dstData = new double[rows * cols];

        try (ResourceTracker tracker = new ResourceTracker()) {
            Pointer pSrc = Pointer.to(srcData);
            Pointer pDst = Pointer.to(dstData);

            cl_mem memA = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * rows * cols, pSrc, null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * rows * cols, null, null), CL::clReleaseMemObject);

            clSetKernelArg(OpenCLHolder.transposeKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(OpenCLHolder.transposeKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(OpenCLHolder.transposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(OpenCLHolder.transposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{cols}));

            long[] globalWorkSize = new long[]{cols, rows};
            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.transposeKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memB, CL_TRUE, 0, (long)rows * cols * Sizeof.cl_double, pDst, 0, null, null);

            return fromDoubleArray(dstData, cols, rows, a);
        } catch (Exception e) {
            throw new UnsupportedOperationException(getName() + ": OpenCL transpose failed", e);
        }
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for multiply()");

        if (isComplex(a)) return multiplyComplex(a, b);

        logger.debug("Entering OpenCL multiply: [{}x{}] * [{}x{}]", a.rows(), a.cols(), b.rows(), b.cols());
        long start = System.nanoTime();
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        double[] h_A = toDoubleArray(a);
        double[] h_B = toDoubleArray(b);
        double[] h_C = new double[m * n];

        try (ResourceTracker tracker = new ResourceTracker()) {
            logger.debug("Creating memA...");
            cl_mem memA = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * k, Pointer.to(h_A), null), CL::clReleaseMemObject);
            logger.debug("Creating memB...");
            cl_mem memB = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * k * n, Pointer.to(h_B), null), CL::clReleaseMemObject);
            logger.debug("Creating memC...");
            cl_mem memC = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * m * n, null, null), CL::clReleaseMemObject);

            logger.debug("Setting kernel args...");
            clSetKernelArg(OpenCLHolder.matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(OpenCLHolder.matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(OpenCLHolder.matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(OpenCLHolder.matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(OpenCLHolder.matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(OpenCLHolder.matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));

            long[] globalWorkSize = new long[]{n, m};
            logger.debug("Enqueuing NDRangeKernel...");
            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            
            logger.debug("Enqueuing ReadBuffer...");
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(h_C), 0, null, null);
            
            logger.debug("Creating result matrix from array...");
            Matrix<Real> result = fromDoubleArray(h_C, m, n, a);
            org.episteme.core.util.PerformanceLogger.log("MatrixMultiply", "Dense/OpenCL", System.nanoTime() - start);
            logger.debug("OpenCL multiply finished successfully.");
            return result;
        } catch (Exception e) {
            logger.error("OpenCL multiply operation failed: {}", e.getMessage(), e);
            throw new RuntimeException("OpenCL Multiply failed", e);
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

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols, Matrix<Real> reference) {
        Real[] reals = new Real[data.length];
        for(int i=0; i<data.length; i++) reals[i] = Real.of(data[i]);
        return new DenseMatrix<Real>(reals, rows, cols, (Ring<Real>) reference.getScalarRing());
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for solve()");
        int m = a.rows();
        int n = a.cols();
        if (m == n) {
            if (b.dimension() != n) throw new IllegalArgumentException("Dimension mismatch");
        } else {
            // Rectangular solve (Least Squares) via SVD: A+ = V S+ U^T
            logger.debug("Rectangular solve via SVD on GPU: [{}x{}]", m, n);
            SVDResult<Real> SVD = svd(a);
            int k = SVD.S().dimension();
            
            // S+ is diagonal(1/s_i)
            double[] sInvData = new double[k];
            for (int i = 0; i < k; i++) {
                double sVal = SVD.S().get(i).doubleValue();
                sInvData[i] = (sVal > 1e-12) ? 1.0 / sVal : 0.0;
            }
            
            // x = V * S+ * U^T * b
            Vector<Real> utb = SVD.U().transpose().multiply(b);
            double[] utbData = toDoubleVec(utb);
            for (int i = 0; i < k; i++) utbData[i] *= sInvData[i];
            return SVD.V().multiply(org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(utbData));
        }
        
        double[] h_A = toDoubleArray(a);
        double[] h_B = toDoubleVec(b);
        double[] pivotCol = new double[n];

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_mem memA = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(h_B), null), CL::clReleaseMemObject);

            for (int k = 0; k < n; k++) {
                // Read back ONLY the pivot column
                for (int i = k; i < n; i++) {
                    clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, (long)(i * n + k) * Sizeof.cl_double, (long)Sizeof.cl_double, Pointer.to(pivotCol).withByteOffset((long)i * Sizeof.cl_double), 0, null, null);
                }
                
                int max = k;
                for (int i = k + 1; i < n; i++) {
                    if (Math.abs(pivotCol[i]) > Math.abs(pivotCol[max])) max = i;
                }

                if (k != max) {
                    // Swap rows on GPU for A
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{max}));
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.swapRowsKernel, 1, null, new long[]{n}, null, 0, null, null);

                    // Swap B elements on CPU
                    double tb = h_B[k]; h_B[k] = h_B[max]; h_B[max] = tb;
                    clEnqueueWriteBuffer(OpenCLHolder.commandQueue, memB, CL_TRUE, 0, (long)n * Sizeof.cl_double, Pointer.to(h_B), 0, null, null);
                }

                double pivot = pivotCol[max];
                if (Math.abs(pivot) < 1e-15) throw new ArithmeticException("Singular matrix");

                // Vectorized elimination on GPU
                clSetKernelArg(OpenCLHolder.gaussElimPhase1WithBKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(OpenCLHolder.gaussElimPhase1WithBKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
                clSetKernelArg(OpenCLHolder.gaussElimPhase1WithBKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(OpenCLHolder.gaussElimPhase1WithBKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                
                if (n - k - 1 > 0) {
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.gaussElimPhase1WithBKernel, 1, null, new long[]{n - k - 1}, null, 0, null, null);
                }
            }
            
            // Back substitution on CPU
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memB, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(h_B), 0, null, null);
            double[] x = new double[n];
            for (int i = n - 1; i >= 0; i--) {
                double sum = 0;
                for (int j = i + 1; j < n; j++) sum += h_A[i * n + j] * x[j];
                x[i] = (h_B[i] - sum) / h_A[i * n + i];
            }
            return toRealVector(x, a);
        }
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for inverse()");
        int m = a.rows();
        int n = a.cols();
        if (m != n) {
             // Rectangular inverse (Pseudo-inverse) via SVD
             logger.debug("Rectangular pseudo-inverse via SVD: [{}x{}]", m, n);
             SVDResult<Real> SVD = svd(a);
             int k = SVD.S().dimension();
             
             // S+ is diagonal(1/s_i)
             double[] sInvData = new double[k];
             for (int i = 0; i < k; i++) {
                 double sVal = SVD.S().get(i).doubleValue();
                 sInvData[i] = (sVal > 1e-12) ? 1.0 / sVal : 0.0;
             }
             
             Matrix<Real> Sinv = org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix.diagonal(sInvData);
             Matrix<Real> vSinv = SVD.V().multiply(Sinv);
             return vSinv.multiply(SVD.U().transpose());
        }
        
        double[] h_A = toDoubleArray(a);
        double[] inv = new double[n * n];
        for (int i = 0; i < n; i++) inv[i * n + i] = 1.0;
        double[] pivotCol = new double[n];

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_mem memA = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null), CL::clReleaseMemObject);
            cl_mem memInv = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(inv), null), CL::clReleaseMemObject);
            
            for (int k = 0; k < n; k++) {
                // Read back only pivot column
                for (int i = k; i < n; i++) {
                    clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, (long)(i * n + k) * Sizeof.cl_double, (long)Sizeof.cl_double, Pointer.to(pivotCol).withByteOffset((long)i * Sizeof.cl_double), 0, null, null);
                }

                int max = k;
                for (int i = k + 1; i < n; i++) {
                    if (Math.abs(pivotCol[i]) > Math.abs(pivotCol[max])) max = i;
                }
                if (k != max) {
                    // Swap rows on GPU for A
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{max}));
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.swapRowsKernel, 1, null, new long[]{n}, null, 0, null, null);

                    // Swap rows on GPU for Inv
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 0, Sizeof.cl_mem, Pointer.to(memInv));
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.swapRowsKernel, 1, null, new long[]{n}, null, 0, null, null);
                }

                // 1. Normalize pivot row on GPU
                clSetKernelArg(OpenCLHolder.normalizeRowInvKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(OpenCLHolder.normalizeRowInvKernel, 1, Sizeof.cl_mem, Pointer.to(memInv));
                clSetKernelArg(OpenCLHolder.normalizeRowInvKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(OpenCLHolder.normalizeRowInvKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.normalizeRowInvKernel, 1, null, new long[]{n}, null, 0, null, null);

                // 2. Eliminate other rows (Gauss-Jordan) on GPU
                clSetKernelArg(OpenCLHolder.gaussJordanInvKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(OpenCLHolder.gaussJordanInvKernel, 1, Sizeof.cl_mem, Pointer.to(memInv));
                clSetKernelArg(OpenCLHolder.gaussJordanInvKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(OpenCLHolder.gaussJordanInvKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.gaussJordanInvKernel, 2, null, new long[]{n, n}, null, 0, null, null);
            }
            
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memInv, CL_TRUE, 0, (long)n * n * Sizeof.cl_double, Pointer.to(inv), 0, null, null);
            return fromDoubleArray(inv, n, n, a);
        }
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        int n = a.rows();
        double[] h_A = toDoubleArray(a);
        double det = 1.0;
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_mem memA = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null), CL::clReleaseMemObject);
            for (int k = 0; k < n; k++) {
                // Read back full matrix for correct pivoting on CPU
                clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)n * n * Sizeof.cl_double, Pointer.to(h_A), 0, null, null);
                int max = k;
                for (int i = k + 1; i < n; i++) if (Math.abs(h_A[i * n + k]) > Math.abs(h_A[max * n + k])) max = i;
                if (k != max) {
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{max}));
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.swapRowsKernel, 1, null, new long[]{n}, null, 0, null, null);
                    det = -det;
                }
                det *= h_A[max * n + k]; // Pivot element (originally at max) in h_A
                clSetKernelArg(OpenCLHolder.gaussElimPhase1Kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(OpenCLHolder.gaussElimPhase1Kernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(OpenCLHolder.gaussElimPhase1Kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                if (n - k - 1 > 0) {
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.gaussElimPhase1Kernel, 1, null, new long[]{n - k - 1}, null, 0, null, null);
                }
                clFinish(OpenCLHolder.commandQueue); // Synchronize loop
            }
            return Real.of(det);
        }
    }

    private static boolean verifyExtensions(cl_device_id device) {
        long[] size = new long[1];
        clGetDeviceInfo(device, CL_DEVICE_EXTENSIONS, 0, null, size);
        byte[] buffer = new byte[(int)size[0]];
        clGetDeviceInfo(device, CL_DEVICE_EXTENSIONS, buffer.length, Pointer.to(buffer), null);
        String extensions = new String(buffer);
        
        boolean hasKhr = extensions.contains("cl_khr_fp64");
        boolean hasAmd = extensions.contains("cl_amd_fp64");
        
        if (hasKhr || hasAmd) {
            logger.info("OpenCL: Device supports double precision ({})", hasKhr ? "cl_khr_fp64" : "cl_amd_fp64");
            return true;
        }
        
        logger.warn("OpenCL: Device does NOT support double precision extensions.");
        return false;
    }

@Override public String getNativeLibraryName() { return "opencl"; }
@Override public DeviceInfo[] getDevices() { return new DeviceInfo[0]; }
    @Override public void selectDevice(int deviceId) { }
    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        if (!isAvailable()) return null;
        return new org.episteme.nativ.technical.backend.gpu.opencl.OpenCLExecutionContext(OpenCLHolder.context, OpenCLHolder.commandQueue);
    }
@Override public long allocateGPUMemory(long size) { return 0; }
@Override public void copyToGPU(long handle, DoubleBuffer buffer, long count) { }
@Override public void copyFromGPU(long handle, DoubleBuffer buffer, long count) { }
@Override public void freeGPUMemory(long handle) { }
@Override public void synchronize() { if (OpenCLHolder.commandQueue != null) clFinish(OpenCLHolder.commandQueue); }
@Override public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) { }

/** Matrix add via element-wise OpenCL. */
@Override public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
    if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for Matrix add()");
    if (isComplex(a)) return elementWiseVecComplex(toComplexDoubleArray(a), toComplexDoubleArray(b), OpenCLHolder.complexVecAddKernel, a.rows(), a.cols(), a);
    return elementWiseVec(toDoubleArray(a), toDoubleArray(b), OpenCLHolder.vecAddKernel, a.rows(), a.cols(), a);
}
@Override public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
    if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for Matrix subtract()");
    if (isComplex(a)) return elementWiseVecComplex(toComplexDoubleArray(a), toComplexDoubleArray(b), OpenCLHolder.complexVecSubKernel, a.rows(), a.cols(), a);
    return elementWiseVec(toDoubleArray(a), toDoubleArray(b), OpenCLHolder.vecSubKernel, a.rows(), a.cols(), a);
}
@Override public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
    if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for scale()");
    if (isComplex(a)) return fromComplexDoubleArray(scaleVecComplex(toComplexDoubleArray(a), scalar), a.rows(), a.cols(), a);
    return fromDoubleArray(scaleVec(toDoubleArray(a), scalar.doubleValue()), a.rows(), a.cols(), a);
}
    @Override public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        // Mv = A * (b as column matrix) — reuse GPU matmul kernel
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for MatVec multiply()");
        double[] av = toDoubleArray(a), bv = toDoubleVec(b);
        double[] result = matVecMul(av, bv, a.rows(), a.cols());
        return toRealVector(result, a);
    }
    @Override public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for Vector add()");
        return toRealVector(vecOp(toDoubleVec(a), toDoubleVec(b), OpenCLHolder.vecAddKernel), a);
    }
    @Override public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for Vector subtract()");
        return toRealVector(vecOp(toDoubleVec(a), toDoubleVec(b), OpenCLHolder.vecSubKernel), a);
    }
    @Override public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for Vector multiply()");
        return toRealVector(scaleVec(toDoubleVec(vector), scalar.doubleValue()), vector);
    }
    @Override public Real dot(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for dot()");
        if (isComplexVec(a)) {
            double[] av = toComplexDoubleVec(a), bv = toComplexDoubleVec(b);
            double[] products = vecOpComplex(av, bv, OpenCLHolder.complexVecDotPartialKernel);
            double re = 0, im = 0;
            for (int i = 0; i < products.length / 2; i++) {
                re += products[i * 2];
                im += products[i * 2 + 1];
            }
            return (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(re, im);
        }
        double[] products = vecOp(toDoubleVec(a), toDoubleVec(b), OpenCLHolder.vecDotPartialKernel);
        double sum = 0; for (double v : products) sum += v;
        return Real.of(sum);
    }
    @Override public Real norm(Vector<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for norm()");
        if (isComplexVec(a)) {
            double[] av = toComplexDoubleVec(a);
            double[] products = vecOpComplex(av, av, OpenCLHolder.complexVecDotPartialKernel);
            double sum = 0;
            for (int i = 0; i < products.length / 2; i++) {
                sum += products[i * 2]; // For norm, we only need real part of a*conj(a) which is |a|^2
            }
            return Real.of(Math.sqrt(sum));
        }
        double[] av = toDoubleVec(a);
        double[] sq = scaleVecElementWise(av, av); // a[i]*a[i] via dot kernel
        double sum = 0; for (double v : sq) sum += v;
        return Real.of(Math.sqrt(sum));
    }
    @Override
    public LUResult<Real> lu(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for LU decomposition");
        int m = a.rows();
        int n = a.cols();
        if (m != n) throw new UnsupportedOperationException("Non-square LU decomposition not yet implemented natively in OpenCL");

        double[] h_A = toDoubleArray(a);
        double[] pData = new double[n];
        for (int i = 0; i < n; i++) pData[i] = i;

        cl_mem memA = null;
        try {
            memA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null);
            
            for (int k = 0; k < n; k++) {
                // Pivoting on CPU
                clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), 0, null, null);
                int max = k;
                for (int i = k + 1; i < n; i++) if (Math.abs(h_A[i * n + k]) > Math.abs(h_A[max * n + k])) max = i;
                
                if (max != k) {
                    // Swap rows on GPU
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                    clSetKernelArg(OpenCLHolder.swapRowsKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{max}));
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.swapRowsKernel, 1, null, new long[]{n}, null, 0, null, null);
                    
                    double tp = pData[k]; pData[k] = pData[max]; pData[max] = tp;
                }

                if (Math.abs(h_A[max * n + k]) < 1e-15) throw new ArithmeticException("Singular matrix in LU decomposition");

                // Decompose step on GPU
                clSetKernelArg(OpenCLHolder.luDecomposeStepKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(OpenCLHolder.luDecomposeStepKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(OpenCLHolder.luDecomposeStepKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                
                int remaining = n - k - 1;
                if (remaining > 0) {
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.luDecomposeStepKernel, 2, null, new long[]{remaining, remaining}, null, 0, null, null);
                }
            }
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), 0, null, null);

            // Extract L and U from h_A
            double[] lData = new double[n * n];
            double[] uData = new double[n * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) {
                        lData[i * n + j] = h_A[i * n + j];
                    } else if (i == j) {
                        lData[i * n + j] = 1.0;
                        uData[i * n + j] = h_A[i * n + j];
                    } else {
                        uData[i * n + j] = h_A[i * n + j];
                    }
                }
            }

            return new LUResult<Real>(
                fromDoubleArray(lData, n, n, a),
                fromDoubleArray(uData, n, n, a),
                toRealVector(pData, a)
            );
        } finally {
            if (memA != null) clReleaseMemObject(memA);
        }
    }
    @Override
    public QRResult<Real> qr(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for QR decomposition");
        int m = a.rows();
        int n = a.cols();
        double[] h_A = toDoubleArray(a);
        double[] qData = new double[m * m];
        for (int i = 0; i < m; i++) qData[i * m + i] = 1.0;
        
        cl_mem memA = null, memQ = null;
        try {
            memA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * n, Pointer.to(h_A), null);
            memQ = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * m, Pointer.to(qData), null);
            
            for (int k = 0; k < Math.min(m, n); k++) {
                // Read sub-column for Householder vector calculation on CPU
                double[] col = new double[m - k];
                for (int i = k; i < m; i++) {
                    clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, (long)(i * n + k) * Sizeof.cl_double, (long)Sizeof.cl_double, Pointer.to(col).withByteOffset((long)(i - k) * Sizeof.cl_double), 0, null, null);
                }

                double norm = 0; for (double v_val : col) norm += v_val * v_val; norm = Math.sqrt(norm);
                double alpha = (col[0] > 0) ? -norm : norm;
                double[] v = new double[m];
                v[k] = col[0] - alpha;
                for (int i = k + 1; i < m; i++) v[i] = col[i - k];
                double vNorm = 0; for (int i = k; i < m; i++) vNorm += v[i] * v[i]; vNorm = Math.sqrt(vNorm);
                if (vNorm > 1e-15) for (int i = k; i < m; i++) v[i] /= vNorm;

                cl_mem memV = null;
                try {
                    memV = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m, Pointer.to(v), null);
                    
                    // Apply Householder to A (becomes R)
                    clSetKernelArg(OpenCLHolder.qrHouseholderApplyKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                    clSetKernelArg(OpenCLHolder.qrHouseholderApplyKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{m}));
                    clSetKernelArg(OpenCLHolder.qrHouseholderApplyKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                    clSetKernelArg(OpenCLHolder.qrHouseholderApplyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                    clSetKernelArg(OpenCLHolder.qrHouseholderApplyKernel, 4, Sizeof.cl_mem, Pointer.to(memV));
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.qrHouseholderApplyKernel, 2, null, new long[]{n - k, m - k}, null, 0, null, null);

                    // Apply Householder to Q
                    clSetKernelArg(OpenCLHolder.qrHouseholderApplyKernel, 0, Sizeof.cl_mem, Pointer.to(memQ));
                    clSetKernelArg(OpenCLHolder.qrHouseholderApplyKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{m}));
                    clSetKernelArg(OpenCLHolder.qrHouseholderApplyKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.qrHouseholderApplyKernel, 2, null, new long[]{m - k, m - k}, null, 0, null, null);
                    clFinish(OpenCLHolder.commandQueue);
                } finally {
                    if (memV != null) clReleaseMemObject(memV);
                }
            }
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)m * n * Sizeof.cl_double, Pointer.to(h_A), 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memQ, CL_TRUE, 0, (long)m * m * Sizeof.cl_double, Pointer.to(qData), 0, null, null);
            
            return new QRResult<Real>(
                fromDoubleArray(qData, m, m, a),
                fromDoubleArray(h_A, m, n, a)
            );
        } finally {
            if (memA != null) clReleaseMemObject(memA);
            if (memQ != null) clReleaseMemObject(memQ);
        }
    }

    @Override
    public SVDResult<Real> svd(Matrix<Real> a) {
        init();
        if (!initialized) throw new UnsupportedOperationException(getName() + ": OpenCL not available for SVD");
        
        int m = a.rows();
        int n = a.cols();
        
        boolean transposed = false;
        Matrix<Real> workA = a;
        if (m < n) {
            transposed = true;
            workA = a.transpose();
            int tmp = m; m = n; n = tmp;
        }

        logger.debug("Entering OpenCL SVD (Hestenes One-Sided Jacobi): [{}x{}]", m, n);
        
        double[] h_A = toDoubleArray(workA);
        double[] h_V = new double[n * n];
        for (int i = 0; i < n; i++) h_V[i * n + i] = 1.0;
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_mem memA = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * n, Pointer.to(h_A), null), CL::clReleaseMemObject);
            cl_mem memV = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_V), null), CL::clReleaseMemObject);
            cl_mem memDots = tracker.track(clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * 3, null, null), CL::clReleaseMemObject);
            
            double[] h_Dots = new double[3];
            double tolerance = 1e-15;
            int maxSweeps = 50;
            
            for (int sweep = 0; sweep < maxSweeps; sweep++) {
                boolean converged = true;
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        // Reset dots buffer
                        clEnqueueWriteBuffer(OpenCLHolder.commandQueue, memDots, CL_TRUE, 0, (long)Sizeof.cl_double * 3, Pointer.to(new double[3]), 0, null, null);
                        
                        // Compute dots: alpha = col_i . col_i, beta = col_j . col_j, gamma = col_i . col_j
                        clSetKernelArg(OpenCLHolder.hestenesDotKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                        clSetKernelArg(OpenCLHolder.hestenesDotKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{m}));
                        clSetKernelArg(OpenCLHolder.hestenesDotKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                        clSetKernelArg(OpenCLHolder.hestenesDotKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{i}));
                        clSetKernelArg(OpenCLHolder.hestenesDotKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{j}));
                        clSetKernelArg(OpenCLHolder.hestenesDotKernel, 5, Sizeof.cl_mem, Pointer.to(memDots));
                        
                        int localSize = 256;
                        int globalSize = ((m + localSize - 1) / localSize) * localSize;
                        clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.hestenesDotKernel, 1, null, new long[]{globalSize}, new long[]{localSize}, 0, null, null);
                        clEnqueueReadBuffer(OpenCLHolder.commandQueue, memDots, CL_TRUE, 0, (long)Sizeof.cl_double * 3, Pointer.to(h_Dots), 0, null, null);
                        
                        double alpha = h_Dots[0]; double beta = h_Dots[1]; double gamma = h_Dots[2];
                        
                        if (Math.abs(gamma) > tolerance * Math.sqrt(alpha * beta)) {
                            converged = false;
                            double zeta = (beta - alpha) / (2.0 * gamma);
                            double t = Math.signum(zeta) / (Math.abs(zeta) + Math.sqrt(1.0 + zeta * zeta));
                            double c = 1.0 / Math.sqrt(1.0 + t * t);
                            double s = c * t;
                            
                            // Apply rotation
                            clSetKernelArg(OpenCLHolder.hestenesApplyKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                            clSetKernelArg(OpenCLHolder.hestenesApplyKernel, 1, Sizeof.cl_mem, Pointer.to(memV));
                            clSetKernelArg(OpenCLHolder.hestenesApplyKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                            clSetKernelArg(OpenCLHolder.hestenesApplyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                            clSetKernelArg(OpenCLHolder.hestenesApplyKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{i}));
                            clSetKernelArg(OpenCLHolder.hestenesApplyKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{j}));
                            clSetKernelArg(OpenCLHolder.hestenesApplyKernel, 6, Sizeof.cl_double, Pointer.to(new double[]{c}));
                            clSetKernelArg(OpenCLHolder.hestenesApplyKernel, 7, Sizeof.cl_double, Pointer.to(new double[]{s}));
                            
                            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.hestenesApplyKernel, 1, null, new long[]{Math.max(m, n)}, null, 0, null, null);
                        }
                    }
                }
                if (converged) break;
            }
            
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(h_A), 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memV, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_V), 0, null, null);
            
            double[] norms = new double[n];
            for (int j = 0; j < n; j++) {
                double nrm = 0;
                for (int i = 0; i < m; i++) nrm += h_A[i * n + j] * h_A[i * n + j];
                norms[j] = Math.sqrt(nrm);
                if (norms[j] > tolerance) {
                    double invNorm = 1.0 / norms[j];
                    for (int i = 0; i < m; i++) h_A[i * n + j] *= invNorm;
                }
            }
            
            Matrix<Real> U = fromDoubleArray(h_A, m, n, a);
            Vector<Real> S = toRealVector(norms, a);
            Matrix<Real> V = fromDoubleArray(h_V, n, n, a);
            
            if (transposed) {
                return new SVDResult<Real>(V, S, U);
            }
            return new SVDResult<Real>(U, S, V);
        } catch (Exception e) {
            logger.error("OpenCL SVD failed: {}", e.getMessage(), e);
            throw new RuntimeException("OpenCL SVD failed", e);
        }
    }

    @Override
    public CholeskyResult<Real> cholesky(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for Cholesky");
        int n = a.rows();
        double[] h_A = toDoubleArray(a);
        cl_mem memA = null;
        
        try {
            memA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null);
            for (int k = 0; k < n; k++) {
                // Calculate diagonal element on CPU
                clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), 0, null, null);
                double sum = 0;
                for (int j = 0; j < k; j++) sum += h_A[k * n + j] * h_A[k * n + j];
                double diag = Math.sqrt(h_A[k * n + k] - sum);
                if (Double.isNaN(diag)) throw new ArithmeticException("Matrix is not positive definite");
                h_A[k * n + k] = diag;
                clEnqueueWriteBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, (long)(k * n + k) * Sizeof.cl_double, (long)Sizeof.cl_double, Pointer.to(new double[]{diag}), 0, null, null);

                // Update column on GPU
                clSetKernelArg(OpenCLHolder.choleskyDecomposeStepKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(OpenCLHolder.choleskyDecomposeStepKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(OpenCLHolder.choleskyDecomposeStepKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                if (n - k - 1 > 0) {
                    clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.choleskyDecomposeStepKernel, 1, null, new long[]{n - k - 1}, null, 0, null, null);
                }
            }
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)n * n * Sizeof.cl_double, Pointer.to(h_A), 0, null, null);
            // Zero out upper part
            for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) h_A[i * n + j] = 0.0;
            return new CholeskyResult<Real>(fromDoubleArray(h_A, n, n, a));
        } finally {
            if (memA != null) clReleaseMemObject(memA);
        }
    }


    @Override
    public EigenResult<Real> eigen(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": OpenCL not available for Eigen decomposition");
        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        
        // Check for symmetry - Jacobi is only for symmetric matrices
        double[] h_A = toDoubleArray(a);
        boolean symmetric = true;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(h_A[i * n + j] - h_A[j * n + i]) > 1e-10) { symmetric = false; break; }
            }
            if (!symmetric) break;
        }

        if (!symmetric) throw new UnsupportedOperationException("General Eigen decomposition not yet implemented natively in OpenCL (Symmetric only via Jacobi)");

        double[] vData = new double[n * n];
        for (int i = 0; i < n; i++) vData[i * n + i] = 1.0;

        cl_mem memA = null, memV = null;
        try {
            memA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), null);
            memV = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(vData), null);

            int maxIters = 50 * n * n;
            for (int iter = 0; iter < maxIters; iter++) {
                // Find element with max absolute value in upper triangular part
                clEnqueueReadBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), 0, null, null);
                int p = 0, q = 1;
                double maxVal = Math.abs(h_A[0 * n + 1]);
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        double val = Math.abs(h_A[i * n + j]);
                        if (val > maxVal) { maxVal = val; p = i; q = j; }
                    }
                }

                if (maxVal < 1e-15) break;

                // Jacobi rotation on CPU (simplest path for now to avoid many small kernels)
                double app = h_A[p * n + p];
                double aqq = h_A[q * n + q];
                double apq = h_A[p * n + q];
                double phi = 0.5 * Math.atan2(2.0 * apq, aqq - app);
                double cos_val = Math.cos(phi);
                double sin_val = Math.sin(phi);

                // Update A and V (could be GPU kernels if n is large)
                for (int i = 0; i < n; i++) {
                    double api = h_A[p * n + i];
                    double aqi = h_A[q * n + i];
                    h_A[p * n + i] = cos_val * api - sin_val * aqi;
                    h_A[q * n + i] = sin_val * api + cos_val * aqi;
                    h_A[i * n + p] = h_A[p * n + i];
                    h_A[i * n + q] = h_A[q * n + i];

                    double vpi = vData[p * n + i];
                    double vqi = vData[q * n + i];
                    vData[p * n + i] = cos_val * vpi - sin_val * vqi;
                    vData[q * n + i] = sin_val * vpi + cos_val * vqi;
                }
                h_A[p * n + p] = cos_val * cos_val * app - 2.0 * sin_val * cos_val * apq + sin_val * sin_val * aqq;
                h_A[q * n + q] = sin_val * sin_val * app + 2.0 * sin_val * cos_val * apq + cos_val * cos_val * aqq;
                h_A[p * n + q] = 0.0;
                h_A[q * n + p] = 0.0;

                clEnqueueWriteBuffer(OpenCLHolder.commandQueue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(h_A), 0, null, null);
                clEnqueueWriteBuffer(OpenCLHolder.commandQueue, memV, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(vData), 0, null, null);
            }

            double[] eigenvalues = new double[n];
            for (int i = 0; i < n; i++) eigenvalues[i] = h_A[i * n + i];

            return new EigenResult<Real>(
                fromDoubleArray(vData, n, n, a),
                toRealVector(eigenvalues, a)
            );
        } finally {
            if (memA != null) clReleaseMemObject(memA);
            if (memV != null) clReleaseMemObject(memV);
        }
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

    private Matrix<Real> elementWiseVec(double[] a, double[] b, cl_kernel k, int rows, int cols, Matrix<Real> reference) {
        return fromDoubleArray(vecOp(a, b, k), rows, cols, reference);
    }

    private double[] vecOp(double[] a, double[] b, cl_kernel k) {
        int n = a.length;
        double[] result = new double[n];
        cl_mem mA = null, mB = null, mC = null;
        try {
            mA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(a), null);
            mB = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(b), null);
            mC = clCreateBuffer(OpenCLHolder.context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null);

            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mB));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
            return result;
        } finally { 
            if (mA != null) clReleaseMemObject(mA); 
            if (mB != null) clReleaseMemObject(mB); 
            if (mC != null) clReleaseMemObject(mC); 
        }
    }

    private double[] scaleVec(double[] a, double s) {
        int n = a.length;
        double[] result = new double[n];
        cl_mem mA = null, mC = null;
        try {
            mA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(a), null);
            mC = clCreateBuffer(OpenCLHolder.context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null);

            clSetKernelArg(OpenCLHolder.vecScaleKernel, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(OpenCLHolder.vecScaleKernel, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
            clSetKernelArg(OpenCLHolder.vecScaleKernel, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(OpenCLHolder.vecScaleKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.vecScaleKernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
            return result;
        } finally { 
            if (mA != null) clReleaseMemObject(mA); 
            if (mC != null) clReleaseMemObject(mC); 
        }
    }

    private double[] scaleVecElementWise(double[] a, double[] b) {
        return vecOp(a, b, OpenCLHolder.vecDotPartialKernel);
    }

    private Matrix<Real> multiplyComplex(Matrix<Real> a, Matrix<Real> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        double[] h_A = toComplexDoubleArray(a);
        double[] h_B = toComplexDoubleArray(b);
        double[] h_C = new double[m * n * 2];

        cl_mem memA = null, memB = null, memC = null;
        try {
            memA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * m * k, Pointer.to(h_A), null);
            memB = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * k * n, Pointer.to(h_B), null);
            memC = clCreateBuffer(OpenCLHolder.context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * 2 * m * n, null, null);

            clSetKernelArg(OpenCLHolder.complexMatMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(OpenCLHolder.complexMatMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(OpenCLHolder.complexMatMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(OpenCLHolder.complexMatMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(OpenCLHolder.complexMatMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(OpenCLHolder.complexMatMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));

            long[] globalWorkSize = new long[]{n, m};
            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.complexMatMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * 2 * m * n, Pointer.to(h_C), 0, null, null);
            
            return fromComplexDoubleArray(h_C, m, n, a);
        } finally {
            if (memA != null) clReleaseMemObject(memA);
            if (memB != null) clReleaseMemObject(memB);
            if (memC != null) clReleaseMemObject(memC);
        }
    }

    private boolean isComplex(Matrix<Real> m) {
        if (m.rows() == 0 || m.cols() == 0) return false;
        return ((Object)m.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private double[] toComplexDoubleArray(Matrix<Real> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Real val = m.get(i, j);
                if (((Object)val) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) {
                    data[(i * cols + j) * 2] = cv.getReal().doubleValue();
                    data[(i * cols + j) * 2 + 1] = cv.getImaginary().doubleValue();
                } else {
                    data[(i * cols + j) * 2] = val.doubleValue();
                    data[(i * cols + j) * 2 + 1] = 0.0;
                }
            }
        }
        return data;
    }


    private Matrix<Real> fromComplexDoubleArray(double[] data, int rows, int cols, Matrix<Real> reference) {
        Real[] reals = new Real[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            reals[i] = (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
        }
        return new DenseMatrix<Real>(reals, rows, cols, (Ring<Real>) reference.getScalarRing());
    }

    private Matrix<Real> elementWiseVecComplex(double[] a, double[] b, cl_kernel k, int rows, int cols, Matrix<Real> reference) {
        return fromComplexDoubleArray(vecOpComplex(a, b, k), rows, cols, reference);
    }

    private double[] vecOpComplex(double[] a, double[] b, cl_kernel k) {
        int n = a.length / 2;
        double[] result = new double[n * 2];
        cl_mem mA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * n, Pointer.to(a), null);
        cl_mem mB = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * n, Pointer.to(b), null);
        cl_mem mC = clCreateBuffer(OpenCLHolder.context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * 2 * n, null, null);
        try {
            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mB));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * 2 * n, Pointer.to(result), 0, null, null);
        } finally { 
            if (mA != null) clReleaseMemObject(mA); 
            if (mB != null) clReleaseMemObject(mB); 
            if (mC != null) clReleaseMemObject(mC); 
        }
        return result;
    }

    private boolean isComplexVec(Vector<Real> v) {
        if (v.dimension() == 0) return false;
        return ((Object)v.get(0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private double[] toComplexDoubleVec(Vector<Real> v) {
        int dim = v.dimension();
        double[] data = new double[dim * 2];
        for (int i = 0; i < dim; i++) {
            Real val = v.get(i);
            if (((Object)val) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) {
                data[i * 2] = cv.getReal().doubleValue();
                data[i * 2 + 1] = cv.getImaginary().doubleValue();
            } else {
                data[i * 2] = val.doubleValue();
                data[i * 2 + 1] = 0.0;
            }
        }
        return data;
    }

    private double[] scaleVecComplex(double[] a, Real s) {
        int n = a.length / 2;
        double[] result = new double[n * 2];
        double[] scal = new double[2];
        if (((Object)s) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) {
            scal[0] = cv.getReal().doubleValue();
            scal[1] = cv.getImaginary().doubleValue();
        } else {
            scal[0] = s.doubleValue();
            scal[1] = 0.0;
        }

        cl_mem mA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * n, Pointer.to(a), null);
        cl_mem mC = clCreateBuffer(OpenCLHolder.context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * 2 * n, null, null);
        try {
            clSetKernelArg(OpenCLHolder.complexVecScaleKernel, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(OpenCLHolder.complexVecScaleKernel, 1, Sizeof.cl_double * 2, Pointer.to(scal));
            clSetKernelArg(OpenCLHolder.complexVecScaleKernel, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(OpenCLHolder.complexVecScaleKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.complexVecScaleKernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * 2 * n, Pointer.to(result), 0, null, null);
        } finally { 
            if (mA != null) clReleaseMemObject(mA); 
            if (mC != null) clReleaseMemObject(mC); 
        }
        return result;
    }

    private double[] toDoubleVec(Vector<Real> v) {
        double[] d = new double[v.dimension()];
        for (int i = 0; i < d.length; i++) d[i] = v.get(i).doubleValue();
        return d;
    }

    private Vector<Real> toRealVector(double[] d, Matrix<Real> reference) {
        List<Real> reals = new ArrayList<>(d.length);
        for(double v : d) reals.add(Real.of(v));
        return new DenseVector<>(reals, (Ring<Real>) reference.getScalarRing());
    }

    private Vector<Real> toRealVector(double[] d, Vector<Real> reference) {
        List<Real> reals = new ArrayList<>(d.length);
        for(double v : d) reals.add(Real.of(v));
        return new DenseVector<>(reals, (Ring<Real>) reference.getScalarRing());
    }

    private double[] matVecMul(double[] a, double[] b, int rows, int cols) {
        // Mv = treat b as nx1 matrix and use GPU matmul
        double[] bMat = b; // already a flat column
        double[] c = new double[rows];
        cl_mem mA = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * rows * cols, Pointer.to(a), null);
        cl_mem mB = clCreateBuffer(OpenCLHolder.context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * cols, Pointer.to(bMat), null);
        cl_mem mC = clCreateBuffer(OpenCLHolder.context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * rows, null, null);
        try {
            clSetKernelArg(OpenCLHolder.matMulKernel, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(OpenCLHolder.matMulKernel, 1, Sizeof.cl_mem, Pointer.to(mB));
            clSetKernelArg(OpenCLHolder.matMulKernel, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(OpenCLHolder.matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(OpenCLHolder.matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{1}));
            clSetKernelArg(OpenCLHolder.matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{cols}));
            long[] globalWorkSize = new long[]{1, rows};
            clEnqueueNDRangeKernel(OpenCLHolder.commandQueue, OpenCLHolder.matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(OpenCLHolder.commandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * rows, Pointer.to(c), 0, null, null);
        } finally { 
            if (mA != null) clReleaseMemObject(mA); 
            if (mB != null) clReleaseMemObject(mB); 
            if (mC != null) clReleaseMemObject(mC); 
        }
        return c;
    }

    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override
    public String getId() {
        return "opencl";
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public void shutdown() {
        if (initialized) {
            clReleaseKernel(OpenCLHolder.matMulKernel);
            clReleaseKernel(OpenCLHolder.vecAddKernel);
            clReleaseKernel(OpenCLHolder.vecSubKernel);
            clReleaseKernel(OpenCLHolder.vecScaleKernel);
            clReleaseKernel(OpenCLHolder.vecDotPartialKernel);
            clReleaseKernel(OpenCLHolder.transposeKernel);
            clReleaseKernel(OpenCLHolder.normalizeRowKernel);
            clReleaseKernel(OpenCLHolder.gaussJordanKernel);
            clReleaseKernel(OpenCLHolder.normalizeRowInvKernel);
            clReleaseKernel(OpenCLHolder.gaussJordanInvKernel);
            clReleaseKernel(OpenCLHolder.gaussElimPhase1Kernel);
            clReleaseKernel(OpenCLHolder.gaussElimPhase1WithBKernel);
            clReleaseKernel(OpenCLHolder.swapRowsKernel);
            clReleaseKernel(OpenCLHolder.luDecomposeStepKernel);
            clReleaseKernel(OpenCLHolder.choleskyDecomposeStepKernel);
            clReleaseKernel(OpenCLHolder.qrHouseholderApplyKernel);
            clReleaseKernel(OpenCLHolder.complexMatMulKernel);
            clReleaseKernel(OpenCLHolder.complexVecAddKernel);
            clReleaseKernel(OpenCLHolder.complexVecSubKernel);
            clReleaseKernel(OpenCLHolder.complexVecScaleKernel);
            clReleaseKernel(OpenCLHolder.complexVecDotPartialKernel);
            clReleaseKernel(OpenCLHolder.vecDotKernel);
            clReleaseKernel(OpenCLHolder.vecNormKernel);
            clReleaseKernel(OpenCLHolder.hestenesDotKernel);
            clReleaseKernel(OpenCLHolder.hestenesApplyKernel);
            clReleaseProgram(OpenCLHolder.program);
            clReleaseCommandQueue(OpenCLHolder.commandQueue);
            clReleaseContext(OpenCLHolder.context);
            initialized = false;
        }
    }


}
