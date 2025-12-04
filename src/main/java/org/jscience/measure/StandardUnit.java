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
 * Standard implementation of {@link Unit}.
 * <p>
 * This implementation supports unit transformations, conversions, and
 * dimensional analysis. Units can be combined through multiplication
 * and division to form derived units.
 * </p>
 * 
 * @param <Q> the quantity type
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
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
    StandardUnit(String symbol, String name, Dimension dimension) {
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
        LinearConverter converter = new LinearConverter(factor);
        UnitConverter combined = this.toSystemUnit.concatenate(converter);

        String newSymbol = factor + "×" + symbol;
        return new StandardUnit<>(newSymbol, name, dimension, combined);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Quantity<R>> Unit<?> multiply(Unit<R> other) {
        Dimension newDim = dimension.multiply(other.getDimension());
        String newSymbol = symbol + "⋅" + other.getSymbol();
        String newName = name + " " + other.getName();

        // Converter composition for product units
        return new StandardUnit(newSymbol, newName, newDim, UnitConverter.identity());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Quantity<R>> Unit<?> divide(Unit<R> other) {
        Dimension newDim = dimension.divide(other.getDimension());
        String newSymbol = symbol + "/" + other.getSymbol();
        String newName = name + " per " + other.getName();

        return new StandardUnit(newSymbol, newName, newDim, UnitConverter.identity());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Unit<?> pow(int exponent) {
        Dimension newDim = dimension.pow(exponent);
        String newSymbol = symbol + (exponent == 2 ? "²" : exponent == 3 ? "³" : "^" + exponent);

        return new StandardUnit(newSymbol, name, newDim, UnitConverter.identity());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Unit<?> sqrt() {
        Dimension newDim = dimension.sqrt();
        String newSymbol = "√" + symbol;

        return new StandardUnit(newSymbol, name, newDim, UnitConverter.identity());
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

    /**
     * Simple linear converter: y = x × scale
     */
    private static class LinearConverter implements UnitConverter {
        private final double scale;

        LinearConverter(double scale) {
            this.scale = scale;
        }

        @Override
        public Real convert(Real value) {
            return value.multiply(Real.of(scale));
        }

        @Override
        public UnitConverter inverse() {
            return new LinearConverter(1.0 / scale);
        }

        @Override
        public UnitConverter concatenate(UnitConverter other) {
            if (other instanceof LinearConverter) {
                double newScale = scale * ((LinearConverter) other).scale;
                return new LinearConverter(newScale);
            }
            return new CompoundConverter(this, other);
        }

        @Override
        public boolean isIdentity() {
            return Math.abs(scale - 1.0) < 1e-10;
        }

        @Override
        public boolean isLinear() {
            return true;
        }

        @Override
        public Real derivative(Real value) {
            return Real.of(scale);
        }

        @Override
        public String toString() {
            return "×" + scale;
        }
    }

    /**
     * Compound converter: applies two converters in sequence.
     */
    private static class CompoundConverter implements UnitConverter {
        private final UnitConverter first;
        private final UnitConverter second;

        CompoundConverter(UnitConverter first, UnitConverter second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public Real convert(Real value) {
            return second.convert(first.convert(value));
        }

        @Override
        public UnitConverter inverse() {
            return new CompoundConverter(second.inverse(), first.inverse());
        }

        @Override
        public UnitConverter concatenate(UnitConverter other) {
            return new CompoundConverter(this, other);
        }

        @Override
        public boolean isIdentity() {
            return first.isIdentity() && second.isIdentity();
        }

        @Override
        public boolean isLinear() {
            return first.isLinear() && second.isLinear();
        }

        @Override
        public Real derivative(Real value) {
            // Chain rule: (f ∘ g)' = f'(g(x)) × g'(x)
            Real intermediateValue = first.convert(value);
            Real firstDeriv = first.derivative(value);
            Real secondDeriv = second.derivative(intermediateValue);
            return firstDeriv.multiply(secondDeriv);
        }

        @Override
        public String toString() {
            return first + " then " + second;
        }
    }
}
