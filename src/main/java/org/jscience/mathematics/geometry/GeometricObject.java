package org.jscience.mathematics.geometry;

/**
 * Base interface for all geometric objects.
 * <p>
 * A geometric object is any mathematical entity that exists in a geometric
 * space,
 * such as points, lines, planes, curves, surfaces, etc.
 * </p>
 * 
 * @param <T> the type representing points in the space
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface GeometricObject<T> {

    /**
     * Returns the intrinsic dimension of this geometric object.
     * <p>
     * Examples:
     * - Point: 0
     * - Line: 1
     * - Plane: 2
     * - Volume: 3
     * </p>
     * 
     * @return the dimension
     */
    int dimension();

    /**
     * Returns the dimension of the ambient space containing this object.
     * <p>
     * For example, a 2D plane in 3D space has dimension=2 but ambientDimension=3.
     * </p>
     * 
     * @return the ambient space dimension
     */
    int ambientDimension();

    /**
     * Checks if a point lies on or within this geometric object.
     * <p>
     * The exact meaning depends on the object type:
     * - Point: equality check
     * - Line: point is on the line
     * - Plane: point is on the plane
     * - Volume: point is inside the volume
     * </p>
     * 
     * @param point the point to test
     * @return true if the point is contained in this object
     */
    boolean contains(T point);

    /**
     * Returns a human-readable description of this geometric object.
     * 
     * @return description string
     */
    String description();
}

