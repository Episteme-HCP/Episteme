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

package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.geometry.Point2D;

/**
 * The Standard Map (Chirikov-Taylor map):
 * p_{n+1} = (p_n + K * sin(theta_n)) mod 2π
 * theta_{n+1} = (theta_n + p_{n+1}) mod 2π
 * <p>
 * An area-preserving chaotic map from a square with side 2π onto itself.
 * Used to model the motion of a kicked rotator.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StandardMap implements DiscreteMap<Point2D> {

    private final double K;
    private static final double TWO_PI = 2 * Math.PI;

    /**
     * Creates a Standard Map with parameter K.
     * 
     * @param K the stochasticity parameter
     */
    public StandardMap(double K) {
        this.K = K;
    }

    @Override
    public Point2D evaluate(Point2D state) {
        // state = (theta, p)
        double theta = state.getX().doubleValue();
        double p = state.getY().doubleValue();

        double nextP = (p + K * Math.sin(theta)) % TWO_PI;
        double nextTheta = (theta + nextP) % TWO_PI;

        // Ensure positive result for modulo
        if (nextP < 0)
            nextP += TWO_PI;
        if (nextTheta < 0)
            nextTheta += TWO_PI;

        return Point2D.of(nextTheta, nextP);
    }

    @Override
    public String getDomain() {
        return "[0, 2π]²";
    }

    @Override
    public String getCodomain() {
        return "[0, 2π]²";
    }

    @Override
    public String toString() {
        return "StandardMap(K=" + K + ")";
    }
}