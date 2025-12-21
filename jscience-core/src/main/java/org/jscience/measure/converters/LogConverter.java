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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.measure.converters;

import org.jscience.measure.UnitConverter;
import org.jscience.mathematics.numbers.real.Real;

/**
 * <p>
 * This class represents a logarithmic converter.
 * </p>
 * 
 * <p>
 * Instances of this class are immutable.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public final class LogConverter implements UnitConverter {

    /**
     * Holds the logarithmic base.
     */
    private final double _base;

    /**
     * Holds the natural logarithm of the base.
     */
    private final double _logBase;

    /**
     * Holds the inverse of the natural logarithm of the base.
     */
    private final double _invLogBase;

    /**
     * Holds the inverse of this converter.
     */
    private final Inverse _inverse = new Inverse();

    /**
     * Creates a logarithmic converter having the specified base.
     * 
     * @param base the logarithmic base (e.g. <code>Math.E</code> for
     *             the Natural Logarithm).
     */
    public LogConverter(double base) {
        _base = base;
        _logBase = Math.log(base);
        _invLogBase = 1.0 / _logBase;
    }

    /**
     * Returns the logarithmic base of this converter.
     *
     * @return the logarithmic base.
     */
    public double getBase() {
        return _base;
    }

    @Override
    public UnitConverter inverse() {
        return _inverse;
    }

    @Override
    public Real convert(Real value) {
        // Real currently doesn't have log functions exposed directly in basic interface
        // usually,
        // relying on double conversion for this specific mathematical op if not
        // available.
        // Assuming Real wraps double or we use double value.
        return Real.of(_invLogBase * Math.log(value.doubleValue()));
    }

    @Override
    public boolean isLinear() {
        return false;
    }

    @Override
    public boolean isIdentity() {
        return false;
    }

    @Override
    public Real derivative(Real value) {
        // d(log_b(x))/dx = 1 / (x * ln(b))
        double x = value.doubleValue();
        return Real.of(1.0 / (x * _logBase));
    }

    /**
     * This inner class represents the inverse of the logarithmic converter
     * (exponentiation converter).
     */
    private class Inverse implements UnitConverter {

        @Override
        public UnitConverter inverse() {
            return LogConverter.this;
        }

        @Override
        public Real convert(Real value) {
            return Real.of(Math.exp(_logBase * value.doubleValue()));
        }

        @Override
        public boolean isLinear() {
            return false;
        }

        @Override
        public boolean isIdentity() {
            return false;
        }

        @Override
        public Real derivative(Real value) {
            // d(b^x)/dx = b^x * ln(b)
            double x = value.doubleValue();
            return Real.of(Math.pow(_base, x) * _logBase);
        }
    }
}
