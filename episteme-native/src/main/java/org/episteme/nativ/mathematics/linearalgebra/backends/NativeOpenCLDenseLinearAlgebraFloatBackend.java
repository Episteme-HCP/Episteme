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
    
    private cl_kernel complexMatMulKernel;
    private cl_kernel complexVecAddKernel;
    private cl_kernel complexVecSubKernel;
    private cl_kernel complexVecScaleKernel;
    
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
            
            complexMatMulKernel = tryCreateKernel(program, "complexMatrixMultiplyFloat");
            complexVecAddKernel = tryCreateKernel(program, "complex_vec_add_float");
            complexVecSubKernel = tryCreateKernel(program, "complex_vec_sub_float");
            complexVecScaleKernel = tryCreateKernel(program, "complex_vec_scale_float");

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
            
            return fromFloatArray(fc, m, n, a);
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
            return fromComplexFloatArray(fc, m, n, a);
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
            
            return fromFloatArray(dst, cols, rows, a);
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
            
            return fromFloatArray(fc, a.rows(), a.cols(), a);
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
            return fromComplexFloatArray(fc, a.rows(), a.cols(), a);
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
            
            return fromFloatArray(fc, a.rows(), a.cols(), a);
        }
    }

    private Matrix<E> scaleComplex(E scalar, Matrix<E> a) {
        int n = a.rows() * a.cols();
        float[] fa = toComplexFloatArray(a);
        org.episteme.core.mathematics.numbers.complex.Complex sc = (org.episteme.core.mathematics.numbers.complex.Complex) scalar;
        float sr = ((Number) sc.real()).floatValue();
        float si = ((Number) sc.imaginary()).floatValue();
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
            return fromComplexFloatArray(fc, a.rows(), a.cols(), a);
        }
    }

    // Fallbacks for more complex operations on Float precision
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { return LinearAlgebraProvider.super.solve(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { return LinearAlgebraProvider.super.inverse(a); }
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
    @Override public Vector<E> multiply(Matrix<E> a, Vector<E> b) { return LinearAlgebraProvider.super.multiply(a, b); }
    @Override public E dot(Vector<E> a, Vector<E> b) { return LinearAlgebraProvider.super.dot(a, b); }
    @Override public E norm(Vector<E> a) { return LinearAlgebraProvider.super.norm(a); }

    // Helpers
    private boolean isComplex(Matrix<E> m) {
        return m.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private float[] toFloatArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        float[] data = new float[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = ((Number) m.get(i, j)).floatValue();
            }
        }
        return data;
    }

    private float[] toComplexFloatArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        float[] data = new float[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) m.get(i, j);
                data[(i * cols + j) * 2] = ((Number) c.real()).floatValue();
                data[(i * cols + j) * 2 + 1] = ((Number) c.imaginary()).floatValue();
            }
        }
        return data;
    }

    private Matrix<E> fromFloatArray(float[] data, int rows, int cols, Matrix<E> reference) {
        Ring<E> ring = reference.getScalarRing();
        E[] elements = (E[]) new FieldElement[data.length];
        for (int i = 0; i < data.length; i++) {
            elements[i] = (E) RealFloat.create(data[i]);
        }
        return new DenseMatrix<>(elements, rows, cols, ring);
    }

    private Matrix<E> fromComplexFloatArray(float[] data, int rows, int cols, Matrix<E> reference) {
        Ring<E> ring = reference.getScalarRing();
        E[] elements = (E[]) new FieldElement[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            elements[i] = (E) org.episteme.core.mathematics.numbers.complex.Complex.of(RealFloat.create(data[i * 2]), RealFloat.create(data[i * 2 + 1]));
        }
        return new DenseMatrix<>(elements, rows, cols, ring);
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
            if (complexMatMulKernel != null) clReleaseKernel(complexMatMulKernel);
            if (complexVecAddKernel != null) clReleaseKernel(complexVecAddKernel);
            if (complexVecSubKernel != null) clReleaseKernel(complexVecSubKernel);
            if (complexVecScaleKernel != null) clReleaseKernel(complexVecScaleKernel);
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
