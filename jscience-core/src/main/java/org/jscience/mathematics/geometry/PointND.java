package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an N-dimensional point.
 * <p>
 * A point is a position in N-dimensional Euclidean space, represented
 * by N coordinates. Points are distinct from vectors - they represent
 * positions, not directions.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class PointND implements GeometricObject<PointND>, org.jscience.mathematics.topology.MetricSpace<PointND> {

    private final Vector<Real> coordinates;

    /**
     * Creates a point from a list of coordinates.
     * 
     * @param coords the coordinates
     */
    public PointND(List<Real> coords) {
        if (coords == null || coords.isEmpty()) {
            throw new IllegalArgumentException("Coordinates cannot be null or empty");
        }
        this.coordinates = new DenseVector<>(coords, org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Creates a point from a vector.
     * 
     * @param coords the coordinate vector
     */
    public PointND(Vector<Real> coords) {
        if (coords == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        this.coordinates = coords;
    }

    /**
     * Creates a point from varargs coordinates.
     * 
     * @param coords the coordinates
     */
    public static PointND of(Real... coords) {
        return new PointND(Arrays.asList(coords));
    }

    /**
     * Creates a point from double coordinates.
     * 
     * @param coords the coordinates
     */
    public static PointND of(double... coords) {
        Real[] reals = new Real[coords.length];
        for (int i = 0; i < coords.length; i++) {
            reals[i] = Real.of(coords[i]);
        }
        return new PointND(Arrays.asList(reals));
    }

    @Override
    public int dimension() {
        return 0; // A point has intrinsic dimension 0
    }

    @Override
    public int ambientDimension() {
        return coordinates.dimension();
    }

    /**
     * Gets the coordinate at the specified index.
     * 
     * @param index the index (0-based)
     * @return the coordinate value
     */
    public Real get(int index) {
        return coordinates.get(index);
    }

    /**
     * Returns the coordinates as a vector.
     * 
     * @return the coordinate vector
     */
    public Vector<Real> toVector() {
        return coordinates;
    }

    public boolean containsPoint(PointND point) {
        return this.equals(point);
    }

    @Override
    public boolean contains(PointND point) {
        return containsPoint(point);
    }

    /**
     * Computes the Euclidean distance to another point.
     * 
     * @param other the other point
     * @return the distance
     */
    // MetricSpace implementation
    @Override
    public Real distance(PointND a, PointND b) {
        return a.distanceTo(b);
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

    public Real distanceTo(PointND other) {
        if (other.ambientDimension() != this.ambientDimension()) {
            throw new IllegalArgumentException("Points must have same dimension");
        }
        return coordinates.subtract(other.coordinates).norm();
    }

    /**
     * Translates this point by a vector.
     * 
     * @param vector the translation vector
     * @return the translated point
     */
    public PointND translate(Vector<Real> vector) {
        if (vector.dimension() != this.ambientDimension()) {
            throw new IllegalArgumentException("Vector dimension must match point dimension");
        }
        return new PointND(coordinates.add(vector));
    }

    /**
     * Computes the midpoint between this point and another.
     * 
     * @param other the other point
     * @return the midpoint
     */
    public PointND midpoint(PointND other) {
        if (other.ambientDimension() != this.ambientDimension()) {
            throw new IllegalArgumentException("Points must have same dimension");
        }
        Vector<Real> mid = coordinates.add(other.coordinates).multiply(Real.of(0.5));
        return new PointND(mid);
    }

    /**
     * Linear interpolation between this point and another.
     * <p>
     * Returns: this + t*(other - this)
     * - t=0 gives this
     * - t=1 gives other
     * - t=0.5 gives midpoint
     * </p>
     * 
     * @param other the other point
     * @param t     the interpolation parameter
     * @return the interpolated point
     */
    public PointND interpolate(PointND other, Real t) {
        if (other.ambientDimension() != this.ambientDimension()) {
            throw new IllegalArgumentException("Points must have same dimension");
        }
        Vector<Real> diff = other.coordinates.subtract(coordinates);
        Vector<Real> result = coordinates.add(diff.multiply(t));
        return new PointND(result);
    }

    @Override
    public String description() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Point(");
        for (int i = 0; i < coordinates.dimension(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(coordinates.get(i));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PointND))
            return false;
        PointND other = (PointND) obj;
        return coordinates.equals(other.coordinates);
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }
}
