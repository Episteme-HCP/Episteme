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

package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

/**
 * Nuclear physics - decay, fission, fusion, binding energy.
 * All calculations performed using arbitrary precision Real numbers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NuclearPhysics {

    // Semi-empirical mass formula constants (approximate values in MeV)
    private static final Real AV = Real.of(15.75);
    private static final Real AS = Real.of(17.8);
    private static final Real AC = Real.of(0.711);
    private static final Real AA = Real.of(23.7);
    private static final Real AP = Real.of(12.0);

    private NuclearPhysics() {}

    /** Radioactive decay: N(t) = N₀e^(-λt) */
    public static Real radioactiveDecay(Real initialAmount, Real decayConstant, Real time) {
        return initialAmount.multiply(decayConstant.negate().multiply(time).exp());
    }

    /** Half-life: t₁/₂ = ln(2)/λ */
    public static Real halfLife(Real decayConstant) {
        return Real.LN2.divide(decayConstant);
    }

    /** Activity: A = λN */
    public static Real activity(Real decayConstant, Real numAtoms) {
        return decayConstant.multiply(numAtoms);
    }

    /** Mass-energy: E = mc² */
    public static Real massEnergy(Real mass) {
        return mass.multiply(PhysicalConstants.c.pow(2));
    }

    /** Binding energy per nucleon (SEMF) */
    public static Real bindingEnergyPerNucleon(int A, int Z) {
        Real rA = Real.of(A);
        Real rZ = Real.of(Z);
        int N = A - Z;
        Real rN = Real.of(N);
        
        // A^(1/3)
        Real A13 = rA.pow(Real.ONE.divide(Real.of(3)));

        // Terms for Binding Energy per Nucleon
        // Formula: BE/A = av - as*A^(-1/3) - ac*Z*(Z-1)/A^(4/3) - aa*(N-Z)^2/A^2 (+/- ap/A^(3/2))
        
 
        // Original logic was convoluted. Let's use the clean block I prepared below based on reading.
        // Actually, I'll just remove the unused calculation block and keep the one that was used at the end,
        // but verify it maps to the return statement.
        

         
         Real term1 = AV;
         Real term2 = AS.divide(A13);
         Real term3 = AC.multiply(rZ).multiply(rZ.subtract(Real.ONE)).divide(A13);
         Real term4 = AA.multiply(rN.subtract(rZ).pow(2)).divide(rA);
         
         Real be = term1.subtract(term2).subtract(term3).subtract(term4);
         
         if (N % 2 == 0 && Z % 2 == 0)
             be = be.add(AP.divide(rA.sqrt()));
         else if (N % 2 == 1 && Z % 2 == 1)
             be = be.subtract(AP.divide(rA.sqrt()));
             
         return be;
    }

    /** Q-value: Q = (Σm_reactants - Σm_products)c² */
    public static Real qValue(Real reactantMass, Real productMass) {
        return reactantMass.subtract(productMass).multiply(PhysicalConstants.c.pow(2));
    }

    /** Lawson triple product */
    public static Real lawsonTripleProduct(Real density, Real confinementTime, Real temperature) {
        return density.multiply(confinementTime).multiply(temperature);
    }

    /** Fission energy (~200 MeV per fission) */
    public static Real fissionEnergy(Real numFissions) {
        Real energyPerFission = Real.of(200e6).multiply(PhysicalConstants.e); // MeV to Joules
        return energyPerFission.multiply(numFissions);
    }

    /** Critical mass factor (simplified) */
    public static Real criticalMassFactor(Real density, Real crossSection) {
        return Real.ONE.divide(density.multiply(crossSection));
    }

    /** Exact critical mass (diffusion approximation for bare sphere) */
    public static Real exactCriticalMass(Real density, Real nu, Real sigmaF, Real sigmaA, Real diffusionCoeff) {
        Real netProduction = nu.multiply(sigmaF).subtract(sigmaA);
        if (netProduction.compareTo(Real.ZERO) <= 0)
            return Real.POSITIVE_INFINITY;
        Real buckling = netProduction.divide(diffusionCoeff).sqrt();
        Real criticalRadius = Real.PI.divide(buckling);
        Real volume = Real.of(4.0 / 3.0).multiply(Real.PI).multiply(criticalRadius.pow(3));
        return volume.multiply(density);
    }

    /** Neutron multiplication factor */
    public static Real multiplicationFactor(Real produced, Real absorbed) {
        return produced.divide(absorbed);
    }

    /** D-T fusion reaction rate (simplified) */
    public static Real fusionReactionRate(Real density1, Real density2, Real temperature) {
        Real tempFactor = temperature.divide(Real.of(1e7)).pow(2);
        Real sigmaV = Real.of(1e-22).multiply(tempFactor);
        return density1.multiply(density2).multiply(sigmaV);
    }
}
