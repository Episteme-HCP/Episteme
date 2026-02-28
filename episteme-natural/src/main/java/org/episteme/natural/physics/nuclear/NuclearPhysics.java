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

package org.episteme.natural.physics.nuclear;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.PhysicalConstants;

/**
 * Nuclear physics calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NuclearPhysics {

    /** Mass of proton (kg) */
    public static final Real MASS_PROTON = PhysicalConstants.m_p;
    
    /** Mass of neutron (kg) */
    public static final Real MASS_NEUTRON = PhysicalConstants.m_n;
    
    /** Speed of light (m/s) */
    public static final Real C = PhysicalConstants.c;
    
    /**
     * Calculates the binding energy of a nucleus (Semi-Empirical Mass Formula / Weizsaecker).
     * B = aV*A - aS*A^(2/3) - aC*Z^2/A^(1/3) - aA*(A-2Z)^2/A + delta(A,Z)
     * 
     * @param A Mass number
     * @param Z Atomic number
     * @return Binding energy in Joules (approx)
     */
    public static Real bindingEnergySEMF(int A, int Z) {
        if (A <= 0) return Real.ZERO;
        
        // Coefficients in MeV
        Real aV = Real.of(15.75);
        Real aS = Real.of(17.8);
        Real aC = Real.of(0.711);
        Real aA = Real.of(23.7);
        Real aP = Real.of(11.18);
        
        Real rA = Real.of(A);
        Real rZ = Real.of(Z);
        
        Real vol = aV.multiply(rA);
        Real surf = aS.multiply(rA.pow(2.0/3.0));
        Real coul = aC.multiply(rZ.multiply(rZ)).divide(rA.pow(1.0/3.0));
        Real asym = aA.multiply(rA.subtract(rZ.multiply(Real.of(2))).pow(2)).divide(rA);
        
        Real pairing = Real.ZERO;
        if (A % 2 != 0) {
            pairing = Real.ZERO;
        } else if (Z % 2 == 0) {
            // Even-Even
            pairing = aP.divide(rA.sqrt());
        } else {
            // Odd-Odd
            pairing = aP.negate().divide(rA.sqrt());
        }
        
        Real energyMeV = vol.subtract(surf).subtract(coul).subtract(asym).add(pairing);
        
        // Convert MeV to Joules: 1 eV = 1.60218e-19 J
        return energyMeV.multiply(Real.of(1.60218e-13)); 
    }
    
    /**
     * Calculates decay constant from half-life.
     * lambda = ln(2) / T_half
     */
    public static Real decayConstant(Real halfLife) {
        return Real.of(Math.log(2)).divide(halfLife);
    }
    
    /**
     * Calculates remaining quantity after time t.
     * N(t) = N0 * exp(-lambda * t)
     */
    public static Real radioactiveDecay(Real N0, Real decayConstant, Real time) {
        return N0.multiply(decayConstant.negate().multiply(time).exp());
    }
}
