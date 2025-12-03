package org.jscience.mathematics.geometry.surfaces;

import org.jscience.mathematics.geometry.ParametricSurface;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.vector.DenseVector;

import java.util.Arrays;

/**
 * Represents a cylindrical surface.
 * <p>
 * A cylinder is a surface of revolution with constant radius.
 * Parametric form: S(θ, z) = (r·cos(θ), r·sin(θ), z)
 * where θ ∈ [0, 2π] and z ∈ [z_min, z_max]
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Cylinder implements ParametricSurface {

    private final PointND center;
    private final Real radius;
    private final Real height;

    /**
     * Creates a cylinder with the given radius and height.
     * 
     * @param radius the radius
     * @param height the height
     */
    public Cylinder(Real radius, Real height) {
        this(PointND.of(Real.ZERO, Real.ZERO, Real.ZERO), radius, height);
    }

    /**
     * Creates a cylinder with the given center, radius, and height.
     * 
     * @param center the center of the base
     * @param radius the radius
     * @param height the height
     */
    public Cylinder(PointND center, Real radius, Real height) {
        if (center.ambientDimension() != 3) {
            throw new IllegalArgumentException("Cylinder requires 3D center point");
        }
        this.center = center;
        this.radius = radius;
        this.height = height;
    }

    @Override
    public PointND at(Real u, Real v) {
        // u = θ ∈ [0, 2π], v = z ∈ [0, 1] (normalized)
        Real x = center.get(0).add(radius.multiply(Real.of(Math.cos(u.doubleValue()))));
        Real y = center.get(1).add(radius.multiply(Real.of(Math.sin(u.doubleValue()))));
        Real z = center.get(2).add(height.multiply(v));
        return PointND.of(x, y, z);
    }

    @Override
    public Vector<Real> partialU(Real u, Real v, Real h) {
        // ∂S/∂θ = (-r·sin(θ), r·cos(θ), 0)
        Real dx = radius.multiply(Real.of(Math.sin(u.doubleValue()))).negate();
        Real dy = radius.multiply(Real.of(Math.cos(u.doubleValue())));
        Real dz = Real.ZERO;
        return new DenseVector<>(Arrays.asList(dx, dy, dz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Vector<Real> partialV(Real u, Real v, Real h) {
        // ∂S/∂z = (0, 0, h)
        return new DenseVector<>(Arrays.asList(Real.ZERO, Real.ZERO, height), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Vector<Real> normal(Real u, Real v, Real h) {
        // Normal = (cos(θ), sin(θ), 0) (outward pointing)
        Real nx = Real.of(Math.cos(u.doubleValue()));
        Real ny = Real.of(Math.sin(u.doubleValue()));
        Real nz = Real.ZERO;
        return new DenseVector<>(Arrays.asList(nx, ny, nz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Real gaussianCurvature(Real u, Real v, Real h) {
        // Gaussian curvature of cylinder is always 0
        return Real.ZERO;
    }

    /**
     * Returns the radius of the cylinder.
     * 
     * @return the radius
     */
    public Real getRadius() {
        return radius;
    }

    /**
     * Returns the height of the cylinder.
     * 
     * @return the height
     */
    public Real getHeight() {
        return height;
    }

    /**
     * Returns the center point.
     * 
     * @return the center
     */
    public PointND getCenter() {
        return center;
    }

    /**
     * Returns the lateral surface area (excluding top and bottom).
     * A = 2πrh
     * 
     * @return the lateral surface area
     */
    public Real lateralSurfaceArea() {
        return Real.of(2 * Math.PI).multiply(radius).multiply(height);
    }

    /**
     * Returns the total surface area (including top and bottom).
     * A = 2πr(r + h)
     * 
     * @return the total surface area
     */
    public Real totalSurfaceArea() {
        return Real.of(2 * Math.PI).multiply(radius).multiply(radius.add(height));
    }

    /**
     * Returns the volume of the cylinder.
     * V = πr²h
     * 
     * @return the volume
     */
    public Real volume() {
        return Real.of(Math.PI).multiply(radius).multiply(radius).multiply(height);
    }
}
