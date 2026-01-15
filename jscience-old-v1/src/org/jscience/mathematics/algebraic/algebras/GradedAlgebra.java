package org.jscience.mathematics.algebraic.algebras;

/**
 * This interface defines a graded algebra.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface GradedAlgebra extends Algebra {
/**
     * This interface defines a member of a GradedAlgebra .
     */
    interface Member extends Algebra.Member {
        /**
         * Returns the degree of this element.
         *
         * @return DOCUMENT ME!
         */
        public int getDegree();
    }
}
