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

/*
 * FiniteElement.java
 *
 * Created on December 29, 2004, 9:17 PM
 */
package org.jscience.physics.solids;

import org.jscience.physics.solids.geom.AtlasCoordSys;

import java.util.ArrayList;


/**
 * Top level abstract object for all finite elements.
 *
 * @author Wegge
 */
public abstract class AtlasElement extends AtlasObject {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract AtlasCoordSys getLocalCoordSys();

    /**
     * Method to contribute stiffness contributions to the stiffness
     * matrix.
     *
     * @param m DOCUMENT ME!
     *
     * @throws AtlasException DOCUMENT ME!
     */
    public abstract void contributeMatrices(SolutionMatrices m)
        throws AtlasException;

    /**
     * Returns all of the nodes referenced by this element.
     *
     * @return DOCUMENT ME!
     */
    public abstract AtlasNode[] getNodes();

    /**
     * Returns the number of nodes referenced by this element.
     *
     * @return DOCUMENT ME!
     */
    public int getNumberNodes() {
        return getNodes().length;
    }

    /**
     * Computes the volume of the element.
     *
     * @param m DOCUMENT ME!
     *
     * @throws AtlasException DOCUMENT ME!
     */
    public abstract void computeResults(SolutionMatrices m)
        throws AtlasException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract ArrayList getResults();
}
