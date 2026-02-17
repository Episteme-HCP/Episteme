/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.gpu.opencl;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.providers.StandardLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLBackend;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLExecutionContext;
import org.jscience.core.technical.backend.nativ.LibraryBackend;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.sets.Reals;

import com.google.auto.service.AutoService;

import org.jocl.*;
import static org.jocl.CL.*;
import java.nio.DoubleBuffer;
import java.util.logging.Logger;
import java.util.Arrays;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.linearalgebra.vectors.GenericVector;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;

/**
 * Native OpenCL Linear Algebra Backend using JOCL.
 * <p>
 * Provides GPU-accelerated linear algebra operations for both dense and sparse matrices.
 * Uses {@link OpenCLBackend} for context and device management.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, LibraryBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, AlgorithmProvider.class})
public class OpenCLLinearAlgebraBackend implements GPUBackend, LibraryBackend, SparseLinearAlgebraProvider<Real> {

    private static final Logger LOGGER = Logger.getLogger(OpenCLLinearAlgebraBackend.class.getName());

    // Composition: Uses the core OpenCLBackend for infrastructure
    private final OpenCLBackend backend = new OpenCLBackend();
    
    private final StandardLinearAlgebraProvider<Real> denseFallback = new StandardLinearAlgebraProvider<>();
    private final CPUSparseLinearAlgebraProvider<Real> sparseFallback = new CPUSparseLinearAlgebraProvider<>(Reals.getInstance());

    // CSR SpMV Kernel
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

    private static final String KERNEL_DENSE = 
        "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
        "__kernel void vectorAdd(__global const double *a, __global const double *b, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) c[i] = a[i] + b[i];\n" +
        "}\n" +
        "\n" +
        "__kernel void vectorSubtract(__global const double *a, __global const double *b, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) c[i] = a[i] - b[i];\n" +
        "}\n" +
        "\n" +
        "__kernel void vectorScalarMultiply(__global const double *a, const double s, __global double *b, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) b[i] = a[i] * s;\n" +
        "}\n" +
        "\n" +
        "__kernel void matrixMultiply(__global const double *a, __global const double *b, __global double *c, const int m, const int n, const int k) {\n" +
        "    int row = get_global_id(1);\n" +
        "    int col = get_global_id(0);\n" +
        "    if (row < m && col < n) {\n" +
        "        double sum = 0.0;\n" +
        "        for (int i = 0; i < k; i++) {\n" +
        "            sum += a[row * k + i] * b[i * n + col];\n" +
        "        }\n" +
        "        c[row * n + col] = sum;\n" +
        "    }\n" +
        "}\n";

    private boolean initialized = false;
    private cl_kernel spmvKernel;
    private cl_kernel vecAddKernel;
    private cl_kernel vecSubKernel;
    private cl_kernel vecScalarMulKernel;
    private cl_kernel matMulKernel;
    private cl_program sparseProgram;
    private cl_program denseProgram;

    // --- Backend / ComputeBackend / GPUBackend Delegation ---

    @Override
    public boolean isAvailable() {
        return backend.isAvailable();
    }

    @Override
    public String getName() {
        return "Native OpenCL Linear Algebra Backend";
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.GPU;
    }

    @Override
    public ExecutionContext createContext() {
        return backend.createContext();
    }

    @Override
    public DeviceInfo[] getDevices() {
        return backend.getDevices();
    }

    @Override
    public void selectDevice(int deviceId) {
        backend.selectDevice(deviceId);
    }

    @Override
    public long allocateGPUMemory(long size) {
        return backend.allocateGPUMemory(size);
    }

    @Override
    public void freeGPUMemory(long handle) {
        backend.freeGPUMemory(handle);
    }

    @Override
    public void copyToGPU(long handle, DoubleBuffer buffer, long size) {
        backend.copyToGPU(handle, buffer, size);
    }

    @Override
    public void copyFromGPU(long handle, DoubleBuffer buffer, long size) {
        backend.copyFromGPU(handle, buffer, size);
    }

    @Override
    public void synchronize() {
        backend.synchronize();
    }
    
    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        backend.matrixMultiply(A, B, C, m, n, k);
    }

    @Override
    public boolean isLoaded() {
        return backend.isAvailable();
    }

    @Override
    public String getNativeLibraryName() {
        return "OpenCL";
    }

    // --- LinearAlgebraProvider (Dense) Implementation ---

    @Override
    public boolean isCompatible(Ring<?> ring) {
        return ring instanceof Reals;
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (isAvailable()) {
            try {
                return multiplyDenseGPU(a, b);
            } catch (Exception e) {
                LOGGER.warning("OpenCL Matrix Multiply failed: " + e.getMessage());
            }
        }
        return denseFallback.multiply(a, b);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) { return denseFallback.add(a, b); }
    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) { return denseFallback.subtract(a, b); }
    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) { return denseFallback.scale(scalar, a); }
    @Override
    public Matrix<Real> transpose(Matrix<Real> a) { return denseFallback.transpose(a); }
    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (isAvailable()) {
            try {
                return executeBinaryVecOp(a, b, "vectorAdd");
            } catch (Exception e) {
                LOGGER.warning("OpenCL Vector Add failed: " + e.getMessage());
            }
        }
        return denseFallback.add(a, b);
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (isAvailable()) {
            try {
                return executeBinaryVecOp(a, b, "vectorSubtract");
            } catch (Exception e) {
                LOGGER.warning("OpenCL Vector Subtract failed: " + e.getMessage());
            }
        }
        return denseFallback.subtract(a, b);
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        if (isAvailable()) {
            try {
                return executeScalarVecOp(vector, scalar.doubleValue(), "vectorScalarMultiply");
            } catch (Exception e) {
                LOGGER.warning("OpenCL Vector Scalar Multiply failed: " + e.getMessage());
            }
        }
        return denseFallback.multiply(vector, scalar);
    }
    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) { return denseFallback.dot(a, b); }
    @Override
    public Real norm(Vector<Real> a) { return denseFallback.norm(a); }
    @Override
    public Matrix<Real> inverse(Matrix<Real> a) { return denseFallback.inverse(a); }
    @Override
    public Real determinant(Matrix<Real> a) { return denseFallback.determinant(a); }
    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) { return denseFallback.solve(a, b); }

    // --- SparseLinearAlgebraProvider Implementation ---

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> x) {
        // Heuristic: Use GPU for large matrices, fallback to CPU implementation otherwise
        if (a.rows() > 100) {
            try {
                return multiplyGPU(a, x);
            } catch (Exception e) {
                LOGGER.warning("GPU SpMV failed, fallback to CPU: " + e.getMessage());
            }
        }
        return sparseFallback.multiply(a, x);
    }

    private synchronized void initGPU() {
        if (initialized) return;
        if (!backend.isAvailable()) throw new IllegalStateException("OpenCL not available");
        
        try {
            OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
            cl_context context = ctx.getContext();
            
            // Sparse Program
            sparseProgram = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SPMV}, null, null);
            clBuildProgram(sparseProgram, 0, null, null, null, null);
            spmvKernel = clCreateKernel(sparseProgram, "spmv_csr", null);

            // Dense Program
            denseProgram = clCreateProgramWithSource(context, 1, new String[]{KERNEL_DENSE}, null, null);
            clBuildProgram(denseProgram, 0, null, null, null, null);
            vecAddKernel = clCreateKernel(denseProgram, "vectorAdd", null);
            vecSubKernel = clCreateKernel(denseProgram, "vectorSubtract", null);
            vecScalarMulKernel = clCreateKernel(denseProgram, "vectorScalarMultiply", null);
            matMulKernel = clCreateKernel(denseProgram, "matrixMultiply", null);
            
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to init OpenCL kernels", e);
        }
    }

    private Matrix<Real> multiplyDenseGPU(Matrix<Real> a, Matrix<Real> b) {
        initGPU();
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        double[] dataA = toDoubleArray(a);
        double[] dataB = toDoubleArray(b);
        double[] dataC = new double[m * n];

        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();

        cl_mem memA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * m * k, Pointer.to(dataA), null);
        cl_mem memB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * k * n, Pointer.to(dataB), null);
        cl_mem memC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * m * n, null, null);

        try {
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));

            long[] globalWorkSize = new long[]{n, m};
            clEnqueueNDRangeKernel(queue, matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, Sizeof.cl_double * m * n, Pointer.to(dataC), 0, null, null);
        } finally {
            clReleaseMemObject(memA);
            clReleaseMemObject(memB);
            clReleaseMemObject(memC);
        }

        Real[] resReals = new Real[dataC.length];
        for (int i = 0; i < dataC.length; i++) resReals[i] = Real.of(dataC[i]);
        return new org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix<Real>(resReals, m, n, Reals.getInstance());
    }

    private Vector<Real> executeBinaryVecOp(Vector<Real> a, Vector<Real> b, String kernelName) {
        initGPU();
        int n = a.dimension();
        if (n != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");

        double[] dataA = toDoubleArray(a);
        double[] dataB = toDoubleArray(b);
        double[] dataC = new double[n];

        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();

        cl_kernel kernel = kernelName.equals("vectorAdd") ? vecAddKernel : vecSubKernel;

        cl_mem memA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(dataA), null);
        cl_mem memB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(dataB), null);
        cl_mem memC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * n, null, null);

        try {
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));

            long[] globalWorkSize = new long[]{n};
            clEnqueueNDRangeKernel(queue, kernel, 1, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(queue, memC, CL_TRUE, 0, Sizeof.cl_double * n, Pointer.to(dataC), 0, null, null);
        } finally {
            clReleaseMemObject(memA);
            clReleaseMemObject(memB);
            clReleaseMemObject(memC);
        }

        return toVector(dataC);
    }

    private Vector<Real> executeScalarVecOp(Vector<Real> v, double s, String kernelName) {
        initGPU();
        int n = v.dimension();
        double[] dataV = toDoubleArray(v);
        double[] dataR = new double[n];

        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();

        cl_mem memV = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(dataV), null);
        cl_mem memR = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * n, null, null);

        try {
            clSetKernelArg(vecScalarMulKernel, 0, Sizeof.cl_mem, Pointer.to(memV));
            clSetKernelArg(vecScalarMulKernel, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
            clSetKernelArg(vecScalarMulKernel, 2, Sizeof.cl_mem, Pointer.to(memR));
            clSetKernelArg(vecScalarMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));

            long[] globalWorkSize = new long[]{n};
            clEnqueueNDRangeKernel(queue, vecScalarMulKernel, 1, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(queue, memR, CL_TRUE, 0, Sizeof.cl_double * n, Pointer.to(dataR), 0, null, null);
        } finally {
            clReleaseMemObject(memV);
            clReleaseMemObject(memR);
        }

        return toVector(dataR);
    }

    private double[] toDoubleArray(Vector<Real> v) {
        int n = v.dimension();
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) arr[i] = v.get(i).doubleValue();
        return arr;
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] arr = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[i * cols + j] = m.get(i, j).doubleValue();
            }
        }
        return arr;
    }

    private Vector<Real> toVector(double[] data) {
        Real[] reals = new Real[data.length];
        for (int i = 0; i < data.length; i++) reals[i] = Real.of(data[i]);
        return (Vector<Real>) DenseVector.of(Arrays.asList(reals), Reals.getInstance());
    }

    private Vector<Real> multiplyGPU(Matrix<Real> a, Vector<Real> x) {
        initGPU();
        int rows = a.rows();
        int cols = a.cols();
        if (x.dimension() != cols) throw new IllegalArgumentException("Dimension mismatch");

        // 1. Convert Matrix to CSR Format (Host side)
        java.util.List<Integer> ptrObj = new java.util.ArrayList<>();
        java.util.List<Integer> indicesObj = new java.util.ArrayList<>();
        java.util.List<Double> valuesObj = new java.util.ArrayList<>();
        
        ptrObj.add(0);
        int nnzCounter = 0;
        
        for (int i = 0; i < rows; i++) {
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
            clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(memPtr));
            clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(memIdx));
            clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(memVal));
            clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(memX));
            clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(memY));
            
            long[] globalWorkSize = new long[]{rows};
            clEnqueueNDRangeKernel(queue, spmvKernel, 1, null, globalWorkSize, null, 0, null, null);
            
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
        for(int i=0; i<rows; i++) resReals[i] = Real.of(yData[i]);
        
        return new GenericVector<Real>(
             new DenseVectorStorage<Real>(Arrays.asList(resReals)),
             (LinearAlgebraProvider<Real>) this,
             Reals.getInstance()
        );
    }
}
