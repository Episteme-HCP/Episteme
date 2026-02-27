/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.fluids.backends;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.physics.classical.matter.fluids.LatticeBoltzmannProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.nativ.mathematics.linearalgebra.backends.NativeOpenCLSparseLinearAlgebraBackend;
import org.jscience.nativ.mathematics.linearalgebra.backends.OpenCLExecutionContext;

import static org.jocl.CL.*;
import org.jocl.*;

import java.util.logging.Logger;

import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ExecutionContext;
import java.nio.DoubleBuffer;

/**
 * GPU-accelerated Lattice Boltzmann Method (LBM) backend using OpenCL.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({LatticeBoltzmannProvider.class, GPUBackend.class, ComputeBackend.class, Backend.class})
public class NativeOpenCLLatticeBoltzmannBackend implements LatticeBoltzmannProvider, GPUBackend {

    private static final Logger LOGGER = Logger.getLogger(NativeOpenCLLatticeBoltzmannBackend.class.getName());
    
    private static final String KERNEL_SOURCE = 
    "__kernel void lbm_d2q9(__global double* f, __global double* fNew, __global int* obstacle, int width, int height, double omega) {\n" +
    "    int x = get_global_id(0);\n" +
    "    int y = get_global_id(1);\n" +
    "    if (x >= width || y >= height) return;\n" +
    "\n" +
    "    int idx = y * width + x;\n" +
    "    int base = idx * 9;\n" +
    "\n" +
    "    // D2Q9 Constants\n" +
    "    int ex[9] = {0, 1, 0, -1, 0, 1, -1, -1, 1};\n" +
    "    int ey[9] = {0, 0, 1, 0, -1, 1, 1, -1, -1};\n" +
    "    double w[9] = {4.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/9.0, 1.0/36.0, 1.0/36.0, 1.0/36.0, 1.0/36.0};\n" +
    "    int opp[9] = {0, 3, 4, 1, 2, 7, 8, 5, 6};\n" +
    "\n" +
    "    // Check obstacle\n" +
    "    if (obstacle[idx] != 0) {\n" +
    "        return;\n" +
    "    }\n" +
    "\n" +
    "    // Macroscopic\n" +
    "    double rho = 0;\n" +
    "    double ux = 0;\n" +
    "    double uy = 0;\n" +
    "    double f_local[9];\n" +
    "\n" +
    "    for(int i=0; i<9; i++) {\n" +
    "        f_local[i] = f[base + i];\n" +
    "        rho += f_local[i];\n" +
    "        ux += f_local[i] * ex[i];\n" +
    "        uy += f_local[i] * ey[i];\n" +
    "    }\n" +
    "    \n" +
    "    if (rho > 0) {\n" +
    "        ux /= rho;\n" +
    "        uy /= rho;\n" +
    "    }\n" +
    "    \n" +
    "    double u2 = ux*ux + uy*uy;\n" +
    "    \n" +
    "    // Collide & Stream\n" +
    "    for(int i=0; i<9; i++) {\n" +
    "        double cu = 3.0 * (ux * ex[i] + uy * ey[i]);\n" +
    "        double feq = rho * w[i] * (1.0 + cu + 0.5 * cu * cu - 1.5 * u2);\n" +
    "        double f_post = f_local[i] + omega * (feq - f_local[i]);\n" +
    "        \n" +
    "        // Stream to neighbor\n" +
    "        int nx = (x + ex[i] + width) % width;\n" +
    "        int ny = (y + ey[i] + height) % height;\n" +
    "        int n_idx = (ny * width + nx) * 9 + i;\n" +
    "        \n" +
    "        fNew[n_idx] = f_post;\n" +
    "    }\n" +
    "}\n";

    private final NativeOpenCLSparseLinearAlgebraBackend backend = new NativeOpenCLSparseLinearAlgebraBackend();
    private boolean initialized = false;
    private cl_program program;
    private cl_kernel kernel;
 
    @Override
    public org.jscience.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.jscience.core.technical.backend.HardwareAccelerator.GPU;
    }
 
    @Override
    public String getAlgorithmType() {
        return "fluid-dynamics";
    }

    @Override
    public boolean isAvailable() {
        return backend.isAvailable();
    }

    public synchronized void initialize() {
        if (initialized) return;
        if (!backend.isAvailable()) throw new IllegalStateException("OpenCL not available");

        try {
            OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
            cl_context context = ctx.getContext();

            // Create Program
            program = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SOURCE}, null, null);
            clBuildProgram(program, 0, null, null, null, null);

            // Create Kernel
            kernel = clCreateKernel(program, "lbm_d2q9", null);

            initialized = true;
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize OpenCL LBM: " + e.getMessage());
            throw new RuntimeException("OpenCL initialization error", e);
        }
    }

    @Override
    public int getPriority() {
        return 65;
    }

    /** Minimum grid size where GPU LBM outperforms CPU. */
    private static final int GPU_LBM_THRESHOLD = 256;

    /**
     * Context-aware scoring that accounts for GPU data transfer overhead.
     * <p>
     * LBM is inherently parallelizable, so GPU benefits kick in at
     * relatively small grid sizes.
     * </p>
     */
    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1;
        double base = getPriority();
        if (context.getDataSize() < GPU_LBM_THRESHOLD) base -= 100;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 30;
        if (context.hasHint(OperationContext.Hint.BATCH)) base += 20;
        if (context.hasHint(OperationContext.Hint.LOW_LATENCY)) base -= 50;
        if (context.hasHint(OperationContext.Hint.HIGH_THROUGHPUT)) base += 25;
        return base;
    }

    @Override
    public void evolve(double[][][] f, boolean[][] obstacle, double omega) {
        initialize();
        
        int width = f.length;
        int height = f[0].length;
        int size = width * height;
        
        // Flatten data for GPU
        double[] fFlat = new double[size * 9];
        int[] obsFlat = new int[size];
        
        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++) {
                int idx = y * width + x;
                for(int i=0; i<9; i++) fFlat[idx * 9 + i] = f[x][y][i];
                if (obstacle != null) obsFlat[idx] = obstacle[x][y] ? 1 : 0;
            }
        }
        
        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();
        
        // Allocate
        cl_mem memF = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * fFlat.length, Pointer.to(fFlat), null);
        cl_mem memFNew = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * fFlat.length, null, null);
        cl_mem memObs = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * obsFlat.length, Pointer.to(obsFlat), null);
        
        try {
            // Args
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memF));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memFNew));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memObs));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{width}));
            clSetKernelArg(kernel, 4, Sizeof.cl_int, Pointer.to(new int[]{height}));
            clSetKernelArg(kernel, 5, Sizeof.cl_double, Pointer.to(new double[]{omega}));
            
            // Execute
            long[] globalWorkSize = new long[]{width, height};
            clEnqueueNDRangeKernel(queue, kernel, 2, null, globalWorkSize, null, 0, null, null);
            
            // Read back
            clEnqueueReadBuffer(queue, memFNew, CL_TRUE, 0, Sizeof.cl_double * fFlat.length, Pointer.to(fFlat), 0, null, null);
            
            // Unflatten
            for(int x=0; x<width; x++) {
                for(int y=0; y<height; y++) {
                    int idx = y * width + x;
                    if (obstacle != null && obstacle[x][y]) {
                         // obstacle handling...
                    } else {
                        for(int i=0; i<9; i++) f[x][y][i] = fFlat[idx * 9 + i];
                    }
                }
            }
            
        } finally {
            clReleaseMemObject(memF);
            clReleaseMemObject(memFNew);
            clReleaseMemObject(memObs);
        }
    }

    @Override
    public void evolve(Real[][][] f, boolean[][] obstacle, Real omega) {
         int width = f.length;
         int height = f[0].length;
         double[][][] fPrim = new double[width][height][9];
         
         for(int x=0; x<width; x++) {
             for(int y=0; y<height; y++) {
                 for(int i=0; i<9; i++) fPrim[x][y][i] = f[x][y][i].doubleValue();
             }
         }
         
         evolve(fPrim, obstacle, omega.doubleValue());
         
         for(int x=0; x<width; x++) {
             for(int y=0; y<height; y++) {
                 for(int i=0; i<9; i++) f[x][y][i] = Real.of(fPrim[x][y][i]);
             }
         }
    }

    @Override
    public String getName() {
        return "Native OpenCL (LBM GPU)";
    }


    @Override
    public DeviceInfo[] getDevices() {
        return new DeviceInfo[0];
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public long allocateGPUMemory(long sizeBytes) { return 0; }

    @Override
    public void copyToGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {}

    @Override
    public void copyFromGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {}

    @Override
    public void freeGPUMemory(long gpuHandle) {}

    @Override
    public void synchronize() {}

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {}

    @Override
    public ExecutionContext createContext() {
        return backend.createContext();
    }
}
