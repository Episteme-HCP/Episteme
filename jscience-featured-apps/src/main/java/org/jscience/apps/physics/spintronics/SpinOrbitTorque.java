/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Spin-Orbit Torque (SOT) calculations for heavy metal / ferromagnet bilayers.
 * <p>
 * SOT enables efficient magnetization switching via Spin Hall Effect (SHE).
 * </p>
 * 
 * <h3>Physics</h3>
 * <p>
 * A charge current J_c in the heavy metal (Pt, Ta, W) generates a transverse spin current
 * via the Spin Hall Effect:
 * $$ J_s = \theta_{SH} \frac{\hbar}{2e} J_c $$
 * This spin current exerts a torque on the adjacent ferromagnet.
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Liu, L. et al.</b> (2012). "Spin-Torque Switching with the Giant Spin Hall Effect of Tantalum". 
 *     <i>Science</i>, 336(6081), 555-558. 
 *     <a href="https://doi.org/10.1126/science.1218197">DOI: 10.1126/science.1218197</a></li>
 * <li><b>Miron, I. M. et al.</b> (2011). "Perpendicular switching of a single ferromagnetic layer induced by in-plane current injection". 
 *     <i>Nature</i>, 476, 189-193. 
 *     <a href="https://doi.org/10.1038/nature10309">DOI: 10.1038/nature10309</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SpinOrbitTorque {

    /**
     * Heavy metal properties.
     */
    public static class HeavyMetal {
        public final String name;
        public final Real spinHallAngle;    // θ_SH (dimensionless, can be negative)
        public final Real spinDiffusionLength; // λ_sf (m)
        public final Real resistivity;       // ρ (Ω·m)

        public HeavyMetal(String name, Real sha, Real sdl, Real rho) {
            this.name = name;
            this.spinHallAngle = sha;
            this.spinDiffusionLength = sdl;
            this.resistivity = rho;
        }

        public static final HeavyMetal PLATINUM = new HeavyMetal("Platinum", Real.of(0.08), Real.of(1.5e-9), Real.of(105e-9));
        public static final HeavyMetal TANTALUM_BETA = new HeavyMetal("β-Tantalum", Real.of(-0.15), Real.of(1.8e-9), Real.of(190e-9));
        public static final HeavyMetal TUNGSTEN_BETA = new HeavyMetal("β-Tungsten", Real.of(-0.30), Real.of(1.5e-9), Real.of(200e-9));
    }

    private final HeavyMetal metal;
    private final Real thickness; // Heavy metal thickness (m)

    public SpinOrbitTorque(HeavyMetal metal, Real thickness) {
        this.metal = metal;
        this.thickness = thickness;
    }

    /**
     * Calculates the damping-like SOT vector (Anti-damping torque).
     * $$ \tau_{DL} = \tau_0 (\mathbf{m} \times (\hat{\sigma} \times \mathbf{m})) $$
     * where σ is the spin polarization direction (perpendicular to current and normal).
     * 
     * @param currentDensity Charge current density in HM (A/m²)
     * @param layer Ferromagnetic layer receiving the torque
     * @param currentDirection Unit vector of current flow (usually x)
     * @return Damping-like torque vector
     */
    public Real[] calculateDampingLikeTorque(Real currentDensity, FerromagneticLayer layer, Real[] currentDirection) {
        // Spin polarization direction: σ = z × J_direction (for in-plane current, spin points perpendicular)
        Real[] sigma = {
            Real.ZERO.subtract(currentDirection[1]), // Simplified: perpendicular to current in-plane
            currentDirection[0],
            Real.ZERO
        };

        // τ_0 = (ℏ/2e) * θ_SH * J_c / (M_s * t_FM)
        Real hbar = Real.of(1.054571817e-34);
        Real e = Real.of(1.602176634e-19);
        Real ms = layer.getMaterial().getSaturationMagnetization();
        Real tFM = layer.getThickness();

        Real tau0 = hbar.divide(Real.TWO.multiply(e))
                .multiply(metal.spinHallAngle)
                .multiply(currentDensity)
                .divide(ms.multiply(tFM));

        Real[] m = layer.getMagnetization();

        // σ × m
        Real[] sigmaCrossM = crossProduct(sigma, m);
        // m × (σ × m)
        Real[] torque = crossProduct(m, sigmaCrossM);

        return new Real[] {
            torque[0].multiply(tau0),
            torque[1].multiply(tau0),
            torque[2].multiply(tau0)
        };
    }

    /**
     * Calculates the field-like SOT vector.
     * $$ \tau_{FL} = \tau_{FL,0} (\mathbf{m} \times \hat{\sigma}) $$
     */
    public Real[] calculateFieldLikeTorque(Real currentDensity, FerromagneticLayer layer, Real[] currentDirection) {
        Real[] sigma = { Real.ZERO.subtract(currentDirection[1]), currentDirection[0], Real.ZERO };
        Real[] m = layer.getMagnetization();

        // Simplified: assume field-like coefficient is 0.3 * damping-like
        Real hbar = Real.of(1.054571817e-34);
        Real e = Real.of(1.602176634e-19);
        Real ms = layer.getMaterial().getSaturationMagnetization();
        Real tFM = layer.getThickness();

        Real tau0 = hbar.divide(Real.TWO.multiply(e))
                .multiply(metal.spinHallAngle.multiply(Real.of(0.3)))
                .multiply(currentDensity)
                .divide(ms.multiply(tFM));

        Real[] torque = crossProduct(m, sigma);
        return new Real[] {
            torque[0].multiply(tau0),
            torque[1].multiply(tau0),
            torque[2].multiply(tau0)
        };
    }

    private static Real[] crossProduct(Real[] a, Real[] b) {
        return new Real[] {
            a[1].multiply(b[2]).subtract(a[2].multiply(b[1])),
            a[2].multiply(b[0]).subtract(a[0].multiply(b[2])),
            a[0].multiply(b[1]).subtract(a[1].multiply(b[0]))
        };
    }

    public HeavyMetal getMetal() { return metal; }
    public Real getThickness() { return thickness; }
}
