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

package org.jscience.physics.geophysics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.Temperature;

import java.io.Serializable;
import org.jscience.physics.PhysicalConstants;

/**
 * Atmospheric physics calculations and data model.
 * <p>
 * Provides atmospheric property calculations (pressure, temperature, density)
 * and serves as a data model for planetary atmospheres.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Atmosphere implements Serializable {

    // --- Constants using Real ---
    /** Standard sea level pressure (Pa) */
    public static final Real P0 = Real.of(101325);

    /** Standard sea level temperature (K) */
    public static final Real T0 = Real.of(288.15);

    /** Temperature lapse rate (K/m) in troposphere */
    public static final Real LAPSE_RATE = Real.of(0.0065);

    /** Gas constant for dry air (J/(kg·K)) */
    public static final Real R_AIR = Real.of(287.05);

    /** Gravitational acceleration (m/s²) */
    private static final Real G = PhysicalConstants.g_n;

    // --- Data Model Fields (from earth/Atmosphere) ---
    private Quantity<Length> height;
    private String composition;
    private Quantity<Pressure> pressureValue;
    private Quantity<Temperature> averageTemperature;

    // --- Constructors ---
    public Atmosphere() {
    }

    public Atmosphere(Quantity<Length> height, String composition) {
        this.height = height;
        this.composition = composition;
    }

    // --- Data Model Accessors ---
    public Quantity<Length> getHeight() {
        return height;
    }

    public void setHeight(Quantity<Length> height) {
        this.height = height;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public Quantity<Pressure> getPressureValue() {
        return pressureValue;
    }

    public void setPressureValue(Quantity<Pressure> pressureValue) {
        this.pressureValue = pressureValue;
    }

    public Quantity<Temperature> getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(Quantity<Temperature> averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    // --- Static Calculation Methods (using Real) ---

    /**
     * Barometric formula: pressure vs altitude.
     * P = P0 * (1 - L*h/T0)^(g*M/(R*L))
     * 
     * @param altitude Height above sea level (m)
     * @return Pressure (Pa)
     */
    /** Troposphere boundary altitude (m) */
    private static final Real TROPOPAUSE = Real.of(11000);
    /** Tropopause temperature (K) */
    private static final Real T_TROPOPAUSE = Real.of(216.65);

    public static Real pressure(Real altitude) {
        if (altitude.compareTo(Real.ZERO) < 0) {
            altitude = Real.ZERO;
        }
        if (altitude.compareTo(TROPOPAUSE) > 0) {
            // Above troposphere - isothermal approximation
            Real P11 = pressure(TROPOPAUSE);
            Real T11 = temperature(TROPOPAUSE);
            Real expo = G.negate().multiply(altitude.subtract(TROPOPAUSE)).divide(R_AIR.multiply(T11));
            return P11.multiply(expo.exp());
        }
        Real exponent = G.divide(R_AIR.multiply(LAPSE_RATE));
        Real base = Real.ONE.subtract(LAPSE_RATE.multiply(altitude).divide(T0));
        return P0.multiply(base.pow(exponent));
    }

    /**
     * Temperature vs altitude (troposphere).
     */
    public static Real temperature(Real altitude) {
        if (altitude.compareTo(TROPOPAUSE) > 0) {
            return T_TROPOPAUSE;
        }
        return T0.subtract(LAPSE_RATE.multiply(altitude));
    }

    /**
     * Air density from temperature and pressure.
     * ρ = P / (R * T)
     */
    public static Real density(Real pressure, Real temperature) {
        return pressure.divide(R_AIR.multiply(temperature));
    }

    /**
     * Density at altitude.
     */
    public static Real densityAtAltitude(Real altitude) {
        return density(pressure(altitude), temperature(altitude));
    }

    /**
     * Scale height: H = RT/g
     */
    public static Real scaleHeight(Real temperature) {
        return R_AIR.multiply(temperature).divide(G);
    }

    /**
     * Speed of sound in air.
     * c = sqrt(γ * R * T) where γ = 1.4 for diatomic gas
     */
    public static Real speedOfSound(Real temperature) {
        return Real.of(1.4).multiply(R_AIR).multiply(temperature).sqrt();
    }

    /**
     * Relative humidity from dew point and temperature.
     * RH ≈ 100 * exp(17.625 * Td / (243.04 + Td)) / exp(17.625 * T / (243.04 + T))
     */
    public static Real relativeHumidity(Real tempCelsius, Real dewPointCelsius) {
        Real es = saturationVaporPressure(tempCelsius);
        Real e = saturationVaporPressure(dewPointCelsius);
        return Real.of(100).multiply(e).divide(es);
    }

    /**
     * Saturation vapor pressure (Magnus formula).
     * e_s = 6.112 * exp(17.67 * T / (T + 243.5)) in hPa
     */
    public static Real saturationVaporPressure(Real tempCelsius) {
        Real exponent = Real.of(17.67).multiply(tempCelsius).divide(tempCelsius.add(Real.of(243.5)));
        return Real.of(6.112).multiply(exponent.exp());
    }

    /**
     * Heat index (feels-like temperature).
     */
    public static Real heatIndex(Real tempCelsius, Real humidity) {
        Real T = tempCelsius.multiply(Real.of(9.0 / 5.0)).add(Real.of(32)); // Fahrenheit
        Real RH = humidity;

        Real HI = Real.of(-42.379)
                .add(Real.of(2.04901523).multiply(T))
                .add(Real.of(10.14333127).multiply(RH))
                .subtract(Real.of(0.22475541).multiply(T).multiply(RH))
                .subtract(Real.of(0.00683783).multiply(T.pow(2)))
                .subtract(Real.of(0.05481717).multiply(RH.pow(2)))
                .add(Real.of(0.00122874).multiply(T.pow(2)).multiply(RH))
                .add(Real.of(0.00085282).multiply(T).multiply(RH.pow(2)))
                .subtract(Real.of(0.00000199).multiply(T.pow(2)).multiply(RH.pow(2)));

        return HI.subtract(Real.of(32)).multiply(Real.of(5.0 / 9.0)); // Back to Celsius
    }

    /**
     * Wind chill (Fahrenheit formula converted to Celsius).
     */
    /** Constants for wind chill calculation */
    private static final Real WIND_CHILL_TEMP_LIMIT = Real.of(10);
    private static final Real WIND_CHILL_SPEED_LIMIT = Real.of(4.8);
    private static final Real WIND_CHILL_EXPONENT = Real.of(0.16);

    public static Real windChill(Real tempCelsius, Real windSpeedKmh) {
        if (tempCelsius.compareTo(WIND_CHILL_TEMP_LIMIT) > 0 ||
                windSpeedKmh.compareTo(WIND_CHILL_SPEED_LIMIT) < 0) {
            return tempCelsius;
        }

        Real T = tempCelsius;
        Real V = windSpeedKmh.pow(WIND_CHILL_EXPONENT);

        return Real.of(13.12)
                .add(Real.of(0.6215).multiply(T))
                .subtract(Real.of(11.37).multiply(V))
                .add(Real.of(0.3965).multiply(T).multiply(V));
    }
}
