package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactComplex;


/**
 * The ExactComplexField class encapsulates the field of complex numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class ExactComplexField extends Object implements Field {
    /** DOCUMENT ME! */
    public static final ExactComplex ZERO = new ExactComplex(0.0, 0.0);

    /** DOCUMENT ME! */
    public static final ExactComplex I = new ExactComplex(0.0, 1.0);

    /** DOCUMENT ME! */
    public static final ExactComplex ONE = new ExactComplex(1.0, 0.0);

    /** DOCUMENT ME! */
    public static final ExactComplex MINUS_ONE = new ExactComplex(-1.0, 0.0);

    /** DOCUMENT ME! */
    public static final ExactComplex MINUS_I = new ExactComplex(0.0, -1.0);

    /** DOCUMENT ME! */
    public static final ExactComplex HALF = new ExactComplex(0.5, 0.0);

    /** DOCUMENT ME! */
    public static final ExactComplex MINUS_HALF = new ExactComplex(-0.5, 0.0);

    /** DOCUMENT ME! */
    public static final ExactComplex HALF_I = new ExactComplex(0.0, 0.5);

    /** DOCUMENT ME! */
    public static final ExactComplex MINUS_HALF_I = new ExactComplex(0.0, -0.5);

    /** DOCUMENT ME! */
    public static final ExactComplex TWO = new ExactComplex(2.0, 0.0);

    /** DOCUMENT ME! */
    public static final ExactComplex MINUS_TWO = new ExactComplex(-2.0, 0.0);

    /** DOCUMENT ME! */
    public static final ExactComplex SQRT_HALF = new ExactComplex(Math.sqrt(0.5),
            0.0);

    /** DOCUMENT ME! */
    public static final ExactComplex SQRT_HALF_I = new ExactComplex(0.0,
            Math.sqrt(0.5));

    /** DOCUMENT ME! */
    public static final ExactComplex MINUS_SQRT_HALF_I = new ExactComplex(0.0,
            -Math.sqrt(0.5));

    /**
     * This is the value of PI rounded to that same number of digits as
     * given by Math.PI
     */
    public static final ExactComplex PI = new ExactComplex(Math.PI, 0.0);

    /**
     * This is the value of PI as imaginary rounded to that same number
     * of digits as given by Math.PI
     */
    public static final ExactComplex PI_I = new ExactComplex(0.0, Math.PI);

    /**
     * This is the value of PI/2 rounded to that same number of digits
     * as given by Math.PI/2
     */
    public static final ExactComplex PI_2 = new ExactComplex(Math.PI / 2.0, 0.0);

    /**
     * This is the value of -PI/2 rounded to that same number of digits
     * as given by -Math.PI/2
     */
    public static final ExactComplex MINUS_PI_2 = new ExactComplex(-Math.PI / 2.0,
            0.0);

    /**
     * This is the value of PI/2 as imaginary rounded to that same
     * number of digits as given by Math.PI/2
     */
    public static final ExactComplex PI_2_I = new ExactComplex(0.0,
            Math.PI / 2.0);

    /**
     * This is the value of -PI/2 as imaginary rounded to that same
     * number of digits as given by -Math.PI/2
     */
    public static final ExactComplex MINUS_PI_2_I = new ExactComplex(0.0,
            -Math.PI / 2.0);

    /** DOCUMENT ME! */
    private static ExactComplexField _instance;

/**
     * Constructs a field of complex numbers.
     */
    private ExactComplexField() {
    }

    /**
     * Constructs a field of complex numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ExactComplexField getInstance() {
        if (_instance == null) {
            synchronized (ExactComplexField.class) {
                if (_instance == null) {
                    _instance = new ExactComplexField();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the complex number zero.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Returns true if the complex number is equal to zero.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        return ZERO.equals(g);
    }

    /**
     * Returns true if one complex number is the negative of the other.
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
     * Returns the complex number one.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        return ONE;
    }

    /**
     * Returns true if the complex number is equal to one.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return ONE.equals(r);
    }

    /**
     * Returns true if one complex number is the inverse of the other.
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
