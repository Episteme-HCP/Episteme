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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.chemistry.computational;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Molecular mechanics force field calculations.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ForceField {

    /**
     * Lennard-Jones 12-6 potential. V(r) = ε * [(σ/r)^12 - 2*(σ/r)^6]
     */
    public static Real lennardJones(Real r, Real epsilon, Real sigma) {
        Real ratio = sigma.divide(r);
        Real r6 = ratio.pow(6);
        Real r12 = r6.pow(2);
        return epsilon.multiply(r12.subtract(Real.TWO.multiply(r6)));
    }

    /** Lennard-Jones force. F(r) = 12ε/r * [(σ/r)^12 - (σ/r)^6] */
    public static Real lennardJonesForce(Real r, Real epsilon, Real sigma) {
        Real ratio = sigma.divide(r);
        Real r6 = ratio.pow(6);
        Real r12 = r6.pow(2);
        return Real.of(12).multiply(epsilon).divide(r).multiply(r12.subtract(r6));
    }

    /** Harmonic bond stretching: V(r) = 0.5 * k * (r - r0)² */
    public static Real bondStretch(Real r, Real r0, Real k) {
        Real dr = r.subtract(r0);
        return Real.of(0.5).multiply(k).multiply(dr.pow(2));
    }

    /** Harmonic angle bending: V(θ) = 0.5 * k * (θ - θ0)² */
    public static Real angleBend(Real theta, Real theta0, Real k) {
        Real dTheta = theta.subtract(theta0);
        return Real.of(0.5).multiply(k).multiply(dTheta.pow(2));
    }

    /** Torsional potential: V(φ) = V_n/2 * [1 + cos(n*φ - γ)] */
    public static Real torsion(Real phi, Real Vn, int n, Real gamma) {
        Real cosArg = Real.of(n).multiply(phi).subtract(gamma).cos();
        return Vn.divide(Real.TWO).multiply(Real.ONE.add(cosArg));
    }

    /** Coulomb interaction (kcal/mol when q in e, r in Å) */
    public static Real coulomb(Real q1, Real q2, Real r) {
        return Real.of(332.06).multiply(q1).multiply(q2).divide(r);
    }

    /** Morse potential: V(r) = D_e * [1 - exp(-a*(r-r_e))]² */
    public static Real morse(Real r, Real re, Real De, Real a) {
        Real exp = a.negate().multiply(r.subtract(re)).exp();
        return De.multiply(Real.ONE.subtract(exp).pow(2));
    }

    // --- Common parameters (Angstroms, kcal/mol) ---
    public static final Real CC_EPSILON = Real.of(0.066);
    public static final Real CC_SIGMA = Real.of(3.4);
    public static final Real OO_EPSILON = Real.of(0.152);
    public static final Real OO_SIGMA = Real.of(3.12);
    public static final Real CC_BOND_LENGTH = Real.of(1.54);
    public static final Real CC_DOUBLE_LENGTH = Real.of(1.34);
    public static final Real CH_BOND_LENGTH = Real.of(1.09);

    /**
     * Calculates total potential energy of a molecule.
     */
    public static Real calculatePotentialEnergy(org.jscience.chemistry.Molecule molecule) {
        Real totalEnergy = Real.ZERO;

        for (org.jscience.chemistry.Bond bond : molecule.getBonds()) {
            Real rMeter = bond.getLength().to(org.jscience.measure.Units.METER).getValue();
            Real rAngstrom = rMeter.multiply(Real.of(1e10));

            Real r0 = CC_BOND_LENGTH;
            if (bond.getOrder() == org.jscience.chemistry.Bond.BondOrder.DOUBLE)
                r0 = CC_DOUBLE_LENGTH;

            totalEnergy = totalEnergy.add(bondStretch(rAngstrom, r0, Real.of(300.0)));
        }

        java.util.List<org.jscience.chemistry.Atom> atoms = molecule.getAtoms();
        for (int i = 0; i < atoms.size(); i++) {
            for (int j = i + 1; j < atoms.size(); j++) {
                org.jscience.chemistry.Atom a1 = atoms.get(i);
                org.jscience.chemistry.Atom a2 = atoms.get(j);

                boolean bonded = false;
                for (org.jscience.chemistry.Bond b : molecule.getBondsFor(a1)) {
                    if (b.getOtherAtom(a1) == a2) {
                        bonded = true;
                        break;
                    }
                }
                if (bonded)
                    continue;

                Real distMeter = a1.distanceTo(a2).to(org.jscience.measure.Units.METER).getValue();
                Real distAngstrom = distMeter.multiply(Real.of(1e10));

                if (distAngstrom.doubleValue() < 0.1)
                    distAngstrom = Real.of(0.1);

                totalEnergy = totalEnergy.add(lennardJones(distAngstrom, CC_EPSILON, CC_SIGMA));
            }
        }

        return totalEnergy;
    }
}
