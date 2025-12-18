package org.jscience.engineering.control;

/**
 * Control systems analysis.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ControlSystems {

    /**
     * PID controller output.
     * u(t) = Kp*e + Ki*∫e*dt + Kd*de/dt
     */
    public static class PIDController {
        private final double Kp, Ki, Kd;
        private double integral = 0;
        private double previousError = 0;
        private double dt;

        public PIDController(double Kp, double Ki, double Kd, double dt) {
            this.Kp = Kp;
            this.Ki = Ki;
            this.Kd = Kd;
            this.dt = dt;
        }

        public double compute(double setpoint, double processVariable) {
            double error = setpoint - processVariable;

            // Proportional
            double P = Kp * error;

            // Integral
            integral += error * dt;
            double I = Ki * integral;

            // Derivative
            double derivative = (error - previousError) / dt;
            double D = Kd * derivative;

            previousError = error;

            return P + I + D;
        }

        public void reset() {
            integral = 0;
            previousError = 0;
        }
    }

    /**
     * First-order system step response.
     * y(t) = K * (1 - e^(-t/τ))
     */
    public static double firstOrderStepResponse(double K, double tau, double t) {
        return K * (1 - Math.exp(-t / tau));
    }

    /**
     * Second-order system step response (underdamped).
     * y(t) = 1 - (1/√(1-ζ²)) * e^(-ζωn*t) * sin(ωd*t + φ)
     */
    public static double secondOrderStepResponse(double wn, double zeta, double t) {
        if (zeta >= 1) {
            // Overdamped or critically damped - simplified
            return 1 - Math.exp(-wn * t);
        }
        double wd = wn * Math.sqrt(1 - zeta * zeta);
        double phi = Math.acos(zeta);
        return 1 - (1 / Math.sqrt(1 - zeta * zeta))
                * Math.exp(-zeta * wn * t) * Math.sin(wd * t + phi);
    }

    /**
     * Rise time estimate for second-order system.
     * t_r ≈ (π - φ) / ωd where φ = acos(ζ)
     */
    public static double riseTime(double wn, double zeta) {
        double wd = wn * Math.sqrt(1 - zeta * zeta);
        double phi = Math.acos(zeta);
        return (Math.PI - phi) / wd;
    }

    /**
     * Peak time for second-order underdamped system.
     * t_p = π / ωd
     */
    public static double peakTime(double wn, double zeta) {
        double wd = wn * Math.sqrt(1 - zeta * zeta);
        return Math.PI / wd;
    }

    /**
     * Overshoot percentage.
     * %OS = 100 * e^(-ζπ/√(1-ζ²))
     */
    public static double overshoot(double zeta) {
        return 100 * Math.exp(-zeta * Math.PI / Math.sqrt(1 - zeta * zeta));
    }

    /**
     * Settling time (2% criterion).
     * t_s ≈ 4 / (ζ * ωn)
     */
    public static double settlingTime(double wn, double zeta) {
        return 4.0 / (zeta * wn);
    }

    /**
     * Steady-state error for type 0 system with step input.
     * e_ss = 1 / (1 + Kp)
     */
    public static double steadyStateError(double Kp) {
        return 1.0 / (1 + Kp);
    }

    /**
     * Phase margin from open-loop gain and phase at crossover.
     * PM = 180° + phase at gain crossover
     */
    public static double phaseMargin(double phaseAtCrossover) {
        return 180 + phaseAtCrossover;
    }

    /**
     * Gain margin in dB.
     * GM = -20*log10(gain at phase crossover)
     */
    public static double gainMargin(double gainAtPhaseCrossover) {
        return -20 * Math.log10(gainAtPhaseCrossover);
    }

    /**
     * Bode magnitude for first-order system.
     * |H(jω)| = K / sqrt(1 + (ω/ωc)²)
     */
    public static double firstOrderMagnitude(double K, double omega, double omegaCutoff) {
        double ratio = omega / omegaCutoff;
        return K / Math.sqrt(1 + ratio * ratio);
    }

    /**
     * Bode phase for first-order system.
     * ∠H(jω) = -atan(ω/ωc)
     */
    public static double firstOrderPhase(double omega, double omegaCutoff) {
        return -Math.toDegrees(Math.atan(omega / omegaCutoff));
    }
}
