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

// import java.io.Serializable;

/**
 * Monte Carlo Pi Estimation Task.
 */
public class MonteCarloPiTask implements DistributedTask<Long, Long> {

    private long numSamples;

    public MonteCarloPiTask(long numSamples) {
        this.numSamples = numSamples;
    }

    public MonteCarloPiTask() {
        this(0);
    }

    @Override
    public Class<Long> getInputType() {
        return Long.class;
    }

    @Override
    public Class<Long> getOutputType() {
        return Long.class;
    }

    public enum PrecisionMode {
        REALS,
        PRIMITIVES
    }

    private PrecisionMode mode = PrecisionMode.PRIMITIVES;

    public void setMode(PrecisionMode mode) {
        this.mode = mode;
    }

    /**
     * Executes the task.
     * 
     * @param input Optional input. If null, uses internal numSamples.
     *              If provided, overrides internal numSamples (for pure function
     *              usage).
     * @return Number of points inside circle
     */
    @Override
    public Long execute(Long input) {
        long samples = (input != null) ? input : this.numSamples;

        if (mode == PrecisionMode.REALS) {
            // JScience Mode: Use Real-based Provider
            org.jscience.technical.backend.algorithms.MonteCarloPiProvider provider = new org.jscience.technical.backend.algorithms.MulticoreMonteCarloPiProvider();
            return provider.countPointsInside(samples);
        } else {
            // Primitive Mode: Use side-by-side Support
            MonteCarloPiPrimitiveSupport support = new MonteCarloPiPrimitiveSupport();
            return support.countPointsInside(samples);
        }
    }

    @Override
    public String getTaskType() {
        return "MONTECARLO_PI";
    }

    public long getNumSamples() {
        return numSamples;
    }
}
