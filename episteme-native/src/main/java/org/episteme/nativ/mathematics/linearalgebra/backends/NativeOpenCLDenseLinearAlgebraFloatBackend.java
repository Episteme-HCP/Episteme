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
import org.episteme.core.mathematics.numbers.real.RealFloat;
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
 * OpenCL implementation of Dense Linear Algebra Provider for Float precision.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, GPUBackend.class})
public class NativeOpenCLDenseLinearAlgebraFloatBackend<E extends FieldElement<E>> implements LinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "opencl"; }

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLDenseLinearAlgebraFloatBackend.class);
    
    private cl_program program;
    private cl_kernel matMulKernel;
    private cl_kernel vecAddKernel;
    private cl_kernel vecSubKernel;
    private cl_kernel vecScaleKernel;
    private cl_kernel transposeKernel;
    private cl_kernel dotKernel;
    private cl_kernel normKernel;
    private cl_kernel gaussJordanKernel;
    private cl_kernel normalizeRowKernel;
    private cl_kernel normalizeRowInvKernel;
    private cl_kernel gaussJordanInvKernel;
    private cl_kernel solveTriangularLowerKernel;
    private cl_kernel solveTriangularUpperKernel;
    private cl_kernel luStepKernel;
    private cl_kernel choleskyStepKernel;
    private cl_kernel qrHouseholderKernel;
    private cl_kernel complexSolveTriangularLowerKernel;
    private cl_kernel complexSolveTriangularUpperKernel;
    
    private cl_kernel complexMatMulKernel;
    private cl_kernel complexVecAddKernel;
    private cl_kernel complexVecSubKernel;
    private cl_kernel complexVecScaleKernel;
    private cl_kernel jacobiDotKernel;
    private cl_kernel jacobiApplyKernel;
    private cl_kernel traceKernel;
    private cl_kernel conjugateTransposeKernel;
    private cl_kernel normalizeVecKernel;
    
    private volatile boolean initialized = false;

    private synchronized void ensureInitialized() {
        if (initialized) return;
        
        OpenCLManager.ensureInitialized();
        if (!OpenCLManager.isInitialized()) return;

        try {
            cl_context context = OpenCLManager.getContext();
            String source = OpenCLKernels.DENSE_FLOAT_KERNELS + "\n" + OpenCLKernels.DENSE_FLOAT_COMPLEX_KERNELS;
            program = clCreateProgramWithSource(context, 1, new String[]{source}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            
            matMulKernel = tryCreateKernel(program, "matrixMultiplyFloat");
            vecAddKernel = tryCreateKernel(program, "vec_add_float");
            vecSubKernel = tryCreateKernel(program, "vec_sub_float");
            vecScaleKernel = tryCreateKernel(program, "vec_scale_float");
            transposeKernel = tryCreateKernel(program, "transposeFloat");
            dotKernel = tryCreateKernel(program, "vec_dot_float");
            normKernel = tryCreateKernel(program, "vec_norm_float");
            gaussJordanKernel = tryCreateKernel(program, "gaussJordanFloat");
            normalizeRowKernel = tryCreateKernel(program, "normalizeRowFloat");
            normalizeRowInvKernel = tryCreateKernel(program, "normalizeRowInvFloat");
            gaussJordanInvKernel = tryCreateKernel(program, "gaussJordanInvFloat");
            solveTriangularLowerKernel = tryCreateKernel(program, "solve_triangular_lower_float");
            solveTriangularUpperKernel = tryCreateKernel(program, "solve_triangular_upper_float");
            luStepKernel = tryCreateKernel(program, "lu_decompose_step_float"); // Need to ensure these exist or use generic names
            choleskyStepKernel = tryCreateKernel(program, "cholesky_decompose_step_float");
            qrHouseholderKernel = tryCreateKernel(program, "qr_householder_apply_float");
            complexSolveTriangularLowerKernel = tryCreateKernel(program, "complex_solve_triangular_lower_float");
            complexSolveTriangularUpperKernel = tryCreateKernel(program, "complex_solve_triangular_upper_float");
            
            complexMatMulKernel = tryCreateKernel(program, "complexMatrixMultiplyFloat");
            complexVecAddKernel = tryCreateKernel(program, "complex_vec_add_float");
            complexVecSubKernel = tryCreateKernel(program, "complex_vec_sub_float");
            complexVecScaleKernel = tryCreateKernel(program, "complex_vec_scale_float");
            jacobiDotKernel = tryCreateKernel(program, "hestenes_jacobi_dot_float");
            jacobiApplyKernel = tryCreateKernel(program, "hestenes_jacobi_apply_float");
            traceKernel = tryCreateKernel(program, "trace_kernel_float");
            conjugateTransposeKernel = tryCreateKernel(program, "conjugate_transpose_kernel_float");
            normalizeVecKernel = tryCreateKernel(program, "normalize_vec_kernel_float");

            initialized = (matMulKernel != null);
            if (initialized) {
                logger.info("Native OpenCL Dense Float Backend initialized successfully.");
            }
        } catch (Throwable t) {
            logger.error("Failed to initialize OpenCL Float Backend: {}", t.getMessage());
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
        return zero instanceof RealFloat || (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex c && c.getReal() instanceof RealFloat);
    }

    @Override
    public String getId() { return "opencl-dense-float"; }

    @Override
    public String getName() { return "Native OpenCL Dense Float Backend"; }

    @Override public int getPriority() { return 110; }
    @Override public void shutdown() { close(); }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return multiplyComplex(a, b);
        
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        
        float[] fa = toFloatArray(a);
        float[] fb = toFloatArray(b);
        float[] fc = new float[m * n];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * m * k, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * k * n, Pointer.to(fb), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * m * n, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));
            
            clEnqueueNDRangeKernel(queue, matMulKernel, 2, null, new long[]{n, m}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_float * m * n, Pointer.to(fc), 0, null, null);
            
            return fromFloatArray(fc, m, n, a.getScalarRing());
        }
    }

    private Matrix<E> multiplyComplex(Matrix<E> a, Matrix<E> b) {
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        float[] fa = toComplexFloatArray(a);
        float[] fb = toComplexFloatArray(b);
        float[] fc = new float[m * n * 2];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * m * k, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * k * n, Pointer.to(fb), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float2 * m * n, null, null), CL::clReleaseMemObject);
            clSetKernelArg(complexMatMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(complexMatMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(complexMatMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(complexMatMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(complexMatMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(complexMatMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));
            clEnqueueNDRangeKernel(queue, complexMatMulKernel, 2, null, new long[]{n, m}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_float2 * m * n, Pointer.to(fc), 0, null, null);
            return fromComplexFloatArray(fc, m, n, (Ring<E>) a.getScalarRing());
        }
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        int rows = a.rows();
        int cols = a.cols();
        float[] src = toFloatArray(a);
        float[] dst = new float[rows * cols];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * rows * cols, Pointer.to(src), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * rows * cols, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(transposeKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(transposeKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(transposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(transposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{cols}));
            
            clEnqueueNDRangeKernel(queue, transposeKernel, 2, null, new long[]{cols, rows}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memB, CL_TRUE, 0, (long)Sizeof.cl_float * rows * cols, Pointer.to(dst), 0, null, null);
            
            return fromFloatArray(dst, cols, rows, a.getScalarRing());
        }
    }

    @Override
    public E trace(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = Math.min(a.rows(), a.cols());
        float[] src = isComplex(a) ? toComplexFloatArray(a) : toFloatArray(a);
        float[] res = new float[1];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * (isComplex(a) ? 2 : 1) * a.rows() * a.cols(), Pointer.to(src), null), CL::clReleaseMemObject);
            cl_mem memRes = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * (isComplex(a) ? 2 : 1), null, null), CL::clReleaseMemObject);
            
            if (isComplex(a)) {
                Complex sum = Complex.ZERO;
                for (int i = 0; i < n; i++) {
                    sum = sum.add(Complex.of(RealFloat.create(src[(i * a.cols() + i) * 2]), RealFloat.create(src[(i * a.cols() + i) * 2 + 1])));
                }
                return (E) (Object) sum;
            } else {
                clSetKernelArg(traceKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(traceKernel, 1, Sizeof.cl_mem, Pointer.to(memRes));
                clSetKernelArg(traceKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{a.cols()}));
                
                clEnqueueNDRangeKernel(queue, traceKernel, 1, null, new long[]{1}, null, 0, null, null);
                clEnqueueReadBuffer(queue, memRes, CL_TRUE, 0, (long)Sizeof.cl_float, Pointer.to(res), 0, null, null);
                return (E) (Object) RealFloat.create(res[0]);
            }
        }
    }

    @Override
    public Matrix<E> conjugateTranspose(Matrix<E> a) {
        if (!isComplex(a)) return transpose(a);
        int rows = a.rows(); int cols = a.cols();
        float[] src = toComplexFloatArray(a);
        float[] dst = new float[rows * cols * 2];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * rows * cols * 2, Pointer.to(src), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * rows * cols * 2, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(conjugateTransposeKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(conjugateTransposeKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(conjugateTransposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(conjugateTransposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{cols}));
            
            clEnqueueNDRangeKernel(queue, conjugateTransposeKernel, 2, null, new long[]{cols, rows}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memB, CL_TRUE, 0, (long)Sizeof.cl_float * rows * cols * 2, Pointer.to(dst), 0, null, null);
            
            return fromComplexFloatArray(dst, cols, rows, (Ring<E>) a.getScalarRing());
        }
    }

    @Override
    public Vector<E> normalize(Vector<E> v) {
        E n = norm(v);
        if (n == null) return v;
        float nv = getFloatValue(n);
        if (nv == 0) return v;
        
        int dim = v.dimension();
        if (isComplex(v)) {
             return multiply(v, (E) (Object) Complex.of(RealFloat.create(1.0f / nv), RealFloat.create(0.0f)));
        }
        
        float[] data = toFloatArrayVec(v);
        float[] res = new float[dim];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * dim, Pointer.to(data), null), CL::clReleaseMemObject);
            
            clSetKernelArg(normalizeVecKernel, 0, Sizeof.cl_mem, Pointer.to(memV));
            clSetKernelArg(normalizeVecKernel, 1, Sizeof.cl_float, Pointer.to(new float[]{1.0f / nv}));
            clSetKernelArg(normalizeVecKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{dim}));
            
            clEnqueueNDRangeKernel(queue, normalizeVecKernel, 1, null, new long[]{dim}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memV, CL_TRUE, 0, (long)Sizeof.cl_float * dim, Pointer.to(res), 0, null, null);
            return fromFloatVec(res, (Ring<E>) v.getScalarRing());
        }
    }

    private float getFloat(E val) {
        if (val instanceof Complex c) return c.getReal().floatValue();
        if (val instanceof Real r) return r.floatValue();
        if (val instanceof Number n) return n.floatValue();
        return 0.0f;
    }

    private float getFloatValue(E val) {
        return getFloat(val);
    }

    @Override
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only supported for 3D vectors");
        Ring<E> ring = (Ring<E>) a.getScalarRing();
        if (isComplex(a)) {
            Complex a1 = getComplex(a.get(0)), a2 = getComplex(a.get(1)), a3 = getComplex(a.get(2));
            Complex b1 = getComplex(b.get(0)), b2 = getComplex(b.get(1)), b3 = getComplex(b.get(2));
            return (Vector<E>) (Vector) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(
                a2.multiply(b3).subtract(a3.multiply(b2)),
                a3.multiply(b1).subtract(a1.multiply(b3)),
                a1.multiply(b2).subtract(a2.multiply(b1))
            ), (Ring) ring);
        }
        float a1 = getFloat(a.get(0)), a2 = getFloat(a.get(1)), a3 = getFloat(a.get(2));
        float b1 = getFloat(b.get(0)), b2 = getFloat(b.get(1)), b3 = getFloat(b.get(2));
        return (Vector<E>) (Vector) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(
            (E) (Object) RealFloat.create(a2 * b3 - a3 * b2),
            (E) (Object) RealFloat.create(a3 * b1 - a1 * b3),
            (E) (Object) RealFloat.create(a1 * b2 - a2 * b1)
        ), (Ring) ring);
    }

    private Complex getComplex(Object o) {
        if (o instanceof Complex c) return c;
        if (o instanceof Real r) return Complex.of(RealFloat.create((float) r.doubleValue()), RealFloat.create(0.0f));
        if (o instanceof Number n) return Complex.of(RealFloat.create(n.floatValue()), RealFloat.create(0.0f));
        return Complex.ZERO;
    }

    private float getFloat(Object o) {
        if (o instanceof Real r) return r.floatValue();
        if (o instanceof Number n) return n.floatValue();
        if (o instanceof Complex c) return c.getReal().floatValue();
        return 0.0f;
    }

    @Override
    public E angle(Vector<E> a, Vector<E> b) {
        E d = dot(a, b);
        E nA = norm(a);
        E nB = norm(b);
        float cosTheta = getFloatValue(d) / (getFloatValue(nA) * getFloatValue(nB));
        return (E) (Object) RealFloat.create((float) Math.acos(Math.max(-1.0, Math.min(1.0, cosTheta))));
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
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) {
            cl_kernel complexKernel = (kernel == vecAddKernel) ? complexVecAddKernel : complexVecSubKernel;
            return elementWiseComplex(a, b, complexKernel);
        }
        int n = a.rows() * a.cols();
        float[] fa = toFloatArray(a);
        float[] fb = toFloatArray(b);
        float[] fc = new float[n];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n, Pointer.to(fb), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * n, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_float * n, Pointer.to(fc), 0, null, null);
            
            return fromFloatArray(fc, a.rows(), a.cols(), a.getScalarRing());
        }
    }

    private Matrix<E> elementWiseComplex(Matrix<E> a, Matrix<E> b, cl_kernel kernel) {
        int n = a.rows() * a.cols();
        float[] fa = toComplexFloatArray(a);
        float[] fb = toComplexFloatArray(b);
        float[] fc = new float[n * 2];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * n, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * n, Pointer.to(fb), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float2 * n, null, null), CL::clReleaseMemObject);
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_float2 * n, Pointer.to(fc), 0, null, null);
            return fromComplexFloatArray(fc, a.rows(), a.cols(), (Ring<E>) a.getScalarRing());
        }
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return scaleComplex(scalar, a);
        int n = a.rows() * a.cols();
        float[] fa = toFloatArray(a);
        float s = ((Number) scalar).floatValue();
        float[] fc = new float[n];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * n, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(vecScaleKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(vecScaleKernel, 1, Sizeof.cl_float, Pointer.to(new float[]{s}));
            clSetKernelArg(vecScaleKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(vecScaleKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(queue, vecScaleKernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_float * n, Pointer.to(fc), 0, null, null);
            
            return fromFloatArray(fc, a.rows(), a.cols(), a.getScalarRing());
        }
    }

    private Matrix<E> scaleComplex(E scalar, Matrix<E> a) {
        int n = a.rows() * a.cols();
        float[] fa = toComplexFloatArray(a);
        org.episteme.core.mathematics.numbers.complex.Complex sc = (org.episteme.core.mathematics.numbers.complex.Complex) scalar;
        float sr = sc.getReal().floatValue();
        float si = sc.getImaginary().floatValue();
        float[] fc = new float[n * 2];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * n, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float2 * n, null, null), CL::clReleaseMemObject);
            clSetKernelArg(complexVecScaleKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(complexVecScaleKernel, 1, Sizeof.cl_float2, Pointer.to(new float[]{sr, si}));
            clSetKernelArg(complexVecScaleKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(complexVecScaleKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(queue, complexVecScaleKernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_float2 * n, Pointer.to(fc), 0, null, null);
            return fromComplexFloatArray(fc, a.rows(), a.cols(), (Ring<E>) a.getScalarRing());
        }
    }

    // Fallbacks for more complex operations on Float precision
    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.solve(a, b);
        
        int n = a.rows();
        int m = n + 1; // Augmented matrix
        float[] fa = toFloatArray(a);
        float[] fb = toFloatVec(b);
        float[] augmented = new float[n * m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(fa, i * n, augmented, i * m, n);
            augmented[i * m + n] = fb[i];
        }
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n * m, Pointer.to(augmented), null), CL::clReleaseMemObject);
            
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
            
            float[] result = new float[n * m];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_float * n * m, Pointer.to(result), 0, null, null);
            float[] sol = new float[n];
            for (int i = 0; i < n; i++) sol[i] = result[i * m + n];
            return fromFloatVec(sol, (Ring<E>) a.getScalarRing());
        }
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.inverse(a);
        
        int n = a.rows();
        float[] fa = toFloatArray(a);
        float[] identity = new float[n * n];
        for (int i = 0; i < n; i++) identity[i * n + i] = 1.0f;
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n * n, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memInv = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n * n, Pointer.to(identity), null), CL::clReleaseMemObject);
            
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
            
            float[] res = new float[n * n];
            clEnqueueReadBuffer(queue, memInv, CL_TRUE, 0, (long)Sizeof.cl_float * n * n, Pointer.to(res), 0, null, null);
            return fromFloatArray(res, n, n, a.getScalarRing());
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.lu(a);
        int n = a.rows();
        float[] data = toFloatArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n * n, Pointer.to(data), null), CL::clReleaseMemObject);
            for (int k = 0; k < n; k++) {
                clSetKernelArg(luStepKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(luStepKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(luStepKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, luStepKernel, 2, null, new long[]{n - k - 1, n - k - 1}, null, 0, null, null);
            }
            float[] res = new float[n * n];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_float * n * n, Pointer.to(res), 0, null, null);
            
            float[] lData = new float[n * n];
            float[] uData = new float[n * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) {
                        lData[i * n + j] = res[i * n + j];
                    } else if (i == j) {
                        lData[i * n + j] = 1.0f;
                        uData[i * n + j] = res[i * n + j];
                    } else {
                        uData[i * n + j] = res[i * n + j];
                    }
                }
            }
            
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            Matrix<E> L = fromFloatArray(lData, n, n, ring);
            Matrix<E> U = fromFloatArray(uData, n, n, ring);
            
            float[] pData = new float[n];
            for (int i = 0; i < n; i++) pData[i] = i;
            Vector<E> P = fromFloatVec(pData, ring);
            
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(L, U, P);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.qr(a);
        int m = a.rows();
        int n = a.cols();
        float[] data = toFloatArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * m * n, Pointer.to(data), null), CL::clReleaseMemObject);
            cl_mem memV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * m, null, null), CL::clReleaseMemObject);
            
            float[] qData = new float[m * m];
            for (int i = 0; i < m; i++) qData[i * m + i] = 1.0f;
            cl_mem memQ = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * m * m, Pointer.to(qData), null), CL::clReleaseMemObject);

            for (int k = 0; k < Math.min(m, n); k++) {
                float[] h_a = new float[m * n];
                clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_float * m * n, Pointer.to(h_a), 0, null, null);
                float[] v = new float[m - k];
                float x_norm = 0;
                for (int i = k; i < m; i++) {
                    v[i - k] = h_a[i * n + k];
                    x_norm += v[i - k] * v[i - k];
                }
                x_norm = (float) Math.sqrt(x_norm);
                if (x_norm > 1e-7f) {
                    float alpha = (v[0] >= 0) ? -x_norm : x_norm;
                    v[0] -= alpha;
                    float v_norm = 0;
                    for (float val : v) v_norm += val * val;
                    v_norm = (float) Math.sqrt(v_norm);
                    
                    if (v_norm > 1e-7f) {
                        for (int i = 0; i < v.length; i++) v[i] /= v_norm;
                        float[] fullV = new float[m];
                        System.arraycopy(v, 0, fullV, k, v.length);
                        clEnqueueWriteBuffer(queue, memV, CL_TRUE, 0, (long)Sizeof.cl_float * m, Pointer.to(fullV), 0, null, null);
                        
                        clSetKernelArg(qrHouseholderKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                        clSetKernelArg(qrHouseholderKernel, 1, Sizeof.cl_mem, Pointer.to(memV));
                        clSetKernelArg(qrHouseholderKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                        clSetKernelArg(qrHouseholderKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                        clSetKernelArg(qrHouseholderKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{k}));
                        clEnqueueNDRangeKernel(queue, qrHouseholderKernel, 1, null, new long[]{n - k}, null, 0, null, null);
                        
                        clSetKernelArg(qrHouseholderKernel, 0, Sizeof.cl_mem, Pointer.to(memQ));
                        clSetKernelArg(qrHouseholderKernel, 1, Sizeof.cl_mem, Pointer.to(memV));
                        clSetKernelArg(qrHouseholderKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                        clSetKernelArg(qrHouseholderKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
                        clSetKernelArg(qrHouseholderKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{k}));
                        clEnqueueNDRangeKernel(queue, qrHouseholderKernel, 1, null, new long[]{m}, null, 0, null, null);
                    }
                }
            }
            float[] resR = new float[m * n];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_float * m * n, Pointer.to(resR), 0, null, null);
            
            float[] resQH = new float[m * m];
            clEnqueueReadBuffer(queue, memQ, CL_TRUE, 0, (long)Sizeof.cl_float * m * m, Pointer.to(resQH), 0, null, null);
            
            cl_mem memQH = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * m * m, Pointer.to(resQH), null), CL::clReleaseMemObject);
            cl_mem memQFinal = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * m * m, null, null), CL::clReleaseMemObject);
            clSetKernelArg(transposeKernel, 0, Sizeof.cl_mem, Pointer.to(memQH));
            clSetKernelArg(transposeKernel, 1, Sizeof.cl_mem, Pointer.to(memQFinal));
            clSetKernelArg(transposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(transposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clEnqueueNDRangeKernel(queue, transposeKernel, 2, null, new long[]{m, m}, null, 0, null, null);
            
            float[] resQ = new float[m * m];
            clEnqueueReadBuffer(queue, memQFinal, CL_TRUE, 0, (long)Sizeof.cl_float * m * m, Pointer.to(resQ), 0, null, null);
            
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(fromFloatArray(resQ, m, m, ring), fromFloatArray(resR, m, n, ring));
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.svd(a);
        int m = a.rows(); int n = a.cols();
        float[] data = toFloatArray(a);
        float[] vData = new float[n * n];
        for(int i=0; i<n; i++) vData[i*n+i] = 1.0f;

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * m * n, Pointer.to(data), null), CL::clReleaseMemObject);
            cl_mem memV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n * n, Pointer.to(vData), null), CL::clReleaseMemObject);
            cl_mem memDots = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * 3, null, null), CL::clReleaseMemObject);

            for (int iter = 0; iter < 30; iter++) {
                boolean converged = true;
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        clSetKernelArg(jacobiDotKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                        clSetKernelArg(jacobiDotKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{m}));
                        clSetKernelArg(jacobiDotKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                        clSetKernelArg(jacobiDotKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{i}));
                        clSetKernelArg(jacobiDotKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{j}));
                        clSetKernelArg(jacobiDotKernel, 5, Sizeof.cl_mem, Pointer.to(memDots));
                        clEnqueueNDRangeKernel(queue, jacobiDotKernel, 1, null, new long[]{1}, null, 0, null, null);
                        
                        float[] dots = new float[3];
                        clEnqueueReadBuffer(queue, memDots, CL_TRUE, 0, (long)Sizeof.cl_float * 3, Pointer.to(dots), 0, null, null);
                        
                        if (Math.abs(dots[2]) > 1e-6f * Math.sqrt(dots[0] * dots[1])) {
                            converged = false;
                            double tau = (dots[1] - dots[0]) / (2.0 * dots[2]);
                            float t = (float) (Math.signum(tau) / (Math.abs(tau) + Math.sqrt(1.0 + tau * tau)));
                            float c = (float) (1.0 / Math.sqrt(1.0 + t * t));
                            float s = c * t;
                            
                            clSetKernelArg(jacobiApplyKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                            clSetKernelArg(jacobiApplyKernel, 1, Sizeof.cl_mem, Pointer.to(memV));
                            clSetKernelArg(jacobiApplyKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                            clSetKernelArg(jacobiApplyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                            clSetKernelArg(jacobiApplyKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{i}));
                            clSetKernelArg(jacobiApplyKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{j}));
                            clSetKernelArg(jacobiApplyKernel, 6, Sizeof.cl_float, Pointer.to(new float[]{c}));
                            clSetKernelArg(jacobiApplyKernel, 7, Sizeof.cl_float, Pointer.to(new float[]{s}));
                            clEnqueueNDRangeKernel(queue, jacobiApplyKernel, 1, null, new long[]{Math.max(m, n)}, null, 0, null, null);
                        }
                    }
                }
                if (converged) break;
            }
            float[] resA = new float[m * n];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_float * m * n, Pointer.to(resA), 0, null, null);
            float[] resV = new float[n * n];
            clEnqueueReadBuffer(queue, memV, CL_TRUE, 0, (long)Sizeof.cl_float * n * n, Pointer.to(resV), 0, null, null);
            
            float[] sigma = new float[n];
            float[] uData = new float[m * n];
            for (int j = 0; j < n; j++) {
                float norm = 0;
                for (int i = 0; i < m; i++) norm += resA[i * n + j] * resA[i * n + j];
                norm = (float) Math.sqrt(norm);
                sigma[j] = norm;
                if (norm > 1e-7f) {
                    for (int i = 0; i < m; i++) uData[i * n + j] = resA[i * n + j] / norm;
                }
            }
            
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(
                fromFloatArray(uData, m, n, ring),
                fromFloatVec(sigma, ring),
                fromFloatArray(resV, n, n, ring)
            );
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.cholesky(a);
        int n = a.rows();
        float[] data = toFloatArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n * n, Pointer.to(data), null), CL::clReleaseMemObject);
            for (int k = 0; k < n; k++) {
                float[] h_a = new float[n * n];
                clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_float * n * n, Pointer.to(h_a), 0, null, null);
                float sum = 0;
                for (int j = 0; j < k; j++) sum += h_a[k * n + j] * h_a[k * n + j];
                h_a[k * n + k] = (float) Math.sqrt(h_a[k * n + k] - sum);
                clEnqueueWriteBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_float * n * n, Pointer.to(h_a), 0, null, null);

                clSetKernelArg(choleskyStepKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(choleskyStepKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(choleskyStepKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, choleskyStepKernel, 1, null, new long[]{n - k - 1}, null, 0, null, null);
            }
            float[] res = new float[n * n];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_float * n * n, Pointer.to(res), 0, null, null);
            
            float[] lData = new float[n * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) {
                    lData[i * n + j] = res[i * n + j];
                }
            }
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(fromFloatArray(lData, n, n, (Ring<E>) a.getScalarRing()));
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        var svd = svd(a);
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(svd.V(), svd.S());
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        var lu = lu(a);
        Matrix<E> U = lu.U();
        int n = a.rows();
        Ring<E> ring = (Ring<E>) a.getScalarRing();
        E det = ring.one();
        for (int i = 0; i < n; i++) {
            det = ring.multiply(det, U.get(i, i));
        }
        return det;
    }


    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { return LinearAlgebraProvider.super.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { return LinearAlgebraProvider.super.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> v, E s) { return LinearAlgebraProvider.super.multiply(v, s); }
    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return multiplyComplex(a, b);
        
        int m = a.rows();
        int k = a.cols();
        
        float[] fa = toFloatArray(a);
        float[] fb = toFloatVec(b);
        float[] fc = new float[m];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * m * k, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * k, Pointer.to(fb), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * m, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{1})); // n=1
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));
            
            clEnqueueNDRangeKernel(queue, matMulKernel, 2, null, new long[]{1, m}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_float * m, Pointer.to(fc), 0, null, null);
            
            return fromFloatVec(fc, (Ring<E>) a.getScalarRing());
        }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> b) {
        int m = a.rows();
        int k = a.cols();
        float[] fa = toComplexFloatArray(a);
        float[] fb = toComplexFloatVec(b);
        float[] fc = new float[m * 2];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * m * k, Pointer.to(fa), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * k, Pointer.to(fb), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float2 * m, null, null), CL::clReleaseMemObject);
            clSetKernelArg(complexMatMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(complexMatMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(complexMatMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(complexMatMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(complexMatMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{1}));
            clSetKernelArg(complexMatMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));
            clEnqueueNDRangeKernel(queue, complexMatMulKernel, 2, null, new long[]{1, m}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_float2 * m, Pointer.to(fc), 0, null, null);
            return fromComplexFloatVec(fc, (Ring<E>) a.getScalarRing());
        }
    }

    @Override
    public Vector<E> solveTriangular(Matrix<E> a, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return solveTriangularComplex(a, b, upper, transpose, conjugate, unit);
        
        int n = a.rows();
        float[] da = toFloatArray(a);
        float[] db = toFloatVec(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n, Pointer.to(db), null), CL::clReleaseMemObject);
            
            if (transpose) {
                cl_mem memAT = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * n * n, null, null), CL::clReleaseMemObject);
                clSetKernelArg(transposeKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(transposeKernel, 1, Sizeof.cl_mem, Pointer.to(memAT));
                clSetKernelArg(transposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(transposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(queue, transposeKernel, 2, null, new long[]{n, n}, null, 0, null, null);
                memA = memAT;
            }
            
            cl_kernel kernel = upper ? solveTriangularUpperKernel : solveTriangularLowerKernel;
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{unit ? 1 : 0}));
            
            clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{1}, null, 0, null, null);
            float[] result = new float[n];
            clEnqueueReadBuffer(queue, memB, CL_TRUE, 0, (long)Sizeof.cl_float * n, Pointer.to(result), 0, null, null);
            return fromFloatVec(result, (Ring<E>) a.getScalarRing());
        }
    }

    private Vector<E> solveTriangularComplex(Matrix<E> a, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        int n = a.rows();
        float[] da = toComplexFloatArray(a);
        float[] db = toComplexFloatVec(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * n * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float2 * n, Pointer.to(db), null), CL::clReleaseMemObject);
            
            if (transpose) {
                cl_mem memAT = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float2 * n * n, null, null), CL::clReleaseMemObject);
                cl_kernel tKernel = conjugate ? conjugateTransposeKernel : transposeKernel;
                clSetKernelArg(tKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(tKernel, 1, Sizeof.cl_mem, Pointer.to(memAT));
                clSetKernelArg(tKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(tKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(queue, tKernel, 2, null, new long[]{n, n}, null, 0, null, null);
                memA = memAT;
            }
            
            cl_kernel kernel = upper ? complexSolveTriangularUpperKernel : complexSolveTriangularLowerKernel;
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{unit ? 1 : 0}));
            
            clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{1}, null, 0, null, null);
            float[] result = new float[n * 2];
            clEnqueueReadBuffer(queue, memB, CL_TRUE, 0, (long)Sizeof.cl_float2 * n, Pointer.to(result), 0, null, null);
            return fromComplexFloatVec(result, (Ring<E>) a.getScalarRing());
        }
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.dot(a, b);
        int n = a.dimension();
        float[] da = toFloatVec(a);
        float[] db = toFloatVec(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n, Pointer.to(db), null), CL::clReleaseMemObject);
            cl_mem memRes = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_float, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(dotKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(dotKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(dotKernel, 2, Sizeof.cl_mem, Pointer.to(memRes));
            clSetKernelArg(dotKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(queue, dotKernel, 1, null, new long[]{1}, null, 0, null, null);
            float[] res = new float[1];
            clEnqueueReadBuffer(queue, memRes, CL_TRUE, 0, Sizeof.cl_float, Pointer.to(res), 0, null, null);
            return (E) RealFloat.create(res[0]);
        }
    }

    @Override
    public E norm(Vector<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Float Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.norm(a);
        int n = a.dimension();
        float[] da = toFloatVec(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memRes = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_float, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(normKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(normKernel, 1, Sizeof.cl_mem, Pointer.to(memRes));
            clSetKernelArg(normKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(queue, normKernel, 1, null, new long[]{1}, null, 0, null, null);
            float[] res = new float[1];
            clEnqueueReadBuffer(queue, memRes, CL_TRUE, 0, Sizeof.cl_float, Pointer.to(res), 0, null, null);
            return (E) RealFloat.create(res[0]);
        }
    }


    // Helpers
    private boolean isComplex(Matrix<E> m) { return m.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex; }
    private boolean isComplex(Vector<E> v) { return v.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex; }

    private float[] toFloatArrayVec(Vector<E> v) {
        int dim = v.dimension();
        float[] data = new float[dim];
        for (int i = 0; i < dim; i++) data[i] = getFloatValue(v.get(i));
        return data;
    }

    private float[] toFloatArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        float[] data = new float[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = getFloat(m.get(i, j));
            }
        }
        return data;
    }

    private float[] toFloatVec(Vector<E> v) {
        int n = v.dimension();
        float[] data = new float[n];
        for (int i = 0; i < n; i++) data[i] = getFloat(v.get(i));
        return data;
    }

    private float[] toComplexFloatVec(Vector<E> v) {
        int n = v.dimension();
        float[] data = new float[n * 2];
        for (int i = 0; i < n; i++) {
            Complex c = getComplex(v.get(i));
            data[i * 2] = c.getReal().floatValue();
            data[i * 2 + 1] = c.getImaginary().floatValue();
        }
        return data;
    }

    private float[] toComplexFloatArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        float[] data = new float[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex c = getComplex(m.get(i, j));
                data[(i * cols + j) * 2] = c.getReal().floatValue();
                data[(i * cols + j) * 2 + 1] = c.getImaginary().floatValue();
            }
        }
        return data;
    }

    private Matrix<E> fromFloatArray(float[] data, int rows, int cols, Ring<E> ring) {
        E[] elements = (E[]) new FieldElement[data.length];
        for (int i = 0; i < data.length; i++) {
            elements[i] = (E) (Object) RealFloat.create(data[i]);
        }
        return new DenseMatrix<>(elements, rows, cols, (LinearAlgebraProvider<E>) this, ring);
    }

    private Matrix<E> fromComplexFloatArray(float[] data, int rows, int cols, Ring<E> ring) {
        E[] elements = (E[]) new FieldElement[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            elements[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(RealFloat.create(data[i * 2]), RealFloat.create(data[i * 2 + 1]));
        }
        return new DenseMatrix<>(elements, rows, cols, (LinearAlgebraProvider<E>) this, ring);
    }

    private Vector<E> fromFloatVec(float[] data, Ring<E> ring) {
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) elements[i] = (E) (Object) RealFloat.create(data[i]);
        return new DenseVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(elements), (LinearAlgebraProvider<E>) this, ring);
    }

    private Vector<E> fromComplexFloatVec(float[] data, Ring<E> ring) {
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length / 2);
        for (int i = 0; i < elements.length; i++) {
            elements[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(RealFloat.create(data[i * 2]), RealFloat.create(data[i * 2 + 1]));
        }
        return new DenseVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(elements), (LinearAlgebraProvider<E>) this, ring);
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
            if (solveTriangularLowerKernel != null) clReleaseKernel(solveTriangularLowerKernel);
            if (solveTriangularUpperKernel != null) clReleaseKernel(solveTriangularUpperKernel);
            if (luStepKernel != null) clReleaseKernel(luStepKernel);
            if (choleskyStepKernel != null) clReleaseKernel(choleskyStepKernel);
            if (qrHouseholderKernel != null) clReleaseKernel(qrHouseholderKernel);
            if (complexMatMulKernel != null) clReleaseKernel(complexMatMulKernel);
            if (complexVecAddKernel != null) clReleaseKernel(complexVecAddKernel);
            if (complexVecSubKernel != null) clReleaseKernel(complexVecSubKernel);
            if (complexVecScaleKernel != null) clReleaseKernel(complexVecScaleKernel);
            if (jacobiDotKernel != null) clReleaseKernel(jacobiDotKernel);
            if (jacobiApplyKernel != null) clReleaseKernel(jacobiApplyKernel);
            if (complexSolveTriangularLowerKernel != null) clReleaseKernel(complexSolveTriangularLowerKernel);
            if (complexSolveTriangularUpperKernel != null) clReleaseKernel(complexSolveTriangularUpperKernel);
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
        throw new UnsupportedOperationException("Matrix multiply for DoubleBuffer not implemented in float backend");
    }
}
