/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

    public SpintronicMaterial(String name, Real polarization, Real diffusion, Real mfp, Real rho, boolean fm,
            Real exchange) {
        this.name = name;
        this.spinPolarization = polarization;
        this.spinDiffusionLength = diffusion;
        this.meanFreePath = mfp;
        this.resistivity = rho;
        this.ferromagnetic = fm;
        this.exchangeEnergy = exchange;
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

    @Override
    public String toString() {
        return name;
    }

    // Constantes physiques pour matériaux (valeurs réalistes SI)
    public static final SpintronicMaterial COBALT = new SpintronicMaterial(
            "Cobalt", Real.of(0.45), Real.of(60e-9), Real.of(10e-9), Real.of(60e-9), true, Real.of(1.5e-21));

    public static final SpintronicMaterial PERMALLOY = new SpintronicMaterial(
            "Permalloy (Ni80Fe20)", Real.of(0.35), Real.of(5e-9), Real.of(3e-9), Real.of(200e-9), true,
            Real.of(1.0e-21));

    public static final SpintronicMaterial COPPER = new SpintronicMaterial(
            "Copper", Real.ZERO, Real.of(500e-9), Real.of(40e-9), Real.of(17e-9), false, Real.ZERO);
}
