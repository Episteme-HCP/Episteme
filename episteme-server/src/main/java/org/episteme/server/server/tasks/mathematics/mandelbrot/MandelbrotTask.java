/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.mathematics.mandelbrot;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.analysis.fractals.MandelbrotProvider;
import org.episteme.core.mathematics.analysis.fractals.providers.MulticoreMandelbrotProvider;
import com.google.auto.service.AutoService;

/**
 * Mandelbrot Set Generation Task.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(DistributedTask.class)
public class MandelbrotTask implements DistributedTask<MandelbrotTask, MandelbrotTask> {

    private final int width;
    private final int height;
    private double xMin, xMax, yMin, yMax;
    private int maxIterations = 256;
    private int[][] result;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;

    public MandelbrotTask(int width, int height, double xMin, double xMax, double yMin, double yMax) {
        this.width = width;
        this.height = height;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.result = new int[width][height];
    }

    public MandelbrotTask() {
        this(0, 0, 0, 0, 0, 0);
    }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
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
        return null;
    }

    @Override
    public String getTaskType() { return "MANDELBROT"; }

    public void compute() {
        MandelbrotProvider provider = new MulticoreMandelbrotProvider();
        switch (mode) {
            case REAL -> {
                result = provider.compute(Real.of(xMin), Real.of(xMax), Real.of(yMin), Real.of(yMax), width, height, maxIterations);
            }
            case FLOAT -> {
                result = provider.compute((float)xMin, (float)xMax, (float)yMin, (float)yMax, width, height, maxIterations);
            }
            default -> {
                result = provider.compute(xMin, xMax, yMin, yMax, width, height, maxIterations);
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
}
