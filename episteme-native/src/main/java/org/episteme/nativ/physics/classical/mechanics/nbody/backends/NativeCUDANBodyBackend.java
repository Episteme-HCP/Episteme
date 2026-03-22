/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.physics.classical.mechanics.nbody.backends;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.mechanics.nbody.NBodyProvider;
import org.episteme.core.technical.algorithm.OperationContext;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.Backend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import jcuda.Pointer;
import jcuda.driver.*;
import jcuda.nvrtc.*;
import java.nio.DoubleBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static jcuda.driver.JCudaDriver.*;
import static jcuda.nvrtc.JCudaNvrtc.*;

/**
 * CUDA-accelerated N-Body simulation backend using JCuda.
 */
@AutoService({ComputeBackend.class, GPUBackend.class, Backend.class, NativeBackend.class})
public class NativeCUDANBodyBackend implements NBodyProvider, GPUBackend, NativeBackend {

    private static final Logger logger = LoggerFactory.getLogger(NativeCUDANBodyBackend.class);
    private static final int GPU_THRESHOLD = 512;
    private static volatile Boolean cudaAvailable;
    private boolean initialized = false;
    private CUmodule module;
    private CUfunction kernel;

    private static final String KERNEL_SOURCE = 
        "extern \"C\"\n" +
        "__global__ void nbody_forces(\n" +
        "    const double* positions,\n" +
        "    const double* masses,\n" +
        "    double* forces,\n" +
        "    int num_particles,\n" +
        "    double G,\n" +
        "    double softening)\n" +
        "{\n" +
        "    int i = blockIdx.x * blockDim.x + threadIdx.x;\n" +
        "    if (i >= num_particles) return;\n" +
        "\n" +
        "    double fx = 0.0, fy = 0.0, fz = 0.0;\n" +
        "    double xi = positions[i * 3 + 0];\n" +
        "    double yi = positions[i * 3 + 1];\n" +
        "    double zi = positions[i * 3 + 2];\n" +
        "\n" +
        "    for (int j = 0; j < num_particles; j++) {\n" +
        "        if (i == j) continue;\n" +
        "        double dx = positions[j * 3 + 0] - xi;\n" +
        "        double dy = positions[j * 3 + 1] - yi;\n" +
        "        double dz = positions[j * 3 + 2] - zi;\n" +
        "        double distSq = dx * dx + dy * dy + dz * dz + softening * softening;\n" +
        "        double invDist = rsqrt(distSq);\n" +
        "        double invDist3 = invDist * invDist * invDist;\n" +
        "        double s = G * masses[j] * invDist3;\n" +
        "        fx += dx * s;\n" +
        "        fy += dy * s;\n" +
        "        fz += dz * s;\n" +
        "    }\n" +
        "    forces[i * 3 + 0] = fx;\n" +
        "    forces[i * 3 + 1] = fy;\n" +
        "    forces[i * 3 + 2] = fz;\n" +
        "}";

    @Override
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public String getNativeLibraryName() {
        return "cuda";
    }

    @Override
    public String getName() {
        return "Native CUDA N-Body";
    }

