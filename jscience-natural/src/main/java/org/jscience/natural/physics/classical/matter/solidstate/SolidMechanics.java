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

package org.jscience.natural.physics.classical.matter.solidstate;

import org.jscience.core.mathematics.numbers.real.Real;

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
     * ÃƒÂÃ†â€™ = E * ÃƒÅ½Ã‚Âµ
     */
    public static Real hookesLaw(Real strain, Real youngsModulus) {
        return youngsModulus.multiply(strain);
    }

    /**
     * Strain from stress.
     * ÃƒÅ½Ã‚Âµ = ÃƒÂÃ†â€™ / E
     */
    public static Real strainFromStress(Real stress, Real youngsModulus) {
        return stress.divide(youngsModulus);
    }

    /**
     * Poisson's ratio relation.
     * ÃƒÅ½Ã‚Âµ_transverse = -ÃƒÅ½Ã‚Â½ * ÃƒÅ½Ã‚Âµ_axial
     */
    public static Real transverseStrain(Real axialStrain, Real poissonsRatio) {
        return poissonsRatio.negate().multiply(axialStrain);
    }

    /**
     * Shear modulus from Young's modulus and Poisson's ratio.
     * G = E / (2 * (1 + ÃƒÅ½Ã‚Â½))
     */
    public static Real shearModulus(Real youngsModulus, Real poissonsRatio) {
        return youngsModulus.divide(Real.TWO.multiply(Real.ONE.add(poissonsRatio)));
    }

    /**
     * Bulk modulus from E and ÃƒÅ½Ã‚Â½.
     * K = E / (3 * (1 - 2ÃƒÅ½Ã‚Â½))
     */
    public static Real bulkModulus(Real youngsModulus, Real poissonsRatio) {
        return youngsModulus.divide(Real.of(3).multiply(Real.ONE.subtract(Real.TWO.multiply(poissonsRatio))));
    }

    /**
     * Thermal expansion.
     * ÃƒÅ½Ã¢â‚¬ÂL = L0 * ÃƒÅ½Ã‚Â± * ÃƒÅ½Ã¢â‚¬ÂT
     */
    public static Real thermalExpansion(Real length, Real alpha, Real deltaT) {
        return length.multiply(alpha).multiply(deltaT);
    }

    /**
     * Thermal stress (constrained expansion).
     * ÃƒÂÃ†â€™ = E * ÃƒÅ½Ã‚Â± * ÃƒÅ½Ã¢â‚¬ÂT
     */
    public static Real thermalStress(Real youngsModulus, Real alpha, Real deltaT) {
        return youngsModulus.multiply(alpha).multiply(deltaT);
    }

    /**
     * Bending stress in a beam.
     * ÃƒÂÃ†â€™ = M * y / I
     */
    public static Real bendingStress(Real moment, Real y, Real momentOfInertia) {
        return moment.multiply(y).divide(momentOfInertia);
    }

    /**
     * Critical buckling load (Euler formula).
     * P_cr = ÃƒÂÃ¢â€šÂ¬Ãƒâ€šÃ‚Â² * E * I / LÃƒâ€šÃ‚Â²
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



