package org.jscience.mathematics.algebraic;

import org.jscience.util.IllegalDimensionException;


/**
 * The Matrix superclass provides an abstract encapsulation for traditional 2D
 * matrices. Concrete implementations of this class should implement
 * additional interfaces. See subclasses.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */

//see http://skal.planet-d.net/demo/matrixfaq.htm
public interface Matrix extends Hypermatrix {
    // TODO: This interface should be made generic: Matrix<T extends Ring.Member> or similar.
    /**
     * Returns the number of rows.
     *
     * @return DOCUMENT ME!
     */
    public int numRows();

    /**
     * Returns the number of columns.
     *
     * @return DOCUMENT ME!
     */
    public int numColumns();

    /**
     * Returns the element at position i,j.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Number getElement(int i, int j) throws IllegalDimensionException;

    /**
     * Converts a matrix to an array.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number[][] toArray(Matrix v);

    /**
     * Returns the ith row.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getRow(int i);

    /**
     * Returns the ith column.
     *
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getColumn(int j);

    /**
     * Returns the transpose of this matrix.
     *
     * @return DOCUMENT ME!
     */
    public Matrix transpose();
}
