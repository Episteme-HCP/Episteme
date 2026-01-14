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

package org.jscience.chemistry.crystallography;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Unit cell and crystal system representation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class UnitCell {

    /** The seven crystal systems. */
    public enum CrystalSystem {
        CUBIC("Cubic", "a=b=c, ÃŽÂ±=ÃŽÂ²=ÃŽÂ³=90Ã‚Â°"),
        TETRAGONAL("Tetragonal", "a=bÃ¢â€°Â c, ÃŽÂ±=ÃŽÂ²=ÃŽÂ³=90Ã‚Â°"),
        ORTHORHOMBIC("Orthorhombic", "aÃ¢â€°Â bÃ¢â€°Â c, ÃŽÂ±=ÃŽÂ²=ÃŽÂ³=90Ã‚Â°"),
        HEXAGONAL("Hexagonal", "a=bÃ¢â€°Â c, ÃŽÂ±=ÃŽÂ²=90Ã‚Â°, ÃŽÂ³=120Ã‚Â°"),
        TRIGONAL("Trigonal", "a=b=c, ÃŽÂ±=ÃŽÂ²=ÃŽÂ³Ã¢â€°Â 90Ã‚Â°"),
        MONOCLINIC("Monoclinic", "aÃ¢â€°Â bÃ¢â€°Â c, ÃŽÂ±=ÃŽÂ³=90Ã‚Â°, ÃŽÂ²Ã¢â€°Â 90Ã‚Â°"),
        TRICLINIC("Triclinic", "aÃ¢â€°Â bÃ¢â€°Â c, ÃŽÂ±Ã¢â€°Â ÃŽÂ²Ã¢â€°Â ÃŽÂ³Ã¢â€°Â 90Ã‚Â°");

        private final String constraints;

        CrystalSystem(String name, String constraints) {
            this.constraints = constraints;
        }

        public String getConstraints() {
            return constraints;
        }
    }

    private final Real a, b, c; // Lattice parameters (Ãƒâ€¦)
    private final Real alpha, beta, gamma; // Angles (degrees)
    private final CrystalSystem system;

    public UnitCell(Real a, Real b, Real c, Real alpha, Real beta, Real gamma) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.system = determineSystem();
    }

    public UnitCell(double a, double b, double c, double alpha, double beta, double gamma) {
        this(Real.of(a), Real.of(b), Real.of(c), Real.of(alpha), Real.of(beta), Real.of(gamma));
    }

    /** Creates a cubic unit cell. */
    public static UnitCell cubic(Real a) {
        return new UnitCell(a, a, a, Real.of(90), Real.of(90), Real.of(90));
    }

    public static UnitCell cubic(double a) {
        return cubic(Real.of(a));
    }

    /** Creates a tetragonal unit cell. */
    public static UnitCell tetragonal(Real a, Real c) {
        return new UnitCell(a, a, c, Real.of(90), Real.of(90), Real.of(90));
    }

    public static UnitCell tetragonal(double a, double c) {
        return tetragonal(Real.of(a), Real.of(c));
    }

    /** Creates an orthorhombic unit cell. */
    public static UnitCell orthorhombic(Real a, Real b, Real c) {
        return new UnitCell(a, b, c, Real.of(90), Real.of(90), Real.of(90));
    }

    public static UnitCell orthorhombic(double a, double b, double c) {
        return orthorhombic(Real.of(a), Real.of(b), Real.of(c));
    }

    /** Creates a hexagonal unit cell. */
    public static UnitCell hexagonal(Real a, Real c) {
        return new UnitCell(a, a, c, Real.of(90), Real.of(90), Real.of(120));
    }

    public static UnitCell hexagonal(double a, double c) {
        return hexagonal(Real.of(a), Real.of(c));
    }

    private CrystalSystem determineSystem() {
        Real tol = Real.of(0.001);
        Real angleTol = Real.of(0.1);

        boolean aEqB = a.subtract(b).abs().compareTo(tol) < 0;
        boolean bEqC = b.subtract(c).abs().compareTo(tol) < 0;
        boolean aEqC = a.subtract(c).abs().compareTo(tol) < 0;
        boolean alpha90 = alpha.subtract(Real.of(90)).abs().compareTo(angleTol) < 0;
        boolean beta90 = beta.subtract(Real.of(90)).abs().compareTo(angleTol) < 0;
        boolean gamma90 = gamma.subtract(Real.of(90)).abs().compareTo(angleTol) < 0;
        boolean gamma120 = gamma.subtract(Real.of(120)).abs().compareTo(angleTol) < 0;

        if (aEqB && bEqC && alpha90 && beta90 && gamma90)
            return CrystalSystem.CUBIC;
        if (aEqB && !bEqC && alpha90 && beta90 && gamma90)
            return CrystalSystem.TETRAGONAL;
        if (!aEqB && !bEqC && alpha90 && beta90 && gamma90)
            return CrystalSystem.ORTHORHOMBIC;
        if (aEqB && !bEqC && alpha90 && beta90 && gamma120)
            return CrystalSystem.HEXAGONAL;
        if (aEqB && aEqC && !alpha90)
            return CrystalSystem.TRIGONAL;
        if (alpha90 && gamma90 && !beta90)
            return CrystalSystem.MONOCLINIC;
        return CrystalSystem.TRICLINIC;
    }

    /**
     * Calculates unit cell volume.
     * V = abc * sqrt(1 - cosÃ‚Â²ÃŽÂ± - cosÃ‚Â²ÃŽÂ² - cosÃ‚Â²ÃŽÂ³ + 2cosÃŽÂ±Ã‚Â·cosÃŽÂ²Ã‚Â·cosÃŽÂ³)
     */
    public Real volume() {
        Real cosA = alpha.toRadians().cos();
        Real cosB = beta.toRadians().cos();
        Real cosG = gamma.toRadians().cos();

        Real factor = Real.ONE.subtract(cosA.pow(2)).subtract(cosB.pow(2)).subtract(cosG.pow(2))
                .add(Real.TWO.multiply(cosA).multiply(cosB).multiply(cosG));
        return a.multiply(b).multiply(c).multiply(factor.sqrt());
    }

    /**
     * Calculates d-spacing for a given Miller index.
     */
    public Real dSpacing(int h, int k, int l) {
        Real hR = Real.of(h);
        Real kR = Real.of(k);
        Real lR = Real.of(l);

        if (system == CrystalSystem.CUBIC) {
            return a.divide(hR.pow(2).add(kR.pow(2)).add(lR.pow(2)).sqrt());
        }
        if (system == CrystalSystem.ORTHORHOMBIC || system == CrystalSystem.TETRAGONAL) {
            Real sum = hR.pow(2).divide(a.pow(2))
                    .add(kR.pow(2).divide(b.pow(2)))
                    .add(lR.pow(2).divide(c.pow(2)));
            return Real.ONE.divide(sum.sqrt());
        }
        return a.divide(hR.pow(2).add(kR.pow(2)).add(lR.pow(2)).sqrt());
    }

    /**
     * Bragg's law: calculates diffraction angle.
     * nÃŽÂ» = 2dÃ‚Â·sin(ÃŽÂ¸)
     */
    public static Real braggAngle(Real d, Real wavelength, int n) {
        Real sinTheta = Real.of(n).multiply(wavelength).divide(Real.TWO.multiply(d));
        if (sinTheta.doubleValue() > 1 || sinTheta.doubleValue() < -1)
            return Real.of(Double.NaN);
        return sinTheta.asin().toDegrees();
    }

    // Getters
    public Real getA() {
        return a;
    }

    public Real getB() {
        return b;
    }

    public Real getC() {
        return c;
    }

    public Real getAlpha() {
        return alpha;
    }

    public Real getBeta() {
        return beta;
    }

    public Real getGamma() {
        return gamma;
    }

    public CrystalSystem getSystem() {
        return system;
    }

    // --- Common crystal structures ---
    public static final UnitCell NACL = cubic(5.64);
    public static final UnitCell DIAMOND = cubic(3.567);
    public static final UnitCell SILICON = cubic(5.431);

    /** Cu-KÃŽÂ± X-ray wavelength */
    public static final Real CU_K_ALPHA = Real.of(1.5406);

    /** Mo-KÃŽÂ± X-ray wavelength */
    public static final Real MO_K_ALPHA = Real.of(0.7107);
}