    @Override
    public String getDescription() {
        return "CUDA-accelerated N-Body simulation (O(N^2))";
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
    }

    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1;
        double base = getPriority();
        if (context.getDataSize() < GPU_THRESHOLD) base -= 100;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 30;
        if (context.hasHint(OperationContext.Hint.BATCH)) base += 20;
        if (context.hasHint(OperationContext.Hint.LOW_LATENCY)) base -= 50;
        return base;
    }

    public synchronized void initialize() {
        if (initialized) return;
        if (!isAvailable()) throw new IllegalStateException("CUDA not available");
        
        try {
            nvrtcProgram program = new nvrtcProgram();
            nvrtcCreateProgram(program, KERNEL_SOURCE, "nbody.cu", 0, null, null);
            nvrtcCompileProgram(program, 0, null);
            
            String[] ptx = new String[1];
            nvrtcGetPTX(program, ptx);
            nvrtcDestroyProgram(program);
            
            module = new CUmodule();
            cuModuleLoadData(module, ptx[0]);
            
            kernel = new CUfunction();
            cuModuleGetFunction(kernel, module, "nbody_forces");
            
            initialized = true;
        } catch (Exception e) {
            logger.warn("CUDA NBody init failed: {}", e.getMessage());
            initialized = false;
        }
    }

    @Override
    public boolean isAvailable() {
        if (cudaAvailable == null) {
            synchronized (NativeCUDANBodyBackend.class) {
                if (cudaAvailable == null) {
                    cudaAvailable = detectCuda();
                }
            }
        }
        return cudaAvailable;
    }

    @Override
    public void shutdown() {
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
        if (!isAvailable()) {
            throw new IllegalStateException("CUDA is not available.");
        }
        if (numParticles < GPU_THRESHOLD) {
            throw new IllegalStateException("Problem size too small.");
        }
        computeForcesCUDA(positions, masses, forces, G, softening);
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
        if (!initialized) initialize();
        if (!initialized) throw new IllegalStateException("CUDA Backend not initialized");
        
        int n = masses.length;
        CUdeviceptr d_positions = new CUdeviceptr();
        CUdeviceptr d_masses = new CUdeviceptr();
        CUdeviceptr d_forces = new CUdeviceptr();
        
        cuMemAlloc(d_positions, (long) n * 3 * 8);
        cuMemAlloc(d_masses, (long) n * 8);
        cuMemAlloc(d_forces, (long) n * 3 * 8);
        
        cuMemcpyHtoD(d_positions, jcuda.Pointer.to(positions), (long) n * 3 * 8);
        cuMemcpyHtoD(d_masses, jcuda.Pointer.to(masses), (long) n * 8);
        
        Pointer kernelParams = Pointer.to(
            Pointer.to(d_positions),
            Pointer.to(d_masses),
            Pointer.to(d_forces),
            Pointer.to(new int[]{n}),
            Pointer.to(new double[]{G}),
            Pointer.to(new double[]{softening})
        );
        
        int blockSizeX = 256;
        int gridSizeX = (n + blockSizeX - 1) / blockSizeX;
        
        cuLaunchKernel(kernel,
            gridSizeX, 1, 1,
            blockSizeX, 1, 1,
            0, null,
            kernelParams, null
        );
        cuCtxSynchronize();
        
        cuMemcpyDtoH(jcuda.Pointer.to(forces), d_forces, (long) n * 3 * 8);
        
        cuMemFree(d_positions);
        cuMemFree(d_masses);
        cuMemFree(d_forces);
    }

    @Override
    public DeviceInfo[] getDevices() {
        if (!isAvailable()) return new DeviceInfo[0];
        int[] count = new int[1];
        JCudaDriver.cuDeviceGetCount(count);
        DeviceInfo[] devices = new DeviceInfo[count[0]];
        for (int i = 0; i < count[0]; i++) {
            byte[] name = new byte[256];
            jcuda.driver.CUdevice device = new jcuda.driver.CUdevice();
            JCudaDriver.cuDeviceGet(device, i);
            JCudaDriver.cuDeviceGetName(name, 256, device);
            devices[i] = new DeviceInfo(new String(name).trim(), 0, 0, "NVIDIA GPU");
        }
        return devices;
    }

    @Override
    public void selectDevice(int deviceId) {
        if (!isAvailable()) throw new IllegalStateException("CUDA not available");
        jcuda.driver.CUdevice device = new jcuda.driver.CUdevice();
        JCudaDriver.cuDeviceGet(device, deviceId);
        jcuda.driver.CUcontext context = new jcuda.driver.CUcontext();
        JCudaDriver.cuCtxCreate(context, 0, device);
    }

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        jcuda.driver.CUdeviceptr devicePtr = new jcuda.driver.CUdeviceptr();
        JCudaDriver.cuMemAlloc(devicePtr, sizeBytes);
        return getNativePointer(devicePtr);
    }

    private static long getNativePointer(jcuda.NativePointerObject obj) {
        try {
            java.lang.reflect.Field field = jcuda.NativePointerObject.class.getDeclaredField("nativePointer");
            field.setAccessible(true);
            return field.getLong(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access JCuda native pointer", e);
        }
    }

    private static jcuda.driver.CUdeviceptr createPointer(long handle) {
        try {
            java.lang.reflect.Constructor<jcuda.driver.CUdeviceptr> ctor = 
                jcuda.driver.CUdeviceptr.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            jcuda.driver.CUdeviceptr ptr = ctor.newInstance();
            
            java.lang.reflect.Field field = jcuda.NativePointerObject.class.getDeclaredField("nativePointer");
            field.setAccessible(true);
            field.setLong(ptr, handle);
            return ptr;
        } catch (Exception e) {
            throw new RuntimeException("Failed to wrap native CUDA handle", e);
        }
    }

    @Override
    public void copyToGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {
        JCudaDriver.cuMemcpyHtoD(createPointer(gpuHandle), jcuda.Pointer.to(hostBuffer), sizeBytes);
    }

    @Override
    public void copyFromGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {
        JCudaDriver.cuMemcpyDtoH(jcuda.Pointer.to(hostBuffer), createPointer(gpuHandle), sizeBytes);
    }

    @Override
    public void freeGPUMemory(long gpuHandle) {
        JCudaDriver.cuMemFree(createPointer(gpuHandle));
    }

    @Override
    public void synchronize() {
        JCudaDriver.cuCtxSynchronize();
    }

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        throw new UnsupportedOperationException("Matrix multiplication not implemented in this N-Body specialized backend.");
    }
}
