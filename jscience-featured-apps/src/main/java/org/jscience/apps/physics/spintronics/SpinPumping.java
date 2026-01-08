/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Spin Pumping effect at magnetodynamic interfaces.
 * <p>
 * Precessing magnetization injects spin current into adjacent non-magnetic layer.
 * Detected via DC voltage from ISHE or spin accumulation.
 * </p>
 * 
 * <h3>Physics</h3>
 * <p>
 * Spin current from FMR-driven precession:
 * $$ J_s = \frac{\hbar}{4\pi} g^{\uparrow\downarrow}_{eff} \mathbf{m} \times \frac{d\mathbf{m}}{dt} $$
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Tserkovnyak, Y. et al.</b> (2002). "Enhanced Gilbert damping in thin ferromagnetic films". 
 *     <i>Phys. Rev. Lett.</i>, 88, 117601. 
 *     <a href="https://doi.org/10.1103/PhysRevLett.88.117601">DOI: 10.1103/PhysRevLett.88.117601</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SpinPumping {

    private final Real spinMixingConductance; // g↑↓ (1/m²)
    private final Real fmThickness;           // FM thickness (m)
    private final Real nmThickness;           // NM thickness (m)
    private final SpinOrbitTorque.HeavyMetal nmMaterial;

    private static final Real HBAR = Real.of(1.054571817e-34);

    public SpinPumping(Real gUpDown, Real tFM, Real tNM, SpinOrbitTorque.HeavyMetal nm) {
        this.spinMixingConductance = gUpDown;
        this.fmThickness = tFM;
        this.nmThickness = tNM;
        this.nmMaterial = nm;
    }

    /**
     * Calculates pumped spin current from magnetization precession.
     * @param m Current magnetization (unit vector)
     * @param dmdt Time derivative of magnetization
     * @return Spin current density (A/m²)
     */
    public Real[] calculateSpinCurrent(Real[] m, Real[] dmdt) {
        // J_s = (ℏ/4π) * g↑↓ * (m × dm/dt)
        Real[] cross = crossProduct(m, dmdt);
        Real prefactor = HBAR.divide(Real.of(4 * Math.PI)).multiply(spinMixingConductance);
        
        return new Real[]{
            cross[0].multiply(prefactor),
            cross[1].multiply(prefactor),
            cross[2].multiply(prefactor)
        };
    }

    /**
     * Calculates additional Gilbert damping from spin pumping.
     * $$ \Delta\alpha = \frac{g \mu_B g^{\uparrow\downarrow}}{4\pi M_s t_{FM}} $$
     */
    public Real calculateDampingEnhancement(Real ms, Real gFactor) {
        Real muB = Real.of(9.274e-24); // Bohr magneton
        Real numerator = gFactor.multiply(muB).multiply(spinMixingConductance);
        Real denominator = Real.of(4 * Math.PI).multiply(ms).multiply(fmThickness);
        return numerator.divide(denominator);
    }

    /**
     * Calculates DC voltage from ISHE rectification during FMR.
     * @param rfField AC driving field amplitude (A/m)
     * @param frequency FMR frequency (Hz)
     * @param linewidth FMR linewidth (Hz)
     * @param ms Saturation magnetization
     * @return DC voltage (V)
     */
    public Real calculateFMRVoltage(Real rfField, Real frequency, Real linewidth, Real ms) {
        // Simplified Lorentzian lineshape * ISHE conversion
        // V_DC ∝ θ_SH * λ * J_s * w
        
        // Estimate precession cone angle from RF field
        Real omega = frequency.multiply(Real.of(2 * Math.PI));
        Real gamma = Real.of(1.76e11);
        Real coneAngle = rfField.divide(linewidth.multiply(gamma)); // rad
        
        // dmdt amplitude ∝ ω * sin(cone)
        Real dmdtMag = omega.multiply(coneAngle);
        
        // Spin current
        Real jsMag = HBAR.divide(Real.of(4 * Math.PI))
                .multiply(spinMixingConductance)
                .multiply(dmdtMag);
        
        // ISHE voltage
        return nmMaterial.spinHallAngle
                .multiply(nmMaterial.spinDiffusionLength)
                .multiply(jsMag)
                .multiply(nmThickness);
    }

    private static Real[] crossProduct(Real[] a, Real[] b) {
        return new Real[]{
            a[1].multiply(b[2]).subtract(a[2].multiply(b[1])),
            a[2].multiply(b[0]).subtract(a[0].multiply(b[2])),
            a[0].multiply(b[1]).subtract(a[1].multiply(b[0]))
        };
    }

    // Factory
    public static SpinPumping createNiFePt() {
        return new SpinPumping(
            Real.of(3e19),   // g↑↓ ~ 3×10^19 m⁻²
            Real.of(10e-9),  // 10 nm NiFe
            Real.of(10e-9),  // 10 nm Pt
            SpinOrbitTorque.HeavyMetal.PLATINUM
        );
    }

    public Real getSpinMixingConductance() { return spinMixingConductance; }
}
