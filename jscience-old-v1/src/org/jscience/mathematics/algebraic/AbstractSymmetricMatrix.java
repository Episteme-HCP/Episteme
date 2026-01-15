package org.jscience.mathematics.algebraic;

import org.jscience.util.IllegalDimensionException;


/**
 * This class defines an interface for Matrices whose dimensions are equal,
 * thus leading to a square matrix.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AbstractSymmetricMatrix extends AbstractSquareMatrix
    implements SymmetricMatrix {
    //get sure i = j or j = i;
/**
     * Constructs a matrix.
     *
     * @param rows DOCUMENT ME!
     * @param cols DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractSymmetricMatrix(int rows, int cols) {
        super(rows, cols);

        if (rows != cols) {
            throw new IllegalDimensionException(
                "Matrix dimensions must be equal.");
        }
    }

/**
     * Constructs a matrix.
     *
     * @param rows DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractSymmetricMatrix(int rows) {
        super(rows, rows);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSymmetric() {
        boolean result;
        int i;
        int j;

        result = true;
        i = 0;

        while ((i < numRows()) && result) {
            j = i;

            while ((j < numRows()) && result) {
                result = getElement(i, j).equals(getElement(j, i));
            }
        }

        return result;
    }
}
