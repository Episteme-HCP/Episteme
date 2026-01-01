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

package org.jscience.earth.coordinates;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Defines a Reference Ellipsoid for geodetic calculations.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ReferenceEllipsoid {

    private static final Logger LOGGER = Logger.getLogger(ReferenceEllipsoid.class.getName());
    private static final Map<String, ReferenceEllipsoid> REGISTRY = new HashMap<>();

    static {
        loadEllipsoids();
    }

    private final String name;
    private final String code;
    private final Real semiMajorAxis; // a
    private final Real inverseFlattening; // 1/f
    private final Real flattening; // f = 1 / (1/f)
    private final Real semiMinorAxis; // b = a(1 - f)
    private final Real eccentricitySquared; // e^2 = f(2-f)

    public ReferenceEllipsoid(String name, String code, double semiMajorAxisMeters, double inverseFlatteningVal) {
        this.name = name;
        this.code = code;
        this.semiMajorAxis = Real.of(semiMajorAxisMeters);
        this.inverseFlattening = Real.of(inverseFlatteningVal);

        // Derived parameters
        this.flattening = Real.ONE.divide(this.inverseFlattening);
        this.semiMinorAxis = this.semiMajorAxis.multiply(Real.ONE.subtract(this.flattening));
        this.eccentricitySquared = this.flattening.multiply(Real.TWO.subtract(this.flattening));
    }

    private static void loadEllipsoids() {
        try (InputStream is = ReferenceEllipsoid.class.getResourceAsStream("/org/jscience/earth/ellipsoids.json")) {
            if (is == null) {
                LOGGER.log(Level.SEVERE, "ellipsoids.json not found!");
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);
            if (root.isArray()) {
                for (JsonNode node : root) {
                    try {
                        String n = node.get("name").asText();
                        String c = node.get("code").asText();
                        double a = node.get("semiMajorAxis").asDouble();
                        double invF = node.get("inverseFlattening").asDouble();

                        ReferenceEllipsoid el = new ReferenceEllipsoid(n, c, a, invF);
                        REGISTRY.put(n.toUpperCase(), el);
                        REGISTRY.put(c.toUpperCase(), el);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to parse ellipsoid entry", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load ellipsoids", e);
        }
    }

    public static ReferenceEllipsoid get(String nameOrCode) {
        return REGISTRY.get(nameOrCode.toUpperCase());
    }

    public static final ReferenceEllipsoid WGS84 = get("WGS84");
    public static final ReferenceEllipsoid GRS80 = get("GRS80");

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Quantity<Length> getSemiMajorAxis() {
        return Quantities.create(semiMajorAxis.doubleValue(), Units.METER);
    }

    public Quantity<Length> getSemiMinorAxis() {
        return Quantities.create(semiMinorAxis.doubleValue(), Units.METER);
    }

    public Real getFlattening() {
        return flattening;
    }

    public Real getEccentricitySquared() {
        return eccentricitySquared;
    }
}


