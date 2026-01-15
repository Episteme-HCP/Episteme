/*
 * AtlasConstraint.java
 *
 * Created on December 29, 2004, 9:17 PM
 */
package org.jscience.physics.solids;

/**
 * Top level abstract object for all model constraints.
 *
 * @author Wegge
 */
public abstract class AtlasConstraint extends AtlasObject {
    /**
     * Method to contribute stiffness contributions to the stiffness
     * matrix.
     *
     * @param m DOCUMENT ME!
     */
    public abstract void contributeConstraint(SolutionMatrices m);
}
