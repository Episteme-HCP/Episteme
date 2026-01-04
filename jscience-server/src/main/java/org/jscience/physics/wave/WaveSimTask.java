/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.physics.wave;

import org.jscience.distributed.DistributedTask;
import java.io.Serializable;

public class WaveSimTask implements DistributedTask<WaveSimTask, WaveSimTask>, Serializable {

    private final int width;
    private final int height;
    private double[][] u; // Current
    private double[][] uPrev; // Previous

    private double c = 0.5;
    private double damping = 0.99;

    public WaveSimTask(int width, int height) {
        this.width = width;
        this.height = height;
        this.u = new double[width][height];
        this.uPrev = new double[width][height];
    }
    
    public WaveSimTask() {
        this(0, 0);
    }

    @Override
    public Class<WaveSimTask> getInputType() { return WaveSimTask.class; }
    @Override
    public Class<WaveSimTask> getOutputType() { return WaveSimTask.class; }

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
    public String getTaskType() { return "WAVE_SIM"; }

    public void step() {
        double[][] uNext = new double[width][height];
        double c2 = c * c;
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double laplacian = u[x + 1][y] + u[x - 1][y] + u[x][y + 1] + u[x][y - 1] - 4 * u[x][y];
                uNext[x][y] = (2 * u[x][y] - uPrev[x][y] + c2 * laplacian) * damping;
            }
        }
        uPrev = u;
        u = uNext;
    }

    public double[][] getU() { return u; }
    public double[][] getUPrev() { return uPrev; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getC() { return c; }
    public double getDamping() { return damping; }
    public void setC(double c) { this.c = c; }
    public void setDamping(double damping) { this.damping = damping; }
    public void updateState(double[][] u, double[][] uPrev) {
        this.u = u;
        this.uPrev = uPrev;
    }
}
