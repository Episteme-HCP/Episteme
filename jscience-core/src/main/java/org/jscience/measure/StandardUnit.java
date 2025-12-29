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
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StandardUnit<Q extends Quantity<Q>> implements Unit<Q> {

    private final String symbol;
    private final String name;
    private final Dimension dimension;
    private final UnitConverter toSystemUnit;
    private final boolean isBaseUnit;

    /**
     * Creates a base unit (defines the dimension).
     */
    public StandardUnit(String symbol, String name, Dimension dimension) {
        this.symbol = symbol;
        this.name = name;
        this.dimension = dimension;
        this.toSystemUnit = UnitConverter.identity();
        this.isBaseUnit = true;
    }

    /**
     * Creates a derived unit from a base unit with a converter.
     */
    StandardUnit(String symbol, String name, Dimension dimension,
            UnitConverter toSystemUnit) {
        this.symbol = symbol;
        this.name = name;
        this.dimension = dimension;
        this.toSystemUnit = toSystemUnit;
        this.isBaseUnit = false;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public UnitConverter getConverterTo(Unit<Q> targetUnit) {
        if (!isCompatible(targetUnit)) {
            throw new IllegalArgumentException(
                    "Incompatible units: " + this + " and " + targetUnit);
        }

        // this -> system -> target
        UnitConverter toSystem = this.toSystemUnit;
        UnitConverter fromSystem = ((StandardUnit<Q>) targetUnit).toSystemUnit.inverse();

        return toSystem.concatenate(fromSystem);
    }

    @Override
    public Unit<Q> multiply(double factor) {
        org.jscience.measure.converters.MultiplyConverter converter = new org.jscience.measure.converters.MultiplyConverter(
                Real.of(factor));
        UnitConverter combined = this.toSystemUnit.concatenate(converter);

        String newSymbol = factor + "×" + symbol;
        return new StandardUnit<>(newSymbol, name, dimension, combined);
    }

    @Override
    public Unit<Q> divide(double divisor) {
        return multiply(1.0 / divisor);
    }

    @Override
    public Unit<?> multiply(Unit<?> other) {
        Dimension newDim = dimension.multiply(other.getDimension());
        String newSymbol = symbol + "\u22c5" + other.getSymbol();
        String newName = name + " " + ((StandardUnit<?>) other).getName();

        return new StandardUnit<>(newSymbol, newName, newDim, UnitConverter.identity());
    }

    @Override
    public Unit<?> divide(Unit<?> other) {
        Dimension newDim = dimension.divide(other.getDimension());
        String newSymbol = symbol + "/" + other.getSymbol();
        String newName = name + " per " + ((StandardUnit<?>) other).getName();

        return new StandardUnit<>(newSymbol, newName, newDim, UnitConverter.identity());
    }

    /**
     * Returns a unit equal to this unit shifted by the specified offset.
     * <p>
     * For example, {@code KELVIN.add(273.15)} gives Celsius.
     * </p>
     * 
     * @param offset the offset to add
     * @return the shifted unit
     */
    public Unit<Q> add(double offset) {
        org.jscience.measure.converters.AddConverter converter = new org.jscience.measure.converters.AddConverter(
                Real.of(offset));
        UnitConverter combined = this.toSystemUnit.concatenate(converter);

        String sign = offset >= 0 ? "+" : "";
        String newSymbol = symbol + sign + offset;
        return new StandardUnit<>(newSymbol, name, dimension, combined);
    }

    public Unit<?> pow(int exponent) {
        Dimension newDim = dimension.pow(exponent);
        String newSymbol = symbol + (exponent == 2 ? "²" : exponent == 3 ? "³" : "^" + exponent);

        return new StandardUnit<>(newSymbol, name, newDim, UnitConverter.identity());
    }

    public Unit<?> sqrt() {
        Dimension newDim = dimension.sqrt();
        String newSymbol = "√" + symbol;

        return new StandardUnit<>(newSymbol, name, newDim, UnitConverter.identity());
    }

    @Override
    public boolean isBaseUnit() {
        return isBaseUnit;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof StandardUnit))
            return false;

        StandardUnit<?> other = (StandardUnit<?>) obj;
        return symbol.equals(other.symbol) &&
                dimension.equals(other.dimension);
    }

    @Override
    public int hashCode() {
        return 31 * symbol.hashCode() + dimension.hashCode();
    }

    // Unused CompoundConverter removed

}