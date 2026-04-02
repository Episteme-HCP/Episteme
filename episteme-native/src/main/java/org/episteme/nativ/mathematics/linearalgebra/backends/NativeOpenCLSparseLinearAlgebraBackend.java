/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.nativ.technical.backend.gpu.opencl.OpenCLExecutionContext;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.OperationContext.Hint;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.jocl.*;
import static org.jocl.CL.*;
import java.nio.DoubleBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLSparseLinearAlgebraBackend.class);

    private static cl_context staticContext;
    private static cl_command_queue staticCommandQueue;
    private static cl_device_id staticDevice;
    private static cl_kernel matMulKernel;
    private static cl_program sparseProgram;
    private static cl_program denseProgram;
    private static volatile boolean isInitialized = false;
    private static volatile boolean initAttempted = false;
    private static Boolean cachedAvailability = null;
    // Removed synthetic handle map and counter to use native pointers directly.

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
        "}\n" +
        "\n" +
        "__kernel void transpose(__global const double *a, __global double *b, const int rows, const int cols) {\n" +
        "    int r = get_global_id(1); int c = get_global_id(0);\n" +
        "    if (r < rows && c < cols) b[c * rows + r] = a[r * cols + c];\n" +
        "}\n" +
        "\n" +
        "__kernel void dot_partial(__global const double *a, __global const double *b, __global double *out, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) out[i] = a[i] * b[i];\n" +
        "}\n\n" +
        "__kernel void saxpy(__global const double *x, __global double *y, const double alpha, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) y[i] = alpha * x[i] + y[i];\n" +
        "}\n";

    @Override
    public String getId() {
        return "opencl";
    }

    @Override
    public String getType() {
        return "linearalgebra";
    }

    @Override
    public String getName() {
        return "Native OpenCL Sparse Linear Algebra Backend";
    }

    @Override
    public String getEnvironmentInfo() {
        return isAvailable() ? "GPU (OpenCL)" : "N/A";
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
        if (!isAvailable()) return -1.0;
        if (context.hasHint(Hint.DENSE)) return -1.0;
        
        double base = getPriority();
        
        return base;
    }

    @Override
    public boolean isAvailable() {
        ensureInitialized();
        return cachedAvailability != null && cachedAvailability && !isExplicitlyDisabled();
    }

    private synchronized void ensureInitialized() {
        if (initAttempted) return; // Already tried to initialize or checked availability
        initAttempted = true;

        try {
            Class.forName("org.jocl.CL");
            CL.setExceptionsEnabled(true);
            
            int[] numPlatformsArray = new int[1];
            CL.clGetPlatformIDs(0, null, numPlatformsArray);
            if (numPlatformsArray[0] == 0) {
                cachedAvailability = false;
                return;
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
                            return;
                        }
                    }
                }
            }
            
            cachedAvailability = false;
            return;
        } catch (Throwable t) {
            cachedAvailability = false;
            return;
        }
    }

    @Override
    public boolean isLoaded() {
        return isInitialized;
    }

    @Override
    public void shutdown() {
        if (isInitialized) {
            try {
                if (matMulKernel != null) clReleaseKernel(matMulKernel);
                if (sparseProgram != null) clReleaseProgram(sparseProgram);
                if (denseProgram != null) clReleaseProgram(denseProgram);
                if (staticCommandQueue != null) clReleaseCommandQueue(staticCommandQueue);
                if (staticContext != null) clReleaseContext(staticContext);
                isInitialized = false;
            } catch (Throwable t) {
                logger.warn("Error during OpenCL shutdown: {}", t.getMessage());
            }
        }
    }

    @Override
    public String getNativeLibraryName() {
        return "opencl";
    }

    private boolean attemptInitialization() {
        if (isInitialized) return true;
        if (initAttempted) return isInitialized;
        start();
        return isInitialized;
    }

    private static synchronized void start() {
        if (initAttempted && isInitialized) return;
        initAttempted = true;
        try {
            CL.setExceptionsEnabled(true);
            int[] numPlatformsArray = new int[1];
            CL.clGetPlatformIDs(0, null, numPlatformsArray);
            int numPlatforms = numPlatformsArray[0];
            if (numPlatforms == 0) {
                logger.warn("No OpenCL platforms found.");
                isInitialized = false;
                return;
            }
            
            cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
            CL.clGetPlatformIDs(numPlatforms, platforms, null);
            logger.info("Found {} OpenCL platforms", numPlatforms);
            for (int i = 0; i < numPlatforms; i++) {
                byte[] name = new byte[256];
                CL.clGetPlatformInfo(platforms[i], CL.CL_PLATFORM_NAME, 256, Pointer.to(name), null);
                logger.info("Platform #{}: {}", i, new String(name).trim());
            }
            cl_platform_id platform = platforms[0];
            for (cl_platform_id p : platforms) {
                byte[] platformName = new byte[256];
                CL.clGetPlatformInfo(p, CL.CL_PLATFORM_NAME, 256, Pointer.to(platformName), null);
                String nameStr = new String(platformName).toUpperCase();
                if (nameStr.contains("NVIDIA")) {
                    platform = p;
                    break;
                } else if (nameStr.contains("AMD") || nameStr.contains("APPLE")) {
                    platform = p;
                }
            }
            logger.info("Selected platform: {}", platform);

            
            int[] numDevicesArray = new int[1];
            CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
            int numDevices = numDevicesArray[0];
            if (numDevices == 0) {
                logger.warn("No OpenCL devices found on platform 0.");
                isInitialized = false;
                return;
            }
            
            cl_device_id[] devices = new cl_device_id[numDevices];
            CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, numDevices, devices, null);
            logger.info("Found {} OpenCL devices on platform 0", numDevices);
            for (int i = 0; i < numDevices; i++) {
                byte[] name = new byte[256];
                CL.clGetDeviceInfo(devices[i], CL.CL_DEVICE_NAME, 256, Pointer.to(name), null);
                logger.info("Device #{}: {}", i, new String(name).trim());
            }
            staticDevice = devices[0];
            
            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);
            staticContext = CL.clCreateContext(contextProperties, 1, new cl_device_id[]{staticDevice}, null, null, null);
            cl_queue_properties queueProperties = new cl_queue_properties();
            staticCommandQueue = CL.clCreateCommandQueueWithProperties(staticContext, staticDevice, queueProperties, null);
            
            initKernels();
            
            isInitialized = true;
        } catch (Exception e) {
            isInitialized = false;
            logger.warn("Failed to initialize OpenCL Sparse Backend: {}", e.getMessage());
        }
    }

    private static void initKernels() {
        try {
            // Sparse Program
            sparseProgram = clCreateProgramWithSource(staticContext, 1, new String[]{KERNEL_SPMV}, null, null);
            clBuildProgram(sparseProgram, 0, null, null, null, null);

            // Dense Program
            denseProgram = clCreateProgramWithSource(staticContext, 1, new String[]{KERNEL_DENSE}, null, null);
            clBuildProgram(denseProgram, 0, null, null, null, null);
            matMulKernel = clCreateKernel(denseProgram, "matrixMultiply", null);
        } catch (CLException e) {
            if (e.getMessage() != null && e.getMessage().contains("CL_BUILD_PROGRAM_FAILURE")) {
                logger.warn("This OpenCL device might not support double precision (cl_khr_fp64/cl_amd_fp64) or build failed. Initialization aborted.");
            } else {
                logger.warn("Failed to build OpenCL kernels: {}", e.getMessage());
            }
            // Do not throw; let initialization fail gracefully so fallback occurs.
            isInitialized = false;
        }
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        if (!isInitialized) start();
        if (!isInitialized || staticContext == null) {
            logger.warn("OpenCL context requested but initialization failed or context is null");
            return null;
        }
        return new OpenCLExecutionContext(staticContext, staticCommandQueue);
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

        cl_mem memA = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * m * k, Pointer.to(dataA), null);
        cl_mem memB = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * k * n, Pointer.to(dataB), null);
        cl_mem memC = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, Sizeof.cl_double * m * n, null, null);

        try {
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));

            long[] globalWorkSize = new long[]{n, m};
            clEnqueueNDRangeKernel(staticCommandQueue, matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, memC, CL_TRUE, 0, Sizeof.cl_double * m * n, Pointer.to(dataC), 0, null, null);
            C.put(dataC); C.rewind();
        } finally {
            clReleaseMemObject(memA);
            clReleaseMemObject(memB);
            clReleaseMemObject(memC);
        }
    }

    @Override
    public long allocateGPUMemory(long size) {
        if (!isInitialized) start();
        cl_mem mem = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, size, null, null);
        return getNativePointer(mem);
    }

    private static long getNativePointer(cl_mem mem) {
        try {
            java.lang.reflect.Field field = cl_mem.class.getDeclaredField("nativePointer");
            field.setAccessible(true);
            return field.getLong(mem);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access OpenCL native pointer", e);
        }
    }

    private static cl_mem createMemObject(long handle) {
        cl_mem mem = new cl_mem();
        try {
            java.lang.reflect.Field field = cl_mem.class.getDeclaredField("nativePointer");
            field.setAccessible(true);
            field.setLong(mem, handle);
            return mem;
        } catch (Exception e) {
            throw new RuntimeException("Failed to wrap native OpenCL handle", e);
        }
    }

    @Override
    public void freeGPUMemory(long handle) {
        if (handle == 0) return;
        clReleaseMemObject(createMemObject(handle));
    }

    @Override
    public void copyToGPU(long handle, DoubleBuffer buffer, long size) {
        cl_mem mem = createMemObject(handle);
        double[] data = new double[(int)size];
        buffer.get(data); buffer.rewind();
        clEnqueueWriteBuffer(staticCommandQueue, mem, CL_TRUE, 0, Sizeof.cl_double * size, Pointer.to(data), 0, null, null);
    }

    @Override
    public void copyFromGPU(long handle, DoubleBuffer buffer, long size) {
        cl_mem mem = createMemObject(handle);
        double[] data = new double[(int)size];
        clEnqueueReadBuffer(staticCommandQueue, mem, CL_TRUE, 0, Sizeof.cl_double * size, Pointer.to(data), 0, null, null);
        buffer.put(data); buffer.rewind();
    }

    @Override
    public void synchronize() { if (staticCommandQueue != null) clFinish(staticCommandQueue); }

    // Linear Algebra Implementation
    @Override
    public boolean isCompatible(Ring<?> ring) { return ring instanceof Reals; }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for multiply()");
        }
        logger.debug("Entering OpenCL Dense multiply: [{}x{}] * [{}x{}]", a.rows(), a.cols(), b.rows(), b.cols());
        
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        DoubleBuffer da = DoubleBuffer.allocate(m * k);
        for(int i=0; i<m; i++) for(int j=0; j<k; j++) da.put(a.get(i, j).doubleValue());
        da.flip();
        DoubleBuffer db = DoubleBuffer.allocate(k * n);
        for(int i=0; i<k; i++) for(int j=0; j<n; j++) db.put(b.get(i, j).doubleValue());
        db.flip();
        DoubleBuffer dc = DoubleBuffer.allocate(m * n);
        
        matrixMultiply(da, db, dc, m, n, k);
        
        double[] result = new double[m * n];
        dc.get(result); dc.rewind();
        return toSparseMatrix(result, m, n);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Matrix add()");
        }
        return elementWiseVec(toDoubleArray(a), toDoubleArray(b), "vectorAdd", a.rows(), a.cols());
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Matrix subtract()");
        }
        return elementWiseVec(toDoubleArray(a), toDoubleArray(b), "vectorSubtract", a.rows(), a.cols());
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Matrix scale()");
        }
        double[] data = toDoubleArray(a);
        double s = scalar.doubleValue();
        return fromDoubleArray(scaleVec(data, s), a.rows(), a.cols());
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for transpose()");
        }
        int rows = a.rows(); int cols = a.cols();
        double[] src = toDoubleArray(a);
        double[] dst = new double[rows * cols];
        
        cl_mem memA = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * src.length, Pointer.to(src), null);
        cl_mem memB = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, Sizeof.cl_double * src.length, null, null);
        cl_kernel k = clCreateKernel(denseProgram, "transpose", null);
        try {
            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(k, 2, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{cols}));
            clEnqueueNDRangeKernel(staticCommandQueue, k, 2, null, new long[]{cols, rows}, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, memB, CL_TRUE, 0, Sizeof.cl_double * src.length, Pointer.to(dst), 0, null, null);
        } finally {
            clReleaseKernel(k); clReleaseMemObject(memA); clReleaseMemObject(memB);
        }
        return fromDoubleArray(dst, cols, rows);
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> x) {
        return multiplyCSR(a, x);
    }

    public Vector<Real> multiplyCSR(Matrix<Real> a, Vector<Real> x) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for multiplyCSR()");
        }
        logger.debug("Entering OpenCL Sparse multiplyCSR: [{}x{}] * [{}]", a.rows(), a.cols(), x.dimension());

        // Extract CSR data from SparseMatrixStorage
        // Simplified for now: assuming a is already sparse or converting it
        int rows = a.rows();
        int cols = a.cols();
        
        int[] rowPtr;
        int[] colIndices;
        double[] values;
        int nnz;

        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> sa = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a;
            rowPtr = sa.getRowPointers();
            colIndices = sa.getColIndices();
            nnz = sa.getNnz();
            values = new double[nnz];
            Object[] valsObj = sa.getValues();
            for (int i = 0; i < nnz; i++) values[i] = ((Real) valsObj[i]).doubleValue();
        } else {
            // Expensive fallback conversion to CSR
            java.util.List<Integer> rowPtrList = new java.util.ArrayList<>();
            java.util.List<Integer> colIdxList = new java.util.ArrayList<>();
            java.util.List<Double> valList = new java.util.ArrayList<>();
            rowPtrList.add(0);
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
                rowPtrList.add(rowPtrList.get(i) + count);
            }
            rowPtr = rowPtrList.stream().mapToInt(i -> i).toArray();
            colIndices = colIdxList.stream().mapToInt(i -> i).toArray();
            values = valList.stream().mapToDouble(d -> d).toArray();
            nnz = values.length;
        }

        double[] xData = toDoubleVec(x);
        double[] yData = new double[rows];

        cl_mem memPtr = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * (rows + 1), Pointer.to(rowPtr), null);
        cl_mem memInd = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * nnz, Pointer.to(colIndices), null);
        cl_mem memVal = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * nnz, Pointer.to(values), null);
        cl_mem memX = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * cols, Pointer.to(xData), null);
        cl_mem memY = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, Sizeof.cl_double * rows, null, null);

        cl_kernel spmvKernel = clCreateKernel(sparseProgram, "spmv_csr", null);
        try {
            clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(memPtr));
            clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(memInd));
            clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(memVal));
            clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(memX));
            clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(memY));

            long[] globalWorkSize = new long[]{rows};
            clEnqueueNDRangeKernel(staticCommandQueue, spmvKernel, 1, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, memY, CL_TRUE, 0, Sizeof.cl_double * rows, Pointer.to(yData), 0, null, null);
        } finally {
            clReleaseKernel(spmvKernel);
            clReleaseMemObject(memPtr);
            clReleaseMemObject(memInd);
            clReleaseMemObject(memVal);
            clReleaseMemObject(memX);
            clReleaseMemObject(memY);
        }

        return toSparseVector(yData);
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Vector add()");
        }
        return toRealVector(vecOp(toDoubleVec(a), toDoubleVec(b), "vectorAdd"));
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Vector subtract()");
        }
        return toRealVector(vecOp(toDoubleVec(a), toDoubleVec(b), "vectorSubtract"));
    }

    @Override
    public Vector<Real> multiply(Vector<Real> v, Real s) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Vector scale()");
        }
        return toRealVector(scaleVec(toDoubleVec(v), s.doubleValue()));
    }

    @Override
    public Real dot(Vector<Real> v1, Vector<Real> v2) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for dot()");
        }
        double[] products = vecOp(toDoubleVec(v1), toDoubleVec(v2), "dot_partial");
        double sum = 0; for(double d : products) sum += d;
        return Real.of(sum);
    }

    @Override
    public Real norm(Vector<Real> v) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for norm()");
        }
        double[] d = toDoubleVec(v);
        double[] sq = vecOp(d, d, "dot_partial");
        double sum = 0; for(double s : sq) sum += s;
        return Real.of(Math.sqrt(sum));
    }

    // Helper methods (internal)
    private Matrix<Real> elementWiseVec(double[] a, double[] b, String kernelName, int rows, int cols) {
        return toSparseMatrix(vecOp(a, b, kernelName), rows, cols);
    }

    private double[] vecOp(double[] a, double[] b, String kernelName) {
        int n = a.length;
        double[] result = new double[n];
        cl_mem mA = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(a), null);
        cl_mem mB = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(b), null);
        cl_mem mC = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null);
        cl_kernel k = clCreateKernel(denseProgram, kernelName, null);
        try {
            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mB));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(staticCommandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
        } finally {
            clReleaseKernel(k); clReleaseMemObject(mA); clReleaseMemObject(mB); clReleaseMemObject(mC);
        }
        return result;
    }

    private double[] scaleVec(double[] a, double s) {
        int n = a.length;
        double[] result = new double[n];
        cl_mem mA = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(a), null);
        cl_mem mC = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null);
        cl_kernel k = clCreateKernel(denseProgram, "vectorScalarMultiply", null);
        try {
            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(k, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(staticCommandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
        } finally {
            clReleaseKernel(k); clReleaseMemObject(mA); clReleaseMemObject(mC);
        }
        return result;
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        int rows = m.rows(); int cols = m.cols();
        double[] data = new double[rows * cols];
        for(int i=0; i<rows; i++) for(int j=0; j<cols; j++) data[i*cols + j] = m.get(i, j).doubleValue();
        return data;
    }

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[data.length];
        for(int i=0; i<data.length; i++) reals[i] = Real.of(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(reals, rows, cols, Reals.getInstance());
    }

    private double[] toDoubleVec(Vector<Real> v) {
        double[] data = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) {
            data[i] = v.get(i).doubleValue();
        }
        return data;
    }

    private Vector<Real> toRealVector(double[] data) {
        return toSparseVector(data);
    }

    private Vector<Real> toSparseVector(double[] data) {
        int dim = data.length;
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, Real.ZERO);
        for (int i = 0; i < dim; i++) {
            if (data[i] != 0.0) storage.set(i, Real.of(data[i]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, Reals.getInstance());
    }

    private Matrix<Real> toSparseMatrix(double[] data, int rows, int cols) {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, Real.ZERO);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double val = data[i * cols + j];
                if (val != 0.0) storage.set(i, j, Real.of(val));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, Reals.getInstance());
    }

    // Decompositions and advanced operations (Fallbacks removed as per requirements)

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + " not available");
        }
        
        // For now, only handle sparse matrices with CG
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            try {
                // Use BiCGSTAB as default for non-symmetric or general sparse
                return solveBiCGSTAB((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b, 1000, 1e-10);
            } catch (Exception e) {
                logger.warn("OpenCL BiCGSTAB Solve failed, falling back to CG: {}", e.getMessage());
                try {
                    return solveCG((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b, 1000, 1e-10);
                } catch (Exception e2) {
                    logger.error("OpenCL CG Solve also failed: {}", e2.getMessage());
                    throw new RuntimeException("Iterative solve failed", e2);
                }
            }
        }
        
        throw new UnsupportedOperationException("OpenCL direct dense solve not implemented");
    }

    private Vector<Real> solveCG(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, Vector<Real> b, int maxIter, double tol) throws Exception {
        int n = a.rows();
        int nnz = a.getNnz();
        
        // CSR Data
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        double[] values = new double[nnz];
        Object[] valsObj = a.getValues();
        for (int i = 0; i < nnz; i++) values[i] = ((Real) valsObj[i]).doubleValue();
        double[] bData = toDoubleVec(b);
        
        // GPU Buffers
        cl_mem mPtr = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null);
        cl_mem mInd = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null);
        cl_mem mVal = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(values), null);
        cl_mem mB = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(bData), null);
        
        cl_mem mX = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mR = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mP = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mAp = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mTemp = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);

        // r = b - A*x (assume x=0 initially, so r=b, p=r)
        clEnqueueCopyBuffer(staticCommandQueue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
        clEnqueueCopyBuffer(staticCommandQueue, mR, mP, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
        
        double rsOld = gpuDot(mR, mR, n);
        
        cl_kernel kSpmv = clCreateKernel(sparseProgram, "spmv_csr", null);
        cl_kernel kSaxpy = clCreateKernel(denseProgram, "saxpy", null);
        cl_kernel kDot = clCreateKernel(denseProgram, "dot_partial", null);

        try {
            for (int i = 0; i < maxIter; i++) {
                // Ap = A * p
                clSetKernelArg(kSpmv, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(kSpmv, 1, Sizeof.cl_mem, Pointer.to(mPtr));
                clSetKernelArg(kSpmv, 2, Sizeof.cl_mem, Pointer.to(mInd));
                clSetKernelArg(kSpmv, 3, Sizeof.cl_mem, Pointer.to(mVal));
                clSetKernelArg(kSpmv, 4, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(kSpmv, 5, Sizeof.cl_mem, Pointer.to(mAp));
                clEnqueueNDRangeKernel(staticCommandQueue, kSpmv, 1, null, new long[]{n}, null, 0, null, null);

                // alpha = rsOld / (p * Ap)
                double pAp = gpuDot(mP, mAp, n);
                double alpha = rsOld / pAp;

                // x = x + alpha * p
                clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mX));
                clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{alpha}));
                clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);

                // r = r - alpha * Ap
                clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mAp));
                clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mR));
                clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{-alpha}));
                clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);

                double rsNew = gpuDot(mR, mR, n);
                if (Math.sqrt(rsNew) < tol) break;

                // p = r + (rsNew / rsOld) * p
                // We need p = r + beta*p. Saxpy does y = alpha*x + y.
                // So we first scale p by beta, then add r.
                // Or: p = r + (rsNew/rsOld)*p.
                // Let's use a simpler way: scale p by beta, then add r to p.
                double beta = rsNew / rsOld;
                gpuScale(mP, beta, n);
                gpuAdd(mR, mP, n); // p = r + p

                rsOld = rsNew;
            }

            double[] xRes = new double[n];
            clEnqueueReadBuffer(staticCommandQueue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(xRes), 0, null, null);
            return toRealVector(xRes);
        } finally {
            clReleaseKernel(kSpmv); clReleaseKernel(kSaxpy); clReleaseKernel(kDot);
            clReleaseMemObject(mPtr); clReleaseMemObject(mInd); clReleaseMemObject(mVal);
            clReleaseMemObject(mB); clReleaseMemObject(mX); clReleaseMemObject(mR);
            clReleaseMemObject(mP); clReleaseMemObject(mAp); clReleaseMemObject(mTemp);
        }
    }

    private Vector<Real> solveBiCGSTAB(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, Vector<Real> b, int maxIter, double tol) throws Exception {
        int n = a.rows();
        int nnz = a.getNnz();
        
        // CSR Data
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        double[] values = new double[nnz];
        Object[] valsObj = a.getValues();
        for (int i = 0; i < nnz; i++) values[i] = ((Real) valsObj[i]).doubleValue();
        double[] bData = toDoubleVec(b);
        
        // GPU Buffers
        cl_mem mPtr = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null);
        cl_mem mInd = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null);
        cl_mem mVal = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(values), null);
        cl_mem mB = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(bData), null);
        
        cl_mem mX = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null); // Initially 0
        cl_mem mR = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mR0 = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mP = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mV = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mS = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_mem mT = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);

        // r = b - Ax (assume x=0, so r=b)
        clEnqueueCopyBuffer(staticCommandQueue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
        clEnqueueCopyBuffer(staticCommandQueue, mR, mR0, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
        
        double rho = 1, alpha = 1, omega = 1;

        cl_kernel kSpmv = clCreateKernel(sparseProgram, "spmv_csr", null);
        cl_kernel kSaxpy = clCreateKernel(denseProgram, "saxpy", null);

        try {
            for (int i = 0; i < maxIter; i++) {
                double rhoNext = gpuDot(mR0, mR, n);
                double beta = (rhoNext / rho) * (alpha / omega);
                rho = rhoNext;

                // p = r + beta * (p - omega * v)
                // p = p - omega * v
                clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mV));
                clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{-omega}));
                clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);
                // p = beta * p
                gpuScale(mP, beta, n);
                // p = r + p
                gpuAdd(mR, mP, n);

                // v = Ap
                clSetKernelArg(kSpmv, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(kSpmv, 1, Sizeof.cl_mem, Pointer.to(mPtr));
                clSetKernelArg(kSpmv, 2, Sizeof.cl_mem, Pointer.to(mInd));
                clSetKernelArg(kSpmv, 3, Sizeof.cl_mem, Pointer.to(mVal));
                clSetKernelArg(kSpmv, 4, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(kSpmv, 5, Sizeof.cl_mem, Pointer.to(mV));
                clEnqueueNDRangeKernel(staticCommandQueue, kSpmv, 1, null, new long[]{n}, null, 0, null, null);

                alpha = rho / gpuDot(mR0, mV, n);

                // s = r - alpha * v
                clEnqueueCopyBuffer(staticCommandQueue, mR, mS, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mV));
                clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mS));
                clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{-alpha}));
                clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);

                if (Math.sqrt(gpuDot(mS, mS, n)) < tol) {
                    // x = x + alpha * p
                    clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mP));
                    clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mX));
                    clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{alpha}));
                    clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                    clEnqueueNDRangeKernel(staticCommandQueue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);
                    break;
                }

                // t = As
                clSetKernelArg(kSpmv, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(kSpmv, 1, Sizeof.cl_mem, Pointer.to(mPtr));
                clSetKernelArg(kSpmv, 2, Sizeof.cl_mem, Pointer.to(mInd));
                clSetKernelArg(kSpmv, 3, Sizeof.cl_mem, Pointer.to(mVal));
                clSetKernelArg(kSpmv, 4, Sizeof.cl_mem, Pointer.to(mS));
                clSetKernelArg(kSpmv, 5, Sizeof.cl_mem, Pointer.to(mT));
                clEnqueueNDRangeKernel(staticCommandQueue, kSpmv, 1, null, new long[]{n}, null, 0, null, null);

                omega = gpuDot(mS, mT, n) / gpuDot(mT, mT, n);

                // x = x + alpha * p + omega * s
                clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mX));
                clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{alpha}));
                clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);

                clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mS));
                clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mX));
                clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{omega}));
                clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);

                // r = s - omega * t
                clEnqueueCopyBuffer(staticCommandQueue, mS, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                clSetKernelArg(kSaxpy, 0, Sizeof.cl_mem, Pointer.to(mT));
                clSetKernelArg(kSaxpy, 1, Sizeof.cl_mem, Pointer.to(mR));
                clSetKernelArg(kSaxpy, 2, Sizeof.cl_double, Pointer.to(new double[]{-omega}));
                clSetKernelArg(kSaxpy, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, kSaxpy, 1, null, new long[]{n}, null, 0, null, null);

                if (Math.sqrt(gpuDot(mR, mR, n)) < tol) break;
            }

            double[] xRes = new double[n];
            clEnqueueReadBuffer(staticCommandQueue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(xRes), 0, null, null);
            return toRealVector(xRes);
        } finally {
            clReleaseKernel(kSpmv); clReleaseKernel(kSaxpy);
            clReleaseMemObject(mPtr); clReleaseMemObject(mInd); clReleaseMemObject(mVal);
            clReleaseMemObject(mB); clReleaseMemObject(mX); clReleaseMemObject(mR); clReleaseMemObject(mR0);
            clReleaseMemObject(mP); clReleaseMemObject(mV); clReleaseMemObject(mS); clReleaseMemObject(mT);
        }
    }

    private double gpuDot(cl_mem a, cl_mem b, int n) {
        cl_mem mTemp = clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null);
        cl_kernel kDot = clCreateKernel(denseProgram, "dot_partial", null);
        try {
            clSetKernelArg(kDot, 0, Sizeof.cl_mem, Pointer.to(a));
            clSetKernelArg(kDot, 1, Sizeof.cl_mem, Pointer.to(b));
            clSetKernelArg(kDot, 2, Sizeof.cl_mem, Pointer.to(mTemp));
            clSetKernelArg(kDot, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(staticCommandQueue, kDot, 1, null, new long[]{n}, null, 0, null, null);
            
            double[] partial = new double[n];
            clEnqueueReadBuffer(staticCommandQueue, mTemp, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(partial), 0, null, null);
            double sum = 0; for(double d : partial) sum += d;
            return sum;
        } finally {
            clReleaseKernel(kDot); clReleaseMemObject(mTemp);
        }
    }

    private void gpuScale(cl_mem a, double s, int n) {
        cl_kernel k = clCreateKernel(denseProgram, "vectorScalarMultiply", null);
        try {
            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(a));
            clSetKernelArg(k, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(a)); // In-place
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(staticCommandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
        } finally {
            clReleaseKernel(k);
        }
    }

    private void gpuAdd(cl_mem x, cl_mem y, int n) {
        cl_kernel k = clCreateKernel(denseProgram, "vectorAdd", null);
        try {
            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(x));
            clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(y));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(y)); // In-place
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(staticCommandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
        } finally {
            clReleaseKernel(k);
        }
    }

    @Override
    public Vector<Real> bicgstab(Matrix<Real> a, Vector<Real> b, Vector<Real> x0, Real tolerance, int maxIterations) {
        if (!(a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
            throw new UnsupportedOperationException("OpenCL BiCGSTAB only supports sparse matrices for now");
        }
        try {
            return solveBiCGSTAB((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b, maxIterations, tolerance.doubleValue());
        } catch (Exception e) {
            throw new RuntimeException("OpenCL BiCGSTAB failed", e);
        }
    }

    @Override
    public Vector<Real> conjugateGradient(Matrix<Real> a, Vector<Real> b, Vector<Real> x0, Real tolerance, int maxIterations) {
        if (!(a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
            throw new UnsupportedOperationException("OpenCL CG only supports sparse matrices for now");
        }
        try {
            return solveCG((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b, maxIterations, tolerance.doubleValue());
        } catch (Exception e) {
            throw new RuntimeException("OpenCL CG failed", e);
        }
    }

    @Override
    public Vector<Real> gmres(Matrix<Real> a, Vector<Real> b, Vector<Real> x0, Real tolerance, int maxIterations, int restarts) {
        // Placeholder or basic implementation using CG/BiCGSTAB for now if appropriate, 
        // or just throw UOE if not implemented.
        throw new UnsupportedOperationException("OpenCL GMRES not yet implemented");
    }
}
