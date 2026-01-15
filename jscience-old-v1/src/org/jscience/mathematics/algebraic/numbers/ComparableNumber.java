package org.jscience.mathematics.algebraic.numbers;


//import org.jscience.mathematics.Order;
/**
 * This is the superclass for comparable numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 *
 * @param <T> DOCUMENT ME!
 */

//also see Order
public abstract class ComparableNumber<T extends ComparableNumber>
    extends Number implements Comparable<T> {
    //, Order {
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract T getDistance(ComparableNumber n);

    /**
     * Returns true if this number is NaN.
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isNaN();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract T getNaN();

    /**
     * Returns true if this number is infinite.
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isInfinite();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isPositiveInfinity();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract T getPositiveInfinity();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isNegativeInfinity();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract T getNegativeInfinity();

    /**
     * Returns the min of this number and another, according to
     * compareTo rules.
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public T min(T val) {
        if (compareTo(val) < 0) {
            return (T) this;
        } else {
            return val;
        }
    }

    /**
     * Returns the max of this number and another, according to
     * compareTo rules.
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public T max(T val) {
        if (compareTo(val) < 0) {
            return val;
        } else {
            return (T) this;
        }
    }
}
