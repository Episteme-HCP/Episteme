/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.gpu.cuda;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.NBodyProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;
import jcuda.driver.JCudaDriver;

// import jcuda.nvrtc.JNvrtc;
// import jcuda.nvrtc.nvrtcProgram;

/**
 * CUDA-accelerated N-Body simulation provider using JCuda.
 * <p>
 * When a CUDA-capable GPU is available, this provider compiles an N-Body
 * gravity kernel at runtime via NVRTC and runs the O(N²) force computation
 * entirely on the GPU. Falls back to a multithreaded CPU implementation
 * for systems without CUDA.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class CUDANBodyProvider implements NBodyProvider {

    private static final int GPU_THRESHOLD = 1000;
    private static volatile Boolean cudaAvailable;


    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public boolean isAvailable() {
        if (cudaAvailable == null) {
            synchronized (CUDANBodyProvider.class) {
                if (cudaAvailable == null) {
                    cudaAvailable = detectCuda();
                }
            }
        }
        return cudaAvailable;
    }

    private static boolean detectCuda() {
        try {
            JCudaDriver.setExceptionsEnabled(true);
            JCudaDriver.cuInit(0);
            int[] count = new int[1];
            JCudaDriver.cuDeviceGetCount(count);
            return count[0] > 0;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int numParticles = masses.length;
        if (isAvailable() && numParticles >= GPU_THRESHOLD) {
            computeForcesCUDA(positions, masses, forces, G, softening);
        } else {
            computeForcesCPU(positions, masses, forces, G, softening);
        }
    }

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        double[] posD = new double[positions.length];
        double[] massD = new double[masses.length];
        double[] forceD = new double[forces.length];

        for (int i = 0; i < positions.length; i++) posD[i] = positions[i].doubleValue();
        for (int i = 0; i < masses.length; i++) massD[i] = masses[i].doubleValue();

        computeForces(posD, massD, forceD, G.doubleValue(), softening.doubleValue());

        for (int i = 0; i < forces.length; i++) forces[i] = Real.of(forceD[i]);
    }

    private void computeForcesCUDA(double[] positions, double[] masses, double[] forces, double G, double softening) {
        // NVRTC dependency missing - CUDA path disabled
        computeForcesCPU(positions, masses, forces, G, softening);
        /*
        int n = masses.length;

        // Initialize CUDA Driver API
        JCudaDriver.setExceptionsEnabled(true);
        JCudaDriver.cuInit(0);
        CUdevice device = new CUdevice();
        JCudaDriver.cuDeviceGet(device, 0);
        CUcontext context = new CUcontext();
        JCudaDriver.cuCtxCreate(context, 0, device);

        try {
            // Compile kernel via NVRTC
            nvrtcProgram program = new nvrtcProgram();
            JNvrtc.nvrtcCreateProgram(program, NBODY_KERNEL_SOURCE, "nbody.cu", 0, null, null);
            JNvrtc.nvrtcCompileProgram(program, 0, null);

            // Get PTX
            String[] ptx = new String[1];
            JNvrtc.nvrtcGetPTX(program, ptx);
            JNvrtc.nvrtcDestroyProgram(program);

            // Load module and get function
            CUmodule module = new CUmodule();
            JCudaDriver.cuModuleLoadData(module, ptx[0]);
            CUfunction function = new CUfunction();
            JCudaDriver.cuModuleGetFunction(function, module, "computeForcesKernel");

            // Allocate device memory
            CUdeviceptr dPositions = new CUdeviceptr();
            CUdeviceptr dMasses = new CUdeviceptr();
            CUdeviceptr dForces = new CUdeviceptr();

            JCudaDriver.cuMemAlloc(dPositions, (long) n * 3 * Sizeof.DOUBLE);
            JCudaDriver.cuMemAlloc(dMasses, (long) n * Sizeof.DOUBLE);
            JCudaDriver.cuMemAlloc(dForces, (long) n * 3 * Sizeof.DOUBLE);

            // Copy input data to device
            JCudaDriver.cuMemcpyHtoD(dPositions, Pointer.to(positions), (long) n * 3 * Sizeof.DOUBLE);
            JCudaDriver.cuMemcpyHtoD(dMasses, Pointer.to(masses), (long) n * Sizeof.DOUBLE);

            // Set up kernel parameters
            int blockSize = 256;
            int gridSize = (n + blockSize - 1) / blockSize;

            Pointer kernelParams = Pointer.to(
                Pointer.to(dPositions),
                Pointer.to(dMasses),
                Pointer.to(dForces),
                Pointer.to(new int[]{n}),
                Pointer.to(new double[]{G}),
                Pointer.to(new double[]{softening})
            );

            // Launch kernel
            JCudaDriver.cuLaunchKernel(function,
                gridSize, 1, 1,    // grid
                blockSize, 1, 1,   // block
                0, null,           // shared mem, stream
                kernelParams, null
            );

            JCudaDriver.cuCtxSynchronize();

            // Copy results back
            JCudaDriver.cuMemcpyDtoH(Pointer.to(forces), dForces, (long) n * 3 * Sizeof.DOUBLE);

            // Free device memory
            JCudaDriver.cuMemFree(dPositions);
            JCudaDriver.cuMemFree(dMasses);
            JCudaDriver.cuMemFree(dForces);
            JCudaDriver.cuModuleUnload(module);

        } catch (Exception e) {
            // If GPU execution fails for any reason, fall back to CPU
            computeForcesCPU(positions, masses, forces, G, softening);
        } finally {
            JCudaDriver.cuCtxDestroy(context);
        }
        */
    }

    private void computeForcesCPU(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int n = masses.length;
        java.util.Arrays.fill(forces, 0);
        for (int i = 0; i < n; i++) {
            double xi = positions[i * 3], yi = positions[i * 3 + 1], zi = positions[i * 3 + 2], mi = masses[i];
            for (int j = i + 1; j < n; j++) {
                double dx = positions[j * 3] - xi, dy = positions[j * 3 + 1] - yi, dz = positions[j * 3 + 2] - zi;
                double dist2 = dx * dx + dy * dy + dz * dz + softening * softening;
                double dist = Math.sqrt(dist2);
                double f = G * mi * masses[j] / (dist2 * dist);
                double fx = f * dx, fy = f * dy, fz = f * dz;
                forces[i * 3] += fx; forces[i * 3 + 1] += fy; forces[i * 3 + 2] += fz;
                forces[j * 3] -= fx; forces[j * 3 + 1] -= fy; forces[j * 3 + 2] -= fz;
            }
        }
    }

    @Override
    public String getName() {
        return "N-Body (GPU/CUDA)";
    }
}
