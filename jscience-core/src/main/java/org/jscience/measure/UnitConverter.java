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

package org.jscience.measure;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Converts values between compatible units of measurement.
 * <p>
 * Unit converters provide transformation between different units of the
 * same dimension. Converters are composable and can be chained or
 * combined.
 * </p>
 * <p>
 * <b>Example Usage:</b>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface UnitConverter {

    /**
     * Converts a value from the source unit to the target unit.
     * 
     * @param value the value to convert
     * @return the converted value
     */
    Real convert(Real value);

    /**
     * Converts a value from the source unit to the target unit.
     * 
     * @param value the value to convert
     * @return the converted value
     */
    default double convert(double value) {
        return convert(Real.of(value)).doubleValue();
    }

    /**
     * Returns the inverse of this converter.
     * <p>
     * The inverse converter performs the reverse transformation.
     * </p>
     * 
     * @return the inverse converter
     */
    UnitConverter inverse();

    /**
     * Returns a converter that first applies this converter, then the other.
     * <p>
     * For example, if this converts meters to feet and {@code other} converts
     * feet to miles, the result converts meters to miles.
     * </p>
     * 
     * @param other the converter to apply after this one
     * @return the concatenated converter
     */
    /**
     * Returns a converter that first applies this converter, then the other.
     * <p>
     * For example, if this converts meters to feet and {@code other} converts
     * feet to miles, the result converts meters to miles.
     * </p>
     * 
     * @param other the converter to apply after this one
     * @return the concatenated converter
     */
    default UnitConverter concatenate(UnitConverter other) {
        return new org.jscience.measure.converters.CompositeConverter(this, other);
    }

    /**
     * Checks if this converter is an identity converter (no conversion).
     * 
     * @return true if this is the identity converter
     */
    boolean isIdentity();

    /**
     * Checks if this converter is linear (scale only, no offset).
     * <p>
     * Linear converters satisfy: f(x + y) = f(x) + f(y) and f(ax) = aÃ¢â€¹â€¦f(x).
     * </p>
     * 
     * @return true if this converter is linear
     */
    boolean isLinear();

    /**
     * Returns the derivative of the conversion function at the given value.
     * <p>
     * For linear converters, this is constant. For affine converters
     * (like temperature), this is also constant.
     * </p>
     * 
     * @param value the value at which to evaluate the derivative
     * @return the derivative (conversion rate)
     */
    Real derivative(Real value);

    /**
     * Returns the identity converter.
     * 
     * @return the identity converter
     */
    static UnitConverter identity() {
        return org.jscience.measure.converters.IdentityConverter.INSTANCE;
    }
}

