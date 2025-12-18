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
package org.jscience.measure.converters;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.UnitConverter;

/**
 * Affine converter: y = ax + b
 * Used for temperature conversions (Celsius, Fahrenheit).
 */
public class AddConverter implements UnitConverter {
    private final Real offset;

    public AddConverter(Real offset) {
        this.offset = offset;
    }

    @Override
    public Real convert(Real value) {
        return value.add(offset);
    }

    @Override
    public UnitConverter inverse() {
        return new AddConverter(offset.negate());
    }

    @Override
    public Real derivative(Real value) {
        return Real.ONE; // d(x+b)/dx = 1
    }

    @Override
    public UnitConverter concatenate(UnitConverter converter) {
        if (converter instanceof AddConverter) {
            return new AddConverter(offset.add(((AddConverter) converter).offset));
        }
        // Return a compound converter for non-additive converters
        return new CompoundAddConverter(this, converter);
    }

    // Simple compound converter for Add + other
    private static class CompoundAddConverter implements UnitConverter {
        private final AddConverter first;
        private final UnitConverter second;

        CompoundAddConverter(AddConverter first, UnitConverter second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public Real convert(Real value) {
            return second.convert(first.convert(value));
        }

        @Override
        public UnitConverter inverse() {
            return new CompoundAddConverter((AddConverter) first.inverse(), second.inverse());
        }

        @Override
        public UnitConverter concatenate(UnitConverter other) {
            return new CompoundAddConverter(first, second.concatenate(other));
        }

        @Override
        public Real derivative(Real value) {
            return first.derivative(value).multiply(second.derivative(first.convert(value)));
        }

        @Override
        public boolean isIdentity() {
            return first.isIdentity() && second.isIdentity();
        }

        @Override
        public boolean isLinear() {
            return first.isLinear() && second.isLinear();
        }
    }

    @Override
    public boolean isLinear() {
        return offset.compareTo(Real.ZERO) == 0;
    }

    @Override
    public boolean isIdentity() {
        return offset.compareTo(Real.ZERO) == 0;
    }

    @Override
    public String toString() {
        return offset.compareTo(Real.ZERO) >= 0 ? "+" + offset : offset.toString();
    }
}


