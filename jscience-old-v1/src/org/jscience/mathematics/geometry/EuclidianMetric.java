package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.Vector;
import org.jscience.mathematics.algebraic.numbers.ComparableNumber;
import org.jscience.mathematics.algebraic.numbers.Double;


/**
 * This class provides an implementation for Metric, with the traditional
 * meaning
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//We could treat this as a special case of Minkowski metric, thus removing this class
public final class EuclidianMetric extends Object implements Metric {
/**
     * Creates a new EuclidianMetric object.
     */
    private EuclidianMetric() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param v1 DOCUMENT ME!
     * @param v2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ComparableNumber getDistance(Vector v1, Vector v2) {
        double dist = 0;
        double diff;

        if ((v1 == null) || (v2 == null)) {
            throw new IllegalArgumentException(
                "Distance from a null vector is undefined.");
        }

        //assert (a != null);
        //assert (b != null);
        if (v1.getDimension() != v2.getDimension()) {
            throw new IllegalArgumentException(
                "Vectors must be of the same dimension.");
        }

        //assert (a.getDimension() == b.getDimension() );
        for (int i = 0; i < v1.getDimension(); i++) {
            diff = Math.abs(v1.getElement(i).doubleValue() -
                    v2.getElement(i).doubleValue());
            dist += (diff * diff);
        }

        return new Double(Math.sqrt(dist));
    }
}
