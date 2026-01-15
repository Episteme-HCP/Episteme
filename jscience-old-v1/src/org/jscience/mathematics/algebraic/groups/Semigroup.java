package org.jscience.mathematics.algebraic.groups;

/**
 * This interface defines a semigroup (an associative magma).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @planetmath Semigroup
 */
public interface Semigroup extends Magma {
/**
     * This interface defines a member of a semigroup.
     */
    interface Member extends Magma.Member {
    }
}
