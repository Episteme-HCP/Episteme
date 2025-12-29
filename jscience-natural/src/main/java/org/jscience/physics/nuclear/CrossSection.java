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

package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Nuclear reaction cross-section models.
 * Cross-section measures probability of nuclear reactions.
 * Units: barns (1 barn = 10⁻²⁴ cm²)
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CrossSection {

    /** 1 barn in m² */
    public static final Real BARN = Real.of(1e-28);

    /**
     * Geometric cross-section for a nucleus.
     * σ = π r² where r = r₀ A^(1/3)
     * 
     * @param massNumber A = Z + N
     * @return Cross-section in m²
     */
    public static Real geometric(int massNumber) {
        Real r0 = Real.of(1.2e-15); // fm in meters
        Real radius = r0.multiply(Real.of(massNumber).cbrt());
        return Real.PI.multiply(radius.pow(2));
    }

    /**
     * Breit-Wigner resonance cross-section for compound nucleus formation.
     * σ(E) = σ₀ * (Γ/2)² / [(E - E₀)² + (Γ/2)²]
     * 
     * @param energy           Incident energy
     * @param resonanceEnergy  E₀ - Resonance peak energy
     * @param width            Γ - Resonance width (FWHM)
     * @param peakCrossSection σ₀ - Peak cross-section
     * @return Cross-section at given energy
     */
    public static Real breitWigner(Real energy, Real resonanceEnergy, Real width,
            Real peakCrossSection) {
        Real halfWidth = width.divide(Real.of(2.0));
        Real halfWidthSq = halfWidth.pow(2);
        Real deltaE = energy.subtract(resonanceEnergy);
        Real denom = deltaE.pow(2).add(halfWidthSq);
        return peakCrossSection.multiply(halfWidthSq).divide(denom);
    }

    /**
     * 1/v law for thermal neutron capture.
     * σ(E) = σ₀ * sqrt(E₀/E)
     * 
     * @param thermalCrossSection σ₀ at thermal energy (E₀ = 0.0253 eV)
     * @param energy              Neutron energy in eV
     * @return Cross-section at given energy
     */
    public static Real oneOverV(Real thermalCrossSection, Real energy) {
        Real thermalEnergy = Real.of(0.0253); // eV
        return thermalCrossSection.multiply(thermalEnergy.divide(energy).sqrt());
    }

    /**
     * Calculates reaction rate per target nucleus.
     * R = n * v * σ where n is projectile density, v is velocity
     * 
     * @param crossSection σ in m²
     * @param flux         φ = n*v in particles/(m²·s)
     * @return Reaction rate per target nucleus per second
     */
    public static Real reactionRate(Real crossSection, Real flux) {
        return flux.multiply(crossSection);
    }

    /**
     * Mean free path for neutron in material.
     * λ = 1 / (n * σ) where n is number density of targets
     * 
     * @param crossSection  σ in m²
     * @param numberDensity n in atoms/m³
     * @return Mean free path in meters
     */
    public static Real meanFreePath(Real crossSection, Real numberDensity) {
        return Real.ONE.divide(numberDensity.multiply(crossSection));
    }

    // --- Common thermal neutron capture cross-sections (in barns) ---

    /** Uranium-235 fission σ_f = 584 barns */
    public static final Real U235_FISSION = Real.of(584);

    /** Uranium-238 capture σ_c = 2.68 barns */
    public static final Real U238_CAPTURE = Real.of(2.68);

    /** Cadmium-113 capture σ_c = 20,600 barns */
    public static final Real CD113_CAPTURE = Real.of(20600);

    /** Boron-10 (n,α) σ = 3840 barns */
    public static final Real B10_ALPHA = Real.of(3840);

    /** Hydrogen capture σ = 0.332 barns */
    public static final Real H1_CAPTURE = Real.of(0.332);
}
