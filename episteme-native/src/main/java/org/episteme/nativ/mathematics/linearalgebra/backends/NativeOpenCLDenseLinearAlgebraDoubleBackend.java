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
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.nativ.technical.backend.gpu.opencl.OpenCLManager;
import org.episteme.nativ.technical.backend.gpu.opencl.OpenCLKernels;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.core.mathematics.structures.rings.FieldElement;

import java.util.List;
import java.util.Arrays;

/**
 * OpenCL implementation of Dense Linear Algebra Provider for Double precision.
 * Requires fp64 support on the GPU device.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, GPUBackend.class})
public class NativeOpenCLDenseLinearAlgebraDoubleBackend<E extends FieldElement<E>> implements LinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "opencl"; }

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLDenseLinearAlgebraDoubleBackend.class);
    
    private cl_program program;
    private cl_kernel matMulKernel;
    private cl_kernel vecAddKernel;
    private cl_kernel vecSubKernel;
    private cl_kernel vecScaleKernel;
    private cl_kernel transposeKernel;
    private cl_kernel complexMatMulKernel;
    private cl_kernel dotKernel;
    private cl_kernel normKernel;
    private cl_kernel gaussJordanKernel;
    private cl_kernel normalizeRowKernel;
    private cl_kernel normalizeRowInvKernel;
    private cl_kernel gaussJordanInvKernel;
    
    private volatile boolean initialized = false;

    private synchronized void ensureInitialized() {
        if (initialized) return;
        
        OpenCLManager.ensureInitialized();
        if (!OpenCLManager.isInitialized() || !OpenCLManager.isSupportsDouble()) return;

        try {
            cl_context context = OpenCLManager.getContext();
            String source = OpenCLKernels.FP64_HEADER + OpenCLKernels.DENSE_DOUBLE_KERNELS;
            program = clCreateProgramWithSource(context, 1, new String[]{source}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            
            matMulKernel = tryCreateKernel(program, "matrixMultiply");
            vecAddKernel = tryCreateKernel(program, "vec_add");
            vecSubKernel = tryCreateKernel(program, "vec_sub");
            vecScaleKernel = tryCreateKernel(program, "vec_scale");
            transposeKernel = tryCreateKernel(program, "transpose");
            complexMatMulKernel = tryCreateKernel(program, "complexMatrixMultiply");
            dotKernel = tryCreateKernel(program, "vec_dot");
            normKernel = tryCreateKernel(program, "vec_norm");
            gaussJordanKernel = tryCreateKernel(program, "gaussJordan");
            normalizeRowKernel = tryCreateKernel(program, "normalizeRow");
            normalizeRowInvKernel = tryCreateKernel(program, "normalizeRowInv");
            gaussJordanInvKernel = tryCreateKernel(program, "gaussJordanInv");
            
            initialized = (matMulKernel != null);
            if (initialized) {
                logger.info("Native OpenCL Dense Double Backend initialized successfully.");
            }
        } catch (Throwable t) {
            logger.error("Failed to initialize OpenCL Double Backend: {}", t.getMessage());
        }
    }

    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        ensureInitialized();
        return initialized;
    }

    public boolean isExplicitlyDisabled() {
        return Boolean.getBoolean("episteme.backend.native.disabled") ||
               Boolean.getBoolean("episteme.backend.opencl.disabled") || 
               Boolean.getBoolean("episteme.backend.gpu.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra-opencl.disabled");
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        Object zero = ring.zero();
        return zero instanceof RealDouble || zero instanceof Complex || (zero instanceof Real r && !r.isFast());
    }

    @Override
    public String getId() { return "opencl-dense-double"; }

    @Override
    public String getName() { return "Native OpenCL Dense Double Backend"; }

    @Override public int getPriority() { return 105; }
    @Override public void shutdown() { close(); }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        
        if (isComplex(a)) return multiplyComplex(a, b);
        
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        
        double[] da = toDoubleArray(a);
        double[] db = toDoubleArray(b);
        double[] dc = new double[m * n];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * k, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * k * n, Pointer.to(db), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * m * n, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));
            
            clEnqueueNDRangeKernel(queue, matMulKernel, 2, null, new long[]{n, m}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(dc), 0, null, null);
            
            return fromDoubleArray(dc, m, n, a);
        }
    }

    private Matrix<E> multiplyComplex(Matrix<E> a, Matrix<E> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        double[] h_A = toComplexDoubleArray(a);
        double[] h_B = toComplexDoubleArray(b);
        double[] h_C = new double[m * n * 2];

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();

            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * m * k, Pointer.to(h_A), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * k * n, Pointer.to(h_B), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * 2 * m * n, null, null), CL::clReleaseMemObject);

            clSetKernelArg(complexMatMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(complexMatMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(complexMatMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(complexMatMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(complexMatMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(complexMatMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));

            clEnqueueNDRangeKernel(queue, complexMatMulKernel, 2, null, new long[]{n, m}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * 2 * m * n, Pointer.to(h_C), 0, null, null);
            
            return fromComplexDoubleArray(h_C, m, n, a);
        }
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        int rows = a.rows();
        int cols = a.cols();
        double[] src = toDoubleArray(a);
        double[] dst = new double[rows * cols];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * rows * cols, Pointer.to(src), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * rows * cols, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(transposeKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(transposeKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(transposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(transposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{cols}));
            
            clEnqueueNDRangeKernel(queue, transposeKernel, 2, null, new long[]{cols, rows}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memB, CL_TRUE, 0, (long)Sizeof.cl_double * rows * cols, Pointer.to(dst), 0, null, null);
            
            return fromDoubleArray(dst, cols, rows, a);
        }
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return elementWise(a, b, vecAddKernel);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return elementWise(a, b, vecSubKernel);
    }

    private Matrix<E> elementWise(Matrix<E> a, Matrix<E> b, cl_kernel kernel) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        int n = a.rows() * a.cols();
        double[] da = toDoubleArray(a);
        double[] db = toDoubleArray(b);
        double[] dc = new double[n];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(db), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(dc), 0, null, null);
            
            return fromDoubleArray(dc, a.rows(), a.cols(), a);
        }
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        int n = a.rows() * a.cols();
        double[] da = toDoubleArray(a);
        double s = getRealValue(scalar);
        double[] dc = new double[n];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(vecScaleKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(vecScaleKernel, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
            clSetKernelArg(vecScaleKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(vecScaleKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(queue, vecScaleKernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(dc), 0, null, null);
            
            return fromDoubleArray(dc, a.rows(), a.cols(), a);
        }
    }

    // Standard fallbacks for complex decompositions (will be improved later if needed)
    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.solve(a, b);
        
        int n = a.rows();
        int m = n + 1; // Augmented matrix
        double[] da = toDoubleArray(a);
        double[] db = toDoubleVec(b);
        double[] augmented = new double[n * m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(da, i * n, augmented, i * m, n);
            augmented[i * m + n] = db[i];
        }
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * m, Pointer.to(augmented), null), CL::clReleaseMemObject);
            
            for (int k = 0; k < n; k++) {
                clSetKernelArg(normalizeRowKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(normalizeRowKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(normalizeRowKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                clSetKernelArg(normalizeRowKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, normalizeRowKernel, 1, null, new long[]{m - k - 1}, null, 0, null, null);
                
                clSetKernelArg(gaussJordanKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(gaussJordanKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(gaussJordanKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                clSetKernelArg(gaussJordanKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, gaussJordanKernel, 1, null, new long[]{n}, null, 0, null, null);
            }
            
            double[] result = new double[n * m];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * m, Pointer.to(result), 0, null, null);
            double[] sol = new double[n];
            for (int i = 0; i < n; i++) sol[i] = result[i * m + n];
            return fromDoubleVec(sol, (Ring<E>) a.getScalarRing());
        }
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.inverse(a);
        
        int n = a.rows();
        double[] da = toDoubleArray(a);
        double[] identity = new double[n * n];
        for (int i = 0; i < n; i++) identity[i * n + i] = 1.0;
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memInv = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(identity), null), CL::clReleaseMemObject);
            
            for (int k = 0; k < n; k++) {
                clSetKernelArg(normalizeRowInvKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(normalizeRowInvKernel, 1, Sizeof.cl_mem, Pointer.to(memInv));
                clSetKernelArg(normalizeRowInvKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(normalizeRowInvKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, normalizeRowInvKernel, 1, null, new long[]{n}, null, 0, null, null);
                
                clSetKernelArg(normalizeRowKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(normalizeRowKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(normalizeRowKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(normalizeRowKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, normalizeRowKernel, 1, null, new long[]{n - k - 1}, null, 0, null, null);
                
                clSetKernelArg(gaussJordanInvKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(gaussJordanInvKernel, 1, Sizeof.cl_mem, Pointer.to(memInv));
                clSetKernelArg(gaussJordanInvKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(gaussJordanInvKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, gaussJordanInvKernel, 2, null, new long[]{n, n}, null, 0, null, null);
                
                clSetKernelArg(gaussJordanKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(gaussJordanKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(gaussJordanKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(gaussJordanKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, gaussJordanKernel, 1, null, new long[]{n}, null, 0, null, null);
            }
            
            double[] res = new double[n * n];
            clEnqueueReadBuffer(queue, memInv, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(res), 0, null, null);
            return fromDoubleArray(res, n, n, a);
        }
    }

    @Override public E determinant(Matrix<E> a) { return LinearAlgebraProvider.super.determinant(a); }
    @Override public E trace(Matrix<E> a) { return LinearAlgebraProvider.super.trace(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) { return LinearAlgebraProvider.super.lu(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) { return LinearAlgebraProvider.super.qr(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) { return LinearAlgebraProvider.super.svd(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) { return LinearAlgebraProvider.super.cholesky(a); }
    @Override public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) { return LinearAlgebraProvider.super.eigen(a); }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { return LinearAlgebraProvider.super.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { return LinearAlgebraProvider.super.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> v, E s) { return LinearAlgebraProvider.super.multiply(v, s); }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return multiplyComplex(a, b);
        
        int m = a.rows();
        int k = a.cols();
        
        double[] da = toDoubleArray(a);
        double[] db = toDoubleVec(b);
        double[] dc = new double[m];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * k, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * k, Pointer.to(db), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * m, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{1})); // n=1
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));
            
            clEnqueueNDRangeKernel(queue, matMulKernel, 2, null, new long[]{1, m}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * m, Pointer.to(dc), 0, null, null);
            
            return fromDoubleVec(dc, (Ring<E>) a.getScalarRing());
        }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> b) {
        int m = a.rows();
        int k = a.cols();
        double[] da = toComplexDoubleArray(a);
        double[] db = toComplexDoubleVec(b);
        double[] dc = new double[m * 2];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * m * k, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * k, Pointer.to(db), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * 2 * m, null, null), CL::clReleaseMemObject);
            clSetKernelArg(complexMatMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(complexMatMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(complexMatMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(complexMatMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(complexMatMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{1}));
            clSetKernelArg(complexMatMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));
            clEnqueueNDRangeKernel(queue, complexMatMulKernel, 2, null, new long[]{1, m}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * 2 * m, Pointer.to(dc), 0, null, null);
            return fromComplexDoubleVec(dc, (Ring<E>) a.getScalarRing());
        }
    }

    private double[] toDoubleVec(Vector<E> v) {
        int n = v.dimension();
        double[] data = new double[n];
        for (int i = 0; i < n; i++) data[i] = getRealValue(v.get(i));
        return data;
    }

    private double[] toComplexDoubleVec(Vector<E> v) {
        int n = v.dimension();
        double[] data = new double[n * 2];
        for (int i = 0; i < n; i++) {
            E val = v.get(i);
            if (val instanceof Complex cv) {
                data[i * 2] = cv.real();
                data[i * 2 + 1] = cv.imaginary();
            } else {
                data[i * 2] = getRealValue(val);
                data[i * 2 + 1] = 0.0;
            }
        }
        return data;
    }

    private Vector<E> fromDoubleVec(double[] data, Ring<E> ring) {
        E[] elements = (E[]) new FieldElement[data.length];
        for (int i = 0; i < data.length; i++) elements[i] = (E) RealDouble.of(data[i]);
        return new DenseVector<>(java.util.Arrays.asList(elements), ring);
    }

    private Vector<E> fromComplexDoubleVec(double[] data, Ring<E> ring) {
        E[] elements = (E[]) new FieldElement[data.length / 2];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = (E) Complex.of(data[i * 2], data[i * 2 + 1]);
        }
        return new DenseVector<>(java.util.Arrays.asList(elements), ring);
    }

    @Override public E dot(Vector<E> a, Vector<E> b) { return LinearAlgebraProvider.super.dot(a, b); }
    @Override public E norm(Vector<E> a) { return LinearAlgebraProvider.super.norm(a); }

    // Helpers
    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = getRealValue(m.get(i, j));
            }
        }
        return data;
    }

    private Matrix<E> fromDoubleArray(double[] data, int rows, int cols, Matrix<E> reference) {
        Ring<E> ring = reference.getScalarRing();
        E[] elements = (E[]) new FieldElement[data.length];
        for (int i = 0; i < data.length; i++) {
            elements[i] = (E) RealDouble.of(data[i]);
        }
        return new DenseMatrix<>(elements, rows, cols, ring);
    }

    private double[] toComplexDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = m.get(i, j);
                if (val instanceof Complex cv) {
                    data[(i * cols + j) * 2] = cv.real();
                    data[(i * cols + j) * 2 + 1] = cv.imaginary();
                } else {
                    data[(i * cols + j) * 2] = getRealValue(val);
                    data[(i * cols + j) * 2 + 1] = 0.0;
                }
            }
        }
        return data;
    }

    private Matrix<E> fromComplexDoubleArray(double[] data, int rows, int cols, Matrix<E> reference) {
        Ring<E> ring = reference.getScalarRing();
        E[] elements = (E[]) new FieldElement[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            elements[i] = (E) Complex.of(data[i * 2], data[i * 2 + 1]);
        }
        return new DenseMatrix<>(elements, rows, cols, ring);
    }

    private double getRealValue(E val) {
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof Real r) return r.doubleValue();
        if (val instanceof Complex c) return c.real();
        return 0.0;
    }

    private boolean isComplex(Matrix<E> m) {
        return m.getScalarRing().zero() instanceof Complex;
    }

    private static cl_kernel tryCreateKernel(cl_program program, String name) {
        try {
            return clCreateKernel(program, name, null);
        } catch (Throwable t) {
            logger.warn("Failed to create OpenCL kernel '{}': {}", name, t.getMessage());
            return null;
        }
    }

    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }

    @Override
    public void close() {
        if (program != null) {
            if (matMulKernel != null) clReleaseKernel(matMulKernel);
            if (vecAddKernel != null) clReleaseKernel(vecAddKernel);
            if (vecSubKernel != null) clReleaseKernel(vecSubKernel);
            if (vecScaleKernel != null) clReleaseKernel(vecScaleKernel);
            if (transposeKernel != null) clReleaseKernel(transposeKernel);
            if (dotKernel != null) clReleaseKernel(dotKernel);
            if (normKernel != null) clReleaseKernel(normKernel);
            if (gaussJordanKernel != null) clReleaseKernel(gaussJordanKernel);
            if (normalizeRowKernel != null) clReleaseKernel(normalizeRowKernel);
            if (normalizeRowInvKernel != null) clReleaseKernel(normalizeRowInvKernel);
            if (gaussJordanInvKernel != null) clReleaseKernel(gaussJordanInvKernel);
            if (complexMatMulKernel != null) clReleaseKernel(complexMatMulKernel);
            clReleaseProgram(program);
            program = null;
        }
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        OpenCLManager.ensureInitialized();
        return new org.episteme.nativ.technical.backend.gpu.opencl.OpenCLExecutionContext(
            OpenCLManager.getContext(), OpenCLManager.getCommandQueue(), OpenCLManager.getDevice());
    }

    @Override
    public org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[] getDevices() {
        if (!isAvailable()) return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[0];
        return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[]{
            new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo(
                "OpenCL Device", 0, 0, "OpenCL")
        };
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        return 0; // Not implemented for OpenCL yet
    }

    @Override
    public void copyToGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {}

    @Override
    public void copyFromGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {}

    @Override
    public void freeGPUMemory(long gpuHandle) {}

    @Override
    public void synchronize() {}

    @Override
    public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) {
        ensureInitialized();
        // Fallback or implementation
    }
}
