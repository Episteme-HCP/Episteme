package org.jscience.mathematics.algebraic.fields;

/**
 * This interface defines a kleene algebra.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//perhaps we should also extend order here
public interface KleeneAlgebra extends Semiring {
/**
     * This interface defines a member of a Kleene algebra.
     */
    interface Member extends Semiring.Member {
        /**
         * The kleene star law.
         *
         * @return DOCUMENT ME!
         */
        Member star();
    }
}
