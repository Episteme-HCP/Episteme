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

package org.episteme.natural.engineering.control;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Angle;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.core.measure.quantity.Frequency;
import org.episteme.core.measure.quantity.Time;

/**
 * Control systems analysis.
 * Modernized to use typed Quantities for Time and Frequency.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ControlSystems {

    private ControlSystems() {}

    /**
     * PID controller output.
     * u(t) = Kp*e + Ki*âˆ«e*dt + Kd*de/dt
     * Operating on abstract mathematical signals (Real).
     */
    public static class PIDController {
        private final Real Kp, Ki, Kd;
        private Real integral = Real.ZERO;
        private Real previousError = Real.ZERO;
        private final Quantity<Time> dt;

        public PIDController(Real Kp, Real Ki, Real Kd, Quantity<Time> dt) {
            this.Kp = Kp;
            this.Ki = Ki;
            this.Kd = Kd;
            this.dt = dt;
        }

        public Real compute(Real setpoint, Real processVariable) {
            Real error = setpoint.subtract(processVariable);
            Real dtVal = dt.to(Units.SECOND).getValue();

            // Proportional
            Real P = Kp.multiply(error);

            // Integral
            integral = integral.add(error.multiply(dtVal));
            Real I = Ki.multiply(integral);

            // Derivative
            Real derivative = error.subtract(previousError).divide(dtVal);
            Real D = Kd.multiply(derivative);

            previousError = error;

            return P.add(I).add(D);
        }

        public void reset() {
            integral = Real.ZERO;
            previousError = Real.ZERO;
        }
    }

    /**
     * First-order system step response.
     * y(t) = K * (1 - e^(-t/Ï„))
     */
    public static Real firstOrderStepResponse(Real K, Quantity<Time> tau, Quantity<Time> t) {
        Real time = t.to(Units.SECOND).getValue();
        Real timeConst = tau.to(Units.SECOND).getValue();
        return K.multiply(Real.ONE.subtract(time.negate().divide(timeConst).exp()));
    }

    /**
     * Second-order system step response (underdamped).
     */
    public static Real secondOrderStepResponse(Quantity<Frequency> wn, Real zeta, Quantity<Time> t) {
        Real w_n = wn.to(Units.HERTZ).getValue().multiply(Real.TWO_PI); // rad/s
        Real time = t.to(Units.SECOND).getValue();

        if (zeta.compareTo(Real.ONE) >= 0) {
            return Real.ONE.subtract(w_n.negate().multiply(time).exp());
        }
        Real oneMinusZetaSq = Real.ONE.subtract(zeta.pow(2));
        Real wd = w_n.multiply(oneMinusZetaSq.sqrt());
        Real phi = zeta.acos();
        Real expTerm = zeta.negate().multiply(w_n).multiply(time).exp();
        Real sinTerm = wd.multiply(time).add(phi).sin();
        return Real.ONE.subtract(Real.ONE.divide(oneMinusZetaSq.sqrt()).multiply(expTerm).multiply(sinTerm));
    }

    /**
     * Rise time estimate for second-order system.
     * tr â‰ˆ (Ï€ - Ï†) / wd
     */
    public static Quantity<Time> riseTime(Quantity<Frequency> wn, Real zeta) {
        Real w_n = wn.to(Units.HERTZ).getValue().multiply(Real.TWO_PI);
        Real wd = w_n.multiply(Real.ONE.subtract(zeta.pow(2)).sqrt());
        Real phi = zeta.acos();
        return Quantities.create(Real.PI.subtract(phi).divide(wd), Units.SECOND);
    }

    /**
     * Peak time for second-order underdamped system.
     * tp = Ï€ / wd
     */
    public static Quantity<Time> peakTime(Quantity<Frequency> wn, Real zeta) {
        Real w_n = wn.to(Units.HERTZ).getValue().multiply(Real.TWO_PI);
        Real wd = w_n.multiply(Real.ONE.subtract(zeta.pow(2)).sqrt());
        return Quantities.create(Real.PI.divide(wd), Units.SECOND);
    }

    /**
     * Overshoot percentage.
     * %OS = 100 * e^(-Î¶Ï€ / sqrt(1-Î¶Â²))
     */
    public static Quantity<Dimensionless> overshoot(Real zeta) {
        Real exponent = zeta.negate().multiply(Real.PI).divide(
                Real.ONE.subtract(zeta.pow(2)).sqrt());
        return Quantities.create(Real.of(100).multiply(exponent.exp()), Units.ONE);
    }

    /**
     * Settling time (2% criterion).
     * ts â‰ˆ 4 / (Î¶ * Ï‰n)
     */
    public static Quantity<Time> settlingTime(Quantity<Frequency> wn, Real zeta) {
        Real w_n = wn.to(Units.HERTZ).getValue().multiply(Real.TWO_PI);
        return Quantities.create(Real.of(4.0).divide(zeta.multiply(w_n)), Units.SECOND);
    }

    /**
     * Steady-state error for type 0 system with step input.
     * ess = 1 / (1 + Kp)
     */
    public static Real steadyStateError(Real Kp) {
        return Real.ONE.divide(Real.ONE.add(Kp));
    }

    /**
     * Phase margin from open-loop gain and phase at crossover.
     */
    public static Quantity<Angle> phaseMargin(Quantity<Angle> phaseAtCrossover) {
        Real phase = phaseAtCrossover.to(Units.DEGREE_ANGLE).getValue();
        return Quantities.create(Real.of(180).add(phase), Units.DEGREE_ANGLE);
    }

    /**
     * Gain margin in dB.
     */
    public static Real gainMargin(Real gainAtPhaseCrossover) {
        return Real.of(-20).multiply(gainAtPhaseCrossover.log10());
    }

    /**
     * Bode magnitude for first-order system.
     * |G(jÏ‰)| = K / sqrt(1 + (Ï‰/Ï‰c)Â²)
     */
    public static Real firstOrderMagnitude(Real K, Quantity<Frequency> omega, Quantity<Frequency> omegaCutoff) {
        Real w = omega.to(Units.HERTZ).getValue();
        Real wc = omegaCutoff.to(Units.HERTZ).getValue();
        Real ratio = w.divide(wc);
        return K.divide(Real.ONE.add(ratio.pow(2)).sqrt());
    }

    /**
     * Bode phase for first-order system.
     * Ï† = -arctan(Ï‰/Ï‰c)
     */
    public static Quantity<Angle> firstOrderPhase(Quantity<Frequency> omega, Quantity<Frequency> omegaCutoff) {
        Real w = omega.to(Units.HERTZ).getValue();
        Real wc = omegaCutoff.to(Units.HERTZ).getValue();
        return Quantities.create(w.divide(wc).atan().negate().toDegrees(), Units.DEGREE_ANGLE);
    }
}



