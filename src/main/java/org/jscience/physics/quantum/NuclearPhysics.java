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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

/**
 * Nuclear physics - decay, fission, fusion, binding energy.
 * <p>
 * Based on latest nuclear data and modern computational methods.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class NuclearPhysics {

    /**
     * Radioactive decay: N(t) = N₀e^(-λt)
     * Returns remaining atoms at time t
     */
    public static Real radioactiveDecay(Real initialAmount, Real decayConstant, Real time) {
        return initialAmount.multiply(Real.of(Math.exp(-decayConstant.doubleValue() * time.doubleValue())));
    }

    /**
     * Half-life from decay constant: t₁/₂ = ln(2)/λ
     */
    public static Real halfLife(Real decayConstant) {
        return Real.of(Math.log(2)).divide(decayConstant);
    }

    /**
     * Activity: A = λN = (ln(2)/t₁/₂)N
     * Returns decay rate (Becquerels)
     */
    public static Real activity(Real decayConstant, Real numAtoms) {
        return decayConstant.multiply(numAtoms);
    }

    /**
     * Mass-energy equivalence: E = mc²
     */
    public static Real massEnergy(Real mass) {
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        return mass.multiply(c).multiply(c);
    }

    /**
     * Binding energy per nucleon (semi-empirical mass formula - SEMF):
     * BE/A ≈ aᵥ - aₛA^(-1/3) - aₒZ(Z-1)/A^(4/3) - aₐ(A-2Z)²/A² ± δ/A^(1/2)
     * 
     * Simplified version using Weizsäcker formula constants
     */
    public static Real bindingEnergyPerNucleon(int A, int Z) {
        // SEMF constants (MeV)
        double av = 15.75; // Volume
        double as = 17.8; // Surface
        double ac = 0.711; // Coulomb
        double aa = 23.7; // Asymmetry

        int N = A - Z;
        double A13 = Math.pow(A, 1.0 / 3.0);

        double volume = av;
        double surface = -as / A13;
        double coulomb = -ac * Z * (Z - 1) / A13;
        double asymmetry = -aa * (N - Z) * (N - Z) / A;

        // Pairing term (simplified)
        double pairing = 0;
        if (N % 2 == 0 && Z % 2 == 0)
            pairing = 12.0 / Math.sqrt(A);
        else if (N % 2 == 1 && Z % 2 == 1)
            pairing = -12.0 / Math.sqrt(A);

        double bePerA = volume + surface + coulomb + asymmetry + pairing;
        return Real.of(bePerA);
    }

    /**
     * Q-value for nuclear reaction: Q = (Σm_reactants - Σm_products)c²
     */
    public static Real qValue(Real reactantMass, Real productMass) {
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        return reactantMass.subtract(productMass).multiply(c).multiply(c);
    }

    /**
     * Fusion power (Lawson criterion): nτT > critical value
     * Returns triple product for fusion ignition assessment
     */
    public static Real lawsonTripleProduct(Real density, Real confinementTime, Real temperature) {
        return density.multiply(confinementTime).multiply(temperature);
    }

    /**
     * Fission energy release (U-235): ~200 MeV per fission
     * Returns energy from N fissions
     */
    public static Real fissionEnergy(Real numFissions) {
        Real energyPerFission = Real.of(200e6 * 1.602176634e-19); // 200 MeV in Joules
        return energyPerFission.multiply(numFissions);
    }

    /**
     * Critical mass (simplified sphere): M_crit ∝ 1/(ρσ)
     * Rough estimate for bare sphere
     */
    public static Real criticalMassFactor(Real density, Real crossSection) {
        // This is a simplified factor; real calculation requires neutron transport
        return Real.ONE.divide(density.multiply(crossSection));
    }

    /**
     * Exact Critical Mass (Diffusion Approximation for Bare Sphere).
     * <p>
     * M_c = (4/3) * pi * R_c^3 * rho
     * R_c = pi / B_g
     * B_g = sqrt((nu * Sigma_f - Sigma_a) / D)
     * </p>
     * 
     * @param density        density (rho)
     * @param nu             average neutrons per fission
     * @param sigmaF         macroscopic fission cross-section
     * @param sigmaA         macroscopic absorption cross-section
     * @param diffusionCoeff diffusion coefficient (D)
     * @return critical mass
     */
    public static Real exactCriticalMass(Real density, Real nu, Real sigmaF, Real sigmaA, Real diffusionCoeff) {
        // Calculate material buckling B_m^2 = (nu*Sigma_f - Sigma_a) / D
        Real production = nu.multiply(sigmaF);
        Real netProduction = production.subtract(sigmaA);

        if (netProduction.doubleValue() <= 0) {
            return Real.POSITIVE_INFINITY; // Subcritical material
        }

        Real bucklingSquared = netProduction.divide(diffusionCoeff);
        Real buckling = Real.of(Math.sqrt(bucklingSquared.doubleValue()));

        // Critical radius R_c = pi / B
        Real criticalRadius = Real.of(Math.PI).divide(buckling);

        // Critical mass M_c = 4/3 * pi * R_c^3 * rho
        Real volume = Real.of(4.0 / 3.0 * Math.PI).multiply(criticalRadius.pow(3));
        return volume.multiply(density);
    }

    /**
     * Neutron multiplication factor: k = (neutrons produced)/(neutrons absorbed)
     * k > 1: supercritical, k = 1: critical, k < 1: subcritical
     */
    public static Real multiplicationFactor(Real produced, Real absorbed) {
        return produced.divide(absorbed);
    }

    /**
     * Deuterium-Tritium fusion cross-section peak at ~100 keV
     * Returns approximate reaction rate (simplified)
     */
    public static Real fusionReactionRate(Real density1, Real density2, Real temperature) {
        // Simplified: σv ~ T² for D-T fusion in relevant range
        double sigmaV = 1e-22 * Math.pow(temperature.doubleValue() / 1e7, 2); // m³/s
        return density1.multiply(density2).multiply(Real.of(sigmaV));
    }
}
