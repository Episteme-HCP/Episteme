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

package org.jscience.chemistry.biochemistry;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Enzyme kinetics calculations (Michaelis-Menten).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EnzymeKinetics {

    /**
     * Michaelis-Menten equation.
     * v = (Vmax Ã‚Â· [S]) / (Km + [S])
     * 
     * @param S    Substrate concentration
     * @param Vmax Maximum velocity
     * @param Km   Michaelis constant
     * @return Reaction velocity
     */
    public static Real michaelismenten(Real S, Real Vmax, Real Km) {
        return Vmax.multiply(S).divide(Km.add(S));
    }

    /**
     * Lineweaver-Burk (double reciprocal) transformation.
     * 1/v = (Km/Vmax) Ã‚Â· (1/[S]) + 1/Vmax
     * Returns array with [slope, intercept].
     */
    public static Real[] lineweaverBurkParams(Real Vmax, Real Km) {
        Real slope = Km.divide(Vmax);
        Real intercept = Real.ONE.divide(Vmax);
        return new Real[] { slope, intercept };
    }

    /**
     * Calculates Vmax and Km from Lineweaver-Burk plot slope and intercept.
     * Returns array with [Vmax, Km].
     */
    public static Real[] vMaxKmFromLB(Real slope, Real intercept) {
        Real Vmax = Real.ONE.divide(intercept);
        Real Km = slope.multiply(Vmax);
        return new Real[] { Vmax, Km };
    }

    /**
     * Competitive inhibition: apparent Km increased.
     * Km_app = Km Ã‚Â· (1 + [I]/Ki)
     */
    public static Real competitiveKm(Real Km, Real I, Real Ki) {
        return Km.multiply(Real.ONE.add(I.divide(Ki)));
    }

    /**
     * Non-competitive inhibition: Vmax decreased.
     * Vmax_app = Vmax / (1 + [I]/Ki)
     */
    public static Real noncompetitiveVmax(Real Vmax, Real I, Real Ki) {
        return Vmax.divide(Real.ONE.add(I.divide(Ki)));
    }

    /**
     * Uncompetitive inhibition: both Km and Vmax decreased.
     * Returns array with [Vmax_app, Km_app].
     */
    public static Real[] uncompetitiveParams(Real Vmax, Real Km, Real I, Real Ki) {
        Real factor = Real.ONE.add(I.divide(Ki));
        return new Real[] { Vmax.divide(factor), Km.divide(factor) };
    }

    /**
     * Turnover number (kcat): reactions per enzyme per second.
     * kcat = Vmax / [E]_total
     */
    public static Real turnoverNumber(Real Vmax, Real enzymeConcentration) {
        return Vmax.divide(enzymeConcentration);
    }

    /**
     * Catalytic efficiency.
     * ÃŽÂ· = kcat / Km
     * Higher is better (diffusion limit ~10Ã¢ÂÂ¸-10Ã¢ÂÂ¹ MÃ¢ÂÂ»Ã‚Â¹sÃ¢ÂÂ»Ã‚Â¹)
     */
    public static Real catalyticEfficiency(Real kcat, Real Km) {
        return kcat.divide(Km);
    }

    /**
     * Hill equation for cooperative binding.
     * ÃŽÂ¸ = [S]^n / (K + [S]^n)
     * 
     * @param S Substrate concentration
     * @param K Apparent dissociation constant
     * @param n Hill coefficient (n>1: positive cooperativity)
     * @return Fractional saturation
     */
    public static Real hillEquation(Real S, Real K, Real n) {
        Real Sn = S.pow(n);
        return Sn.divide(K.add(Sn));
    }
}


