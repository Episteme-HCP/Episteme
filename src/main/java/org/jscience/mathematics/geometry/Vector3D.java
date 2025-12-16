package org.jscience.mathematics.geometry;

import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import java.util.Arrays;

/**
 * Represents a 3D vector.
 * <p>
 * providing convenient constructors and accessors.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Vector3D extends DenseVector<Real> implements
        org.jscience.mathematics.topology.MetricSpace<org.jscience.mathematics.linearalgebra.Vector<Real>> {

    private static final Reals REALS = Reals.getInstance();

    public static final Vector3D ZERO = new Vector3D(0, 0, 0);
    public static final Vector3D X_AXIS = new Vector3D(1, 0, 0);
    public static final Vector3D Y_AXIS = new Vector3D(0, 1, 0);
    public static final Vector3D Z_AXIS = new Vector3D(0, 0, 1);

    /**
     * Creates a new Vector3D from double coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector3D(double x, double y, double z) {
        super(Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), REALS);
    }

    /**
     * Creates a new Vector3D from Real coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector3D(Real x, Real y, Real z) {
        super(Arrays.asList(x, y, z), REALS);
    }

    public Real x() {
        return get(0);
    }

    public Real y() {
        return get(1);
    }

    public Real z() {
        return get(2);
    }

    /**
     * Returns the cross product of this vector and another.
     * Overridden to return Vector3D type.
     */
    public Vector3D cross(Vector3D other) {
        Real x1 = this.x();
        Real y1 = this.y();
        Real z1 = this.z();
        Real x2 = other.x();
        Real y2 = other.y();
        Real z2 = other.z();

        Real cx = y1.multiply(z2).subtract(z1.multiply(y2));
        Real cy = z1.multiply(x2).subtract(x1.multiply(z2));
        Real cz = x1.multiply(y2).subtract(y1.multiply(x2));

        return new Vector3D(cx, cy, cz);
    }

    public double getX() {
        return x().doubleValue();
    }

    public double getY() {
        return y().doubleValue();
    }

    public double getZ() {
        return z().doubleValue();
    }

    public Vector3D normalize() {
        Real n = this.normValue();
        if (n.equals(Real.ZERO)) {
            return new Vector3D(0, 0, 0);
        }
        return this.divide(n);
    }

    public Real normValue() {
        return x().multiply(x()).add(y().multiply(y())).add(z().multiply(z())).sqrt();
    }

    public Vector3D multiply(Real scalar) {
        return new Vector3D(x().multiply(scalar), y().multiply(scalar), z().multiply(scalar));
    }

    public Vector3D divide(Real scalar) {
        return new Vector3D(x().divide(scalar), y().divide(scalar), z().divide(scalar));
    }

    public Vector3D negate() {
        return new Vector3D(x().negate(), y().negate(), z().negate());
    }

    @Override
    public Real distance(org.jscience.mathematics.linearalgebra.Vector<Real> a,
            org.jscience.mathematics.linearalgebra.Vector<Real> b) {
        return a.subtract(b).norm();
    }

    // MetricSpace / Set methods
    @Override
    public boolean containsPoint(org.jscience.mathematics.linearalgebra.Vector<Real> p) {
        return this.equals(p);
    }

    @Override
    public boolean contains(org.jscience.mathematics.linearalgebra.Vector<Real> p) {
        return containsPoint(p);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isClosed() {
        return true;
    }

    public Real dot(Vector3D other) {
        return x().multiply(other.x()).add(y().multiply(other.y())).add(z().multiply(other.z()));
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(x().subtract(other.x()), y().subtract(other.y()), z().subtract(other.z()));
    }

    public Vector3D add(Vector3D other) {
        return new Vector3D(x().add(other.x()), y().add(other.y()), z().add(other.z()));
    }
}
