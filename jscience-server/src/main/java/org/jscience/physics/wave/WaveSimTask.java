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

package org.jscience.physics.wave;

import org.jscience.distributed.DistributedTask;



/**
 * Wave Equation Simulation Task.
 */
public class WaveSimTask implements DistributedTask<WaveSimTask, WaveSimTask> {

    private final int width;
    private final int height;
    private double[][] u; // Current (Primitive mode)
    private double[][] uPrev; // Previous (Primitive mode)

    private double c = 0.5;
    private double damping = 0.99;

    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.PRIMITIVES;
    private org.jscience.mathematics.numbers.real.Real[][] uReal;
    private org.jscience.mathematics.numbers.real.Real[][] uRealPrev;

    public WaveSimTask(int width, int height) {
        this.width = width;
        this.height = height;
        this.u = new double[width][height];
        this.uPrev = new double[width][height];
    }

    // No-arg constructor for ServiceLoader
    public WaveSimTask() {
        this(0, 0);
    }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        if (mode == TaskRegistry.PrecisionMode.REALS && uReal == null) {
            syncToReal();
        }
    }

    private void syncToReal() {
        uReal = new org.jscience.mathematics.numbers.real.Real[width][height];
        uRealPrev = new org.jscience.mathematics.numbers.real.Real[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                uReal[x][y] = org.jscience.mathematics.numbers.real.Real.of(u[x][y]);
                uRealPrev[x][y] = org.jscience.mathematics.numbers.real.Real.of(uPrev[x][y]);
            }
        }
    }

    private void syncFromReal() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                u[x][y] = uReal[x][y].doubleValue();
                uPrev[x][y] = uRealPrev[x][y].doubleValue();
            }
        }
    }

    @Override
    public Class<WaveSimTask> getInputType() {
        return WaveSimTask.class;
    }

    @Override
    public Class<WaveSimTask> getOutputType() {
        return WaveSimTask.class;
    }

    @Override
    public WaveSimTask execute(WaveSimTask input) {
        if (input != null && input.width > 0) {
            input.step();
            return input;
        }
        if (this.width > 0) {
            this.step();
            return this;
        }
        return null;
    }

    @Override
    public String getTaskType() {
        return "WAVE_SIM";
    }

    public void step() {
        if (mode == TaskRegistry.PrecisionMode.REALS) {
            // JScience Mode: Use Real-based Provider
            org.jscience.technical.backend.algorithms.WaveProvider provider = new org.jscience.technical.backend.algorithms.MulticoreWaveProvider();
            provider.solve(uReal, uRealPrev, width, height,
                    org.jscience.mathematics.numbers.real.Real.of(c),
                    org.jscience.mathematics.numbers.real.Real.of(damping));
            syncFromReal();
        } else {
            // Primitive Mode: Use side-by-side Support
            WaveSimPrimitiveSupport support = new WaveSimPrimitiveSupport();
            support.solve(u, uPrev, width, height, c, damping);
        }
    }

    public double[][] getU() {
        return u;
    }

    public double[][] getUPrev() {
        return uPrev;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getC() {
        return c;
    }

    public double getDamping() {
        return damping;
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    public void updateState(double[][] u, double[][] uPrev) {
        this.u = u;
        this.uPrev = uPrev;
    }
}
