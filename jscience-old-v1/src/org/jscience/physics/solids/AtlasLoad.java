/*
 * AtlasLoad.java
 *
 * Created on December 29, 2004, 9:17 PM
 */
package org.jscience.physics.solids;

/**
 * Top level abstract object for all model loads.
 *
 * @author Wegge
 */
public abstract class AtlasLoad extends AtlasObject {
    /**
     * Method to contribute contributions to the loads matrix.
     *
     * @param m DOCUMENT ME!
     */
    public abstract void contributeLoad(SolutionMatrices m);
}
