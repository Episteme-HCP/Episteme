/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
