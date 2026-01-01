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
 * Standard implementation of {@link Quantity}.
 * 
 * @param <Q> the quantity type
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
final class StandardQuantity<Q extends Quantity<Q>> implements Quantity<Q> {

    private final Real value;
    private final Unit<Q> unit;

    StandardQuantity(Real value, Unit<Q> unit) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }

    @Override
    public Real getValue() {
        return value;
    }

    @Override
    public Unit<Q> getUnit() {
        return unit;
    }

    @Override
    public Quantity<Q> to(Unit<Q> targetUnit) {
        if (unit.equals(targetUnit)) {
            return this;
        }

        UnitConverter converter = unit.getConverterTo(targetUnit);
        Real convertedValue = converter.convert(value);

        return new StandardQuantity<>(convertedValue, targetUnit);
    }

    @Override
    public Quantity<Q> add(Quantity<Q> other) {
        // Convert other to thisunit
        Quantity<Q> converted = other.to(unit);
        Real newValue = value.add(converted.getValue());
        return new StandardQuantity<>(newValue, unit);
    }

    @Override
    public Quantity<Q> subtract(Quantity<Q> other) {
        Quantity<Q> converted = other.to(unit);
        Real newValue = value.subtract(converted.getValue());
        return new StandardQuantity<>(newValue, unit);
    }

    @Override
    public Quantity<Q> multiply(Real scalar) {
        return new StandardQuantity<>(value.multiply(scalar), unit);
    }

    @Override
    public Quantity<Q> multiply(double scalar) {
        return multiply(Real.of(scalar));
    }

    public <R extends Quantity<R>> Quantity<?> multiply(Quantity<R> other) {
        Real newValue = value.multiply(other.getValue());
        Unit<?> newUnit = unit.multiply(other.getUnit());
        return new StandardQuantity<>(newValue, newUnit);
    }

    @Override
    public Quantity<Q> divide(Real scalar) {
        return new StandardQuantity<>(value.divide(scalar), unit);
    }

    @Override
    public Quantity<Q> divide(double scalar) {
        return divide(Real.of(scalar));
    }

    public <R extends Quantity<R>> Quantity<?> divide(Quantity<R> other) {
        Real newValue = value.divide(other.getValue());
        Unit<?> newUnit = unit.divide(other.getUnit());
        return new StandardQuantity<>(newValue, newUnit);
    }

    @Override
    public Quantity<Q> negate() {
        return new StandardQuantity<>(value.negate(), unit);
    }

    @Override
    public Quantity<Q> abs() {
        return new StandardQuantity<>(value.abs(), unit);
    }

    public Quantity<?> pow(int exponent) {
        Real newValue = value.pow(exponent);
        Unit<?> newUnit = unit.pow(exponent);
        return new StandardQuantity<>(newValue, newUnit);
    }

    public Quantity<?> sqrt() {
        Real newValue = value.sqrt();
        Unit<?> newUnit = unit.sqrt();
        return new StandardQuantity<>(newValue, newUnit);
    }

    @Override
    public int compareTo(Quantity<Q> other) {
        Quantity<Q> converted = other.to(unit);
        return value.compareTo(converted.getValue());
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Quantity))
            return false;

        @SuppressWarnings("unchecked")
        Quantity<Q> other = (Quantity<Q>) obj;

        try {
            Quantity<Q> converted = other.to(unit);
            return value.equals(converted.getValue());
        } catch (Exception e) {
            return false; // Incompatible units
        }
    }

    @Override
    public int hashCode() {
        // Hash based on value in base unit
        return value.hashCode() * 31 + unit.hashCode();
    }

    @Override
    public boolean equals(Quantity<Q> other, Real tolerance) {
        try {
            Quantity<Q> converted = other.to(unit);
            Real diff = value.subtract(converted.getValue()).abs();
            return diff.compareTo(tolerance) <= 0;
        } catch (Exception e) {
            return false; // Incompatible units
        }
    }
}

