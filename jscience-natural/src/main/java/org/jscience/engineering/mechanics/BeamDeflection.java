package org.jscience.engineering.mechanics;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Force;

/**
 * Euler-Bernoulli beam deflection calculations.
 */
public class BeamDeflection {

    private BeamDeflection() {
    }

    /**
     * Maximum deflection for simply supported beam with center point load.
     * δ_max = P * L³ / (48 * E * I)
     * 
     * @param load            Point load (N)
     * @param length          Beam length (m)
     * @param elasticModulus  Young's modulus E (Pa)
     * @param momentOfInertia Second moment of area I (m⁴)
     * @return Maximum deflection
     */
    public static Quantity<Length> simplySupported_CenterLoad(
            Quantity<Force> load, Quantity<Length> length,
            double elasticModulus, double momentOfInertia) {

        double P = load.to(Units.NEWTON).getValue().doubleValue();
        double L = length.to(Units.METER).getValue().doubleValue();

        double deflection = P * L * L * L / (48 * elasticModulus * momentOfInertia);
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Maximum deflection for cantilever beam with end load.
     * δ_max = P * L³ / (3 * E * I)
     */
    public static Quantity<Length> cantilever_EndLoad(
            Quantity<Force> load, Quantity<Length> length,
            double elasticModulus, double momentOfInertia) {

        double P = load.to(Units.NEWTON).getValue().doubleValue();
        double L = length.to(Units.METER).getValue().doubleValue();

        double deflection = P * L * L * L / (3 * elasticModulus * momentOfInertia);
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Simply supported beam with uniformly distributed load.
     * δ_max = 5 * w * L⁴ / (384 * E * I)
     * 
     * @param loadPerMeter Distributed load (N/m)
     */
    public static Quantity<Length> simplySupported_UniformLoad(
            double loadPerMeter, Quantity<Length> length,
            double elasticModulus, double momentOfInertia) {

        double L = length.to(Units.METER).getValue().doubleValue();
        double deflection = 5 * loadPerMeter * L * L * L * L / (384 * elasticModulus * momentOfInertia);
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Rectangle moment of inertia.
     * I = b * h³ / 12
     */
    public static double rectangleMomentOfInertia(double width, double height) {
        return width * Math.pow(height, 3) / 12;
    }

    /**
     * Circular cross-section moment of inertia.
     * I = π * r⁴ / 4
     */
    public static double circleMomentOfInertia(double radius) {
        return Math.PI * Math.pow(radius, 4) / 4;
    }
}
