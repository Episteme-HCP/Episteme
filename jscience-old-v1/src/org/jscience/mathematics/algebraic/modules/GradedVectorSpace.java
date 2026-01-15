package org.jscience.mathematics.algebraic.modules;

/**
 * This interface defines a graded space.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface GradedVectorSpace extends VectorSpace {
    /**
     * This interface defines a member of a GradedVectorSpace.
     */
    interface Member extends VectorSpace.Member {
        /**
         * /**
         * Returns the degree of this element.
         */
        public int getDegree();

    }

}
