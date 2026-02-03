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

import java.util.Arrays;
import java.util.Random;

/**
 * Represents a particle in a Particle Swarm Optimization (PSO).
 */
public class Particle {
    private double[] position;
    private double[] velocity;
    private double[] bestPosition;
    private double bestFitness = Double.NEGATIVE_INFINITY;
    
    private static final Random random = new Random();

    public Particle(int dimensions, double min, double max) {
        this.position = new double[dimensions];
        this.velocity = new double[dimensions];
        this.bestPosition = new double[dimensions];
        
        for (int i = 0; i < dimensions; i++) {
            position[i] = min + (max - min) * random.nextDouble();
            velocity[i] = (random.nextDouble() - 0.5) * (max - min) * 0.1;
        }
        System.arraycopy(position, 0, bestPosition, 0, dimensions);
    }

    public double[] getPosition() { return position; }
    public double[] getVelocity() { return velocity; }
    public double[] getBestPosition() { return bestPosition; }
    public double getBestFitness() { return bestFitness; }

    public void updateBest(double currentFitness) {
        if (currentFitness > bestFitness) {
            bestFitness = currentFitness;
            System.arraycopy(position, 0, bestPosition, 0, position.length);
        }
    }

    public void updateVelocity(double[] globalBestPosition, double inertia, double c1, double c2) {
        for (int i = 0; i < position.length; i++) {
            double r1 = random.nextDouble();
            double r2 = random.nextDouble();
            
            velocity[i] = inertia * velocity[i] + 
                          c1 * r1 * (bestPosition[i] - position[i]) +
                          c2 * r2 * (globalBestPosition[i] - position[i]);
        }
    }

    public void updatePosition(double min, double max) {
        for (int i = 0; i < position.length; i++) {
            position[i] += velocity[i];
            // Clamp to bounds
            if (position[i] < min) position[i] = min;
            if (position[i] > max) position[i] = max;
        }
    }
}
