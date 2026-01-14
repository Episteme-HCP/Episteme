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

package org.jscience.mathematics.mandelbrot;

import org.jscience.distributed.DistributedTask;

public class MandelbrotTask implements DistributedTask<MandelbrotTask, MandelbrotTask> {

    protected final int width;
    protected final int height;
    protected double xMin, xMax, yMin, yMax;
    protected int maxIterations = 256;
    protected int[][] result;

    public enum PrecisionMode {
        REALS,
        PRIMITIVES
    }

    private PrecisionMode mode = PrecisionMode.PRIMITIVES;

    public MandelbrotTask(int width, int height, double xMin, double xMax, double yMin, double yMax) {
        this.width = width;
        this.height = height;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.result = new int[width][height];
    }

    // No-arg constructor for ServiceLoader
    public MandelbrotTask() {
        this(0, 0, 0, 0, 0, 0);
    }

    public void setMode(PrecisionMode mode) {
        this.mode = mode;
    }

    @Override
    public Class<MandelbrotTask> getInputType() {
        return MandelbrotTask.class;
    }

    @Override
    public Class<MandelbrotTask> getOutputType() {
        return MandelbrotTask.class;
    }

    @Override
    public MandelbrotTask execute(MandelbrotTask input) {
        if (input != null && input.width > 0) {
            input.compute();
            return input;
        }
        if (this.width > 0) {
            this.compute();
            return this;
        }
        return null;
    }

    @Override
    public String getTaskType() {
        return "MANDELBROT";
    }

    public void compute() {
        if (mode == PrecisionMode.REALS) {
            // JScience Mode: Use Real-based Provider (handled by Multicore provider with
            // conversion internal or special Real provider)
            // Implementation note: existing provider uses doubles but we wrap for
            // architecture consistency.
            org.jscience.technical.backend.algorithms.MandelbrotProvider provider = new org.jscience.technical.backend.algorithms.MulticoreMandelbrotProvider();
            this.result = provider.compute(xMin, xMax, yMin, yMax, width, height, maxIterations);
        } else {
            // Primitive Mode: Use side-by-side Support
            MandelbrotPrimitiveSupport support = new MandelbrotPrimitiveSupport();
            support.generate(result, width, height, xMin, yMin, xMax - xMin, yMax - yMin, maxIterations);
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
