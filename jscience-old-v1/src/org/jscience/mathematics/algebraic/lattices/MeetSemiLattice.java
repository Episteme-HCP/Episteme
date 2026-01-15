package org.jscience.mathematics.algebraic.lattices;

/**
 * This interface defines a meet (or ∧) semilattice.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface MeetSemiLattice {
    /**
     * Returns the infimum element.
     *
     * @return DOCUMENT ME!
     */
    Member zero();

    /**
     * Returns true if the member is the infimum element of this group.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     */
    boolean isZero(Member g);

/**
     * This interface defines a member of a meet lattice.
     */
    interface Member extends org.jscience.mathematics.Member {
        /**
         * The group composition law.
         *
         * @param g a group member
         *
         * @return DOCUMENT ME!
         */
        Member meet(Member g);
    }
}
