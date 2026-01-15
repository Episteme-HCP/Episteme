package org.jscience.util.mapper;

/**
 * Wrapper class around an array in order to have it implement the {@link
 * ArraySliceMappable} interface.
 *
 * @author L. Maisonobe
 * @version $Id: MappableArray.java,v 1.3 2007-10-23 18:24:37 virtualcall Exp $
 */
public class MappableArray implements ArraySliceMappable {
    /** Internal array holding all data. */
    double[] internalArray;

/**
     * Simple constructor. Build a mappable array from its dimension
     *
     * @param dimension dimension of the array
     */
    public MappableArray(int dimension) {
        internalArray = new double[dimension];

        for (int i = 0; i < dimension; ++i) {
            internalArray[i] = 0;
        }
    }

/**
     * Simple constructor. Build a mappable array from an existing array
     *
     * @param array        array to use
     * @param doReallocate true if a new array should be allocated and
     *                     initialized using the other argument, false if the instance
     *                     should reference the existing array throughout its lifetime
     */
    public MappableArray(double[] array, boolean doReallocate) {
        if (doReallocate) {
            internalArray = new double[array.length];
            System.arraycopy(array, 0, internalArray, 0, array.length);
        } else {
            internalArray = array;
        }
    }

    /**
     * Get the array stored in the instance.
     *
     * @return array stored in the instance
     */
    public double[] getArray() {
        return internalArray;
    }

    /**
     * Get the dimension of the internal array.
     *
     * @return dimension of the array
     */
    public int getStateDimension() {
        return internalArray.length;
    }

    /**
     * Reinitialize internal state from the specified array slice data.
     *
     * @param start start index in the array
     * @param array array holding the data to extract
     */
    public void mapStateFromArray(int start, double[] array) {
        System.arraycopy(array, start, internalArray, 0, internalArray.length);
    }

    /**
     * Store internal state data into the specified array slice.
     *
     * @param start start index in the array
     * @param array array where data should be stored
     */
    public void mapStateToArray(int start, double[] array) {
        System.arraycopy(internalArray, 0, array, start, internalArray.length);
    }
}
