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
package org.jscience.core.computing.ai.simulation;

import java.util.Random;
import java.util.Vector;
import org.jscience.core.computing.ai.agents.FlockingAgent;

/**
 * Craig Reynolds' Boids Algorithm.
 *
 * @author James Matthews
 * @see org.jscience.core.computing.ai.agents.FlockingAgent
 */
public class Flock {
    /**
     * The number of rows in the flocking world (this is equivalent to
     * pixel height).
     */
    public int rows;

    /**
     * The number of columns in the flocking world (this is equivalent to
     * pixel width).
     */
    public int columns;

    /**
     * The vector containing all the boids.
     */
    public Vector<FlockingAgent> boids;

    /**
     * The number of boids.
     */
    public int numBoids;

    /**
     * Creates a new Flock object.
     *
     * @param numBoids number of boids.
     * @param columns width.
     * @param rows height.
     */
    public Flock(int numBoids, int columns, int rows) {
        this.rows = rows;
        this.columns = columns;
        this.numBoids = numBoids;

        boids = new Vector<>();

        Random random = new Random();

        // Randomly initialize the boids
        for (int i = 0; i < numBoids; i++) {
            FlockingAgent boid = new FlockingAgent(this);
            boid.x = random.nextInt(columns);
            boid.y = random.nextInt(rows);
            boids.add(boid);
        }
    }

    /**
     * Perform a step.
     */
    public void doStep() {
        for (int i = 0; i < numBoids; i++) {
            FlockingAgent boid = boids.get(i);
            boid.doStep();
        }
    }
}
