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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.*;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

import java.util.Iterator;
import java.util.Map;
import org.jscience.physics.PhysicalConstants;

import java.io.InputStream;
import java.util.Collections;

/**
 * Loads StarSystems from JSON configuration.
 * Modernized to JScience.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SolarSystemReader {

    public static StarSystem load(String resourcePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = SolarSystemReader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                is = SolarSystemReader.class.getClassLoader().getResourceAsStream(resourcePath);
            }
            if (is == null) {
                if (!resourcePath.startsWith("/")) {
                    is = SolarSystemReader.class.getResourceAsStream("/" + resourcePath);
                }
            }

            if (is == null) {
                // throw new IllegalArgumentException("Resource not found: " + resourcePath);
                System.out.println("WARNING: Solar System resource not found: " + resourcePath + ". Using fallback.");
                return createFallbackSolarSystem();
            }

            JsonNode root = mapper.readTree(is);
            return parseSystem(root);

        } catch (Exception e) {
            System.err.println("Failed to load solar system from " + resourcePath + ": " + e.getMessage());
            e.printStackTrace();
            return createFallbackSolarSystem();
        }
    }

    private static StarSystem parseSystem(JsonNode root) {
        String systemName = root.get("name").asText();
        StarSystem system = new StarSystem(systemName);

        JsonNode starsNode = root.get("stars");
        if (starsNode != null && starsNode.isArray()) {
            for (JsonNode starNode : starsNode) {
                Star star = loadStar(starNode);
                system.addStar(star);
                system.addBody(star);

                JsonNode planetsNode = starNode.get("planets");
                if (planetsNode != null && planetsNode.isArray()) {
                    for (JsonNode planetNode : planetsNode) {
                        Planet planet = loadPlanet(planetNode, star.getMass());
                        star.addChild(planet);
                        system.addBody(planet);

                        JsonNode moonsNode = planetNode.get("moons");
                        if (moonsNode != null && moonsNode.isArray()) {
                            for (JsonNode moonNode : moonsNode) {
                                CelestialBody moon = loadBody(moonNode, planet.getMass());
                                planet.addChild(moon);
                                system.addBody(moon);
                            }
                        }
                    }
                }
            }
        }
        return system;
    }

    private static StarSystem createFallbackSolarSystem() {
        StarSystem system = new StarSystem("Fallback Solar System");

        // Sun
        Star sun = new Star("Sun",
                Quantities.create(1.989e30, Units.KILOGRAM),
                Quantities.create(6.9634e8, Units.METER),
                zeroVector(), zeroVector());
        sun.setTexturePath("2k_sun.jpg");
        system.addStar(sun);
        system.addBody(sun);

        // Earth
        // State vectors approx
        org.jscience.mathematics.linearalgebra.Vector<Real> earthPos = DenseVector
                .of(java.util.Arrays.asList(Real.of(1.496e11), Real.ZERO, Real.ZERO), Reals.getInstance());
        org.jscience.mathematics.linearalgebra.Vector<Real> earthVel = DenseVector
                .of(java.util.Arrays.asList(Real.ZERO, Real.of(29780.0), Real.ZERO), Reals.getInstance());

        Planet earth = new Planet("Earth",
                Quantities.create(5.972e24, Units.KILOGRAM),
                Quantities.create(6.371e6, Units.METER),
                earthPos, earthVel);
        earth.setTexturePath("2k_earth_daymap.jpg");
        earth.setHabitable(true);
        earth.setSurfaceTemperature(Quantities.create(288, Units.KELVIN));
        earth.setSurfacePressure(Quantities.create(101325, Units.PASCAL));

        sun.addChild(earth);
        system.addBody(earth);

        return system;
    }

    private static Star loadStar(JsonNode node) {
        String name = node.get("name").asText();
        Quantity<Mass> mass = Quantities.create(node.get("mass").asDouble(), Units.KILOGRAM);
        Quantity<Length> radius = Quantities.create(node.get("radius").asDouble(), Units.METER);
        Star star = new Star(name, mass, radius, zeroVector(), zeroVector());

        parseTextures(node, star);
        if (node.has("luminosity"))
            star.setLuminosity(Quantities.create(node.get("luminosity").asDouble(), Units.WATT));
        if (node.has("temperature"))
            star.setTemperature(Quantities.create(node.get("temperature").asDouble(), Units.KELVIN));

        return star;
    }

    private static Planet loadPlanet(JsonNode node, Quantity<Mass> parentMass) {
        String name = node.get("name").asText();
        Quantity<Mass> mass = Quantities.create(node.get("mass").asDouble(), Units.KILOGRAM);
        Quantity<Length> radius = Quantities.create(node.get("radius").asDouble(), Units.METER);

        Object[] state = getInitialState(node, parentMass);
        @SuppressWarnings("unchecked")
        org.jscience.mathematics.linearalgebra.Vector<Real> pos = (org.jscience.mathematics.linearalgebra.Vector<Real>) state[0];
        @SuppressWarnings("unchecked")
        org.jscience.mathematics.linearalgebra.Vector<Real> vel = (org.jscience.mathematics.linearalgebra.Vector<Real>) state[1];

        Planet planet = new Planet(name, mass, radius, pos, vel);
        parseTextures(node, planet);

        if (node.has("rings")) {
            JsonNode ringsNode = node.get("rings");
            RingSystem rings = new RingSystem(name + " Rings",
                    Quantities.create(1e10, Units.KILOGRAM),
                    radius,
                    pos, vel);

            if (ringsNode.has("inner"))
                rings.setInnerRadius(Quantities.create(ringsNode.get("inner").asDouble(), Units.METER));
            if (ringsNode.has("outer"))
                rings.setOuterRadius(Quantities.create(ringsNode.get("outer").asDouble(), Units.METER));

            parseTextures(ringsNode, rings);
            planet.addChild(rings);
        }
        if (node.has("habitable"))
            planet.setHabitable(node.get("habitable").asBoolean());

        if (node.has("surface_temperature"))
            planet.setSurfaceTemperature(
                    Quantities.create(node.get("surface_temperature").asDouble(), Units.KELVIN));

        if (node.has("surface_pressure"))
            planet.setSurfacePressure(
                    Quantities.create(node.get("surface_pressure").asDouble(), Units.PASCAL));

        if (node.has("atmosphere")) {
            JsonNode atmNode = node.get("atmosphere");
            if (atmNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fields = atmNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    planet.getAtmosphereComposition().put(field.getKey(), field.getValue().asDouble());
                }
            }
        }

        return planet;
    }

    private static CelestialBody loadBody(JsonNode node, Quantity<Mass> parentMass) {
        String name = node.get("name").asText();
        Quantity<Mass> mass = Quantities.create(node.get("mass").asDouble(), Units.KILOGRAM);
        Quantity<Length> radius = Quantities.create(node.get("radius").asDouble(), Units.METER);

        Object[] state = getInitialState(node, parentMass);
        @SuppressWarnings("unchecked")
        org.jscience.mathematics.linearalgebra.Vector<Real> pos = (org.jscience.mathematics.linearalgebra.Vector<Real>) state[0];
        @SuppressWarnings("unchecked")
        org.jscience.mathematics.linearalgebra.Vector<Real> vel = (org.jscience.mathematics.linearalgebra.Vector<Real>) state[1];

        CelestialBody body = new CelestialBody(name, mass, radius, pos, vel);
        return body;
    }

    private static org.jscience.mathematics.linearalgebra.Vector<Real> zeroVector() {
        return DenseVector.of(Collections.nCopies(3, Real.ZERO), Reals.getInstance());
    }

    private static void parseTextures(JsonNode node, CelestialBody body) {
        if (node.has("texture")) {
            JsonNode texNode = node.get("texture");
            if (texNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fields = texNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    body.setTexture(field.getKey(), field.getValue().asText());
                }
            } else {
                body.setTexturePath(texNode.asText());
            }
        }
    }

    private static Object[] getInitialState(JsonNode node, Quantity<Mass> parentMass) {
        if (node.has("orbit")) {
            JsonNode orbit = node.get("orbit");
            double a = orbit.get("a").asDouble();
            double e = orbit.get("e").asDouble();
            double i = Math.toRadians(orbit.get("i").asDouble());
            double Omega = Math.toRadians(orbit.get("Omega").asDouble());
            double omega = Math.toRadians(orbit.get("omega").asDouble());
            double M = Math.toRadians(orbit.get("M").asDouble());

            double muVal = PhysicalConstants.G.doubleValue()
                    * parentMass.to(Units.KILOGRAM).getValue().doubleValue();

            OrbitalElements elements = new OrbitalElements(
                    Real.of(a), Real.of(e), Real.of(i),
                    Real.of(Omega), Real.of(omega), Real.of(M), Real.of(muVal));
            return elements.toStateVector();
        }
        return new Object[] { zeroVector(), zeroVector() };
    }
}



