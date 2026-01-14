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
 * Antiferromagnetic materials for exchange bias and AF-spintronics.
 * <p>
 * Antiferromagnets (AFMs) have zero net magnetization but can:
 * <ul>
 *   <li>Pin ferromagnetic layers via exchange bias</li>
 *   <li>Transport spin currents with minimal decay</li>
 *   <li>Switch at THz frequencies (ultrafast)</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class AntiferromagneticMaterial {

    private final String name;
    private final Real neelTemperature;    // T_N (K)
    private final Real exchangeBiasField;  // H_eb (A/m)
    private final Real blockingTemperature; // T_B (K)
    private final Real spinDiffusionLength; // λ_sf (m)

    public AntiferromagneticMaterial(String name, Real tn, Real heb, Real tb, Real sdl) {
        this.name = name;
        this.neelTemperature = tn;
        this.exchangeBiasField = heb;
        this.blockingTemperature = tb;
        this.spinDiffusionLength = sdl;
    }

    public String getName() { return name; }
    public Real getNeelTemperature() { return neelTemperature; }
    public Real getExchangeBiasField() { return exchangeBiasField; }
    public Real getBlockingTemperature() { return blockingTemperature; }
    public Real getSpinDiffusionLength() { return spinDiffusionLength; }

    @Override
    public String toString() { return name; }

    // Common AFM materials
    public static final AntiferromagneticMaterial IrMn = new AntiferromagneticMaterial(
        "IrMn₃", Real.of(690), Real.of(3e4), Real.of(500), Real.of(1e-9));
    
    public static final AntiferromagneticMaterial PtMn = new AntiferromagneticMaterial(
        "PtMn", Real.of(975), Real.of(4e4), Real.of(650), Real.of(0.8e-9));
    
    public static final AntiferromagneticMaterial FeMn = new AntiferromagneticMaterial(
        "FeMn", Real.of(500), Real.of(2e4), Real.of(350), Real.of(1.5e-9));
    
    public static final AntiferromagneticMaterial NiO = new AntiferromagneticMaterial(
        "NiO", Real.of(525), Real.of(1e4), Real.of(400), Real.of(5e-9));
    
    public static final AntiferromagneticMaterial Cr2O3 = new AntiferromagneticMaterial(
        "Cr₂O₃", Real.of(308), Real.of(0.5e4), Real.of(300), Real.of(10e-9));
}
