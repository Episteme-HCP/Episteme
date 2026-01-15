package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public class DoubleMonomialBasis implements PolynomialBasis {
    /** DOCUMENT ME! */
    private DoublePolynomial[] _basis;

    /** DOCUMENT ME! */
    private int _dim;

/**
     * Creates a new instance of RealMonomialBasis
     *
     * @param dim DOCUMENT ME!
     */
    public DoubleMonomialBasis(int dim) {
        _dim = dim;
        _basis = new DoublePolynomial[dim];
    }

    /**
     * DOCUMENT ME!
     *
     * @param k
     *
     * @return a basis vector
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public Polynomial getBasisVector(int k) {
        if (k >= _dim) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            if (_basis[k] == null) {
                synchronized (this) {
                    if (_basis[k] == null) {
                        double[] db = new double[_dim];
                        db[k] = 1.0;
                        _basis[k] = new DoublePolynomial(db);
                    }
                }
            }

            return _basis[k];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return the dimension of this basis
     */
    public int dimension() {
        return _dim;
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public Field.Member[] getSamplingPoints() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff
     *
     * @return DOCUMENT ME!
     */
    public Polynomial superposition(Field.Member[] coeff) {
        return superposition(DoublePolynomialRing.toDouble(coeff));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public DoublePolynomial superposition(double[] d) {
        if (d == null) {
            throw new NullPointerException();
        }

        if (d.length != _dim) {
            throw new IllegalArgumentException("Dimension of basis is " + _dim +
                ". Got " + d.length + " coefficients");
        }

        return new DoublePolynomial(d);
    }
}
