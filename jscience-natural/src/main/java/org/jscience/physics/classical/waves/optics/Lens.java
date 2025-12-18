package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.geometry.Point3D;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents an optical lens (thin lens approximation).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Lens {

    private final Real focalLength;
    private final Point3D position;
    private final Real radius;

    /**
     * Creates a lens.
     * 
     * @param focalLength Positive for converging, negative for diverging
     * @param position    Center of the lens
     * @param radius      Radius of the lens aperture
     */
    public Lens(Real focalLength, Point3D position, Real radius) {
        this.focalLength = focalLength;
        this.position = position;
        this.radius = radius;
    }

    /**
     * Helper method to calculate image distance using Thin Lens Equation:
     * 1/f = 1/do + 1/di
     * 
     * @param objectDistance distance from object to lens center (positive)
     * @return image distance (positive = real, negative = virtual)
     */
    public Real calculateImageDistance(Real objectDistance) {
        // 1/di = 1/f - 1/do
        // di = (f * do) / (do - f)
        Real num = focalLength.multiply(objectDistance);
        Real den = objectDistance.subtract(focalLength);

        if (den.isZero()) {
            // Object at focal point -> image at infinity
            // Returning MAX_VALUE as a proxy for infinity if Real doesn't support explicit
            // Infinity
            return Real.of(Double.MAX_VALUE);
        }
        return num.divide(den);
    }

    /**
     * Magnification M = -di / do
     */
    public Real calculateMagnification(Real objectDistance, Real imageDistance) {
        return imageDistance.divide(objectDistance).negate();
    }

    public Real getFocalLength() {
        return focalLength;
    }

    public Point3D getPosition() {
        return position;
    }

    public Real getRadius() {
        return radius;
    }
}
