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
package org.jscience.measure.metrology;

import org.jscience.measure.Quantity;

/**
 * Factory for creating {@link MeasuredQuantity} instances.
 * <p>
 * Provides convenient methods for creating measured quantities with
 * uncertainty.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class MeasuredQuantities {

    private MeasuredQuantities() {
        // Utility class
    }

    /**
     * Creates a measured quantity with absolute uncertainty.
     * 
     * @param <Q>             the quantity type
     * @param value           the central value
     * @param uncertainty     the absolute uncertainty (±)
     * @param confidenceLevel the confidence level (e.g., 0.95 for 95%)
     * @return the measured quantity
     */
    public static <Q extends Quantity<Q>> MeasuredQuantity<Q> create(
            Quantity<Q> value,
            Quantity<Q> uncertainty,
            double confidenceLevel) {
        return new SimpleMeasuredQuantity<>(value, uncertainty, confidenceLevel);
    }

    /**
     * Creates a measured quantity with 95% confidence (k=2).
     * 
     * @param <Q>         the quantity type
     * @param value       the central value
     * @param uncertainty the absolute uncertainty (±)
     * @return the measured quantity with 95% confidence
     */
    public static <Q extends Quantity<Q>> MeasuredQuantity<Q> create(
            Quantity<Q> value,
            Quantity<Q> uncertainty) {
        return create(value, uncertainty, 0.954); // ~95% (2σ)
    }

    /**
     * Creates a measured quantity with relative uncertainty.
     * 
     * @param <Q>                 the quantity type
     * @param value               the central value
     * @param relativeUncertainty the relative uncertainty (e.g., 0.02 for 2%)
     * @param confidenceLevel     the confidence level
     * @return the measured quantity
     */
    public static <Q extends Quantity<Q>> MeasuredQuantity<Q> createRelative(
            Quantity<Q> value,
            double relativeUncertainty,
            double confidenceLevel) {
        Quantity<Q> absoluteUncertainty = value.multiply(relativeUncertainty);
        return create(value, absoluteUncertainty, confidenceLevel);
    }

    /**
     * Creates a measured quantity with relative uncertainty at 95% confidence.
     * 
     * @param <Q>                 the quantity type
     * @param value               the central value
     * @param relativeUncertainty the relative uncertainty (e.g., 0.02 for 2%)
     * @return the measured quantity
     */
    public static <Q extends Quantity<Q>> MeasuredQuantity<Q> createRelative(
            Quantity<Q> value,
            double relativeUncertainty) {
        return createRelative(value, relativeUncertainty, 0.954);
    }

    /**
     * Creates an exact measurement (zero uncertainty).
     * <p>
     * Use this for defined constants or when uncertainty is negligible.
     * </p>
     * 
     * @param <Q>   the quantity type
     * @param value the exact value
     * @return a measured quantity with zero uncertainty
     */
    public static <Q extends Quantity<Q>> MeasuredQuantity<Q> exact(Quantity<Q> value) {
        Quantity<Q> zero = value.multiply(0.0); // Same unit, zero value
        return create(value, zero, 1.0); // 100% confidence
    }
}