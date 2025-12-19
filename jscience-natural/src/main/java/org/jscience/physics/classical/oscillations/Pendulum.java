package org.jscience.physics.classical.oscillations;

/**
 * Pendulum motion calculations.
 */
public class Pendulum {

    public static final double g = 9.80665; // m/s²

    private Pendulum() {
    }

    /**
     * Simple pendulum period (small angle approximation).
     * T = 2π * sqrt(L/g)
     * 
     * @param lengthMeters Pendulum length
     * @return Period in seconds
     */
    public static double simplePendulumPeriod(double lengthMeters) {
        return 2 * Math.PI * Math.sqrt(lengthMeters / g);
    }

    /**
     * Angular frequency of simple pendulum.
     * ω = sqrt(g/L)
     */
    public static double angularFrequency(double lengthMeters) {
        return Math.sqrt(g / lengthMeters);
    }

    /**
     * Simple pendulum position at time t.
     * θ(t) = θ₀ * cos(ωt + φ)
     * 
     * @param amplitude Initial angle (radians)
     * @param omega     Angular frequency
     * @param time      Time (seconds)
     * @param phase     Initial phase
     * @return Angle in radians
     */
    public static double position(double amplitude, double omega, double time, double phase) {
        return amplitude * Math.cos(omega * time + phase);
    }

    /**
     * Damped pendulum amplitude decay.
     * A(t) = A₀ * exp(-γt)
     * 
     * @param initialAmplitude   A₀
     * @param dampingCoefficient γ
     * @param time               t
     */
    public static double dampedAmplitude(double initialAmplitude, double dampingCoefficient, double time) {
        return initialAmplitude * Math.exp(-dampingCoefficient * time);
    }

    /**
     * Exact period for large angles (elliptic integral approximation).
     * T ≈ T₀ * (1 + θ₀²/16 + ...)
     */
    public static double largeAnglePeriod(double lengthMeters, double amplitudeRadians) {
        double T0 = simplePendulumPeriod(lengthMeters);
        double correction = 1 + Math.pow(amplitudeRadians, 2) / 16
                + 11 * Math.pow(amplitudeRadians, 4) / 3072;
        return T0 * correction;
    }

    /**
     * Potential energy at angle θ.
     * U = mgL(1 - cos(θ))
     */
    public static double potentialEnergy(double mass, double lengthMeters, double angleRadians) {
        return mass * g * lengthMeters * (1 - Math.cos(angleRadians));
    }

    /**
     * Kinetic energy at angle θ with angular velocity ω.
     * KE = 0.5 * m * L² * ω²
     */
    public static double kineticEnergy(double mass, double lengthMeters, double angularVelocity) {
        return 0.5 * mass * lengthMeters * lengthMeters * angularVelocity * angularVelocity;
    }
}
