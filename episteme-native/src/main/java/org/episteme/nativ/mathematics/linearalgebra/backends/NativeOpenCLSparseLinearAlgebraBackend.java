/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.nativ.technical.backend.gpu.opencl.OpenCLExecutionContext;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import java.util.function.Function;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.FieldElement;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.RealFloat;
import org.episteme.core.mathematics.numbers.real.RealDouble;

import org.episteme.nativ.technical.backend.nativ.ResourceTracker;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.OperationContext.Hint;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;

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
public class NativeOpenCLSparseLinearAlgebraBackend<E extends FieldElement<E>> implements NativeBackend, SparseLinearAlgebraProvider<E>, GPUBackend {

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLSparseLinearAlgebraBackend.class);

    private static cl_context staticContext;
    private static cl_command_queue staticCommandQueue;
    private static cl_device_id staticDevice;
    private static cl_kernel matMulKernel;
    private static cl_kernel spmvKernel;
    private static cl_kernel vecAddKernel;
    private static cl_kernel vecSubKernel;
    private static cl_kernel vecScalarMulKernel;
    private static cl_kernel saxpyKernel;
    private static cl_kernel dotPartialKernel;
    private static cl_kernel transposeKernel;

    private static cl_program sparseProgram;
    private static cl_program denseProgram;
    private static volatile boolean isInitialized = false;
    private static volatile boolean initAttempted = false;
    private static Boolean cachedAvailability = null;
    // Removed synthetic handle map and counter to use native pointers directly.

    // Kernels
    private static final String KERNEL_SPMV = 
        "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
        "__kernel void spmv_csr(int num_rows, __global const int* ptr, __global const int* indices, __global const double* values, __global const double* x, __global double* y) {\n" +
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
        "}\n\n" +
        "__kernel void csr_transpose_count(int num_rows, int num_cols, __global const int* col_indices, __global int* col_counts) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row < num_rows) {\n" +
        "        // This kernel is simple, but we need to handle atomic increments if we do it this way\n" +
        "        // Alternatively, we use a more parallel-friendly sparse transpose.\n" +
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
        return "opencl-sparse";
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
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public String getNativeLibraryName() {
        return "opencl";
    }

    @Override
    public String getStatusMessage() {
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
        if (isExplicitlyDisabled()) return false;
        ensureInitialized();
        return cachedAvailability != null && cachedAvailability;
    }

    @Override
    public boolean isExplicitlyDisabled() {
        return Boolean.getBoolean("episteme.backend.opencl-sparse.disabled") || 
               Boolean.getBoolean("episteme.backend.opencl.disabled") || 
               GPUBackend.super.isExplicitlyDisabled();
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (ring == null) return false;
        Object zero = ring.zero();
        return zero instanceof Real || zero instanceof Complex;
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
    public void shutdown() {
        if (isInitialized) {
            try {
                if (matMulKernel != null) clReleaseKernel(matMulKernel);
                if (spmvKernel != null) clReleaseKernel(spmvKernel);
                if (vecAddKernel != null) clReleaseKernel(vecAddKernel);
                if (vecSubKernel != null) clReleaseKernel(vecSubKernel);
                if (vecScalarMulKernel != null) clReleaseKernel(vecScalarMulKernel);
                if (saxpyKernel != null) clReleaseKernel(saxpyKernel);
                if (dotPartialKernel != null) clReleaseKernel(dotPartialKernel);
                if (transposeKernel != null) clReleaseKernel(transposeKernel);

                if (sparseProgram != null) clReleaseProgram(sparseProgram);
                if (denseProgram != null) clReleaseProgram(denseProgram);
                if (staticCommandQueue != null) clReleaseCommandQueue(staticCommandQueue);
                if (staticContext != null) clReleaseContext(staticContext);
                
                matMulKernel = null;
                spmvKernel = null;
                vecAddKernel = null;
                vecSubKernel = null;
                vecScalarMulKernel = null;
                saxpyKernel = null;
                dotPartialKernel = null;
                transposeKernel = null;
                sparseProgram = null;
                denseProgram = null;
                staticCommandQueue = null;
                staticContext = null;
                staticDevice = null;

                isInitialized = false;
                initAttempted = false;
            } catch (Throwable t) {
                logger.warn("Error during OpenCL shutdown: {}", t.getMessage());
            }
        }
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
        System.err.println("[OpenCL-Sparse-Init] Starting...");
        try {
            CL.setExceptionsEnabled(true);
            int[] numPlatformsArray = new int[1];
            System.err.println("[OpenCL-Sparse-Init] Invoking clGetPlatformIDs...");
            CL.clGetPlatformIDs(0, null, numPlatformsArray);
            int numPlatforms = numPlatformsArray[0];
            System.err.println("[OpenCL-Sparse-Init] numPlatforms=" + numPlatforms);
            if (numPlatforms == 0) {
                System.err.println("[OpenCL-Sparse-Init] ABORT: No platforms found.");
                isInitialized = false;
                return;
            }
            
            cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
            CL.clGetPlatformIDs(numPlatforms, platforms, null);
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
            System.err.println("[OpenCL-Sparse-Init] Selected platform: " + platform);

            int[] numDevicesArray = new int[1];
            CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
            int numDevices = numDevicesArray[0];
            System.err.println("[OpenCL-Sparse-Init] numDevices=" + numDevices);
            if (numDevices == 0) {
                System.err.println("[OpenCL-Sparse-Init] ABORT: No devices found.");
                isInitialized = false;
                return;
            }
            
            cl_device_id[] devices = new cl_device_id[numDevices];
            CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, numDevices, devices, null);
            staticDevice = devices[0];
            
            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);
            staticContext = CL.clCreateContext(contextProperties, 1, new cl_device_id[]{staticDevice}, null, null, null);
            cl_queue_properties queueProperties = new cl_queue_properties();
            staticCommandQueue = CL.clCreateCommandQueueWithProperties(staticContext, staticDevice, queueProperties, null);
            
            System.err.println("[OpenCL-Sparse-Init] Initializing kernels...");
            initKernels();
            
            isInitialized = true;
            System.err.println("[OpenCL-Sparse-Init] SUCCESS: Native OpenCL Sparse Backend is READY.");
        } catch (Throwable e) {
            isInitialized = false;
            initAttempted = false;
            System.err.println("[OpenCL-Sparse-Init] CRITICAL FAILURE: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            logger.warn("Failed to initialize OpenCL Sparse Backend: {}", e.getMessage());
        }
    }

    private static void initKernels() {
        try {
            // Sparse Program
            sparseProgram = clCreateProgramWithSource(staticContext, 1, new String[]{KERNEL_SPMV}, null, null);
            clBuildProgram(sparseProgram, 0, null, null, null, null);
            spmvKernel = clCreateKernel(sparseProgram, "spmv_csr", null);

            // Dense Program
            denseProgram = clCreateProgramWithSource(staticContext, 1, new String[]{KERNEL_DENSE}, null, null);
            clBuildProgram(denseProgram, 0, null, null, null, null);
            matMulKernel = clCreateKernel(denseProgram, "matrixMultiply", null);
            vecAddKernel = clCreateKernel(denseProgram, "vectorAdd", null);
            vecSubKernel = clCreateKernel(denseProgram, "vectorSubtract", null);
            vecScalarMulKernel = clCreateKernel(denseProgram, "vectorScalarMultiply", null);
            saxpyKernel = clCreateKernel(denseProgram, "saxpy", null);
            dotPartialKernel = clCreateKernel(denseProgram, "dot_partial", null);
            transposeKernel = clCreateKernel(denseProgram, "transpose", null);
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

        cl_mem memA = null, memB = null, memC = null;
        try {
            memA = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * k, Pointer.to(dataA), null);
            memB = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * k * n, Pointer.to(dataB), null);
            memC = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * m * n, null, null);

            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));

            long[] globalWorkSize = new long[]{n, m};
            clEnqueueNDRangeKernel(staticCommandQueue, matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(dataC), 0, null, null);
            C.put(dataC); C.rewind();
        } finally {
            if (memA != null) clReleaseMemObject(memA);
            if (memB != null) clReleaseMemObject(memB);
            if (memC != null) clReleaseMemObject(memC);
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
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for multiply()");
        }
        logger.debug("Entering OpenCL Dense multiply: [{}x{}] * [{}x{}]", a.rows(), a.cols(), b.rows(), b.cols());
        
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        DoubleBuffer da = DoubleBuffer.allocate(m * k);
        for(int i=0; i<m; i++) for(int j=0; j<k; j++) da.put(getRealValue(a.get(i, j)));
        da.flip();
        DoubleBuffer db = DoubleBuffer.allocate(k * n);
        for(int i=0; i<k; i++) for(int j=0; j<n; j++) db.put(getRealValue(b.get(i, j)));
        db.flip();
        DoubleBuffer dc = DoubleBuffer.allocate(m * n);
        
        matrixMultiply(da, db, dc, m, n, k);
        
        double[] result = new double[m * n];
        dc.get(result); dc.rewind();
        return toSparseMatrix(result, m, n, a.getScalarRing());
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Matrix add()");
        }
        return elementWiseVec(toDoubleArray(a), toDoubleArray(b), "vectorAdd", a.rows(), a.cols(), a.getScalarRing());
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Matrix subtract()");
        }
        return elementWiseVec(toDoubleArray(a), toDoubleArray(b), "vectorSubtract", a.rows(), a.cols(), a.getScalarRing());
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Matrix scale()");
        }
        double[] data = toDoubleArray(a);
        double s = getRealValue(scalar);
        return fromDoubleArray(scaleVec(data, s), a.rows(), a.cols(), a.getScalarRing());
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for transpose()");
        }
        int rows = a.rows(); int cols = a.cols();
        double[] src = toDoubleArray(a);
        double[] dst = new double[rows * cols];
        
        cl_mem memA = null, memB = null;
        cl_kernel k = null;
        try {
            memA = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * src.length, Pointer.to(src), null);
            memB = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * src.length, null, null);
            k = clCreateKernel(denseProgram, "transpose", null);

            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(k, 2, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{cols}));
            clEnqueueNDRangeKernel(staticCommandQueue, k, 2, null, new long[]{cols, rows}, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, memB, CL_TRUE, 0, (long)Sizeof.cl_double * src.length, Pointer.to(dst), 0, null, null);

            return fromDoubleArray(dst, cols, rows, a.getScalarRing());
        } finally {
            if (k != null) clReleaseKernel(k);
            if (memA != null) clReleaseMemObject(memA);
            if (memB != null) clReleaseMemObject(memB);
        }
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        return multiplyCSR(a, x);
    }

    public Vector<E> multiplyCSR(Matrix<E> a, Vector<E> x) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for multiplyCSR()");
        }
        logger.debug("Entering OpenCL Sparse multiplyCSR: [{}x{}] * [{}]", a.rows(), a.cols(), x.dimension());

        int rows = a.rows();
        int cols = a.cols();
        
        int[] rowPtr;
        int[] colIndices;
        double[] values;
        int nnz;

        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a;
            rowPtr = sa.getRowPointers();
            colIndices = sa.getColIndices();
            nnz = sa.getNnz();
            values = new double[nnz];
            Object[] valsObj = sa.getValues();
            for (int i = 0; i < nnz; i++) {
                values[i] = getRealValue((E) valsObj[i]);
            }
        } else {
            // Use dense matrix-vector multiply from this provider (which will convert a to dense if needed)
            // But we must avoid recursion. multiply(a, x) calls multiplyCSR.
            // We should use a dense fallback here.
            return multiplyDense(a, x);
        }


        double[] xData = toDoubleVec(x);
        double[] yData = new double[rows];


        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_mem memPtr = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (rows + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem memInd = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIndices), null), CL::clReleaseMemObject);
            cl_mem memVal = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem memX = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * cols, Pointer.to(xData), null), CL::clReleaseMemObject);
            cl_mem memY = tracker.track(clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * rows, null, null), CL::clReleaseMemObject);

            cl_kernel spmvKernel = tracker.track(clCreateKernel(sparseProgram, "spmv_csr", null), CL::clReleaseKernel);
            clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(memPtr));
            clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(memInd));
            clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(memVal));
            clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(memX));
            clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(memY));

            long[] globalWorkSize = new long[]{rows};
            clEnqueueNDRangeKernel(staticCommandQueue, spmvKernel, 1, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, memY, CL_TRUE, 0, (long)Sizeof.cl_double * rows, Pointer.to(yData), 0, null, null);
            
            return toVector(yData, a.getScalarRing());
        }
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Vector add()");
        }
        return toRealVector(vecOp(toDoubleVec(a), toDoubleVec(b), "vectorAdd"));
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Vector subtract()");
        }
        return toRealVector(vecOp(toDoubleVec(a), toDoubleVec(b), "vectorSubtract"));
    }

    @Override
    public Vector<E> multiply(Vector<E> v, E s) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for Vector scale()");
        }
        return toVector(scaleVec(toDoubleVec(v), getRealValue(s)), v.getScalarRing());
    }

    @Override
    public E dot(Vector<E> v1, Vector<E> v2) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for dot()");
        }
        double[] products = vecOp(toDoubleVec(v1), toDoubleVec(v2), "dot_partial");
        double sum = 0; for(double d : products) sum += d;
        return (E) Real.of(sum);
    }

    @Override
    public E norm(Vector<E> v) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + ": OpenCL Sparse not available for norm()");
        }
        double[] d = toDoubleVec(v);
        double[] sq = vecOp(d, d, "dot_partial");
        double sum = 0; for(double s : sq) sum += s;
        return (E) Real.of(Math.sqrt(sum));
    }

    // Helper methods (internal)
    private Matrix<E> elementWiseVec(double[] a, double[] b, String kernelName, int rows, int cols, Ring<E> ring) {
        return toSparseMatrix(vecOp(a, b, kernelName), rows, cols, ring);
    }

    private double[] vecOp(double[] a, double[] b, String kernelName) {
        int n = a.length;
        double[] result = new double[n];
        cl_mem mA = null, mB = null, mC = null;
        cl_kernel k = null;
        try {
            mA = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(a), null);
            mB = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(b), null);
            mC = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null);
            k = clCreateKernel(denseProgram, kernelName, null);

            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(mB));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(staticCommandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
            return result;
        } finally {
            if (k != null) clReleaseKernel(k);
            if (mA != null) clReleaseMemObject(mA);
            if (mB != null) clReleaseMemObject(mB);
            if (mC != null) clReleaseMemObject(mC);
        }
    }

    private double[] scaleVec(double[] a, double s) {
        int n = a.length;
        double[] result = new double[n];
        cl_mem mA = null, mC = null;
        cl_kernel k = null;
        try {
            mA = clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(a), null);
            mC = clCreateBuffer(staticContext, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * n, null, null);
            k = clCreateKernel(denseProgram, "vectorScalarMultiply", null);

            clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(mA));
            clSetKernelArg(k, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
            clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(mC));
            clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clEnqueueNDRangeKernel(staticCommandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(staticCommandQueue, mC, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(result), 0, null, null);
            return result;
        } finally {
            if (k != null) clReleaseKernel(k);
            if (mA != null) clReleaseMemObject(mA);
            if (mC != null) clReleaseMemObject(mC);
        }
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        double[] data = new double[rows * cols];
        for(int i=0; i<rows; i++) for(int j=0; j<cols; j++) data[i*cols + j] = getRealValue(m.get(i, j));
        return data;
    }

    private Matrix<E> fromDoubleArray(double[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for(int i=0; i<data.length; i++) values[i] = castScalar(data[i], ring);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(values, rows, cols, ring);
    }

    private Matrix<E> fromFloatArray(float[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for(int i=0; i<data.length; i++) values[i] = (E) RealFloat.create(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(values, rows, cols, ring);
    }

    private double[] toDoubleVec(Vector<E> v) {
        double[] data = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) {
            data[i] = getRealValue(v.get(i));
        }
        return data;
    }

    private Vector<E> toVector(double[] data, Ring<E> ring) {
        return toSparseVector(data, ring);
    }

    private Vector<E> toRealVector(double[] data) {
        return toVector(data, (Ring<E>) Reals.getInstance());
    }

    private Vector<E> toSparseVector(double[] data, Ring<E> ring) {
        int dim = data.length;
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, ring.zero());
        for (int i = 0; i < dim; i++) {
            if (data[i] != 0.0) storage.set(i, castScalar(data[i], ring));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(storage, this, ring);
    }

    private Vector<E> toSparseVector(float[] data, Ring<E> ring) {
        int dim = data.length;
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, ring.zero());
        for (int i = 0; i < dim; i++) {
            if (data[i] != 0.0f) storage.set(i, (E) RealFloat.create(data[i]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(storage, this, ring);
    }

    private Matrix<E> toSparseMatrix(double[] data, int rows, int cols, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double val = data[i * cols + j];
                if (val != 0.0) storage.set(i, j, castScalar(val, ring));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(storage, ring);
    }

    private E castScalar(double val, Ring<E> ring) {
        Object zero = ring.zero();
        if (zero instanceof RealFloat) return (E) RealFloat.create((float) val);
        if (zero instanceof RealDouble) return (E) RealDouble.of(val);
        return (E) Real.of(val);
    }

    // Decompositions and advanced operations (Fallbacks removed as per requirements)
    @Override
    public E trace(Matrix<E> A) {
        if (A.rows() != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = A.rows();
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) A.getScalarRing();
        E sum = ring.zero();
        for (int i = 0; i < n; i++) {
            sum = ring.add(sum, A.get(i, i));
        }
        return sum;
    }

    @Override
    public E determinant(Matrix<E> A) {
        throw new UnsupportedOperationException("OpenCL Sparse Backend does not support determinant yet.");
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable() || (!isInitialized && !attemptInitialization())) {
            throw new UnsupportedOperationException(getName() + " not available");
        }
        
        // For now, only handle sparse matrices with CG
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            try {
                // Use BiCGSTAB as default for non-symmetric or general sparse
                return solveBiCGSTAB((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a, b, 1000, 1e-10);
            } catch (Exception e) {
                logger.warn("OpenCL BiCGSTAB Solve failed, falling back to CG: {}", e.getMessage());
                try {
                    return solveCG((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a, b, 1000, 1e-10);
                } catch (Exception e2) {
                    logger.error("OpenCL CG Solve also failed: {}", e2.getMessage());
                    throw new RuntimeException("Iterative solve failed", e2);
                }
            }
        }
        
        throw new UnsupportedOperationException("OpenCL direct dense solve not implemented");
    }

    private Vector<E> solveCG(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Vector<E> b, int maxIter, double tol) throws Exception {
        int n = a.rows();
        int nnz = a.getNnz();
        
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        double[] values = new double[nnz];
        Object[] valsObj = a.getValues();
        for (int i = 0; i < nnz; i++) values[i] = getRealValue((E) valsObj[i]);
        double[] bData = toDoubleVec(b);
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_mem mPtr = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(bData), null), CL::clReleaseMemObject);
            
            cl_mem mX = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            clEnqueueWriteBuffer(staticCommandQueue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(new double[n]), 0, null, null);
            
            cl_mem mR = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mP = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mAp = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);

            clEnqueueCopyBuffer(staticCommandQueue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
            clEnqueueCopyBuffer(staticCommandQueue, mR, mP, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
            
            double rsOld = gpuDot_internal(dotPartialKernel, mR, mR, mTemp, n);

            for (int i = 0; i < maxIter; i++) {
                clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(mPtr));
                clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(mInd));
                clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(mVal));
                clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(mAp));
                clEnqueueNDRangeKernel(staticCommandQueue, spmvKernel, 1, null, new long[]{n}, null, 0, null, null);

                double pAp = gpuDot_internal(dotPartialKernel, mP, mAp, mTemp, n);
                double alpha = rsOld / pAp;

                clSetKernelArg(saxpyKernel, 0, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(saxpyKernel, 1, Sizeof.cl_mem, Pointer.to(mX));
                clSetKernelArg(saxpyKernel, 2, Sizeof.cl_double, Pointer.to(new double[]{alpha}));
                clSetKernelArg(saxpyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, saxpyKernel, 1, null, new long[]{n}, null, 0, null, null);

                clSetKernelArg(saxpyKernel, 0, Sizeof.cl_mem, Pointer.to(mAp));
                clSetKernelArg(saxpyKernel, 1, Sizeof.cl_mem, Pointer.to(mR));
                clSetKernelArg(saxpyKernel, 2, Sizeof.cl_double, Pointer.to(new double[]{-alpha}));
                clSetKernelArg(saxpyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, saxpyKernel, 1, null, new long[]{n}, null, 0, null, null);

                double rsNew = gpuDot_internal(dotPartialKernel, mR, mR, mTemp, n);
                if (Math.sqrt(rsNew) < tol) break;

                double beta = rsNew / rsOld;
                gpuScale_internal(vecScalarMulKernel, mP, beta, n);
                gpuAdd_internal(vecAddKernel, mR, mP, n); 

                rsOld = rsNew;
            }

            double[] xRes = new double[n];
            clEnqueueReadBuffer(staticCommandQueue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(xRes), 0, null, null);
            return toRealVector(xRes);
        } catch (Exception e) {
            logger.error("OpenCL CG solver failed: {}", e.getMessage());
            throw new RuntimeException("CG solver failure", e);
        }
    }

    private Vector<E> solveBiCGSTAB(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Vector<E> b, int maxIter, double tol) throws Exception {
        int n = a.rows();
        int nnz = a.getNnz();
        
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        double[] values = new double[nnz];
        Object[] valsObj = a.getValues();
        for (int i = 0; i < nnz; i++) values[i] = getRealValue((E) valsObj[i]);
        double[] bData = toDoubleVec(b);
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_mem mPtr = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (n + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem mInd = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIdx), null), CL::clReleaseMemObject);
            cl_mem mVal = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem mB = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * n, Pointer.to(bData), null), CL::clReleaseMemObject);
            
            cl_mem mX = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            clEnqueueWriteBuffer(staticCommandQueue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(new double[n]), 0, null, null);
            cl_mem mR = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mR0 = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mP = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mV = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mS = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mT = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            cl_mem mTemp = tracker.track(clCreateBuffer(staticContext, CL_MEM_READ_WRITE, (long)Sizeof.cl_double * n, null, null), CL::clReleaseMemObject);
            
            clEnqueueCopyBuffer(staticCommandQueue, mB, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
            clEnqueueCopyBuffer(staticCommandQueue, mR, mR0, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
            
            double rho = 1, alpha = 1, omega = 1;
            
            for (int i = 0; i < maxIter; i++) {
                double rhoNext = gpuDot_internal(dotPartialKernel, mR0, mR, mTemp, n);
                double beta = (rhoNext / rho) * (alpha / omega);
                rho = rhoNext;

                clSetKernelArg(saxpyKernel, 0, Sizeof.cl_mem, Pointer.to(mV));
                clSetKernelArg(saxpyKernel, 1, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(saxpyKernel, 2, Sizeof.cl_double, Pointer.to(new double[]{-omega}));
                clSetKernelArg(saxpyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, saxpyKernel, 1, null, new long[]{n}, null, 0, null, null);

                gpuScale_internal(vecScalarMulKernel, mP, beta, n);
                gpuAdd_internal(vecAddKernel, mR, mP, n);

                clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(mPtr));
                clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(mInd));
                clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(mVal));
                clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(mV));
                clEnqueueNDRangeKernel(staticCommandQueue, spmvKernel, 1, null, new long[]{n}, null, 0, null, null);

                alpha = rho / gpuDot_internal(dotPartialKernel, mR0, mV, mTemp, n);

                clEnqueueCopyBuffer(staticCommandQueue, mR, mS, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                clSetKernelArg(saxpyKernel, 0, Sizeof.cl_mem, Pointer.to(mV));
                clSetKernelArg(saxpyKernel, 1, Sizeof.cl_mem, Pointer.to(mS));
                clSetKernelArg(saxpyKernel, 2, Sizeof.cl_double, Pointer.to(new double[]{-alpha}));
                clSetKernelArg(saxpyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, saxpyKernel, 1, null, new long[]{n}, null, 0, null, null);

                if (Math.sqrt(gpuDot_internal(dotPartialKernel, mS, mS, mTemp, n)) < tol) {
                    clSetKernelArg(saxpyKernel, 0, Sizeof.cl_mem, Pointer.to(mP));
                    clSetKernelArg(saxpyKernel, 1, Sizeof.cl_mem, Pointer.to(mX));
                    clSetKernelArg(saxpyKernel, 2, Sizeof.cl_double, Pointer.to(new double[]{alpha}));
                    clSetKernelArg(saxpyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                    clEnqueueNDRangeKernel(staticCommandQueue, saxpyKernel, 1, null, new long[]{n}, null, 0, null, null);
                    break;
                }

                clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(mPtr));
                clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(mInd));
                clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(mVal));
                clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(mS));
                clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(mT));
                clEnqueueNDRangeKernel(staticCommandQueue, spmvKernel, 1, null, new long[]{n}, null, 0, null, null);

                omega = gpuDot_internal(dotPartialKernel, mS, mT, mTemp, n) / gpuDot_internal(dotPartialKernel, mT, mT, mTemp, n);

                clSetKernelArg(saxpyKernel, 0, Sizeof.cl_mem, Pointer.to(mP));
                clSetKernelArg(saxpyKernel, 1, Sizeof.cl_mem, Pointer.to(mX));
                clSetKernelArg(saxpyKernel, 2, Sizeof.cl_double, Pointer.to(new double[]{alpha}));
                clSetKernelArg(saxpyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, saxpyKernel, 1, null, new long[]{n}, null, 0, null, null);

                clSetKernelArg(saxpyKernel, 0, Sizeof.cl_mem, Pointer.to(mS));
                clSetKernelArg(saxpyKernel, 1, Sizeof.cl_mem, Pointer.to(mX));
                clSetKernelArg(saxpyKernel, 2, Sizeof.cl_double, Pointer.to(new double[]{omega}));
                clSetKernelArg(saxpyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, saxpyKernel, 1, null, new long[]{n}, null, 0, null, null);

                clEnqueueCopyBuffer(staticCommandQueue, mS, mR, 0, 0, (long)Sizeof.cl_double * n, 0, null, null);
                clSetKernelArg(saxpyKernel, 0, Sizeof.cl_mem, Pointer.to(mT));
                clSetKernelArg(saxpyKernel, 1, Sizeof.cl_mem, Pointer.to(mR));
                clSetKernelArg(saxpyKernel, 2, Sizeof.cl_double, Pointer.to(new double[]{-omega}));
                clSetKernelArg(saxpyKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
                clEnqueueNDRangeKernel(staticCommandQueue, saxpyKernel, 1, null, new long[]{n}, null, 0, null, null);

                if (Math.sqrt(gpuDot_internal(dotPartialKernel, mR, mR, mTemp, n)) < tol) break;
            }

            double[] xRes = new double[n];
            clEnqueueReadBuffer(staticCommandQueue, mX, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(xRes), 0, null, null);
            return toRealVector(xRes);
        } catch (Exception e) {
            logger.error("OpenCL BiCGSTAB solver failed: {}", e.getMessage());
            throw new RuntimeException("BiCGSTAB solver failure", e);
        }
    }


    private double gpuDot_internal(cl_kernel kDot, cl_mem a, cl_mem b, cl_mem mTemp, int n) {
        clSetKernelArg(kDot, 0, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(kDot, 1, Sizeof.cl_mem, Pointer.to(b));
        clSetKernelArg(kDot, 2, Sizeof.cl_mem, Pointer.to(mTemp));
        clSetKernelArg(kDot, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(staticCommandQueue, kDot, 1, null, new long[]{n}, null, 0, null, null);
        
        double[] partial = new double[n];
        clEnqueueReadBuffer(staticCommandQueue, mTemp, CL_TRUE, 0, (long)Sizeof.cl_double * n, Pointer.to(partial), 0, null, null);
        double sum = 0; for(double d : partial) sum += d;
        return sum;
    }


    private void gpuScale_internal(cl_kernel k, cl_mem a, double s, int n) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(a));
        clSetKernelArg(k, 1, Sizeof.cl_double, Pointer.to(new double[]{s}));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(a)); // In-place
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(staticCommandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
    }


    private void gpuAdd_internal(cl_kernel k, cl_mem x, cl_mem y, int n) {
        clSetKernelArg(k, 0, Sizeof.cl_mem, Pointer.to(x));
        clSetKernelArg(k, 1, Sizeof.cl_mem, Pointer.to(y));
        clSetKernelArg(k, 2, Sizeof.cl_mem, Pointer.to(y)); // In-place (p = r + p)
        clSetKernelArg(k, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
        clEnqueueNDRangeKernel(staticCommandQueue, k, 1, null, new long[]{n}, null, 0, null, null);
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> A_sparse = ensureSparse(a);
        try {
            return solveBiCGSTAB(A_sparse, b, maxIterations, getRealValue(tolerance));
        } catch (Exception e) {
            throw new RuntimeException("OpenCL BiCGSTAB failed", e);
        }
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> A_sparse = ensureSparse(a);
        try {
            return solveCG(A_sparse, b, maxIterations, getRealValue(tolerance));
        } catch (Exception e) {
            throw new RuntimeException("OpenCL CG failed", e);
        }
    }

    @Override
    public Vector<E> gmres(Matrix<E> A, Vector<E> b, Vector<E> x0, E tol, int maxIter, int restarts) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Backend not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> A_sparse = ensureSparse(A);
        
        NativeOpenCLGMRESSolver solver = new NativeOpenCLGMRESSolver(
            (org.episteme.nativ.technical.backend.gpu.opencl.OpenCLExecutionContext) createContext(),
            sparseProgram,
            denseProgram,
            this::toRealVector
        );
        
        return solver.solve(A_sparse, b, x0, getRealValue(tol), maxIter, restarts);
    }


    private class NativeOpenCLGMRESSolver {
        private final Logger logger = LoggerFactory.getLogger(NativeOpenCLGMRESSolver.class);

        private final OpenCLExecutionContext context;
        private final cl_program sparseProgram;
        private final cl_program denseProgram;
        private final Function<double[], Vector<E>> vectorFactory;

        public NativeOpenCLGMRESSolver(OpenCLExecutionContext context, cl_program sparseProgram, cl_program denseProgram, Function<double[], Vector<E>> vectorFactory) {
            this.context = context;
            this.sparseProgram = sparseProgram;
            this.denseProgram = denseProgram;
            this.vectorFactory = vectorFactory;
        }

        public Vector<E> solve(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> A, Vector<E> b, Vector<E> x0, double tol, int maxIter, int restarts) {
            int n = b.dimension();
            int nnz = A.getNnz();
            int[] rowPtr = A.getRowPointers();
            int[] colIdx = A.getColIndices();
            double[] vals = new double[nnz];
            Object[] valsObj = A.getValues();
            for (int i = 0; i < nnz; i++) vals[i] = getRealValue((E) valsObj[i]);

            double[] bArr = new double[n];
            for (int i = 0; i < n; i++) bArr[i] = getRealValue(b.get(i));

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
                    for (int i = 0; i < n; i++) x0Arr[i] = getRealValue(x0.get(i));
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

    private org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> ensureSparse(Matrix<E> A) {
        if (A instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            return (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) A;
        }
        // Fallback: convert dense to sparse
        int rows = A.rows();
        int cols = A.cols();
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, A.getScalarRing().zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = A.get(i, j);
                if (!val.isZero()) storage.set(i, j, val);
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, A.getScalarRing());
    }
    private double getRealValue(E val) {
        if (val == null) return 0.0;
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof Real r) return r.doubleValue();
        if (val instanceof Complex c) return c.real();
        try {
            return (double) val.getClass().getMethod("doubleValue").invoke(val);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Vector<E> multiplyDense(Matrix<E> a, Vector<E> x) {
        int rows = a.rows();
        int cols = a.cols();
        double[] dataA = toDoubleArray(a);
        double[] dataX = toDoubleVec(x);
        double[] dataY = new double[rows];

        DoubleBuffer da = DoubleBuffer.wrap(dataA);
        DoubleBuffer dx = DoubleBuffer.wrap(dataX);
        DoubleBuffer dy = DoubleBuffer.wrap(dataY);

        matrixMultiply(da, dx, dy, rows, 1, cols);
        dy.rewind();
        dy.get(dataY);
        return toVector(dataY, a.getScalarRing());
    }

    private Complex getComplexValue(E val) {

        if (val == null) return Complex.ZERO;
        if (val instanceof Complex c) return c;
        if (val instanceof Real r) return Complex.of(r.doubleValue());
        if (val instanceof Number n) return Complex.of(n.doubleValue());
        return Complex.of(getRealValue(val));
    }
}
