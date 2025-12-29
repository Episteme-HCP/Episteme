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

package org.jscience.measure.converters;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.UnitConverter;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CompositeConverter implements UnitConverter {

    private final UnitConverter first;
    private final UnitConverter second;

    public CompositeConverter(UnitConverter first, UnitConverter second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Real convert(Real value) {
        return second.convert(first.convert(value));
    }

    @Override
    public UnitConverter inverse() {
        return new CompositeConverter(second.inverse(), first.inverse());
    }

    @Override
    public UnitConverter concatenate(UnitConverter other) {
        return new CompositeConverter(this, other);
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
        Real firstDerivative = first.derivative(value);
        Real convertedValue = first.convert(value);
        Real secondDerivative = second.derivative(convertedValue);
        return firstDerivative.multiply(secondDerivative);
    }
}
