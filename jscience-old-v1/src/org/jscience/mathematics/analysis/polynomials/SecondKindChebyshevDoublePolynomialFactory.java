package org.jscience.mathematics.analysis.polynomials;

/**
 * This class implements Second Kind Chebyshev polynomials.
 * <p/>
 * <p>Second Kind Chebyshev polynomials can be defined by the following recurrence
 * relations:
 * <pre>
 *  T0(X)   = 1
 *  T1(X)   = 2X
 *  2(n+1)(n+2)(2n+1)Tn+1(X) = P(2n+1,3)X Tn(X) - 2(n+1/2)(n+1/2)(2n+3)Tn-1(X)
 * </pre></p>
 * P(x,y) stands for Pochhammer series, see org.jscience.mathematics.series.PochhammerSeries.
 *
 * @author Silvere Martin-Michiellot
 */

//IMPORTANT:
//there is something wrong here as this class is a special case of Jacobi polynomial with a=1/2 and b=1/2
//however http://mathworld.wolfram.com/OrthogonalPolynomials.html states that T1(X) = 2X
//whereas by applying the Jacobi formula one founds T1(X) = 3X/2

//we could also do the same for many other OrthogonalPolynomial subclasses that are special cases of JacobiPolynomials
//although performance might be a key issue here
public final class SecondKindChebyshevDoublePolynomialFactory extends JacobiDoublePolynomialFactory {

    public SecondKindChebyshevDoublePolynomialFactory() {
        super(1 / 2, 1 / 2);
    }

}
