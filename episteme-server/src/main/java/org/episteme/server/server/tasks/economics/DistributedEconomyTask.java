/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.economics;

import java.io.Serializable;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.distributed.TaskState;
import org.episteme.core.mathematics.random.RandomGenerator;
import org.episteme.natural.economics.growth.EconomyProvider;
import org.episteme.natural.economics.growth.EconomyParameters;
import org.episteme.natural.economics.growth.providers.StandardEconomyProvider;


/**
 * Distributed Economic Simulation Task.
 * 
 * Simulates macro-economic dynamics using a stochastic Solow-Swan growth model.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DistributedEconomyTask implements java.io.Serializable {

    private String economyName;
    private EconomyParameters params;
    private TaskState<Real> state;
    
    private double dt = 0.1;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.REAL;
    private long seed;

    public DistributedEconomyTask(String name, Real initialK, EconomyParameters params) {
        this.economyName = name;
        this.params = params;
        this.seed = System.nanoTime();
        this.state = new TaskState<>(initialK, 
            k -> new double[]{k.doubleValue()}, 
            arr -> Real.of(arr[0]),
            k -> new float[]{k.floatValue()}, 
            arr -> Real.of(arr[0])
        );
    }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        this.state.syncTo(mode);
    }

    public void run() {
        EconomyProvider provider = new StandardEconomyProvider();
        RandomGenerator rng = new RandomGenerator(seed);
        double dW = rng.nextGaussian().doubleValue() * Math.sqrt(dt);

        switch (mode) {
            case REAL -> state.setReal(provider.evolve(state.getReal(), params, dt, dW));
            case FLOAT -> state.getFloat()[0] = provider.evolve(state.getFloat()[0], params, (float)dt, (float)dW);
            default -> state.getDouble()[0] = provider.evolve(state.getDouble()[0], params, dt, dW);
        }
        seed = rng.nextInteger(0, Integer.MAX_VALUE).intValue(); // Advance seed
    }

    public Real getGDP() {
        double kVal = getCapital().doubleValue();
        return Real.of(Math.pow(kVal, params.capitalShare()));
    }

    public Real getCapital() {
        state.syncTo(TaskRegistry.PrecisionMode.REAL);
        return state.getReal();
    }

    public String getEconomyName() {
        return economyName;
    }
}
