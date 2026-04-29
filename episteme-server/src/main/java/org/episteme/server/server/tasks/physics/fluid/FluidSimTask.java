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

package org.episteme.server.server.tasks.physics.fluid;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.matter.fluids.LatticeBoltzmannProvider;
import org.episteme.natural.physics.classical.matter.fluids.providers.FluidSimPrimitiveSupport;

public class FluidSimTask implements DistributedTask<FluidSimTask, FluidSimTask> {

    private final int width;
    private final int height;
    private Real[][][] f; // Distribution functions (width x height x 9)
    private double[][][] fPrimitive; // Primitive cache for performance
    private Real[][] rho; // Density
    private Real[][] ux; // X velocity
    private Real[][] uy; // Y velocity
    private boolean[][] obstacle;
    private Real viscosity = Real.of(0.02);
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.REAL;
    private float[][][] fFloat;
    private transient LatticeBoltzmannProvider provider; // Transient, re-initialized on execution

    private static final double[] W = { 4.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 36, 1.0 / 36, 1.0 / 36,
            1.0 / 36 };
    private static final int[] CX = { 0, 1, 0, -1, 0, 1, -1, -1, 1 };
    private static final int[] CY = { 0, 0, 1, 0, -1, 1, 1, -1, -1 };

    public FluidSimTask(int width, int height) {
        this.width = width;
        this.height = height;
        this.f = new Real[width][height][9];
        this.rho = new Real[width][height];
        this.ux = new Real[width][height];
        this.uy = new Real[width][height];
        this.obstacle = new boolean[width][height];
        initialize();
    }

    // No-arg constructor for serialization
    public FluidSimTask() {
        this(0, 0);
    }

    private void initialize() {
        if (width == 0)
            return;

        Real one = Real.ONE;
        Real zero = Real.ZERO;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                rho[x][y] = one;
                ux[x][y] = zero;
                uy[x][y] = zero;
                for (int i = 0; i < 9; i++) {
                    f[x][y][i] = Real.of(W[i]);
                }
            }
        }
    }

    @Override
    public Class<FluidSimTask> getInputType() {
        return FluidSimTask.class;
    }

    @Override
    public Class<FluidSimTask> getOutputType() {
        return FluidSimTask.class;
    }

    @Override
    public FluidSimTask execute(FluidSimTask input) {
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
        return "FLUID_LBM";
    }

    public void step() {
        if (this.provider == null) {
            this.provider = new org.episteme.natural.physics.classical.matter.fluids.providers.MulticoreLatticeBoltzmannProvider();
        }
        switch (mode) {
            case REAL -> stepReal();
            case FLOAT -> stepFloat();
            default -> stepPrimitive();
        }
    }

    private void stepFloat() {
        if (fFloat == null) {
            initializeFloat();
        }
        float omega = 1.0f / (3.0f * viscosity.floatValue() + 0.5f);
        this.provider.evolve(fFloat, obstacle, omega);

        // Update Macroscopic
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y])
                    continue;
                float r = 0, vx = 0, vy = 0;
                for (int i = 0; i < 9; i++) {
                    r += fFloat[x][y][i];
                    vx += CX[i] * fFloat[x][y][i];
                    vy += CY[i] * fFloat[x][y][i];
                }
                rho[x][y] = Real.of(r);
                if (r > 0) {
                    ux[x][y] = Real.of(vx / r);
                    uy[x][y] = Real.of(vy / r);
                }
            }
        }
    }

    private void stepPrimitive() {
        if (fPrimitive == null) {
            initializePrimitive();
        }
        double omega = 1.0 / (3.0 * viscosity.doubleValue() + 0.5);
        this.provider.evolve(fPrimitive, obstacle, omega);

        // Update Macroscopic
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y])
                    continue;
                double r = 0, vx = 0, vy = 0;
                for (int i = 0; i < 9; i++) {
                    r += fPrimitive[x][y][i];
                    vx += CX[i] * fPrimitive[x][y][i];
                    vy += CY[i] * fPrimitive[x][y][i];
                }
                rho[x][y] = Real.of(r);
                if (r > 0) {
                    ux[x][y] = Real.of(vx / r);
                    uy[x][y] = Real.of(vy / r);
                }
            }
        }
    }

    private void stepReal() {
        Real omega = Real.ONE.divide(viscosity.multiply(Real.of(3.0)).add(Real.of(0.5)));

        if (fPrimitive != null && f[0][0][0] == null) {
            syncToReal();
        }

        this.provider.evolve(f, obstacle, omega);

        // Update Macroscopic
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (obstacle[x][y])
                    continue;
                Real r = Real.ZERO;
                Real vx = Real.ZERO;
                Real vy = Real.ZERO;
                for (int i = 0; i < 9; i++) {
                    r = r.add(f[x][y][i]);
                    vx = vx.add(f[x][y][i].multiply(Real.of(CX[i])));
                    vy = vy.add(f[x][y][i].multiply(Real.of(CY[i])));
                }
                rho[x][y] = r;
                if (r.compareTo(Real.ZERO) > 0) {
                    ux[x][y] = vx.divide(r);
                    uy[x][y] = vy.divide(r);
                }
            }
        }
    }

    private void initializePrimitive() {
        if (fPrimitive == null) {
            fPrimitive = new double[width][height][9];
        }
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                for (int i = 0; i < 9; i++)
                    fPrimitive[x][y][i] = (f != null && f[x][y][i] != null) ? f[x][y][i].doubleValue() : W[i];
    }

    private void initializeFloat() {
        if (fFloat == null) {
            fFloat = new float[width][height][9];
        }
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                for (int i = 0; i < 9; i++)
                    fFloat[x][y][i] = (f != null && f[x][y][i] != null) ? f[x][y][i].floatValue() : (float) W[i];
    }

    private void syncToReal() {
        if (fPrimitive == null)
            return;
        if (f == null)
            f = new Real[width][height][9];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                for (int i = 0; i < 9; i++)
                    f[x][y][i] = Real.of(fPrimitive[x][y][i]);
    }

    public void setObstacle(int x, int y, boolean isObstacle) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            this.obstacle[x][y] = isObstacle;
    }

    public Real[][] getRho() {
        return rho;
    }

    public Real[][] getUx() {
        return ux;
    }

    public Real[][] getUy() {
        return uy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
