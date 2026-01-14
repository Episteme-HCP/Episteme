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
 * <p> This class represents a converter multiplying numeric values by a 
 *     constant scaling factor (approximated as a <code>double</code>). 
 *     For exact scaling conversions {@link RationalConverter} is preferred.</p>
 *      
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public final class MultiplyConverter extends UnitConverter {

    /**
     * Holds the scale factor.
     */
    private final double _factor;

    /**
     * Creates a multiply converter with the specified scale factor.
     *
     * @param  factor the scale factor.
     * @throws IllegalArgumentException if offset is one (or close to one).
     */
    public MultiplyConverter(double factor) {
        if ((float)factor == 1.0)
            throw new IllegalArgumentException("Identity converter not allowed");
        _factor = factor;
    }

    /**
     * Returns the scale factor.
     *
     * @return the scale factor.
     */
    public double getFactor() {
        return _factor;
    }

    @Override
    public UnitConverter inverse() {
        return new MultiplyConverter(1.0 / _factor);
    }

    @Override
    public double convert(double amount) {
        return _factor * amount;
    }

    @Override
    public boolean isLinear() {
        return true;
    }

    @Override
    public UnitConverter concatenate(UnitConverter converter) {
        if (converter instanceof MultiplyConverter) {
            double factor = _factor * ((MultiplyConverter) converter)._factor;
            return valueOf(factor);
        } else if (converter instanceof RationalConverter) {
            double factor = _factor
                    * ((RationalConverter) converter).getDividend()
                    / ((RationalConverter) converter).getDivisor();
            return valueOf(factor);
        } else {
            return super.concatenate(converter);
        }
    }

    private static UnitConverter valueOf(double factor) {
        float asFloat = (float) factor;
        return asFloat == 1.0f ? UnitConverter.IDENTITY
                : new MultiplyConverter(factor);
    }

    private static final long serialVersionUID = 1L;
}
