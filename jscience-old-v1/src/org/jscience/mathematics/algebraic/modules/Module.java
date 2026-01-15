package org.jscience.mathematics.algebraic.modules;

import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;


/**
 * This interface defines a module.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath Module
 */
public interface Module extends AbelianGroup {
/**
     * This interface defines a member of a module.
     */
    interface Member extends AbelianGroup.Member {
        /**
         * The scalar multiplication law.
         *
         * @param r a ring member
         *
         * @return DOCUMENT ME!
         */
        Member scalarMultiply(Ring.Member r);
    }
}
