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

package org.jscience.engineering.control;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Control systems analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ControlSystems {

    /**
     * PID controller output.
     * u(t) = Kp*e + Ki*∫e*dt + Kd*de/dt
     */
    public static class PIDController {
        private final Real Kp, Ki, Kd;
        private Real integral = Real.ZERO;
        private Real previousError = Real.ZERO;
        private final Real dt;

        public PIDController(Real Kp, Real Ki, Real Kd, Real dt) {
            this.Kp = Kp;
            this.Ki = Ki;
            this.Kd = Kd;
            this.dt = dt;
        }

        public Real compute(Real setpoint, Real processVariable) {
            Real error = setpoint.subtract(processVariable);

            // Proportional
            Real P = Kp.multiply(error);

            // Integral
            integral = integral.add(error.multiply(dt));
            Real I = Ki.multiply(integral);

            // Derivative
            Real derivative = error.subtract(previousError).divide(dt);
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
     * y(t) = K * (1 - e^(-t/τ))
     */
    public static Real firstOrderStepResponse(Real K, Real tau, Real t) {
        return K.multiply(Real.ONE.subtract(t.negate().divide(tau).exp()));
    }

    /**
     * Second-order system step response (underdamped).
     */
    public static Real secondOrderStepResponse(Real wn, Real zeta, Real t) {
        if (zeta.compareTo(Real.ONE) >= 0) {
            return Real.ONE.subtract(wn.negate().multiply(t).exp());
        }
        Real oneMinusZetaSq = Real.ONE.subtract(zeta.pow(2));
        Real wd = wn.multiply(oneMinusZetaSq.sqrt());
        Real phi = zeta.acos();
        Real expTerm = zeta.negate().multiply(wn).multiply(t).exp();
        Real sinTerm = wd.multiply(t).add(phi).sin();
        return Real.ONE.subtract(Real.ONE.divide(oneMinusZetaSq.sqrt()).multiply(expTerm).multiply(sinTerm));
    }

    /**
     * Rise time estimate for second-order system.
     */
    public static Real riseTime(Real wn, Real zeta) {
        Real wd = wn.multiply(Real.ONE.subtract(zeta.pow(2)).sqrt());
        Real phi = zeta.acos();
        return Real.PI.subtract(phi).divide(wd);
    }

    /**
     * Peak time for second-order underdamped system.
     */
    public static Real peakTime(Real wn, Real zeta) {
        Real wd = wn.multiply(Real.ONE.subtract(zeta.pow(2)).sqrt());
        return Real.PI.divide(wd);
    }

    /**
     * Overshoot percentage.
     */
    public static Real overshoot(Real zeta) {
        Real exponent = zeta.negate().multiply(Real.PI).divide(
                Real.ONE.subtract(zeta.pow(2)).sqrt());
        return Real.of(100).multiply(exponent.exp());
    }

    /**
     * Settling time (2% criterion).
     */
    public static Real settlingTime(Real wn, Real zeta) {
        return Real.of(4.0).divide(zeta.multiply(wn));
    }

    /**
     * Steady-state error for type 0 system with step input.
     */
    public static Real steadyStateError(Real Kp) {
        return Real.ONE.divide(Real.ONE.add(Kp));
    }

    /**
     * Phase margin from open-loop gain and phase at crossover.
     */
    public static Real phaseMargin(Real phaseAtCrossover) {
        return Real.of(180).add(phaseAtCrossover);
    }

    /**
     * Gain margin in dB.
     */
    public static Real gainMargin(Real gainAtPhaseCrossover) {
        return Real.of(-20).multiply(gainAtPhaseCrossover.log10());
    }

    /**
     * Bode magnitude for first-order system.
     */
    public static Real firstOrderMagnitude(Real K, Real omega, Real omegaCutoff) {
        Real ratio = omega.divide(omegaCutoff);
        return K.divide(Real.ONE.add(ratio.pow(2)).sqrt());
    }

    /**
     * Bode phase for first-order system.
     */
    public static Real firstOrderPhase(Real omega, Real omegaCutoff) {
        return omega.divide(omegaCutoff).atan().negate().toDegrees();
    }
}
