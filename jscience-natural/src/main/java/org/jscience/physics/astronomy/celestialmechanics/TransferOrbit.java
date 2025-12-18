package org.jscience.physics.astronomy.celestialmechanics;

/**
 * Orbital maneuvers and transfer orbits.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class TransferOrbit {

    /** Gravitational parameter of Sun (m³/s²) */
    public static final double MU_SUN = 1.32712440018e20;

    /** Astronomical Unit (m) */
    public static final double AU = 1.495978707e11;

    /**
     * Delta-V for Hohmann transfer between two circular orbits.
     * 
     * @param r1 Radius of initial orbit (m)
     * @param r2 Radius of final orbit (m)
     * @param mu Gravitational parameter (m³/s²)
     * @return Total delta-V (m/s)
     */
    public static double hohmannTransferDeltaV(double r1, double r2, double mu) {
        // Velocity magnitude in circular orbits
        double v1 = Math.sqrt(mu / r1);
        double v2 = Math.sqrt(mu / r2);

        // Semi-major axis of transfer ellipse
        double a_transfer = (r1 + r2) / 2.0;

        // Velocity at periapsis and apoapsis of transfer orbit
        double v_transfer1 = Math.sqrt(mu * (2.0 / r1 - 1.0 / a_transfer));
        double v_transfer2 = Math.sqrt(mu * (2.0 / r2 - 1.0 / a_transfer));

        // Delta-V for each burn
        double dv1 = Math.abs(v_transfer1 - v1);
        double dv2 = Math.abs(v2 - v_transfer2);

        return dv1 + dv2;
    }

    /**
     * Time of flight for Hohmann transfer.
     * t = π * √(a³ / μ)
     */
    public static double hohmannTransferTime(double r1, double r2, double mu) {
        double a_transfer = (r1 + r2) / 2.0;
        return Math.PI * Math.sqrt(Math.pow(a_transfer, 3) / mu);
    }

    /**
     * Bi-elliptic transfer Delta-V.
     * Often more efficient than Hohmann if r2/r1 > 11.94.
     * 
     * @param r1 Radius of initial orbit
     * @param r2 Radius of final orbit
     * @param rb Radius of apoapsis of intermediate ellipse (must be > max(r1, r2))
     * @param mu Gravitational parameter
     */
    public static double biEllipticTransferDeltaV(double r1, double r2, double rb, double mu) {
        double v1 = Math.sqrt(mu / r1);
        double v2 = Math.sqrt(mu / r2);

        // Transfer 1: r1 to rb
        double a1 = (r1 + rb) / 2.0;
        double v_t1_p = Math.sqrt(mu * (2.0 / r1 - 1.0 / a1));
        double v_t1_a = Math.sqrt(mu * (2.0 / rb - 1.0 / a1));

        // Transfer 2: rb to r2
        double a2 = (r2 + rb) / 2.0;
        double v_t2_a = Math.sqrt(mu * (2.0 / rb - 1.0 / a2));
        double v_t2_p = Math.sqrt(mu * (2.0 / r2 - 1.0 / a2));

        double dv1 = Math.abs(v_t1_p - v1);
        double dv2 = Math.abs(v_t2_a - v_t1_a);
        double dv3 = Math.abs(v_t2_p - v2);

        return dv1 + dv2 + dv3;
    }

    /**
     * Synodic period between two bodies.
     * 1/S = |1/P1 - 1/P2|
     */
    public static double synodicPeriod(double P1, double P2) {
        return 1.0 / Math.abs(1.0 / P1 - 1.0 / P2);
    }

    /**
     * Launch window frequency.
     * Similar to synodic period.
     */
    public static double launchWindowInterval(double periodEarth, double periodTarget) {
        return synodicPeriod(periodEarth, periodTarget);
    }
}
