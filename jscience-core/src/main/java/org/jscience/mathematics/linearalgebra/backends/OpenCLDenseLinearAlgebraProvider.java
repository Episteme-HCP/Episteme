/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.linearalgebra.backends;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.opencl.OpenCLBackend;
import org.jocl.*;

/**
 * OpenCL Linear Algebra Provider (Dense).
 * <p>
 * Delegates to CPU for now, but setup for JOCL implementation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpenCLDenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private static final OpenCLBackend backend = new OpenCLBackend();

    public OpenCLDenseLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
    }

    @Override
    public boolean isAvailable() {
        return backend.isAvailable();
    }

    public static boolean isOpenCLAvailable() {
        return new OpenCLBackend().isAvailable();
    }

    @Override
    public String getName() {
        return "OpenCL (Dense)";
    }

    @Override
    public ExecutionContext createContext() {
        return backend.createContext();
    }

    @Override
    public int getPriority() {
        return 10;
    }

    private static final String KERNEL_SRC = "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
            "__kernel void vectorAdd(__global const double *a, __global const double *b, __global double *c, const int n) {"
            +
            "    int i = get_global_id(0);" +
            "    if (i < n) {" +
            "        c[i] = a[i] + b[i];" +
            "    }" +
            "}" +
            "\n" +
            "__kernel void matrixMultiply(__global const double *a, __global const double *b, __global double *c, const int m, const int n, const int k) {"
            +
            "    int row = get_global_id(1);" +
            "    int col = get_global_id(0);" +
            "    if (row < m && col < n) {" +
            "        double sum = 0.0;" +
            "        for (int i = 0; i < k; i++) {" +
            "            sum += a[row * k + i] * b[i * n + col];" +
            "        }" +
            "        c[row * n + col] = sum;" +
            "    }" +
            "}";

    private static cl_program program;
    private static cl_kernel kernelVectorAdd;
    private static cl_kernel kernelMatrixMultiply;

    private static boolean initialized = false;
    private static boolean usable = false;

    private synchronized void ensureInitialized() {
        if (!initialized) {
            initialized = true;
            if (backend.isAvailable()) {
                try {
                    // Run a tiny self-test
                    double[] a = { 1.0 };
                    double[] b = { 2.0 };
                    double[] c = { 0.0 };
                    executeVectorAdd(a, b, c, 1);
                    if (Math.abs(c[0] - 3.0) < 0.0001) {
                        usable = true;
                    } else {
                        System.err.println("OpenCL self-test failed: 1+2=" + c[0] + ". Disabling OpenCL.");
                    }
                } catch (Throwable t) {
                    System.err.println("OpenCL initialization failed: " + t.getMessage());
                    t.printStackTrace();
                }
            }
        }
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        ensureInitialized();
        if (!usable || !(field.zero() instanceof org.jscience.mathematics.numbers.real.Real)) {
            return cpuProvider.add(a, b);
        }

        try {
            int n = a.dimension();
            if (n != b.dimension()) {
                throw new IllegalArgumentException("Vector dimension mismatch");
            }

            double[] dataA = toDoubleArray(a);
            double[] dataB = toDoubleArray(b);
            double[] dataC = new double[n];

            executeVectorAdd(dataA, dataB, dataC, n);

            return toVector(dataC);
        } catch (Exception e) {
            // Fallback on error (e.g. OOM, OpenCL error)
            e.printStackTrace();
            return cpuProvider.add(a, b);
        }
    }

    private void executeVectorAdd(double[] a, double[] b, double[] c, int n) {
        org.jscience.technical.backend.opencl.OpenCLExecutionContext ctx = (org.jscience.technical.backend.opencl.OpenCLExecutionContext) createContext();

        cl_context clContext = ctx.getContext();
        cl_command_queue commandQueue = ctx.getCommandQueue();

        synchronized (OpenCLDenseLinearAlgebraProvider.class) {
            if (program == null) {
                program = org.jocl.CL.clCreateProgramWithSource(clContext, 1, new String[] { KERNEL_SRC }, null, null);
                org.jocl.CL.clBuildProgram(program, 0, null, null, null, null);
                org.jocl.CL.clBuildProgram(program, 0, null, null, null, null);
                kernelVectorAdd = org.jocl.CL.clCreateKernel(program, "vectorAdd", null);
                kernelMatrixMultiply = org.jocl.CL.clCreateKernel(program, "matrixMultiply", null);
            }
        }

        cl_mem memA = org.jocl.CL.clCreateBuffer(clContext,
                org.jocl.CL.CL_MEM_READ_ONLY | org.jocl.CL.CL_MEM_COPY_HOST_PTR,
                org.jocl.Sizeof.cl_double * n, org.jocl.Pointer.to(a), null);
        cl_mem memB = org.jocl.CL.clCreateBuffer(clContext,
                org.jocl.CL.CL_MEM_READ_ONLY | org.jocl.CL.CL_MEM_COPY_HOST_PTR,
                org.jocl.Sizeof.cl_double * n, org.jocl.Pointer.to(b), null);
        cl_mem memC = org.jocl.CL.clCreateBuffer(clContext, org.jocl.CL.CL_MEM_WRITE_ONLY,
                org.jocl.Sizeof.cl_double * n, null, null);

        org.jocl.CL.clSetKernelArg(kernelVectorAdd, 0, org.jocl.Sizeof.cl_mem, org.jocl.Pointer.to(memA));
        org.jocl.CL.clSetKernelArg(kernelVectorAdd, 1, org.jocl.Sizeof.cl_mem, org.jocl.Pointer.to(memB));
        org.jocl.CL.clSetKernelArg(kernelVectorAdd, 2, org.jocl.Sizeof.cl_mem, org.jocl.Pointer.to(memC));
        org.jocl.CL.clSetKernelArg(kernelVectorAdd, 3, org.jocl.Sizeof.cl_int, org.jocl.Pointer.to(new int[] { n }));

        long global_work_size[] = new long[] { n };
        org.jocl.CL.clEnqueueNDRangeKernel(commandQueue, kernelVectorAdd, 1, null, global_work_size, null, 0, null,
                null);

        org.jocl.CL.clEnqueueReadBuffer(commandQueue, memC, org.jocl.CL.CL_TRUE, 0,
                org.jocl.Sizeof.cl_double * n, org.jocl.Pointer.to(c), 0, null, null);

        org.jocl.CL.clReleaseMemObject(memA);
        org.jocl.CL.clReleaseMemObject(memB);
        org.jocl.CL.clReleaseMemObject(memC);
    }

    private double[] toDoubleArray(Vector<E> v) {
        int n = v.dimension();
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) {
            org.jscience.mathematics.numbers.real.Real r = (org.jscience.mathematics.numbers.real.Real) v.get(i);
            arr[i] = r.doubleValue();
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    private Vector<E> toVector(double[] data) {
        java.util.List<org.jscience.mathematics.numbers.real.Real> list = new java.util.ArrayList<>(data.length);
        for (double d : data) {
            list.add(org.jscience.mathematics.numbers.real.Real.of(d));
        }
        // Assuming DenseVector supports constructor from List.
        // We might need to look at how to create a Vector<E>.
        // Using cpuProvider to create trivial vector or densevector directly.
        // Actually, DenseVector<Real> constructor takes List<Real>.
        return (Vector<E>) new org.jscience.mathematics.linearalgebra.vectors.DenseVector<>(list,
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    // Delegate remaining operations to CPU for this stub
    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        return cpuProvider.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return cpuProvider.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        ensureInitialized();
        if (!usable || !(field.zero() instanceof org.jscience.mathematics.numbers.real.Real)) {
            return cpuProvider.multiply(a, b);
        }

        try {
            int m = a.rows();
            int k = a.cols();
            int n = b.cols();

            if (k != b.rows()) {
                throw new IllegalArgumentException("Matrix dimension mismatch");
            }

            // Only use OpenCL for "large enough" matrices to amortize overhead
            if (m * n * k < 1_000_000) {
                return cpuProvider.multiply(a, b);
            }

            double[] dataA = null;
            double[] dataB = null;
            org.jocl.Pointer ptrA;
            org.jocl.Pointer ptrB;

            if (a instanceof org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
                org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix rdm = (org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix) a;
                if (rdm.isDirect()) {
                    ptrA = org.jocl.Pointer.to(rdm.getBuffer());
                } else {
                    dataA = toDoubleArray(a);
                    ptrA = org.jocl.Pointer.to(dataA);
                }
            } else {
                dataA = toDoubleArray(a);
                ptrA = org.jocl.Pointer.to(dataA);
            }

            if (b instanceof org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix) {
                org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix rdm = (org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix) b;
                if (rdm.isDirect()) {
                    ptrB = org.jocl.Pointer.to(rdm.getBuffer());
                } else {
                    dataB = toDoubleArray(b);
                    ptrB = org.jocl.Pointer.to(dataB);
                }
            } else {
                dataB = toDoubleArray(b);
                ptrB = org.jocl.Pointer.to(dataB);
            }

            // Allocate result directly in off-heap memory
            org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix resultC = org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix
                    .direct(m, n);
            org.jocl.Pointer ptrC = org.jocl.Pointer.to(resultC.getBuffer());

            executeMatrixMultiply(ptrA, ptrB, ptrC, m, n, k);

            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) resultC;
            return res;
        } catch (

        Exception e) {
            e.printStackTrace();
            return cpuProvider.multiply(a, b);
        }
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] arr = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                org.jscience.mathematics.numbers.real.Real r = (org.jscience.mathematics.numbers.real.Real) m.get(i, j);
                arr[i * cols + j] = r.doubleValue();
            }
        }
        return arr;
    }

    private void executeMatrixMultiply(org.jocl.Pointer ptrA, org.jocl.Pointer ptrB, org.jocl.Pointer ptrC, int m,
            int n, int k) {
        org.jscience.technical.backend.opencl.OpenCLExecutionContext ctx = (org.jscience.technical.backend.opencl.OpenCLExecutionContext) createContext();
        cl_context clContext = ctx.getContext();
        cl_command_queue commandQueue = ctx.getCommandQueue();

        synchronized (OpenCLDenseLinearAlgebraProvider.class) {
            if (kernelMatrixMultiply == null) {
                // Should have been initialized but just in case
                program = org.jocl.CL.clCreateProgramWithSource(clContext, 1, new String[] { KERNEL_SRC }, null, null);
                org.jocl.CL.clBuildProgram(program, 0, null, null, null, null);
                kernelVectorAdd = org.jocl.CL.clCreateKernel(program, "vectorAdd", null);
                kernelMatrixMultiply = org.jocl.CL.clCreateKernel(program, "matrixMultiply", null);
            }
        }

        // Use CL_MEM_USE_HOST_PTR to try to use the DirectBuffer in-place if possible
        // (Zero-Copy)
        long startTransfer = System.nanoTime();
        cl_mem memA = org.jocl.CL.clCreateBuffer(clContext,
                org.jocl.CL.CL_MEM_READ_ONLY | org.jocl.CL.CL_MEM_USE_HOST_PTR,
                org.jocl.Sizeof.cl_double * m * k, ptrA, null);
        cl_mem memB = org.jocl.CL.clCreateBuffer(clContext,
                org.jocl.CL.CL_MEM_READ_ONLY | org.jocl.CL.CL_MEM_USE_HOST_PTR,
                org.jocl.Sizeof.cl_double * k * n, ptrB, null);
        cl_mem memC = org.jocl.CL.clCreateBuffer(clContext,
                org.jocl.CL.CL_MEM_WRITE_ONLY | org.jocl.CL.CL_MEM_USE_HOST_PTR,
                org.jocl.Sizeof.cl_double * m * n, ptrC, null);
        org.jscience.util.PerformanceLogger.log("OpenCL:BufferCreation", System.nanoTime() - startTransfer);

        org.jocl.CL.clSetKernelArg(kernelMatrixMultiply, 0, org.jocl.Sizeof.cl_mem, org.jocl.Pointer.to(memA));
        org.jocl.CL.clSetKernelArg(kernelMatrixMultiply, 1, org.jocl.Sizeof.cl_mem, org.jocl.Pointer.to(memB));
        org.jocl.CL.clSetKernelArg(kernelMatrixMultiply, 2, org.jocl.Sizeof.cl_mem, org.jocl.Pointer.to(memC));
        org.jocl.CL.clSetKernelArg(kernelMatrixMultiply, 3, org.jocl.Sizeof.cl_int,
                org.jocl.Pointer.to(new int[] { m }));
        org.jocl.CL.clSetKernelArg(kernelMatrixMultiply, 4, org.jocl.Sizeof.cl_int,
                org.jocl.Pointer.to(new int[] { n }));
        org.jocl.CL.clSetKernelArg(kernelMatrixMultiply, 5, org.jocl.Sizeof.cl_int,
                org.jocl.Pointer.to(new int[] { k }));

        long startExec = System.nanoTime();
        long global_work_size[] = new long[] { n, m }; // col, row
        org.jocl.CL.clEnqueueNDRangeKernel(commandQueue, kernelMatrixMultiply, 2, null, global_work_size, null, 0, null,
                null);
        // Ensure kernel is done for accurate timing (synchronous wait)
        org.jocl.CL.clFinish(commandQueue);
        org.jscience.util.PerformanceLogger.log("OpenCL:KernelExec", System.nanoTime() - startExec);

        long startRead = System.nanoTime();
        // Read back (blocking) - for USE_HOST_PTR this should just sync, but we call
        // ReadBuffer to be safe/portable
        // Actually, if we use USE_HOST_PTR, the GPU writes directly to system memory
        // (over PCIe or shared).
        // A simple Finish or Map/Unmap is preferred, but ReadBuffer works.
        org.jocl.CL.clEnqueueReadBuffer(commandQueue, memC, org.jocl.CL.CL_TRUE, 0,
                org.jocl.Sizeof.cl_double * m * n, ptrC, 0, null, null);
        org.jscience.util.PerformanceLogger.log("OpenCL:ReadBack", System.nanoTime() - startRead);

        org.jocl.CL.clReleaseMemObject(memA);
        org.jocl.CL.clReleaseMemObject(memB);
        org.jocl.CL.clReleaseMemObject(memC);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return cpuProvider.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        return cpuProvider.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        return cpuProvider.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return cpuProvider.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return cpuProvider.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return cpuProvider.scale(scalar, a);
    }
}


