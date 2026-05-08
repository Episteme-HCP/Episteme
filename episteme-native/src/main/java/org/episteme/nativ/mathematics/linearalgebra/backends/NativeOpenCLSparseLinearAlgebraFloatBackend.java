/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.jocl.*;
import static org.jocl.CL.*;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealFloat;
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
 * OpenCL implementation of Sparse Linear Algebra Provider for Float precision.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class})
public class NativeOpenCLSparseLinearAlgebraFloatBackend<E extends FieldElement<E>> implements SparseLinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "opencl"; }

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLSparseLinearAlgebraFloatBackend.class);
    
    private cl_program program;
    private cl_kernel spmvKernel;
    private cl_kernel complexSpmvKernel;
    private cl_kernel vecAddKernel;
    private cl_kernel complexVecAddKernel;
    private cl_kernel vecSubKernel;
    private cl_kernel complexVecSubKernel;
    private cl_kernel vecScaleKernel;
    private cl_kernel complexVecScaleKernel;
    private cl_kernel dotPartialKernel;
    private cl_kernel complexDotPartialKernel;
    private cl_kernel saxpyKernel;
    private cl_kernel complexSaxpyKernel;
    
    private volatile boolean initialized = false;

    private synchronized void ensureInitialized() {
        if (initialized) return;
        
        OpenCLManager.ensureInitialized();
        if (!OpenCLManager.isInitialized()) return;

        try {
            cl_context context = OpenCLManager.getContext();
            String source = OpenCLKernels.SPARSE_FLOAT_KERNELS + "\n" + 
                           OpenCLKernels.DENSE_FLOAT_KERNELS + "\n" +
                           OpenCLKernels.DENSE_FLOAT_COMPLEX_KERNELS;
            program = clCreateProgramWithSource(context, 1, new String[]{source}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            
            spmvKernel = tryCreateKernel(program, "spmv_csr_float");
            complexSpmvKernel = tryCreateKernel(program, "complex_spmv_csr_float");
            vecAddKernel = tryCreateKernel(program, "vec_add_float");
            complexVecAddKernel = tryCreateKernel(program, "complex_vec_add_float");
            vecSubKernel = tryCreateKernel(program, "vec_sub_float");
            complexVecSubKernel = tryCreateKernel(program, "complex_vec_sub_float");
            vecScaleKernel = tryCreateKernel(program, "vec_scale_float");
            complexVecScaleKernel = tryCreateKernel(program, "complex_vec_scale_float");
            dotPartialKernel = tryCreateKernel(program, "vec_dot_partial_float");
            complexDotPartialKernel = tryCreateKernel(program, "complex_dot_partial_float");
            saxpyKernel = tryCreateKernel(program, "saxpy_float");
            complexSaxpyKernel = tryCreateKernel(program, "complex_saxpy_float");

            initialized = (spmvKernel != null);
            if (initialized) {
                logger.info("Native OpenCL Sparse Float Backend initialized successfully.");
            }
        } catch (Throwable t) {
            logger.error("Failed to initialize OpenCL Sparse Float Backend: {}", t.getMessage());
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
        return zero instanceof RealFloat || (zero instanceof Complex c && c.getReal() instanceof RealFloat);
    }

    @Override
    public String getId() { return "opencl-sparse-float"; }

    @Override
    public String getName() { return "Native OpenCL Sparse Float Backend"; }

    @Override public int getPriority() { return 115; } // Slightly higher than Dense
    @Override public void shutdown() { close(); }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Float Backend not available");
        
        if (isComplex(a)) return multiplyComplex(a, x);
        
        SparseMatrix<E> sa = ensureSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        int nnz = sa.getNnz();
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIndices = sa.getColIndices();
        float[] values = new float[nnz];
        Object[] valsObj = sa.getValues();
        for (int i = 0; i < nnz; i++) values[i] = ((Number) valsObj[i]).floatValue();
        
        float[] xData = new float[cols];
        for (int i = 0; i < cols; i++) xData[i] = ((Number) x.get(i)).floatValue();
        
        float[] yData = new float[rows];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (rows + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem memInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIndices), null), CL::clReleaseMemObject);
            cl_mem memVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem memX = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * cols, Pointer.to(xData), null), CL::clReleaseMemObject);
            cl_mem memY = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * rows, null, null), CL::clReleaseMemObject);
            
            computeSpmv(queue, spmvKernel, rows, memPtr, memInd, memVal, memX, memY, false);
            clEnqueueReadBuffer(queue, memY, CL_TRUE, 0, (long)Sizeof.cl_float * rows, Pointer.to(yData), 0, null, null);
            
            return fromFloatArray(yData, sa.getScalarRing());
        }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> x) {
        SparseMatrix<E> sa = ensureSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        int nnz = sa.getNnz();
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIndices = sa.getColIndices();
        float[] values = toComplexFloatArray(sa);
        float[] xData = toComplexFloatVec(x);
        float[] yData = new float[rows * 2];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (rows + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem memInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIndices), null), CL::clReleaseMemObject);
            cl_mem memVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * 2 * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem memX = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * 2 * cols, Pointer.to(xData), null), CL::clReleaseMemObject);
            cl_mem memY = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * 2 * rows, null, null), CL::clReleaseMemObject);
            
            computeSpmv(queue, complexSpmvKernel, rows, memPtr, memInd, memVal, memX, memY, true);
            clEnqueueReadBuffer(queue, memY, CL_TRUE, 0, (long)Sizeof.cl_float * 2 * rows, Pointer.to(yData), 0, null, null);
            
            return fromComplexFloatArray(yData, sa.getScalarRing());
        }
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Float Backend not available");
        
        try {
            return bicgstab(a, b, null, (E)(Object) RealFloat.create(1e-6f), 1000);
        } catch (Exception e) {
            logger.warn("BiCGSTAB failed, falling back to CG: {}", e.getMessage());
            return conjugateGradient(a, b, null, (E)(Object) RealFloat.create(1e-6f), 1000);
        }
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Float Backend not available");
        SparseMatrix<E> sa = ensureSparse(a);
        int n = sa.rows();
        int nnz = sa.getNnz();
        float tol = ((Number)tolerance).floatValue();

        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        boolean isComplex = isComplex(a);
        float[] values = isComplex ? toComplexFloatArray(sa) : toFloatArray(sa);
        float[] bData = isComplex ? toComplexFloatVec(b) : toFloatVec(b);
        int elemSize = isComplex ? 2 : 1;

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();

            cl_mem mPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_COPY_HOST_PTR, (long)Sizeof.cl_float * elemSize * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_COPY_HOST_PTR, (long)Sizeof.cl_float * elemSize * n, Pointer.to(bData), null), CL::clReleaseMemObject);
            
            cl_mem mX = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            if (x0 != null) {
                 float[] x0Data = isComplex ? toComplexFloatVec(x0) : toFloatVec(x0);
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_float * elemSize * n, Pointer.to(x0Data), 0, null, null);
            } else {
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_float * elemSize * n, Pointer.to(new float[n * elemSize]), 0, null, null);
            }
            
            cl_mem mR = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mP = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mAp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);

            cl_kernel currentSpmv = isComplex ? complexSpmvKernel : spmvKernel;
            cl_kernel currentDot = isComplex ? complexDotPartialKernel : dotPartialKernel;
            cl_kernel currentAdd = isComplex ? complexVecAddKernel : vecAddKernel;
            cl_kernel currentSub = isComplex ? complexVecSubKernel : vecSubKernel;
            cl_kernel currentScale = isComplex ? complexVecScaleKernel : vecScaleKernel;

            // r = b - Ax
            computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mX, mAp, isComplex);
            clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_float * elemSize * n, 0, null, null);
            gpuSub(queue, currentSub, mR, mAp, n, isComplex);
            clEnqueueCopyBuffer(queue, mR, mP, 0, 0, (long)Sizeof.cl_float * elemSize * n, 0, null, null);
            
            Object rsOld = gpuDot(queue, currentDot, mR, mR, mTemp, n, isComplex);
            float tolSq = tol * tol;
            
            for (int i = 0; i < maxIterations; i++) {
                computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mP, mAp, isComplex);
                Object pAp = gpuDot(queue, currentDot, mP, mAp, mTemp, n, isComplex);
                
                Object alpha = isComplex ? ((Complex)rsOld).divide((Complex)pAp) : ((Float)rsOld) / ((Float)pAp);
                
                gpuSaxpy(queue, currentAdd, currentScale, n, mP, mX, alpha, isComplex);
                gpuSaxpy(queue, currentSub, currentScale, n, mAp, mR, alpha, isComplex);
                
                Object rsNew = gpuDot(queue, currentDot, mR, mR, mTemp, n, isComplex);
                if (isComplex) {
                    if (((Complex)rsNew).abs().doubleValue() < tolSq) break;
                } else {
                    if ((Float)rsNew < tolSq) break;
                }
                
                Object beta = isComplex ? ((Complex)rsNew).divide((Complex)rsOld) : ((Float)rsNew) / ((Float)rsOld);
                gpuScale(queue, currentScale, mP, beta, n, isComplex);
                gpuAdd(queue, currentAdd, mR, mP, n, isComplex);
                rsOld = rsNew;
            }
            
            float[] res = new float[n * elemSize];
            clEnqueueReadBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_float * elemSize * n, Pointer.to(res), 0, null, null);
            return isComplex ? fromComplexFloatArray(res, sa.getScalarRing()) : fromFloatArray(res, sa.getScalarRing());
        } catch (Exception e) {
            throw new RuntimeException("OpenCL CG solver failure", e);
        }
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Float Backend not available");
        SparseMatrix<E> sa = ensureSparse(a);
        int n = sa.rows();
        int nnz = sa.getNnz();
        float tol = ((Number)tolerance).floatValue();

        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        boolean isComplex = isComplex(a);
        float[] values = isComplex ? toComplexFloatArray(sa) : toFloatArray(sa);
        float[] bData = isComplex ? toComplexFloatVec(b) : toFloatVec(b);
        int elemSize = isComplex ? 2 : 1;

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();

            cl_mem mPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_COPY_HOST_PTR, (long)Sizeof.cl_float * elemSize * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_COPY_HOST_PTR, (long)Sizeof.cl_float * elemSize * n, Pointer.to(bData), null), CL::clReleaseMemObject);
            
            cl_mem mX = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            if (x0 != null) {
                 float[] x0Data = isComplex ? toComplexFloatVec(x0) : toFloatVec(x0);
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_float * elemSize * n, Pointer.to(x0Data), 0, null, null);
            } else {
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_float * elemSize * n, Pointer.to(new float[n * elemSize]), 0, null, null);
            }
            
            cl_mem mR = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mR0 = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mP = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mS = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mT = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_float * elemSize * n, null, null), CL::clReleaseMemObject);

            cl_kernel currentSpmv = isComplex ? complexSpmvKernel : spmvKernel;
            cl_kernel currentDot = isComplex ? complexDotPartialKernel : dotPartialKernel;
            cl_kernel currentAdd = isComplex ? complexVecAddKernel : vecAddKernel;
            cl_kernel currentSub = isComplex ? complexVecSubKernel : vecSubKernel;
            cl_kernel currentScale = isComplex ? complexVecScaleKernel : vecScaleKernel;
            cl_kernel currentSaxpy = isComplex ? complexSaxpyKernel : saxpyKernel;

            // r = b - Ax
            computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mX, mV, isComplex);
            clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_float * elemSize * n, 0, null, null);
            gpuSub(queue, currentSub, mR, mV, n, isComplex);
            clEnqueueCopyBuffer(queue, mR, mR0, 0, 0, (long)Sizeof.cl_float * elemSize * n, 0, null, null);
            
            Complex rho = Complex.of(1, 0);
            Complex alpha = Complex.of(1, 0);
            Complex omega = Complex.of(1, 0);
            float rhoF = 1, alphaF = 1, omegaF = 1;

            for (int i = 0; i < maxIterations; i++) {
                Object rhoNext = gpuDot(queue, currentDot, mR0, mR, mTemp, n, isComplex);
                
                Object beta;
                if (isComplex) {
                    beta = ((Complex)rhoNext).divide(rho).multiply(alpha.divide(omega));
                    rho = (Complex)rhoNext;
                } else {
                    float rn = (Float)rhoNext;
                    beta = (rn / rhoF) * (alphaF / omegaF);
                    rhoF = rn;
                }

                gpuSaxpy(queue, currentSaxpy, n, mV, mP, isComplex ? (Object) omega.negate() : (Object) Float.valueOf(-omegaF), isComplex);
                gpuScale(queue, currentScale, mP, beta, n, isComplex);
                gpuSaxpy(queue, currentSaxpy, n, mR, mP, isComplex ? (Object) Complex.ONE : (Object) 1.0f, isComplex);

                computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mP, mV, isComplex);
                Object dotV = gpuDot(queue, currentDot, mR0, mV, mTemp, n, isComplex);
                if (isComplex) {
                    alpha = rho.divide((Complex)dotV);
                } else {
                    alphaF = rhoF / (Float)dotV;
                }

                clEnqueueCopyBuffer(queue, mR, mS, 0, 0, (long)Sizeof.cl_float * elemSize * n, 0, null, null);
                gpuSaxpy(queue, currentSaxpy, n, mV, mS, isComplex ? (Object) alpha.negate() : (Object) Float.valueOf(-alphaF), isComplex);

                computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mS, mT, isComplex);
                Object tDotS = gpuDot(queue, currentDot, mT, mS, mTemp, n, isComplex);
                Object tDotT = gpuDot(queue, currentDot, mT, mT, mTemp, n, isComplex);
                
                if (isComplex) {
                    omega = ((Complex)tDotS).divide((Complex)tDotT);
                } else {
                    omegaF = (Float)tDotS / (Float)tDotT;
                }

                gpuSaxpy(queue, currentSaxpy, n, mP, mX, isComplex ? (Object) alpha : (Object) Float.valueOf(alphaF), isComplex);
                gpuSaxpy(queue, currentSaxpy, n, mS, mX, isComplex ? (Object) omega : (Object) Float.valueOf(omegaF), isComplex);

                clEnqueueCopyBuffer(queue, mS, mR, 0, 0, (long)Sizeof.cl_float * elemSize * n, 0, null, null);
                gpuSaxpy(queue, currentSaxpy, n, mT, mR, isComplex ? (Object) omega.negate() : (Object) Float.valueOf(-omegaF), isComplex);

                Object resNorm = gpuDot(queue, currentDot, mR, mR, mTemp, n, isComplex);
                double normVal = isComplex ? ((Complex)resNorm).abs().doubleValue() : (Float)resNorm;
                if (normVal < tol * tol) break;
            }
            
            float[] res = new float[n * elemSize];
            clEnqueueReadBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_float * elemSize * n, Pointer.to(res), 0, null, null);
            return isComplex ? fromComplexFloatArray(res, sa.getScalarRing()) : fromFloatArray(res, sa.getScalarRing());
        } catch (Exception e) {
            throw new RuntimeException("OpenCL BiCGSTAB failure", e);
        }
    }

    private void computeSpmv(cl_command_queue queue, cl_kernel k, int n, cl_mem mPtr, cl_mem mInd, cl_mem mVal, cl_mem mX, cl_mem mY, boolean isComplex) {
        clSetKernelArg(k, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mPtr));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mInd));
        clSetKernelArg(k, 3, Sizeof.cl_mem, Pointer.to(mVal));
        clSetKernelArg(k, 4, Sizeof.cl_mem, Pointer.to(mX));
        clSetKernelArg(k, 5, Sizeof.cl_mem, Pointer.to(mY));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private Object gpuDot(cl_command_queue queue, cl_kernel k, cl_mem a, cl_mem b, cl_mem mTemp, int n, boolean isComplex) {
        int localSize = 128;
        int numGroups = (n + localSize - 1) / localSize;
        int elemSize = isComplex ? 2 : 1;
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(b));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mTemp));
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clSetKernelArg(k, 4, (long) localSize * Sizeof.cl_float * elemSize, null);
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{(long) numGroups * localSize}, new long[]{localSize}, 0, null, null);
        float[] partial = new float[numGroups * elemSize];
        clEnqueueReadBuffer(queue, mTemp, CL_TRUE, 0, (long)Sizeof.cl_float * elemSize * numGroups, Pointer.to(partial), 0, null, null);
        if (isComplex) {
            float resR = 0, resI = 0;
            for (int i = 0; i < numGroups; i++) {
                resR += partial[i * 2];
                resI += partial[i * 2 + 1];
            }
            return Complex.of(resR, resI);
        } else {
            float sum = 0; for(float d : partial) sum += d;
            return sum;
        }
    }

    private void gpuSaxpy(cl_command_queue queue, cl_kernel k, int n, cl_mem x, cl_mem y, Object alpha, boolean isComplex) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(x));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(y));
        if (isComplex) {
            Complex c = (Complex) alpha;
            clSetKernelArg(k, 2, Sizeof.cl_float2, Pointer.to(new float[]{c.getReal().floatValue(), c.getImaginary().floatValue()}));
        } else {
            float a = ((Number) alpha).floatValue();
            clSetKernelArg(k, 2, Sizeof.cl_float, Pointer.to(new float[]{a}));
        }
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private void gpuScale(cl_command_queue queue, cl_kernel k, cl_mem a, Object s, int n, boolean isComplex) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(a));
        if (isComplex) {
            Complex c = (Complex) s;
            clSetKernelArg(k, 1, Sizeof.cl_float2, Pointer.to(new float[]{c.getReal().floatValue(), c.getImaginary().floatValue()}));
        } else {
            clSetKernelArg(k, 1, Sizeof.cl_float, Pointer.to(new float[]{((Number)s).floatValue()}));
        }
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private void gpuAdd(cl_command_queue queue, cl_kernel k, cl_mem x, cl_mem y, int n, boolean isComplex) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(x));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(y));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(y));
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private void gpuSub(cl_command_queue queue, cl_kernel k, cl_mem x, cl_mem y, int n, boolean isComplex) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(x));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(y));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(x)); // x = x - y
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private SparseMatrix<E> ensureSparse(Matrix<E> a) {
        if (a instanceof SparseMatrix) return (SparseMatrix<E>) a;
        Ring<E> r = a.getScalarRing();
        SparseMatrix<E> s = SparseMatrix.zeros(a.rows(), a.cols(), r);
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) {
                E val = a.get(i, j);
                if (!val.equals(r.zero())) s.set(i, j, val);
            }
        }
        return s;
    }

    private float[] toFloatArray(SparseMatrix<E> m) {
        Object[] vals = m.getValues();
        float[] data = new float[vals.length];
        for (int i = 0; i < vals.length; i++) data[i] = ((Number) vals[i]).floatValue();
        return data;
    }

    private float[] toFloatVec(Vector<E> v) {
        float[] data = new float[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) data[i] = ((Number) v.get(i)).floatValue();
        return data;
    }

    private Vector<E> fromFloatArray(float[] data, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(data.length, ring.zero());
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0.0f) storage.set(i, (E) (Object) RealFloat.create(data[i]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, ring);
    }

    private float[] toComplexFloatArray(SparseMatrix<E> m) {
        int nnz = m.getNnz();
        float[] data = new float[nnz * 2];
        Object[] vals = m.getValues();
        for (int i = 0; i < nnz; i++) {
            Complex c = (Complex) vals[i];
            data[i * 2] = c.getReal().floatValue();
            data[i * 2 + 1] = c.getImaginary().floatValue();
        }
        return data;
    }

    private float[] toComplexFloatVec(Vector<E> v) {
        int n = v.dimension();
        float[] data = new float[n * 2];
        for (int i = 0; i < n; i++) {
            Complex c = (Complex) v.get(i);
            data[i * 2] = c.getReal().floatValue();
            data[i * 2 + 1] = c.getImaginary().floatValue();
        }
        return data;
    }

    private Vector<E> fromComplexFloatArray(float[] data, Ring<E> ring) {
        int n = data.length / 2;
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n);
        for (int i = 0; i < n; i++) {
            elements[i] = (E) (Object) Complex.of(RealFloat.create(data[i * 2]), RealFloat.create(data[i * 2 + 1]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(elements), null, ring);
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

    @Override
    public void close() {
        if (program != null) {
            if (spmvKernel != null) clReleaseKernel(spmvKernel);
            if (complexSpmvKernel != null) clReleaseKernel(complexSpmvKernel);
            if (vecAddKernel != null) clReleaseKernel(vecAddKernel);
            if (complexVecAddKernel != null) clReleaseKernel(complexVecAddKernel);
            if (vecSubKernel != null) clReleaseKernel(vecSubKernel);
            if (complexVecSubKernel != null) clReleaseKernel(complexVecSubKernel);
            if (vecScaleKernel != null) clReleaseKernel(vecScaleKernel);
            if (complexVecScaleKernel != null) clReleaseKernel(complexVecScaleKernel);
            if (dotPartialKernel != null) clReleaseKernel(dotPartialKernel);
            if (complexDotPartialKernel != null) clReleaseKernel(complexDotPartialKernel);
            if (saxpyKernel != null) clReleaseKernel(saxpyKernel);
            if (complexSaxpyKernel != null) clReleaseKernel(complexSaxpyKernel);
            clReleaseProgram(program);
            program = null;
        }
    }

    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }

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
        return 0;
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
        throw new UnsupportedOperationException("Matrix multiply for DoubleBuffer not implemented in sparse backend");
    }
}
