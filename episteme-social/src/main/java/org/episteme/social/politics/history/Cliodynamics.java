/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.politics.history;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Implements models from Cliodynamics and Structural-Demographic Theory (SDT).
 * These models attempt to explain historical cycles of instability and state breakdown.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Cliodynamics {

    private Cliodynamics() {
        // Utility class
    }

    /**
     * Calculates the Political Stress Index (PSI), a key metric in Structural-Demographic Theory.
     * PSI = (Mass Mobilization Potential) * (Elite Mobilization Potential) * (State Fiscal Distress)
     *
     * @param mmp Mass Mobilization Potential (e.g., related to immiseration, urbanization)
     * @param emp Elite Mobilization Potential (e.g., elite overproduction, intra-elite competition)
     * @param sfd State Fiscal Distress (e.g., debt/revenue ratio)
     * @return The Political Stress Index
     */
    public static Real calculatePoliticalStressIndex(Real mmp, Real emp, Real sfd) {
        return mmp.multiply(emp).multiply(sfd);
    }
    
    /**
     * Estimates Mass Mobilization Potential (MMP).
     * Simplified proxy: Inverse of real wage relative to expectation.
     * 
     * @param realWaage Actual real wage
     * @param expectedWage Culturally expected standard of living
     * @param urbanizationRate Rate of urbanization (multiplier)
     * @return MMP
     */
    public static Real estimateMMP(Real realWage, Real expectedWage, Real urbanizationRate) {
        if (realWage.equals(Real.ZERO)) return Real.of(100.0); // High stress
        
        // (Expected / Real) * Urbanization
        return expectedWage.divide(realWage).multiply(urbanizationRate);
    }
}
