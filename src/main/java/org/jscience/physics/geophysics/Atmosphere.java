package org.jscience.physics.geophysics;

/**
 * Atmospheric physics calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Atmosphere {

    /** Standard sea level pressure (Pa) */
    public static final double P0 = 101325;

    /** Standard sea level temperature (K) */
    public static final double T0 = 288.15;

    /** Temperature lapse rate (K/m) in troposphere */
    public static final double LAPSE_RATE = 0.0065;

    /** Gas constant for dry air (J/(kg·K)) */
    public static final double R_AIR = 287.05;

    /** Gravitational acceleration (m/s²) */
    public static final double G = 9.80665;

    /**
     * Barometric formula: pressure vs altitude.
     * P = P0 * (1 - L*h/T0)^(g*M/(R*L))
     * 
     * @param altitude Height above sea level (m)
     * @return Pressure (Pa)
     */
    public static double pressure(double altitude) {
        if (altitude < 0)
            altitude = 0;
        if (altitude > 11000) {
            // Above troposphere - isothermal approximation
            double P11 = pressure(11000);
            double T11 = temperature(11000);
            return P11 * Math.exp(-G * (altitude - 11000) / (R_AIR * T11));
        }
        double exponent = G / (R_AIR * LAPSE_RATE);
        return P0 * Math.pow(1 - LAPSE_RATE * altitude / T0, exponent);
    }

    /**
     * Temperature vs altitude (troposphere).
     */
    public static double temperature(double altitude) {
        if (altitude > 11000)
            return 216.65; // Tropopause
        return T0 - LAPSE_RATE * altitude;
    }

    /**
     * Air density from temperature and pressure.
     * ρ = P / (R * T)
     */
    public static double density(double pressure, double temperature) {
        return pressure / (R_AIR * temperature);
    }

    /**
     * Density at altitude.
     */
    public static double densityAtAltitude(double altitude) {
        return density(pressure(altitude), temperature(altitude));
    }

    /**
     * Scale height: H = RT/g
     */
    public static double scaleHeight(double temperature) {
        return R_AIR * temperature / G;
    }

    /**
     * Speed of sound in air.
     * c = sqrt(γ * R * T) where γ = 1.4 for diatomic gas
     */
    public static double speedOfSound(double temperature) {
        return Math.sqrt(1.4 * R_AIR * temperature);
    }

    /**
     * Relative humidity from dew point and temperature.
     * RH ≈ 100 * exp(17.625 * Td / (243.04 + Td)) / exp(17.625 * T / (243.04 + T))
     */
    public static double relativeHumidity(double tempCelsius, double dewPointCelsius) {
        double es = saturationVaporPressure(tempCelsius);
        double e = saturationVaporPressure(dewPointCelsius);
        return 100 * e / es;
    }

    /**
     * Saturation vapor pressure (Magnus formula).
     * e_s = 6.112 * exp(17.67 * T / (T + 243.5)) in hPa
     */
    public static double saturationVaporPressure(double tempCelsius) {
        return 6.112 * Math.exp(17.67 * tempCelsius / (tempCelsius + 243.5));
    }

    /**
     * Heat index (feels-like temperature).
     */
    public static double heatIndex(double tempCelsius, double humidity) {
        double T = tempCelsius * 9.0 / 5.0 + 32; // Convert to Fahrenheit
        double RH = humidity;

        double HI = -42.379 + 2.04901523 * T + 10.14333127 * RH
                - 0.22475541 * T * RH - 0.00683783 * T * T
                - 0.05481717 * RH * RH + 0.00122874 * T * T * RH
                + 0.00085282 * T * RH * RH - 0.00000199 * T * T * RH * RH;

        return (HI - 32) * 5.0 / 9.0; // Convert back to Celsius
    }

    /**
     * Wind chill (Fahrenheit formula converted to Celsius).
     */
    public static double windChill(double tempCelsius, double windSpeedKmh) {
        if (tempCelsius > 10 || windSpeedKmh < 4.8)
            return tempCelsius;

        double T = tempCelsius;
        double V = Math.pow(windSpeedKmh, 0.16);

        return 13.12 + 0.6215 * T - 11.37 * V + 0.3965 * T * V;
    }
}
