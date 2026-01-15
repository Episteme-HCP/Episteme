package org.jscience.mathematics.algebraic.lattices;

/**
 * This interface defines a join (or ∨) semilattice.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface JoinSemiLattice {
    /**
     * Returns the supremum element.
     *
     * @return DOCUMENT ME!
     */
    Member one();

    /**
     * Returns true if the member is the supremum element of this
     * group.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     */
    boolean isOne(Member g);

/**
     * This interface defines a member of a join semilattice.
     */
    interface Member extends org.jscience.mathematics.Member {
        /**
         * The group composition law.
         *
         * @param g a group member
         *
         * @return DOCUMENT ME!
         */
        Member join(Member g);
    }
}
