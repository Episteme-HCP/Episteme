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
import org.jscience.mathematics.numbers.real.Real;

/**
 * The Hénon Map:
 * x_{n+1} = 1 - a * x_n^2 + y_n
 * y_{n+1} = b * x_n
 * <p>
 * A discrete-time dynamical system that exhibits chaotic behavior.
 * Classic values: a = 1.4, b = 0.3.
 * </p>
 *
 * <h2>References</h2>
 * <ul>
 * <li>Michel Hénon, "A two-dimensional mapping with a strange attractor",
 * Communications in Mathematical Physics, Vol. 50, No. 1, 1976, pp. 69-77</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HenonMap implements DiscreteMap<Point2D> {

    private final Real a;
    private final Real b;

    /**
     * Creates a Hénon Map with default chaotic parameters (a=1.4, b=0.3).
     */
    public HenonMap() {
        this(1.4, 0.3);
    }

    /**
     * Creates a Hénon Map with specified parameters (double precision).
     * <p>
     * For arbitrary precision, use {@link #HenonMap(Real, Real)}.
     * </p>
     * 
     * @param a parameter a
     * @param b parameter b
     */
    public HenonMap(double a, double b) {
        this(Real.of(a), Real.of(b));
    }

    /**
     * Creates a Hénon Map with specified parameters (arbitrary precision).
     * 
     * @param a parameter a
     * @param b parameter b
     */
    public HenonMap(Real a, Real b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Point2D evaluate(Point2D p) {
        Real x = p.getX();
        Real y = p.getY();

        Real nextX = Real.ONE.subtract(a.multiply(x).multiply(x)).add(y);
        Real nextY = b.multiply(x);

        return Point2D.of(nextX, nextY);
    }

    /**
     * Gets parameter a.
     * 
     * @return parameter a
     */
    public Real getA() {
        return a;
    }

    /**
     * Gets parameter b.
     * 
     * @return parameter b
     */
    public Real getB() {
        return b;
    }

    @Override
    public String getDomain() {
        return "ℝ²";
    }

    @Override
    public String getCodomain() {
        return "ℝ²";
    }

    @Override
    public String toString() {
        return "HenonMap(a=" + a + ", b=" + b + ")";
    }
}