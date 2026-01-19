package org.jscience.geography.coordinates;

/**
 * Coordinate Interface.
 * Represents a point in a spatial reference frame.
 */
public interface Coordinates {
    
    /**
     * Returns the dimensionality of this coordinate.
     */
    int getDimension();

    /**
     * Returns the value for a specific dimension index.
     */
    double get(int dimension);
}
