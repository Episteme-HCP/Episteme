package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.modules.VectorSpace;


/**
 * This interface defines a Banach space.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath BanachSpace
 */
public interface BanachSpace extends VectorSpace {
/**
     * This interface defines a member of a Banach Space.
     */
    interface Member extends VectorSpace.Member {
        /**
         * Returns the norm.
         *
         * @return DOCUMENT ME!
         */
        double norm();
    }
}
