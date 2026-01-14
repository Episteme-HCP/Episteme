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
 * CurveElement.java
 *
 * Created on December 30, 2004, 1:40 AM
 */
package org.jscience.physics.solids;

import org.jscience.physics.solids.load.Traction1D;


/**
 * Abstract class that represents a one-dimensional element topology.  This
 * will be extended by specific element types (rods, bars, etc...)
 *
 * @author Wegge
 */
public abstract class AreaElement extends AtlasElement {
    /**
     * DOCUMENT ME!
     */
    protected AtlasNode[] nodeList;

    /**
     * DOCUMENT ME!
     */
    protected double area;

    /**
     * DOCUMENT ME!
     */
    protected double thickness;

    /**
     * DOCUMENT ME!
     */
    protected AtlasMaterial mat;

    /**
     * 
    DOCUMENT ME!
     *
     * @return Returns the mat.
     */
    public AtlasMaterial getMat() {
        return mat;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param mat The mat to set.
     */
    public void setMat(AtlasMaterial mat) {
        this.mat = mat;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return Returns the thickness.
     */
    public double getThickness() {
        return thickness;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param thickness The thickness to set.
     */
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nodes DOCUMENT ME!
     */
    public void setNodes(AtlasNode[] nodes) {
        nodeList = nodes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasNode[] getNodes() {
        return nodeList;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getArea() {
        return area;
    }

    /**
     * DOCUMENT ME!
     *
     * @param load DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public abstract void contributeTractionLoad(Traction1D load,
        SolutionMatrices m);
}
