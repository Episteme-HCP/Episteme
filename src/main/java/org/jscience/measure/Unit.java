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

/**
 * A unit of measurement for a physical quantity.
 * <p>
 * Units define how quantities are measured and provide conversion between
 * different units of the same dimension. Units are immutable and can be
 * combined through multiplication and division to form derived units.
 * </p>
 * <p>
 * <b>Example Usage:</b>
 * 
 * <pre>{@code
 * // Base units
 * Unit<Length> meter = Units.METER;
 * Unit<Time> second = Units.SECOND;
 * 
 * // Derived units
 * Unit<Velocity> mps = meter.divide(second); // m/s
 * Unit<Force> newton = Units.NEWTON; // kg⋅m/s²
 * 
 * // Unit conversions
 * UnitConverter toKm = meter.getConverterTo(Units.KILOMETER);
 * Real km = toKm.convert(Real.of(1000)); // 1 km
 * }</pre>
 * </p>
 * 
 * @param <Q> the quantity type this unit measures
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 * 
 * @see Quantity
 * @see Dimension
 * @see UnitConverter
 */
public interface Unit<Q extends Quantity<Q>> {

    /**
     * Returns the symbol for this unit (e.g., "m", "kg", "s").
     * <p>
     * Symbols follow UCUM (Unified Code for Units of Measure) conventions
     * where possible.
     * </p>
     * 
     * @return the unit symbol
     */
    String getSymbol();

    /**
     * Returns the human-readable name for this unit (e.g., "meter", "kilogram").
     * 
     * @return the unit name
     */
    String getName();

    /**
     * Returns the dimension of this unit.
     * <p>
     * The dimension describes the physical nature of the quantity (e.g.,
     * length, mass, time) as a combination of base SI dimensions.
     * </p>
     * 
     * @return the dimension
     */
    Dimension getDimension();

    /**
     * Returns a converter to convert values from this unit to the target unit.
     * <p>
     * Both units must have compatible dimensions.
     * </p>
     * 
     * @param targetUnit the target unit
     * @return a converter from this unit to the target
     * @throws IllegalArgumentException if units are incompatible
     */
    UnitConverter getConverterTo(Unit<Q> targetUnit);

    /**
     * Returns a converter from the target unit to this unit.
     * <p>
     * This is the inverse of {@link #getConverterTo(Unit)}.
     * </p>
     * 
     * @param sourceUnit the source unit
     * @return a converter from the source unit to this unit
     */
    default UnitConverter getConverterFrom(Unit<Q> sourceUnit) {
        return sourceUnit.getConverterTo(this);
    }

    /**
     * Returns a unit equal to this unit multiplied by the specified factor.
     * <p>
     * For example, {@code METER.multiply(1000)} gives kilometer.
     * </p>
     * 
     * @param factor the multiplier
     * @return the scaled unit
     */
    Unit<Q> multiply(double factor);

    /**
     * Returns a unit equal to this unit divided by the specified factor.
     * <p>
     * For example, {@code METER.divide(100)} gives centimeter.
     * </p>
     * 
     * @param factor the divisor
     * @return the scaled unit
     */
    default Unit<Q> divide(double factor) {
        return multiply(1.0 / factor);
    }

    /**
     * Returns the product of this unit with another unit.
     * <p>
     * For example, multiplying Length by Length gives Area.
     * </p>
     * 
     * @param <R>   the other quantity type
     * @param other the unit to multiply with
     * @return the product unit
     */
    <R extends Quantity<R>> Unit<?> multiply(Unit<R> other);

    /**
     * Returns the quotient of this unit by another unit.
     * <p>
     * For example, dividing Length by Time gives Velocity.
     * </p>
     * 
     * @param <R>   the other quantity type
     * @param other the unit to divide by
     * @return the quotient unit
     */
    <R extends Quantity<R>> Unit<?> divide(Unit<R> other);

    /**
     * Returns this unit raised to the specified power.
     * <p>
     * For example, {@code METER.pow(2)} gives square meters (area).
     * </p>
     * 
     * @param exponent the exponent
     * @return the unit raised to the power
     */
    Unit<?> pow(int exponent);

    /**
     * Casts this unit to a specific quantity type.
     * <p>
     * This is primarily used for type safety when creating derived units.
     * For example, casting J/K to Entropy type.
     * </p>
     * 
     * @param <R>  the target quantity type
     * @param type the target quantity class
     * @return this unit cast to the target type
     */
    @SuppressWarnings("unchecked")
    default <R extends Quantity<R>> Unit<R> asType(Class<R> type) {
        return (Unit<R>) this;
    }

    /**
     * Returns the inverse of this unit.
     * <p>
     * For example, the inverse of Time is Frequency (1/s = Hz).
     * </p>
     * 
     * @return the inverse unit
     */
    default Unit<?> inverse() {
        return pow(-1);
    }

    /**
     * Returns the square root of this unit.
     * <p>
     * The dimension exponents must all be even.
     * </p>
     * 
     * @return the square root unit
     * @throws IllegalArgumentException if square root is undefined
     */
    Unit<?> sqrt();

    /**
     * Checks if this unit is compatible with another unit.
     * <p>
     * Units are compatible if they have the same dimension, allowing
     * conversion between them.
     * </p>
     * 
     * @param other the other unit
     * @return true if units are compatible
     */
    default boolean isCompatible(Unit<?> other) {
        return getDimension().equals(other.getDimension());
    }

    /**
     * Checks if this is a base SI unit (meter, kilogram, second, etc.).
     * 
     * @return true if this is a base unit
     */
    boolean isBaseUnit();
}
