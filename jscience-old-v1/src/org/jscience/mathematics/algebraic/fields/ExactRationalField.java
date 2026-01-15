package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactRational;


/**
 * The RationalField class encapsulates the field of rational numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class ExactRationalField extends Object implements Field {
    /** DOCUMENT ME! */
    public final static ExactRational ZERO = new ExactRational(0);

    /** DOCUMENT ME! */
    public final static ExactRational ONE = new ExactRational(1);

    //there is no way to get the private constructor for infinity members
    //public final static ExactRational NEGATIVE_INFINITY=new ExactRational(true, false);
    //public final static ExactRational POSITIVE_INFINITY=new ExactRational(false, true);
    //public final static ExactRational NaN=new ExactRational(true, true);
    /** DOCUMENT ME! */
    private static ExactRationalField _instance;

/**
     * Constructs a field of rational numbers.
     */
    private ExactRationalField() {
    }

    /**
     * Constructs a field of rational numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ExactRationalField getInstance() {
        if (_instance == null) {
            synchronized (ExactRationalField.class) {
                if (_instance == null) {
                    _instance = new ExactRationalField();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the real number zero.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Returns true if the real number is equal to zero.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        return ZERO.equals(g);
    }

    /**
     * Returns true if one real number is the negative of the other.
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
     * Returns the real number one.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        return ONE;
    }

    /**
     * Returns true if the real number is equal to one.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return ONE.equals(r);
    }

    /**
     * Returns true if one real number is the inverse of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInverse(Field.Member a, Field.Member b) {
        return ONE.equals(a.multiply(b));
    }
}
