/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.providers.StandardLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.nativ.technical.backend.nativ.NativeBackend;
import org.jocl.*;
import static org.jocl.CL.*;
import java.nio.DoubleBuffer;
import java.util.logging.Logger;
import com.google.auto.service.AutoService;

/**
 * OpenCL implementation of GPUBackend for cross-platform hardware acceleration,
 * integrated with Sparse and Dense Linear Algebra support.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, AlgorithmProvider.class, GPUBackend.class})
public class NativeOpenCLSparseLinearAlgebraBackend implements NativeBackend, SparseLinearAlgebraProvider<Real>, GPUBackend {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(NativeOpenCLSparseLinearAlgebraBackend.class.getName());

    private cl_context context;
    private cl_command_queue commandQueue;
    private cl_device_id device;
    private boolean isInitialized = false;

    private final StandardLinearAlgebraProvider<Real> denseFallback = new StandardLinearAlgebraProvider<>();
    private final CPUSparseLinearAlgebraProvider<Real> sparseFallback = new CPUSparseLinearAlgebraProvider<>(Reals.getInstance());

    private cl_kernel matMulKernel;
    private cl_program sparseProgram;
    private cl_program denseProgram;

    // Kernels
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

    @Override
    public String getId() {
        return "opencl";
    }

    @Override
    public String getName() {
        return "Native OpenCL Sparse Linear Algebra Backend";
    }

    @Override
    public String getDescription() {
        return "GPU-accelerated linear algebra operations via OpenCL.";
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1;
        double base = getPriority();
        if (context.getDataSize() < 100) base -= 100;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 30;
        return base;
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jocl.CL");
            int[] numPlatformsArray = new int[1];
            CL.clGetPlatformIDs(0, null, numPlatformsArray);
            return numPlatformsArray[0] > 0;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isLoaded() {
        return isInitialized;
    }

    @Override
    public String getNativeLibraryName() {
        return "opencl";
    }

    private synchronized void start() {
        if (isInitialized) return;
        try {
            CL.setExceptionsEnabled(true);
            int[] numPlatformsArray = new int[1];
            CL.clGetPlatformIDs(0, null, numPlatformsArray);
            cl_platform_id platform = new cl_platform_id();
            CL.clGetPlatformIDs(1, new cl_platform_id[]{platform}, null);
            
            cl_device_id[] devices = new cl_device_id[1];
            CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, 1, devices, null);
            device = devices[0];
            
            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);
            context = CL.clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
            cl_queue_properties queueProperties = new cl_queue_properties();
            commandQueue = CL.clCreateCommandQueueWithProperties(context, device, queueProperties, null);
            
            initKernels();
            
            isInitialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize OpenCL Backend", e);
        }
    }

    private void initKernels() {
        // Sparse Program
        sparseProgram = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SPMV}, null, null);
        clBuildProgram(sparseProgram, 0, null, null, null, null);

        // Dense Program
        denseProgram = clCreateProgramWithSource(context, 1, new String[]{KERNEL_DENSE}, null, null);
        clBuildProgram(denseProgram, 0, null, null, null, null);
        matMulKernel = clCreateKernel(denseProgram, "matrixMultiply", null);
    }

    @Override
    public ExecutionContext createContext() {
        if (!isInitialized) start();
        return new OpenCLExecutionContext(context, commandQueue);
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.GPU;
    }

    @Override
    public DeviceInfo[] getDevices() {
        return new DeviceInfo[]{new DeviceInfo("OpenCL Device", 0, 0, "Unknown")};
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        if (!isInitialized) start();
        double[] dataA = new double[m * k]; A.get(dataA); A.rewind();
        double[] dataB = new double[k * n]; B.get(dataB); B.rewind();
        double[] dataC = new double[m * n];

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
            clEnqueueNDRangeKernel(commandQueue, matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memC, CL_TRUE, 0, Sizeof.cl_double * m * n, Pointer.to(dataC), 0, null, null);
            C.put(dataC); C.rewind();
        } finally {
            clReleaseMemObject(memA);
            clReleaseMemObject(memB);
            clReleaseMemObject(memC);
        }
    }

    @Override
    public long allocateGPUMemory(long size) { return 0; }
    @Override
    public void freeGPUMemory(long handle) {}
    @Override
    public void copyToGPU(long handle, DoubleBuffer buffer, long size) {}
    @Override
    public void copyFromGPU(long handle, DoubleBuffer buffer, long size) {}
    @Override
    public void synchronize() { if (commandQueue != null) clFinish(commandQueue); }

    // Linear Algebra Implementation
    @Override
    public boolean isCompatible(Ring<?> ring) { return ring instanceof Reals; }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) return denseFallback.multiply(a, b);
        try {
            int m = a.rows(); int k = a.cols(); int n = b.cols();
            DoubleBuffer da = DoubleBuffer.allocate(m * k);
            for(int i=0; i<m; i++) for(int j=0; j<k; j++) da.put(a.get(i, j).doubleValue());
            da.flip();
            DoubleBuffer db = DoubleBuffer.allocate(k * n);
            for(int i=0; i<k; i++) for(int j=0; j<n; j++) db.put(b.get(i, j).doubleValue());
            db.flip();
            DoubleBuffer dc = DoubleBuffer.allocate(m * n);
            matrixMultiply(da, db, dc, m, n, k);
            Real[] res = new Real[m * n];
            for(int i=0; i<m*n; i++) res[i] = Real.of(dc.get(i));
            return new org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix<>(res, m, n, Reals.getInstance());
        } catch (Exception e) {
            return denseFallback.multiply(a, b);
        }
    }

    @Override public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) { return denseFallback.add(a, b); }
    @Override public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) { return denseFallback.subtract(a, b); }
    @Override public Matrix<Real> scale(Real s, Matrix<Real> a) { return denseFallback.scale(s, a); }
    @Override public Matrix<Real> transpose(Matrix<Real> a) { return denseFallback.transpose(a); }
    
    @Override public Vector<Real> multiply(Matrix<Real> a, Vector<Real> x) {
        return sparseFallback.multiply(a, x);
    }

    @Override public Vector<Real> add(Vector<Real> a, Vector<Real> b) { return denseFallback.add(a, b); }
    @Override public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) { return denseFallback.subtract(a, b); }
    @Override public Vector<Real> multiply(Vector<Real> v, Real s) { return denseFallback.multiply(v, s); }
    @Override public Real dot(Vector<Real> a, Vector<Real> b) { return denseFallback.dot(a, b); }
    @Override public Real norm(Vector<Real> a) { return denseFallback.norm(a); }
    @Override public Matrix<Real> inverse(Matrix<Real> a) { return denseFallback.inverse(a); }
    @Override public Real determinant(Matrix<Real> a) { return denseFallback.determinant(a); }
    @Override public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) { return denseFallback.solve(a, b); }
}
