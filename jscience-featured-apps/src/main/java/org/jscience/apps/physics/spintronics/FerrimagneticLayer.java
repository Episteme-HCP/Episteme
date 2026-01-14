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

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Ferrimagnetic two-sublattice dynamics.
 * <p>
 * Models materials like GdFeCo and CoGd where two antiferromagnetically 
 * coupled sublattices (rare-earth and transition metal) have different
 * moments and gyromagnetic ratios.
 * </p>
 * 
 * <h3>Physics</h3>
 * <p>
 * Coupled LLG equations:
 * $$\frac{d\mathbf{m}_A}{dt} = -\gamma_A \mathbf{m}_A \times \mathbf{H}_{eff,A} 
 *   + \alpha_A \mathbf{m}_A \times \frac{d\mathbf{m}_A}{dt} + J_{ex} \mathbf{m}_A \times \mathbf{m}_B$$
 * $$\frac{d\mathbf{m}_B}{dt} = -\gamma_B \mathbf{m}_B \times \mathbf{H}_{eff,B} 
 *   + \alpha_B \mathbf{m}_B \times \frac{d\mathbf{m}_B}{dt} + J_{ex} \mathbf{m}_B \times \mathbf{m}_A$$
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Radu, I. et al.</b> (2011). "Transient ferromagnetic-like state mediating ultrafast reversal of antiferromagnetically coupled spins". 
 *     <i>Nature</i>, 472, 205-208. 
 *     <a href="https://doi.org/10.1038/nature09901">DOI: 10.1038/nature09901</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class FerrimagneticLayer {

    private final String name;
    private Real[] mA; // Sublattice A (e.g., Fe/Co) magnetization
    private Real[] mB; // Sublattice B (e.g., Gd) magnetization
    
    private final Real msA; // Saturation magnetization sublattice A
    private final Real msB; // Saturation magnetization sublattice B
    private final Real gammaA; // Gyromagnetic ratio A (rad/s/T)
    private final Real gammaB; // Gyromagnetic ratio B
    private final Real alphaA; // Damping A
    private final Real alphaB; // Damping B
    private final Real jExchange; // Inter-sublattice exchange (J/m²)
    private final Real thickness;

    public FerrimagneticLayer(String name, Real msA, Real msB, Real gammaA, Real gammaB,
                               Real alphaA, Real alphaB, Real jEx, Real thickness) {
        this.name = name;
        this.msA = msA;
        this.msB = msB;
        this.gammaA = gammaA;
        this.gammaB = gammaB;
        this.alphaA = alphaA;
        this.alphaB = alphaB;
        this.jExchange = jEx;
        this.thickness = thickness;

        // Initialize antiparallel
        this.mA = new Real[]{Real.ZERO, Real.ZERO, Real.ONE};
        this.mB = new Real[]{Real.ZERO, Real.ZERO, Real.ONE.negate()};
    }

    /**
     * Gets the net magnetization (M_net = M_A - M_B for antiferromagnetic coupling).
     */
    public Real[] getNetMagnetization() {
        return new Real[]{
            mA[0].multiply(msA).subtract(mB[0].multiply(msB)),
            mA[1].multiply(msA).subtract(mB[1].multiply(msB)),
            mA[2].multiply(msA).subtract(mB[2].multiply(msB))
        };
    }

    /**
     * Gets the net angular momentum.
     */
    public Real[] getNetAngularMomentum() {
        // L = M/γ
        return new Real[]{
            mA[0].multiply(msA).divide(gammaA).subtract(mB[0].multiply(msB).divide(gammaB)),
            mA[1].multiply(msA).divide(gammaA).subtract(mB[1].multiply(msB).divide(gammaB)),
            mA[2].multiply(msA).divide(gammaA).subtract(mB[2].multiply(msB).divide(gammaB))
        };
    }

    /**
     * Checks if system is at angular momentum compensation.
     * At this point, ultrafast dynamics occur.
     */
    public boolean isAtAngularCompensation() {
        Real[] L = getNetAngularMomentum();
        Real Lmag = L[0].pow(2).add(L[1].pow(2)).add(L[2].pow(2)).sqrt();
        return Lmag.doubleValue() < 1e-6 * msA.doubleValue();
    }

    /**
     * Checks if system is at magnetization compensation.
     */
    public boolean isAtMagnetizationCompensation() {
        Real[] M = getNetMagnetization();
        Real Mmag = M[0].pow(2).add(M[1].pow(2)).add(M[2].pow(2)).sqrt();
        return Mmag.doubleValue() < 1e-6 * msA.doubleValue();
    }

    /**
     * Performs one time step of coupled LLG dynamics.
     */
    public void step(Real[] hExtA, Real[] hExtB, Real dt) {
        // Exchange fields from inter-sublattice coupling
        Real mu0 = Real.of(1.2566370614e-6);
        Real hexFactor = jExchange.multiply(Real.TWO).divide(mu0.multiply(thickness));

        Real[] hExA = scale(mB, hexFactor.negate().divide(msA)); // Field on A from B
        Real[] hExB = scale(mA, hexFactor.negate().divide(msB)); // Field on B from A

        // Total effective fields
        Real[] hEffA = add(hExtA, hExA);
        Real[] hEffB = add(hExtB, hExB);

        // Solve LLG for each sublattice
        mA = solveLLG(mA, hEffA, gammaA, alphaA, dt);
        mB = solveLLG(mB, hEffB, gammaB, alphaB, dt);
    }

    private Real[] solveLLG(Real[] m, Real[] hEff, Real gamma, Real alpha, Real dt) {
        Real[] prec = crossProduct(m, hEff);
        Real[] damp = crossProduct(m, prec);
        Real factor = gamma.divide(Real.ONE.add(alpha.pow(2))).negate();

        Real[] dm = new Real[]{
            factor.multiply(prec[0].add(alpha.multiply(damp[0]))),
            factor.multiply(prec[1].add(alpha.multiply(damp[1]))),
            factor.multiply(prec[2].add(alpha.multiply(damp[2])))
        };

        Real mx = m[0].add(dm[0].multiply(dt));
        Real my = m[1].add(dm[1].multiply(dt));
        Real mz = m[2].add(dm[2].multiply(dt));
        Real norm = mx.pow(2).add(my.pow(2)).add(mz.pow(2)).sqrt();
        return new Real[]{mx.divide(norm), my.divide(norm), mz.divide(norm)};
    }

    private static Real[] crossProduct(Real[] a, Real[] b) {
        return new Real[]{
            a[1].multiply(b[2]).subtract(a[2].multiply(b[1])),
            a[2].multiply(b[0]).subtract(a[0].multiply(b[2])),
            a[0].multiply(b[1]).subtract(a[1].multiply(b[0]))
        };
    }

    private static Real[] scale(Real[] v, Real s) {
        return new Real[]{v[0].multiply(s), v[1].multiply(s), v[2].multiply(s)};
    }

    private static Real[] add(Real[] a, Real[] b) {
        return new Real[]{a[0].add(b[0]), a[1].add(b[1]), a[2].add(b[2])};
    }

    // Getters and setters
    public Real[] getMA() { return mA; }
    public Real[] getMB() { return mB; }
    public void setMA(Real mx, Real my, Real mz) { mA = new Real[]{mx, my, mz}; }
    public void setMB(Real mx, Real my, Real mz) { mB = new Real[]{mx, my, mz}; }
    public String getName() { return name; }
    public Real getThickness() { return thickness; }

    // Preset materials
    public static FerrimagneticLayer createGdFeCo() {
        // Gd25Fe66Co9 near compensation
        return new FerrimagneticLayer("GdFeCo",
            Real.of(8e5),   // Ms_FeCo
            Real.of(6e5),   // Ms_Gd
            Real.of(1.76e11), // γ_FeCo
            Real.of(1.76e11 * 0.9), // γ_Gd (slightly different g-factor)
            Real.of(0.01),  // α_FeCo
            Real.of(0.05),  // α_Gd (higher damping)
            Real.of(-1e-3), // J_ex (antiferromagnetic)
            Real.of(20e-9)
        );
    }

    public static FerrimagneticLayer createMn4N() {
        // Mn4N - zero-moment half-metal candidate
        return new FerrimagneticLayer("Mn4N",
            Real.of(3e5),
            Real.of(2.8e5),
            Real.of(1.76e11),
            Real.of(1.76e11),
            Real.of(0.02),
            Real.of(0.02),
            Real.of(-0.5e-3),
            Real.of(10e-9)
        );
    }
}
