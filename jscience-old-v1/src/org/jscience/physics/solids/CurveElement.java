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

import org.jscience.physics.solids.load.DistLoad;


/**
 * Abstract class that represents a one-dimensional element topology.  This
 * will be extended by specific element types (rods, bars, etc...)
 *
 * @author Wegge
 */
public abstract class CurveElement extends AtlasElement {
    /**
     * DOCUMENT ME!
     */
    private AtlasNode endA;

    /**
     * DOCUMENT ME!
     */
    private AtlasNode endB;

    /**
     * DOCUMENT ME!
     *
     * @param nodes DOCUMENT ME!
     */
    public void setNodes(AtlasNode[] nodes) {
        endA = nodes[0];
        endB = nodes[1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasNode[] getNodes() {
        AtlasNode[] ret = new AtlasNode[2];
        ret[0] = endA;
        ret[1] = endB;

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param load DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public abstract void contributeDistLoad(DistLoad load, SolutionMatrices m);

    /**
     * Returns the length of the element.
     *
     * @return DOCUMENT ME!
     */
    public double computeLength() {
        return endA.computeDistance(endB);
    }

    /**
     * Sets the first node.
     *
     * @param node DOCUMENT ME!
     */
    public void setNodeA(AtlasNode node) {
        endA = node;
    }

    /**
     * Returns the first node.
     *
     * @return DOCUMENT ME!
     */
    public AtlasNode getEndA() {
        return endA;
    }

    /**
     * Returns the second node.
     *
     * @return DOCUMENT ME!
     */
    public AtlasNode getEndB() {
        return endB;
    }

    /**
     * Sets the second node.
     *
     * @param node DOCUMENT ME!
     */
    public void setNodeB(AtlasNode node) {
        endB = node;
    }
}
