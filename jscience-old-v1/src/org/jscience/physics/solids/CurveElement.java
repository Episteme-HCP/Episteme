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
