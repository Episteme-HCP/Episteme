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

package org.jscience.mathematics.statistics.distributions;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.statistics.ContinuousDistribution;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StudentTDistribution extends ContinuousDistribution {
    private final int degreesOfFreedom;
    private final double normConstant;

    public StudentTDistribution(int degreesOfFreedom) {
        if (degreesOfFreedom <= 0)
            throw new IllegalArgumentException("df must be positive");
        this.degreesOfFreedom = degreesOfFreedom;
        this.normConstant = computeNormConstant(degreesOfFreedom);
    }

    private double computeNormConstant(int nu) {
        double lgammaTop = lgamma((nu + 1) / 2.0);
        double lgammaBot = lgamma(nu / 2.0);
        return Math.exp(lgammaTop - lgammaBot - 0.5 * Math.log(nu * Math.PI));
    }

    private double lgamma(double x) {
        return (x - 0.5) * Math.log(x) - x + 0.5 * Math.log(2 * Math.PI);
    }

    @Override
    public Real density(Real x) {
        double xVal = x.doubleValue();
        double nu = degreesOfFreedom;
        double result = normConstant * Math.pow(1 + (xVal * xVal) / nu, -(nu + 1) / 2.0);
        return Real.of(result);
    }

    @Override
    public Real mean() {
        if (degreesOfFreedom <= 1)
            throw new ArithmeticException("Mean undefined for ν ≤ 1");
        return Real.ZERO;
    }

    @Override
    public Real variance() {
        if (degreesOfFreedom <= 2)
            throw new ArithmeticException("Variance undefined for ν ≤ 2");
        return Real.of((double) degreesOfFreedom / (degreesOfFreedom - 2));
    }

    @Override
    public String toString() {
        return String.format("t(ν=%d)", degreesOfFreedom);
    }
}