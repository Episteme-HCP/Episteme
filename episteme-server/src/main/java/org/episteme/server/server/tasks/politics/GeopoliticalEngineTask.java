/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.politics;

import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.distributed.TaskState;
import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.random.RandomGenerator;
import org.episteme.natural.politics.GeopoliticalParameters;
import java.io.Serializable;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.List;

/**
 * Geopolitical Engine Distributed Task.
 */
@SuppressWarnings("rawtypes")
@AutoService(DistributedTask.class)
public class GeopoliticalEngineTask implements DistributedTask<GeopoliticalEngineTask, GeopoliticalEngineTask> {

    public static class NationState implements Serializable {
        public String name;
        public List<String> allies = new ArrayList<>();
        public TaskState<Real[]> data;

        public NationState(String name, double stability, double military) {
            this.name = name;
            this.data = new TaskState<>(
                new Real[]{Real.of(stability), Real.of(military)},
                r -> new double[]{r[0].doubleValue(), r[1].doubleValue()},
                d -> new Real[]{Real.of(d[0]), Real.of(d[1])},
                r -> new float[]{r[0].floatValue(), r[1].floatValue()},
                f -> new Real[]{Real.of(f[0]), Real.of(f[1])}
            );
        }

        public double getStability() {
            if (data.getDouble() != null) return data.getDouble()[0];
            if (data.getFloat() != null) return data.getFloat()[0];
            return data.getReal()[0].doubleValue();
        }

        public double getMilitary() {
            if (data.getDouble() != null) return data.getDouble()[1];
            if (data.getFloat() != null) return data.getFloat()[1];
            return data.getReal()[1].doubleValue();
        }
    }

    private List<NationState> nations;
    private GeopoliticalParameters params;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;
    private long seed;

    public GeopoliticalEngineTask(List<NationState> nations, GeopoliticalParameters params) {
        this.nations = nations;
        this.params = params;
        this.seed = System.nanoTime();
    }

    public GeopoliticalEngineTask() { this(new ArrayList<>(), GeopoliticalParameters.standard()); }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        for (NationState n : nations) if(n.data != null) n.data.syncTo(mode);
    }

    @Override
    public Class<GeopoliticalEngineTask> getInputType() { return GeopoliticalEngineTask.class; }
    @Override
    public Class<GeopoliticalEngineTask> getOutputType() { return GeopoliticalEngineTask.class; }

    @Override
    public GeopoliticalEngineTask execute(GeopoliticalEngineTask input) {
        if (input != null && input.nations != null) {
            input.run();
            return input;
        }
        return null;
    }

    @Override
    public String getTaskType() { return "GEOPOLITICS"; }

    public void run() {
        RandomGenerator rng = new RandomGenerator(seed);
        for (NationState n : nations) {
            evolveStability(n, rng);
            checkConflicts(n, nations, rng);
        }
        seed = rng.nextInteger(0, Integer.MAX_VALUE).longValue();
    }

    private void evolveStability(NationState n, RandomGenerator rng) {
        double shock = (rng.nextReal().doubleValue() - 0.5) * 0.1;
        double decay = params.stabilityDecay();
        double recovery = params.recoveryRate();

        switch (mode) {
            case REAL -> {
                Real[] d = n.data.getReal();
                double s = d[0].doubleValue();
                s = Math.max(0, Math.min(1.0, s + shock - decay + recovery * (1.0 - s)));
                d[0] = Real.of(s);
            }
            case FLOAT -> {
                float[] d = n.data.getFloat();
                d[0] = (float)Math.max(0, Math.min(1.0, d[0] + shock - decay + recovery * (1.0 - d[0])));
            }
            default -> {
                double[] d = n.data.getDouble();
                d[0] = Math.max(0, Math.min(1.0, d[0] + shock - decay + recovery * (1.0 - d[0])));
            }
        }
    }

    private void checkConflicts(NationState a, List<NationState> all, RandomGenerator rng) {
        double stability = a.getStability();
        if (stability < 0.3) {
            for (NationState b : all) {
                if (a != b && !a.allies.contains(b.name)) {
                    double tension = (a.getMilitary() / (b.getMilitary() + 1e-6)) * (1.0 - stability);
                    if (tension > 2.0 && rng.nextReal().doubleValue() > (1.0 - params.conflictProbability())) {
                        applyConflictEffect(a, b);
                    }
                }
            }
        }
    }

    private void applyConflictEffect(NationState a, NationState b) {
        switch (mode) {
            case REAL -> {
                a.data.getReal()[0] = a.data.getReal()[0].subtract(Real.of(0.1));
                b.data.getReal()[0] = b.data.getReal()[0].subtract(Real.of(0.05));
            }
            case FLOAT -> {
                a.data.getFloat()[0] -= 0.1f;
                b.data.getFloat()[0] -= 0.05f;
            }
            default -> {
                a.data.getDouble()[0] -= 0.1;
                b.data.getDouble()[0] -= 0.05;
            }
        }
    }

    public List<NationState> getNations() { return nations; }
}
