/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
