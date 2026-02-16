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

package org.jscience.nativ.media.vision.providers;

import org.jscience.core.media.vision.ImageOp;
import org.jscience.core.media.vision.VisionAlgorithmBackend;
import org.jscience.core.technical.backend.Backend;
import com.google.auto.service.AutoService;

/**
 * CUDA-accelerated vision provider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService(Backend.class)
public class NativeCUDAVisionProvider implements VisionAlgorithmBackend<Object> {

    @Override public String getType() { return "vision"; }
    @Override public String getId() { return "native-cuda-vision"; }
    @Override public String getDescription() { return "GPU-accelerated image processing using NVIDIA CUDA."; }
    @Override public boolean isAvailable() {
        try {
            // Basic JCuda check
            Class.forName("jcuda.driver.JCudaDriver");
            return true; 
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public Object createBackend() {
        return this;
    }

    static {
        // Initialize JCuda
    }

    @Override
    public Object apply(Object image, ImageOp<Object> op) {
        if (!(image instanceof jcuda.driver.CUdeviceptr)) {
            throw new IllegalArgumentException("Expected CUdeviceptr for CUDAVisionProvider");
        }
        return op.process(image);
    }

    @Override
    public Object createImage(Object data, int width, int height) {
        if (data instanceof int[]) {
            int[] pixels = (int[]) data;
            jcuda.driver.CUdeviceptr deviceData = new jcuda.driver.CUdeviceptr();
            jcuda.driver.JCudaDriver.cuMemAlloc(deviceData, (long) pixels.length * Sizeof.INT);
            jcuda.driver.JCudaDriver.cuMemcpyHtoD(deviceData, jcuda.Pointer.to(pixels), (long) pixels.length * Sizeof.INT);
            return deviceData;
        }
        throw new UnsupportedOperationException("CUDA data upload only supported for int arrays for now.");
    }
    
    private static class Sizeof {
        static final int INT = 4;
    }

    @Override
    public String getName() {
        return "CUDA Vision Provider";
    }

    @Override
    public int getPriority() {
        return 20; // Higher priority than CPU
    }
}

