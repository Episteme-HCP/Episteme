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
import org.jscience.core.media.vision.VisionProvider;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

/**
 * Implementation of VisionProvider using Java Parallel Streams (multicore CPU).
 * <p>
 * This provider handles BufferedImage objects and parallelizes pixel operations
 * across available CPU cores.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class NativeMulticoreVisionProvider implements VisionProvider<BufferedImage> {

    @Override
    public BufferedImage apply(BufferedImage image, ImageOp<BufferedImage> op) {
        // For simple point operations, we can parallelize.
        // However, generic ImageOp might be global.
        // Assuming ImageOp handles its own logic, but if we wanted to enforce parallelism:
        // We would need a PixelOp interface.
        
        // Since ImageOp is generic T -> T, we just delegate.
        // But to make this "Multicore", let's assume we implement specific ops here differently.
        // For now, it simply runs the op.
        return op.process(image);
    }
    
    /**
     * Example of specific multicore optimizations could go here.
     * e.g., parallel convolution.
     */
    public BufferedImage parallelConvolution(BufferedImage source, float[] kernel, int kernelWidth) {
        int width = source.getWidth();
        int height = source.getHeight();
        BufferedImage result = new BufferedImage(width, height, source.getType());
        
        int[] srcPixels = source.getRGB(0, 0, width, height, null, 0, width);
        int[] destPixels = new int[srcPixels.length];
        
        // Parallel Loop
        IntStream.range(0, height).parallel().forEach(y -> {
            for (int x = 0; x < width; x++) {
                float r = 0, g = 0, b = 0, a = 0;
                int kOffset = 0;
                for (int ky = -kernelWidth/2; ky <= kernelWidth/2; ky++) {
                    for (int kx = -kernelWidth/2; kx <= kernelWidth/2; kx++) {
                        int ix = Math.min(Math.max(x + kx, 0), width - 1);
                        int iy = Math.min(Math.max(y + ky, 0), height - 1);
                        int pixel = srcPixels[iy * width + ix];
                        float weight = kernel[kOffset++];
                        
                        a += ((pixel >> 24) & 0xFF) * weight;
                        r += ((pixel >> 16) & 0xFF) * weight;
                        g += ((pixel >> 8) & 0xFF) * weight;
                        b += (pixel & 0xFF) * weight;
                    }
                }
                int ia = Math.min(Math.max((int)a, 0), 255);
                int ir = Math.min(Math.max((int)r, 0), 255);
                int ig = Math.min(Math.max((int)g, 0), 255);
                int ib = Math.min(Math.max((int)b, 0), 255);
                destPixels[y * width + x] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
            }
        });
        
        result.setRGB(0, 0, width, height, destPixels, 0, width);
        return result;
    }


    public BufferedImage createImage(Object data, int width, int height) {
         if (data instanceof int[]) {
             BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
             img.setRGB(0, 0, width, height, (int[]) data, 0, width);
             return img;
         }
         throw new IllegalArgumentException("Unsupported data type for NativeMulticoreVisionProvider");
    }
    @Override
    public String getName() {
        return "Native Multicore Vision Provider";
    }

    @Override
    public int getPriority() {
        return 5; // Standard priority
    }
}
