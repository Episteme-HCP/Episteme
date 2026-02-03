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
package org.jscience.core.mathematics.optimization.swarm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Particle Swarm Optimization (PSO) Engine.
 */
public class PSO {
    private final List<Particle> swarm;
    private final ToDoubleFunction<double[]> fitnessFunction;
    private final int dimensions;
    private final double min;
    private final double max;

    private double[] globalBestPosition;
    private double globalBestFitness = Double.NEGATIVE_INFINITY;

    // PSO Parameters
    private double inertia = 0.729;
    private double cognitiveWeight = 1.494; // c1
    private double socialWeight = 1.494;    // c2

    public PSO(int swarmSize, int dimensions, double min, double max, ToDoubleFunction<double[]> fitnessFunction) {
        this.swarm = new ArrayList<>(swarmSize);
        this.dimensions = dimensions;
        this.min = min;
        this.max = max;
        this.fitnessFunction = fitnessFunction;
        this.globalBestPosition = new double[dimensions];

        for (int i = 0; i < swarmSize; i++) {
            swarm.add(new Particle(dimensions, min, max));
        }
    }

    public void step() {
        for (Particle p : swarm) {
            double fitness = fitnessFunction.applyAsDouble(p.getPosition());
            p.updateBest(fitness);
            
            if (fitness > globalBestFitness) {
                globalBestFitness = fitness;
                System.arraycopy(p.getPosition(), 0, globalBestPosition, 0, dimensions);
            }
        }

        for (Particle p : swarm) {
            p.updateVelocity(globalBestPosition, inertia, cognitiveWeight, socialWeight);
            p.updatePosition(min, max);
        }
    }

    public double[] getBestPosition() { return globalBestPosition; }
    public double getBestFitness() { return globalBestFitness; }
    
    public void solve(int iterations) {
        for (int i = 0; i < iterations; i++) {
            step();
        }
    }
}
