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

package org.jscience.chemistry.polymer;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Polymer chemistry calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Polymer {

    private final String name;
    private final String repeatUnit;
    private final Real repeatUnitMass; // g/mol
    private final Real Mn; // Number-average molecular weight
    private final Real Mw; // Weight-average molecular weight

    public Polymer(String name, String repeatUnit, Real repeatUnitMass,
            Real Mn, Real Mw) {
        this.name = name;
        this.repeatUnit = repeatUnit;
        this.repeatUnitMass = repeatUnitMass;
        this.Mn = Mn;
        this.Mw = Mw;
    }

    /**
     * Creates polymer from degree of polymerization.
     */
    public static Polymer fromDP(String name, String repeatUnit,
            Real repeatUnitMass, int DP) {
        Real M = repeatUnitMass.multiply(Real.of(DP));
        return new Polymer(name, repeatUnit, repeatUnitMass, M, M);
    }

    /**
     * Polydispersity index: PDI = Mw/Mn
     * PDI = 1 for monodisperse, >1 for polydisperse
     */
    public Real polydispersityIndex() {
        return Mw.divide(Mn);
    }

    /**
     * Number-average degree of polymerization.
     * DPn = Mn / M0
     */
    public Real degreeOfPolymerization() {
        return Mn.divide(repeatUnitMass);
    }

    /**
     * Mark-Houwink equation: intrinsic viscosity.
     * [η] = K · M^a
     */
    public static Real intrinsicViscosity(Real M, Real K, Real a) {
        return K.multiply(M.pow(a));
    }

    /**
     * End-to-end distance for ideal chain.
     * <R²>^(1/2) = l·sqrt(n) for freely-jointed chain
     */
    public static Real endToEndDistance(int n, Real l) {
        return l.multiply(Real.of(n).sqrt());
    }

    /**
     * Radius of gyration for ideal chain.
     * Rg = <R²>^(1/2) / sqrt(6)
     */
    public static Real radiusOfGyration(int n, Real l) {
        return endToEndDistance(n, l).divide(Real.of(6).sqrt());
    }

    /**
     * Flory-Huggins interaction parameter estimation.
     * χ = V_s(δ_p - δ_s)² / RT
     */
    public static Real floryHugginsParameter(Real Vs, Real deltaP,
            Real deltaS, Real T) {
        Real R = Real.of(1.987); // cal/(mol·K)
        Real diff = deltaP.subtract(deltaS);
        return Vs.multiply(diff.pow(2)).divide(R.multiply(T));
    }

    /**
     * Glass transition temperature estimation (Fox equation for copolymers).
     * 1/Tg = w1/Tg1 + w2/Tg2
     */
    public static Real foxEquation(Real w1, Real Tg1, Real Tg2) {
        Real w2 = Real.ONE.subtract(w1);
        return Real.ONE.divide(w1.divide(Tg1).add(w2.divide(Tg2)));
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getRepeatUnit() {
        return repeatUnit;
    }

    public Real getRepeatUnitMass() {
        return repeatUnitMass;
    }

    public Real getMn() {
        return Mn;
    }

    public Real getMw() {
        return Mw;
    }

    /**
     * Generates a representative single-chain Molecule with the specified Degree of
     * Polymerization.
     */
    public org.jscience.chemistry.Molecule generateMolecule(int dp) {
        org.jscience.chemistry.Molecule mol = new org.jscience.chemistry.Molecule(name + "_DP" + dp);

        org.jscience.chemistry.Element carbon = org.jscience.chemistry.PeriodicTable.getElement("Carbon");

        org.jscience.chemistry.Atom prev = null;
        for (int i = 0; i < dp; i++) {
            double x = i * 1.5;
            double y = (i % 2 == 0) ? 0 : 1.0;
            double z = 0;

            double scale = 1e-10;

            org.jscience.chemistry.Atom atom = new org.jscience.chemistry.Atom(
                    carbon,
                    new org.jscience.mathematics.geometry.Vector3D(x * scale, y * scale, z * scale));

            mol.addAtom(atom);

            if (prev != null) {
                mol.addBond(new org.jscience.chemistry.Bond(prev, atom, org.jscience.chemistry.Bond.BondOrder.SINGLE));
            }
            prev = atom;
        }

        return mol;
    }

    // --- Common polymers (repeat unit mass in g/mol) ---

    public static final Real PE_REPEAT_MASS = Real.of(28.05);
    public static final Real PP_REPEAT_MASS = Real.of(42.08);
    public static final Real PS_REPEAT_MASS = Real.of(104.15);
    public static final Real PVC_REPEAT_MASS = Real.of(62.50);
    public static final Real PMMA_REPEAT_MASS = Real.of(100.12);
    public static final Real NYLON66_REPEAT_MASS = Real.of(226.32);
}
