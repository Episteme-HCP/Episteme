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

package org.jscience.chemistry;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.Units;

import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an isotope of a chemical element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Isotope implements Serializable {
    private static final long serialVersionUID = 2L;

    @Attribute
    private final Element element;
    @Attribute
    private final String symbol;
    @Attribute
    private final int massNumber;
    @Attribute
    private final Real atomicMass; // in u
    @Attribute
    private final Real abundance; // relative abundance (0 to 1)

    public Isotope(Element element, int massNumber, Real atomicMass, Real abundance) {
        this.element = Objects.requireNonNull(element);
        this.symbol = element.getSymbol() + "-" + massNumber;
        this.massNumber = massNumber;
        this.atomicMass = atomicMass;
        this.abundance = abundance;
    }

    public Isotope(Element element, int massNumber, double atomicMassValue, double abundanceValue) {
        this(element, massNumber, Real.of(atomicMassValue), Real.of(abundanceValue));
    }

    public Isotope(String symbol, int massNumber, Real atomicMass, Real abundance) {
        // Deprecated or legacy constructor - infer element? 
        // For now, minimal support or throw error if element strictness required.
        // We'll require Element.
        throw new UnsupportedOperationException("Isotope requires Element reference");
    }

    public Element getElement() {
        return element;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getMassNumber() {
        return massNumber;
    }

    public Real getAtomicMass() {
        return atomicMass;
    }

    public Quantity<Mass> getMass() {
        return org.jscience.measure.Quantities.create(atomicMass, Units.UNIFIED_ATOMIC_MASS);
    }

    public Real getAbundance() {
        return abundance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Isotope isotope))
            return false;
        return massNumber == isotope.massNumber && Objects.equals(symbol, isotope.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, massNumber);
    }

    @Override
    public String toString() {
        return String.format("%d%s (Mass: %s u, Abundance: %.2f%%)",
                massNumber, symbol, atomicMass, abundance.multiply(Real.of(100)).doubleValue());
    }
}
