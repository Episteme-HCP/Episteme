/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.core.technical.backend.gpu.opencl;

import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jocl.CL;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;
import org.jocl.cl_command_queue;
import org.jocl.cl_program;
import org.jocl.cl_kernel;
import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.nio.DoubleBuffer;

import static org.jocl.CL.*;

import com.google.auto.service.AutoService;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;

/**
 * OpenCL implementation of GPUBackend for cross-platform hardware acceleration.
 * <p>
 * Uses JOCL to execute computations on GPUs from various vendors (NVIDIA, AMD, Intel).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, ComputeBackend.class})
public class OpenCLBackend implements GPUBackend {

    private cl_context context;
    private cl_command_queue commandQueue;
    private cl_device_id device;
    private boolean isInitialized = false;

    @Override
    public String getId() {
        return "opencl";
    }

    @Override
    public String getName() {
        return "GPU (OpenCL)";
    }

    @Override
    public String getDescription() {
        return "Cross-platform hardware acceleration via OpenCL for heterogeneous computing.";
    }

    @Override
    public boolean isAvailable() {
        try {
            // Check if JOCL is in classpath
            Class.forName("org.jocl.CL");
            // Check if we can get platforms
            int[] numPlatformsArray = new int[1];
            CL.clGetPlatformIDs(0, null, numPlatformsArray);
            return numPlatformsArray[0] > 0;
        } catch (Throwable t) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public void start() {
        if (isInitialized) return;
        try {
            CL.setExceptionsEnabled(true);
            
            // 1. Get Platform
            int[] numPlatformsArray = new int[1];
            CL.clGetPlatformIDs(0, null, numPlatformsArray);
            int numPlatforms = numPlatformsArray[0];
            
            cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
            CL.clGetPlatformIDs(platforms.length, platforms, null);
            cl_platform_id platform = platforms[0]; // Use first platform
            
            // 2. Get Device
            int[] numDevicesArray = new int[1];
            CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
            int numDevices = numDevicesArray[0];
            
            cl_device_id[] devices = new cl_device_id[numDevices];
            CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_ALL, numDevices, devices, null);
            device = devices[0]; // Use first device
            
            // 3. Create Context
            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);
            context = CL.clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
            
            // 4. Create Command Queue
            commandQueue = CL.clCreateCommandQueue(context, device, 0, null);
            
            isInitialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize OpenCL Backend", e);
        }
    }

    public void stop() {
        if (!isInitialized) return;
        if (commandQueue != null) CL.clReleaseCommandQueue(commandQueue);
        if (context != null) CL.clReleaseContext(context);
        isInitialized = false;
    }

    public cl_context getContext() {
        if (!isInitialized) start();
        return context;
    }

    public cl_command_queue getCommandQueue() {
        if (!isInitialized) start();
        return commandQueue;
    }
    
    public cl_program compileProgram(String source) {
        if (!isInitialized) start();
        
        cl_program program = CL.clCreateProgramWithSource(context, 1, new String[]{ source }, null, null);
        try {
            CL.clBuildProgram(program, 0, null, null, null, null);
        } catch (org.jocl.CLException e) {
             // Get build log
             long[] size = new long[1];
             CL.clGetProgramBuildInfo(program, device, CL.CL_PROGRAM_BUILD_LOG, 0, null, size);
             byte[] log = new byte[(int)size[0]];
             CL.clGetProgramBuildInfo(program, device, CL.CL_PROGRAM_BUILD_LOG, size[0], org.jocl.Pointer.to(log), null);
             throw new RuntimeException("OpenCL Build Error:\n" + new String(log).trim(), e);
        }
        return program;
    }
    
    public cl_kernel createKernel(cl_program program, String kernelName) {
        return CL.clCreateKernel(program, kernelName, null);
    }

    @Override
    public ExecutionContext createContext() {
        if (!isInitialized) {
            start();
        }
        return new OpenCLExecutionContext(context, commandQueue);
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public int getPriority() {
        return 10; 
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.GPU;
    }

    @Override
    public DeviceInfo[] getDevices() {
        if (!isAvailable()) return new DeviceInfo[0];
        
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];
        
        java.util.List<DeviceInfo> deviceInfos = new java.util.ArrayList<>();
        
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        
        for (cl_platform_id platform : platforms) {
            int numDevicesArray[] = new int[1];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
            
            cl_device_id devices[] = new cl_device_id[numDevicesArray[0]];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, devices.length, devices, null);
            
            for (cl_device_id device : devices) {
                String name = getDeviceInfoString(device, CL_DEVICE_NAME);
                String vendor = getDeviceInfoString(device, CL_DEVICE_VENDOR);
                long memSize = getDeviceInfoLong(device, CL_DEVICE_GLOBAL_MEM_SIZE);
                int cu = getDeviceInfoInt(device, CL_DEVICE_MAX_COMPUTE_UNITS);
                
                deviceInfos.add(new DeviceInfo(name, memSize, cu, vendor));
            }
        }
        
        return deviceInfos.toArray(new DeviceInfo[0]);
    }

    @Override
    public void selectDevice(int deviceId) {
        // Recreate context/queue for the selected device
        // For now, store selection and reinitialize on next operation
        // TODO: Full device migration with context recreation
        java.util.logging.Logger.getLogger(getClass().getName())
            .info("OpenCL device selection requested for device " + deviceId + " — using default device");
    }

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, 
                               int m, int n, int k) {
        // CPU fallback: naive matrix multiply until OpenCL kernel is loaded
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int p = 0; p < k; p++) {
                    sum += A.get(i * k + p) * B.get(p * n + j);
                }
                C.put(i * n + j, sum);
            }
        }
    }

    public void elementWise(String operation, DoubleBuffer input, DoubleBuffer output, int size) {
        // CPU fallback for element-wise operations
        for (int i = 0; i < size; i++) {
            double val = input.get(i);
            switch (operation) {
                case "abs": output.put(i, Math.abs(val)); break;
                case "sqrt": output.put(i, Math.sqrt(val)); break;
                case "exp": output.put(i, Math.exp(val)); break;
                case "log": output.put(i, Math.log(val)); break;
                case "sin": output.put(i, Math.sin(val)); break;
                case "cos": output.put(i, Math.cos(val)); break;
                case "negate": output.put(i, -val); break;
                default: throw new IllegalArgumentException("Unknown operation: " + operation);
            }
        }
    }

    public void fft(DoubleBuffer real, DoubleBuffer imag, 
                    DoubleBuffer realOut, DoubleBuffer imagOut, 
                    int n, boolean inverse) {
        // CPU fallback: DFT O(n²)
        double sign = inverse ? 1.0 : -1.0;
        double norm = inverse ? 1.0 / n : 1.0;
        for (int k = 0; k < n; k++) {
            double sumR = 0, sumI = 0;
            for (int t = 0; t < n; t++) {
                double angle = sign * 2.0 * Math.PI * t * k / n;
                double cosA = Math.cos(angle);
                double sinA = Math.sin(angle);
                sumR += real.get(t) * cosA - imag.get(t) * sinA;
                sumI += real.get(t) * sinA + imag.get(t) * cosA;
            }
            realOut.put(k, sumR * norm);
            imagOut.put(k, sumI * norm);
        }
    }

    public double reduce(String operation, DoubleBuffer input, int size) {
        // CPU fallback for reduction
        double result;
        switch (operation) {
            case "sum":
                result = 0;
                for (int i = 0; i < size; i++) result += input.get(i);
                return result;
            case "max":
                result = Double.NEGATIVE_INFINITY;
                for (int i = 0; i < size; i++) result = Math.max(result, input.get(i));
                return result;
            case "min":
                result = Double.POSITIVE_INFINITY;
                for (int i = 0; i < size; i++) result = Math.min(result, input.get(i));
                return result;
            case "product":
                result = 1;
                for (int i = 0; i < size; i++) result *= input.get(i);
                return result;
            default:
                throw new IllegalArgumentException("Unknown reduction: " + operation);
        }
    }

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        // cl_mem mem = clCreateBuffer(context, CL_MEM_READ_WRITE, sizeBytes, null, null);
        return 0; // Placeholder until handle management is implemented
    }

    @Override
    public void copyToGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {
        // Requires cl_mem handle
    }

    @Override
    public void copyFromGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {
        // Requires cl_mem handle
    }

    @Override
    public void freeGPUMemory(long gpuHandle) {
        // Requires cl_mem handle
    }

    @Override
    public void synchronize() {
        clFinish(commandQueue);
    }

    private String getDeviceInfoString(cl_device_id device, int paramName) {
        long size[] = new long[1];
        clGetDeviceInfo(device, paramName, 0, null, size);
        byte buffer[] = new byte[(int) size[0]];
        clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length - 1);
    }

    private long getDeviceInfoLong(cl_device_id device, int paramName) {
        long values[] = new long[1];
        clGetDeviceInfo(device, paramName, Sizeof.cl_long, Pointer.to(values), null);
        return values[0];
    }

    private int getDeviceInfoInt(cl_device_id device, int paramName) {
        int values[] = new int[1];
        clGetDeviceInfo(device, paramName, Sizeof.cl_int, Pointer.to(values), null);
        return values[0];
    }
}

