/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.io.external;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Pressure;
import org.jscience.measure.quantity.Velocity;

/**
 * Weather information with type-safe Quantity measurements.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class WeatherInfo {

    private final String location;
    private final String description;
    private final double temperatureCelsius;
    private final double feelsLikeCelsius;
    private final double pressureHPa;
    private final double humidityPercent;
    private final double windSpeedMps;
    private final double cloudPercent;

    private WeatherInfo(Builder builder) {
        this.location = builder.location;
        this.description = builder.description;
        this.temperatureCelsius = builder.temperatureCelsius;
        this.feelsLikeCelsius = builder.feelsLikeCelsius;
        this.pressureHPa = builder.pressureHPa;
        this.humidityPercent = builder.humidityPercent;
        this.windSpeedMps = builder.windSpeedMps;
        this.cloudPercent = builder.cloudPercent;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public double getHumidityPercent() {
        return humidityPercent;
    }

    public double getCloudPercent() {
        return cloudPercent;
    }

    /**
     * Returns temperature as type-safe Quantity in Kelvin.
     */
    public Quantity<Temperature> getTemperature() {
        return Quantities.create(temperatureCelsius + 273.15, Units.KELVIN);
    }

    /**
     * Returns temperature in Celsius (raw value).
     */
    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    /**
     * Returns "feels like" temperature as Quantity in Kelvin.
     */
    public Quantity<Temperature> getFeelsLike() {
        return Quantities.create(feelsLikeCelsius + 273.15, Units.KELVIN);
    }

    /**
     * Returns pressure as type-safe Quantity in Pascal.
     */
    public Quantity<Pressure> getPressure() {
        return Quantities.create(pressureHPa * 100.0, Units.PASCAL); // hPa to Pa
    }

    /**
     * Returns wind speed as type-safe Quantity in m/s.
     */
    public Quantity<Velocity> getWindSpeed() {
        return Quantities.create(windSpeedMps, Units.METER_PER_SECOND);
    }

    /**
     * Returns wind speed in km/h.
     */
    public double getWindSpeedKmh() {
        return windSpeedMps * 3.6;
    }

    /**
     * Calculates heat index (feels-like in high humidity).
     * 
     * @return heat index in Celsius
     */
    public double getHeatIndex() {
        double T = temperatureCelsius;
        double R = humidityPercent;
        if (T < 27)
            return T;

        // Rothfusz regression
        return -8.78469475556 + 1.61139411 * T + 2.33854883889 * R
                - 0.14611605 * T * R - 0.012308094 * T * T
                - 0.0164248277778 * R * R + 0.002211732 * T * T * R
                + 0.00072546 * T * R * R - 0.000003582 * T * T * R * R;
    }

    @Override
    public String toString() {
        return String.format("WeatherInfo{%s: %.1f°C (%s), humidity=%.0f%%, wind=%.1f m/s}",
                location, temperatureCelsius, description, humidityPercent, windSpeedMps);
    }

    public static class Builder {
        private String location = "";
        private String description = "";
        private double temperatureCelsius = Double.NaN;
        private double feelsLikeCelsius = Double.NaN;
        private double pressureHPa = Double.NaN;
        private double humidityPercent = Double.NaN;
        private double windSpeedMps = Double.NaN;
        private double cloudPercent = Double.NaN;

        public Builder location(String loc) {
            this.location = loc;
            return this;
        }

        public Builder description(String desc) {
            this.description = desc;
            return this;
        }

        public Builder temperatureCelsius(double t) {
            this.temperatureCelsius = t;
            return this;
        }

        public Builder feelsLikeCelsius(double t) {
            this.feelsLikeCelsius = t;
            return this;
        }

        public Builder pressureHPa(double p) {
            this.pressureHPa = p;
            return this;
        }

        public Builder humidityPercent(double h) {
            this.humidityPercent = h;
            return this;
        }

        public Builder windSpeedMps(double w) {
            this.windSpeedMps = w;
            return this;
        }

        public Builder cloudPercent(double c) {
            this.cloudPercent = c;
            return this;
        }

        public WeatherInfo build() {
            return new WeatherInfo(this);
        }
    }
}
