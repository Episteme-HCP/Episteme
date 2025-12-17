package org.jscience.physics.astronomy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;
import java.util.Iterator;
import java.util.Map;
import org.jscience.physics.PhysicalConstants;

import java.io.InputStream;

import java.util.Collections;

/**
 * Loads StarSystems from JSON configuration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SolarSystemLoader {

    public static StarSystem load(String resourcePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Note: Resource stream loading is relative to the class location.
            // If resources move, this path needs care.
            InputStream is = SolarSystemLoader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                // Try absolute path from classpath root if relative fails
                is = SolarSystemLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            }
            if (is == null) {
                // Try adding / if missing
                if (!resourcePath.startsWith("/")) {
                    is = SolarSystemLoader.class.getResourceAsStream("/" + resourcePath);
                }
            }

            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            JsonNode root = mapper.readTree(is);
            String systemName = root.get("name").asText();
            StarSystem system = new StarSystem(systemName);

            // Load Star(s)
            JsonNode starsNode = root.get("stars");
            if (starsNode != null && starsNode.isArray()) {
                for (JsonNode starNode : starsNode) {
                    Star star = loadStar(starNode);
                    if (system.getStars().isEmpty()) {
                        // First star is considered main by convention for now
                        // system.setMainStar(star);
                    }
                    system.addStar(star);
                    system.addBody(star);

                    // Load Planets for this star
                    JsonNode planetsNode = starNode.get("planets");
                    if (planetsNode != null && planetsNode.isArray()) {
                        for (JsonNode planetNode : planetsNode) {
                            Planet planet = loadPlanet(planetNode, star.getMass());
                            star.addChild(planet);
                            system.addBody(planet);

                            // Load Moons
                            JsonNode moonsNode = planetNode.get("moons");
                            if (moonsNode != null && moonsNode.isArray()) {
                                for (JsonNode moonNode : moonsNode) {
                                    CelestialBody moon = loadBody(moonNode, planet.getMass()); // Generic body for moon
                                    planet.addChild(moon);
                                    system.addBody(moon);
                                }
                            }
                        }
                    }
                }
            }

            return system;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load solar system from " + resourcePath, e);
        }
    }

    private static Star loadStar(JsonNode node) {
        String name = node.get("name").asText();
        Quantity<Mass> mass = Quantities.create(node.get("mass").asDouble(), Units.KILOGRAM);
        Quantity<Length> radius = Quantities.create(node.get("radius").asDouble(), Units.METER);
        // Position/Velocity defaults to zero if not specified (e.g. central star)
        // In reality, barycenter...
        Star star = new Star(name, mass, radius, zeroVector(), zeroVector());

        if (node.has("texture"))
            star.setTexturePath(node.get("texture").asText());
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
        if (node.has("texture"))
            planet.setTexturePath(node.get("texture").asText());
        if (node.has("habitable"))
            planet.setHabitable(node.get("habitable").asBoolean());

        if (node.has("surface_temperature"))
            planet.setSurfaceTemperature(Quantities.create(node.get("surface_temperature").asDouble(), Units.KELVIN));

        if (node.has("surface_pressure"))
            planet.setSurfacePressure(Quantities.create(node.get("surface_pressure").asDouble(), Units.PASCAL));

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
        if (node.has("texture"))
            body.setTexturePath(node.get("texture").asText());
        return body;
    }

    private static org.jscience.mathematics.linearalgebra.Vector<Real> zeroVector() {
        return DenseVector.of(Collections.nCopies(3, Real.ZERO), Reals.getInstance());
    }

    private static Object[] getInitialState(JsonNode node, Quantity<Mass> parentMass) {
        if (node.has("orbit")) {
            JsonNode orbit = node.get("orbit");
            double a = orbit.get("a").asDouble(); // Semi-major axis
            double e = orbit.get("e").asDouble(); // Eccentricity
            double i = Math.toRadians(orbit.get("i").asDouble()); // Inclination (deg -> rad)
            double Omega = Math.toRadians(orbit.get("Omega").asDouble()); // Ascending node (deg -> rad)
            double omega = Math.toRadians(orbit.get("omega").asDouble()); // Arg of periapsis (deg -> rad)
            double M = Math.toRadians(orbit.get("M").asDouble()); // Mean anomaly (deg -> rad)

            // Standard gravitational parameter mu = G * M
            // G is N*m^2/kg^2. M is kg. Result is N*m^2/kg = m^3/s^2.
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
