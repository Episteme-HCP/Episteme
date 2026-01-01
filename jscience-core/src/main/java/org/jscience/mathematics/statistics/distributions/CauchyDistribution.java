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
 * Cauchy distribution (also called Lorentz distribution).
 * PDF: f(x) = 1/(Ãâ‚¬ÃŽÂ³[1 + ((x-xÃ¢â€šâ‚¬)/ÃŽÂ³)Ã‚Â²])
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CauchyDistribution extends ContinuousDistribution {
    private final Real location; // xÃ¢â€šâ‚¬
    private final Real scale; // ÃŽÂ³

    public CauchyDistribution() {
        this(Real.ZERO, Real.ONE);
    }

    public CauchyDistribution(Real location, Real scale) {
        if (scale.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Scale must be positive");
        }
        this.location = location;
        this.scale = scale;
    }

    @Override
    public Real density(Real x) {
        double x0 = location.doubleValue();
        double gamma = scale.doubleValue();
        double xVal = x.doubleValue();

        double numerator = 1.0;
        double denominator = Math.PI * gamma * (1 + Math.pow((xVal - x0) / gamma, 2));

        return Real.of(numerator / denominator);
    }

    @Override
    public Real mean() {
        throw new UnsupportedOperationException("Cauchy distribution has no defined mean");
    }

    @Override
    public Real variance() {
        throw new UnsupportedOperationException("Cauchy distribution has no defined variance");
    }

    @Override
    public String toString() {
        return String.format("Cauchy(xÃ¢â€šâ‚¬=%.4f, ÃŽÂ³=%.4f)", location.doubleValue(), scale.doubleValue());
    }
}

