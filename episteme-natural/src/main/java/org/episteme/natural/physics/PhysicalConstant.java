/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Unit;


/**
 * Fundamental physical constants.
 * Ported to use Real for high-precision scientific computing.
 * Values based on CODATA 2018.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PhysicalConstant<Q extends Quantity<Q>> {

    private final String name;
    private final Real value;
    @SuppressWarnings("rawtypes")
    private final Unit unit;
    private final String source;
    private final Real uncertainty;

    public PhysicalConstant(String name, double value, @SuppressWarnings("rawtypes") Unit unit, Object source, double uncertainty) {
        this.name = name;
        this.value = Real.of(value);
        this.unit = unit;
        this.source = String.valueOf(source);
        this.uncertainty = Real.of(uncertainty);
    }

    public String getName() {
        return name;
    }

    public Real getValue() {
        return value;
    }

    @SuppressWarnings("rawtypes")
    public Unit getUnit() {
        return unit;
    }

    public String getSource() {
        return source;
    }

    public Real getUncertainty() {
        return uncertainty;
    }

    @SuppressWarnings("unchecked")
    public Quantity<Q> toQuantity() {
        return (Quantity<Q>) Quantities.create(value, unit);
    }
}

