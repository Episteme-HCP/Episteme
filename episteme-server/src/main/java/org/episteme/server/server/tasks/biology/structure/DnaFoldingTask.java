/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.biology.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.distributed.TaskState;
import org.episteme.core.mathematics.random.RandomGenerator;
import org.episteme.natural.biology.structure.DnaFoldingProvider;
import org.episteme.natural.biology.structure.providers.StandardDnaFoldingProvider;
import com.google.auto.service.AutoService;
import org.episteme.core.distributed.DistributedTask;

/**
 * DNA Folding Simulation Task.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(DistributedTask.class)
public class DnaFoldingTask
        implements org.episteme.core.distributed.DistributedTask<DnaFoldingTask, DnaFoldingTask> {

    private final int numBases;
    private final int steps;
    private final double temperature;
    private final String sequence;
    
    private TaskState<Real[][]> state;
    private double currentEnergy;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;
    private long seed;

    public DnaFoldingTask(int numBases, int steps, double temperature) {
        this.numBases = numBases;
        this.steps = steps;
        this.temperature = temperature;
        char[] seqArr = new char[numBases];
        Arrays.fill(seqArr, 'A');
        this.sequence = new String(seqArr);
        this.seed = System.nanoTime();
        
        Real[][] initial = new Real[numBases][3];
        RandomGenerator rng = new RandomGenerator(seed);
        for (int i = 0; i < numBases; i++) {
            initial[i][0] = Real.of(i * 0.34);
            initial[i][1] = Real.of(rng.nextReal().doubleValue() * 0.1);
            initial[i][2] = Real.of(rng.nextReal().doubleValue() * 0.1);
        }
        
        this.state = new TaskState<>(initial,
            r -> flattenDouble(r), d -> unflattenDouble(d),
            r -> flattenFloat(r), f -> unflattenFloat(f)
        );
    }

    private double[] flattenDouble(Real[][] r) {
        double[] d = new double[r.length * 3];
        for (int i = 0; i < r.length; i++) {
            d[i*3] = r[i][0].doubleValue();
            d[i*3+1] = r[i][1].doubleValue();
            d[i*3+2] = r[i][2].doubleValue();
        }
        return d;
    }

    private Real[][] unflattenDouble(double[] d) {
        Real[][] r = new Real[d.length / 3][3];
        for (int i = 0; i < r.length; i++) {
            r[i][0] = Real.of(d[i*3]);
            r[i][1] = Real.of(d[i*3+1]);
            r[i][2] = Real.of(d[i*3+2]);
        }
        return r;
    }

    private float[] flattenFloat(Real[][] r) {
        float[] f = new float[r.length * 3];
        for (int i = 0; i < r.length; i++) {
            f[i*3] = r[i][0].floatValue();
            f[i*3+1] = r[i][1].floatValue();
            f[i*3+2] = r[i][2].floatValue();
        }
        return f;
    }

    private Real[][] unflattenFloat(float[] f) {
        Real[][] r = new Real[f.length / 3][3];
        for (int i = 0; i < r.length; i++) {
            r[i][0] = Real.of(f[i*3]);
            r[i][1] = Real.of(f[i*3+1]);
            r[i][2] = Real.of(f[i*3+2]);
        }
        return r;
    }

    public DnaFoldingTask() {
        this(0, 0, 0);
    }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        this.state.syncTo(mode);
    }

    @Override
    public Class<DnaFoldingTask> getInputType() { return DnaFoldingTask.class; }
    @Override
    public Class<DnaFoldingTask> getOutputType() { return DnaFoldingTask.class; }

    @Override
    public DnaFoldingTask execute(DnaFoldingTask input) {
        if (input != null && input.numBases > 0) {
            input.run();
            return input;
        }
        return null;
    }

    @Override
    public String getTaskType() { return "DNA_FOLDING"; }

    public void run() {
        DnaFoldingProvider provider = new StandardDnaFoldingProvider();
        RandomGenerator rng = new RandomGenerator(seed);
        for (int s = 0; s < steps; s++) {
            long currentSeed = rng.nextInteger(0, Integer.MAX_VALUE).longValue();
            switch (mode) {
                case REAL -> {
                    List<Real[]> pointsList = new ArrayList<>(Arrays.asList(state.getReal()));
                    provider.step(pointsList, sequence, temperature, currentSeed);
                    // StandardDnaFoldingProvider.step(List<Real[]>) modifies the list items?
                    // Yes, it does points.set(idx, ...). But our TaskState holds the Real[][] array.
                    // We need to sync back if the list was a COPY of the array elements.
                    // Arrays.asList(state.getReal()) creates a list backed by the array.
                    // So pointsList.set(idx, ...) will modify the Real[][] array!
                }
                case FLOAT -> {
                    float[] flat = state.getFloat();
                    provider.step(flat, sequence, (float)temperature, currentSeed);
                }
                default -> {
                    double[] flat = state.getDouble();
                    provider.step(flat, sequence, temperature, currentSeed);
                }
            }
        }
        calculateEnergy(provider);
        seed = rng.nextInteger(0, Integer.MAX_VALUE).longValue();
    }

    private void calculateEnergy(DnaFoldingProvider provider) {
        switch (mode) {
            case REAL -> currentEnergy = provider.calculateEnergy(Arrays.asList(state.getReal()), sequence).doubleValue();
            case FLOAT -> currentEnergy = provider.calculateEnergy(state.getFloat(), sequence);
            default -> currentEnergy = provider.calculateEnergy(state.getDouble(), sequence);
        }
    }

    public Real[][] getCoordinates() { state.syncTo(TaskRegistry.PrecisionMode.REAL); return state.getReal(); }
    public double getCurrentEnergy() { return currentEnergy; }
}
