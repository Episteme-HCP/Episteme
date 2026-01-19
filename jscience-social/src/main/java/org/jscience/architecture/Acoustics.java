package org.jscience.architecture;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Area;
import org.jscience.measure.quantity.Volume;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Architectural Acoustics calculator.
 * 
 * Reference: Sabine, W. C. (1922). Collected Papers on Acoustics.
 */
public final class Acoustics {

    private Acoustics() {}

    /**
     * Calculates the reverberation time (RT60) using Sabine's formula.
     * RT60 = 0.161 * V / A (in metric units)
     * 
     * @param volume The volume of the room.
     * @param totalAbsorption The total absorption in Sabins (m^2).
     * @return The estimated reverberation time.
     */
    public static Quantity<Time> calculateSabineRT60(Quantity<Volume> volume, Quantity<Area> totalAbsorption) {
        double v = volume.to(Units.CUBIC_METER).getValue().doubleValue();
        double a = totalAbsorption.to(Units.SQUARE_METER).getValue().doubleValue();
        
        if (a <= 0) {
            throw new IllegalArgumentException("Total absorption must be greater than zero");
        }
        
        double rt60 = 0.161 * v / a;
        return Quantities.create(rt60, Units.SECOND);
    }

    /**
     * Calculates the absorption of a surface.
     * 
     * @param area The area of the surface.
     * @param absorptionCoefficient The absorption coefficient (0.0 to 1.0).
     * @return The absorption in Sabins (m^2).
     */
    public static Quantity<Area> calculateAbsorption(Quantity<Area> area, double absorptionCoefficient) {
        return area.multiply(absorptionCoefficient).asType(Area.class);
    }
}
