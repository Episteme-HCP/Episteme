package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.Vector;
import org.jscience.mathematics.algebraic.numbers.ComparableNumber;


/**
 * This interface defines a scheme for distance related methods.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Metric {
    //Vectors should be of the same dimension
    /**
     * DOCUMENT ME!
     *
     * @param v1 DOCUMENT ME!
     * @param v2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComparableNumber getDistance(Vector v1, Vector v2);
}
