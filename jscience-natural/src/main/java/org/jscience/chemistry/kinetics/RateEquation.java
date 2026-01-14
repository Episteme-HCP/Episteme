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

package org.jscience.chemistry.kinetics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Chemical kinetics calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RateEquation {

    /** Rate law order enumeration. */
    public enum Order {
        ZERO(0), FIRST(1), SECOND(2);

        private final int value;

        Order(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Calculates reaction rate from rate constant and concentrations.
     * rate = k * [A]^a * [B]^b * ...
     */
    public static Real rate(Real k, Real[] concentrations, Real[] orders) {
        Real rate = k;
        for (int i = 0; i < concentrations.length; i++) {
            rate = rate.multiply(concentrations[i].pow(orders[i]));
        }
        return rate;
    }

    /** Zero-order integrated rate law. [A] = [A]Ã¢â€šâ‚¬ - kÃ‚Â·t */
    public static Real zeroOrderConcentration(Real A0, Real k, Real t) {
        Real result = A0.subtract(k.multiply(t));
        return result.compareTo(Real.ZERO) < 0 ? Real.ZERO : result;
    }

    /** First-order integrated rate law. [A] = [A]Ã¢â€šâ‚¬ Ã‚Â· exp(-kÃ‚Â·t) */
    public static Real firstOrderConcentration(Real A0, Real k, Real t) {
        return A0.multiply(k.negate().multiply(t).exp());
    }

    /** Second-order integrated rate law. 1/[A] = 1/[A]Ã¢â€šâ‚¬ + kÃ‚Â·t */
    public static Real secondOrderConcentration(Real A0, Real k, Real t) {
        return Real.ONE.divide(A0.inverse().add(k.multiply(t)));
    }

    /** Calculates half-life for different reaction orders. */
    public static Real halfLife(Real A0, Real k, Order order) {
        switch (order) {
            case ZERO:
                return A0.divide(Real.TWO.multiply(k));
            case FIRST:
                return Real.LN2.divide(k);
            case SECOND:
                return Real.ONE.divide(k.multiply(A0));
            default:
                throw new IllegalArgumentException("Unknown order");
        }
    }

    /**
     * Arrhenius equation: k = A Ã‚Â· exp(-Ea/(RÃ‚Â·T))
     * 
     * @param A  Pre-exponential factor
     * @param Ea Activation energy (kJ/mol)
     * @param T  Temperature (K)
     */
    public static Real arrhenius(Real A, Real Ea, Real T) {
        Real R = Real.of(0.008314); // kJ/(molÃ‚Â·K)
        return A.multiply(Ea.negate().divide(R.multiply(T)).exp());
    }

    /** Calculates activation energy from two rate constants. */
    public static Real activationEnergy(Real k1, Real T1, Real k2, Real T2) {
        Real R = Real.of(0.008314);
        return R.multiply(k2.divide(k1).log()).divide(T1.inverse().subtract(T2.inverse()));
    }

    /**
     * Eyring equation (transition state theory).
     * k = (k_BÃ‚Â·T/h) Ã‚Â· exp(-ÃŽâ€GÃ¢â‚¬Â¡/(RÃ‚Â·T))
     */
    public static Real eyring(Real T, Real deltaG_act) {
        Real kB = Real.of(1.380649e-23);
        Real h = Real.of(6.62607015e-34);
        Real R = Real.of(8.314);
        Real prefactor = kB.multiply(T).divide(h);
        return prefactor.multiply(deltaG_act.multiply(Real.of(1000)).negate().divide(R.multiply(T)).exp());
    }
}


