package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.numbers.ExactReal;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public class ExactRealMonomialBasis implements PolynomialBasis {
    /** DOCUMENT ME! */
    private ExactRealPolynomial[] _basis;

    /** DOCUMENT ME! */
    private int _dim;

/**
     * Creates a new instance of RealMonomialBasis
     *
     * @param dim DOCUMENT ME!
     */
    public ExactRealMonomialBasis(int dim) {
        _dim = dim;
        _basis = new ExactRealPolynomial[dim];
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
                        ExactReal[] db = new ExactReal[_dim];
                        db[k] = ExactReal.ONE;
                        _basis[k] = new ExactRealPolynomial(db);
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
        return superposition(ExactRealPolynomialRing.toExactReal(coeff));
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
    public ExactRealPolynomial superposition(ExactReal[] d) {
        if (d == null) {
            throw new NullPointerException();
        }

        if (d.length != _dim) {
            throw new IllegalArgumentException("Dimension of basis is " + _dim +
                ". Got " + d.length + " coefficients");
        }

        return new ExactRealPolynomial(d);
    }
}
