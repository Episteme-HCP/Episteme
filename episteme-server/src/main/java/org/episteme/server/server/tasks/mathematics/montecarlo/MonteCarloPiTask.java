/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.mathematics.montecarlo;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.statistics.montecarlo.MonteCarloProvider;
import org.episteme.core.mathematics.statistics.montecarlo.providers.MulticoreMonteCarloProvider;
import com.google.auto.service.AutoService;

/**
 * Monte Carlo Pi Estimation Task.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(DistributedTask.class)
public class MonteCarloPiTask implements DistributedTask<Long, Long> {

    private final long numSamples;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;

    public MonteCarloPiTask(long numSamples) {
        this.numSamples = numSamples;
    }

    public MonteCarloPiTask() {
        this(0);
    }

    @Override
    public Class<Long> getInputType() { return Long.class; }
    @Override
    public Class<Long> getOutputType() { return Long.class; }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
    }

    @Override
    public Long execute(Long input) {
        long samples = (input != null) ? input : this.numSamples;
        MonteCarloProvider provider = new MulticoreMonteCarloProvider();

        double pi;
        switch (mode) {
            case REAL -> pi = provider.estimatePi((int) samples, true).doubleValue();
            case FLOAT -> pi = (double) provider.estimatePiFloat((int) samples);
            default -> pi = provider.estimatePi((int) samples);
        }
        return (long) (samples * (pi / 4.0));
    }

    @Override
    public String getTaskType() { return "MONTECARLO_PI"; }
    public long getNumSamples() { return numSamples; }
}
