package org.jscience.mathematics.algebraic;

/**
 * This class defines an interface for matrices whose components are all
 * zeros in the upper right or down left triangle.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AbstractTriangularMatrix extends AbstractBandedMatrix
    implements TriangularMatrix {
/**
     * Creates a new AbstractTriangularMatrix object.
     *
     * @param rows DOCUMENT ME!
     */
    public AbstractTriangularMatrix(int rows, int k1, int k2) {
        super(rows, k1, k2);

        if (((k1 == 0) && (k2 == (rows - 1))) ||
                ((k1 == 0) && (k2 == (rows - 1)))) {
        } else {
            throw new IllegalArgumentException(
                "One of k1 and k2 must be equal to zero and the other to rows - 1.");
        }
    }
}
