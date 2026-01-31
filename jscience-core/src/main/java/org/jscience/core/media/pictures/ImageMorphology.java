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

package org.jscience.core.media.pictures;

/**
 * Mathematical morphology for binary and grayscale images.
 */
public final class ImageMorphology {

    private ImageMorphology() {}

    public enum Shape { SQUARE, CROSS, CIRCLE }

    /**
     * Dilation operation (Max filter).
     */
    public static float[][] dilate(float[][] input, int radius, Shape shape) {
        int width = input.length;
        int height = input[0].length;
        float[][] output = new float[width][height];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float max = 0;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        if (isValid(x + dx, y + dy, width, height, dx, dy, radius, shape)) {
                            max = Math.max(max, input[x + dx][y + dy]);
                        }
                    }
                }
                output[x][y] = max;
            }
        }
        return output;
    }

    /**
     * Erosion operation (Min filter).
     */
    public static float[][] erode(float[][] input, int radius, Shape shape) {
        int width = input.length;
        int height = input[0].length;
        float[][] output = new float[width][height];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float min = 1.0f;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        if (isValid(x + dx, y + dy, width, height, dx, dy, radius, shape)) {
                            min = Math.min(min, input[x + dx][y + dy]);
                        }
                    }
                }
                output[x][y] = min;
            }
        }
        return output;
    }

    /**
     * Opening operation (Erosion then Dilation).
     * Removes small bright spots.
     */
    public static float[][] open(float[][] input, int radius, Shape shape) {
        return dilate(erode(input, radius, shape), radius, shape);
    }

    /**
     * Closing operation (Dilation then Erosion).
     * Fills small dark holes.
     */
    public static float[][] close(float[][] input, int radius, Shape shape) {
        return erode(dilate(input, radius, shape), radius, shape);
    }

    private static boolean isValid(int x, int y, int w, int h, int dx, int dy, int r, Shape s) {
        if (x < 0 || x >= w || y < 0 || y >= h) return false;
        
        return switch (s) {
            case SQUARE -> true;
            case CROSS -> dx == 0 || dy == 0;
            case CIRCLE -> (dx * dx + dy * dy) <= (r * r);
        };
    }
}

