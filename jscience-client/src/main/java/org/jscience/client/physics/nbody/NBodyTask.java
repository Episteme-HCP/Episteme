/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.client.physics.nbody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * N-Body Gravitational Simulation Task.
 * Computes gravitational interactions between bodies using direct summation.
 */
public class NBodyTask implements Serializable {

    public static class Body implements Serializable {
        public double x, y, z;
        public double vx, vy, vz;
        public double mass;

        public Body(double x, double y, double z, double vx, double vy, double vz, double mass) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            this.mass = mass;
        }
    }

    private List<Body> bodies;
    private double dt = 0.01;
    private double softening = 0.1;
    private static final double G = 1.0; // Normalized units

    public NBodyTask(List<Body> bodies) {
        this.bodies = new ArrayList<>(bodies);
    }

    public void step() {
        int n = bodies.size();
        double[][] acc = new double[n][3];

        // Compute accelerations
        for (int i = 0; i < n; i++) {
            Body bi = bodies.get(i);
            for (int j = i + 1; j < n; j++) {
                Body bj = bodies.get(j);
                double dx = bj.x - bi.x;
                double dy = bj.y - bi.y;
                double dz = bj.z - bi.z;
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz + softening * softening);
                double f = G / (dist * dist * dist);

                acc[i][0] += f * dx * bj.mass;
                acc[i][1] += f * dy * bj.mass;
                acc[i][2] += f * dz * bj.mass;

                acc[j][0] -= f * dx * bi.mass;
                acc[j][1] -= f * dy * bi.mass;
                acc[j][2] -= f * dz * bi.mass;
            }
        }

        // Update velocities and positions
        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            b.vx += acc[i][0] * dt;
            b.vy += acc[i][1] * dt;
            b.vz += acc[i][2] * dt;
            b.x += b.vx * dt;
            b.y += b.vy * dt;
            b.z += b.vz * dt;
        }
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public void setSoftening(double s) {
        this.softening = s;
    }
}
