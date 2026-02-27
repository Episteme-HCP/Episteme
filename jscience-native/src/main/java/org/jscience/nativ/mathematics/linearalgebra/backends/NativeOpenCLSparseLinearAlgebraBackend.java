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
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.*;
import org.jscience.core.mathematics.context.MathContext;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.technical.algorithm.OperationContext.Hint;
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
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class})
public class NativeOpenCLSparseLinearAlgebraBackend implements NativeBackend, SparseLinearAlgebraProvider<Real>, GPUBackend {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(NativeOpenCLSparseLinearAlgebraBackend.class.getName());

    private cl_context context;
    private cl_command_queue commandQueue;
    private cl_device_id device;
    private boolean isInitialized = false;
    private static Boolean cachedAvailability = null;

    private cl_kernel matMulKernel;
    private cl_program sparseProgram;
    private cl_program denseProgram;

    // Kernels
    private static final String KERNEL_SPMV = 
        "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
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
        if (!isInitialized && !attemptInitialization()) return -1;
        if (MathContext.getCurrent().getRealPrecision() == MathContext.RealPrecision.EXACT) {
            return -1.0; // Hardware Float/Double cannot handle Arbitrary Precision MathContext
        }

        
        double base = getPriority();
        if (context.hasHint(Hint.GPU_RESIDENT)) base += 50;
        if (context.hasHint(Hint.SPARSE)) base += 20;
        
        // Granular scoring for OpenCL
        if (context.hasHint(Hint.MAT_MUL)) base += 10;
        if (context.hasHint(Hint.MAT_SOLVE)) base -= 30; // Native solver might be slow or less stable
        
        if (context.getDataSize() > 1000) base += 20;
        
