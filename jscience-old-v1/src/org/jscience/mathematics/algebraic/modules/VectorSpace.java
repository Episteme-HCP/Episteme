package org.jscience.mathematics.algebraic.modules;

import org.jscience.mathematics.algebraic.fields.Field;


/**
 * This interface defines a vector space.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath VectorSpace
 */
public interface VectorSpace extends Module {
/**
     * This interface defines a member of a vector space, i.e. a vector.
     *
     * @planetmath Vector
     */
    interface Member extends Module.Member {
        /**
         * The scalar multiplication law with inverse.
         *
         * @param f a field member
         *
         * @return DOCUMENT ME!
         */
        Member scalarDivide(Field.Member f);
    }
}
