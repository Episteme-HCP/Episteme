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
 * Represents a material with advanced spintronic properties.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SpintronicMaterial {
    private final String name;
    private final Real spinPolarization; // P, dimensionless [-1, 1]
    private final Real spinDiffusionLength; // λ_sf (m)
    private final Real meanFreePath; // l_mfp (m)
    private final Real resistivity; // ρ (Ω·m)
    private final boolean ferromagnetic;
    private final Real exchangeEnergy; // J (J)
    private final Real saturationMagnetization; // Ms (A/m)

    public SpintronicMaterial(String name, Real polarization, Real diffusion, Real mfp, Real rho, boolean fm,
            Real exchange, Real ms) {
        this.name = name;
        this.spinPolarization = polarization;
        this.spinDiffusionLength = diffusion;
        this.meanFreePath = mfp;
        this.resistivity = rho;
        this.ferromagnetic = fm;
        this.exchangeEnergy = exchange;
        this.saturationMagnetization = ms;
    }

    public String getName() {
        return name;
    }

    public Real getSpinPolarization() {
        return spinPolarization;
    }

    public Real getSpinDiffusionLength() {
        return spinDiffusionLength;
    }

    public Real getMeanFreePath() {
        return meanFreePath;
    }

    public Real getResistivity() {
        return resistivity;
    }

    public boolean isFerromagnetic() {
        return ferromagnetic;
    }

    public Real getExchangeEnergy() {
        return exchangeEnergy;
    }

    public Real getSaturationMagnetization() {
        return saturationMagnetization;
    }

    /** Returns a synthetic spacer material (simplification for logic) */
    public static SpintronicMaterial toSAFSpacer() {
        return RUTHENIUM;
    }

    @Override
    public String toString() {
        return name;
    }

    // Constantes physiques pour matériaux (valeurs réalistes SI)
    public static final SpintronicMaterial COBALT = new SpintronicMaterial(
            "Cobalt", Real.of(0.45), Real.of(60e-9), Real.of(10e-9), Real.of(60e-9), true, Real.of(1.5e-21),
            Real.of(1.4e6));

    public static final SpintronicMaterial PERMALLOY = new SpintronicMaterial(
            "Permalloy (Ni80Fe20)", Real.of(0.35), Real.of(5e-9), Real.of(3e-9), Real.of(200e-9), true,
            Real.of(0.8e-21), Real.of(8.0e5));

    public static final SpintronicMaterial IRON = new SpintronicMaterial(
            "Iron", Real.of(0.40), Real.of(40e-9), Real.of(4e-9), Real.of(100e-9), true, Real.of(1.8e-21),
            Real.of(1.7e6));

    public static final SpintronicMaterial NICKEL = new SpintronicMaterial(
            "Nickel", Real.of(0.33), Real.of(20e-9), Real.of(6e-9), Real.of(70e-9), true, Real.of(0.8e-21),
            Real.of(4.9e5));

    public static final SpintronicMaterial COPPER = new SpintronicMaterial(
            "Copper", Real.ZERO, Real.of(500e-9), Real.of(40e-9), Real.of(17e-9), false, Real.ZERO, Real.ZERO);

    public static final SpintronicMaterial SILVER = new SpintronicMaterial(
            "Silver", Real.ZERO, Real.of(700e-9), Real.of(50e-9), Real.of(16e-9), false, Real.ZERO, Real.ZERO);

    public static final SpintronicMaterial ALUMINUM = new SpintronicMaterial(
            "Aluminum", Real.ZERO, Real.of(1000e-9), Real.of(20e-9), Real.of(27e-9), false, Real.ZERO, Real.ZERO);

    public static final SpintronicMaterial RUTHENIUM = new SpintronicMaterial(
            "Ruthenium", Real.ZERO, Real.of(10e-9), Real.of(10e-9), Real.of(71e-9), false, Real.ZERO, Real.ZERO);

    // Heavy Metals for SOT
    public static final SpintronicMaterial PLATINUM = new SpintronicMaterial(
            "Platinum", Real.of(0.1), Real.of(2e-9), Real.of(5e-9), Real.of(106e-9), false, Real.ZERO, Real.ZERO);

    public static final SpintronicMaterial TANTALUM = new SpintronicMaterial(
            "Tantalum", Real.of(-0.15), Real.of(1.5e-9), Real.of(2e-9), Real.of(190e-9), false, Real.ZERO, Real.ZERO);

    public static final SpintronicMaterial TUNGSTEN = new SpintronicMaterial(
            "Tungsten (\u03B2-W)", Real.of(-0.3), Real.of(2e-9), Real.of(3e-9), Real.of(150e-9), false, Real.ZERO, Real.ZERO);
}
