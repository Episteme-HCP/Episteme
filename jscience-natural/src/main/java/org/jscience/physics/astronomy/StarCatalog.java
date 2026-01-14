/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.astronomy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.quantity.*;
import org.jscience.measure.units.SI;
import org.jscience.io.MiniCatalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Star catalog with Quantity-based astronomical data loaded from JSON.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StarCatalog implements MiniCatalog<StarCatalog.Star> {

    private static final StarCatalog INSTANCE = new StarCatalog();
    private final List<Star> stars = new ArrayList<>();

    /**
     * Returns the singleton instance.
     */
    public static StarCatalog getInstance() {
        return INSTANCE;
    }

    public StarCatalog() {
        loadFromJSON();
    }

    /**
     * Load star data from JSON resource.
     */
    private void loadFromJSON() {
        try {
            InputStream is = getClass().getResourceAsStream("/org/jscience/physics/astronomy/stars.json");
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

    // ========== MiniCatalog Implementation ==========

    @Override
    public List<Star> getAll() {
        return Collections.unmodifiableList(stars);
    }

    @Override
    public Optional<Star> findByName(String name) {
        return stars.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public int size() {
        return stars.size();
    }

    // ========== Enhanced Query Methods ==========

    /**
     * @deprecated Use {@link #getAll()} instead
     */
    @Deprecated
    public List<Star> getStars() {
        return getAll();
    }

    /**
     * Filters stars by spectral class (O, B, A, F, G, K, M).
     *
     * @param spectralClass the spectral class letter
     * @return matching stars
     */
    public List<Star> filterBySpectralClass(char spectralClass) {
        String prefix = String.valueOf(spectralClass).toUpperCase();
        return stars.stream()
                .filter(s -> s.getSpectralType().toUpperCase().startsWith(prefix))
                .collect(Collectors.toList());
    }

    /**
     * Finds stars within a distance range.
     *
     * @param maxDistanceLightYears maximum distance in light years
     * @return stars within range, sorted by distance
     */
    public List<Star> findNearest(double maxDistanceLightYears) {
        return stars.stream()
                .filter(s -> s.getDistanceLightYears() <= maxDistanceLightYears)
                .sorted(Comparator.comparingDouble(Star::getDistanceLightYears))
                .collect(Collectors.toList());
    }

    /**
     * Filters stars by temperature range (for H-R diagram).
     *
     * @param minKelvin minimum temperature
     * @param maxKelvin maximum temperature
     * @return matching stars
     */
    public List<Star> filterByTemperature(double minKelvin, double maxKelvin) {
        return stars.stream()
                .filter(s -> {
                    double t = s.getTemperature().getValue().doubleValue();
                    return t >= minKelvin && t <= maxKelvin;
                })
                .collect(Collectors.toList());
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


