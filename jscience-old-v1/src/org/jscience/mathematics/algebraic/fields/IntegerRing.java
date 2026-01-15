package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Integer;


/**
 * The IntegerRing class encapsulates the ring of integer numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class IntegerRing extends Object implements Ring {
    /** DOCUMENT ME! */
    public final static Integer ZERO = new Integer(0);

    /** DOCUMENT ME! */
    public final static Integer ONE = new Integer(1);

    /** DOCUMENT ME! */
    public final static Integer NEGATIVE_INFINITY = new Integer(Integer.NEGATIVE_INFINITY);

    /** DOCUMENT ME! */
    public final static Integer POSITIVE_INFINITY = new Integer(Integer.POSITIVE_INFINITY);

    /** DOCUMENT ME! */
    private static IntegerRing _instance;

/**
     * Constructs a ring of integer numbers.
     */
    private IntegerRing() {
    }

    /**
     * Constructs a ring of integer numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final IntegerRing getInstance() {
        if (_instance == null) {
            synchronized (IntegerRing.class) {
                if (_instance == null) {
                    _instance = new IntegerRing();
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
