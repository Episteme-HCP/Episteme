package org.jscience.earth.oceanography;

/**
 * Tidal prediction using harmonic analysis.
 */
public class TidalPrediction {

    private TidalPrediction() {
    }

    /**
     * Harmonic constituent for tidal prediction.
     */
    public static class TidalConstituent {
        public final String name;
        public final double amplitude; // meters
        public final double phase; // degrees
        public final double speed; // degrees per hour

        public TidalConstituent(String name, double amplitude, double phase, double speed) {
            this.name = name;
            this.amplitude = amplitude;
            this.phase = phase;
            this.speed = speed;
        }
    }

    // Common tidal constituents (approximate speeds in degrees/hour)
    public static final double M2_SPEED = 28.984104; // Principal lunar semidiurnal
    public static final double S2_SPEED = 30.0; // Principal solar semidiurnal
    public static final double N2_SPEED = 28.439730; // Larger lunar elliptic
    public static final double K1_SPEED = 15.041069; // Lunar diurnal
    public static final double O1_SPEED = 13.943035; // Lunar diurnal
    public static final double M4_SPEED = 57.968208; // Shallow water overtide

    /**
     * Predicts tide height at a given time using harmonic constituents.
     * h(t) = h₀ + Σ Aᵢ cos(ωᵢt - φᵢ)
     * 
     * @param meanSeaLevel   h₀ (meters)
     * @param constituents   Array of tidal constituents
     * @param hoursFromEpoch Time since reference epoch
     * @return Predicted tide height (meters)
     */
    public static double predictHeight(double meanSeaLevel, TidalConstituent[] constituents,
            double hoursFromEpoch) {
        double height = meanSeaLevel;

        for (TidalConstituent c : constituents) {
            double angle = Math.toRadians(c.speed * hoursFromEpoch - c.phase);
            height += c.amplitude * Math.cos(angle);
        }

        return height;
    }

    /**
     * Calculates tidal range (high - low) from constituents.
     * Approximate as 2 * sum of amplitudes for max range.
     */
    public static double maxTidalRange(TidalConstituent[] constituents) {
        double sumAmplitudes = 0;
        for (TidalConstituent c : constituents) {
            sumAmplitudes += c.amplitude;
        }
        return 2 * sumAmplitudes;
    }

    /**
     * Creates a simple M2+S2 tide model.
     */
    public static TidalConstituent[] simpleSemidiurnalModel(double m2Amp, double m2Phase,
            double s2Amp, double s2Phase) {
        return new TidalConstituent[] {
                new TidalConstituent("M2", m2Amp, m2Phase, M2_SPEED),
                new TidalConstituent("S2", s2Amp, s2Phase, S2_SPEED)
        };
    }

    /**
     * Estimates time to next high tide (simple approximation).
     */
    public static double hoursToNextHighTide(double currentHoursFromEpoch) {
        // M2 period is ~12.42 hours
        // M2 period is ~12.42 hours
        double phase = (currentHoursFromEpoch * M2_SPEED) % 360;
        return (360 - phase) / M2_SPEED;
    }
}
