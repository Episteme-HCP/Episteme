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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.physics.astronomy.loaders;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Time;

/**
 * Data transfer object for exoplanet information from NASA Exoplanet Archive.
 * <p>
 * Contains key planetary and stellar parameters with type-safe Quantity
 * support.
 * </p>
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ExoplanetInfo {

    private static final double JUPITER_MASS_KG = 1.898e27;
    private static final double JUPITER_RADIUS_M = 6.9911e7;
    private static final double PARSEC_M = 3.0857e16;
    private static final double DAY_S = 86400.0;

    private final String name;
    private final String hostStar;
    private final double massJupiter;
    private final double radiusJupiter;
    private final double orbitalPeriodDays;
    private final double equilibriumTemperatureK;
    private final double distanceParsecs;

    private ExoplanetInfo(Builder builder) {
        this.name = builder.name;
        this.hostStar = builder.hostStar;
        this.massJupiter = builder.massJupiter;
        this.radiusJupiter = builder.radiusJupiter;
        this.orbitalPeriodDays = builder.orbitalPeriodDays;
        this.equilibriumTemperatureK = builder.equilibriumTemperatureK;
        this.distanceParsecs = builder.distanceParsecs;
    }

    public String getName() {
        return name;
    }

    public String getHostStar() {
        return hostStar;
    }

    public double getMassJupiter() {
        return massJupiter;
    }

    public double getRadiusJupiter() {
        return radiusJupiter;
    }

    public double getOrbitalPeriodDays() {
        return orbitalPeriodDays;
    }

    public double getEquilibriumTemperatureK() {
        return equilibriumTemperatureK;
    }

    public double getDistanceParsecs() {
        return distanceParsecs;
    }

    /**
     * Returns mass as type-safe Quantity in kg.
     */
    public Quantity<Mass> getMass() {
        return Quantities.create(massJupiter * JUPITER_MASS_KG, Units.KILOGRAM);
    }

    /**
     * Returns radius as type-safe Quantity in meters.
     */
    public Quantity<Length> getRadius() {
        return Quantities.create(radiusJupiter * JUPITER_RADIUS_M, Units.METER);
    }

    /**
     * Returns equilibrium temperature as type-safe Quantity in Kelvin.
     */
    public Quantity<Temperature> getEquilibriumTemperature() {
        return Quantities.create(equilibriumTemperatureK, Units.KELVIN);
    }

    /**
     * Returns orbital period as type-safe Quantity in seconds.
     */
    public Quantity<Time> getOrbitalPeriod() {
        return Quantities.create(orbitalPeriodDays * DAY_S, Units.SECOND);
    }

    /**
     * Returns distance from Earth as type-safe Quantity in meters.
     */
    public Quantity<Length> getDistance() {
        return Quantities.create(distanceParsecs * PARSEC_M, Units.METER);
    }

    /**
     * Checks if this planet is potentially in the habitable zone.
     * Uses simplified criterion: equilibrium temperature 200-320 K.
     */
    public boolean isPotentiallyHabitable() {
        return equilibriumTemperatureK >= 200 && equilibriumTemperatureK <= 320;
    }

    /**
     * Returns Earth Similarity Index estimate (simplified).
     * Based on radius and temperature ratios.
     */
    public double getEarthSimilarityIndex() {
        double radiusEarth = radiusJupiter * 11.2; // Jupiter radii to Earth radii
        double tempEarth = 255.0; // Earth equilibrium temp

        double radiusRatio = Math.min(radiusEarth, 1.0 / radiusEarth);
        double tempRatio = Math.min(equilibriumTemperatureK / tempEarth, tempEarth / equilibriumTemperatureK);

        return Math.pow(radiusRatio * tempRatio, 0.5);
    }

    @Override
    public String toString() {
        return String.format("ExoplanetInfo{name='%s', host='%s', mass=%.2f Mj, radius=%.2f Rj, T=%.0f K}",
                name, hostStar, massJupiter, radiusJupiter, equilibriumTemperatureK);
    }

    public static class Builder {
        private String name = "";
        private String hostStar = "";
        private double massJupiter = Double.NaN;
        private double radiusJupiter = Double.NaN;
        private double orbitalPeriodDays = Double.NaN;
        private double equilibriumTemperatureK = Double.NaN;
        private double distanceParsecs = Double.NaN;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder hostStar(String host) {
            this.hostStar = host;
            return this;
        }

        public Builder massJupiter(double mass) {
            this.massJupiter = mass;
            return this;
        }

        public Builder radiusJupiter(double radius) {
            this.radiusJupiter = radius;
            return this;
        }

        public Builder orbitalPeriodDays(double days) {
            this.orbitalPeriodDays = days;
            return this;
        }

        public Builder equilibriumTemperatureK(double temp) {
            this.equilibriumTemperatureK = temp;
            return this;
        }

        public Builder distanceParsecs(double dist) {
            this.distanceParsecs = dist;
            return this;
        }

        public ExoplanetInfo build() {
            return new ExoplanetInfo(this);
        }
    }
}