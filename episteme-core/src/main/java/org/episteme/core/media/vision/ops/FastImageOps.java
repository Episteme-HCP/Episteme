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
package org.episteme.core.media.vision.ops;

import java.util.stream.IntStream;

/**
 * High-performance Image Operations.
 * <p>
 * Uses Parallel Streams to accelerate pixel-wise operations, mimicking GPU SIMD behavior
 * where actual GPU context is unavailable or overhead is too high.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class FastImageOps {

    /**
     * Converts an RGB image array to Grayscale in parallel.
     * @param rgbPixels input pixels (int array ARGB).
     * @param width image width.
     * @param height image height.
     * @return grayscale byte array.
     */
    public static byte[] rgbToGrayscale(int[] rgbPixels, int width, int height) {
        byte[] gray = new byte[width * height];
        
        IntStream.range(0, rgbPixels.length).parallel().forEach(i -> {
            int p = rgbPixels[i];
            int r = (p >> 16) & 0xFF;
            int g = (p >> 8) & 0xFF;
            int b = p & 0xFF;
            // Luminosity method: 0.21 R + 0.72 G + 0.07 B
            gray[i] = (byte)((r * 0.21 + g * 0.72 + b * 0.07));
        });
        
        return gray;
    }

    /**
     * Resizes an image using Nearest Neighbor interpolation in parallel.
     * @param pixels source pixels.
     * @param w1 source width.
     * @param h1 source height.
     * @param w2 target width.
     * @param h2 target height.
     * @return resized pixels.
     */
    public static int[] resizeNearest(int[] pixels, int w1, int h1, int w2, int h2) {
        int[] result = new int[w2 * h2];
        double xRatio = (double) w1 / w2;
        double yRatio = (double) h1 / h2;
        
        IntStream.range(0, h2).parallel().forEach(y -> {
            int py = (int) (y * yRatio);
            for (int x = 0; x < w2; x++) {
                int px = (int) (x * xRatio);
                result[y * w2 + x] = pixels[py * w1 + px];
            }
        });
        
        return result;
    }
}
