package org.jscience.mathematics.algebraic.fields;


//import org.jscience.mathematics.algebraic.groups.AbelianGroup;
/**
 * This interface defines a semiring (similar to a ring but without additive
 * inverses).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//an semiring is a set with a commutative monoid operation as addition, together with a monoid operation as multiplication, satisfying distributivity
//and should be defined as
//public interface Semiring extends Monoid {
//   Member one();
//   boolean isOne(Member r);
//    interface Member extends Monoid.Member {
//         Member multiply(Member r);
//    }
//}
//however, it is worth to be defined this way as methods have meaningful names
//also see AbelianGroup.
public interface Semiring {
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
     * Returns the unit element.
     *
     * @return DOCUMENT ME!
     */
    Member one();

    /**
     * Returns true if the member is the unit element.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean isOne(Member r);

/**
     * This interface defines a member of a semiring.
     */
    interface Member extends org.jscience.mathematics.Member {
        /**
         * The addition law.
         *
         * @param g a group member
         *
         * @return DOCUMENT ME!
         */
        Member add(Member g);

        /**
         * The multiplication law.
         *
         * @param r a ring member
         *
         * @return DOCUMENT ME!
         */
        Member multiply(Member r);
    }
}
