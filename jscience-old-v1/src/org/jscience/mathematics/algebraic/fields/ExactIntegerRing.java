package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactInteger;


/**
 * The ExactIntegerRing class encapsulates the ring of integer numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class ExactIntegerRing extends Object implements Ring {
    /** DOCUMENT ME! */
    public final static ExactInteger ZERO = new ExactInteger(0);

    /** DOCUMENT ME! */
    public final static ExactInteger ONE = new ExactInteger(1);

    //there is no way to get the private constructor for infinity members
    //public final static ExactInteger NEGATIVE_INFINITY=new ExactInteger(true, false);
    //public final static ExactInteger POSITIVE_INFINITY=new ExactInteger(false, true);
    //public final static ExactInteger NaN=new ExactInteger(true, true);
    /** DOCUMENT ME! */
    private static ExactIntegerRing _instance;

/**
     * Constructs a ring of integer numbers.
     */
    private ExactIntegerRing() {
    }

    /**
     * Constructs a ring of integer numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ExactIntegerRing getInstance() {
        if (_instance == null) {
            synchronized (ExactIntegerRing.class) {
                if (_instance == null) {
                    _instance = new ExactIntegerRing();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the integer number zero.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Returns true if the integer number is equal to zero.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        return ZERO.equals(g);
    }

    /**
     * Returns true if one integer number is the negative of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        return ZERO.equals(a.add(b));
    }

    /**
     * Returns the integer number one.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        return ONE;
    }

    /**
     * Returns true if the integer number is equal to one.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return ONE.equals(r);
    }
}
