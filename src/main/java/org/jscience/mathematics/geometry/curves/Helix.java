package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.geometry.ParametricCurve;
import org.jscience.mathematics.geometry.PointND;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.vector.DenseVector;

import java.util.Arrays;

/**
 * Represents a 3D helix (spiral) curve.
 * <p>
 * A helix is a curve that winds around a cylinder at a constant angle.
 * Parametric form: C(t) = (r·cos(t), r·sin(t), h·t)
 * where r is the radius and h is the pitch (height per radian).
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Helix implements ParametricCurve {

    private final Real radius;
    private final Real pitch;
    private final PointND center;

    /**
     * Creates a helix with the given radius and pitch.
     * 
     * @param radius the radius of the helix
     * @param pitch  the pitch (height per radian)
     */
    public Helix(Real radius, Real pitch) {
        this(PointND.of(Real.ZERO, Real.ZERO, Real.ZERO), radius, pitch);
    }

    /**
     * Creates a helix with the given center, radius, and pitch.
     * 
     * @param center the center point (axis passes through this)
     * @param radius the radius of the helix
     * @param pitch  the pitch (height per radian)
     */
    public Helix(PointND center, Real radius, Real pitch) {
        if (center.ambientDimension() != 3) {
            throw new IllegalArgumentException("Helix requires 3D center point");
        }
        this.center = center;
        this.radius = radius;
        this.pitch = pitch;
    }

    @Override
    public PointND at(Real t) {
        Real x = center.get(0).add(radius.multiply(Real.of(Math.cos(t.doubleValue()))));
        Real y = center.get(1).add(radius.multiply(Real.of(Math.sin(t.doubleValue()))));
        Real z = center.get(2).add(pitch.multiply(t));
        return PointND.of(x, y, z);
    }

    @Override
    public int dimension() {
        return 3;
    }

    @Override
    public Vector<Real> tangent(Real t, Real h) {
        // Analytical tangent: C'(t) = (-r·sin(t), r·cos(t), h)
        Real dx = radius.multiply(Real.of(Math.sin(t.doubleValue()))).negate();
        Real dy = radius.multiply(Real.of(Math.cos(t.doubleValue())));
        Real dz = pitch;
        return new DenseVector<>(Arrays.asList(dx, dy, dz), org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public Real curvature(Real t, Real h) {
        // Analytical curvature: κ = r / (r² + h²)
        Real r2 = radius.multiply(radius);
        Real h2 = pitch.multiply(pitch);
        return radius.divide(r2.add(h2));
    }

    /**
     * Returns the radius of the helix.
     * 
     * @return the radius
     */
    public Real getRadius() {
        return radius;
    }

    /**
     * Returns the pitch of the helix.
     * 
     * @return the pitch
     */
    public Real getPitch() {
        return pitch;
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
     * Returns the total height for one complete turn (2π radians).
     * 
     * @return the height per turn
     */
    public Real heightPerTurn() {
        return pitch.multiply(Real.of(2 * Math.PI));
    }
}
