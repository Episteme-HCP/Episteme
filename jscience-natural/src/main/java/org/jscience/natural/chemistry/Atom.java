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

package org.jscience.natural.chemistry;

import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.physics.classical.mechanics.Particle;

import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.ElectricCharge;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.quantity.Mass;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;

/**
 * An atom in a molecular structure.
 * Extends Generic Linear Algebra Particle for physical simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Atom extends Particle implements ComprehensiveIdentification {

    @Id
    private final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private final Element element;
    @Attribute
    private Isotope isotope;
    @Attribute
    private Quantity<ElectricCharge> formalCharge;
    @Attribute
    private Vector<Real> force;

    public Atom(Element element, Vector<Real> position) {
        super(position, position.multiply(Real.ZERO), calculateMass(element, null));
        this.id = new SimpleIdentification("ATOM:" + UUID.randomUUID());
        this.element = element;
        this.formalCharge = Quantities.create(0, Units.COULOMB);
        this.isotope = null;
        this.force = position.multiply(Real.ZERO);
    }

    public Atom(Isotope isotope, Vector<Real> position) {
        super(position, position.multiply(Real.ZERO), calculateMass(isotope.getElement(), isotope));
        this.id = new SimpleIdentification("ATOM:" + UUID.randomUUID());
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

    @Override
    public Identification getId() {
        return id;
    }

    /**
     * Returns the x-coordinate of this atom.
     * @return the x-coordinate
     */
    public double getX() {
        return getPosition().get(0).doubleValue();
    }

    /**
     * Returns the y-coordinate of this atom.
     * @return the y-coordinate
     */
    public double getY() {
        return getPosition().dimension() > 1 ? getPosition().get(1).doubleValue() : 0.0;
    }

    /**
     * Returns the z-coordinate of this atom.
     * @return the z-coordinate
     */
    public double getZ() {
        return getPosition().dimension() > 2 ? getPosition().get(2).doubleValue() : 0.0;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
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




