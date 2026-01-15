package org.jscience.mathematics.algebraic;

import org.jscience.util.IllegalDimensionException;


/**
 * This class defines an interface for Matrix whose all elements but the
 * ones on the diagonal are zero.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AbstractDiagonalMatrix extends AbstractTridiagonalMatrix
    implements DiagonalMatrix {
/**
     * Creates a new AbstractDiagonalMatrix object.
     *
     * @param rows DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractDiagonalMatrix(int rows) {
        super(rows, 0, 0);
    }
}
