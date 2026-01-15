package org.jscience.mathematics.algebraic.groups;

/**
 * This interface defines a magma (a set with a single binary operation).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we could extend Set here, although I see really no benefit from doing that but confusing the user
public interface Magma {
/**
     * This interface defines a member of a magma.
     */
    interface Member extends org.jscience.mathematics.Member {
        /**
         * The magma composition law.
         *
         * @param g a magma member
         *
         * @return DOCUMENT ME!
         */
        Member compose(Member g);
    }
}
