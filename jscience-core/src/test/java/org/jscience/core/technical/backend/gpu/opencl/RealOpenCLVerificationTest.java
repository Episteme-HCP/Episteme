/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.gpu.opencl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification test for OpenCL Backend and Vision Provider.
 * Requires OpenCL hardware/drivers.
 */
public class RealOpenCLVerificationTest {

    @SuppressWarnings("unused")
    private static boolean isOpenCLAvailable() {
        return new OpenCLBackend().isAvailable();
    }

    @Test
    @EnabledIf("isOpenCLAvailable")
    public void testBackendInitialization() {
        OpenCLBackend backend = new OpenCLBackend();
        assertTrue(backend.isAvailable(), "OpenCL should be available");
        backend.start();
        assertNotNull(backend.getContext(), "Context should be created");
        assertNotNull(backend.getCommandQueue(), "CommandQueue should be created");
        backend.stop();
    }

    @Test
    @EnabledIf("isOpenCLAvailable")
    public void testKernelCompilation() {
        OpenCLBackend backend = new OpenCLBackend();
        backend.start();
        
        String source = "__kernel void test_k(__global int* a) { a[get_global_id(0)] = 1; }";
        assertDoesNotThrow(() -> backend.compileProgram(source));
        
        backend.stop();
    }
    
    // Note: We cannot easily test VisionProvider without a real image op interface implementation
    // But we can test the backend logic it relies on.
}
