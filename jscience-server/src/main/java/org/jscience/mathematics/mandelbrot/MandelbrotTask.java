/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.mandelbrot;

import org.jscience.distributed.DistributedTask;
import java.io.Serializable;

public class MandelbrotTask implements DistributedTask<MandelbrotTask, MandelbrotTask>, Serializable {

    protected final int width;
    protected final int height;
    protected double xMin, xMax, yMin, yMax;
    protected int maxIterations = 256;
    protected int[][] result;

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

    @Override
    public Class<MandelbrotTask> getInputType() { return MandelbrotTask.class; }
    @Override
    public Class<MandelbrotTask> getOutputType() { return MandelbrotTask.class; }

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
    public String getTaskType() { return "MANDELBROT"; }

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
        this.xMin = xMin; this.xMax = xMax; this.yMin = yMin; this.yMax = yMax;
    }
    public void setMaxIterations(int max) { this.maxIterations = max; }
    public int[][] getResult() { return result; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getMaxIterations() { return maxIterations; }
}
