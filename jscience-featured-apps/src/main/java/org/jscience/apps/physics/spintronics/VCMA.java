/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Voltage-Controlled Magnetic Anisotropy (VCMA) effect.
 * <p>
 * Electric field at the FM/oxide interface modifies the perpendicular
 * anisotropy constant, enabling low-power magnetization control.
 * </p>
 * 
 * <h3>Physics</h3>
 * <p>
 * $$ K_{eff}(V) = K_0 + \xi_{VCMA} \frac{V}{t_{ox}} $$
 * where ξ_VCMA is the VCMA coefficient (typically 30-100 fJ/V·m).
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Wang, W.G. et al.</b> (2012). "Electric-field-assisted switching in magnetic tunnel junctions". 
 *     <i>Nature Materials</i>, 11, 64-68. 
 *     <a href="https://doi.org/10.1038/nmat3171">DOI: 10.1038/nmat3171</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class VCMA {

    private final Real baseAnisotropy;     // K_0 (J/m³)
    private final Real vcmaCoefficient;    // ξ_VCMA (J/V·m = fJ/V·m * 1e-15)
    private final Real oxideThickness;     // t_ox (m)

    /**
     * Creates a VCMA system.
     * @param k0 Base anisotropy constant (J/m³)
     * @param xi VCMA coefficient (fJ/V·m) - will be converted to SI
     * @param tOx Oxide barrier thickness (nm) - will be converted to m
     */
    public VCMA(Real k0, Real xi_fJ_Vm, Real tOx_nm) {
        this.baseAnisotropy = k0;
        this.vcmaCoefficient = xi_fJ_Vm.multiply(Real.of(1e-15)); // fJ to J
        this.oxideThickness = tOx_nm.multiply(Real.of(1e-9)); // nm to m
    }

    /**
     * Calculates effective anisotropy under applied voltage.
     * @param voltage Applied voltage (V)
     * @return Effective anisotropy K_eff (J/m³)
     */
    public Real getEffectiveAnisotropy(Real voltage) {
        // K_eff = K_0 + ξ * V / t_ox
        Real deltaK = vcmaCoefficient.multiply(voltage).divide(oxideThickness);
        return baseAnisotropy.add(deltaK);
    }

    /**
     * Calculates the voltage required to reduce anisotropy to zero.
     * This is the critical voltage for precessional switching.
     * @return Critical voltage (V)
     */
    public Real getCriticalVoltage() {
        // V_c = -K_0 * t_ox / ξ
        return baseAnisotropy.negate().multiply(oxideThickness).divide(vcmaCoefficient);
    }

    /**
     * Calculates VCMA-assisted switching energy.
     * @param voltage Pulse voltage
     * @param pulseDuration Pulse duration (s)
     * @param capacitance Device capacitance (F)
     * @return Energy per switching event (J)
     */
    public Real getSwitchingEnergy(Real voltage, Real pulseDuration, Real capacitance) {
        // E = C * V² (simplified, ignoring leakage)
        return capacitance.multiply(voltage.pow(2));
    }

    /**
     * Calculates effective anisotropy field under voltage.
     * @param voltage Applied voltage
     * @param ms Saturation magnetization
     * @return H_K effective (A/m)
     */
    public Real getAnisotropyField(Real voltage, Real ms) {
        Real kEff = getEffectiveAnisotropy(voltage);
        Real mu0 = Real.of(1.2566370614e-6);
        // H_K = 2 * K_eff / (μ₀ * M_s)
        return kEff.multiply(Real.TWO).divide(mu0.multiply(ms));
    }

    // Getters
    public Real getBaseAnisotropy() { return baseAnisotropy; }
    public Real getVcmaCoefficient() { return vcmaCoefficient; }
    public Real getOxideThickness() { return oxideThickness; }

    // Preset configurations
    public static VCMA createCoFeBMgO() {
        // Typical CoFeB/MgO: K_0 = 1 MJ/m³, ξ = 50 fJ/V·m, t_ox = 1 nm
        return new VCMA(Real.of(1e6), Real.of(50), Real.of(1.0));
    }

    public static VCMA createHighEfficiency() {
        // High-ξ system: ξ = 100 fJ/V·m
        return new VCMA(Real.of(0.5e6), Real.of(100), Real.of(0.8));
    }
}
