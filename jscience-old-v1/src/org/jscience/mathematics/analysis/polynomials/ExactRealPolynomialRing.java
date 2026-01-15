package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactReal;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public class ExactRealPolynomialRing implements Ring {
    /** DOCUMENT ME! */
    public static final ExactRealPolynomial ZERO = new ExactRealPolynomial(new ExactReal[] {
                ExactReal.ZERO
            });

    /** DOCUMENT ME! */
    public static final ExactRealPolynomial ONE = new ExactRealPolynomial(new ExactReal[] {
                ExactReal.ONE
            });

    /** DOCUMENT ME! */
    private static ExactRealPolynomialRing _instance;

/**
     * Creates a new instance of PolynomialRing
     */
    protected ExactRealPolynomialRing() {
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ExactRealPolynomialRing getInstance() {
        if (_instance == null) {
            synchronized (ExactRealPolynomialRing.class) {
                if (_instance == null) {
                    _instance = new ExactRealPolynomialRing();
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
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        if ((a instanceof ExactRealPolynomial) &&
                (b instanceof ExactRealPolynomial)) {
            ExactRealPolynomial p1 = (ExactRealPolynomial) a;
            ExactRealPolynomial p2 = (ExactRealPolynomial) b;

            return p1.add(p2).equals(ZERO);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns true if the member is the unit element.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        if (r instanceof ExactRealPolynomial) {
            return ((ExactRealPolynomial) r).isOne();
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns true if the member is the identity element of this
     * group.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        if (g instanceof ExactRealPolynomial) {
            return ((ExactRealPolynomial) g).isZero();
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
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
     * internal method for safe typecast
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected static ExactReal[] toExactReal(Field.Member[] f) {
        if (f == null) {
            return null;
        }

        int dim = f.length;

        ExactReal[] d = new ExactReal[dim];

        for (int k = 0; k < dim; k++) {
            if (f[k] instanceof ExactReal) {
                d[k] = (ExactReal) f[k];
            } else {
                throw new IllegalArgumentException("Expected ExactReal. Got (" +
                    k + ") " + f[k]);
            }
        }

        return d;
    }

    /**
     * internal method for safe typecast
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static ExactReal[] toExactReal(ExactReal[] d) {
        if (d == null) {
            return null;
        }

        int dim = d.length;
        ExactReal[] s = new ExactReal[dim];

        for (int k = 0; k < dim; k++) {
            s[k] = new ExactReal(d[k]);
        }

        return s;
    }
}
