/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.backend.gpu.opencl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Utility to load OpenCL kernel source code from resources.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class OpenCLKernelLoader {

    /**
     * Loads the content of an OpenCL source file from the classpath.
     * 
     * @param path the path to the .cl file (e.g., "/kernels/opencl/vision_kernels.cl")
     * @return the source code as a String
     * @throws RuntimeException if the file cannot be found or read
     */
    public static String loadKernelSource(String path) {
        try (InputStream in = OpenCLKernelLoader.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("Kernel file not found: " + path);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load kernel source: " + path, e);
        }
    }
}
