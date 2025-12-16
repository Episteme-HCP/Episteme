package org.jscience.physics.geophysics;

/**
 * Seismology calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Seismology {

    /**
     * Richter magnitude from seismogram amplitude.
     * M_L = log10(A) - log10(A0(δ))
     * 
     * @param amplitude  Maximum amplitude (micrometers)
     * @param distanceKm Distance to epicenter (km)
     * @return Local magnitude
     */
    public static double richterMagnitude(double amplitude, double distanceKm) {
        // Simplified attenuation correction
        double logA0 = 1.0 + 0.00189 * distanceKm - 2.0; // Approximate
        return Math.log10(amplitude) - logA0;
    }

    /**
     * Moment magnitude from seismic moment.
     * M_w = (2/3) * log10(M0) - 10.7
     * 
     * @param seismicMoment M0 in N·m
     * @return Moment magnitude
     */
    public static double momentMagnitude(double seismicMoment) {
        return (2.0 / 3.0) * Math.log10(seismicMoment) - 10.7;
    }

    /**
     * Energy released from magnitude.
     * log10(E) = 1.5*M + 4.8 (E in joules)
     */
    public static double energyFromMagnitude(double magnitude) {
        return Math.pow(10, 1.5 * magnitude + 4.8);
    }

    /**
     * P-wave velocity in Earth's crust.
     * Vp ≈ 6 km/s in upper crust
     */
    public static final double VP_CRUST = 6.0; // km/s

    /**
     * S-wave velocity (typically Vp/1.73).
     */
    public static final double VS_CRUST = 3.5; // km/s

    /**
     * Estimates distance from P-S time difference.
     * d = Δt / (1/Vs - 1/Vp)
     */
    public static double distanceFromPSTime(double deltaT_seconds) {
        return deltaT_seconds / (1.0 / VS_CRUST - 1.0 / VP_CRUST);
    }

    /**
     * Travel time for P-wave.
     */
    public static double pWaveTravelTime(double distanceKm) {
        return distanceKm / VP_CRUST;
    }

    /**
     * Travel time for S-wave.
     */
    public static double sWaveTravelTime(double distanceKm) {
        return distanceKm / VS_CRUST;
    }

    /**
     * Gutenberg-Richter relation: frequency-magnitude.
     * log10(N) = a - b*M
     * 
     * @param magnitude Magnitude threshold
     * @param a         Regional activity level
     * @param b         b-value (typically ~1.0)
     * @return Expected number of earthquakes per year ≥ M
     */
    public static double gutenbergRichter(double magnitude, double a, double b) {
        return Math.pow(10, a - b * magnitude);
    }

    /**
     * Modified Mercalli Intensity from magnitude and distance.
     * Approximate empirical relation.
     */
    public static int mercalliIntensity(double magnitude, double distanceKm) {
        double intensity = 1.5 * magnitude - 2.5 * Math.log10(distanceKm + 1) + 2;
        return (int) Math.round(Math.max(1, Math.min(12, intensity)));
    }
}
