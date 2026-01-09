/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a ferromagnetic layer with 3D magnetization vector.
 * <p>
 * Implements the macrospin approximation where the entire layer is treated as a
 * single magnetic moment.
 * This is valid for dimensions smaller than the exchange length (~5-10 nm for
 * Permalloy).
 * </p>
 * 
 * @see <a href="https://doi.org/10.1103/RevModPhys.77.1375">Žutić, I., et al.
 *      Spintronics: Fundamentals and applications. Rev. Mod. Phys. 77, 1375
 *      (2005)</a>
 */
public class FerromagneticLayer {
    private SpintronicMaterial material;
    private Real thickness;
    private final boolean pinned;
    private Real[] magnetization = { Real.ONE, Real.ZERO, Real.ZERO }; // Vector (mx, my, mz)
    private Real damping = Real.of(0.01); 
    private Real temperature = Real.of(300); // K
    private Real pmaEnergy = Real.ZERO; // J/m3 (Ku)
    private boolean perpendicularAnisotropy = false;
    private Real spinHallAngle = Real.ZERO; // SOT Efficiency
    private Real sotCurrentDensity = Real.ZERO; // J_SOT (A/m2)
    private Real coercivity = Real.of(1000); // Hc (A/m)
    private Real anisotropyField = Real.of(5000); // Hk (A/m)
    private Real[] easyAxis = { Real.ONE, Real.ZERO, Real.ZERO };

    public FerromagneticLayer(SpintronicMaterial material, Real thickness, boolean pinned) {
        this.material = material;
        this.thickness = thickness;
        this.pinned = pinned;
        this.coercivity = Real.of(1000); // Default
        this.anisotropyField = Real.of(5000);
    }

    public void setEasyAxis(Real x, Real y, Real z) {
        Real norm = x.pow(2).add(y.pow(2)).add(z.pow(2)).sqrt();
        this.easyAxis[0] = x.divide(norm);
        this.easyAxis[1] = y.divide(norm);
        this.easyAxis[2] = z.divide(norm);
    }

    public void setMagnetization(Real mx, Real my, Real mz) {
        // Normalize
        Real norm = mx.pow(2).add(my.pow(2)).add(mz.pow(2)).sqrt();
        this.magnetization[0] = mx.divide(norm);
        this.magnetization[1] = my.divide(norm);
        this.magnetization[2] = mz.divide(norm);
    }

    public Real[] getMagnetization() {
        return magnetization;
    }

    public SpintronicMaterial getMaterial() {
        return material;
    }

    public void setMaterial(SpintronicMaterial m) {
        this.material = m;
    }

    public Real getThickness() {
        return thickness;
    }

    public void setThickness(Real t) {
        this.thickness = t;
    }

    public Real getCoercivity() {
        return coercivity;
    }

    public void setCoercivity(Real coercivity) {
        this.coercivity = coercivity;
    }

    public Real getDamping() { return damping; }
    public void setDamping(Real damping) { this.damping = damping; }

    public Real getTemperature() { return temperature; }
    public void setTemperature(Real temperature) { this.temperature = temperature; }

    public Real getPmaEnergy() { return pmaEnergy; }
    public void setPmaEnergy(Real pmaEnergy) { this.pmaEnergy = pmaEnergy; }

    public boolean isPerpendicularAnisotropy() { return perpendicularAnisotropy; }
    public void setPerpendicularAnisotropy(boolean perpendicularAnisotropy) { this.perpendicularAnisotropy = perpendicularAnisotropy; }

    public Real getSpinHallAngle() { return spinHallAngle; }
    public void setSpinHallAngle(Real angle) { this.spinHallAngle = angle; }

    public Real getSotCurrentDensity() { return sotCurrentDensity; }
    public void setSotCurrentDensity(Real j) { this.sotCurrentDensity = j; }

    /**
     * Calculates the effective magnetic field (A/m) for this layer.
     * Includes Anisotropy (In-plane or Perpendicular) and Demagnetization.
     */
    public Real[] calculateEffectiveField() {
        Real ms = material.getSaturationMagnetization();
        if (ms.doubleValue() < 1e-6) return new Real[]{Real.ZERO, Real.ZERO, Real.ZERO};

        Real[] field = new Real[3];
        
        // --- 1. Shape/Crystal Anisotropy (Hk) ---
        // H_ani = Hk * (m . easyAxis) * easyAxis
        Real dotEasy = magnetization[0].multiply(easyAxis[0])
                      .add(magnetization[1].multiply(easyAxis[1]))
                      .add(magnetization[2].multiply(easyAxis[2]));
        
        field[0] = anisotropyField.multiply(dotEasy).multiply(easyAxis[0]);
        field[1] = anisotropyField.multiply(dotEasy).multiply(easyAxis[1]);
        field[2] = anisotropyField.multiply(dotEasy).multiply(easyAxis[2]);

        // --- 2. Perpendicular Anisotropy (PMA) ---
        // Effective PMA field along Y (stack axis)
        if (perpendicularAnisotropy) {
            // H_k_pma = 2*Ku / (mu0 * Ms)
            double mu0 = 4.0 * Math.PI * 1e-7;
            Real hPma = pmaEnergy.multiply(Real.TWO).divide(Real.of(mu0).multiply(ms));
            field[1] = field[1].add(hPma.multiply(magnetization[1]));
        }

        // --- 3. Demagnetizing Field (simplified) ---
        // For a thin film stacked on Y: Nx=Nz=0, Ny=1 (SI)
        // H_demag = -Ms * my * j_vec
        field[1] = field[1].subtract(ms.multiply(magnetization[1]));

        return field;
    }

    public boolean isPinned() {
        return pinned;
    }

    /** Angle relatif avec une autre couche (produit scalaire) */
    public Real getAngleWith(FerromagneticLayer other) {
        Real dot = magnetization[0].multiply(other.magnetization[0])
                .add(magnetization[1].multiply(other.magnetization[1]))
                .add(magnetization[2].multiply(other.magnetization[2]));
        // Clamp for acos
        if (dot.compareTo(Real.ONE) > 0)
            dot = Real.ONE;
        if (dot.compareTo(Real.ONE.negate()) < 0)
            dot = Real.ONE.negate();
        return dot.acos();
    }

    /**
     * Gets the in-plane magnetization angle relative to the X-axis.
     * 
     * @return Angle in radians [-PI, PI]
     */
    public double getMagnetizationAngle() {
        return Math.atan2(magnetization[1].doubleValue(), magnetization[0].doubleValue());
    }
}
