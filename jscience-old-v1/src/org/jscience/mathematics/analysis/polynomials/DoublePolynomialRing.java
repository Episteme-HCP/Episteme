package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Double;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public class DoublePolynomialRing implements Ring {
    /** DOCUMENT ME! */
    public static final DoublePolynomial ZERO = new DoublePolynomial(new double[] {
                0.0
            });

    /** DOCUMENT ME! */
    public static final DoublePolynomial ONE = new DoublePolynomial(new double[] {
                1.0
            });

    /** DOCUMENT ME! */
    private static DoublePolynomialRing _instance;

/**
     * Creates a new instance of PolynomialRing
     */
    protected DoublePolynomialRing() {
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final DoublePolynomialRing getInstance() {
        if (_instance == null) {
            synchronized (DoublePolynomialRing.class) {
                if (_instance == null) {
                    _instance = new DoublePolynomialRing();
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
        if ((a instanceof DoublePolynomial) && (b instanceof DoublePolynomial)) {
            DoublePolynomial p1 = (DoublePolynomial) a;
            DoublePolynomial p2 = (DoublePolynomial) b;

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
        if (r instanceof DoublePolynomial) {
            return ((DoublePolynomial) r).isOne();
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
        if (g instanceof DoublePolynomial) {
            return ((DoublePolynomial) g).isZero();
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
    protected static double[] toDouble(Field.Member[] f) {
        if (f == null) {
            return null;
        }

        int dim = f.length;

        double[] d = new double[dim];

        for (int k = 0; k < dim; k++) {
            if (f[k] instanceof Double) {
                d[k] = ((Double) f[k]).value();
            } else {
                throw new IllegalArgumentException("Expected Double. Got (" +
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
    protected static Double[] toDouble(double[] d) {
        if (d == null) {
            return null;
        }

        int dim = d.length;
        Double[] s = new Double[dim];

        for (int k = 0; k < dim; k++) {
            s[k] = new Double(d[k]);
        }

        return s;
    }
}
