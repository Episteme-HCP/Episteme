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

package org.jscience.client.mathematics.mandelbrot;

import java.io.Serializable;

/**
 * Mandelbrot Set Computation Task.
 * Computes iteration counts for a region of the complex plane.
 */
public class MandelbrotTask implements Serializable {

    private final int width;
    private final int height;
    private double xMin, xMax, yMin, yMax;
    private int maxIterations = 256;
    private int[][] result;

    public MandelbrotTask(int width, int height, double xMin, double xMax, double yMin, double yMax) {
        this.width = width;
        this.height = height;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.result = new int[width][height];
    }

    public void compute() {
        double dx = (xMax - xMin) / width;
        double dy = (yMax - yMin) / height;

        for (int px = 0; px < width; px++) {
            for (int py = 0; py < height; py++) {
                double x0 = xMin + px * dx;
                double y0 = yMin + py * dy;
                double x = 0, y = 0;
                int iter = 0;

                while (x * x + y * y <= 4 && iter < maxIterations) {
                    double xTemp = x * x - y * y + x0;
                    y = 2 * x * y + y0;
                    x = xTemp;
                    iter++;
                }
                result[px][py] = iter;
            }
        }
    }

    public void setRegion(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public void setMaxIterations(int max) {
        this.maxIterations = max;
    }

    public int[][] getResult() {
        return result;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxIterations() {
        return maxIterations;
    }
}
