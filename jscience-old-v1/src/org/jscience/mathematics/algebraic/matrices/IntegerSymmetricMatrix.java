package org.jscience.mathematics.algebraic.matrices;

import java.io.Serializable;


// TODO as it extends IntegerSquareMatrix -- maybe able to simplify some of the operations.
/**
 * The IntegerSymmetricMatrix class provides an object for encapsulating
 * square matrices containing doubles.
 *
 * @author Mark Hale
 * @version 2.3
 */
public class IntegerSymmetricMatrix extends IntegerSquareMatrix
    implements Cloneable, Serializable {
/**
     * Constructs an empty matrix.
     *
     * @param size the number of rows/columns.
     */
    public IntegerSymmetricMatrix(final int size) {
        super(size);
    }

/**
     * Constructs a matrix by wrapping an array.
     *
     * @param array an assigned value.
     */
    public IntegerSymmetricMatrix(final int[][] array) {
        super(array);
    }

/**
     * Constructs a matrix from an array of vectors (columns).
     *
     * @param array an assigned value.
     */
    public IntegerSymmetricMatrix(final IntegerVector[] array) {
        super(array);
    }

/**
     * Copy constructor.
     *
     * @param mat an assigned value.
     */
    public IntegerSymmetricMatrix(final IntegerSymmetricMatrix mat) {
        super(mat);
    }

    /**
     * Also sets the symmetric element.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param x DOCUMENT ME!
     */
    public void setElement(final int i, final int j, final int x) {
        super.setElement(i, j, x);

        if (i != j) {
            super.setElement(j, i, x);
        }
    }

    //============
    // OPERATIONS
    //============
    // ADDITION
    // SUBTRACTION
    // INVERSE
    // LU DECOMPOSITION
    // CHOLESKY DECOMPOSITION
    // QR DECOMPOSITION
    // SINGULAR VALUE DECOMPOSITION
    // POLAR DECOMPOSITION
    // MAP ELEMENTS
    /**
     * Clone matrix into a new matrix.
     *
     * @return the cloned matrix.
     */
    public Object clone() {
        return new IntegerSymmetricMatrix(this);
    }
}
