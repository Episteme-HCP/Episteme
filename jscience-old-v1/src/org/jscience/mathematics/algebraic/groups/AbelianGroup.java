package org.jscience.mathematics.algebraic.groups;

/**
 * This interface defines an abelian group.
 *
 * @author Mark Hale
 * @version 1.0
 */

//an abelian group is a commutative group and should be defined as
//public interface AbelianGroup extends Group {
//    interface Member extends Group.Member {
//    }
//}
//however, it is worth to be defined this way as methods have meaningful names
public interface AbelianGroup {
    /**
     * Returns the identity element.
     *
     * @return DOCUMENT ME!
     */
    Member zero();

    /**
     * Returns true if the member is the identity element of this
     * group.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     */
    boolean isZero(Member g);

    /**
     * Returns true if one member is the negative of the other.
     *
     * @param a a group member
     * @param b a group member
     *
     * @return DOCUMENT ME!
     */
    boolean isNegative(Member a, Member b);

/**
     * This interface defines a member of an abelian group.
     */
    interface Member extends org.jscience.mathematics.Member {
        /**
         * The group composition law.
         *
         * @param g a group member
         *
         * @return DOCUMENT ME!
         */
        Member add(Member g);

        /**
         * Returns the inverse member.
         *
         * @return DOCUMENT ME!
         */
        Member negate();

        /**
         * The group composition law with inverse.
         *
         * @param g a group member
         *
         * @return DOCUMENT ME!
         */
        Member subtract(Member g);
    }
}
