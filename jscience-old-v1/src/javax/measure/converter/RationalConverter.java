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
 * <p> This class represents a converter multiplying numeric values by an
 *     exact scaling factor (represented as the quotient of two 
 *     <code>long</code> numbers).</p>
 *  
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public final class RationalConverter extends UnitConverter {

    /**
     * Holds the converter dividend.
     */
    private final long _dividend;

    /**
     * Holds the converter divisor (always positive).
     */
    private final long _divisor;

    /**
     * Creates a rational converter with the specified dividend and 
     * divisor.
     *
     * @param dividend the dividend.
     * @param divisor the positive divisor.
     * @throws IllegalArgumentException if <code>divisor &lt; 0</code>
     * @throws IllegalArgumentException if <code>dividend == divisor</code>
     */
    public RationalConverter(long dividend, long divisor) {
        if (divisor < 0)
            throw new IllegalArgumentException("Negative divisor");
        if (dividend == divisor) 
            throw new IllegalArgumentException("Identity converter not allowed");
        _dividend = dividend;
        _divisor = divisor;
    }

    /**
     * Returns the dividend for this rational converter.
     *
     * @return this converter dividend.
     */
    public long getDividend() {
        return _dividend;
    }

    /**
     * Returns the positive divisor for this rational converter.
     *
     * @return this converter divisor.
     */
    public long getDivisor() {
        return _divisor;
    }

    @Override
    public UnitConverter inverse() {
        return _dividend < 0 ? new RationalConverter(-_divisor, -_dividend)
                : new RationalConverter(_divisor, _dividend);
    }

    @Override
    public double convert(double amount) {
        return amount * _dividend / _divisor;
    }

    @Override
    public boolean isLinear() {
        return true;
    }

    @Override
    public UnitConverter concatenate(UnitConverter converter) {
        if (converter instanceof RationalConverter) {
            RationalConverter that = (RationalConverter) converter;
            long dividendLong = this._dividend * that._dividend;
            long divisorLong = this._divisor * that._divisor;
            double dividendDouble = ((double)this._dividend) * that._dividend;
            double divisorDouble = ((double)this._divisor) * that._divisor;
            if ((dividendLong != dividendDouble) || 
                    (divisorLong != divisorDouble)) { // Long overflows.
                return new MultiplyConverter(dividendDouble / divisorDouble);
            }
            long gcd = gcd(dividendLong, divisorLong);
            return RationalConverter.valueOf(dividendLong / gcd, divisorLong / gcd);
        } else if (converter instanceof MultiplyConverter) {
            return converter.concatenate(this);
        } else {
            return super.concatenate(converter);
        }
    }

    private static UnitConverter valueOf(long dividend, long divisor) {
        return (dividend == 1L) && (divisor == 1L) ? UnitConverter.IDENTITY
                : new RationalConverter(dividend, divisor);
    }

    /**
     * Returns the greatest common divisor (Euclid's algorithm).
     *
     * @param  m the first number.
     * @param  nn the second number.
     * @return the greatest common divisor.
     */
    private static long gcd(long m, long n) {
        if (n == 0L) {
            return m;
        } else {
            return gcd(n, m % n);
        }
    }

    private static final long serialVersionUID = 1L;
}
