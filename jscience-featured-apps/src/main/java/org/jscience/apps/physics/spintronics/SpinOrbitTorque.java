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

    // ========== PHASE 2 ADDITIONS ==========

    /**
     * Materials exhibiting Orbital Hall Effect (OHE).
     * OHE generates orbital angular momentum current without requiring heavy elements.
     * 
     * @see <a href="https://doi.org/10.1038/s41563-021-01151-2">OHE in light metals, Nature Materials 2022</a>
     */
    public static class OrbitalHallMaterial {
        public final String name;
        public final Real orbitalHallAngle;  // θ_OH (dimensionless)
        public final Real spinOrbitConversion; // η_SO (orbital-to-spin conversion)
        public final Real resistivity;

        public OrbitalHallMaterial(String name, Real oha, Real soc, Real rho) {
            this.name = name;
            this.orbitalHallAngle = oha;
            this.spinOrbitConversion = soc;
            this.resistivity = rho;
        }

        public static final OrbitalHallMaterial COPPER = new OrbitalHallMaterial("Cu", Real.of(0.02), Real.of(0.1), Real.of(17e-9));
        public static final OrbitalHallMaterial TITANIUM = new OrbitalHallMaterial("Ti", Real.of(0.05), Real.of(0.15), Real.of(420e-9));
        public static final OrbitalHallMaterial MANGANESE = new OrbitalHallMaterial("Mn", Real.of(0.08), Real.of(0.2), Real.of(1440e-9));
        public static final OrbitalHallMaterial CHROMIUM = new OrbitalHallMaterial("Cr", Real.of(0.06), Real.of(0.12), Real.of(125e-9));
    }

    /**
     * Rashba-induced field-like torque at the interface.
     * $$ \tau_{Rashba} = \frac{\alpha_R m_e}{\hbar e} J_c (\mathbf{m} \times \hat{y}) $$
     * 
     * @param currentDensity Charge current in x-direction
     * @param layer Target ferromagnetic layer
     * @param rashbaParameter α_R (eV·Å) - typically 0.1-1.0
     */
    public Real[] calculateRashbaTorque(Real currentDensity, FerromagneticLayer layer, Real rashbaParameter) {
        Real[] m = layer.getMagnetization();
        Real[] yAxis = {Real.ZERO, Real.ONE, Real.ZERO};

        // Convert Rashba parameter from eV·Å to SI
        Real alphaR_SI = rashbaParameter.multiply(Real.of(1.602e-19 * 1e-10)); // eV·Å to J·m
        Real me = Real.of(9.109e-31);
        Real hbar = Real.of(1.054571817e-34);
        Real e = Real.of(1.602176634e-19);
        Real ms = layer.getMaterial().getSaturationMagnetization();
        Real tFM = layer.getThickness();

        // τ_R = (α_R * m_e) / (ℏ * e * M_s * t) * J_c
        Real prefactor = alphaR_SI.multiply(me)
                .divide(hbar.multiply(e).multiply(ms).multiply(tFM))
                .multiply(currentDensity);

        Real[] torque = crossProduct(m, yAxis);
        return new Real[] {
            torque[0].multiply(prefactor),
            torque[1].multiply(prefactor),
            torque[2].multiply(prefactor)
        };
    }

    /**
     * Orbital Hall Effect (OHE) torque.
     * Similar structure to SHE but with orbital-to-spin conversion at interface.
     */
    public static Real[] calculateOrbitalTorque(Real currentDensity, FerromagneticLayer layer, 
                                                 OrbitalHallMaterial oheMaterial, Real oheThickness,
                                                 Real[] currentDirection) {
        Real[] sigma = { Real.ZERO.subtract(currentDirection[1]), currentDirection[0], Real.ZERO };
        Real[] m = layer.getMagnetization();

        Real hbar = Real.of(1.054571817e-34);
        Real e = Real.of(1.602176634e-19);
        Real ms = layer.getMaterial().getSaturationMagnetization();
        Real tFM = layer.getThickness();

        // Effective angle = θ_OH * η_SO (orbital-to-spin conversion efficiency)
        Real effectiveAngle = oheMaterial.orbitalHallAngle.multiply(oheMaterial.spinOrbitConversion);

        Real tau0 = hbar.divide(Real.TWO.multiply(e))
                .multiply(effectiveAngle)
                .multiply(currentDensity)
                .divide(ms.multiply(tFM));

        Real[] sigmaCrossM = crossProduct(sigma, m);
        Real[] torque = crossProduct(m, sigmaCrossM);

        return new Real[] {
            torque[0].multiply(tau0),
            torque[1].multiply(tau0),
            torque[2].multiply(tau0)
        };
    }

    /**
     * Combined SOT + Exchange Bias for field-free switching.
     * Uses effective field from AFM layer to break symmetry.
     * 
     * @param currentDensity Charge current density
     * @param layer FM layer
     * @param currentDirection Current direction
     * @param afm Antiferromagnetic material providing exchange bias
     * @param biasDirection Direction of exchange bias field
     * @return Total effective field including SOT equivalent + bias
     */
    public Real[] calculateFieldFreeTorque(Real currentDensity, FerromagneticLayer layer, 
                                           Real[] currentDirection,
                                           AntiferromagneticMaterial afm, Real[] biasDirection) {
        // Regular SOT components
        Real[] tauDL = calculateDampingLikeTorque(currentDensity, layer, currentDirection);
        Real[] tauFL = calculateFieldLikeTorque(currentDensity, layer, currentDirection);

        // Exchange bias contribution
        Real hEB = afm.getExchangeBiasField();
        Real[] hBias = {
            biasDirection[0].multiply(hEB),
            biasDirection[1].multiply(hEB),
            biasDirection[2].multiply(hEB)
        };

        // Total torque = SOT + m × H_bias
        Real[] m = layer.getMagnetization();
        Real[] biasTorque = crossProduct(m, hBias);

        return new Real[] {
            tauDL[0].add(tauFL[0]).add(biasTorque[0]),
            tauDL[1].add(tauFL[1]).add(biasTorque[1]),
            tauDL[2].add(tauFL[2]).add(biasTorque[2])
        };
    }

    /**
     * SOT switching probability for a given pulse.
     * Uses Néel-Brown thermal activation model.
     * 
     * @param currentDensity Current density
     * @param pulseDuration Pulse duration (s)
     * @param temperature Temperature (K)
     * @param layer Target layer
     * @param attemptFrequency f_0, typically 1e9-1e10 Hz
     * @return Switching probability [0, 1]
     */
    public double calculateSwitchingProbability(Real currentDensity, Real pulseDuration, 
                                                 Real temperature, FerromagneticLayer layer,
                                                 Real attemptFrequency) {
        // Energy barrier reduction by SOT: ΔE = E_0 * (1 - J/J_c)^2
        Real jC = Real.of(5e11); // Critical current density (simplified)
        Real ratio = currentDensity.divide(jC);
        
        if (ratio.compareTo(Real.ONE) >= 0) {
            return 1.0; // Deterministic switching above J_c
        }

        // E_0 = K_u * V (anisotropy energy barrier)
        Real ku = Real.of(5e5); // Anisotropy constant J/m³
        Real volume = layer.getThickness().multiply(Real.of(100e-9 * 100e-9)); // 100x100 nm pillar
        Real E0 = ku.multiply(volume);

        // Reduced barrier
        Real oneMinusRatio = Real.ONE.subtract(ratio);
        Real deltaE = E0.multiply(oneMinusRatio.pow(2));

        // Thermal stability factor
        Real kB = Real.of(1.380649e-23);
        Real thermalFactor = deltaE.divide(kB.multiply(temperature));

        // Switching probability: P = 1 - exp(-f_0 * t * exp(-ΔE/kT))
        double exponent = -attemptFrequency.doubleValue() * pulseDuration.doubleValue() 
                          * Math.exp(-thermalFactor.doubleValue());
        
        return 1.0 - Math.exp(exponent);
    }
}
