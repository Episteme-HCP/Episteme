package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public class ComplexPolynomialRing implements Ring {
    /** DOCUMENT ME! */
    private static final ComplexPolynomial ZERO = new ComplexPolynomial(new Complex[] {
                Complex.ZERO
            });

    /** DOCUMENT ME! */
    private static final ComplexPolynomial ONE = new ComplexPolynomial(new Complex[] {
                Complex.ONE
            });

    /** DOCUMENT ME! */
    private static ComplexPolynomialRing _instance;

/**
     * Creates a new instance of ComplexPolynomialRing
     */
    protected ComplexPolynomialRing() {
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ComplexPolynomialRing getInstance() {
        if (_instance == null) {
            synchronized (ComplexPolynomialRing.class) {
                if (_instance == null) {
                    _instance = new ComplexPolynomialRing();
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
    protected static Complex[] toComplex(Field.Member[] f) {
        Complex[] _c = null;

        if (f == null) {
            _c = new Complex[] { Complex.ZERO };
        }

        if (f.length == 0) {
            _c = new Complex[] { Complex.ZERO };
        } else {
            _c = new Complex[f.length];

            for (int k = 0; k < _c.length; k++) {
                if (f[k] instanceof Complex) {
                    _c[k] = (Complex) f[k];
                } else if (f[k] instanceof Double) {
                    _c[k] = new Complex(((Double) f[k]).value(), 0.);
                } else {
                    throw new IllegalArgumentException(
                        "Different fields. Argument was " + f[k]);
                }
            }
        }

        return _c;
    }
}
