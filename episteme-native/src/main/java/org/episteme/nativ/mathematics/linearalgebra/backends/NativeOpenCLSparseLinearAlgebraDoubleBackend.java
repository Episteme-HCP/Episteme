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
import org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;
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

import java.util.function.Function;

/**
 * OpenCL implementation of Sparse Linear Algebra Provider for Double precision.
 * Requires fp64 support.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class})
public class NativeOpenCLSparseLinearAlgebraDoubleBackend<E extends FieldElement<E>> implements SparseLinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "opencl"; }

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLSparseLinearAlgebraDoubleBackend.class);
    
    private cl_program program;
    private cl_kernel spmvKernel;
    private cl_kernel complexSpmvKernel;
    private cl_kernel saxpyKernel;
    private cl_kernel complexSaxpyKernel;
    private cl_kernel vecAddKernel;
    private cl_kernel complexVecAddKernel;
    private cl_kernel vecSubKernel;
    private cl_kernel complexVecSubKernel;
    private cl_kernel vecScaleKernel;
    private cl_kernel complexVecScaleKernel;
    private cl_kernel dotPartialKernel;
    private cl_kernel complexDotPartialKernel;
    
    private volatile boolean initialized = false;

    private synchronized void ensureInitialized() {
        if (initialized) return;
        
        OpenCLManager.ensureInitialized();
        if (!OpenCLManager.isInitialized() || !OpenCLManager.isSupportsDouble()) return;

        try {
            cl_context context = OpenCLManager.getContext();
            // Combine headers and kernels for sparse and some dense/helper operations needed for solvers
            String source = OpenCLKernels.FP64_HEADER + OpenCLKernels.SPARSE_DOUBLE_KERNELS + OpenCLKernels.DENSE_DOUBLE_KERNELS;
            program = clCreateProgramWithSource(context, 1, new String[]{source}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            
            spmvKernel = tryCreateKernel(program, "spmv_csr_double");
            complexSpmvKernel = tryCreateKernel(program, "complex_spmv_csr_double");
            vecAddKernel = tryCreateKernel(program, "vec_add");
            complexVecAddKernel = tryCreateKernel(program, "complex_vec_add");
            vecSubKernel = tryCreateKernel(program, "vec_sub");
            complexVecSubKernel = tryCreateKernel(program, "complex_vec_sub");
            vecScaleKernel = tryCreateKernel(program, "vec_scale");
            complexVecScaleKernel = tryCreateKernel(program, "complex_vec_scale");
            saxpyKernel = tryCreateKernel(program, "saxpy");
            complexSaxpyKernel = tryCreateKernel(program, "complex_saxpy");
            dotPartialKernel = tryCreateKernel(program, "vec_dot_partial");
            complexDotPartialKernel = tryCreateKernel(program, "complex_dot_partial");

            initialized = (spmvKernel != null);
            if (initialized) {
                logger.info("Native OpenCL Sparse Double Backend initialized successfully.");
            }
        } catch (Throwable t) {
            logger.error("Failed to initialize OpenCL Sparse Double Backend: {}", t.getMessage());
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
    public String getId() { return "opencl-sparse-double"; }

    @Override
    public String getName() { return "Native OpenCL Sparse Double Backend"; }

    @Override public int getPriority() { return 105; }
    @Override public void shutdown() { close(); }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Double Backend not available");
        
        if (isComplex(a)) return multiplyComplex(a, x);
        
        SparseMatrix<E> sa = ensureSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        int nnz = sa.getNnz();
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIndices = sa.getColIndices();
        double[] values = new double[nnz];
        Object[] valsObj = sa.getValues();
        for (int i = 0; i < nnz; i++) values[i] = getRealValue((E) valsObj[i]);
        
        double[] xData = new double[cols];
        for (int i = 0; i < cols; i++) xData[i] = getRealValue(x.get(i));
        
        double[] yData = new double[rows];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (rows + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem memInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIndices), null), CL::clReleaseMemObject);
            cl_mem memVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem memX = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * cols, Pointer.to(xData), null), CL::clReleaseMemObject);
            cl_mem memY = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * rows, null, null), CL::clReleaseMemObject);
            
            computeSpmv(queue, spmvKernel, rows, memPtr, memInd, memVal, memX, memY);
            clEnqueueReadBuffer(queue, memY, CL_TRUE, 0, (long)Sizeof.cl_double * rows, Pointer.to(yData), 0, null, null);
            
            return fromDoubleArray(yData, sa.getScalarRing());
        }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> x) {
        SparseMatrix<E> sa = ensureSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        int nnz = sa.getNnz();
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIndices = sa.getColIndices();
        double[] values = toComplexDoubleArray(sa);
        double[] xData = toComplexDoubleVec(x);
        double[] yData = new double[rows * 2];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (rows + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem memInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIndices), null), CL::clReleaseMemObject);
            cl_mem memVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem memX = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * 2 * cols, Pointer.to(xData), null), CL::clReleaseMemObject);
            cl_mem memY = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * 2 * rows, null, null), CL::clReleaseMemObject);
            
            computeSpmv(queue, complexSpmvKernel, rows, memPtr, memInd, memVal, memX, memY);
            clEnqueueReadBuffer(queue, memY, CL_TRUE, 0, (long)Sizeof.cl_double * 2 * rows, Pointer.to(yData), 0, null, null);
            
            return fromComplexDoubleArray(yData, sa.getScalarRing());
        }
    }

    @Override
    public E trace(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        SparseMatrix<E> sa = ensureSparse(a);
        int n = Math.min(sa.rows(), sa.cols());
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] values = sa.getValues();
        
        if (isComplex(sa)) {
            Complex sum = Complex.ZERO;
            for (int i = 0; i < n; i++) {
                for (int j = rowPtr[i]; j < rowPtr[i + 1]; j++) {
                    if (colIdx[j] == i) {
                        sum = sum.add(getComplex(values[j]));
                        break;
                    }
                }
            }
            return (E) sum;
        } else {
            double sum = 0.0;
            for (int i = 0; i < n; i++) {
                for (int j = rowPtr[i]; j < rowPtr[i + 1]; j++) {
                    if (colIdx[j] == i) {
                        sum += ((Number) values[j]).doubleValue();
                        break;
                    }
                }
            }
            return (E) RealDouble.of(sum);
        }
    }

    @Override
    public Matrix<E> conjugateTranspose(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        SparseMatrix<E> sa = ensureSparse(a);
        Ring<E> ring = (Ring<E>) sa.getScalarRing();
        SparseMatrixStorage<E> transposedStorage = sa.getSparseStorage().transpose();
        
        if (isComplex(sa)) {
             // Conjugate values
             for (java.util.Map.Entry<Long, E> entry : transposedStorage.getData().entrySet()) {
                 Complex c = getComplex(entry.getValue());
                 transposedStorage.set((int)(entry.getKey() >>> 32), (int)(entry.getKey() & 0xFFFFFFFFL), (E) c.conjugate());
             }
        }
        
        return new SparseMatrix<>(transposedStorage, (LinearAlgebraProvider<E>) this, ring);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Double Backend not available");
        
        if (a instanceof SparseMatrix) {
            try {
                // Use BiCGSTAB as default for general sparse
                return bicgstab(a, b, null, (E)(Object) RealDouble.of(1e-10), 1000);
            } catch (Exception e) {
                logger.warn("BiCGSTAB failed, falling back to CG: {}", e.getMessage());
                return conjugateGradient(a, b, null, (E)(Object) RealDouble.of(1e-10), 1000);
            }
        }
        return SparseLinearAlgebraProvider.super.solve(a, b);
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Double Backend not available");
        SparseMatrix<E> sa = ensureSparse(a);
        int n = sa.rows();
        int nnz = sa.getNnz();
        double tol = getRealValue(tolerance);

        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        boolean isComplex = isComplex(a);
        double[] values = isComplex ? toComplexDoubleArray(sa) : new double[nnz];
        if (!isComplex) {
            Object[] valsObj = sa.getValues();
            for (int i = 0; i < nnz; i++) values[i] = getRealValue((E) valsObj[i]);
        }
        double[] bData = isComplex ? toComplexDoubleVec(b) : toDoubleVec(b);
        int elemSize = isComplex ? 2 : 1;

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();

            cl_mem mPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * elemSize * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * elemSize * n, Pointer.to(bData), null), CL::clReleaseMemObject);
            
            cl_mem mX = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            if (x0 != null) {
                 double[] x0Data = isComplex ? toComplexDoubleVec(x0) : toDoubleVec(x0);
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * elemSize * n, Pointer.to(x0Data), 0, null, null);
            } else {
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * elemSize * n, Pointer.to(new double[n * elemSize]), 0, null, null);
            }
            
            cl_mem mR = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mP = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mAp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);

            cl_kernel currentSpmv = isComplex ? complexSpmvKernel : spmvKernel;
            cl_kernel currentDot = isComplex ? complexDotPartialKernel : dotPartialKernel;
            cl_kernel currentSaxpy = isComplex ? complexSaxpyKernel : saxpyKernel;
            cl_kernel currentAdd = isComplex ? complexVecAddKernel : vecAddKernel;
            cl_kernel currentScale = isComplex ? complexVecScaleKernel : vecScaleKernel;

            // r = b - Ax (initial)
            computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mX, mAp);
            clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * elemSize * n, 0, null, null);
            gpuSaxpy(queue, currentSaxpy, n, mAp, mR, isComplex ? Complex.of(-1, 0) : -1.0);
            clEnqueueCopyBuffer(queue, mR, mP, 0, 0, (long)Sizeof.cl_double * elemSize * n, 0, null, null);
            
            Object rsOld = gpuDot(queue, currentDot, mR, mR, mTemp, n, isComplex);
            double tolSq = tol * tol;
            
            for (int i = 0; i < maxIterations; i++) {
                computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mP, mAp);
                Object pAp = gpuDot(queue, currentDot, mP, mAp, mTemp, n, isComplex);
                
                Object alpha = isComplex ? ((Complex)rsOld).divide((Complex)pAp) : ((Double)rsOld) / ((Double)pAp);
                
                gpuSaxpy(queue, currentSaxpy, n, mP, mX, alpha);
                gpuSaxpy(queue, currentSaxpy, n, mAp, mR, isComplex ? ((Complex)alpha).negate() : -((Double)alpha));
                
                Object rsNew = gpuDot(queue, currentDot, mR, mR, mTemp, n, isComplex);
                if (isComplex) {
                    if (((Complex)rsNew).abs().doubleValue() < tolSq) break;
                } else {
                    if ((Double)rsNew < tolSq) break;
                }
                
                Object beta = isComplex ? ((Complex)rsNew).divide((Complex)rsOld) : ((Double)rsNew) / ((Double)rsOld);
                gpuScale(queue, currentScale, mP, beta, n, isComplex);
                gpuAdd(queue, currentAdd, mR, mP, n, isComplex);
                rsOld = rsNew;
            }
            
            double[] res = new double[n * elemSize];
            clEnqueueReadBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * elemSize * n, Pointer.to(res), 0, null, null);
            return isComplex ? fromComplexDoubleArray(res, sa.getScalarRing()) : fromDoubleArray(res, sa.getScalarRing());
        } catch (Exception e) {
            throw new RuntimeException("OpenCL CG solver failure", e);
        }
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Double Backend not available");
        SparseMatrix<E> sa = ensureSparse(a);
        int n = sa.rows();
        int nnz = sa.getNnz();
        double tol = getRealValue(tolerance);

        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        boolean isComplex = isComplex(a);
        double[] values = isComplex ? toComplexDoubleArray(sa) : new double[nnz];
        if (!isComplex) {
            Object[] valsObj = sa.getValues();
            for (int i = 0; i < nnz; i++) values[i] = getRealValue((E) valsObj[i]);
        }
        double[] bData = isComplex ? toComplexDoubleVec(b) : toDoubleVec(b);
        int elemSize = isComplex ? 2 : 1;

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();

            cl_mem mPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * elemSize * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * elemSize * n, Pointer.to(bData), null), CL::clReleaseMemObject);
            
            cl_mem mX = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            if (x0 != null) {
                 double[] x0Data = isComplex ? toComplexDoubleVec(x0) : toDoubleVec(x0);
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * elemSize * n, Pointer.to(x0Data), 0, null, null);
            } else {
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * elemSize * n, Pointer.to(new double[n * elemSize]), 0, null, null);
            }
            
            cl_mem mR = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mR0 = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mP = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mS = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mT = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * elemSize * n, null, null), CL::clReleaseMemObject);

            cl_kernel currentSpmv = isComplex ? complexSpmvKernel : spmvKernel;
            cl_kernel currentDot = isComplex ? complexDotPartialKernel : dotPartialKernel;
            cl_kernel currentSaxpy = isComplex ? complexSaxpyKernel : saxpyKernel;
            cl_kernel currentAdd = isComplex ? complexVecAddKernel : vecAddKernel;
            cl_kernel currentSub = isComplex ? complexVecSubKernel : vecSubKernel;
            cl_kernel currentScale = isComplex ? complexVecScaleKernel : vecScaleKernel;

            // r = b - Ax
            computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mX, mV);
            clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * elemSize * n, 0, null, null);
            gpuSaxpy(queue, currentSaxpy, n, mV, mR, isComplex ? Complex.of(-1, 0) : -1.0);
            clEnqueueCopyBuffer(queue, mR, mR0, 0, 0, (long)Sizeof.cl_double * elemSize * n, 0, null, null);
            
            Complex rho = Complex.of(1, 0);
            Complex alpha = Complex.of(1, 0);
            Complex omega = Complex.of(1, 0);
            
            double rhoReal = 1, alphaReal = 1, omegaReal = 1;

            for (int i = 0; i < maxIterations; i++) {
                Object rhoNext = gpuDot(queue, currentDot, mR0, mR, mTemp, n, isComplex);
                
                Object beta;
                if (isComplex) {
                    beta = ((Complex)rhoNext).divide(rho).multiply(alpha.divide(omega));
                    rho = (Complex)rhoNext;
                } else {
                    double rn = (Double)rhoNext;
                    beta = (rn / rhoReal) * (alphaReal / omegaReal);
                    rhoReal = rn;
                }

                gpuSaxpy(queue, currentSaxpy, n, mV, mP, isComplex ? ((Complex)omega).negate() : -omegaReal);
                gpuScale(queue, currentScale, mP, beta, n, isComplex);
                gpuAdd(queue, currentAdd, mR, mP, n, isComplex);

                computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mP, mV);
                Object dotV = gpuDot(queue, currentDot, mR0, mV, mTemp, n, isComplex);
                if (isComplex) {
                    alpha = rho.divide((Complex)dotV);
                } else {
                    alphaReal = rhoReal / (Double)dotV;
                }

                clEnqueueCopyBuffer(queue, mR, mS, 0, 0, (long)Sizeof.cl_double * elemSize * n, 0, null, null);
                gpuSaxpy(queue, currentSaxpy, n, mV, mS, isComplex ? ((Complex)alpha).negate() : -alphaReal);

                computeSpmv(queue, currentSpmv, n, mPtr, mInd, mVal, mS, mT);
                Object tDotS = gpuDot(queue, currentDot, mT, mS, mTemp, n, isComplex);
                Object tDotT = gpuDot(queue, currentDot, mT, mT, mTemp, n, isComplex);
                
                if (isComplex) {
                    omega = ((Complex)tDotS).divide((Complex)tDotT);
                } else {
                    omegaReal = (Double)tDotS / (Double)tDotT;
                }

                gpuSaxpy(queue, currentSaxpy, n, mP, mX, isComplex ? alpha : alphaReal);
                gpuSaxpy(queue, currentSaxpy, n, mS, mX, isComplex ? omega : omegaReal);

                clEnqueueCopyBuffer(queue, mS, mR, 0, 0, (long)Sizeof.cl_double * elemSize * n, 0, null, null);
                gpuSaxpy(queue, currentSaxpy, n, mT, mR, isComplex ? ((Complex)omega).negate() : -omegaReal);

                Object resNorm = gpuDot(queue, currentDot, mR, mR, mTemp, n, isComplex);
                double normVal = isComplex ? ((Complex)resNorm).abs().doubleValue() : (Double)resNorm;
                if (normVal < tol * tol) break;
            }
            
            double[] res = new double[n * elemSize];
            clEnqueueReadBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * elemSize * n, Pointer.to(res), 0, null, null);
            return isComplex ? fromComplexDoubleArray(res, sa.getScalarRing()) : fromDoubleArray(res, sa.getScalarRing());
        } catch (Exception e) {
            throw new RuntimeException("OpenCL BiCGSTAB failure", e);
        }
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Double Backend not available");
        // GMRES is real only for now in this backend, falling back to super for complex
        if (isComplex(a)) return SparseLinearAlgebraProvider.super.gmres(a, b, x0, tolerance, maxIterations, restarts);
        
        SparseMatrix<E> sa = ensureSparse(a);
        double tol = getRealValue(tolerance);
        int n = sa.rows();
        
        double[] bArr = toDoubleVec(b);
        int nnz = sa.getNnz();
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        double[] vals = new double[nnz];
        Object[] valsObj = sa.getValues();
        for (int i = 0; i < nnz; i++) vals[i] = getRealValue((E) valsObj[i]);

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();

            cl_mem mPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(vals), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(bArr), null), CL::clReleaseMemObject);
            cl_mem mX = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            
            if (x0 != null) {
                clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(toDoubleVec(x0)), 0, null, null);
            } else {
                clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(new double[n]), 0, null, null);
            }

            cl_mem mR = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mAx = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            
            cl_mem[] mV = new cl_mem[maxIterations + 1];
            for (int i = 0; i < mV.length; i++) {
                mV[i] = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            }

            for (int restart = 0; restart < restarts; restart++) {
                computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mX, mAx);
                clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                gpuSaxpy(queue, saxpyKernel, n, mAx, mR, -1.0);

                double residualNorm = Math.sqrt((Double)gpuDot(queue, dotPartialKernel, mR, mR, mTemp, n, false));
                if (residualNorm < tol) break;

                gpuScale(queue, vecScaleKernel, mR, 1.0 / residualNorm, n, false);
                clEnqueueCopyBuffer(queue, mR, mV[0], 0, 0, (long)Sizeof.cl_double * n, 0, null, null);

                double[][] H = new double[maxIterations + 1][maxIterations];
                int jLimit = 0;
                for (int j = 0; j < maxIterations; j++) {
                    jLimit = j + 1;
                    computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mV[j], mV[j+1]);

                    for (int i = 0; i <= j; i++) {
                        H[i][j] = (Double)gpuDot(queue, dotPartialKernel, mV[i], mV[j+1], mTemp, n, false);
                        gpuSaxpy(queue, saxpyKernel, n, mV[i], mV[j+1], -H[i][j]);
                    }
                    H[j+1][j] = Math.sqrt((Double)gpuDot(queue, dotPartialKernel, mV[j+1], mV[j+1], mTemp, n, false));
                    if (H[j+1][j] < 1e-18) break; 
                    gpuScale(queue, vecScaleKernel, mV[j+1], 1.0 / H[j+1][j], n, false);
                }

                double[] y = solveHessenbergSystem(H, residualNorm, jLimit);
                for (int i = 0; i < y.length; i++) {
                    gpuSaxpy(queue, saxpyKernel, n, mV[i], mX, y[i]);
                }
                
                computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mX, mAx);
                clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                gpuSaxpy(queue, saxpyKernel, n, mAx, mR, -1.0);
                if (Math.sqrt((Double)gpuDot(queue, dotPartialKernel, mR, mR, mTemp, n, false)) < tol) break;
            }

            double[] xRes = new double[n];
            clEnqueueReadBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(xRes), 0, null, null);
            return fromDoubleVec(xRes, sa.getScalarRing());
        } catch (Exception e) {
            throw new RuntimeException("OpenCL GMRES failure", e);
        }
    }

    private double[] solveHessenbergSystem(double[][] H, double beta, int size) {
        double[] g = new double[size + 1];
        g[0] = beta;
        double[] cs = new double[size];
        double[] sn = new double[size];
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < i; k++) {
                double h1 = H[k][i];
                double h2 = H[k+1][i];
                H[k][i] = cs[k] * h1 + sn[k] * h2;
                H[k+1][i] = -sn[k] * h1 + cs[k] * h2;
            }
            double h1 = H[i][i];
            double h2 = H[i+1][i];
            if (h2 == 0) { cs[i] = 1.0; sn[i] = 0.0; }
            else if (Math.abs(h2) > Math.abs(h1)) {
                double t = h1 / h2; sn[i] = 1.0 / Math.sqrt(1.0 + t * t); cs[i] = sn[i] * t;
            } else {
                double t = h2 / h1; cs[i] = 1.0 / Math.sqrt(1.0 + t * t); sn[i] = cs[i] * t;
            }
            H[i][i] = cs[i] * h1 + sn[i] * h2;
            H[i+1][i] = 0.0;
            double g1 = g[i];
            g[i] = cs[i] * g1;
            g[i+1] = -sn[i] * g1;
        }
        double[] y = new double[size];
        for (int i = size - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < size; j++) sum += H[i][j] * y[j];
            y[i] = (g[i] - sum) / H[i][i];
        }
        return y;
    }

    // Helper methods for kernels
    private void computeSpmv(cl_command_queue queue, cl_kernel k, int n, cl_mem mPtr, cl_mem mInd, cl_mem mVal, cl_mem mX, cl_mem mY) {
        clSetKernelArg(k, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mPtr));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mInd));
        clSetKernelArg(k, 3, Sizeof.cl_mem, Pointer.to(mVal));
        clSetKernelArg(k, 4, Sizeof.cl_mem, Pointer.to(mX));
        clSetKernelArg(k, 5, Sizeof.cl_mem, Pointer.to(mY));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private void gpuSaxpy(cl_command_queue queue, cl_kernel k, int n, cl_mem mX, cl_mem mY, Object alpha) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mX));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mY));
        if (alpha instanceof Complex c) {
            clSetKernelArg(k, 2, Sizeof.cl_double * 2, Pointer.to(new double[]{c.real(), c.imaginary()}));
        } else {
            clSetKernelArg(k, 2, Sizeof.cl_double, Pointer.to(new double[]{((Number)alpha).doubleValue()}));
        }
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
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
        clSetKernelArg(k, 4, (long) localSize * Sizeof.cl_double * elemSize, null);
        
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{(long) numGroups * localSize}, new long[]{localSize}, 0, null, null);
        
        double[] partial = new double[numGroups * elemSize];
        clEnqueueReadBuffer(queue, mTemp, CL_TRUE, 0, (long)Sizeof.cl_double * elemSize * numGroups, Pointer.to(partial), 0, null, null);
        
        if (isComplex) {
            double resR = 0, resI = 0;
            for (int i = 0; i < numGroups; i++) {
                resR += partial[i * 2];
                resI += partial[i * 2 + 1];
            }
            return Complex.of(resR, resI);
        } else {
            double sum = 0; for(double d : partial) sum += d;
            return sum;
        }
    }

    private void gpuScale(cl_command_queue queue, cl_kernel k, cl_mem a, Object s, int n, boolean isComplex) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(a));
        if (isComplex) {
            Complex c = (Complex) s;
            clSetKernelArg(k, 1, Sizeof.cl_double * 2, Pointer.to(new double[]{c.real(), c.imaginary()}));
        } else {
            clSetKernelArg(k, 1, Sizeof.cl_double, Pointer.to(new double[]{((Number)s).doubleValue()}));
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

    private SparseMatrix<E> ensureSparse(Matrix<E> a) {
        if (a instanceof SparseMatrix) return (SparseMatrix<E>) a;
        Ring<E> r = a.getScalarRing();
        SparseMatrix<E> s = new SparseMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(a.rows(), a.cols(), r.zero()), (LinearAlgebraProvider<E>) this, r);
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) {
                E val = a.get(i, j);
                if (!val.equals(r.zero())) s.set(i, j, val);
            }
        }
        return s;
    }

    private double[] toDoubleVec(Vector<E> v) {
        double[] data = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) data[i] = getRealValue(v.get(i));
        return data;
    }

    private Vector<E> fromDoubleVec(double[] data, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(data.length, ring.zero());
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0.0) storage.set(i, (E) RealDouble.of(data[i]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, (LinearAlgebraProvider<E>) this, ring);
    }

    private Vector<E> fromDoubleArray(double[] data, Ring<E> ring) { return fromDoubleVec(data, ring); }

    private double getRealValue(E val) {
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof Real r) return r.doubleValue();
        if (val instanceof Complex c) return c.real();
        return 0.0;
    }

    private Complex getComplex(Object val) {
        if (val instanceof Complex c) return c;
        if (val instanceof Number n) return Complex.of(RealDouble.of(n.doubleValue()));
        if (val instanceof Real r) return Complex.of(RealDouble.of(r.doubleValue()));
        throw new IllegalArgumentException("Cannot convert to complex: " + val.getClass());
    }

    private double[] toComplexDoubleArray(SparseMatrix<E> m) {
        int nnz = m.getNnz();
        double[] data = new double[nnz * 2];
        Object[] vals = m.getValues();
        for (int i = 0; i < nnz; i++) {
            E val = (E) vals[i];
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

    private Vector<E> fromComplexDoubleArray(double[] data, Ring<E> ring) {
        int n = data.length / 2;
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n);
        for (int i = 0; i < n; i++) {
            elements[i] = (E) Complex.of(data[i * 2], data[i * 2 + 1]);
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(elements), (LinearAlgebraProvider<E>) this, ring);
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
            if (saxpyKernel != null) clReleaseKernel(saxpyKernel);
            if (complexSaxpyKernel != null) clReleaseKernel(complexSaxpyKernel);
            if (dotPartialKernel != null) clReleaseKernel(dotPartialKernel);
            if (complexDotPartialKernel != null) clReleaseKernel(complexDotPartialKernel);
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

    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }
}
