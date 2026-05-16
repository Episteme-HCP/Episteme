/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.physics.wave;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.distributed.TaskState;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.waves.WaveProvider;
import org.episteme.natural.physics.classical.waves.providers.MulticoreWaveProvider;
import com.google.auto.service.AutoService;

/**
 * Wave Equation Simulation Task.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
@AutoService(DistributedTask.class)
public class WaveSimTask implements DistributedTask<WaveSimTask, WaveSimTask> {

    private final int width;
    private final int height;
    
    // State: [u(width*height), uPrev(width*height)]
    private TaskState<Real[]> state;
    private double c = 0.5;
    private double damping = 0.99;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;

    public WaveSimTask(int width, int height) {
        this.width = width;
        this.height = height;
        if (width > 0) {
            Real[] initial = new Real[width * height * 2];
            for (int i = 0; i < initial.length; i++) initial[i] = Real.ZERO;
            this.state = new TaskState<>(initial,
                r -> flattenDouble(r), d -> unflattenReal(d),
                r -> flattenFloat(r), f -> unflattenReal(f)
            );
        }
    }

    private double[] flattenDouble(Real[] r) {
        double[] d = new double[r.length];
        for(int i=0; i<r.length; i++) d[i] = r[i].doubleValue();
        return d;
    }

    private float[] flattenFloat(Real[] r) {
        float[] f = new float[r.length];
        for(int i=0; i<r.length; i++) f[i] = r[i].floatValue();
        return f;
    }

    private Real[] unflattenReal(double[] d) {
        Real[] r = new Real[d.length];
        for(int i=0; i<d.length; i++) r[i] = Real.of(d[i]);
        return r;
    }

    private Real[] unflattenReal(float[] f) {
        Real[] r = new Real[f.length];
        for(int i=0; i<f.length; i++) r[i] = Real.of(f[i]);
        return r;
    }

    public WaveSimTask() { this(0, 0); }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        if(state != null) this.state.syncTo(mode);
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
        return null;
    }

    @Override
    public String getTaskType() { return "WAVE_SIM"; }

    public void step() {
        WaveProvider provider = new MulticoreWaveProvider();
        int gridSize = width * height;
        
        switch (mode) {
            case REAL -> {
                Real[] flat = state.getReal();
                Real[][] u = unflatten2D(flat, 0);
                Real[][] uPrev = unflatten2D(flat, gridSize);
                provider.solve(u, uPrev, width, height, Real.of(c), Real.of(damping));
                flatten2D(u, flat, 0);
                flatten2D(uPrev, flat, gridSize);
            }
            case FLOAT -> {
                float[] flat = state.getFloat();
                float[][] u = unflatten2DFloat(flat, 0);
                float[][] uPrev = unflatten2DFloat(flat, gridSize);
                provider.solve(u, uPrev, width, height, (float)c, (float)damping);
                flatten2DFloat(u, flat, 0);
                flatten2DFloat(uPrev, flat, gridSize);
            }
            default -> {
                double[] flat = state.getDouble();
                double[][] u = unflatten2DDouble(flat, 0);
                double[][] uPrev = unflatten2DDouble(flat, gridSize);
                provider.solve(u, uPrev, width, height, c, damping);
                flatten2DDouble(u, flat, 0);
                flatten2DDouble(uPrev, flat, gridSize);
            }
        }
    }

    private Real[][] unflatten2D(Real[] flat, int start) {
        Real[][] res = new Real[width][height];
        for(int i=0; i<width; i++) System.arraycopy(flat, start + i*height, res[i], 0, height);
        return res;
    }

    private void flatten2D(Real[][] src, Real[] flat, int start) {
        for(int i=0; i<width; i++) System.arraycopy(src[i], 0, flat, start + i*height, height);
    }

    private double[][] unflatten2DDouble(double[] flat, int start) {
        double[][] res = new double[width][height];
        for(int i=0; i<width; i++) System.arraycopy(flat, start + i*height, res[i], 0, height);
        return res;
    }

    private void flatten2DDouble(double[][] src, double[] flat, int start) {
        for(int i=0; i<width; i++) System.arraycopy(src[i], 0, flat, start + i*height, height);
    }

    private float[][] unflatten2DFloat(float[] flat, int start) {
        float[][] res = new float[width][height];
        for(int i=0; i<width; i++) System.arraycopy(flat, start + i*height, res[i], 0, height);
        return res;
    }

    private void flatten2DFloat(float[][] src, float[] flat, int start) {
        for(int i=0; i<width; i++) System.arraycopy(src[i], 0, flat, start + i*height, height);
    }

    public double[][] getU() {
        state.syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        return unflatten2DDouble(state.getDouble(), 0);
    }

    public void updateState(double[][] u, double[][] uPrev) {
        state.syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        double[] flat = state.getDouble();
        flatten2DDouble(u, flat, 0);
        flatten2DDouble(uPrev, flat, width * height);
    }

    public void setC(double c) { this.c = c; }
    public void setDamping(double d) { this.damping = d; }
    public double getC() { return c; }
    public double getDamping() { return damping; }
    
    public double[][] getUPrev() {
        state.syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        return unflatten2DDouble(state.getDouble(), width * height);
    }
}