        return base;
    }

    @Override
    public boolean isAvailable() {
        if (cachedAvailability != null) return cachedAvailability;
        
        try {
            Class.forName("org.jocl.CL");
            CL.setExceptionsEnabled(true);
            
            int[] numPlatformsArray = new int[1];
            CL.clGetPlatformIDs(0, null, numPlatformsArray);
            if (numPlatformsArray[0] == 0) {
                cachedAvailability = false;
                return false;
            }
            
            cl_platform_id[] platforms = new cl_platform_id[numPlatformsArray[0]];
            CL.clGetPlatformIDs(platforms.length, platforms, null);
            
            for (cl_platform_id platform : platforms) {
                int[] numDevicesArray = new int[1];
                CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
                
                if (numDevicesArray[0] > 0) {
                    cl_device_id[] devices = new cl_device_id[numDevicesArray[0]];
                    CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, devices.length, devices, null);
                    
                    for (cl_device_id dev : devices) {
                        long[] sizeArray = new long[1];
                        CL.clGetDeviceInfo(dev, CL.CL_DEVICE_EXTENSIONS, 0, null, sizeArray);
                        byte[] buffer = new byte[(int)sizeArray[0]];
                        CL.clGetDeviceInfo(dev, CL.CL_DEVICE_EXTENSIONS, buffer.length, Pointer.to(buffer), null);
                        String extensions = new String(buffer);
                        
                        if (extensions.contains("cl_khr_fp64") || extensions.contains("cl_amd_fp64")) {
                            cachedAvailability = true;
                            return true;
                        }
                    }
                }
            }
            
            LOGGER.warning("OpenCL found but no device supports double precision (cl_khr_fp64/cl_amd_fp64).");
            cachedAvailability = false;
            return false;
        } catch (Throwable t) {
            cachedAvailability = false;
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

    private boolean attemptInitialization() {
        if (isInitialized) return true;
        start();
        return isInitialized;
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
            isInitialized = false;
            LOGGER.warning("Failed to initialize OpenCL Backend: " + e.getMessage());
        }
    }

    private void initKernels() {
        try {
            // Sparse Program
            sparseProgram = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SPMV}, null, null);
            clBuildProgram(sparseProgram, 0, null, null, null, null);

            // Dense Program
            denseProgram = clCreateProgramWithSource(context, 1, new String[]{KERNEL_DENSE}, null, null);
            clBuildProgram(denseProgram, 0, null, null, null, null);
            matMulKernel = clCreateKernel(denseProgram, "matrixMultiply", null);
        } catch (CLException e) {
            if (e.getMessage() != null && e.getMessage().contains("CL_BUILD_PROGRAM_FAILURE")) {
                LOGGER.warning("This OpenCL device might not support double precision (cl_khr_fp64/cl_amd_fp64) or build failed. Initialization aborted.");
            } else {
                LOGGER.warning("Failed to build OpenCL kernels: " + e.getMessage());
            }
            // Do not throw; let initialization fail gracefully so fallback occurs.
            isInitialized = false;
        }
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
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            return org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider.super.multiply(a, b);
        }
        
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
    }

    @Override public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) { throw new UnsupportedOperationException("OpenCL add not implemented"); }
    @Override public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) { throw new UnsupportedOperationException("OpenCL subtract not implemented"); }
    @Override public Matrix<Real> scale(Real s, Matrix<Real> a) { throw new UnsupportedOperationException("OpenCL scale not implemented"); }
    @Override public Matrix<Real> transpose(Matrix<Real> a) { throw new UnsupportedOperationException("OpenCL transpose not implemented"); }
    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> x) {
        return multiplyCSR(a, x);
    }

    public Vector<Real> multiplyCSR(Matrix<Real> a, Vector<Real> x) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            return org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider.super.multiply(a, x);
        }

        // Extract CSR data from SparseMatrixStorage
        // Simplified for now: assuming a is already sparse or converting it
        int rows = a.rows();
        int cols = a.cols();
        
        // This is a placeholder for actual CSR extraction logic
        // In a real implementation, we would access the storage directly if it's SparseMatrixStorage
        int[] rowPtr = new int[rows + 1];
        java.util.List<Integer> colIdxList = new java.util.ArrayList<>();
        java.util.List<Double> valList = new java.util.ArrayList<>();
        
        rowPtr[0] = 0;
        for (int i = 0; i < rows; i++) {
            int count = 0;
            for (int j = 0; j < cols; j++) {
                Real val = a.get(i, j);
                if (val.doubleValue() != 0.0) {
                    colIdxList.add(j);
                    valList.add(val.doubleValue());
                    count++;
                }
            }
            rowPtr[i+1] = rowPtr[i] + count;
        }
        
        int nnz = colIdxList.size();
        int[] colIndices = colIdxList.stream().mapToInt(i -> i).toArray();
        double[] values = valList.stream().mapToDouble(d -> d).toArray();
        double[] xData = new double[cols];
        for (int i = 0; i < cols; i++) xData[i] = x.get(i).doubleValue();
        double[] yData = new double[rows];

        cl_mem memPtr = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * (rows + 1), Pointer.to(rowPtr), null);
        cl_mem memInd = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nnz, Pointer.to(colIndices), null);
        cl_mem memVal = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * nnz, Pointer.to(values), null);
        cl_mem memX = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * cols, Pointer.to(xData), null);
        cl_mem memY = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * rows, null, null);

        cl_kernel spmvKernel = clCreateKernel(sparseProgram, "spmv_csr", null);
        try {
            clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(memPtr));
            clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(memInd));
            clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(memVal));
            clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(memX));
            clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(memY));

            long[] globalWorkSize = new long[]{rows};
            clEnqueueNDRangeKernel(commandQueue, spmvKernel, 1, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memY, CL_TRUE, 0, Sizeof.cl_double * rows, Pointer.to(yData), 0, null, null);
        } finally {
            clReleaseKernel(spmvKernel);
            clReleaseMemObject(memPtr);
            clReleaseMemObject(memInd);
            clReleaseMemObject(memVal);
            clReleaseMemObject(memX);
            clReleaseMemObject(memY);
        }

        Real[] res = new Real[rows];
        for (int i = 0; i < rows; i++) res[i] = Real.of(yData[i]);
        return new org.jscience.core.mathematics.linearalgebra.vectors.DenseVector<>(java.util.Arrays.asList(res), Reals.getInstance());
    }

    @Override public Vector<Real> add(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL vector add not implemented"); }
    @Override public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL vector subtract not implemented"); }
    @Override public Vector<Real> multiply(Vector<Real> v, Real s) { throw new UnsupportedOperationException("OpenCL vector scale not implemented"); }
    @Override public Real dot(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL dot not implemented"); }
    @Override public Real norm(Vector<Real> a) { throw new UnsupportedOperationException("OpenCL norm not implemented"); }
    @Override public Matrix<Real> inverse(Matrix<Real> a) { throw new UnsupportedOperationException("OpenCL inverse not implemented"); }
    @Override public Real determinant(Matrix<Real> a) { throw new UnsupportedOperationException("OpenCL determinant not implemented"); }
    @Override public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL solve not implemented"); }

    @Override
    public LUResult<Real> lu(Matrix<Real> a) {
        throw new UnsupportedOperationException("OpenCL LU decomposition requires CLBlast/clMAGMA integration.");
    }

    @Override
    public QRResult<Real> qr(Matrix<Real> a) {
        throw new UnsupportedOperationException("OpenCL QR decomposition requires CLBlast/clMAGMA integration.");
    }

    @Override
    public CholeskyResult<Real> cholesky(Matrix<Real> a) {
        throw new UnsupportedOperationException("OpenCL Cholesky decomposition requires CLBlast/clMAGMA integration.");
    }

    @Override
    public SVDResult<Real> svd(Matrix<Real> a) {
        throw new UnsupportedOperationException("OpenCL SVD requires CLBlast/clMAGMA integration.");
    }

    @Override
    public EigenResult<Real> eigen(Matrix<Real> a) {
        throw new UnsupportedOperationException("OpenCL Eigen decomposition requires CLBlast/clMAGMA integration.");
    }
}
