package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.io.Serializable;


// TODO as it extends DoubleSquareMatrix -- maybe able to simplify some of the operations.
/**
 * The ComplexSymmetricMatrix class provides an object for encapsulating
 * square matrices containing doubles.
 *
 * @author Mark Hale
 * @version 2.3
 */
public class ComplexSymmetricMatrix extends ComplexSquareMatrix
    implements Cloneable, Serializable {
/**
     * Constructs an empty matrix.
     *
     * @param size the number of rows/columns.
     */
    public ComplexSymmetricMatrix(final int size) {
        super(size);
    }

/**
     * Constructs a matrix by wrapping an array.
     *
     * @param array an assigned value.
     */
    public ComplexSymmetricMatrix(final Complex[][] array) {
        super(array);
    }

/**
     * Constructs a matrix from an array of vectors (columns).
     *
     * @param array an assigned value.
     */
    public ComplexSymmetricMatrix(final ComplexVector[] array) {
        super(array);
    }

/**
     * Copy constructor.
     *
     * @param mat an assigned value.
     */
    public ComplexSymmetricMatrix(final ComplexSymmetricMatrix mat) {
        super(mat);
    }

    /**
     * Also sets the symmetric element.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param x DOCUMENT ME!
     */
    public void setElement(final int i, final int j, final Complex x) {
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
        return new ComplexSymmetricMatrix(this);
    }
}
