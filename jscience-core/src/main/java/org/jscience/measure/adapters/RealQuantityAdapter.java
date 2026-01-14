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

package org.jscience.measure.adapters;

import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.measure.Quantities;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Adapters between JScience Real numbers and JSR-385 Quantities.
 * <p>
 * This class provides bidirectional conversion between JScience's
 * exact/approximate
 * Real numbers and JSR-385 Quantity values.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class RealQuantityAdapter {

    private RealQuantityAdapter() {
        // Utility class
    }

    /**
     * Converts a JScience Real to a JSR-385 Quantity.
     * 
     * @param <Q>   the quantity type
     * @param value the Real value
     * @param unit  the target unit
     * @return the equivalent Quantity
     */
    public static <Q extends Quantity<Q>> Quantity<Q> toQuantity(Real value, Unit<Q> unit) {
        return Quantities.create(value.doubleValue(), unit);
    }

    /**
     * Converts a JSR-385 Quantity to a JScience Real.
     * 
     * @param quantity the quantity to convert
     * @return the equivalent Real value
     */
    public static Real toReal(Quantity<?> quantity) {
        return Real.of(quantity.getValue().doubleValue());
    }

    /**
     * Converts a JSR-385 Quantity to a JScience exact Real.
     * 
     * @param quantity the quantity to convert
     * @return the equivalent exact Real value
     */
    public static Real toExactReal(Quantity<?> quantity) {
        return Real.of(String.valueOf(quantity.getValue()));
    }

    /**
     * Extracts the numeric value from a Quantity as a Real.
     * 
     * @param <Q>      the quantity type
     * @param quantity the quantity
     * @param unit     the unit to convert to
     * @return the value in the specified unit as a Real
     */
    public static <Q extends Quantity<Q>> Real getValue(Quantity<Q> quantity, Unit<Q> unit) {
        return Real.of(quantity.to(unit).getValue().doubleValue());
    }
}


