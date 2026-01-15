package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;


/**
 * This interface defines a ring.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath Ring
 */
public interface Ring extends AbelianGroup {
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
     * This interface defines a member of a ring.
     */
    interface Member extends AbelianGroup.Member {
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
