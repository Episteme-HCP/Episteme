package org.jscience.mathematics.algebraic.groups;

/**
 * This interface defines a monoid (a semigroup with an identity element).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @planetmath Monoid
 */
public interface Monoid extends Semigroup {
    /**
     * Returns the identity element.
     *
     * @return DOCUMENT ME!
     */
    Member identity();

    /**
     * Returns true if the member is the identity element of this
     * monoid.
     *
     * @param g a monoid member
     *
     * @return DOCUMENT ME!
     */
    boolean isIdentity(Member g);

/**
     * This interface defines a member of a monoid.
     */
    interface Member extends Semigroup.Member {
    }
}
