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

package org.jscience.physics.classical.matter.solidstate;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Solid Mechanics equations (Stress, Strain, Elasticity).
 * <p>
 * Formerly MaterialProperties.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SolidMechanics {

    private SolidMechanics() {
    }

    /**
     * Hooke's law: stress-strain relation.
     * ÃÆ’ = E * ÃŽÂµ
     */
    public static Real hookesLaw(Real strain, Real youngsModulus) {
        return youngsModulus.multiply(strain);
    }

    /**
     * Strain from stress.
     * ÃŽÂµ = ÃÆ’ / E
     */
    public static Real strainFromStress(Real stress, Real youngsModulus) {
        return stress.divide(youngsModulus);
    }

    /**
     * Poisson's ratio relation.
     * ÃŽÂµ_transverse = -ÃŽÂ½ * ÃŽÂµ_axial
     */
    public static Real transverseStrain(Real axialStrain, Real poissonsRatio) {
        return poissonsRatio.negate().multiply(axialStrain);
    }

    /**
     * Shear modulus from Young's modulus and Poisson's ratio.
     * G = E / (2 * (1 + ÃŽÂ½))
     */
    public static Real shearModulus(Real youngsModulus, Real poissonsRatio) {
        return youngsModulus.divide(Real.TWO.multiply(Real.ONE.add(poissonsRatio)));
    }

    /**
     * Bulk modulus from E and ÃŽÂ½.
     * K = E / (3 * (1 - 2ÃŽÂ½))
     */
    public static Real bulkModulus(Real youngsModulus, Real poissonsRatio) {
        return youngsModulus.divide(Real.of(3).multiply(Real.ONE.subtract(Real.TWO.multiply(poissonsRatio))));
    }

    /**
     * Thermal expansion.
     * ÃŽâ€L = L0 * ÃŽÂ± * ÃŽâ€T
     */
    public static Real thermalExpansion(Real length, Real alpha, Real deltaT) {
        return length.multiply(alpha).multiply(deltaT);
    }

    /**
     * Thermal stress (constrained expansion).
     * ÃÆ’ = E * ÃŽÂ± * ÃŽâ€T
     */
    public static Real thermalStress(Real youngsModulus, Real alpha, Real deltaT) {
        return youngsModulus.multiply(alpha).multiply(deltaT);
    }

    /**
     * Bending stress in a beam.
     * ÃÆ’ = M * y / I
     */
    public static Real bendingStress(Real moment, Real y, Real momentOfInertia) {
        return moment.multiply(y).divide(momentOfInertia);
    }

    /**
     * Critical buckling load (Euler formula).
     * P_cr = Ãâ‚¬Ã‚Â² * E * I / LÃ‚Â²
     */
    public static Real eulerBucklingLoad(Real youngsModulus, Real momentOfInertia, Real length) {
        return Real.PI.pow(2).multiply(youngsModulus).multiply(momentOfInertia)
                .divide(length.pow(2));
    }

    /**
     * Hardness conversion: Brinell to Rockwell C (approximate).
     */
    public static Real brinellToRockwellC(Real brinell) {
        return Real.of(100).subtract(Real.of(0.014).multiply(brinell.subtract(Real.of(100))));
    }
}


