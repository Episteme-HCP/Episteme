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
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Units;
import org.jscience.technical.backend.algorithms.MolecularDynamicsProvider;
import org.jscience.technical.backend.algorithms.MulticoreMolecularDynamicsProvider;
import java.util.Arrays;

/**
 * A simple molecular dynamics engine using a spring-mass model.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularDynamics {

    private static MolecularDynamicsProvider provider = new MulticoreMolecularDynamicsProvider();

    public static void setProvider(MolecularDynamicsProvider p) {
        provider = p;
    }

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
        // 1. Pack data
        java.util.List<Atom> atoms = molecule.getAtoms();
        int numAtoms = atoms.size();
        Real[] positions = new Real[numAtoms * 3];
        Real[] velocities = new Real[numAtoms * 3];
        Real[] forces = new Real[numAtoms * 3];
        Real[] masses = new Real[numAtoms];

        for (int i = 0; i < numAtoms; i++) {
            Atom a = atoms.get(i);
            Vector<Real> pos = a.getPosition();
            Vector<Real> vel = a.getVelocity();

            positions[i * 3] = pos.get(0);
            positions[i * 3 + 1] = pos.get(1);
            positions[i * 3 + 2] = pos.get(2);

            velocities[i * 3] = vel.get(0);
            velocities[i * 3 + 1] = vel.get(1);
            velocities[i * 3 + 2] = vel.get(2);

            forces[i * 3] = Real.ZERO;
            forces[i * 3 + 1] = Real.ZERO;
            forces[i * 3 + 2] = Real.ZERO;

            masses[i] = a.getMass().to(Units.KILOGRAM).getValue();
        }

        // 2. Bonds
        java.util.List<Bond> bonds = molecule.getBonds();
        int numBonds = bonds.size();
        int[] bondIndices = new int[numBonds * 2];
        Real[] bondLengths = new Real[numBonds];
        Real[] bondConstants = new Real[numBonds];

        for (int i = 0; i < numBonds; i++) {
            Bond b = bonds.get(i);
            int idx1 = atoms.indexOf(b.getAtom1());
            int idx2 = atoms.indexOf(b.getAtom2());
            bondIndices[i * 2] = idx1;
            bondIndices[i * 2 + 1] = idx2;

            // Calc r0 logic
            double r1Val = b.getAtom1().getElement().getAtomicRadius().to(Units.METER).getValue().doubleValue();
            double r2Val = b.getAtom2().getElement().getAtomicRadius().to(Units.METER).getValue().doubleValue();
            double r0Val = r1Val + r2Val;
            if (r0Val == 0)
                r0Val = 1.5e-10;
            bondLengths[i] = Real.of(r0Val);
            bondConstants[i] = K_STRETCH;
        }

        // 3. Delegate to Provider
        provider.calculateBondForces(positions, forces, bondIndices, bondLengths, bondConstants);
        provider.integrate(positions, velocities, forces, masses, dt, DAMPING);

        // 4. Unpack
        for (int i = 0; i < numAtoms; i++) {
            Atom a = atoms.get(i);
            a.setPosition(DenseVector.of(Arrays.asList(
                    positions[i * 3], positions[i * 3 + 1], positions[i * 3 + 2]), Real.ZERO));

            a.setVelocity(DenseVector.of(Arrays.asList(
                    velocities[i * 3], velocities[i * 3 + 1], velocities[i * 3 + 2]), Real.ZERO));

            // Forces might be useful to update too for visualization
            a.clearForce();
            a.addForce(DenseVector.of(Arrays.asList(
                    forces[i * 3], forces[i * 3 + 1], forces[i * 3 + 2]), Real.ZERO));
        }
    }

    /**
     * Legacy step method for double.
     */
    public static void step(Molecule molecule, double dt) {
        step(molecule, Real.of(dt));
    }
}
