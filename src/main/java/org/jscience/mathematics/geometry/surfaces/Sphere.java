package org.jscience.mathematics.geometry.surfaces;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.geometry.ParametricSurface;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.Arrays;

/**
 * Represents a sphere as a parametric surface.
 * <p>
 * Spherical coordinates parametrization:
 * S(θ,φ) = center + (r*sin(θ)*cos(φ), r*sin(θ)*sin(φ), r*cos(θ))
 * where θ ∈ [0,π] is the polar angle and φ ∈ [0,2π] is the azimuthal angle.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Sphere implements ParametricSurface {

    private final PointND center;
    private final Real radius;

    /**
     * Creates a sphere.
     * 
     * @param center the center point (must be 3D)
     * @param radius the radius
     */
    public Sphere(PointND center, Real radius) {
        if (center.ambientDimension() != 3) {
            throw new IllegalArgumentException("Sphere requires 3D center");
        }
        if (radius.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        this.center = center;
        this.radius = radius;
    }

    @Override
    public PointND at(Real theta, Real phi) {
        // x = r*sin(θ)*cos(φ)
        // y = r*sin(θ)*sin(φ)
        // z = r*cos(θ)

        double t = theta.doubleValue();
        double p = phi.doubleValue();
        double r = radius.doubleValue();

        Real x = Real.of(r * Math.sin(t) * Math.cos(p));
        Real y = Real.of(r * Math.sin(t) * Math.sin(p));
        Real z = Real.of(r * Math.cos(t));

        Vector<Real> offset = new DenseVector<>(
                Arrays.asList(x, y, z),
                org.jscience.mathematics.sets.Reals.getInstance());

        return new PointND(center.toVector().add(offset));
    }

    @Override
    public Vector<Real> normal(Real theta, Real phi, Real h) {
        // For a sphere, the normal at any point is simply the radial direction
        // N = (point - center) / ||point - center||

        PointND point = at(theta, phi);
        Vector<Real> radial = point.toVector().subtract(center.toVector());
        return radial.normalize();
    }

    @Override
    public Real gaussianCurvature(Real theta, Real phi, Real h) {
        // Sphere has constant Gaussian curvature K = 1/r²
        return Real.ONE.divide(radius.multiply(radius));
    }

    /**
     * Returns the surface area of the sphere.
     * 
     * @return 4πr²
     */
    public Real surfaceArea() {
        return radius.multiply(radius).multiply(Real.of(4 * Math.PI));
    }

    /**
     * Returns the volume of the sphere.
     * 
     * @return (4/3)πr³
     */
    public Real volume() {
        return radius.pow(3).multiply(Real.of(4.0 / 3.0 * Math.PI));
    }

    /**
     * Returns the center of the sphere.
     * 
     * @return the center point
     */
    public PointND getCenter() {
        return center;
    }

    /**
     * Returns the radius of the sphere.
     * 
     * @return the radius
     */
    public Real getRadius() {
        return radius;
    }

    /**
     * Checks if a point is on the surface of the sphere.
     * 
     * @param point     the point to check
     * @param tolerance the tolerance
     * @return true if on surface
     */
    public boolean contains(PointND point, Real tolerance) {
        Real distance = center.distanceTo(point);
        return distance.subtract(radius).abs().compareTo(tolerance) < 0;
    }

    @Override
    public String toString() {
        return "Sphere(center=" + center + ", radius=" + radius + ")";
    }
}
