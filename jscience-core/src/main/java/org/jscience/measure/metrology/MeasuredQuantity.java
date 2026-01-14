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

package org.jscience.measure.metrology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;

/**
 * A measured physical quantity with associated uncertainty.
 * <p>
 * Represents a measurement as a central value with uncertainty bounds,
 * following ISO/IEC Guide 98-3 (GUM) standards. Uncertainty propagation
 * is automatic through mathematical operations.
 * </p>
 * <p>
 * <b>Example Usage:</b>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MeasuredQuantity<Q extends Quantity<Q>> {

    /**
     * Returns the central (best estimate) value of this measurement.
     * 
     * @return the measured value
     */
    Quantity<Q> getValue();

    /**
     * Returns the absolute uncertainty (Ã‚Â±).
     * <p>
     * This is the standard uncertainty (1ÃÆ’) or expanded uncertainty (kÃÆ’)
     * depending on the confidence level.
     * </p>
     * 
     * @return the uncertainty in the same units as the value
     */
    Quantity<Q> getUncertainty();

    /**
     * Returns the relative uncertainty as a fraction.
     * <p>
     * Relative uncertainty = |uncertainty / value|
     * </p>
     * 
     * @return the relative uncertainty (dimensionless)
     */
    Real getRelativeUncertainty();

    /**
     * Returns the confidence level (e.g., 0.95 for 95%).
     * <p>
     * Common levels:
     * </p>
     * <ul>
     * <li>0.683 (68.3%) Ã¢â€ â€™ 1ÃÆ’</li>
     * <li>0.954 (95.4%) Ã¢â€ â€™ 2ÃÆ’</li>
     * <li>0.997 (99.7%) Ã¢â€ â€™ 3ÃÆ’</li>
     * </ul>
     * 
     * @return the confidence level between 0 and 1
     */
    double getConfidenceLevel();

    /**
     * Returns the coverage factor k.
     * <p>
     * For normal distributions: k Ã¢â€°Ë† 1 for 68%, k Ã¢â€°Ë† 2 for 95%, k Ã¢â€°Ë† 3 for 99.7%
     * </p>
     * 
     * @return the coverage factor
     */
    default double getCoverageFactor() {
        // Approximate inverse of normal CDF
        if (getConfidenceLevel() >= 0.997)
            return 3.0;
        if (getConfidenceLevel() >= 0.954)
            return 2.0;
        return 1.0;
    }

    // ========== Arithmetic Operations with Uncertainty Propagation ==========

    /**
     * Adds another measured quantity.
     * <p>
     * Uncertainty propagation: ÃÆ’_z = Ã¢Ë†Å¡(ÃÆ’_xÃ‚Â² + ÃÆ’_yÃ‚Â²)
     * </p>
     * 
     * @param other the quantity to add
     * @return the sum with propagated uncertainty
     */
    MeasuredQuantity<Q> add(MeasuredQuantity<Q> other);

    /**
     * Subtracts another measured quantity.
     * <p>
     * Uncertainty propagation: ÃÆ’_z = Ã¢Ë†Å¡(ÃÆ’_xÃ‚Â² + ÃÆ’_yÃ‚Â²)
     * </p>
     * 
     * @param other the quantity to subtract
     * @return the difference with propagated uncertainty
     */
    MeasuredQuantity<Q> subtract(MeasuredQuantity<Q> other);

    /**
     * Multiplies by a scalar (exact, no uncertainty).
     * <p>
     * Uncertainty propagation: ÃÆ’_z = |scalar| Ãƒâ€” ÃÆ’_x
     * </p>
     * 
     * @param scalar the exact multiplier
     * @return the product with scaled uncertainty
     */
    MeasuredQuantity<Q> multiply(Real scalar);

    /**
     * Multiplies by a scalar (exact, no uncertainty).
     * 
     * @param scalar the exact multiplier
     * @return the product with scaled uncertainty
     */
    default MeasuredQuantity<Q> multiply(double scalar) {
        return multiply(Real.of(scalar));
    }

    /**
     * Multiplies by another measured quantity.
     * <p>
     * Uncertainty propagation: ÃÆ’_z/z = Ã¢Ë†Å¡((ÃÆ’_x/x)Ã‚Â² + (ÃÆ’_y/y)Ã‚Â²)
     * </p>
     * 
     * @param <R>   the other quantity type
     * @param other the quantity to multiply by
     * @return the product with propagated uncertainty
     */
    <R extends Quantity<R>> MeasuredQuantity<?> multiply(MeasuredQuantity<R> other);

    /**
     * Divides by a scalar (exact, no uncertainty).
     * <p>
     * Uncertainty propagation: ÃÆ’_z = ÃÆ’_x / |scalar|
     * </p>
     * 
     * @param scalar the exact divisor
     * @return the quotient with scaled uncertainty
     */
    MeasuredQuantity<Q> divide(Real scalar);

    /**
     * Divides by a scalar (exact, no uncertainty).
     * 
     * @param scalar the exact divisor
     * @return the quotient with scaled uncertainty
     */
    default MeasuredQuantity<Q> divide(double scalar) {
        return divide(Real.of(scalar));
    }

    /**
     * Divides by another measured quantity.
     * <p>
     * Uncertainty propagation: ÃÆ’_z/z = Ã¢Ë†Å¡((ÃÆ’_x/x)Ã‚Â² + (ÃÆ’_y/y)Ã‚Â²)
     * </p>
     * 
     * @param <R>   the other quantity type
     * @param other the quantity to divide by
     * @return the quotient with propagated uncertainty
     */
    <R extends Quantity<R>> MeasuredQuantity<?> divide(MeasuredQuantity<R> other);

    // ========== Unit Conversion ==========

    /**
     * Converts this measurement to another unit.
     * <p>
     * Both value and uncertainty are converted.
     * </p>
     * 
     * @param targetUnit the target unit
     * @return the measurement in the new unit
     */
    MeasuredQuantity<Q> to(org.jscience.measure.Unit<Q> targetUnit);

    // ========== Display Formats ==========

    /**
     * Returns a string representation in scientific notation.
     * <p>
     * Format: "value Ã‚Â± uncertainty unit (confidence%)"
     * Example: "10.0 Ã‚Â± 0.2 m (95%)"
     * </p>
     * 
     * @return the formatted string
     */
    String toScientificNotation();

    /**
     * Returns a string representation with relative uncertainty.
     * <p>
     * Format: "value unit Ã‚Â± percentage%"
     * Example: "10.0 m Ã‚Â± 2.0%"
     * </p>
     * 
     * @return the formatted string
     */
    String toPercentageNotation();

    /**
     * Returns a concise string representation.
     * <p>
     * Format: "value [lower, upper] unit"
     * Example: "10.0 [9.8, 10.2] m"
     * </p>
     * 
     * @return the formatted string
     */
    String toIntervalNotation();

    // ========== Statistical Properties ==========

    /**
     * Returns true if this measurement overlaps with another within uncertainties.
     * 
     * @param other the other measurement
     * @return true if confidence intervals overlap
     */
    boolean overlaps(MeasuredQuantity<Q> other);

    /**
     * Returns true if this measurement is consistent with another.
     * <p>
     * Consistency check: |x - y| Ã¢â€°Â¤ kÃ¢Ë†Å¡(ÃÆ’_xÃ‚Â² + ÃÆ’_yÃ‚Â²)
     * </p>
     * 
     * @param other the other measurement
     * @return true if measurements are consistent
     */
    boolean isConsistentWith(MeasuredQuantity<Q> other);

    /**
     * Returns the number of standard deviations between this and another
     * measurement.
     * <p>
     * Also known as normalized distance or z-score.
     * </p>
     * 
     * @param other the other measurement
     * @return the number of standard deviations
     */
    Real getStandardizedDifference(MeasuredQuantity<Q> other);
}

