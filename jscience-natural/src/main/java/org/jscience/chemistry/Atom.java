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

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.classical.mechanics.Particle;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.ElectricCharge;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * An atom in a molecular structure.
 * Extends Generic Linear Algebra Particle for physical simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Atom extends Particle {

    private final Element element;
    private Isotope isotope;
    private Quantity<ElectricCharge> formalCharge;
    private Vector<Real> force;

    public Atom(Element element, Vector<Real> position) {
        super(position, position.multiply(Real.ZERO), calculateMass(element, null));
        this.element = element;
        this.formalCharge = Quantities.create(0, Units.COULOMB);
        this.isotope = null;
        this.force = position.multiply(Real.ZERO);
    }

    public Atom(Isotope isotope, Vector<Real> position) {
        super(position, position.multiply(Real.ZERO), calculateMass(isotope.getElement(), isotope));
        this.element = isotope.getElement();
        this.isotope = isotope;
        this.formalCharge = Quantities.create(0, Units.COULOMB);
        this.force = position.multiply(Real.ZERO);
    }

    private static Quantity<Mass> calculateMass(Element element, Isotope isotope) {
        if (isotope != null) {
            return isotope.getMass();
        }
        return element.getAtomicMass();
    }

    // --- Properties ---

    public Element getElement() {
        return element;
    }

    public Vector<Real> getForce() {
        return force;
    }

    public void setForce(Vector<Real> force) {
        this.force = force;
    }

    public void addForce(Vector<Real> f) {
        this.force = this.force.add(f);
    }

    public void clearForce() {
        if (force != null)
            this.force = force.multiply(Real.ZERO);
    }

    public Quantity<ElectricCharge> getFormalCharge() {
        return formalCharge;
    }

    public void setFormalCharge(Quantity<ElectricCharge> charge) {
        this.formalCharge = charge;
    }

    public void setIsotope(Isotope isotope) {
        if (isotope != null && !isotope.getElement().equals(this.element)) {
            throw new IllegalArgumentException("Isotope element mismatch");
        }
        this.isotope = isotope;
    }

    public Isotope getIsotope() {
        return isotope;
    }

    /**
     * Distance to another atom.
     */
    public Quantity<Length> distanceTo(Atom other) {
        Real dist = this.distanceTo((Particle) other);
        return Quantities.create(dist.doubleValue(), Units.METER);
    }

    @Override
    public String toString() {
        return element.getSymbol();
    }
}


