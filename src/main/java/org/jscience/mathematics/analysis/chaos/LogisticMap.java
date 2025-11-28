/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.number.Real;

/**
 * The Logistic Map: x_{n+1} = r * x_n * (1 - x_n).
 * <p>
 * A classic example of how complex, chaotic behaviour can arise from very
 * simple non-linear dynamical equations. The logistic map exhibits a
 * period-doubling
 * route to chaos as the parameter r increases.
 * </p>
 * <p>
 * <b>Parameter Ranges:</b>
 * <ul>
 * <li>0 ≤ r ≤ 1: Population dies out</li>
 * <li>1 < r < 3: Converges to (r-1)/r</li>
 * <li>3 ≤ r < 1+√6 ≈ 3.45: Oscillates between two values</li>
 * <li>r ≈ 3.57: Period-doubling cascade to chaos</li>
 * <li>r = 4: Fully chaotic on [0, 1]</li>
 * </ul>
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li>May, Robert M. (1976). "Simple mathematical models with very complicated
 * dynamics".
 * <i>Nature</i>. <b>261</b> (5560): 459–467. doi:10.1038/261459a0</li>
 * <li><a href="https://en.wikipedia.org/wiki/Logistic_map">Wikipedia: Logistic
 * map</a></li>
 * </ul>
 * 
 * @author Robert M. May (original mathematical model, 1976)
 * @author Silvere Martin-Michiellot (implementation)
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LogisticMap implements DiscreteMap<Double> {

    private final Real r;

    /**
     * Creates a Logistic Map with parameter r (double precision).
     * <p>
     * For arbitrary precision, use {@link #LogisticMap(Real)}.
     * </p>
     * 
     * @param r the growth rate parameter (typically [0, 4])
     */
    public LogisticMap(double r) {
        this(Real.of(r));
    }

    /**
     * Creates a Logistic Map with parameter r (arbitrary precision).
     * 
     * @param r the growth rate parameter (typically [0, 4])
     */
    public LogisticMap(Real r) {
        this.r = r;
    }

    @Override
    public Double apply(Double x) {
        return iterate(Real.of(x)).doubleValue();
    }

    @Override
    public Double evaluate(Double x) {
        return apply(x);
    }

    /**
     * Iterates the logistic map (arbitrary precision).
     * 
     * @param x current value
     * @return next value: r * x * (1 - x)
     */
    public Real iterate(Real x) {
        // x_{n+1} = r * x_n * (1 - x_n)
        return r.multiply(x).multiply(Real.ONE.subtract(x));
    }

    /**
     * Returns the growth rate parameter.
     * 
     * @return r parameter
     */
    public Real getR() {
        return r;
    }

    @Override
    public String getDomain() {
        return "[0, 1]";
    }

    @Override
    public String getCodomain() {
        return "[0, 1]";
    }

    @Override
    public String toString() {
        return "LogisticMap(r=" + r + ")";
    }
}
