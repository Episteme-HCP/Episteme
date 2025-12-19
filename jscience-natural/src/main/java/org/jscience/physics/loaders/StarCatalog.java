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
package org.jscience.physics.loaders;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.quantity.*;
import org.jscience.measure.units.SI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.util.*;

/**
 * Star catalog with Quantity-based astronomical data loaded from JSON.
 * <p>
 * Uses JScience Quantities for physical properties: Mass, Temperature, Power,
 * Length.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class StarCatalog {

    private final List<Star> stars = new ArrayList<>();

    public StarCatalog() {
        loadFromJSON();
    }

    /**
     * Load star data from JSON resource.
     */
    private void loadFromJSON() {
        try {
            InputStream is = getClass().getResourceAsStream("/org/jscience/astronomy/stars.json");
            if (is == null) {
                System.err.println("Could not load stars.json");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            for (JsonNode node : root) {
                String name = node.get("name").asText();
                String spectralType = node.get("spectralType").asText();

                // Create Quantities using Real and SI units
                Quantity<Mass> mass = Quantities.create(
                        Real.of(node.get("mass").asDouble()), SI.KILOGRAM);

                Quantity<Temperature> temperature = Quantities.create(
                        Real.of(node.get("temperature").asDouble()), SI.KELVIN);

                Quantity<Power> luminosity = Quantities.create(
                        Real.of(node.get("luminosity").asDouble()), SI.WATT);

                Quantity<Length> radius = Quantities.create(
                        Real.of(node.get("radius").asDouble()), SI.METRE);

                double distance = node.get("distance").asDouble(); // light years

                stars.add(new Star(name, spectralType, mass, temperature, luminosity, radius, distance));
            }

        } catch (IOException e) {
            System.err.println("Error loading star catalog: " + e.getMessage());
        }
    }

    public List<Star> getStars() {
        return Collections.unmodifiableList(stars);
    }

    public Star findByName(String name) {
        return stars.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Star data class using JScience Quantities.
     */
    public static class Star {
        private final String name;
        private final String spectralType;
        private final Quantity<Mass> mass;
        private final Quantity<Temperature> temperature;
        private final Quantity<Power> luminosity;
        private final Quantity<Length> radius;
        private final double distanceLightYears;

        public Star(String name, String spectralType,
                Quantity<Mass> mass, Quantity<Temperature> temperature,
                Quantity<Power> luminosity, Quantity<Length> radius,
                double distanceLightYears) {
            this.name = name;
            this.spectralType = spectralType;
            this.mass = mass;
            this.temperature = temperature;
            this.luminosity = luminosity;
            this.radius = radius;
            this.distanceLightYears = distanceLightYears;
        }

        public String getName() {
            return name;
        }

        public String getSpectralType() {
            return spectralType;
        }

        public Quantity<Mass> getMass() {
            return mass;
        }

        public Quantity<Temperature> getTemperature() {
            return temperature;
        }

        public Quantity<Power> getLuminosity() {
            return luminosity;
        }

        public Quantity<Length> getRadius() {
            return radius;
        }

        public double getDistanceLightYears() {
            return distanceLightYears;
        }

        @Override
        public String toString() {
            return String.format("%s (%s): T=%s K, M=%s kg, L=%s W, R=%s m, d=%.1f ly",
                    name, spectralType,
                    temperature.getValue(),
                    mass.getValue(),
                    luminosity.getValue(),
                    radius.getValue(),
                    distanceLightYears);
        }
    }
}
