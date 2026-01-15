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
