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
package org.jscience.physics.classical.matter.solidstate;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Solid Mechanics equations (Stress, Strain, Elasticity).
 * <p>
 * Formerly MaterialProperties.
 * </p>
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SolidMechanics {

    private SolidMechanics() {
    }

    /**
     * Hooke's law: stress-strain relation.
     * σ = E * ε
     */
    public static Real hookesLaw(Real strain, Real youngsModulus) {
        return youngsModulus.multiply(strain);
    }

    /**
     * Strain from stress.
     * ε = σ / E
     */
    public static Real strainFromStress(Real stress, Real youngsModulus) {
        return stress.divide(youngsModulus);
    }

    /**
     * Poisson's ratio relation.
     * ε_transverse = -ν * ε_axial
     */
    public static Real transverseStrain(Real axialStrain, Real poissonsRatio) {
        return poissonsRatio.negate().multiply(axialStrain);
    }

    /**
     * Shear modulus from Young's modulus and Poisson's ratio.
     * G = E / (2 * (1 + ν))
     */
    public static Real shearModulus(Real youngsModulus, Real poissonsRatio) {
        return youngsModulus.divide(Real.TWO.multiply(Real.ONE.add(poissonsRatio)));
    }

    /**
     * Bulk modulus from E and ν.
     * K = E / (3 * (1 - 2ν))
     */
    public static Real bulkModulus(Real youngsModulus, Real poissonsRatio) {
        return youngsModulus.divide(Real.of(3).multiply(Real.ONE.subtract(Real.TWO.multiply(poissonsRatio))));
    }

    /**
     * Thermal expansion.
     * ΔL = L0 * α * ΔT
     */
    public static Real thermalExpansion(Real length, Real alpha, Real deltaT) {
        return length.multiply(alpha).multiply(deltaT);
    }

    /**
     * Thermal stress (constrained expansion).
     * σ = E * α * ΔT
     */
    public static Real thermalStress(Real youngsModulus, Real alpha, Real deltaT) {
        return youngsModulus.multiply(alpha).multiply(deltaT);
    }

    /**
     * Bending stress in a beam.
     * σ = M * y / I
     */
    public static Real bendingStress(Real moment, Real y, Real momentOfInertia) {
        return moment.multiply(y).divide(momentOfInertia);
    }

    /**
     * Critical buckling load (Euler formula).
     * P_cr = π² * E * I / L²
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
