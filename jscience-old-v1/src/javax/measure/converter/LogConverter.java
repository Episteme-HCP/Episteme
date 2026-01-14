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

package javax.measure.converter;

/**
 * <p> This class represents a logarithmic converter. Such converter 
 *     is typically used to create logarithmic unit. For example:[code]
 *     Unit<Dimensionless> BEL = Unit.ONE.transform(new LogConverter(10).inverse());
 *     [/code]</p>
 *     
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public final class LogConverter extends UnitConverter {

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
     * @param  base the logarithmic base (e.g. <code>Math.E</code> for
     *         the Natural Logarithm).
     */
    public LogConverter(double base) {
        _base = base;
        _logBase = Math.log(base);
        _invLogBase = 1.0 / _logBase;
    }

    /**
     * Returns the logarithmic base of this converter.
     *
     * @return the logarithmic base (e.g. <code>Math.E</code> for
     *         the Natural Logarithm).
     */
    public double getBase() {
        return _base;
    }

    @Override
    public UnitConverter inverse() {
        return _inverse;
    }

    @Override
    public double convert(double amount) {
        return _invLogBase * Math.log(amount);
    }

    @Override
    public boolean isLinear() {
        return false;
    }

    /**
     * This inner class represents the inverse of the logarithmic converter
     * (exponentiation converter).
     */
    private class Inverse extends UnitConverter {


        @Override
        public UnitConverter inverse() {
            return LogConverter.this;
        }

        @Override
        public double convert(double amount) {
            return Math.exp(_logBase * amount);
        }

        @Override
        public boolean isLinear() {
            return false;
        }

        private static final long serialVersionUID = 1L;
    }

    private static final long serialVersionUID = 1L;
}
