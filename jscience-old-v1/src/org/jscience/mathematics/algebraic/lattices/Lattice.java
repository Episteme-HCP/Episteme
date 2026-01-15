package org.jscience.mathematics.algebraic.lattices;

/**
 * This interface defines a (ordered) lattice.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Lattice extends JoinSemiLattice, MeetSemiLattice {
/**
     * This interface defines a member of a join semilattice.
     */
    interface Member extends JoinSemiLattice.Member, MeetSemiLattice.Member {
    }
}
