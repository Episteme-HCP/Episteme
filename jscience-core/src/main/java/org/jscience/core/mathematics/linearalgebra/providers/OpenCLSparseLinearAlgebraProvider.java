/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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




package org.jscience.core.mathematics.linearalgebra.providers;


import org.jscience.core.mathematics.structures.rings.Field;

import org.jscience.core.mathematics.linearalgebra.Matrix;

import org.jscience.core.mathematics.linearalgebra.Vector;


import org.jscience.core.technical.backend.gpu.opencl.OpenCLBackend;

/**
 * OpenCL Linear Algebra Provider (Sparse).
 * <p>
 * Placeholder for sparse OpenCL implementation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
import org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import com.google.auto.service.AutoService;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jocl.*;
import static org.jocl.CL.*;
import java.util.logging.Logger;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLExecutionContext;

@AutoService({SparseLinearAlgebraProvider.class, AlgorithmProvider.class})
public class OpenCLSparseLinearAlgebraProvider<E> implements SparseLinearAlgebraProvider<E> {

    private static final Logger LOGGER = Logger.getLogger(OpenCLSparseLinearAlgebraProvider.class.getName());
    private final CPUSparseLinearAlgebraProvider<E> cpuProvider;
    private final OpenCLBackend backend = new OpenCLBackend();
    
    // CSR SpMV Kernel
    // Arguments: 
    // num_rows: int
    // ptr: int[] (row pointers, size num_rows + 1)
    // indices: int[] (column indices of non-zeros)
    // values: double[] (values of non-zeros)
    // x: double[] (input vector)
    // y: double[] (output vector result)
    private static final String KERNEL_SPMV = 
        "__kernel void spmv_csr(int num_rows, __global int* ptr, __global int* indices, __global double* values, __global double* x, __global double* y) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row < num_rows) {\n" +
        "        double dot = 0;\n" +
        "        int start = ptr[row];\n" +
        "        int end = ptr[row+1];\n" +
        "        for (int j = start; j < end; j++) {\n" +
        "            dot += values[j] * x[indices[j]];\n" +
        "        }\n" +
        "        y[row] = dot;\n" +
        "    }\n" +
        "}\n";

    private boolean initialized = false;
    private cl_kernel kernel;
    private cl_program program;

    @SuppressWarnings("unchecked")
    public OpenCLSparseLinearAlgebraProvider() {
        this((Field<E>) org.jscience.core.mathematics.sets.Reals.getInstance());
    }

    public OpenCLSparseLinearAlgebraProvider(Field<E> field) {
        this.cpuProvider = new CPUSparseLinearAlgebraProvider<>(field);
        // Only initialize basic state, JOCL init lazy
    }

    @Override
    public boolean isAvailable() {
        return backend.isAvailable();
    }
    
    private synchronized void initGPU() {
        if (initialized) return;
        if (!isAvailable()) throw new IllegalStateException("OpenCL not available");
        
        try {
            OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
            cl_context context = ctx.getContext();
            
            program = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SPMV}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            kernel = clCreateKernel(program, "spmv_csr", null);
            
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to init OpenCL SpMV", e);
        }
    }

    @Override
    public String getName() {
        return "Native OpenCL (Sparse CSR)";
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        // GPU Path only for Real numbers and significant size
        if (a.getScalarRing() instanceof org.jscience.core.mathematics.sets.Reals && a.rows() > 100) {
            try {
                return (Vector<E>) multiplyGPU((Matrix<Real>) a, (Vector<Real>) x);
            } catch (Exception e) {
                LOGGER.warning("GPU SpMV failed, fallback to CPU: " + e.getMessage());
            }
        }
        return cpuProvider.multiply(a, x);
    }

    // --- GPU Implementation ---

    private Vector<Real> multiplyGPU(Matrix<Real> a, Vector<Real> x) {
        initGPU();
        int rows = a.rows();
        int cols = a.cols();
        if (x.dimension() != cols) throw new IllegalArgumentException("Dimension mismatch");

        // 1. Convert Matrix to CSR Format (Host side)
        // Note: Ideally Matrix structure already exposes CSR, but here we build it.
        // This overhead makes GPU viable only for large matrices or repeated ops (if we cached CSR).
        // For "Implement everything", we do the honest conversion.
        
        java.util.List<Integer> ptrObj = new java.util.ArrayList<>();
        java.util.List<Integer> indicesObj = new java.util.ArrayList<>();
        java.util.List<Double> valuesObj = new java.util.ArrayList<>();
        
        ptrObj.add(0);
        int nnzCounter = 0;
        
        for (int i = 0; i < rows; i++) {
            // Sparse Iteration? Generic Matrix doesn't expose it easily.
            // We scan. For true SparseMatrix implementation, we would access storage directly.
            // Assuming 'a' might be sparse.
            for (int j = 0; j < cols; j++) {
                Real val = a.get(i, j);
                double d = val.doubleValue();
                if (Math.abs(d) > 1e-12) {
                    valuesObj.add(d);
                    indicesObj.add(j);
                    nnzCounter++;
                }
            }
            ptrObj.add(nnzCounter);
        }
        
        int nnz = valuesObj.size();
        int[] ptr = ptrObj.stream().mapToInt(i->i).toArray();
        int[] indices = indicesObj.stream().mapToInt(i->i).toArray();
        double[] values = valuesObj.stream().mapToDouble(d->d).toArray();
        
        double[] xData = new double[cols];
        for(int i=0; i<cols; i++) xData[i] = x.get(i).doubleValue();
        
        double[] yData = new double[rows];

        // 2. OpenCL Execution
        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();
        
        cl_mem memPtr = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * ptr.length, Pointer.to(ptr), null);
        cl_mem memIdx = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * indices.length, Pointer.to(indices), null);
        cl_mem memVal = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * values.length, Pointer.to(values), null);
        cl_mem memX = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * xData.length, Pointer.to(xData), null);
        cl_mem memY = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * yData.length, null, null);
        
        try {
            clSetKernelArg(kernel, 0, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memPtr));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memIdx));
            clSetKernelArg(kernel, 3, Sizeof.cl_mem, Pointer.to(memVal));
            clSetKernelArg(kernel, 4, Sizeof.cl_mem, Pointer.to(memX));
            clSetKernelArg(kernel, 5, Sizeof.cl_mem, Pointer.to(memY));
            
            long[] globalWorkSize = new long[]{rows};
            clEnqueueNDRangeKernel(queue, kernel, 1, null, globalWorkSize, null, 0, null, null);
            
            clEnqueueReadBuffer(queue, memY, CL_TRUE, 0, Sizeof.cl_double * rows, Pointer.to(yData), 0, null, null);
            
        } finally {
            clReleaseMemObject(memPtr);
            clReleaseMemObject(memIdx);
            clReleaseMemObject(memVal);
            clReleaseMemObject(memX);
            clReleaseMemObject(memY);
        }
        
        // 3. Convert result back to Vector
        Real[] resReals = new Real[rows];
        for(int i=0; i<rows; i++) resReals[i] = org.jscience.core.mathematics.numbers.real.Real.of(yData[i]);
        
        // Note: Constructing Vector from array depends on implementation. 
        // We use cpuProvider's method or Factory. Here relying on generic approach is hard.
        // We act as if we are creating a Dense Vector result (result of SpMV is often dense).
        // Let's use a helper or return DenseVector via standard construction?
        // LinearAlgebraProvider doesn't create vectors directly easily.
        // Hack: generic vector over storage.
        
        // Simpler: Return GenericVector using the values.
        return new org.jscience.core.mathematics.linearalgebra.vectors.GenericVector<Real>(
             new org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<Real>(resReals),
             (org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider<Real>) this,
             org.jscience.core.mathematics.sets.Reals.getInstance()
        );
    }
    
    // Fallback for others
    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { return cpuProvider.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { return cpuProvider.subtract(a, b); }
    @Override public Vector<E> multiply(Vector<E> vector, E scalar) { return cpuProvider.multiply(vector, scalar); }
    @Override public E dot(Vector<E> a, Vector<E> b) { return cpuProvider.dot(a, b); }
    @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { return cpuProvider.add(a, b); }
    @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { return cpuProvider.subtract(a, b); }
    @Override public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) { return cpuProvider.multiply(a, b); }
    @Override public Matrix<E> inverse(Matrix<E> a) { return cpuProvider.inverse(a); }
    @Override public E determinant(Matrix<E> a) { return cpuProvider.determinant(a); }
    @Override public Vector<E> solve(Matrix<E> a, Vector<E> b) { return cpuProvider.solve(a, b); }
    @Override public Matrix<E> transpose(Matrix<E> a) { return cpuProvider.transpose(a); }
    @Override public Matrix<E> scale(E scalar, Matrix<E> a) { return cpuProvider.scale(scalar, a); }
    @Override public E norm(Vector<E> a) { return cpuProvider.norm(a); }
}



