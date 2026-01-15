package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactComplex;
import org.jscience.mathematics.algebraic.numbers.ExactReal;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public class ExactComplexPolynomialRing implements Ring {
    /** DOCUMENT ME! */
    private static final ExactComplexPolynomial ZERO = new ExactComplexPolynomial(new ExactComplex[] {
                ExactComplex.ZERO
            });

    /** DOCUMENT ME! */
    private static final ExactComplexPolynomial ONE = new ExactComplexPolynomial(new ExactComplex[] {
                ExactComplex.ONE
            });

    /** DOCUMENT ME! */
    private static ExactComplexPolynomialRing _instance;

/**
     * Creates a new instance of ExactComplexPolynomialRing
     */
    protected ExactComplexPolynomialRing() {
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ExactComplexPolynomialRing getInstance() {
        if (_instance == null) {
            synchronized (ExactComplexPolynomialRing.class) {
                if (_instance == null) {
                    _instance = new ExactComplexPolynomialRing();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns true if one member is the negative of the other.
     *
     * @param a a group member
     * @param b a group member
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        return a.add(b).equals(ZERO);
    }

    /**
     * Returns true if the member is the unit element.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return r.equals(ONE);
    }

    /**
     * Returns true if the member is the identity element of this
     * group.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        return g.equals(ZERO);
    }

    /**
     * Returns the unit element.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        return ONE;
    }

    /**
     * Returns the identity element.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Internal method for typesafe cast
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected static ExactComplex[] toExactComplex(Field.Member[] f) {
        ExactComplex[] _c = null;

        if (f == null) {
            _c = new ExactComplex[] { ExactComplex.ZERO };
        }

        if (f.length == 0) {
            _c = new ExactComplex[] { ExactComplex.ZERO };
        } else {
            _c = new ExactComplex[f.length];

            for (int k = 0; k < _c.length; k++) {
                if (f[k] instanceof ExactComplex) {
                    _c[k] = (ExactComplex) f[k];
                } else if (f[k] instanceof ExactReal) {
                    _c[k] = new ExactComplex(((ExactReal) f[k]), ExactReal.ZERO);
                } else {
                    throw new IllegalArgumentException(
                        "Different fields. Argument was " + f[k]);
                }
            }
        }

        return _c;
    }
}
