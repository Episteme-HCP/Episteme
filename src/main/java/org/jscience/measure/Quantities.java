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

import org.jscience.mathematics.numbers.real.Real;

/**
 * Factory for creating {@link Quantity} instances.
 * <p>
 * Provides convenient factory methods for creating physical quantities.
 * </p>
 * <p>
 * <b>Example Usage:</b>
 * 
 * <pre>{@code
 * import static org.jscience.measure.Units.*;
 * 
 * // Create quantities
 * Quantity<Length> distance = Quantities.create(100, METER);
 * Quantity<Time> time = Quantities.create(10, SECOND);
 * 
 * // Perform calculations
 * Quantity<Velocity> speed = distance.divide(time);  // 10 m/s
 * 
 * // Convert units
 * Quantity<Velocity> kmh = speed.to(KILOMETER_PER_HOUR);  // 36 km/h
 * }</pre>
 * </p>
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 * 
 * @see Quantity
 * @see Unit
 * @see Units
 */
public final class Quantities {

    private Quantities() {
        // Utility class
    }

    /**
     * Creates a quantity from a Real value and unit.
     * 
     * @param <Q>   the quantity type
     * @param value the numerical value
     * @param unit  the unit of measurement
     * @return the quantity
     */
    public static <Q extends Quantity<Q>> Quantity<Q> create(Real value, Unit<Q> unit) {
        return new StandardQuantity<>(value, unit);
    }

    /**
     * Creates a quantity from a double value and unit.
     * 
     * @param <Q>   the quantity type
     * @param value the numerical value
     * @param unit  the unit of measurement
     * @return the quantity
     */
    public static <Q extends Quantity<Q>> Quantity<Q> create(double value, Unit<Q> unit) {
        return create(Real.of(value), unit);
    }

    /**
     * Creates a quantity from a long value and unit.
     * 
     * @param <Q>   the quantity type
     * @param value the numerical value
     * @param unit  the unit of measurement
     * @return the quantity
     */
    public static <Q extends Quantity<Q>> Quantity<Q> create(long value, Unit<Q> unit) {
        return create(Real.of(value), unit);
    }

    /**
     * Creates a quantity from an int value and unit.
     * 
     * @param <Q>   the quantity type
     * @param value the numerical value
     * @param unit  the unit of measurement
     * @return the quantity
     */
    public static <Q extends Quantity<Q>> Quantity<Q> create(int value, Unit<Q> unit) {
        return create(Real.of(value), unit);
    }

    /**
     * Parses a quantity from a string representation.
     * <p>
     * Expected format: "value unit" (e.g., "100 m", "10.5 kg", "50 km/h")
     * </p>
     * 
     * @param <Q>          the quantity type
     * @param text         the string to parse
     * @param expectedUnit the expected unit (for validation)
     * @return the parsed quantity
     * @throws IllegalArgumentException if parsing fails
     */
    public static <Q extends Quantity<Q>> Quantity<Q> parse(String text, Unit<Q> expectedUnit) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Cannot parse null or empty string");
        }

        String[] parts = text.trim().split("\\s+", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid quantity format: " + text);
        }

        try {
            double value = Double.parseDouble(parts[0]);
            String unitSymbol = parts[1];

            // Verify unit matches or convert
            Unit<?> parsedUnit = Units.parseUnit(unitSymbol);
            if (!parsedUnit.isCompatible(expectedUnit)) {
                throw new IllegalArgumentException(
                        "Unit mismatch: expected compatible with " + expectedUnit.getSymbol() +
                                ", got " + unitSymbol);
            }

            // Create with parsed unit and convert
            // We need to cast safely because we checked compatibility
            @SuppressWarnings("unchecked")
            Unit<Q> castUnit = (Unit<Q>) parsedUnit;
            Quantity<Q> quantity = create(value, castUnit);
            return quantity.to(expectedUnit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + parts[0], e);
        }
    }

    /**
     * Parses a quantity from a string representation without specifying unit type.
     * <p>
     * Expected format: "value unit" (e.g., "100 m", "10.5 kg", "50 km/h")
     * </p>
     * <p>
     * This overload returns Quantity<?> since the unit type is not known at compile
     * time.
     * </p>
     * 
     * @param text the string to parse
     * @return the parsed quantity with unknown type
     * @throws IllegalArgumentException if parsing fails
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Quantity<?> parse(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Cannot parse null or empty string");
        }

        String[] parts = text.trim().split("\\s+", 2);
        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid quantity format: " + text);
        }

        try {
            double value = Double.parseDouble(parts[0]);
            if (parts.length == 1) {
                // Just a number, return as dimensionless
                return create(value, Units.ONE);
            }
            String unitSymbol = parts[1];

            // Try to find the unit - for now, just return with a basic unit
            // A full implementation would look up units by symbol
            Unit<?> unit = Units.parseUnit(unitSymbol);
            return new StandardQuantity(Real.of(value), unit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + parts[0], e);
        }
    }
}
