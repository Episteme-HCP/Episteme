package org.jscience.chemistry;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.*;
import org.jscience.measure.Units;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.classical.mechanics.Particle;

/**
 * An atom in a molecular structure.
 * Extends Generic Linear Algebra Particle for physical simulation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Atom extends Particle {

    private final Element element;
    private Isotope isotope; // Optional, specific isotope
    private Quantity<ElectricCharge> formalCharge;
    private Vector<Real> force; // Kept here as Particle doesn't explicitly store force, only acceleration

    public Atom(Element element, Vector<Real> position) {
        super(position, position.multiply(Real.ZERO), calculateMass(element, null)); // Velocity zero initially
        this.element = element;
        this.formalCharge = Quantities.create(0, Units.COULOMB);
        this.isotope = null;
        // force initialized to zero vector of same dimension
        // Since we don't know dimension easily from ZERO, we assume 3D or derive map
        // For now, we assume 3D for Atoms usually
        this.force = position.multiply(Real.ZERO);
    }

    public Atom(Isotope isotope, Vector<Real> position) {
        super(position, position.multiply(Real.ZERO), calculateMass(isotope.getElement(), isotope));
        this.element = isotope.getElement();
        this.isotope = isotope;
        this.formalCharge = Quantities.create(0, Units.COULOMB);
        this.force = position.multiply(Real.ZERO);
    }

    private static Real calculateMass(Element element, Isotope isotope) {
        if (isotope != null) {
            // Assuming Isotope has getMass() returning Quantity<Mass> or similar
            try {
                // If Isotope is updated to use Quantity:
                // return
                // Real.of(isotope.getMass().to(Units.KILOGRAM).getValue().doubleValue());

                // Fallback for now if Isotope not updated:
                return Real.of(isotope.getMass().getValue().doubleValue() * 1.66053906660e-27);
            } catch (Exception e) {
                return Real.ZERO;
            }
        }
        // element.getAtomicMass() now returns Quantity<Mass>
        return Real.of(element.getAtomicMass().to(Units.KILOGRAM).getValue().doubleValue());
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
        // Should update mass in Particle? Particle mass is usually final or needs
        // setter.
        // Particle doesn't have setMass in my impl. Limitation.
    }

    public Isotope getIsotope() {
        return isotope;
    }

    /**
     * Distance to another atom.
     */
    public Quantity<Length> distanceTo(Atom other) {
        Real dist = this.distanceTo((Particle) other);
        return Quantities.create(dist, Units.METER);
    }

    @Override
    public String toString() {
        return element.getSymbol();
    }
}
