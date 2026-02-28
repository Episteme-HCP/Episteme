/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics.nuclear;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Nuclear reaction cross-section models.
 * Cross-section measures probability of nuclear reactions.
 * Units: barns (1 barn = 10ÃƒÂ¢Ã‚ÂÃ‚Â»Ãƒâ€šÃ‚Â²ÃƒÂ¢Ã‚ÂÃ‚Â´ cmÃƒâ€šÃ‚Â²)
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CrossSection {

    /** 1 barn in mÃƒâ€šÃ‚Â² */
    public static final Real BARN = Real.of(1e-28);

    /**
     * Geometric cross-section for a nucleus.
     * ÃƒÂÃ†â€™ = ÃƒÂÃ¢â€šÂ¬ rÃƒâ€šÃ‚Â² where r = rÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬ A^(1/3)
     * 
     * @param massNumber A = Z + N
     * @return Cross-section in mÃƒâ€šÃ‚Â²
     */
    public static Real geometric(int massNumber) {
        Real r0 = Real.of(1.2e-15); // fm in meters
        Real radius = r0.multiply(Real.of(massNumber).cbrt());
        return Real.PI.multiply(radius.pow(2));
    }

    /**
     * Breit-Wigner resonance cross-section for compound nucleus formation.
     * ÃƒÂÃ†â€™(E) = ÃƒÂÃ†â€™ÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬ * (ÃƒÅ½Ã¢â‚¬Å“/2)Ãƒâ€šÃ‚Â² / [(E - EÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬)Ãƒâ€šÃ‚Â² + (ÃƒÅ½Ã¢â‚¬Å“/2)Ãƒâ€šÃ‚Â²]
     * 
     * @param energy           Incident energy
     * @param resonanceEnergy  EÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬ - Resonance peak energy
     * @param width            ÃƒÅ½Ã¢â‚¬Å“ - Resonance width (FWHM)
     * @param peakCrossSection ÃƒÂÃ†â€™ÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬ - Peak cross-section
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
     * ÃƒÂÃ†â€™(E) = ÃƒÂÃ†â€™ÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬ * sqrt(EÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬/E)
     * 
     * @param thermalCrossSection ÃƒÂÃ†â€™ÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬ at thermal energy (EÃƒÂ¢Ã¢â‚¬Å¡Ã¢â€šÂ¬ = 0.0253 eV)
     * @param energy              Neutron energy in eV
     * @return Cross-section at given energy
     */
    public static Real oneOverV(Real thermalCrossSection, Real energy) {
        Real thermalEnergy = Real.of(0.0253); // eV
        return thermalCrossSection.multiply(thermalEnergy.divide(energy).sqrt());
    }

    /**
     * Calculates reaction rate per target nucleus.
     * R = n * v * ÃƒÂÃ†â€™ where n is projectile density, v is velocity
     * 
     * @param crossSection ÃƒÂÃ†â€™ in mÃƒâ€šÃ‚Â²
     * @param flux         ÃƒÂÃ¢â‚¬Â  = n*v in particles/(mÃƒâ€šÃ‚Â²Ãƒâ€šÃ‚Â·s)
     * @return Reaction rate per target nucleus per second
     */
    public static Real reactionRate(Real crossSection, Real flux) {
        return flux.multiply(crossSection);
    }

    /**
     * Mean free path for neutron in material.
     * ÃƒÅ½Ã‚Â» = 1 / (n * ÃƒÂÃ†â€™) where n is number density of targets
     * 
     * @param crossSection  ÃƒÂÃ†â€™ in mÃƒâ€šÃ‚Â²
     * @param numberDensity n in atoms/mÃƒâ€šÃ‚Â³
     * @return Mean free path in meters
     */
    public static Real meanFreePath(Real crossSection, Real numberDensity) {
        return Real.ONE.divide(numberDensity.multiply(crossSection));
    }

    // --- Common thermal neutron capture cross-sections (in barns) ---

    /** Uranium-235 fission ÃƒÂÃ†â€™_f = 584 barns */
    public static final Real U235_FISSION = Real.of(584);

    /** Uranium-238 capture ÃƒÂÃ†â€™_c = 2.68 barns */
    public static final Real U238_CAPTURE = Real.of(2.68);

    /** Cadmium-113 capture ÃƒÂÃ†â€™_c = 20,600 barns */
    public static final Real CD113_CAPTURE = Real.of(20600);

    /** Boron-10 (n,ÃƒÅ½Ã‚Â±) ÃƒÂÃ†â€™ = 3840 barns */
    public static final Real B10_ALPHA = Real.of(3840);

    /** Hydrogen capture ÃƒÂÃ†â€™ = 0.332 barns */
    public static final Real H1_CAPTURE = Real.of(0.332);
}



