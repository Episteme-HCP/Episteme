/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.solvers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.nativ.technical.backend.gpu.opencl.OpenCLExecutionContext;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.jocl.*;
import static org.jocl.CL.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * Native-Safe OpenCL implementation of the Generalized Minimal Residual (GMRES) method.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class NativeOpenCLGMRESSolver {
    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLGMRESSolver.class);

    private final OpenCLExecutionContext context;
    private final cl_program sparseProgram;
    private final cl_program denseProgram;
    private final Function<double[], Vector<Real>> vectorFactory;

    public NativeOpenCLGMRESSolver(OpenCLExecutionContext context, cl_program sparseProgram, cl_program denseProgram, Function<double[], Vector<Real>> vectorFactory) {
        this.context = context;
        this.sparseProgram = sparseProgram;
        this.denseProgram = denseProgram;
        this.vectorFactory = vectorFactory;
    }

    public Vector<Real> solve(SparseMatrix<Real> A, Vector<Real> b, Vector<Real> x0, double tol, int maxIter, int restarts) {
        int n = b.dimension();
        int nnz = A.getNnz();
        int[] rowPtr = A.getRowPointers();
        int[] colIdx = A.getColIndices();
        double[] vals = new double[nnz];
        Object[] valsObj = A.getValues();
        for (int i = 0; i < nnz; i++) vals[i] = ((Real) valsObj[i]).doubleValue();

        double[] bArr = new double[n];
        for (int i = 0; i < n; i++) bArr[i] = b.get(i).doubleValue();

        cl_context clCtx = context.getContext();
        cl_command_queue queue = context.getCommandQueue();

        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_mem mPtr = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(vals), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(bArr), null), CL::clReleaseMemObject);
            cl_mem mX = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            
            if (x0 != null) {
                double[] x0Arr = new double[n];
                for (int i = 0; i < n; i++) x0Arr[i] = x0.get(i).doubleValue();
                clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(x0Arr), 0, null, null);
            } else {
                clEnqueueWriteBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(new double[n]), 0, null, null);
            }

            cl_mem mR = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mAx = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            
            cl_mem[] mV = new cl_mem[maxIter + 1];
            for (int i = 0; i < mV.length; i++) {
                mV[i] = tracker.track(clCreateBuffer(clCtx, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            }

            cl_kernel kSpmv = tracker.track(clCreateKernel(sparseProgram, "spmv_csr", null), CL::clReleaseKernel);
            cl_kernel kSaxpy = tracker.track(clCreateKernel(denseProgram, "saxpy", null), CL::clReleaseKernel);
            cl_kernel kDot = tracker.track(clCreateKernel(denseProgram, "dot_partial", null), CL::clReleaseKernel);
            cl_kernel kScale = tracker.track(clCreateKernel(denseProgram, "vectorScalarMultiply", null), CL::clReleaseKernel);

            for (int restart = 0; restart < restarts; restart++) {
                // r = b - Ax
                computeSpmv(queue, kSpmv, n, mPtr, mInd, mVal, mX, mAx);
                clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                computeSaxpy(queue, kSaxpy, n, mAx, mR, -1.0);

                double residualNorm = Math.sqrt(computeDot(queue, kDot, mR, mR, mTemp, n));
                if (residualNorm < tol) break;

                computeScale(queue, kScale, n, mR, 1.0 / residualNorm);
                clEnqueueCopyBuffer(queue, mR, mV[0], 0, 0, (long)Sizeof.cl_double * n, 0, null, null);

                double[][] H = new double[maxIter + 1][maxIter];
                int j = 0;
                for (j = 0; j < maxIter; j++) {
                    // w = A * V[j]
                    computeSpmv(queue, kSpmv, n, mPtr, mInd, mVal, mV[j], mV[j+1]);

                    for (int i = 0; i <= j; i++) {
                        H[i][j] = computeDot(queue, kDot, mV[i], mV[j+1], mTemp, n);
                        computeSaxpy(queue, kSaxpy, n, mV[i], mV[j+1], -H[i][j]);
                    }
                    H[j+1][j] = Math.sqrt(computeDot(queue, kDot, mV[j+1], mV[j+1], mTemp, n));
                    if (H[j+1][j] < 1e-18) break; 
                    computeScale(queue, kScale, n, mV[j+1], 1.0 / H[j+1][j]);
                }

                double[] y = solveHessenbergSystem(H, residualNorm, j);
                for (int i = 0; i < y.length; i++) {
                    computeSaxpy(queue, kSaxpy, n, mV[i], mX, y[i]);
                }
                
                // Final residual check after restart
                computeSpmv(queue, kSpmv, n, mPtr, mInd, mVal, mX, mAx);
                clEnqueueCopyBuffer(queue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                computeSaxpy(queue, kSaxpy, n, mAx, mR, -1.0);
                if (Math.sqrt(computeDot(queue, kDot, mR, mR, mTemp, n)) < tol) break;
            }

            double[] xRes = new double[n];
            clEnqueueReadBuffer(queue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(xRes), 0, null, null);
            return vectorFactory.apply(xRes);

        } catch (Exception e) {
            logger.error("Native OpenCL GMRES failed: {}", e.getMessage());
            throw new RuntimeException("GMRES solver failure", e);
        }
    }

    private void computeSpmv(cl_command_queue queue, cl_kernel kSpmv, int n, cl_mem mPtr, cl_mem mInd, cl_mem mVal, cl_mem mX, cl_mem mY) {
        clSetKernelArg(kSpmv, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clSetKernelArg(kSpmv, 1, Sizeof.cl_mem, Pointer.to(mPtr));
        clSetKernelArg(kSpmv, 2, Sizeof.cl_mem, Pointer.to(mInd));
        clSetKernelArg(kSpmv, 3, Sizeof.cl_mem, Pointer.to(mVal));
        clSetKernelArg(kSpmv, 4, Sizeof.cl_mem, Pointer.to(mX));
        clSetKernelArg(kSpmv, 5, Sizeof.cl_mem, Pointer.to(mY));
        clEnqueueNDRangeKernel(queue, kSpmv, 1, null, new long[]{n}, null, 0, null, null);
    }

    private void computeSaxpy(cl_command_queue queue, cl_kernel kSaxpy, int n, cl_mem mX, cl_mem mY, double alpha) {
        clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mX));
        clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mY));
        clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{alpha}));
        clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);
    }

    private double computeDot(cl_command_queue queue, cl_kernel kDot, cl_mem a, cl_mem b, cl_mem mTemp, int n) {
        clSetKernelArg(kDot, 0, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(kDot, 1, Sizeof.cl_mem, Pointer.to(b));
        clSetKernelArg(kDot, 2, Sizeof.cl_mem, Pointer.to(mTemp));
        clSetKernelArg(kDot, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, kDot, 1, null, new long[]{n}, null, 0, null, null);
        double[] partial = new double[n];
        clEnqueueReadBuffer(queue, mTemp, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(partial), 0, null, null);
        double sum = 0; for(double d : partial) sum += d;
        return sum;
    }

    private void computeScale(cl_command_queue queue, cl_kernel kScale, int n, cl_mem a, double s) {
        clSetKernelArg(kScale, 0, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(kScale, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
        clSetKernelArg(kScale, 2, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(kScale, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(queue, kScale, 1, null, new long[]{n}, null, 0, null, null);
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
}
