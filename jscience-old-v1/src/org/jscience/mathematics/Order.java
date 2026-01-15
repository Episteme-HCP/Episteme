package org.jscience.mathematics;

/**
 * This interface defines an order relation.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Order {
    /**
     * Returns true if one member is inferior or equal to.
     *
     * @param a a group member
     * @param b a group member
     *
     * @return DOCUMENT ME!
     */
    boolean isInferiorOrEqualTo(Member a, Member b);
}
