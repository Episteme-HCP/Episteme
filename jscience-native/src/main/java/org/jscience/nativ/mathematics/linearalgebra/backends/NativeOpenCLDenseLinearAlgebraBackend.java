/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jocl.*;
import static org.jocl.CL.*;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.nativ.technical.backend.nativ.NativeBackend;
import com.google.auto.service.AutoService;

import java.nio.DoubleBuffer;

/**
 * OpenCL implementation of Dense Linear Algebra Provider.
 * Uses JOCL to provide cross-platform GPU acceleration.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, AlgorithmProvider.class, GPUBackend.class})
public class NativeOpenCLDenseLinearAlgebraBackend implements NativeBackend, LinearAlgebraProvider<Real>, GPUBackend {

    private cl_context context;
    private cl_command_queue commandQueue;
    private cl_kernel matMulKernel;
    private cl_program program;
    private boolean initialized = false;


    private static final String KERNEL_SOURCE = 
        "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
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

    private synchronized void init() {
        if (initialized) return;
        try {
            setExceptionsEnabled(true);
            int[] numPlatformsArray = new int[1];
            clGetPlatformIDs(0, null, numPlatformsArray);
            if (numPlatformsArray[0] == 0) return;

            cl_platform_id[] platforms = new cl_platform_id[numPlatformsArray[0]];
            clGetPlatformIDs(platforms.length, platforms, null);
            cl_platform_id platform = platforms[0];

            cl_device_id[] devices = new cl_device_id[1];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 1, devices, null);
            cl_device_id device = devices[0];

            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
            context = clCreateContext(contextProperties, 1, devices, null, null, null);
            
            cl_queue_properties queueProperties = new cl_queue_properties();
            commandQueue = clCreateCommandQueueWithProperties(context, device, queueProperties, null);

            program = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SOURCE}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            matMulKernel = clCreateKernel(program, "matrixMultiply", null);

            initialized = true;
        } catch (Exception e) {
            initialized = false;
        }
    }

    @Override public boolean isAvailable() { if (!initialized) init(); return initialized; }
    @Override public boolean isLoaded() { return initialized; }
    @Override public String getName() { return "Native OpenCL Dense Backend"; }
    @Override public int getPriority() { return 105; }
    @Override public boolean isCompatible(Ring<?> ring) { return ring instanceof Reals; }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL not available");

        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        double[] h_A = toDoubleArray(a);
        double[] h_B = toDoubleArray(b);
        double[] h_C = new double[m * n];

        cl_mem memA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * m * k, Pointer.to(h_A), null);
        cl_mem memB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_double * k * n, Pointer.to(h_B), null);
        cl_mem memC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_double * m * n, null, null);

        try {
            clSetKernelArg(matMulKernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(matMulKernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(matMulKernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(matMulKernel, 3, Sizeof.cl_int, Pointer.to(new int[]{m}));
            clSetKernelArg(matMulKernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(matMulKernel, 5, Sizeof.cl_int, Pointer.to(new int[]{k}));

            long[] globalWorkSize = new long[]{n, m};
            clEnqueueNDRangeKernel(commandQueue, matMulKernel, 2, null, globalWorkSize, null, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memC, CL_TRUE, 0, (long)Sizeof.cl_double * m * n, Pointer.to(h_C), 0, null, null);
            
            return fromDoubleArray(h_C, m, n);
        } finally {
            clReleaseMemObject(memA);
            clReleaseMemObject(memB);
            clReleaseMemObject(memC);
        }
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                data[i*cols + j] = m.get(i, j).doubleValue();
            }
        }
        return data;
    }

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[data.length];
        for(int i=0; i<data.length; i++) reals[i] = Real.of(data[i]);
        return new DenseMatrix<Real>(reals, rows, cols, Reals.getInstance());
    }

    @Override public String getNativeLibraryName() { return "opencl"; }
    @Override public DeviceInfo[] getDevices() { return new DeviceInfo[0]; }
    @Override public void selectDevice(int deviceId) { }
    @Override public long allocateGPUMemory(long size) { return 0; }
    @Override public void copyToGPU(long handle, DoubleBuffer buffer, long count) { }
    @Override public void copyFromGPU(long handle, DoubleBuffer buffer, long count) { }
    @Override public void freeGPUMemory(long handle) { }
    @Override public void synchronize() { if (commandQueue != null) clFinish(commandQueue); }
    @Override public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) { }

    @Override public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) { throw new UnsupportedOperationException("OpenCL add not implemented"); }
    @Override public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) { throw new UnsupportedOperationException("OpenCL subtract not implemented"); }
    @Override public Matrix<Real> scale(Real scalar, Matrix<Real> a) { throw new UnsupportedOperationException("OpenCL scale not implemented"); }
    @Override public Matrix<Real> transpose(Matrix<Real> a) { throw new UnsupportedOperationException("OpenCL transpose not implemented"); }
    @Override public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL multiply not implemented"); }
    @Override public Vector<Real> add(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL add not implemented"); }
    @Override public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL subtract not implemented"); }
    @Override public Vector<Real> multiply(Vector<Real> vector, Real scalar) { throw new UnsupportedOperationException("OpenCL multiply not implemented"); }
    @Override public Real dot(Vector<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL dot not implemented"); }
    @Override public Real norm(Vector<Real> a) { throw new UnsupportedOperationException("OpenCL norm not implemented"); }
    @Override public Matrix<Real> inverse(Matrix<Real> a) { throw new UnsupportedOperationException("OpenCL inverse not implemented"); }
    @Override public Real determinant(Matrix<Real> a) { throw new UnsupportedOperationException("OpenCL determinant not implemented"); }
    @Override public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) { throw new UnsupportedOperationException("OpenCL solve not implemented"); }

    @Override public double score(OperationContext context) {
        if (!isAvailable()) return -1.0;
        double base = getPriority();
        if (context.getDataSize() < 256) base -= 200;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 50;
        if (context.hasHint(OperationContext.Hint.MAT_MUL)) base += 20;
        return base;
    }

    @Override public org.jscience.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.jscience.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return null;
    }
}
