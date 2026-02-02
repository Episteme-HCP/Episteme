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
package org.jscience.core.computing.ai.simulation.swarm;

import java.util.Random;
import java.util.function.Function;

/**
 * Particle Swarm Optimization (PSO) implementation.
 * <p>
 * Minimizes a function defined over a continuous space.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class PSO {
    private int particleCount;
    private int dimensions;
    private double[][] positions;
    private double[][] velocities;
    private double[][] pBestPositions; // Personal best positions
    private double[] pBestScores;     // Personal best scores
    private double[] gBestPosition;   // Global best position
    private double gBestScore;        // Global best score
    
    private double w = 0.729; // Inertia weight
    private double c1 = 1.49445; // Cognito (personal) weight
    private double c2 = 1.49445; // Social (global) weight
    
    private Function<double[], Double> fitnessFunction;
    private double[] lowerBounds;
    private double[] upperBounds;
    private Random random = new Random();

    public PSO(int particleCount, int dimensions, double[] lowerBounds, double[] upperBounds, Function<double[], Double> fitnessFunction) {
        this.particleCount = particleCount;
        this.dimensions = dimensions;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.fitnessFunction = fitnessFunction;
        
        init();
    }
    
    private void init() {
        positions = new double[particleCount][dimensions];
        velocities = new double[particleCount][dimensions];
        pBestPositions = new double[particleCount][dimensions];
        pBestScores = new double[particleCount];
        gBestPosition = new double[dimensions];
        gBestScore = Double.MAX_VALUE;
        
        for (int i = 0; i < particleCount; i++) {
            for (int d = 0; d < dimensions; d++) {
                positions[i][d] = lowerBounds[d] + random.nextDouble() * (upperBounds[d] - lowerBounds[d]);
                velocities[i][d] = random.nextDouble() * 2 - 1; // Small random velocity
                pBestPositions[i][d] = positions[i][d];
            }
            double score = fitnessFunction.apply(positions[i]);
            pBestScores[i] = score;
            
            if (score < gBestScore) {
                gBestScore = score;
                System.arraycopy(positions[i], 0, gBestPosition, 0, dimensions);
            }
        }
    }
    
    public void step() {
        for (int i = 0; i < particleCount; i++) {
            for (int d = 0; d < dimensions; d++) {
                double r1 = random.nextDouble();
                double r2 = random.nextDouble();
                
                // Update velocity
                velocities[i][d] = w * velocities[i][d] +
                                   c1 * r1 * (pBestPositions[i][d] - positions[i][d]) +
                                   c2 * r2 * (gBestPosition[d] - positions[i][d]);
                                   
                // Update position
                positions[i][d] += velocities[i][d];
                
                // Clamp bounds
                if (positions[i][d] < lowerBounds[d]) positions[i][d] = lowerBounds[d];
                if (positions[i][d] > upperBounds[d]) positions[i][d] = upperBounds[d];
            }
            
            // Evaluate
            double score = fitnessFunction.apply(positions[i]);
            
            // Update personal best
            if (score < pBestScores[i]) {
                pBestScores[i] = score;
                System.arraycopy(positions[i], 0, pBestPositions[i], 0, dimensions);
            }
            
            // Update global best
            if (score < gBestScore) {
                gBestScore = score;
                System.arraycopy(positions[i], 0, gBestPosition, 0, dimensions);
            }
        }
    }
    
    public double[] getBestPosition() {
        return gBestPosition;
    }
    
    public double getBestScore() {
        return gBestScore;
    }
}
