package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.ExactComplex;
import org.jscience.mathematics.algebraic.numbers.ExactReal;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public final class PolynomialMathUtils extends Object {
    /**
     * Creates a new PolynomialMathUtils object.
     */
    private PolynomialMathUtils() {
    }

    /**
     * Returns the companion matrix of a given polynomial.
     * The eigenvalues of the companion matrix are the roots of the polynomial.
     *
     * @param p the polynomial
     * @return the companion matrix
     * @planetmath CompanionMatrix
     */
    public static AbstractDoubleSquareMatrix toCompanionMatrix(DoublePolynomial p) {
        DoublePolynomial np = normalize(p);

        int n = np.degree();
        if (n < 1) {
            throw new IllegalArgumentException("Cannot get a companion matrix for a constant factor");
        }

        AbstractDoubleSquareMatrix dsm = new DoubleSquareMatrix(n);

        for (int k = 0; k < (n - 1); k++) {
            // fill subdiagonal
            dsm.setElement(k + 1, k, 1.0);
            // fill lastCol
            dsm.setElement(k, n - 1, np.getCoefficientAsDouble(k));
        }
        // fill lastRow/lastCol
        dsm.setElement(n - 1, n - 1, np.getCoefficientAsDouble(n - 1));

        return dsm;
    }

    /**
     * Returns the companion matrix of a given polynomial.
     * The eigenvalues of the companion matrix are the roots of the polynomial.
     *
     * @param p the polynomial
     * @return the companion matrix
     * @planetmath CompanionMatrix
     */
    public static RingMatrix toCompanionMatrix(ExactRealPolynomial p) {
        ExactRealPolynomial np = normalize(p);

        int n = np.degree();
        if (n < 1) {
            throw new IllegalArgumentException("Cannot get a companion matrix for a constant factor");
        }

        RingMatrix dsm = new RingMatrix(n, n);

        for (int k = 0; k < (n - 1); k++) {
            // fill subdiagonal
            dsm.setElement(k + 1, k, ExactReal.ONE);
            // fill lastCol
            dsm.setElement(k, n - 1, np.getCoefficientAsExactReal(k));
        }
        // fill lastRow/lastCol
        dsm.setElement(n - 1, n - 1, np.getCoefficientAsExactReal(n - 1));

        return dsm;
    }

    public static AbstractComplexSquareMatrix toCompanionMatrix(ComplexPolynomial p) {
        ComplexPolynomial np = normalize(p);

        int n = np.degree();
        if (n < 1) {
            throw new IllegalArgumentException("Cannot get a companion matrix for a constant factor");
        }

        AbstractComplexSquareMatrix csm = new ComplexSquareMatrix(n);

        for (int k = 0; k < (n - 1); k++) {
            // fill subdiagonal
            csm.setElement(k + 1, k, 1.0, 0.0);
            // fill lastCol
            csm.setElement(k, n - 1, np.getCoefficientAsComplex(k));
        }
        // fill lastRow/lastCol
        csm.setElement(n - 1, n - 1, np.getCoefficientAsComplex(n - 1));

        return csm;
    }

    public static RingMatrix toCompanionMatrix(ExactComplexPolynomial p) {
        ExactComplexPolynomial np = normalize(p);

        int n = np.degree();
        if (n < 1) {
            throw new IllegalArgumentException("Cannot get a companion matrix for a constant factor");
        }

        RingMatrix csm = new RingMatrix(n, n);

        for (int k = 0; k < (n - 1); k++) {
            // fill subdiagonal
            csm.setElement(k + 1, k, ExactComplex.ONE);
            // fill lastCol
            csm.setElement(k, n - 1, np.getCoefficientAsExactComplex(k));
        }
        // fill lastRow/lastCol
        csm.setElement(n - 1, n - 1, np.getCoefficientAsExactComplex(n - 1));

        return csm;
    }

    /**
     * Calculates the roots of a given polynomial by solving the
     * eigenvalue problem for the companion matrix.
     * This is not yet implemented (depends on a QR- decomposition)
     * @param p the polynomial
     *
     * @return (unordered) list of roots.
     */
    //public static Complex[] findRoots( DoublePolynomial p ) {
    //AbstractDoubleSquareMatrix matrix = toCompanionMatrix( p );

    // return solveEigenvalueByQR(c);
    //throw new java.lang.UnsupportedOperationException("Not yet implemented.");
    //}

    /**
     * Get the maximum degree of two polynomials
     *
     * @param p1
     * @param p2
     */
    public static int maxDegree(Polynomial p1, Polynomial p2) {
        return Math.max(p1.degree(), p2.degree());
    }

    /**
     * Get the minimal degree of two polynomials
     *
     * @param p1
     * @param p2
     */
    public static int minDegree(Polynomial p1, Polynomial p2) {
        return Math.min(p1.degree(), p2.degree());
    }

    /**
     * Evaluates a polynomial by Horner's scheme.
     *
     * @param p
     * @param t
     */
    public static double evalPolynomial(DoublePolynomial p, double t) {
        final int n = p.degree();
        double r = p.getCoefficientAsDouble(n);
        for (int i = n - 1; i >= 0; i--) {
            r = p.getCoefficientAsDouble(i) + (r * t);
        }

        return r;
    }

    /**
     * Evaluates a polynomial by Horner's scheme.
     *
     * @param p
     * @param t
     */
    public static ExactReal evalPolynomial(ExactRealPolynomial p, ExactReal t) {
        final int n = p.degree();
        ExactReal r = p.getCoefficientAsExactReal(n);
        for (int i = n - 1; i >= 0; i--) {
            r = p.getCoefficientAsExactReal(i).add(r.multiply(t));
        }

        return r;
    }

    /**
     * Evaluates a polynomial by Horner's scheme.
     *
     * @param p
     * @param t
     */
    public static Complex evalPolynomial(ComplexPolynomial p, Complex t) {
        final int n = p.degree();
        Complex r = p.getCoefficientAsComplex(n);
        for (int i = n - 1; i >= 0; i--) {
            r = p.getCoefficientAsComplex(i).add(r.multiply(t));
        }

        return r;
    }

    /**
     * Evaluates a polynomial by Horner's scheme.
     *
     * @param p
     * @param t
     */
    public static ExactComplex evalPolynomial(ExactComplexPolynomial p, ExactComplex t) {
        final int n = p.degree();
        ExactComplex r = p.getCoefficientAsExactComplex(n);
        for (int i = n - 1; i >= 0; i--) {
            r = p.getCoefficientAsExactComplex(i).add(r.multiply(t));
        }

        return r;
    }

    /**
     * Interpolates a polynomial.
     * Caveat: this method is brute-force, slow and not very stable.
     * It shouldn't be used for more than approx. 10 points.
     * Remember the strong variations of higher degree polynomials.
     *
     * @param samplingPoints an array[2][n] where array[0] denotes x-values, array[1] y-values
     */
    public static DoublePolynomial interpolateLagrange(double[][] samplingPoints) {
        DoubleLagrangeBasis r = new DoubleLagrangeBasis(samplingPoints[0]);
        return ((DoublePolynomial) r.superposition(samplingPoints[1]));
    }

    /**
     * Interpolates a polynomial.
     * Caveat: this method is brute-force, slow and not very stable.
     * It shouldn't be used for more than approx. 10 points.
     * Remember the strong variations of higher degree polynomials.
     *
     * @param samplingPoints an array[2][n] where array[0] denotes x-values, array[1] y-values
     */
    public static ComplexPolynomial interpolateLagrange(Complex[][] samplingPoints) {
        ComplexLagrangeBasis r = new ComplexLagrangeBasis(samplingPoints[0]);
        return ((ComplexPolynomial) r.superposition(samplingPoints[1]));
    }

    /**
     * Normalizes a given real polynomial, i.e. divide by the leading coefficient.
     *
     * @param p
     */
    public static DoublePolynomial normalize(DoublePolynomial p) {
        final int n = p.degree();
        final double c = p.getCoefficientAsDouble(n);
        double[] m = new double[n + 1];
        m[n] = 1.0;
        for (int i = 0; i < n; i++) {
            m[i] = p.getCoefficientAsDouble(i) / c;
        }

        return new DoublePolynomial(m);
    }

    /**
     * Normalizes a given real polynomial, i.e. divide by the leading coefficient.
     *
     * @param p
     */
    public static ExactRealPolynomial normalize(ExactRealPolynomial p) {
        final int n = p.degree();
        final ExactReal c = p.getCoefficientAsExactReal(n);
        ExactReal[] m = new ExactReal[n + 1];
        m[n] = ExactReal.ONE;
        for (int i = 0; i < n; i++) {
            m[i] = p.getCoefficientAsExactReal(i).divide(c);
        }

        return new ExactRealPolynomial(m);
    }

    /**
     * Normalizes a given complex polynomial, i.e. divide by the leading coefficient.
     *
     * @param p
     */
    public static ComplexPolynomial normalize(ComplexPolynomial p) {
        final int n = p.degree();
        final Complex c = p.getCoefficientAsComplex(n);
        Complex[] m = new Complex[n + 1];
        m[n] = Complex.ONE;
        for (int i = 0; i < n; i++) {
            m[i] = p.getCoefficientAsComplex(i).divide(c);
        }

        return new ComplexPolynomial(m);
    }

    /**
     * Normalize a given complex polynomial, i.e. divide by the leading
     * coefficient.
     *
     * @param p
     * @return DOCUMENT ME!
     */
    public static ExactComplexPolynomial normalize(ExactComplexPolynomial p) {
        int n = p.degree();
        ExactComplex c = p.getCoefficientAsExactComplex(n);
        ExactComplex[] m = new ExactComplex[n + 1];
        m[n] = ExactComplex.ONE;

        for (int k = 0; k < n; k++) {
            m[k] = p.getCoefficientAsExactComplex(k).divide(c);
        }

        return new ExactComplexPolynomial(m);
    }

    /**
     * Try to cast a Polynomial to a complex polynomial
     */
    public static ComplexPolynomial toComplex(Polynomial p) {
        if (p instanceof ComplexPolynomial) {
            return (ComplexPolynomial) p;
        } else if (p instanceof DoublePolynomial) {
            double[] d = ((DoublePolynomial) p).getCoefficientsAsDoubles();
            Complex[] c = new Complex[d.length];
            for (int k = 0; k < d.length; k++) {
                c[k] = new Complex(d[k], 0);
            }

            return new ComplexPolynomial(c);
        } else {
            throw new IllegalArgumentException("Polynomial class not recognised by this method.");
        }
    }

    /**
     * Try to cast a Polynomial to a complex polynomial
     *
     * @param p DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static ExactComplexPolynomial toExactComplexPolynomial(Polynomial p) {
        if (p instanceof ExactComplexPolynomial) {
            return (ExactComplexPolynomial) p;
        } else if (p instanceof ExactRealPolynomial) {
            ExactReal[] d = ((ExactRealPolynomial) p).getCoefficientsAsExactReals();
            ExactComplex[] c = new ExactComplex[d.length];

            for (int k = 0; k < d.length; k++) {
                c[k] = new ExactComplex(d[k], ExactReal.ZERO);
            }

            return new ExactComplexPolynomial(c);
        } else if (p instanceof ComplexPolynomial) {
            Complex[] d = ((ComplexPolynomial) p).getCoefficientsAsComplexes();
            ExactComplex[] c = new ExactComplex[d.length];

            for (int k = 0; k < d.length; k++) {
                c[k] = new ExactComplex(d[k]);
            }

            return new ExactComplexPolynomial(c);
        } else if (p instanceof DoublePolynomial) {
            double[] d = ((DoublePolynomial) p).getCoefficientsAsDoubles();
            ExactComplex[] c = new ExactComplex[d.length];

            for (int k = 0; k < d.length; k++) {
                c[k] = new ExactComplex(d[k], 0.);
            }

            return new ExactComplexPolynomial(c);
        } else {
            throw new IllegalArgumentException(
                    "Polynomial class not recognised by this method.");
        }
    }

}
