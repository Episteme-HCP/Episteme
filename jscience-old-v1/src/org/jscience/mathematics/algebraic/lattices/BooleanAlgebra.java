package org.jscience.mathematics.algebraic.lattices;

/**
 * This interface defines a boolean algebra.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//our boolean algebra is an algebra and should extend Algebra interface I believe
public interface BooleanAlgebra extends Lattice {
/**
     * This interface defines a member of a boolean algebra.
     */
    interface Member extends Lattice.Member {
        /**
         * The complement (or ¬).
         *
         * @return DOCUMENT ME!
         */
        Member complement();
    }
}
