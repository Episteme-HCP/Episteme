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
 * Spin Seebeck Effect (SSE) for thermal spintronics.
 * <p>
 * Temperature gradients across magnetic materials generate spin currents
 * that can be detected as voltage via the Inverse Spin Hall Effect.
 * </p>
 * 
 * <h3>Physics</h3>
 * <p>
 * $$ J_s = S_{SSE} \nabla T $$
 * $$ V_{ISHE} = \theta_{SH} \lambda_{sf} J_s w $$
 * </p>
 * 
 * <h3>References</h3>
 * <ul>
 * <li><b>Uchida, K. et al.</b> (2008). "Observation of the spin Seebeck effect". 
 *     <i>Nature</i>, 455, 778-781. 
 *     <a href="https://doi.org/10.1038/nature07321">DOI: 10.1038/nature07321</a></li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SpinSeebeckEffect {

    private final Real sseCoefficient;      // S_SSE (A/m/K)
    private final SpinOrbitTorque.HeavyMetal detector; // For ISHE detection
    private final Real detectorThickness;   // Heavy metal thickness (m)
    private final Real detectorWidth;       // Width for voltage measurement (m)

    public SpinSeebeckEffect(Real sseCoeff, SpinOrbitTorque.HeavyMetal detector, 
                             Real detectorThickness, Real detectorWidth) {
        this.sseCoefficient = sseCoeff;
        this.detector = detector;
        this.detectorThickness = detectorThickness;
        this.detectorWidth = detectorWidth;
    }

    /** Returns the detector thickness in meters. */
    public Real getDetectorThickness() {
        return detectorThickness;
    }

    /**
     * Calculates spin current density from temperature gradient.
     * @param temperatureGradient ∇T (K/m)
     * @return Spin current density (A/m²)
     */
    public Real calculateSpinCurrent(Real temperatureGradient) {
        return sseCoefficient.multiply(temperatureGradient);
    }

    /**
     * Calculates ISHE voltage from spin current.
     * @param spinCurrent Spin current density (A/m²)
     * @return Voltage (V)
     */
    public Real calculateISHEVoltage(Real spinCurrent) {
        // V_ISHE = θ_SH * λ_sf * J_s * w
        return detector.spinHallAngle
                .multiply(detector.spinDiffusionLength)
                .multiply(spinCurrent)
                .multiply(detectorWidth);
    }

    /**
     * Full SSE measurement: ∇T → J_s → V_ISHE
     */
    public Real measureVoltage(Real temperatureGradient) {
        Real js = calculateSpinCurrent(temperatureGradient);
        return calculateISHEVoltage(js);
    }

    /**
     * Calculates SSE power factor for thermoelectric efficiency.
     */
    public Real getPowerFactor(Real temperatureGradient, Real resistivity, Real crossSection) {
        Real voltage = measureVoltage(temperatureGradient);
        Real resistance = resistivity.multiply(detectorWidth).divide(crossSection);
        // P = V² / R per unit temperature gradient squared
        return voltage.pow(2).divide(resistance);
    }

    // Factory methods for common materials
    public static SpinSeebeckEffect createYIGPt() {
        // YIG (Yttrium Iron Garnet) + Pt detector
        return new SpinSeebeckEffect(
            Real.of(1e4),  // S_SSE typical for YIG
            SpinOrbitTorque.HeavyMetal.PLATINUM,
            Real.of(10e-9), // 10 nm Pt
            Real.of(5e-3)   // 5 mm width
        );
    }

    public static SpinSeebeckEffect createNiFePt() {
        // NiFe + Pt (metallic ferromagnet)
        return new SpinSeebeckEffect(
            Real.of(1e3),
            SpinOrbitTorque.HeavyMetal.PLATINUM,
            Real.of(5e-9),
            Real.of(1e-3)
        );
    }
}
