/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.gpu.opencl;

import org.jocl.*;
import static org.jocl.CL.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared OpenCL infrastructure and lifecycle management.
 * Consolidates context, queue, and device management for all OpenCL backends.
 */
public final class OpenCLManager {
    private static final Logger logger = LoggerFactory.getLogger(OpenCLManager.class);

    private static cl_context context;
    private static cl_command_queue commandQueue;
    private static cl_device_id device;
    private static boolean supportsDouble = false;
    private static boolean initialized = false;

    private OpenCLManager() {}

    public static synchronized void ensureInitialized() {
        if (initialized) return;

        try {
            CL.setExceptionsEnabled(true);
            
            int[] numPlatforms = new int[1];
            clGetPlatformIDs(0, null, numPlatforms);
            if (numPlatforms[0] == 0) throw new RuntimeException("No OpenCL platforms found");
            
            cl_platform_id[] platforms = new cl_platform_id[numPlatforms[0]];
            clGetPlatformIDs(platforms.length, platforms, null);
            cl_platform_id platform = platforms[0];

            int[] numDevices = new int[1];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 0, null, numDevices);
            cl_device_id[] devices = new cl_device_id[numDevices[0]];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, devices.length, devices, null);
            device = devices[0];

            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
            context = clCreateContext(contextProperties, 1, new cl_device_id[]{device}, null, null, null);
            
            cl_queue_properties queueProperties = new cl_queue_properties();
            commandQueue = clCreateCommandQueueWithProperties(context, device, queueProperties, null);

            // Check for double precision support
            String extensions = getDeviceString(device, CL_DEVICE_EXTENSIONS);
            supportsDouble = extensions.contains("cl_khr_fp64") || extensions.contains("cl_amd_fp64");

            initialized = true;
            logger.info("OpenCL Manager initialized successfully. Device: {}, Double Support: {}", 
                getDeviceString(device, CL_DEVICE_NAME), supportsDouble);
        } catch (Throwable t) {
            logger.error("Failed to initialize OpenCL Manager: {}", t.getMessage());
            throw new RuntimeException("OpenCL initialization failed", t);
        }
    }

    public static cl_context getContext() { return context; }
    public static cl_command_queue getCommandQueue() { return commandQueue; }
    public static cl_device_id getDevice() { return device; }
    public static boolean isSupportsDouble() { return supportsDouble; }
    public static boolean isInitialized() { return initialized; }

    private static String getDeviceString(cl_device_id device, int paramName) {
        long[] size = new long[1];
        clGetDeviceInfo(device, paramName, 0, null, size);
        byte[] buffer = new byte[(int) size[0]];
        clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length - 1);
    }
}
