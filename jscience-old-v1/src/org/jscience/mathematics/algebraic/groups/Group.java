package org.jscience.mathematics.algebraic.groups;

/**
 * This interface defines a group.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath Group
 */
public interface Group extends Monoid {
    /**
     * Returns true if one member is the inverse of the other.
     *
     * @param a a group member
     * @param b a group member
     *
     * @return DOCUMENT ME!
     */
    boolean isInverse(Member a, Member b);

/**
     * This interface defines a member of a group.
     */
    interface Member extends Monoid.Member {
        /**
         * Returns the inverse member.
         *
         * @return DOCUMENT ME!
         */
        Member inverse();
    }
}
