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
 * <p> This class represents a converter adding a constant offset 
 *     (approximated as a <code>double</code>) to numeric values.</p>
 *     
 * <p> Instances of this class are immutable.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public final class AddConverter extends UnitConverter {

    /**
     * Holds the offset.
     */
    private final double _offset;

    /**
     * Creates an add converter with the specified offset.
     *
     * @param  offset the offset value.
     * @throws IllegalArgumentException if offset is zero (or close to zero).
     */
    public AddConverter(double offset) {
        if ((float)offset == 0.0)
            throw new IllegalArgumentException("Identity converter not allowed");
        _offset = offset;
    }

    /**
     * Returns the offset value for this add converter.
     *
     * @return the offset value.
     */
    public double getOffset() {
        return _offset;
    }
    
    @Override
    public UnitConverter inverse() {
        return new AddConverter(- _offset);
    }

    @Override
    public double convert(double amount) {
        return amount + _offset;
    }

    @Override
    public boolean isLinear() {
        return false;
    }

    @Override
    public UnitConverter concatenate(UnitConverter converter) {
        if (converter instanceof AddConverter) {
            double offset = _offset + ((AddConverter)converter)._offset;
            return valueOf(offset);
        } else {
            return super.concatenate(converter);
        }
    }

    private static UnitConverter valueOf(double offset) {
        float asFloat = (float) offset;
        return asFloat == 0.0f ? UnitConverter.IDENTITY : new AddConverter(offset);
    }
    
    private static final long serialVersionUID = 1L;
}
