package org.jscience.chemistry;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Molecule;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.measure.Units;
import org.jscience.mathematics.numbers.real.Real;

/**
 * A simple molecular dynamics engine using a spring-mass model.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class MolecularDynamics {

    private static final double K_STRETCH = 500.0; // Spring constant (N/m) approx for standard bond
    // Note: Realistic K is much higher (~100-500 N/m or sometimes measured in
    // kcal/mol/A^2)
    // For visualization stability, we might damp or scale.

    private static final double DAMPING = 0.98; // Friction

    /**
     * Performs a single simulation step.
     * 
     * @param molecule The molecule to simulate
     * @param dt       Time step in seconds
     */
    public static void step(Molecule molecule, double dt) {
        clearForces(molecule);
        calculateBondForces(molecule);
        // calculateNonBondedForces(molecule); // Optional: Lennard-Jones
        integrate(molecule, dt);
    }

    private static void clearForces(Molecule molecule) {
        for (Atom atom : molecule.getAtoms()) {
            atom.clearForce();
        }
    }

    private static void calculateBondForces(Molecule molecule) {
        for (Bond bond : molecule.getBonds()) {
            Atom a1 = bond.getAtom1();
            Atom a2 = bond.getAtom2();

            Vector<Real> p1 = a1.getPosition();
            Vector<Real> p2 = a2.getPosition();

            // Distance vector
            Vector<Real> delta = p2.subtract(p1);
            double dist = delta.norm().doubleValue();

            // Equilibrium length (ideal).
            // Simplified: Use the initial bond length or estimating from radii?
            // For now, let's assume standard 1.54 Angstrom (1.54e-10 m) if not defined,
            // OR define target length based on atom radii sum.
            double r1 = a1.getElement().getAtomicRadius().to(Units.METER).getValue().doubleValue();
            double r2 = a2.getElement().getAtomicRadius().to(Units.METER).getValue().doubleValue();
            double r0 = r1 + r2;
            // Element radii are usually in pm (picometers). 120 pm = 1.2e-10 m.
            // Let's assume a default if 0.
            if (r0 == 0)
                r0 = 1.5e-10;

            // Hooke's Law: F = -k * (r - r0)
            double displacement = dist - r0;
            double forceMag = K_STRETCH * displacement;

            // Force direction: along delta.
            // If dist > r0 (stretched), force pulls them together.
            // On a1: direction towards a2 (delta).
            // F_a1 = k * (dist - r0) * (delta / dist)
            // Use multiply(inverse) instead of divide
            Vector<Real> forceDir = delta.multiply(org.jscience.mathematics.numbers.real.Real.of(dist).inverse());
            Vector<Real> f1 = forceDir.multiply(org.jscience.mathematics.numbers.real.Real.of(forceMag));
            Vector<Real> f2 = f1.multiply(org.jscience.mathematics.numbers.real.Real.of(-1.0));

            a1.addForce(f1);
            a2.addForce(f2);
        }
    }

    private static void integrate(Molecule molecule, double dt) {
        // Symplectic Euler or Verlet
        org.jscience.mathematics.numbers.real.Real dtReal = org.jscience.mathematics.numbers.real.Real.of(dt);
        org.jscience.mathematics.numbers.real.Real dampingReal = org.jscience.mathematics.numbers.real.Real.of(DAMPING);

        for (Atom atom : molecule.getAtoms()) {
            // F = ma -> a = F/m
            double massKg = atom.getMass().to(Units.KILOGRAM).getValue().doubleValue();
            if (massKg == 0)
                massKg = 1.66e-27; // Safety

            org.jscience.mathematics.numbers.real.Real massReal = org.jscience.mathematics.numbers.real.Real.of(massKg);
            Vector<Real> accel = atom.getForce().multiply(massReal.inverse());

            // v = v + a*dt
            Vector<Real> vel = atom.getVelocity().add(accel.multiply(dtReal));

            // Damping
            vel = vel.multiply(dampingReal);

            atom.setVelocity(vel);

            // x = x + v*dt
            Vector<Real> pos = atom.getPosition().add(vel.multiply(dtReal));
            atom.setPosition(pos);
        }
    }
}
