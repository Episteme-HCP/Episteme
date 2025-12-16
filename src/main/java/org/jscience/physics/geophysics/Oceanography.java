package org.jscience.physics.geophysics;

/**
 * Oceanography calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Oceanography {

    /** Standard density of seawater (kg/m³) */
    public static final double RHO_SEAWATER = 1025;

    /** Gravitational acceleration (m/s²) */
    public static final double G = 9.80665;

    /**
     * Deep water wave speed.
     * c = √(gL / 2π) = gT / 2π
     * 
     * @param wavelength Wavelength (m)
     * @return Phase velocity (m/s)
     */
    public static double deepWaterWaveSpeed(double wavelength) {
        return Math.sqrt(G * wavelength / (2 * Math.PI));
    }

    /**
     * Deep water wave speed from period.
     */
    public static double deepWaterWaveSpeedFromPeriod(double period) {
        return G * period / (2 * Math.PI);
    }

    /**
     * Shallow water wave speed.
     * c = √(gh)
     * 
     * @param depth Water depth (m)
     */
    public static double shallowWaterWaveSpeed(double depth) {
        return Math.sqrt(G * depth);
    }

    /**
     * General dispersion relation (solved iteratively).
     * ω² = gk * tanh(kh)
     */
    public static double waveNumber(double period, double depth) {
        double omega = 2 * Math.PI / period;
        double k = omega * omega / G; // Initial guess (deep water)

        for (int i = 0; i < 20; i++) {
            double f = omega * omega - G * k * Math.tanh(k * depth);
            double df = -G * (Math.tanh(k * depth) + k * depth / Math.pow(Math.cosh(k * depth), 2));
            k = k - f / df;
        }

        return k;
    }

    /**
     * Significant wave height from wind (simplified).
     * H_s ≈ 0.0246 * U² (Fully developed sea)
     * 
     * @param windSpeed Wind speed at 10m (m/s)
     */
    public static double significantWaveHeight(double windSpeed) {
        return 0.0246 * windSpeed * windSpeed;
    }

    /**
     * Tidal constituent prediction.
     * η(t) = Σ A_i * cos(ω_i * t - φ_i)
     */
    public static double tidalHeight(double time, double[] amplitudes,
            double[] frequencies, double[] phases) {
        double height = 0;
        for (int i = 0; i < amplitudes.length; i++) {
            height += amplitudes[i] * Math.cos(frequencies[i] * time - phases[i]);
        }
        return height;
    }

    /**
     * M2 tidal constituent frequency (rad/s).
     * Period ≈ 12.42 hours
     */
    public static final double OMEGA_M2 = 2 * Math.PI / (12.42 * 3600);

    /**
     * S2 tidal constituent frequency.
     * Period = 12.00 hours
     */
    public static final double OMEGA_S2 = 2 * Math.PI / (12.00 * 3600);

    /**
     * Density of seawater from temperature and salinity (UNESCO 1983).
     * Simplified linear approximation.
     * 
     * @param temperature Temperature (°C)
     * @param salinity    Salinity (PSU)
     * @return Density (kg/m³)
     */
    public static double seawaterDensity(double temperature, double salinity) {
        // Reference: 35 PSU, 10°C -> ~1027 kg/m³
        double rho0 = 1028.1;
        double alpha = 0.00015; // Thermal expansion (1/°C)
        double beta = 0.00078; // Haline contraction (1/PSU)

        return rho0 * (1 - alpha * (temperature - 10) + beta * (salinity - 35));
    }

    /**
     * Speed of sound in seawater (Mackenzie equation).
     * 
     * @param temperature Temperature (°C)
     * @param salinity    Salinity (PSU)
     * @param depth       Depth (m)
     * @return Sound speed (m/s)
     */
    public static double soundSpeedSeawater(double temperature, double salinity, double depth) {
        double T = temperature;
        double S = salinity;
        double D = depth;

        return 1448.96 + 4.591 * T - 0.05304 * T * T + 0.0002374 * T * T * T
                + 1.340 * (S - 35) + 0.0163 * D + 1.675e-7 * D * D
                - 0.01025 * T * (S - 35) - 7.139e-13 * T * D * D * D;
    }

    /**
     * Mixed layer depth estimate from wind stress.
     * h_ml ≈ (2 * τ * t / ρ)^(1/2) (simplified Kraus-Turner)
     */
    public static double mixedLayerDepth(double windStress, double time, double density) {
        return Math.sqrt(2 * windStress * time / density);
    }

    /**
     * Ekman transport.
     * M_E = τ / (ρ * f) where f = 2Ω sin(φ)
     * 
     * @param windStress Surface wind stress (N/m²)
     * @param latitude   Latitude (degrees)
     * @return Ekman transport (m²/s)
     */
    public static double ekmanTransport(double windStress, double latitude) {
        double omega = 7.292e-5; // Earth's rotation rate
        double f = 2 * omega * Math.sin(Math.toRadians(latitude));
        return windStress / (RHO_SEAWATER * Math.abs(f));
    }

    /**
     * Brunt-Väisälä (buoyancy) frequency.
     * N² = -(g/ρ) * dρ/dz
     */
    public static double bruntVaisalaFrequency(double densityGradient, double meanDensity) {
        double N2 = -G / meanDensity * densityGradient;
        return N2 > 0 ? Math.sqrt(N2) : 0;
    }
}
