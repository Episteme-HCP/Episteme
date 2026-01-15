package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.MathConstants;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactReal;


/**
 * The RealField class encapsulates the field of real numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class ExactRealField extends Object implements Field {
    /** DOCUMENT ME! */
    public final static ExactReal ZERO = new ExactReal(0.0);

    /** DOCUMENT ME! */
    public final static ExactReal ONE = new ExactReal(1.0);

    /** DOCUMENT ME! */
    public final static ExactReal PI = new ExactReal(Math.PI);

    /** DOCUMENT ME! */
    public final static ExactReal E = new ExactReal(Math.E);

    /** DOCUMENT ME! */
    public final static ExactReal GAMMA = new ExactReal(MathConstants.GAMMA);

    //there is no way to get the private constructor for infinity members
    //public final static ExactReal NEGATIVE_INFINITY=new ExactReal(true, false);
    //public final static ExactReal POSITIVE_INFINITY=new ExactReal(false, true);
    //public final static ExactReal NaN=new ExactReal(true, true);
    /** DOCUMENT ME! */
    private static ExactRealField _instance;

/**
     * Constructs a field of real numbers.
     */
    private ExactRealField() {
    }

    /**
     * Constructs a field of real numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ExactRealField getInstance() {
        if (_instance == null) {
            synchronized (ExactRealField.class) {
                if (_instance == null) {
                    _instance = new ExactRealField();
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
