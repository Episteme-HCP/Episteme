package org.jscience.architecture;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Force;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Area;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Basic structural engineering primitives for architectural analysis.
 */
public final class StructuralPrimitives {

    private StructuralPrimitives() {}

    /**
     * Calculates the pressure (stress) on a surface given a force.
     * 
     * @param load The total force applied.
     * @param area The area over which the force is distributed.
     * @return The pressure in Pascals.
     */
    public static Quantity<?> calculateStress(Quantity<Force> load, Quantity<Area> area) {
        return load.divide(area);
    }

    /**
     * Calculates the horizontal thrust of a simple parabolic arch under uniform load.
     * H = (w * L^2) / (8 * h)
     */
    public static Quantity<Force> calculateArchThrust(Quantity<?> uniformLoad, Quantity<Length> span, Quantity<Length> height) {
        double w = uniformLoad.getValue().doubleValue(); // Load per unit length
        double l = span.to(Units.METER).getValue().doubleValue();
        double h = height.to(Units.METER).getValue().doubleValue();
        
        double thrust = (w * l * l) / (8 * h);
        return Quantities.create(thrust, Units.NEWTON);
    }
}
