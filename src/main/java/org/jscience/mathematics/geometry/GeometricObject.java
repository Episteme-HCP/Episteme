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
     * <ul>
     * <li>Point: 0</li>
     * <li>Line/Segment: 1</li>
     * <li>Plane/Surface: 2</li>
     * <li>Volume: 3</li>
     * </ul>
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
     * Returns a human-readable description of this geometric object.
     * 
     * @return description string
     */
    String description();
}
