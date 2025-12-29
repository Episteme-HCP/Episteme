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

package org.jscience.chemistry;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.measure.Units;
import org.jscience.mathematics.numbers.real.Real;

/**
 * A simple molecular dynamics engine using a spring-mass model.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularDynamics {

    private static final Real K_STRETCH = Real.of(500.0); // Spring constant (N/m) approx for standard bond
    // Note: Realistic K is much higher (~100-500 N/m or sometimes measured in
    // kcal/mol/A^2)
    // For visualization stability, we might damp or scale.

    private static final Real DAMPING = Real.of(0.98); // Friction

    /**
     * Performs a single simulation step.
     * 
     * @param molecule The molecule to simulate
     * @param dt       Time step in seconds
     */
    public static void step(Molecule molecule, Real dt) {
        clearForces(molecule);
        calculateBondForces(molecule);
        // calculateNonBondedForces(molecule); // Optional: Lennard-Jones
        integrate(molecule, dt);
    }

    /**
     * Legacy step method for double.
     */
    public static void step(Molecule molecule, double dt) {
        step(molecule, Real.of(dt));
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
            Real dist = delta.norm();

            // Equilibrium length (ideal).
            // Simplified: Use the initial bond length or estimating from radii?
            // For now, let's assume standard 1.54 Angstrom (1.54e-10 m) if not defined,
            // OR define target length based on atom radii sum.
            double r1Val = a1.getElement().getAtomicRadius().to(Units.METER).getValue().doubleValue();
            double r2Val = a2.getElement().getAtomicRadius().to(Units.METER).getValue().doubleValue();
            Real r0 = Real.of(r1Val + r2Val);

            // Element radii are usually in pm (picometers). 120 pm = 1.2e-10 m.
            // Let's assume a default if 0.
            if (r0.isZero())
                r0 = Real.of(1.5e-10);

            // Hooke's Law: F = -k * (r - r0)
            Real displacement = dist.subtract(r0);
            Real forceMag = K_STRETCH.multiply(displacement);

            // Force direction: along delta.
            // If dist > r0 (stretched), force pulls them together.
            // On a1: direction towards a2 (delta).
            // F_a1 = k * (dist - r0) * (delta / dist)
            // Use multiply(inverse) instead of divide
            Vector<Real> forceDir = delta.multiply(dist.inverse());
            Vector<Real> f1 = forceDir.multiply(forceMag);
            Vector<Real> f2 = f1.multiply(Real.ONE.multiply(Real.of(-1.0)));

            a1.addForce(f1);
            a2.addForce(f2);
        }
    }

    private static void integrate(Molecule molecule, Real dt) {
        // Symplectic Euler or Verlet

        for (Atom atom : molecule.getAtoms()) {
            // F = ma -> a = F/m
            double massKg = atom.getMass().to(Units.KILOGRAM).getValue().doubleValue();
            if (massKg == 0)
                massKg = 1.66e-27; // Safety

            Real massReal = Real.of(massKg);
            Vector<Real> accel = atom.getForce().multiply(massReal.inverse());

            // v = v + a*dt
            Vector<Real> vel = atom.getVelocity().add(accel.multiply(dt));

            // Damping
            vel = vel.multiply(DAMPING);

            atom.setVelocity(vel);

            // x = x + v*dt
            Vector<Real> pos = atom.getPosition().add(vel.multiply(dt));
            atom.setPosition(pos);
        }
    }
}
