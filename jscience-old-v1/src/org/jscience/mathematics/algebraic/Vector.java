package org.jscience.mathematics.algebraic;

import org.jscience.util.IllegalDimensionException;


/**
 * The Vector superclass provides an abstract encapsulation for vectors.
 * Vectors are basically a matrix with 1 column. Concrete implementations of
 * this class should implement additional interfaces. See subclasses.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Vector extends Matrix {
    /**
     * Returns the vector's dimension.
     *
     * @return DOCUMENT ME!
     */
    public int getDimension();

    /**
     * Returns the element at position i.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Number getElement(int i) throws IllegalDimensionException;

    /**
     * Converts a vector to an array.
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number[] toArray(Vector v);
}
