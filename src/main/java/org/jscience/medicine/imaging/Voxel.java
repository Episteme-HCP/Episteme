package org.jscience.medicine.imaging;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;

/**
 * Represents a Voxel (Volumetric Pixel) with physical dimensions.
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class Voxel {

    private final Quantity<Length> spacingX;
    private final Quantity<Length> spacingY;
    private final Quantity<Length> spacingZ;
    private final double value;

    public Voxel(Quantity<Length> spacingX, Quantity<Length> spacingY, Quantity<Length> spacingZ, double value) {
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        this.spacingZ = spacingZ;
        this.value = value;
    }

    public Quantity<Length> getSpacingX() {
        return spacingX;
    }

    public Quantity<Length> getSpacingY() {
        return spacingY;
    }

    public Quantity<Length> getSpacingZ() {
        return spacingZ;
    }

    public double getValue() {
        return value;
    }

    /**
     * Calculates the physical volume of this voxel.
     */
    public Quantity<?> getVolume() {
        return spacingX.multiply(spacingY).multiply(spacingZ);
    }
}
