package org.jscience.geography;

import org.jscience.geography.coordinates.Coord2D;

import org.jscience.mathematics.algebraic.Matrix;


/**
 * A class representing a 3D map of geographical sort.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class HeightMap extends Map {
    /** DOCUMENT ME! */
    private Matrix heights;

/**
     * Creates a new HeightMap object.
     *
     * @param name          DOCUMENT ME!
     * @param scale         DOCUMENT ME!
     * @param width         DOCUMENT ME!
     * @param height        DOCUMENT ME!
     * @param topLeftCoords DOCUMENT ME!
     */
    public HeightMap(String name, double scale, double width, double height,
        Coord2D topLeftCoords) {
        super(name, scale, width, height, topLeftCoords);
        this.heights = null;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Matrix getHeights() {
        return heights;
    }

    /**
     * DOCUMENT ME!
     *
     * @param heights DOCUMENT ME!
     */
    public void setHeights(Matrix heights) {
        this.heights = heights;
    }

    //we could add a good bunch of methods here to set individual points, get the coordinates of height point (x, y), 
    //interpolate height for other coordinates
    //we could also provide a bridge to render this in Java3D or as a beans, etc...
    //let us know what you need
}
