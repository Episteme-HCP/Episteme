package org.jscience.mathematics.algebraic.algebras;

/**
 * This interface defines a C<sup>*</sup>-algebra.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath CAlgebra
 */
public interface CStarAlgebra extends Algebra {
/**
     * This interface defines a member of a C<sup>*</sup>-algebra.
     */
    interface Member extends Algebra.Member, BanachSpace.Member {
        /**
         * The involution operation.
         *
         * @return DOCUMENT ME!
         */
        Member involution();
    }
}
