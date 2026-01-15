package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;

/**
 * The Lagrange Basis for real polynomials. For a given set of sampling points {x_1, ..., x_n},
 * the corresponding lagrange polynomials are L_k = \kronecker_kj   \forall j=1..n.
 * The explicit form is
 * <p/>
 * L_k=  \prod_{j=0, j\neq k}^n \frac{t-t_j}{t_k-t_j}
 *
 * @author b.dietrich
 */
public class DoubleLagrangeBasis implements PolynomialBasis {
    protected double[] _samplingsX;
    protected int _dim;
    private DoublePolynomial[] _basis;
    private Field.Member[] _samplings;

    /**
     * Creates a new instance of LagrangeBasis for given sampling points
     */
    public DoubleLagrangeBasis(Field.Member[] samplings) {
        if (samplings == null) {
            throw new NullPointerException();
        }

        _dim = samplings.length;
        _samplings = samplings;
        _samplingsX = DoublePolynomialRing.toDouble(_samplings);
        buildBasis();
    }

    /**
     * Creates a new DoubleLagrangeBasis object.
     *
     * @param samplings
     */
    public DoubleLagrangeBasis(double[] samplings) {
        if (samplings == null) {
            throw new NullPointerException();
        }

        _dim = samplings.length;
        _samplingsX = samplings;
        buildBasis();
    }

    protected DoubleLagrangeBasis() {
    }

    /**
     * The basis vector as described above
     *
     * @param k
     */
    public Polynomial getBasisVector(int k) {
        return _basis[k];
    }

    /**
     * The dimension ( # of sampling points)
     *
     * @return the dimension
     */
    public int dimension() {
        return _dim;
    }

    /**
     * The sampling points used in constructor
     *
     * @return the sampling points
     */
    public Field.Member[] getSamplingPoints() {
        if (_samplings == null) {
            _samplings = DoublePolynomialRing.toDouble(_samplingsX);
        }

        return _samplings;
    }

    /**
     * Make a superposition of basis- vectors for a given set of coefficients.
     * Due to the properties of a lagrange base, the result is the interpolating
     * polynomial with values coeff[k] at sampling point k
     *
     * @param coeff in this case the values of the interpolation problem
     * @return the interpolating polynomial
     */
    public Polynomial superposition(Field.Member[] coeff) {
        if (coeff == null) {
            throw new NullPointerException();
        }

        if (coeff.length != _dim) {
            throw new IllegalArgumentException("Dimensions do not match");
        }

        double[] d = DoublePolynomialRing.toDouble(coeff);

        return superposition(d);
    }

    /**
     * Same as above, but type-safe
     */
    public DoublePolynomial superposition(double[] c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c.length != _dim) {
            throw new IllegalArgumentException("Dimension of basis is " + _dim +
                    ". Got " + c.length + " coefficients");
        }

        DoublePolynomial rp = (DoublePolynomial) DoublePolynomialRing.getInstance()
                .zero();

        for (int k = 0; k < _dim; k++) {
            DoublePolynomial b = (DoublePolynomial) getBasisVector(k);
            DoublePolynomial ba = b.scalarMultiply(c[k]);
            rp = (DoublePolynomial) rp.add(ba);
        }

        return rp;
    }

    protected void buildBasis() {
        _basis = new DoublePolynomial[_dim];

        for (int k = 0; k < _dim; k++) {
            _basis[k] = (DoublePolynomial) DoublePolynomialRing.getInstance().one();

            double fac = 1.;

            for (int j = 0; j < _dim; j++) {
                if (j == k) {
                    continue;
                } else {
                    DoublePolynomial n = new DoublePolynomial(new double[]{
                            -_samplingsX[j], 1.
                    });
                    _basis[k] = (DoublePolynomial) _basis[k].multiply(n);
                    fac *= (_samplingsX[k] - _samplingsX[j]);
                }
            }

            _basis[k] = _basis[k].scalarDivide(fac);
        }
    }
}
