package org.jscience.mathematics.algebraic.groups;

/**
 * This interface defines a loop (a quasigroup with an identity element).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Loop extends Quasigroup {
    /**
     * Returns the identity element.
     *
     * @return DOCUMENT ME!
     */
    Member identity();

    /**
     * Returns true if the member is the identity element of this loop.
     *
     * @param g a loop member
     *
     * @return DOCUMENT ME!
     */
    boolean isIdentity(Member g);

/**
     * This interface defines a member of a loop.
     */
    interface Member extends Quasigroup.Member {
    }
}
