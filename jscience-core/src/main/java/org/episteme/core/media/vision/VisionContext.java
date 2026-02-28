/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.media.vision;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Context for configuring computer vision backends.
 * <p>
 * Selects whether to use Java AWT, Native OpenCV, or OpenCL for image processing.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class VisionContext {

    public enum Backend {
        /** Pure Java AWT/BufferedImage implementation */
        JAVA_AWT,
        /** Native OpenCV implementation */
        OPENCV_NATIVE,
        /** OpenCL GPU acceleration */
        OPENCL_GPU,
        /** Java multithreaded array processing */
        JAVA_MULTICORE,
        /** CUDA GPU acceleration */
        CUDA_GPU
    }

    private static final ThreadLocal<VisionContext> CURRENT = ThreadLocal.withInitial(() -> {
        VisionContext ctx = new VisionContext();
        ctx.registerBackend(Backend.JAVA_AWT, new org.episteme.core.media.vision.backends.JavaAWTVisionBackend());
        return ctx;
    });
    
    private volatile Backend backend = Backend.JAVA_AWT;
    private final Map<Backend, VisionAlgorithmBackend<?>> backends = new ConcurrentHashMap<>();

    /**
     * Returns the current thread-local vision context.
     */
    public static VisionContext current() {
        return CURRENT.get();
    }

    /**
     * Sets the current vision backend.
     */
    public void setBackend(Backend backend) {
        this.backend = backend;
    }
    
    public Backend getBackend() {
        return backend;
    }
    
    /**
     * Registers a backend implementation.
     */
    public void registerBackend(Backend backend, VisionAlgorithmBackend<?> implementation) {
        backends.put(backend, implementation);
    }
    
    /**
     * Gets the implementation for the current backend.
     * @return the backend implementation, cast to the expected type.
     */
    @SuppressWarnings("unchecked")
    public <T> VisionAlgorithmBackend<T> getBackendImplementation() {
        return (VisionAlgorithmBackend<T>) backends.get(backend);
    }
}
