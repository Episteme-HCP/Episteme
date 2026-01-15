package org.jscience.mathematics.algebraic;

/**
 * This class defines an interface for matrices whose components are all
 * zeros except on the diagonal and/or the superdiagonal and/or the
 * subdiagonal.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AbstractTridiagonalMatrix extends AbstractBandedMatrix
    implements TridiagonalMatrix {
/**
     * Creates a new AbstractTridiagonalMatrix object.
     *
     * @param rows DOCUMENT ME!
     */
    public AbstractTridiagonalMatrix(int rows, int k1, int k2) {
        super(rows, k1, k2);

        if ((k1 == 0) || (k1 == 1) || (k2 == 0) || (k2 == 1)) {
        } else {
            throw new IllegalArgumentException(
                "k1 and k2 must be equal to 0 or 1.");
        }
    }
}
