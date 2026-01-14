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


//import org.jscience.mathematics.geometry.*;
import org.jscience.geography.coordinates.Coord;

import org.jscience.util.Positioned;


/**
 * A class representing a boundary.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you should feed this class with valid coordinates
//this is actually more than a boundary, it defines for a 2D space regions that are in or out using convex polygons.
//single hole regions are defined with two overlapping polygons.
//may be we should replace this class by org.jscience.geometry.ParametricCurve
public class Boundary extends Object implements Positioned {
    /** DOCUMENT ME! */
    private Coord[] coords;

    /** DOCUMENT ME! */
    private boolean[] boundariesIncluded;

/**
     * Creates a new Boundary object.
     */
    public Boundary() {
        this.coords = new Coord[0];
        boundariesIncluded = new boolean[coords.length];
    }

    //this defines a polygonal boundary of any sort (may be empty): last coordinates should be linked to the zeroth coordinate thus closing the boundary
    /**
     * Creates a new Boundary object.
     *
     * @param coords DOCUMENT ME!
     */
    public Boundary(Coord[] coords) {
        if (coords != null) {
            this.coords = coords;
            boundariesIncluded = new boolean[coords.length];

            for (int i = 0; i < boundariesIncluded.length; i++) {
                boundariesIncluded[i] = true;
            }
        } else {
            throw new IllegalArgumentException(
                "The Boundary constructor can't have null arguments.");
        }
    }

    //last element of the boundariesIncluded is to be used for the closure
    /**
     * Creates a new Boundary object.
     *
     * @param coords DOCUMENT ME!
     * @param boundariesIncluded DOCUMENT ME!
     */
    public Boundary(Coord[] coords, boolean[] boundariesIncluded) {
        if ((coords != null) && (boundariesIncluded.length > 0) &&
                (boundariesIncluded.length == coords.length)) {
            this.coords = coords;
            this.boundariesIncluded = boundariesIncluded;
        } else {
            throw new IllegalArgumentException(
                "The Boundary constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPosition() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord[] getCoords() {
        return coords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean[] getBoundariesInclusion() {
        return boundariesIncluded;
    }

    /**
     * DOCUMENT ME!
     *
     * @param boundary DOCUMENT ME!
     */
    public void union(Boundary boundary) {
        //    if (boundary!=null) {
        //TODO   //this is very complicated code I don't have time to produce now
        //    } else throw new IllegalArgumentException("You can't make the union with null boundary.");
        throw new RuntimeException(
            "Not yet implemented ; use org.jscience.geometry to provide similar function.");
    }

    //public void intersection(Boundary boundary) {
    //    if (boundary!=null) {
    //TODO   //this is very complicated code I don't have time to produce now
    //    } else throw new IllegalArgumentException("You can't make the union with null boundary.");
    //  }

    //we should also provide:
    // public boolean isIncluded(Boundary boundary);
    // public boolean isEmptyIntersection(Boundary boundary);
    //public double computeArea();
    //don't forget to include an implementation for the subclass TimedBoundary
}
