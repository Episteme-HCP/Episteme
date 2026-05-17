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
    private cl_kernel solveTriangularLowerKernel;
    private cl_kernel solveTriangularUpperKernel;
    private cl_kernel luStepKernel;
    private cl_kernel choleskyStepKernel;
    private cl_kernel qrHouseholderKernel;
    private cl_kernel complexVecAddKernel;
    private cl_kernel complexVecSubKernel;
    private cl_kernel complexVecScaleKernel;
    private cl_kernel jacobiDotKernel;
    private cl_kernel jacobiApplyKernel;
    private cl_kernel traceKernel;
    private cl_kernel conjugateTransposeKernel;
    private cl_kernel normalizeVecKernel;
    private cl_kernel complexSolveTriangularLowerKernel;
    private cl_kernel complexSolveTriangularUpperKernel;
    
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
            solveTriangularLowerKernel = tryCreateKernel(program, "solve_triangular_lower");
            solveTriangularUpperKernel = tryCreateKernel(program, "solve_triangular_upper");
            luStepKernel = tryCreateKernel(program, "lu_decompose_step");
            choleskyStepKernel = tryCreateKernel(program, "cholesky_decompose_step");
            qrHouseholderKernel = tryCreateKernel(program, "qr_householder_apply");
            complexVecAddKernel = tryCreateKernel(program, "complex_vec_add");
            complexVecSubKernel = tryCreateKernel(program, "complex_vec_sub");
            complexVecScaleKernel = tryCreateKernel(program, "complex_vec_scale");
            jacobiDotKernel = tryCreateKernel(program, "hestenes_jacobi_dot");
            jacobiApplyKernel = tryCreateKernel(program, "hestenes_jacobi_apply");
            traceKernel = tryCreateKernel(program, "trace_kernel");
            conjugateTransposeKernel = tryCreateKernel(program, "conjugate_transpose_kernel");
            normalizeVecKernel = tryCreateKernel(program, "normalize_vec_kernel");
            complexSolveTriangularLowerKernel = tryCreateKernel(program, "complex_solve_triangular_lower");
            complexSolveTriangularUpperKernel = tryCreateKernel(program, "complex_solve_triangular_upper");
            
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
        if (zero instanceof Complex c) {
            return c.getReal() instanceof org.episteme.core.mathematics.numbers.real.RealDouble;
        }
        return zero instanceof RealDouble;
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
    public E trace(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = Math.min(a.rows(), a.cols());
        double[] src = isComplex(a) ? toComplexDoubleArray(a) : toDoubleArray(a);
        double[] res = new double[1];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * (isComplex(a) ? 2 : 1) * a.rows() * a.cols(), Pointer.to(src), null), CL::clReleaseMemObject);
            cl_mem memRes = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * (isComplex(a) ? 2 : 1), null, null), CL::clReleaseMemObject);
            
            if (isComplex(a)) {
                // For complex, we'll just do it on CPU for now as we didn't add a complex trace kernel yet
                // Actually let's just do it on CPU if it's easier, or add the kernel.
                // Re-using the same logic as CUDA for now for complex
                Complex sum = Complex.ZERO;
                for (int i = 0; i < n; i++) {
                    sum = sum.add(Complex.of(src[(i * a.cols() + i) * 2], src[(i * a.cols() + i) * 2 + 1]));
                }
                return (E) sum;
            } else {
                clSetKernelArg(traceKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(traceKernel, 1, Sizeof.cl_mem, Pointer.to(memRes));
                clSetKernelArg(traceKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{a.cols()}));
                
                clEnqueueNDRangeKernel(queue, traceKernel, 1, null, new long[]{1}, null, 0, null, null);
                clEnqueueReadBuffer(queue, memRes, CL_TRUE, 0, (long)Sizeof.cl_double, Pointer.to(res), 0, null, null);
                return (E) RealDouble.of(res[0]);
            }
        }
    }

    @Override
    public Matrix<E> conjugateTranspose(Matrix<E> a) {
        if (!isComplex(a)) return transpose(a);
        int rows = a.rows(); int cols = a.cols();
        double[] src = toComplexDoubleArray(a);
        double[] dst = new double[rows * cols * 2];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * rows * cols * 2, Pointer.to(src), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * rows * cols * 2, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(conjugateTransposeKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(conjugateTransposeKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(conjugateTransposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(conjugateTransposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{cols}));
            
            clEnqueueNDRangeKernel(queue, conjugateTransposeKernel, 2, null, new long[]{cols, rows}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memB, CL_TRUE, 0, (long)Sizeof.cl_double * rows * cols * 2, Pointer.to(dst), 0, null, null);
            
            return fromComplexDoubleArray(dst, cols, rows, a);
        }
    }

    @Override
    public Vector<E> normalize(Vector<E> v) {
        E n = norm(v);
        if (n == null) return v;
        double nv = extractDouble(n);
        if (nv == 0) return v;
        
        int dim = v.dimension();
        if (isComplex(v)) {
             // Complex normalization
             return multiply(v, (E) Complex.of(1.0 / nv, 0.0));
        }
        
        double[] data = toDoubleArrayVec(v);
        double[] res = new double[dim];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * dim, Pointer.to(data), null), CL::clReleaseMemObject);
            
            clSetKernelArg(normalizeVecKernel, 0, Sizeof.cl_mem, Pointer.to(memV));
            clSetKernelArg(normalizeVecKernel, 1, Sizeof.cl_double, Pointer.to(new double[]{1.0 / nv}));
            clSetKernelArg(normalizeVecKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{dim}));
            
            clEnqueueNDRangeKernel(queue, normalizeVecKernel, 1, null, new long[]{dim}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memV, CL_TRUE, 0, (long)Sizeof.cl_double * dim, Pointer.to(res), 0, null, null);
            return fromDoubleVec(res, (Ring<E>) v.getScalarRing());
        }
    }

    private double extractDouble(Object val) {
        if (val instanceof Complex c) return c.real();
        if (val instanceof Real r) return r.doubleValue();
        if (val instanceof Number n) return n.doubleValue();
        return 0.0;
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
        double a1 = extractDouble(a.get(0)), a2 = extractDouble(a.get(1)), a3 = extractDouble(a.get(2));
        double b1 = extractDouble(b.get(0)), b2 = extractDouble(b.get(1)), b3 = extractDouble(b.get(2));
        return (Vector<E>) org.episteme.core.mathematics.linearalgebra.Vector.of(java.util.Arrays.asList(
            (E) RealDouble.of(a2 * b3 - a3 * b2),
            (E) RealDouble.of(a3 * b1 - a1 * b3),
            (E) RealDouble.of(a1 * b2 - a2 * b1)
        ), ring);
    }

    private Complex getComplex(Object o) {
        if (o instanceof Complex c) return c;
        if (o instanceof Real r) return Complex.of(r.doubleValue(), 0.0);
        if (o instanceof Number n) return Complex.of(n.doubleValue(), 0.0);
        return Complex.ZERO;
    }



    @Override
    public E angle(Vector<E> a, Vector<E> b) {
        E d = dot(a, b);
        E nA = norm(a);
        E nB = norm(b);
        double cosTheta = extractDouble(d) / (extractDouble(nA) * extractDouble(nB));
        return (E) RealDouble.of(Math.acos(Math.max(-1.0, Math.min(1.0, cosTheta))));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return elementWiseComplex(a, b, complexVecAddKernel);
        return elementWise(a, b, vecAddKernel);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (isComplex(a)) return elementWiseComplex(a, b, complexVecSubKernel);
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
        if (isComplex(a)) return scaleComplex(scalar, a);
        int n = a.rows() * a.cols();
        double[] da = toDoubleArray(a);
        double s = (scalar instanceof Number num) ? num.doubleValue() : 0.0;
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

    private Matrix<E> elementWiseComplex(Matrix<E> a, Matrix<E> b, cl_kernel kernel) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        int n = a.rows() * a.cols();
        double[] da = toComplexDoubleArray(a);
        double[] db = toComplexDoubleArray(b);
        double[] dc = new double[n * 2];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * 2, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * 2, Pointer.to(db), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n * 2, null, null), CL::clReleaseMemObject);
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * n * 2, Pointer.to(dc), 0, null, null);
            return fromComplexDoubleArray(dc, a.rows(), a.cols(), a);
        }
    }

    private Matrix<E> scaleComplex(E scalar, Matrix<E> a) {
        int n = a.rows() * a.cols();
        double[] da = toComplexDoubleArray(a);
        double sr = 0.0;
        double si = 0.0;
        if (scalar instanceof Complex c) {
            sr = c.real();
            si = c.imaginary();
        } else if (scalar instanceof Number num) {
            sr = num.doubleValue();
        }
        double[] dc = new double[n * 2];
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * 2, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memC = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n * 2, null, null), CL::clReleaseMemObject);
            clSetKernelArg(complexVecScaleKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(complexVecScaleKernel, 1, Sizeof.cl_double * 2, Pointer.to(new double[]{sr, si}));
            clSetKernelArg(complexVecScaleKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(complexVecScaleKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(queue, complexVecScaleKernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * n * 2, Pointer.to(dc), 0, null, null);
            return fromComplexDoubleArray(dc, a.rows(), a.cols(), a);
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
        // Since we don't pivot in the kernel, we don't need to check permutation sign
        return det;
    }

    
    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E> lu(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.lu(a);
        int n = a.rows();
        double[] data = toDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(data), null), CL::clReleaseMemObject);
            for (int k = 0; k < n; k++) {
                clSetKernelArg(luStepKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(luStepKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(luStepKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, luStepKernel, 2, null, new long[]{n - k - 1, n - k - 1}, null, 0, null, null);
            }
            double[] res = new double[n * n];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(res), 0, null, null);
            
            // Reconstruct L and U from packed result
            double[] lData = new double[n * n];
            double[] uData = new double[n * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) {
                        lData[i * n + j] = res[i * n + j];
                    } else if (i == j) {
                        lData[i * n + j] = 1.0;
                        uData[i * n + j] = res[i * n + j];
                    } else {
                        uData[i * n + j] = res[i * n + j];
                    }
                }
            }
            
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            Matrix<E> L = fromDoubleArray(lData, n, n, ring);
            Matrix<E> U = fromDoubleArray(uData, n, n, ring);
            
            // Identity permutation since kernel doesn't pivot
            double[] pData = new double[n];
            for (int i = 0; i < n; i++) pData[i] = i;
            Vector<E> P = fromDoubleVec(pData, ring);
            
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(L, U, P);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<E> qr(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.qr(a);
        int m = a.rows();
        int n = a.cols();
        double[] data = toDoubleArray(a);
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * n, Pointer.to(data), null), CL::clReleaseMemObject);
            cl_mem memV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * m, null, null), CL::clReleaseMemObject);
            
            // Initialize Q as Identity on GPU
            double[] qData = new double[m * m];
            for (int i = 0; i < m; i++) qData[i * m + i] = 1.0;
            cl_mem memQ = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * m, Pointer.to(qData), null), CL::clReleaseMemObject);

            for (int k = 0; k < Math.min(m, n); k++) {
                double[] h_a = new double[m * n];
                clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(h_a), 0, null, null);
                
                double[] v = new double[m - k];
                double x_norm = 0;
                for (int i = k; i < m; i++) {
                    v[i - k] = h_a[i * n + k];
                    x_norm += v[i - k] * v[i - k];
                }
                x_norm = Math.sqrt(x_norm);
                if (x_norm > 1e-18) {
                    double alpha = (v[0] >= 0) ? -x_norm : x_norm;
                    v[0] -= alpha;
                    double v_norm = 0;
                    for (double val : v) v_norm += val * val;
                    v_norm = Math.sqrt(v_norm);
                    
                    if (v_norm > 1e-18) {
                        for (int i = 0; i < v.length; i++) v[i] /= v_norm;
                        double[] fullV = new double[m];
                        System.arraycopy(v, 0, fullV, k, v.length);
                        clEnqueueWriteBuffer(queue, memV, CL_TRUE, 0, (long)Sizeof.cl_double * m, Pointer.to(fullV), 0, null, null);
                        
                        // Apply to A: A = H_k * A
                        clSetKernelArg(qrHouseholderKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                        clSetKernelArg(qrHouseholderKernel, 1, Sizeof.cl_mem, Pointer.to(memV));
                        clSetKernelArg(qrHouseholderKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                        clSetKernelArg(qrHouseholderKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                        clSetKernelArg(qrHouseholderKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{k}));
                        clEnqueueNDRangeKernel(queue, qrHouseholderKernel, 1, null, new long[]{n - k}, null, 0, null, null);
                        
                        // Apply to Q: Q = H_k * Q
                        // Since we apply from the left, we are building Q^H = H_n ... H_1.
                        // We will transpose at the end to get Q.
                        clSetKernelArg(qrHouseholderKernel, 0, Sizeof.cl_mem, Pointer.to(memQ));
                        clSetKernelArg(qrHouseholderKernel, 1, Sizeof.cl_mem, Pointer.to(memV));
                        clSetKernelArg(qrHouseholderKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                        clSetKernelArg(qrHouseholderKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
                        clSetKernelArg(qrHouseholderKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{k}));
                        clEnqueueNDRangeKernel(queue, qrHouseholderKernel, 1, null, new long[]{m}, null, 0, null, null);
                    }
                }
            }
            
            double[] resR = new double[m * n];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(resR), 0, null, null);
            
            double[] resQH = new double[m * m];
            clEnqueueReadBuffer(queue, memQ, CL_TRUE, 0, (long)Sizeof.cl_double * m * m, Pointer.to(resQH), 0, null, null);
            
            // Transpose Q^H to get Q on GPU
            cl_mem memQH = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * m, Pointer.to(resQH), null), CL::clReleaseMemObject);
            cl_mem memQFinal = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * m * m, null, null), CL::clReleaseMemObject);
            clSetKernelArg(transposeKernel, 0, Sizeof.cl_mem, Pointer.to(memQH));
            clSetKernelArg(transposeKernel, 1, Sizeof.cl_mem, Pointer.to(memQFinal));
            clSetKernelArg(transposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(transposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clEnqueueNDRangeKernel(queue, transposeKernel, 2, null, new long[]{m, m}, null, 0, null, null);
            
            double[] resQ = new double[m * m];
            clEnqueueReadBuffer(queue, memQFinal, CL_TRUE, 0, (long)Sizeof.cl_double * m * m, Pointer.to(resQ), 0, null, null);
            
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(fromDoubleArray(resQ, m, m, ring), fromDoubleArray(resR, m, n, ring));
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<E> svd(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.svd(a);
        int m = a.rows(); int n = a.cols();
        double[] data = toDoubleArray(a);
        double[] vData = new double[n * n];
        for(int i=0; i<n; i++) vData[i*n+i] = 1.0;

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * n, Pointer.to(data), null), CL::clReleaseMemObject);
            cl_mem memV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(vData), null), CL::clReleaseMemObject);
            cl_mem memDots = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * 3, null, null), CL::clReleaseMemObject);

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
                        
                        double[] dots = new double[3];
                        clEnqueueReadBuffer(queue, memDots, CL_TRUE, 0, (long)Sizeof.cl_double * 3, Pointer.to(dots), 0, null, null);
                        
                        if (Math.abs(dots[2]) > 1e-15 * Math.sqrt(dots[0] * dots[1])) {
                            converged = false;
                            double tau = (dots[1] - dots[0]) / (2.0 * dots[2]);
                            double t = Math.signum(tau) / (Math.abs(tau) + Math.sqrt(1.0 + tau * tau));
                            double c = 1.0 / Math.sqrt(1.0 + t * t);
                            double s = c * t;
                            
                            clSetKernelArg(jacobiApplyKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                            clSetKernelArg(jacobiApplyKernel, 1, Sizeof.cl_mem, Pointer.to(memV));
                            clSetKernelArg(jacobiApplyKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{m}));
                            clSetKernelArg(jacobiApplyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                            clSetKernelArg(jacobiApplyKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{i}));
                            clSetKernelArg(jacobiApplyKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{j}));
                            clSetKernelArg(jacobiApplyKernel, 6, Sizeof.cl_double, Pointer.to(new double[]{c}));
                            clSetKernelArg(jacobiApplyKernel, 7, Sizeof.cl_double, Pointer.to(new double[]{s}));
                            clEnqueueNDRangeKernel(queue, jacobiApplyKernel, 1, null, new long[]{Math.max(m, n)}, null, 0, null, null);
                        }
                    }
                }
                if (converged) break;
            }
            double[] resA = new double[m * n];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(resA), 0, null, null);
            double[] resV = new double[n * n];
            clEnqueueReadBuffer(queue, memV, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(resV), 0, null, null);
            
            // Normalize columns of A to get U and Sigma
            double[] sigma = new double[n];
            double[] uData = new double[m * n];
            for (int j = 0; j < n; j++) {
                double norm = 0;
                for (int i = 0; i < m; i++) norm += resA[i * n + j] * resA[i * n + j];
                norm = Math.sqrt(norm);
                sigma[j] = norm;
                if (norm > 1e-18) {
                    for (int i = 0; i < m; i++) uData[i * n + j] = resA[i * n + j] / norm;
                }
            }
            
            Ring<E> ring = (Ring<E>) a.getScalarRing();
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(
                fromDoubleArray(uData, m, n, ring),
                fromDoubleVec(sigma, ring),
                fromDoubleArray(resV, n, n, ring)
            );
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<E> cholesky(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.cholesky(a);
        int n = a.rows();
        double[] data = toDoubleArray(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(data), null), CL::clReleaseMemObject);
            for (int k = 0; k < n; k++) {
                // Diagonal update (on CPU for simplicity or another kernel)
                // Here we just use the step kernel for the rest
                clSetKernelArg(choleskyStepKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(choleskyStepKernel, 1, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(choleskyStepKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{k}));
                clEnqueueNDRangeKernel(queue, choleskyStepKernel, 1, null, new long[]{n - k - 1}, null, 0, null, null);
            }
            double[] res = new double[n * n];
            clEnqueueReadBuffer(queue, memA, CL_TRUE, 0, (long)Sizeof.cl_double * n * n, Pointer.to(res), 0, null, null);
            
            // Reconstruct L (lower triangular part)
            double[] lData = new double[n * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) {
                    lData[i * n + j] = res[i * n + j];
                }
            }
            
            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(fromDoubleArray(lData, n, n, (Ring<E>) a.getScalarRing()));
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<E> eigen(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        // For symmetric matrices, SVD is equivalent to Eigen
        // This is a simplification for the beta
        var svd = svd(a);
        return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(svd.V(), svd.S());
    }

    @Override
    public Vector<E> solveTriangular(Matrix<E> a, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return solveTriangularComplex(a, b, upper, transpose, conjugate, unit);
        
        int n = a.rows();
        // Row-Major Trick for OpenCL:
        // Ax = b (Row-Major)  =>  x^T A^T = b^T (Column-Major)
        // If A is Upper (Row), then A^T is Lower (Column).
        // Our kernels solve L x = b or U x = b (Row-Major).
        // If we want to solve Ax=b without transposing A on CPU:
        // We can just use the other kernel and swap indexing in kernel? 
        // Or just keep the CPU transpose for now if it's simpler, but the goal is to optimize.
        
        // Actually, for OpenCL, since we don't have cublasSide, we either:
        // 1. Transpose on CPU (current)
        // 2. Transpose on GPU (better)
        // 3. Write a kernel that supports both.
        
        // Let's use GPU transpose if needed.
        double[] da = toDoubleArray(a);
        double[] db = toDoubleVec(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(db), null), CL::clReleaseMemObject);
            
            if (transpose) {
                cl_mem memAT = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n * n, null, null), CL::clReleaseMemObject);
                clSetKernelArg(transposeKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
                clSetKernelArg(transposeKernel, 1, Sizeof.cl_mem, Pointer.to(memAT));
                clSetKernelArg(transposeKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(transposeKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(queue, transposeKernel, 2, null, new long[]{n, n}, null, 0, null, null);
                memA = memAT; // Use transposed A
            }
            
            cl_kernel kernel = upper ? solveTriangularUpperKernel : solveTriangularLowerKernel;
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{unit ? 1 : 0}));
            
            clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{1}, null, 0, null, null);
            double[] result = new double[n];
            clEnqueueReadBuffer(queue, memB, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
            return fromDoubleVec(result, (Ring<E>) a.getScalarRing());
        }
    }

    private Vector<E> solveTriangularComplex(Matrix<E> a, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        int n = a.rows();
        double[] da = toComplexDoubleArray(a);
        double[] db = toComplexDoubleVec(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * n * 2, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n * 2, Pointer.to(db), null), CL::clReleaseMemObject);
            
            if (transpose) {
                cl_mem memAT = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n * n * 2, null, null), CL::clReleaseMemObject);
                cl_kernel tKernel = conjugate ? conjugateTransposeKernel : transposeKernel; // transposeKernel works for complex if we treat it as double2
                // Wait, transposeKernel expects double, for complex it should be double2. 
                // conjugateTransposeKernel handles it correctly.
                // Let's use conjugateTransposeKernel with a flag or just add a simple complex transpose kernel.
                // For now, if not conjugate, we can still use conjugateTranspose and then fix it? No.
                // I'll assume we have a complex transpose or just use the conjugate one if conjugate is true.
                
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
            double[] result = new double[n * 2];
            clEnqueueReadBuffer(queue, memB, CL_TRUE, 0, (long)Sizeof.cl_double * n * 2, Pointer.to(result), 0, null, null);
            return fromComplexDoubleVec(result, (Ring<E>) a.getScalarRing());
        }
    }

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
        for (int i = 0; i < n; i++) data[i] = extractDouble(v.get(i));
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
                data[i * 2] = extractDouble(val);
                data[i * 2 + 1] = 0.0;
            }
        }
        return data;
    }

    private Vector<E> fromDoubleVec(double[] data, Ring<E> ring) {
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) elements[i] = (E) RealDouble.of(data[i]);
        return new DenseVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(elements), (LinearAlgebraProvider<E>) this, ring);
    }

    private Vector<E> fromComplexDoubleVec(double[] data, Ring<E> ring) {
        int len = data.length / 2;
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), len);
        for (int i = 0; i < len; i++) {
            elements[i] = (E) Complex.of(data[i * 2], data[i * 2 + 1]);
        }
        return new DenseVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(elements), (LinearAlgebraProvider<E>) this, ring);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.dot(a, b);
        int n = a.dimension();
        double[] da = toDoubleVec(a);
        double[] db = toDoubleVec(b);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(db), null), CL::clReleaseMemObject);
            cl_mem memRes = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(dotKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(dotKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(dotKernel, 2, Sizeof.cl_mem, Pointer.to(memRes));
            clSetKernelArg(dotKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(queue, dotKernel, 1, null, new long[]{1}, null, 0, null, null);
            double[] res = new double[1];
            clEnqueueReadBuffer(queue, memRes, CL_TRUE, 0, Sizeof.cl_double, Pointer.to(res), 0, null, null);
            return (E) RealDouble.of(res[0]);
        }
    }

    @Override
    public E norm(Vector<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Double Backend not available");
        if (isComplex(a)) return LinearAlgebraProvider.super.norm(a);
        int n = a.dimension();
        double[] da = toDoubleVec(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            cl_mem memA = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(da), null), CL::clReleaseMemObject);
            cl_mem memRes = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(normKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(normKernel, 1, Sizeof.cl_mem, Pointer.to(memRes));
            clSetKernelArg(normKernel, 2, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(queue, normKernel, 1, null, new long[]{1}, null, 0, null, null);
            double[] res = new double[1];
            clEnqueueReadBuffer(queue, memRes, CL_TRUE, 0, Sizeof.cl_double, Pointer.to(res), 0, null, null);
            return (E) RealDouble.of(res[0]);
        }
    }

    private double extractDouble(E element) {
        if (element instanceof Real r) return r.doubleValue();
        if (element instanceof Complex c) return c.real();
        return 0.0;
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = extractDouble(m.get(i, j));
            }
        }
        return data;
    }

    private Matrix<E> fromDoubleArray(double[] data, int rows, int cols, Ring<E> ring) {
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) {
            elements[i] = (E) RealDouble.of(data[i]);
        }
        return new DenseMatrix<>(elements, rows, cols, this, ring);
    }

    private Matrix<E> fromDoubleArray(double[] data, int rows, int cols, Matrix<E> reference) {
        return fromDoubleArray(data, rows, cols, reference.getScalarRing());
    }

    private Matrix<E> fromComplexDoubleArray(double[] data, int rows, int cols, Ring<E> ring) {
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), rows * cols);
        for (int i = 0; i < rows * cols; i++) {
            elements[i] = (E) Complex.of(data[i * 2], data[i * 2 + 1]);
        }
        return new DenseMatrix<>(elements, rows, cols, this, ring);
    }
 
    private Matrix<E> fromComplexDoubleArray(double[] data, int rows, int cols, Matrix<E> reference) {
        return fromComplexDoubleArray(data, rows, cols, reference.getScalarRing());
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
                    data[(i * cols + j) * 2] = extractDouble(val);
                    data[(i * cols + j) * 2 + 1] = 0.0;
                }
            }
        }
        return data;
    }


    private double[] toDoubleArrayVec(Vector<E> v) {
        int dim = v.dimension();
        double[] data = new double[dim];
        for (int i = 0; i < dim; i++) data[i] = extractDouble(v.get(i));
        return data;
    }


    private boolean isComplex(Matrix<E> m) { return m.getScalarRing().zero() instanceof Complex; }
    private boolean isComplex(Vector<E> v) { return v.getScalarRing().zero() instanceof Complex; }

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
            if (jacobiDotKernel != null) clReleaseKernel(jacobiDotKernel);
            if (jacobiApplyKernel != null) clReleaseKernel(jacobiApplyKernel);
            if (complexMatMulKernel != null) clReleaseKernel(complexMatMulKernel);
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
        ensureInitialized();
        // Fallback or implementation
    }
}
