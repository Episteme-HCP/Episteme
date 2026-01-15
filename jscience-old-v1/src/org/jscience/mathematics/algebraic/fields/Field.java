package org.jscience.mathematics.algebraic.fields;

/**
 * This interface defines a field (a commutative division ring).
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath Field
 */

//this class also would define a DivisionRing
public interface Field extends Ring {
    /**
     * Returns true if one member is the inverse of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean isInverse(Member a, Member b);

/**
     * This interface defines a member of a field.
     */
    interface Member extends Ring.Member {
        /**
         * Returns the inverse member.
         *
         * @return DOCUMENT ME!
         */
        Member inverse();

        /**
         * The multiplication law with inverse.
         *
         * @param f a field member
         *
         * @return DOCUMENT ME!
         */
        Member divide(Member f);
    }
}
