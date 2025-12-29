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

package org.jscience.physics.astronomy.mechanics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Orbital maneuvers and transfer orbits.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TransferOrbit {

    public static final Real MU_SUN = Real.of(1.32712440018e20);
    public static final Real AU = Real.of(1.495978707e11);

    /** Hohmann transfer delta-V */
    public static Real hohmannTransferDeltaV(Real r1, Real r2, Real mu) {
        Real v1 = mu.divide(r1).sqrt();
        Real v2 = mu.divide(r2).sqrt();
        Real a_transfer = r1.add(r2).divide(Real.TWO);

        Real v_transfer1 = mu.multiply(Real.TWO.divide(r1).subtract(Real.ONE.divide(a_transfer))).sqrt();
        Real v_transfer2 = mu.multiply(Real.TWO.divide(r2).subtract(Real.ONE.divide(a_transfer))).sqrt();

        Real dv1 = v_transfer1.subtract(v1).abs();
        Real dv2 = v2.subtract(v_transfer2).abs();

        return dv1.add(dv2);
    }

    /** Hohmann transfer time: t = π * √(a³ / μ) */
    public static Real hohmannTransferTime(Real r1, Real r2, Real mu) {
        Real a_transfer = r1.add(r2).divide(Real.TWO);
        return Real.PI.multiply(a_transfer.pow(3).divide(mu).sqrt());
    }

    /** Bi-elliptic transfer delta-V */
    public static Real biEllipticTransferDeltaV(Real r1, Real r2, Real rb, Real mu) {
        Real v1 = mu.divide(r1).sqrt();
        Real v2 = mu.divide(r2).sqrt();

        Real a1 = r1.add(rb).divide(Real.TWO);
        Real v_t1_p = mu.multiply(Real.TWO.divide(r1).subtract(Real.ONE.divide(a1))).sqrt();
        Real v_t1_a = mu.multiply(Real.TWO.divide(rb).subtract(Real.ONE.divide(a1))).sqrt();

        Real a2 = r2.add(rb).divide(Real.TWO);
        Real v_t2_a = mu.multiply(Real.TWO.divide(rb).subtract(Real.ONE.divide(a2))).sqrt();
        Real v_t2_p = mu.multiply(Real.TWO.divide(r2).subtract(Real.ONE.divide(a2))).sqrt();

        Real dv1 = v_t1_p.subtract(v1).abs();
        Real dv2 = v_t2_a.subtract(v_t1_a).abs();
        Real dv3 = v_t2_p.subtract(v2).abs();

        return dv1.add(dv2).add(dv3);
    }

    /** Synodic period: 1/S = |1/P1 - 1/P2| */
    public static Real synodicPeriod(Real P1, Real P2) {
        return Real.ONE.divide(P1.inverse().subtract(P2.inverse()).abs());
    }

    /** Launch window interval */
    public static Real launchWindowInterval(Real periodEarth, Real periodTarget) {
        return synodicPeriod(periodEarth, periodTarget);
    }
}
