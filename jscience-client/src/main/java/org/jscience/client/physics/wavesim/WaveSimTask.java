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

package org.jscience.client.physics.wavesim;

import java.io.Serializable;

/**
 * 2D Wave Equation Simulation Task.
 */
public class WaveSimTask implements Serializable {

    private final int width;
    private final int height;
    private double[][] u; // Current
    private double[][] uPrev; // Previous

    private double c = 0.5;
    private double damping = 0.99;

    public WaveSimTask(int width, int height) {
        this.width = width;
        this.height = height;
        this.u = new double[width][height];
        this.uPrev = new double[width][height];
    }

    public void step() {
        double[][] uNext = new double[width][height];
        double c2 = c * c;
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                double laplacian = u[x + 1][y] + u[x - 1][y] + u[x][y + 1] + u[x][y - 1] - 4 * u[x][y];
                uNext[x][y] = (2 * u[x][y] - uPrev[x][y] + c2 * laplacian) * damping;
            }
        }
        uPrev = u;
        u = uNext;
    }

    public double[][] getU() {
        return u;
    }

    public double[][] getUPrev() {
        return uPrev;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getC() {
        return c;
    }

    public double getDamping() {
        return damping;
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    public void updateState(double[][] u, double[][] uPrev) {
        this.u = u;
        this.uPrev = uPrev;
    }
}
