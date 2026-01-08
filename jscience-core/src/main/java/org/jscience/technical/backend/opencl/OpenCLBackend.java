/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.technical.backend.opencl;

import org.jscience.technical.backend.ComputeBackend;
import org.jscience.technical.backend.ExecutionContext;
import org.jocl.*;

import static org.jocl.CL.*;

/**
 * OpenCL implementation of ComputeBackend for GPU acceleration.
 * <p>
 * Uses JOCL to execute computations on the GPU.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpenCLBackend implements ComputeBackend {

    private static boolean available;
    private static cl_context context;
    private static cl_command_queue commandQueue;

    static {
        try {
            // Initialize OpenCL
            CL.setExceptionsEnabled(false); // Disable to avoid crashes on systems without OpenCL

            // Try to find a platform and device
            int numPlatformsArray[] = new int[1];
            clGetPlatformIDs(0, null, numPlatformsArray);
            int numPlatforms = numPlatformsArray[0];

            if (numPlatforms > 0) {
                cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
                clGetPlatformIDs(platforms.length, platforms, null);
                cl_platform_id platform = platforms[0]; // Use first platform

                cl_context_properties contextProperties = new cl_context_properties();
                contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

                int numDevicesArray[] = new int[1];
                clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, 0, null, numDevicesArray);
                int numDevices = numDevicesArray[0];

                if (numDevices > 0) {
                    cl_device_id devices[] = new cl_device_id[numDevices];
                    clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, numDevices, devices, null);
                    cl_device_id device = devices[0]; // Use first GPU

                    // Get device name for logging
                    long size[] = new long[1];
                    clGetDeviceInfo(device, CL_DEVICE_NAME, 0, null, size);
                    byte buffer[] = new byte[(int) size[0]];
                    clGetDeviceInfo(device, CL_DEVICE_NAME, buffer.length, Pointer.to(buffer), null);
                    String deviceName = new String(buffer, 0, buffer.length - 1);
                    System.out.println("JScience: OpenCL initialized on device: " + deviceName);

                    context = clCreateContext(
                            contextProperties, 1, new cl_device_id[] { device },
                            null, null, null);

                    cl_queue_properties properties = new cl_queue_properties();
                    commandQueue = clCreateCommandQueueWithProperties(
                            context, device, properties, null);

                    available = true;
                }
            } else {
                System.err.println("JScience: No OpenCL platform found. Fallback to CPU.");
            }
        } catch (Throwable t) {
            // OpenCL not available
            available = false;
            System.err.println("JScience: GPU Acceleration unavailable (" + t.getMessage() + "). Fallback to CPU.");
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
        return 10; // Higher priority than CPU
    }
}
