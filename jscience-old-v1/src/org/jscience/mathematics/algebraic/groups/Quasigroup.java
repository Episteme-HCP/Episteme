package org.jscience.mathematics.algebraic.groups;

/**
 * This interface defines a quasigroup (a magma in which division is always
 * possible, not necessarily associative).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Quasigroup {
/**
     * This interface defines a member of a Quasigroup.
     */
    interface Member extends Magma.Member {
    }
}
