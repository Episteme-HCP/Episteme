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

package org.jscience.natural.physics.astronomy;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.quantity.*;
import org.jscience.core.measure.Units;
import org.jscience.core.io.MiniCatalog;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.sets.Reals;

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
public class StarCatalog implements MiniCatalog<Star> {

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
                String catalogId = node.has("catalogId") ? node.get("catalogId").asText() : null;

                // Create Quantities using Real and Units
                Quantity<Mass> mass = Quantities.create(
                        Real.of(node.get("mass").asDouble()), Units.KILOGRAM);

                Quantity<Temperature> temperature = Quantities.create(
                        Real.of(node.get("temperature").asDouble()), Units.KELVIN);

                Quantity<Power> luminosity = Quantities.create(
                        Real.of(node.get("luminosity").asDouble()), Units.WATT);

                Quantity<Length> radius = Quantities.create(
                        Real.of(node.get("radius").asDouble()), Units.METER);

                double distance = node.get("distance").asDouble(); // light years
                // We don't have position/velocity in JSON yet, so use placeholder or calculate?
                // The top-level Star expects position/velocity vectors.
                // We'll use Dummy vectors for now as we don't have 3D coordinates in stars.json
                
                Star star = new Star(name, catalogId, mass, radius, 
                     DenseVector.of(List.of(Real.ZERO, Real.ZERO, Real.ZERO), Reals.getInstance()), 
                     DenseVector.of(List.of(Real.ZERO, Real.ZERO, Real.ZERO), Reals.getInstance()));
                
                star.setSpectralType(spectralType);
                star.setLuminosity(luminosity);
                star.setTemperature(temperature);
                star.setDistanceLightYears(distance);
                
                stars.add(star);
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
}



