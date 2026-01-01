/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.classical.thermodynamics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Thermodynamic property calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermodynamicProperties {

    /** Gas constant R in J/(molÃ‚Â·K) */
    public static final Real R_J = Real.of(8.314462618);
    /** Gas constant R in kJ/(molÃ‚Â·K) */
    public static final Real R_KJ = Real.of(0.008314462618);
    /** Gas constant R in cal/(molÃ‚Â·K) */
    public static final Real R_CAL = Real.of(1.98720425864);

    /**
     * Calculates Gibbs free energy change. ÃŽâ€G = ÃŽâ€H - TÃ‚Â·ÃŽâ€S
     * 
     * @param deltaH Enthalpy change (kJ/mol)
     * @param deltaS Entropy change (J/molÃ‚Â·K)
     * @param T      Temperature (K)
     * @return ÃŽâ€G in kJ/mol
     */
    public static Real gibbsEnergy(Real deltaH, Real deltaS, Real T) {
        return deltaH.subtract(T.multiply(deltaS).divide(Real.of(1000)));
    }

    /** Equilibrium constant from Gibbs energy. K = exp(-ÃŽâ€GÃ‚Â°/(RÃ‚Â·T)) */
    public static Real equilibriumConstant(Real deltaG, Real T) {
        return deltaG.negate().divide(R_KJ.multiply(T)).exp();
    }

    /** Gibbs energy from equilibrium constant. ÃŽâ€GÃ‚Â° = -RÃ‚Â·TÃ‚Â·ln(K) */
    public static Real gibbsFromK(Real K, Real T) {
        return R_KJ.negate().multiply(T).multiply(K.log());
    }

    /**
     * Reaction quotient Q = [products]^ÃŽÂ½ / [reactants]^ÃŽÂ½
     */
    public static Real reactionQuotient(Real[] productConc, int[] productCoeff,
            Real[] reactantConc, int[] reactantCoeff) {
        Real numerator = Real.ONE;
        for (int i = 0; i < productConc.length; i++) {
            numerator = numerator.multiply(productConc[i].pow(Real.of(productCoeff[i])));
        }
        Real denominator = Real.ONE;
        for (int i = 0; i < reactantConc.length; i++) {
            denominator = denominator.multiply(reactantConc[i].pow(Real.of(reactantCoeff[i])));
        }
        return numerator.divide(denominator);
    }

    /** Returns true if spontaneous (ÃŽâ€G < 0) */
    public static boolean isSpontaneous(Real deltaG) {
        return deltaG.compareTo(Real.ZERO) < 0;
    }

    /** Equilibrium temperature T_eq = ÃŽâ€H / ÃŽâ€S */
    public static Real equilibriumTemperature(Real deltaH, Real deltaS) {
        return deltaH.multiply(Real.of(1000)).divide(deltaS);
    }

    /** Van't Hoff equation: K2 = K1 * exp(-ÃŽâ€H/R * (1/T2 - 1/T1)) */
    public static Real vantHoff(Real K1, Real T1, Real T2, Real deltaH) {
        Real exponent = deltaH.negate().divide(R_KJ).multiply(T2.inverse().subtract(T1.inverse()));
        return K1.multiply(exponent.exp());
    }

    /** Heat transfer q = nÃ‚Â·CpÃ‚Â·ÃŽâ€T */
    public static Real heatTransfer(Real n, Real Cp, Real deltaT) {
        return n.multiply(Cp).multiply(deltaT);
    }

    /** Standard enthalpy of reaction from formation enthalpies */
    public static Real reactionEnthalpy(Real[] productFormH, int[] productCoeff,
            Real[] reactantFormH, int[] reactantCoeff) {
        Real sum = Real.ZERO;
        for (int i = 0; i < productFormH.length; i++) {
            sum = sum.add(Real.of(productCoeff[i]).multiply(productFormH[i]));
        }
        for (int i = 0; i < reactantFormH.length; i++) {
            sum = sum.subtract(Real.of(reactantCoeff[i]).multiply(reactantFormH[i]));
        }
        return sum;
    }
}


