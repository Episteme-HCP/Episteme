package org.jscience.mathematics.algebraic;

import org.jscience.util.IllegalDimensionException;


/**
 * The Hypermatrix superclass provides an abstract encapsulation for extended
 * matrices. Hypermatrices are also known as MultiArrays. Please recall that
 * 10 elements in 10 dimensions is 10 power 10 total elements, therefore
 * probably more than your system is able to handle.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Hypermatrix {
    /**
     * Returns the number of dimensions.
     *
     * @return DOCUMENT ME!
     */
    public int numDimensions();

    /**
     * Returns the array of dimensions.
     *
     * @return DOCUMENT ME!
     */
    public int[] getDimensions();

    /**
     * Returns the number of elements for the given dimension.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int numElements(int i);

    /**
     * Returns the total number of elements.
     *
     * @return DOCUMENT ME!
     */
    public int numElements();

    /**
     * Returns the element at position given by the array of int.
     *
     * @param position DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Number getElement(int[] position) throws IllegalDimensionException;

    //    /**
    //     * Returns an independent copy of this hypermatrix.
    //     *
    //     * @return a copy of this hypermatrix
    //     */
    //    public Hypermatrix copy();
    /**
     * Converts this hypermatrix to an array of n dimensions
     *
     * @return DOCUMENT ME!
     */
    public Object toArray();

    /**
     * Converts a hypermatrix to an array of n dimensions
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object toArray(Hypermatrix m);

    //we could also provide a method to extract/project an hypermatrix onto one of the dimensions
    //thus resulting in an hypermatrix of n-1 dimensions
}
