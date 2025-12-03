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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.measure;

import org.jscience.mathematics.number.Real;

/**
 * A physical quantity combining a numerical value with a unit of measurement.
 * <p>
 * This interface provides type-safe dimensional analysis using parameterized
 * types.
 * Quantities are immutable - all operations return new instances.
 * </p>
 * <p>
 * <b>Example Usage:</b>
 * 
 * <pre>{@code
 * Quantity<Length> distance = Quantities.create(100, Units.METER);
 * Quantity<Time> time = Quantities.create(10, Units.SECOND);
 * 
 * // Type-safe operations
 * Quantity<Velocity> speed = distance.divide(time);  // 10 m/s
 * 
 * // Unit conversion
 * Quantity<Length> km = distance.to(Units.KILOMETER);  // 0.1 km
 * 
 * #// Compile error - incompatible dimensions
 * // distance.add(time);  // ❌ Won't compile
 * }</pre>
 * </p>
 * 
 * @param <Q> the quantity type (e.g., Length, Mass, Time)
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 * 
 * @see Unit
 * @see Dimension
 * @see Quantities
 */
public interface Quantity<Q extends Quantity<Q>> {

    /**
     * Returns the numerical value of this quantity in its current unit.
     * 
     * @return the quantity value
     */
    Real getValue();

    /**
     * Returns the unit of measurement for this quantity.
     * 
     * @return the unit
     */
    Unit<Q> getUnit();

    /**
     * Converts this quantity to the specified unit.
     * <p>
     * Returns a new quantity with the same physical magnitude but expressed
     * in the target unit.
     * </p>
     * 
     * @param targetUnit the unit to convert to
     * @return a new quantity in the target unit
     * @throws IllegalArgumentException if units are incompatible
     */
    Quantity<Q> to(Unit<Q> targetUnit);

    /**
     * Returns the value of this quantity in the specified unit.
     * <p>
     * Convenience method equivalent to {@code to(unit).getValue()}.
     * </p>
     * 
     * @param unit the unit to convert to
     * @return the value in the specified unit
     */
    default Real getValue(Unit<Q> unit) {
        return to(unit).getValue();
    }

    /**
     * Adds another quantity to this one.
     * <p>
     * If the units differ, the other quantity is converted to this quantity's
     * unit before addition.
     * </p>
     * 
     * @param other the quantity to add
     * @return a new quantity representing the sum
     */
    Quantity<Q> add(Quantity<Q> other);

    /**
     * Subtracts another quantity from this one.
     * <p>
     * If the units differ, the other quantity is converted to this quantity's
     * unit before subtraction.
     * </p>
     * 
     * @param other the quantity to subtract
     * @return a new quantity representing the difference
     */
    Quantity<Q> subtract(Quantity<Q> other);

    /**
     * Multiplies this quantity by a scalar value.
     * 
     * @param scalar the scalar multiplier
     * @return a new quantity scaled by the given factor
     */
    Quantity<Q> multiply(Real scalar);

    /**
     * Multiplies this quantity by a scalar value.
     * 
     * @param scalar the scalar multiplier
     * @return a new quantity scaled by the given factor
     */
    default Quantity<Q> multiply(double scalar) {
        return multiply(Real.of(scalar));
    }

    /**
     * Multiplies this quantity by another quantity.
     * <p>
     * The result's unit is the product of the two units. For example,
     * multiplying Length by Length gives Area.
     * </p>
     * 
     * @param <R>   the other quantity type
     * @param other the quantity to multiply by
     * @return a new quantity representing the product
     */
    <R extends Quantity<R>> Quantity<?> multiply(Quantity<R> other);

    /**
     * Divides this quantity by a scalar value.
     * 
     * @param scalar the scalar divisor
     * @return a new quantity divided by the given factor
     */
    Quantity<Q> divide(Real scalar);

    /**
     * Divides this quantity by a scalar value.
     * 
     * @param scalar the scalar divisor
     * @return a new quantity divided by the given factor
     */
    default Quantity<Q> divide(double scalar) {
        return divide(Real.of(scalar));
    }

    /**
     * Divides this quantity by another quantity.
     * <p>
     * The result's unit is the quotient of the two units. For example,
     * dividing Length by Time gives Velocity.
     * </p>
     * 
     * @param <R>   the other quantity type
     * @param other the quantity to divide by
     * @return a new quantity representing the quotient
     */
    <R extends Quantity<R>> Quantity<?> divide(Quantity<R> other);

    /**
     * Returns the absolute value of this quantity.
     * 
     * @return a new quantity with absolute value
     */
    Quantity<Q> abs();

    /**
     * Returns the negation of this quantity.
     * 
     * @return a new quantity with negated value
     */
    Quantity<Q> negate();

    /**
     * Checks if this quantity is zero within tolerance.
     * 
     * @return true if the value is approximately zero
     */
    default boolean isZero() {
        return getValue().abs().compareTo(Real.of(1e-10)) < 0;
    }

    /**
     * Compares this quantity with another for order.
     * Both quantities are compared in this quantity's unit.
     * 
     * @param other the quantity to compare with
     * @return negative if less, zero if equal, positive if greater
     */
    int compareTo(Quantity<Q> other);

    /**
     * Checks if this quantity equals another within tolerance.
     * 
     * @param other     the quantity to compare with
     * @param tolerance the acceptable difference
     * @return true if quantities are equal within tolerance
     */
    boolean equals(Quantity<Q> other, Real tolerance);
}
