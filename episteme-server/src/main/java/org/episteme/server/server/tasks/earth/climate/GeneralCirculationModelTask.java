/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.earth.climate;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.distributed.TaskState;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.matter.fluids.NavierStokesProvider;
import org.episteme.natural.physics.classical.matter.fluids.providers.MulticoreNavierStokesProvider;
import com.google.auto.service.AutoService;

/**
 * Advanced General Circulation Model (GCM) Task.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(DistributedTask.class)
public class GeneralCirculationModelTask
        implements DistributedTask<GeneralCirculationModelTask, GeneralCirculationModelTask> {

    private final int latBins;
    private final int longBins;
    private static final int LAYERS = 3;

    // State: [temperature(3), u(3), v(3), w(3), humidity(1)] = 13 fields
    private TaskState<Real[]> state;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;

    public GeneralCirculationModelTask(int latBins, int longBins) {
        this.latBins = latBins;
        this.longBins = longBins;
        
        if (latBins > 0) {
            Real[] initial = new Real[LAYERS * 4 * latBins * longBins + latBins * longBins];
            initialize(initial);
            this.state = new TaskState<>(initial,
                r -> flattenDouble(r), d -> unflattenReal(d),
                r -> flattenFloat(r), f -> unflattenReal(f)
            );
        }
    }

    private void initialize(Real[] flat) {
        int gridSize = latBins * longBins;
        for (int i = 0; i < latBins; i++) {
            double lat = Math.PI * (i - latBins / 2.0) / latBins;
            for (int j = 0; j < longBins; j++) {
                int idx = i * longBins + j;
                flat[idx] = Real.of(288.0 - 40 * Math.sin(lat) * Math.sin(lat)); // T Surface
                flat[gridSize + idx] = Real.of(flat[idx].doubleValue() - 30); // T Troposphere
                flat[2 * gridSize + idx] = Real.of(210.0); // T Stratosphere
                
                flat[3 * gridSize + idx] = Real.of(10.0 * Math.random()); // u Surface
                flat[4 * gridSize + idx] = Real.of(20.0 + 5.0 * Math.random()); // u Troposphere
                flat[5 * gridSize + idx] = Real.ZERO; // u Stratosphere
                
                // v, w, humidity... initialize with zeros or small values
                for(int k=6; k<13; k++) flat[k * gridSize + idx] = Real.ZERO;
                flat[12 * gridSize + idx] = Real.of(0.01 * Math.exp(-Math.abs(lat))); // Humidity
            }
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

    public GeneralCirculationModelTask() { this(0, 0); }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        if(state != null) this.state.syncTo(mode);
    }

    @Override
    public Class<GeneralCirculationModelTask> getInputType() { return GeneralCirculationModelTask.class; }
    @Override
    public Class<GeneralCirculationModelTask> getOutputType() { return GeneralCirculationModelTask.class; }

    @Override
    public GeneralCirculationModelTask execute(GeneralCirculationModelTask input) {
        if (input != null && input.latBins > 0) {
            input.step(3600);
            return input;
        }
        return null;
    }

    @Override
    public String getTaskType() { return "GCM_CLIMATE"; }

    public void step(double dt) {
        NavierStokesProvider provider = new MulticoreNavierStokesProvider();
        int gridSize = latBins * longBins;
        
        switch (mode) {
            case REAL -> {
                // Simulation logic here... simplified for now as it's a huge step
                // Call provider.solve for each layer
            }
            case FLOAT -> {
                float[] flat = state.getFloat();
                for(int k=0; k<LAYERS; k++) {
                    float[] density = slice(flat, k * gridSize, gridSize);
                    float[] u = slice(flat, (3+k) * gridSize, gridSize);
                    float[] v = slice(flat, (6+k) * gridSize, gridSize);
                    float[] w = slice(flat, (9+k) * gridSize, gridSize);
                    provider.solve(density, u, v, w, (float)dt, 0.0001f, longBins, latBins, 1);
                    unslice(flat, density, k * gridSize);
                    unslice(flat, u, (3+k) * gridSize);
                    unslice(flat, v, (6+k) * gridSize);
                    unslice(flat, w, (9+k) * gridSize);
                }
            }
            default -> {
                double[] flat = state.getDouble();
                for(int k=0; k<LAYERS; k++) {
                    double[] density = slice(flat, k * gridSize, gridSize);
                    double[] u = slice(flat, (3+k) * gridSize, gridSize);
                    double[] v = slice(flat, (6+k) * gridSize, gridSize);
                    double[] w = slice(flat, (9+k) * gridSize, gridSize);
                    provider.solve(density, u, v, w, dt, 0.0001, longBins, latBins, 1);
                    unslice(flat, density, k * gridSize);
                    unslice(flat, u, (3+k) * gridSize);
                    unslice(flat, v, (6+k) * gridSize);
                    unslice(flat, w, (9+k) * gridSize);
                }
            }
        }
    }

    private float[] slice(float[] flat, int start, int len) {
        float[] s = new float[len];
        System.arraycopy(flat, start, s, 0, len);
        return s;
    }

    private void unslice(float[] flat, float[] s, int start) {
        System.arraycopy(s, 0, flat, start, s.length);
    }

    private double[] slice(double[] flat, int start, int len) {
        double[] s = new double[len];
        System.arraycopy(flat, start, s, 0, len);
        return s;
    }

    private void unslice(double[] flat, double[] s, int start) {
        System.arraycopy(s, 0, flat, start, s.length);
    }

    public double[][] getSurfaceTemperature() {
        state.syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        double[] flat = state.getDouble();
        double[][] grid = new double[latBins][longBins];
        for(int i=0; i<latBins; i++) System.arraycopy(flat, i*longBins, grid[i], 0, longBins);
        return grid;
    }

    public double[][] getAirTemperature() {
        state.syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        double[] flat = state.getDouble();
        int gridSize = latBins * longBins;
        double[][] grid = new double[latBins][longBins];
        for(int i=0; i<latBins; i++) System.arraycopy(flat, gridSize + i*longBins, grid[i], 0, longBins);
        return grid;
    }

    public double[][] getHumidity() {
        state.syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        double[] flat = state.getDouble();
        int gridSize = latBins * longBins;
        double[][] grid = new double[latBins][longBins];
        for(int i=0; i<latBins; i++) System.arraycopy(flat, 12 * gridSize + i*longBins, grid[i], 0, longBins);
        return grid;
    }

    public void updateState(double[][][] temps, double[][] humidity) {
        state.syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        double[] flat = state.getDouble();
        int gridSize = latBins * longBins;
        if (temps != null) {
            for(int k=0; k<Math.min(temps.length, 3); k++) {
                if (temps[k] != null) {
                    for(int i=0; i<latBins; i++) System.arraycopy(temps[k][i], 0, flat, k*gridSize + i*longBins, longBins);
                }
            }
        }
        if (humidity != null) {
            for(int i=0; i<latBins; i++) System.arraycopy(humidity[i], 0, flat, 12*gridSize + i*longBins, longBins);
        }
    }
}
