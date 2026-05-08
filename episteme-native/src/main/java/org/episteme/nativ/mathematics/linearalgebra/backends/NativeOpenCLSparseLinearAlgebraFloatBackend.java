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
import org.episteme.core.mathematics.numbers.real.RealFloat;
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

/**
 * OpenCL implementation of Sparse Linear Algebra Provider for Float precision.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class})
public class NativeOpenCLSparseLinearAlgebraFloatBackend<E extends FieldElement<E>> implements SparseLinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "opencl"; }

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLSparseLinearAlgebraFloatBackend.class);
    
    private cl_program program;
    private cl_kernel spmvKernel;
    
    private volatile boolean initialized = false;

    private synchronized void ensureInitialized() {
        if (initialized) return;
        
        OpenCLManager.ensureInitialized();
        if (!OpenCLManager.isInitialized()) return;

        try {
            cl_context context = OpenCLManager.getContext();
            program = clCreateProgramWithSource(context, 1, new String[]{OpenCLKernels.SPARSE_FLOAT_KERNELS}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            
            spmvKernel = tryCreateKernel(program, "spmv_csr_float");

            initialized = (spmvKernel != null);
            if (initialized) {
                logger.info("Native OpenCL Sparse Float Backend initialized successfully.");
            }
        } catch (Throwable t) {
            logger.error("Failed to initialize OpenCL Sparse Float Backend: {}", t.getMessage());
        }
    }

    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        ensureInitialized();
        return initialized;
    }

    public boolean isExplicitlyDisabled() {
        return Boolean.getBoolean("episteme.backend.native.disabled") ||
               Boolean.getBoolean("episteme.backend.opencl.disabled") || 
               Boolean.getBoolean("episteme.backend.gpu.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra-opencl.disabled");
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        return ring.zero() instanceof RealFloat;
    }

    @Override
    public String getId() { return "opencl-sparse-float"; }

    @Override
    public String getName() { return "Native OpenCL Sparse Float Backend"; }

    @Override public int getPriority() { return 110; }
    @Override public void shutdown() { close(); }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        if (!isAvailable()) throw new UnsupportedOperationException("OpenCL Sparse Float Backend not available");
        
        SparseMatrix<E> sa = ensureSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        int nnz = sa.getNnz();
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIndices = sa.getColIndices();
        float[] values = new float[nnz];
        Object[] valsObj = sa.getValues();
        for (int i = 0; i < nnz; i++) values[i] = ((Number) valsObj[i]).floatValue();
        
        float[] xData = new float[cols];
        for (int i = 0; i < cols; i++) xData[i] = ((Number) x.get(i)).floatValue();
        
        float[] yData = new float[rows];
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            cl_context context = OpenCLManager.getContext();
            cl_command_queue queue = OpenCLManager.getCommandQueue();
            
            cl_mem memPtr = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * (rows + 1), Pointer.to(rowPtr), null), CL::clReleaseMemObject);
            cl_mem memInd = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_int * nnz, Pointer.to(colIndices), null), CL::clReleaseMemObject);
            cl_mem memVal = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * nnz, Pointer.to(values), null), CL::clReleaseMemObject);
            cl_mem memX = tracker.track(clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, (long)Sizeof.cl_float * cols, Pointer.to(xData), null), CL::clReleaseMemObject);
            cl_mem memY = tracker.track(clCreateBuffer(context, CL_MEM_WRITE_ONLY, (long)Sizeof.cl_float * rows, null, null), CL::clReleaseMemObject);
            
            clSetKernelArg(spmvKernel, 0, Sizeof.cl_int, Pointer.to(new int[]{rows}));
            clSetKernelArg(spmvKernel, 1, Sizeof.cl_mem, Pointer.to(memPtr));
            clSetKernelArg(spmvKernel, 2, Sizeof.cl_mem, Pointer.to(memInd));
            clSetKernelArg(spmvKernel, 3, Sizeof.cl_mem, Pointer.to(memVal));
            clSetKernelArg(spmvKernel, 4, Sizeof.cl_mem, Pointer.to(memX));
            clSetKernelArg(spmvKernel, 5, Sizeof.cl_mem, Pointer.to(memY));
            
            clEnqueueNDRangeKernel(queue, spmvKernel, 1, null, new long[]{rows}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memY, CL_TRUE, 0, (long)Sizeof.cl_float * rows, Pointer.to(yData), 0, null, null);
            
            return fromFloatArray(yData, sa.getScalarRing());
        }
    }

    private Vector<E> fromFloatArray(float[] data, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(data.length, ring.zero());
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0.0f) storage.set(i, (E) RealFloat.create(data[i]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, ring);
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
            clReleaseProgram(program);
            program = null;
        }
    }

    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }

    private SparseMatrix<E> ensureSparse(Matrix<E> a) {
        if (a instanceof SparseMatrix) return (SparseMatrix<E>) a;
        Ring<E> r = a.getScalarRing();
        SparseMatrix<E> s = SparseMatrix.zeros(a.rows(), a.cols(), r);
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) {
                E val = a.get(i, j);
                if (!val.equals(r.zero())) s.set(i, j, val);
            }
        }
        return s;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        OpenCLManager.ensureInitialized();
        return new org.episteme.nativ.technical.backend.gpu.opencl.OpenCLExecutionContext(
            OpenCLManager.getContext(), OpenCLManager.getCommandQueue(), OpenCLManager.getDevice());
    }

    @Override
    public org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[] getDevices() {
        if (!isAvailable()) return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[0];
        return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[]{
            new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo(
                "OpenCL Device", 0, 0, "OpenCL")
        };
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        return 0;
    }

    @Override
    public void copyToGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {}

    @Override
    public void copyFromGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {}

    @Override
    public void freeGPUMemory(long gpuHandle) {}

    @Override
    public void synchronize() {}

    @Override
    public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) {
        throw new UnsupportedOperationException("Matrix multiply for DoubleBuffer not implemented in sparse backend");
    }
}
