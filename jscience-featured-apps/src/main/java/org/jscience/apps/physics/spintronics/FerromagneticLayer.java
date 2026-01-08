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
    private Real coercivity; // Hc (A/m)
    private Real anisotropyField; // Hk (A/m)
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
