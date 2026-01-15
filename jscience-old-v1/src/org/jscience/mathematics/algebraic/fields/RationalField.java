package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Rational;


/**
 * The RationalField class encapsulates the field of rational numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class RationalField extends Object implements Field {
    /** DOCUMENT ME! */
    public final static Rational ZERO = new Rational(0);

    /** DOCUMENT ME! */
    public final static Rational ONE = new Rational(1);

    /** DOCUMENT ME! */
    public final static Rational NEGATIVE_INFINITY = new Rational(Rational.NEGATIVE_INFINITY);

    /** DOCUMENT ME! */
    public final static Rational POSITIVE_INFINITY = new Rational(Rational.POSITIVE_INFINITY);

    /** DOCUMENT ME! */
    private static RationalField _instance;

/**
     * Constructs a field of rational numbers.
     */
    private RationalField() {
    }

    /**
     * Constructs a field of rational numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final RationalField getInstance() {
        if (_instance == null) {
            synchronized (RationalField.class) {
                if (_instance == null) {
                    _instance = new RationalField();
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
