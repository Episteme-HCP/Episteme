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

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLSparseLinearAlgebraDoubleBackend.class);
    
    private cl_program program;
    private cl_kernel spmvKernel;
    private cl_kernel vecAddKernel;
    private cl_kernel vecScaleKernel;
    private cl_kernel saxpyKernel;
    private cl_kernel dotPartialKernel;
    
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
            vecAddKernel = tryCreateKernel(program, "vec_add");
            vecScaleKernel = tryCreateKernel(program, "vec_scale");
            saxpyKernel = tryCreateKernel(program, "saxpy");
            dotPartialKernel = tryCreateKernel(program, "dot_partial");

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

    @Override
    public boolean isCompatible(Ring<?> ring) {
        Object zero = ring.zero();
        return zero instanceof RealDouble || zero instanceof Complex || (zero instanceof Real r && !r.isFast());
    }

    @Override
    public String getId() { return "opencl-sparse-double"; }

    @Override
    public String getName() { return "Native OpenCL Sparse Double Backend"; }

    @Override
    public int getPriority() { return 105; }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Double Backend not available");
        
        if (!(a instanceof SparseMatrix)) {
             return SparseLinearAlgebraProvider.super.multiply(a, x);
        }

        SparseMatrix<E> sa = (SparseMatrix<E>) a;
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
            
            clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(memPtr));
            clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(memInd));
            clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(memVal));
            clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(memX));
            clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(memY));
            
            clEnqueueNDRangeKernel(queue, spmvKernel, 1, null, new long[]{rows}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memY, CL_TRUE, 0, (long)Sizeof.cl_double * rows, Pointer.to(yData), 0, null, null);
            
            return fromDoubleArray(yData, sa.getScalarRing());
        }
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Double Backend not available");
        
        if (a instanceof SparseMatrix) {
            try {
                // Use BiCGSTAB as default for general sparse
                return bicgstab(a, b, null, (E) RealDouble.of(1e-10), 1000);
            } catch (Exception e) {
                logger.warn("BiCGSTAB failed, falling back to CG: {}", e.getMessage());
                return conjugateGradient(a, b, null, (E) RealDouble.of(1e-10), 1000);
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
        double[] values = new double[nnz];
        Object[] valsObj = sa.getValues();
        for (int i = 0; i < nnz; i++) values[i] = getRealValue((E) valsObj[i]);
        double[] bData = toDoubleVec(b);

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();

            cl_mem mPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(bData), null), CL::clReleaseMemObject);
            
            cl_mem mX = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            if (x0 != null) {
                 double[] x0Data = toDoubleVec(x0);
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(x0Data), 0, null, null);
            } else {
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(new double[n]), 0, null, null);
            }
            
            cl_mem mR = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mP = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mAp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);

            // r = b - Ax (initial)
            computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mX, mAp);
            clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
            computeSaxpy(queue, saxpyKernel, n, mAp, mR, -1.0);
            clEnqueueCopyBuffer(queue, mR, mP, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
            
            double rsOld = gpuDot(queue, dotPartialKernel, mR, mR, mTemp, n);

            for (int i = 0; i < maxIterations; i++) {
                computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mP, mAp);
                double pAp = gpuDot(queue, dotPartialKernel, mP, mAp, mTemp, n);
                double alpha = rsOld / pAp;

                computeSaxpy(queue, saxpyKernel, n, mP, mX, alpha);
                computeSaxpy(queue, saxpyKernel, n, mAp, mR, -alpha);

                double rsNew = gpuDot(queue, dotPartialKernel, mR, mR, mTemp, n);
                if (Math.sqrt(rsNew) < tol) break;

                double beta = rsNew / rsOld;
                gpuScale(queue, vecScaleKernel, mP, beta, n);
                gpuAdd(queue, vecAddKernel, mR, mP, n); 
                rsOld = rsNew;
            }

            double[] xRes = new double[n];
            clEnqueueReadBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(xRes), 0, null, null);
            return fromDoubleVec(xRes, sa.getScalarRing());
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
        double[] values = new double[nnz];
        Object[] valsObj = sa.getValues();
        for (int i = 0; i < nnz; i++) values[i] = getRealValue((E) valsObj[i]);
        double[] bData = toDoubleVec(b);

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();

            cl_mem mPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(bData), null), CL::clReleaseMemObject);
            
            cl_mem mX = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            if (x0 != null) {
                 double[] x0Data = toDoubleVec(x0);
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(x0Data), 0, null, null);
            } else {
                 clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(new double[n]), 0, null, null);
            }
            
            cl_mem mR = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mR0 = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mP = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mV = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mS = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mT = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(context, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            
            // r = b - Ax
            computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mX, mV);
            clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
            computeSaxpy(queue, saxpyKernel, n, mV, mR, -1.0);
            clEnqueueCopyBuffer(queue, mR, mR0, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
            
            double rho = 1, alpha = 1, omega = 1;
            
            for (int i = 0; i < maxIterations; i++) {
                double rhoNext = gpuDot(queue, dotPartialKernel, mR0, mR, mTemp, n);
                double beta = (rhoNext / rho) * (alpha / omega);
                rho = rhoNext;

                computeSaxpy(queue, saxpyKernel, n, mV, mP, -omega);
                gpuScale(queue, vecScaleKernel, mP, beta, n);
                gpuAdd(queue, vecAddKernel, mR, mP, n);

                computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mP, mV);
                alpha = rho / gpuDot(queue, dotPartialKernel, mR0, mV, mTemp, n);

                clEnqueueCopyBuffer(queue, mR, mS, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                computeSaxpy(queue, saxpyKernel, n, mV, mS, -alpha);

                if (Math.sqrt(gpuDot(queue, dotPartialKernel, mS, mS, mTemp, n)) < tol) {
                    computeSaxpy(queue, saxpyKernel, n, mP, mX, alpha);
                    break;
                }

                computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mS, mT);
                omega = gpuDot(queue, dotPartialKernel, mS, mT, mTemp, n) / gpuDot(queue, dotPartialKernel, mT, mT, mTemp, n);

                computeSaxpy(queue, saxpyKernel, n, mP, mX, alpha);
                computeSaxpy(queue, saxpyKernel, n, mS, mX, omega);

                clEnqueueCopyBuffer(queue, mS, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                computeSaxpy(queue, saxpyKernel, n, mT, mR, -omega);

                if (Math.sqrt(gpuDot(queue, dotPartialKernel, mR, mR, mTemp, n)) < tol) break;
            }

            double[] xRes = new double[n];
            clEnqueueReadBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(xRes), 0, null, null);
            return fromDoubleVec(xRes, sa.getScalarRing());
        } catch (Exception e) {
            throw new RuntimeException("OpenCL BiCGSTAB failure", e);
        }
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Double Backend not available");
        SparseMatrix<E> sa = ensureSparse(a);
        double tol = getRealValue(tolerance);
        int n = sa.rows();
        
        // Ported GMRES logic
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
                computeSaxpy(queue, saxpyKernel, n, mAx, mR, -1.0);

                double residualNorm = Math.sqrt(gpuDot(queue, dotPartialKernel, mR, mR, mTemp, n));
                if (residualNorm < tol) break;

                gpuScale(queue, vecScaleKernel, mR, 1.0 / residualNorm, n);
                clEnqueueCopyBuffer(queue, mR, mV[0], 0, 0, (long)Sizeof.cl_double * n, 0, null, null);

                double[][] H = new double[maxIterations + 1][maxIterations];
                int jLimit = 0;
                for (int j = 0; j < maxIterations; j++) {
                    jLimit = j + 1;
                    computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mV[j], mV[j+1]);

                    for (int i = 0; i <= j; i++) {
                        H[i][j] = gpuDot(queue, dotPartialKernel, mV[i], mV[j+1], mTemp, n);
                        computeSaxpy(queue, saxpyKernel, n, mV[i], mV[j+1], -H[i][j]);
                    }
                    H[j+1][j] = Math.sqrt(gpuDot(queue, dotPartialKernel, mV[j+1], mV[j+1], mTemp, n));
                    if (H[j+1][j] < 1e-18) break; 
                    gpuScale(queue, vecScaleKernel, mV[j+1], 1.0 / H[j+1][j], n);
                }

                double[] y = solveHessenbergSystem(H, residualNorm, jLimit);
                for (int i = 0; i < y.length; i++) {
                    computeSaxpy(queue, saxpyKernel, n, mV[i], mX, y[i]);
                }
                
                computeSpmv(queue, spmvKernel, n, mPtr, mInd, mVal, mX, mAx);
                clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                computeSaxpy(queue, saxpyKernel, n, mAx, mR, -1.0);
                if (Math.sqrt(gpuDot(queue, dotPartialKernel, mR, mR, mTemp, n)) < tol) break;
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

    private void computeSaxpy(cl_command_queue queue, cl_kernel k, int n, cl_mem mX, cl_mem mY, double alpha) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mX));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mY));
        clSetKernelArg(k, 2, Sizeof.cl_double, Pointer.to(new double[]{alpha}));
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private double gpuDot(cl_command_queue queue, cl_kernel k, cl_mem a, cl_mem b, cl_mem mTemp, int n) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(b));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mTemp));
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
        double[] partial = new double[n];
        clEnqueueReadBuffer(queue, mTemp, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(partial), 0, null, null);
        double sum = 0; for(double d : partial) sum += d;
        return sum;
    }

    private void gpuScale(cl_command_queue queue, cl_kernel k, cl_mem a, double s, int n) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(k, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private void gpuAdd(cl_command_queue queue, cl_kernel k, cl_mem x, cl_mem y, int n) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(x));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(y));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(y));
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    private SparseMatrix<E> ensureSparse(Matrix<E> a) {
        if (a instanceof SparseMatrix) return (SparseMatrix<E>) a;
        // Optimization: create sparse from dense
        return SparseLinearAlgebraProvider.super.toSparse(a);
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
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, ring);
    }

    private Vector<E> fromDoubleArray(double[] data, Ring<E> ring) { return fromDoubleVec(data, ring); }

    private double getRealValue(E val) {
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof Real r) return r.doubleValue();
        if (val instanceof Complex c) return c.real();
        return 0.0;
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
            clReleaseKernel(spmvKernel);
            clReleaseKernel(vecAddKernel);
            clReleaseKernel(vecScaleKernel);
            clReleaseKernel(saxpyKernel);
            clReleaseKernel(dotPartialKernel);
            clReleaseProgram(program);
            program = null;
        }
    }

    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }
}
