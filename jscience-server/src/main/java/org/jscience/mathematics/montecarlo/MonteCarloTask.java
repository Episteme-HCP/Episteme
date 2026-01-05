/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.montecarlo;

import org.jscience.distributed.DistributedTask;

import java.util.Random;

public class MonteCarloTask implements DistributedTask<MonteCarloTask, MonteCarloTask> {

    private final long samples;
    private long insideCircle;
    private double estimatedPi;

    public MonteCarloTask(long samples) {
        this.samples = samples;
    }

    public MonteCarloTask() {
        this(0);
    }

    @Override
    public Class<MonteCarloTask> getInputType() {
        return MonteCarloTask.class;
    }

    @Override
    public Class<MonteCarloTask> getOutputType() {
        return MonteCarloTask.class;
    }

    @Override
    public MonteCarloTask execute(MonteCarloTask input) {
        if (input != null && input.samples > 0) {
            input.compute();
            return input;
        }
        if (this.samples > 0) {
            this.compute();
            return this;
        }
        return null;
    }

    @Override
    public String getTaskType() {
        return "MONTE_CARLO_PI";
    }

    public void compute() {
        Random rand = new Random();
        insideCircle = 0;
        for (long i = 0; i < samples; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            if (x * x + y * y <= 1.0) {
                insideCircle++;
            }
        }
        estimatedPi = 4.0 * insideCircle / samples;
    }

    public long getSamples() {
        return samples;
    }

    public long getInsideCircle() {
        return insideCircle;
    }

    public double getEstimatedPi() {
        return estimatedPi;
    }
}
