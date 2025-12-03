package org.jscience.mathematics.geometry;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.vector.DenseVector;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents an N-dimensional geometric vector.
 * <p>
 * A geometric vector represents a direction and magnitude in N-dimensional
 * space.
 * Unlike points (which represent positions), vectors represent displacements,
 * velocities, forces, etc.
 * </p>
 * <p>
 * Key distinction from algebraic vectors:
 * - VectorND is specifically for geometric operations (cross product, angles,
 * etc.)
 * - Vector<Real> is for general linear algebra
 * - VectorND wraps Vector<Real> but adds geometric semantics
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class VectorND {

    private final Vector<Real> components;

    /**
     * Creates a vector from a list of components.
     * 
     * @param components the vector components
     */
    public VectorND(List<Real> components) {
        if (components == null || components.isEmpty()) {
            throw new IllegalArgumentException("Components cannot be null or empty");
        }
        this.components = new DenseVector<>(components, org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Creates a vector from an algebraic vector.
     * 
     * @param vector the algebraic vector
     */
    public VectorND(Vector<Real> vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector cannot be null");
        }
        this.components = vector;
    }

    /**
     * Creates a vector from varargs components.
     * 
     * @param components the components
     */
    public static VectorND of(Real... components) {
        return new VectorND(Arrays.asList(components));
    }

    /**
     * Creates a vector from double components.
     * 
     * @param components the components
     */
    public static VectorND of(double... components) {
        Real[] reals = new Real[components.length];
        for (int i = 0; i < components.length; i++) {
            reals[i] = Real.of(components[i]);
        }
        return new VectorND(Arrays.asList(reals));
    }

    /**
     * Creates a zero vector of specified dimension.
     * 
     * @param dimension the dimension
     * @return the zero vector
     */
    public static VectorND zero(int dimension) {
        List<Real> zeros = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            zeros.add(Real.ZERO);
        }
        return new VectorND(zeros);
    }

    /**
     * Creates a unit vector in the specified direction.
     * 
     * @param dimension the total dimension
     * @param direction the direction index (0-based)
     * @return the unit vector
     */
    public static VectorND unitVector(int dimension, int direction) {
        if (direction < 0 || direction >= dimension) {
            throw new IllegalArgumentException("Direction must be in [0, " + (dimension - 1) + "]");
        }
        List<Real> components = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            components.add(i == direction ? Real.ONE : Real.ZERO);
        }
        return new VectorND(components);
    }

    /**
     * Returns the dimension of this vector.
     * 
     * @return the dimension
     */
    public int dimension() {
        return components.dimension();
    }

    /**
     * Gets the component at the specified index.
     * 
     * @param index the index (0-based)
     * @return the component value
     */
    public Real get(int index) {
        return components.get(index);
    }

    /**
     * Returns the underlying algebraic vector.
     * 
     * @return the vector
     */
    public Vector<Real> toVector() {
        return components;
    }

    /**
     * Adds another vector to this vector.
     * 
     * @param other the other vector
     * @return the sum
     */
    public VectorND add(VectorND other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        return new VectorND(components.add(other.components));
    }

    /**
     * Subtracts another vector from this vector.
     * 
     * @param other the other vector
     * @return the difference
     */
    public VectorND subtract(VectorND other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        return new VectorND(components.subtract(other.components));
    }

    /**
     * Multiplies this vector by a scalar.
     * 
     * @param scalar the scalar
     * @return the scaled vector
     */
    public VectorND multiply(Real scalar) {
        return new VectorND(components.multiply(scalar));
    }

    /**
     * Divides this vector by a scalar.
     * 
     * @param scalar the scalar
     * @return the scaled vector
     */
    public VectorND divide(Real scalar) {
        List<Real> divided = new ArrayList<>();
        for (int i = 0; i < dimension(); i++) {
            divided.add(get(i).divide(scalar));
        }
        return new VectorND(divided);
    }

    /**
     * Negates this vector.
     * 
     * @return the negated vector
     */
    public VectorND negate() {
        return new VectorND(components.negate());
    }

    /**
     * Computes the dot product with another vector.
     * 
     * @param other the other vector
     * @return the dot product
     */
    public Real dot(VectorND other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        return components.dot(other.components);
    }

    /**
     * Computes the cross product with another vector (3D only).
     * 
     * @param other the other vector
     * @return the cross product
     */
    public VectorND cross(VectorND other) {
        if (this.dimension() != 3 || other.dimension() != 3) {
            throw new IllegalArgumentException("Cross product is only defined for 3D vectors");
        }
        return new VectorND(components.cross(other.components));
    }

    /**
     * Computes the magnitude (norm) of this vector.
     * 
     * @return the magnitude
     */
    public Real norm() {
        return components.norm();
    }

    /**
     * Returns the squared magnitude (avoids square root).
     * 
     * @return the squared magnitude
     */
    public Real normSquared() {
        return dot(this);
    }

    /**
     * Normalizes this vector to unit length.
     * 
     * @return the unit vector
     */
    public VectorND normalize() {
        Real magnitude = norm();
        if (magnitude.equals(Real.ZERO)) {
            throw new ArithmeticException("Cannot normalize zero vector");
        }
        return divide(magnitude);
    }

    /**
     * Checks if this is a unit vector.
     * 
     * @return true if unit vector
     */
    public boolean isUnit() {
        return Math.abs(norm().doubleValue() - 1.0) < 1e-10;
    }

    /**
     * Checks if this is a zero vector.
     * 
     * @return true if zero vector
     */
    public boolean isZero() {
        return norm().doubleValue() < 1e-10;
    }

    /**
     * Computes the angle between this vector and another.
     * 
     * @param other the other vector
     * @return the angle in radians [0, π]
     */
    public Real angleTo(VectorND other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        Real dotProduct = dot(other);
        Real magnitudeProduct = norm().multiply(other.norm());
        Real cosTheta = dotProduct.divide(magnitudeProduct);

        // Clamp to [-1, 1] to avoid numerical errors
        double cosVal = Math.max(-1.0, Math.min(1.0, cosTheta.doubleValue()));

        return Real.of(Math.acos(cosVal));
    }

    /**
     * Checks if this vector is parallel to another.
     * 
     * @param other the other vector
     * @return true if parallel
     */
    public boolean isParallelTo(VectorND other) {
        if (other.dimension() != this.dimension()) {
            return false;
        }

        // Vectors are parallel if cross product is zero (for 3D)
        // or if one is a scalar multiple of the other
        if (dimension() == 3) {
            return cross(other).isZero();
        }

        // General case: check if proportional
        Real ratio = null;
        for (int i = 0; i < dimension(); i++) {
            Real thisComp = get(i);
            Real otherComp = other.get(i);

            if (Math.abs(thisComp.doubleValue()) < 1e-10 &&
                    Math.abs(otherComp.doubleValue()) < 1e-10) {
                continue;
            }

            if (Math.abs(thisComp.doubleValue()) < 1e-10 ||
                    Math.abs(otherComp.doubleValue()) < 1e-10) {
                return false;
            }

            Real currentRatio = thisComp.divide(otherComp);
            if (ratio == null) {
                ratio = currentRatio;
            } else if (Math.abs(ratio.subtract(currentRatio).doubleValue()) > 1e-10) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if this vector is orthogonal to another.
     * 
     * @param other the other vector
     * @return true if orthogonal
     */
    public boolean isOrthogonalTo(VectorND other) {
        return Math.abs(dot(other).doubleValue()) < 1e-10;
    }

    /**
     * Projects this vector onto another vector.
     * 
     * @param onto the vector to project onto
     * @return the projection
     */
    public VectorND projectOnto(VectorND onto) {
        Real scalar = dot(onto).divide(onto.dot(onto));
        return onto.multiply(scalar);
    }

    /**
     * Computes the component of this vector perpendicular to another.
     * 
     * @param to the reference vector
     * @return the perpendicular component
     */
    public VectorND perpendicularTo(VectorND to) {
        return subtract(projectOnto(to));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Vector(");
        for (int i = 0; i < dimension(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(get(i));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof VectorND))
            return false;
        VectorND other = (VectorND) obj;
        return components.equals(other.components);
    }

    @Override
    public int hashCode() {
        return components.hashCode();
    }
}
