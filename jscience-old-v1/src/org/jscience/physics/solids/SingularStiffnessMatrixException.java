/*
 * SingularStiffnessMatrixException.java
 *
 * Created on December 31, 2004, 1:28 AM
 */
package org.jscience.physics.solids;

/**
 * Exception that gets thrown when the stiffness matrix is singular.
 *
 * @author Wegge
 */
public class SingularStiffnessMatrixException extends InvalidSolutionException {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 3426995140797959690L;

/**
     * Creates a new instance of InvalidSolutionException
     */
    public SingularStiffnessMatrixException(String message) {
        super(message);
    }
}
