package org.jscience.mathematics.algebraic;

/**
 * This class defines an interface for matrices defined such as if all matrix elements vanish outside a diagonally bordered  "band" of some range and size:
 * ai,j=0 if j<i-k1 or j>i+k2
 * for some
 * k1, k2 > 0.
 * The quantities k1,k2 are called the left and right halfbandwidth respectively. The bandwidth of the matrix is k1 + k2 + 1.
 * A band matrix with k1 = k2 = 0 is a diagonal matrix; a band matrix with k1 = k2 = 1 is a tridiagonal matrix. If one puts k1 = 0, k2 = n-1, one obtains the definition of a lower triangular matrix, for k1 = n-1, k2 = 0 an upper triangular matrix.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface BandedMatrix extends SquareMatrix {
    //left halfbandwidth
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK1();

    //right halfbandwidth
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK2();
}
