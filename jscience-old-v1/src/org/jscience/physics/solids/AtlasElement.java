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
