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
import org.jocl.*;
import java.nio.DoubleBuffer;

import static org.jocl.CL.*;

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
public class OpenCLBackend implements GPUBackend {

    private static boolean available;
    private static cl_context context;
    private static cl_command_queue commandQueue;
    private static cl_device_id selectedDevice;

    static {
        try {
            // Initialize OpenCL
            CL.setExceptionsEnabled(false); 

            int numPlatformsArray[] = new int[1];
            clGetPlatformIDs(0, null, numPlatformsArray);
            int numPlatforms = numPlatformsArray[0];

            if (numPlatforms > 0) {
                cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
                clGetPlatformIDs(platforms.length, platforms, null);
                cl_platform_id platform = platforms[0]; 

                cl_context_properties contextProperties = new cl_context_properties();
                contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

                int numDevicesArray[] = new int[1];
                clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, 0, null, numDevicesArray);
                int numDevices = numDevicesArray[0];

                if (numDevices > 0) {
                    cl_device_id devices[] = new cl_device_id[numDevices];
                    clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, numDevices, devices, null);
                    selectedDevice = devices[0]; 

                    context = clCreateContext(
                            contextProperties, 1, new cl_device_id[] { selectedDevice },
                            null, null, null);

                    cl_queue_properties properties = new cl_queue_properties();
                    commandQueue = clCreateCommandQueueWithProperties(
                            context, selectedDevice, properties, null);

                    available = true;
                }
            }
        } catch (Throwable t) {
            available = false;
        }
    }

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
        return available;
    }

    @Override
    public ExecutionContext createContext() {
        if (!available) {
            throw new IllegalStateException("GPU backend is not available");
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
    public DeviceInfo[] getDevices() {
        if (!available) return new DeviceInfo[0];
        
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
        // In a full implementation, we'd recreate context/queue for the selected device
        throw new UnsupportedOperationException("Device selection not yet implemented for OpenCL.");
    }

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, 
                               int m, int n, int k) {
        // Implementation requires building and running an OpenCL kernel.
        // This is more complex than CUDA as it requires runtime compilation.
        throw new UnsupportedOperationException("OpenCL matrix multiplication kernel needs to be implemented.");
    }

    @Override
    public void elementWise(String operation, DoubleBuffer input, DoubleBuffer output, int size) {
        throw new UnsupportedOperationException("OpenCL element-wise operations require kernel loading.");
    }

    @Override
    public void fft(DoubleBuffer real, DoubleBuffer imag, 
                   DoubleBuffer realOut, DoubleBuffer imagOut, 
                   int n, boolean inverse) {
        // Requires clFFT library or custom kernels
        throw new UnsupportedOperationException("FFT requires clFFT or custom kernels.");
    }

    @Override
    public double reduce(String operation, DoubleBuffer input, int size) {
        throw new UnsupportedOperationException("Reduction not implemented for OpenCL.");
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

