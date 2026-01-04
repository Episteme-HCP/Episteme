/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.mandelbrot;

import java.util.stream.IntStream;

/**
 * Primitive-based support for Mandelbrot Set generation.
 * Optimized for high-performance double-precision computation.
 */
public class MandelbrotPrimitiveSupport {

    /**
     * Generates the Mandelbrot set into an integer array using double primitives.
     * 
     * @param result  Result array [width][height]
     * @param width   Width
     * @param height  Height
     * @param x0      Mindividual x-coord
     * @param y0      Mindividual y-coord
     * @param dx      X range
     * @param dy      Y range
     * @param maxIter Maximum iterations
     */
    public void generate(int[][] result, int width, int height, double x0, double y0, double dx, double dy,
            int maxIter) {
        IntStream.range(0, width).parallel().forEach(i -> {
            for (int j = 0; j < height; j++) {
                double zx = 0;
                double zy = 0;
                double cx = x0 + i * dx / width;
                double cy = y0 + j * dy / height;
                int iter = 0;
                while (zx * zx + zy * zy < 4 && iter < maxIter) {
                    double tmp = zx * zx - zy * zy + cx;
                    zy = 2 * zx * zy + cy;
                    zx = tmp;
                    iter++;
                }
                result[i][j] = iter;
            }
        });
    }
}
