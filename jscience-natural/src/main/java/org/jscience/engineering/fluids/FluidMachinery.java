package org.jscience.engineering.fluids;

/**
 * Fluid machinery calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class FluidMachinery {

    /** Water density (kg/m³) */
    public static final double RHO_WATER = 1000;

    /** Standard gravity (m/s²) */
    public static final double G = 9.80665;

    /**
     * Pump hydraulic power.
     * P_h = ρ * g * Q * H
     * 
     * @param density  Fluid density (kg/m³)
     * @param flowRate Volume flow rate (m³/s)
     * @param head     Total head (m)
     * @return Hydraulic power (W)
     */
    public static double hydraulicPower(double density, double flowRate, double head) {
        return density * G * flowRate * head;
    }

    /**
     * Pump efficiency.
     * η = P_hydraulic / P_shaft
     */
    public static double pumpEfficiency(double hydraulicPower, double shaftPower) {
        return hydraulicPower / shaftPower;
    }

    /**
     * Affinity laws: flow rate vs speed.
     * Q1/Q2 = N1/N2
     */
    public static double affinityFlowRate(double Q1, double N1, double N2) {
        return Q1 * N2 / N1;
    }

    /**
     * Affinity laws: head vs speed.
     * H1/H2 = (N1/N2)²
     */
    public static double affinityHead(double H1, double N1, double N2) {
        return H1 * Math.pow(N2 / N1, 2);
    }

    /**
     * Affinity laws: power vs speed.
     * P1/P2 = (N1/N2)³
     */
    public static double affinityPower(double P1, double N1, double N2) {
        return P1 * Math.pow(N2 / N1, 3);
    }

    /**
     * Net Positive Suction Head available.
     * NPSH_a = P_atm/ρg + z_s - h_f - P_v/ρg
     * 
     * @param atmosphericPressure P_atm (Pa)
     * @param suctionHeight       z_s (m, negative if below pump)
     * @param frictionLoss        h_f (m)
     * @param vaporPressure       P_v (Pa)
     * @param density             ρ (kg/m³)
     */
    public static double npshAvailable(double atmosphericPressure, double suctionHeight,
            double frictionLoss, double vaporPressure, double density) {
        return atmosphericPressure / (density * G) + suctionHeight
                - frictionLoss - vaporPressure / (density * G);
    }

    /**
     * Cavitation number.
     * σ = (P - P_v) / (0.5 * ρ * V²)
     */
    public static double cavitationNumber(double pressure, double vaporPressure,
            double density, double velocity) {
        return (pressure - vaporPressure) / (0.5 * density * velocity * velocity);
    }

    /**
     * Specific speed (dimensionless).
     * N_s = N * √Q / H^(3/4)
     */
    public static double specificSpeed(double rpm, double flowRate, double head) {
        return rpm * Math.sqrt(flowRate) / Math.pow(head, 0.75);
    }

    /**
     * Turbine power output.
     * P = η * ρ * g * Q * H
     */
    public static double turbinePower(double efficiency, double density,
            double flowRate, double head) {
        return efficiency * density * G * flowRate * head;
    }

    /**
     * Pelton wheel jet velocity.
     * V_jet = C_v * √(2gH)
     */
    public static double peltonJetVelocity(double head, double velocityCoefficient) {
        return velocityCoefficient * Math.sqrt(2 * G * head);
    }

    /**
     * Optimal bucket speed for Pelton wheel.
     * u = V_jet / 2
     */
    public static double peltonOptimalBucketSpeed(double jetVelocity) {
        return jetVelocity / 2;
    }

    /**
     * Francis turbine runner speed.
     * u = π * D * N / 60
     */
    public static double runnerSpeed(double diameter, double rpm) {
        return Math.PI * diameter * rpm / 60;
    }

    /**
     * Compressor isentropic work.
     * W = (γ/(γ-1)) * R * T1 * ((P2/P1)^((γ-1)/γ) - 1)
     */
    public static double isentropicWork(double gamma, double R, double T1,
            double P1, double P2) {
        double exponent = (gamma - 1) / gamma;
        return (gamma / (gamma - 1)) * R * T1 * (Math.pow(P2 / P1, exponent) - 1);
    }

    /**
     * Fan laws (same as affinity laws).
     */
    public static double fanFlowRate(double Q1, double D1, double D2, double N1, double N2) {
        return Q1 * Math.pow(D2 / D1, 3) * (N2 / N1);
    }
}
