package org.jscience.earth.coordinates;

import java.io.InputStream;
import java.io.IOException;
import java.util.Collections;
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
 * Ellipsoids are loaded from {@code /org/jscience/earth/ellipsoids.json}.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
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
