package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;


/**
 * A Polynomial as a <code>Ring.Member</code> over a <code>Field</code>
 *
 * @author b.dietrich
 */
public interface Polynomial extends Ring.Member {
    /**
     * Get the coefficient of degree k, i.e. <I>a_k</I> if <I>P(x)</I>
     * := sum_{k=0}^n <I>a_k x^k</I>
     *
     * @param k degree
     *
     * @return coefficient as described above
     */
    public Field.Member getCoefficient(int k);

    /**
     * Get the coefficients as an array
     *
     * @return the coefficients as an array
     */
    public Field.Member[] getCoefficients();

    /**
     * The degree understood as the highest degree
     *
     * @return the degree
     */
    public int degree();

    /**
     * Return a new Polynomial with coefficients divided by <I>a</I>
     *
     * @param a divisor
     *
     * @return new Polynomial with coefficients /= <I>a</I>
     */
    public Polynomial scalarDivide(Field.Member a);

    /**
     * Return a new Polynomial with coefficients multiplied by <I>a</I>
     *
     * @param a factor
     *
     * @return new Polynomial with coefficients = <I>a</I>
     */
    public Polynomial scalarMultiply(Field.Member a);
}
