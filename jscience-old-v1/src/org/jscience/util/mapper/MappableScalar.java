package org.jscience.util.mapper;

/**
 * Wrapper class around a scalar in order to have it implement the {@link
 * ArraySliceMappable} interface.
 *
 * @author L. Maisonobe
 * @version $Id: MappableScalar.java,v 1.3 2007-10-23 18:24:37 virtualcall Exp $
 */
public class MappableScalar implements ArraySliceMappable {
    /** Internal scalar. */
    double value;

/**
     * Simple constructor. Build a mappable scalar
     */
    public MappableScalar() {
        value = 0;
    }

/**
     * Simple constructor. Build a mappable scalar from its initial value
     *
     * @param value initial value of the scalar
     */
    public MappableScalar(double value) {
        this.value = value;
    }

    /**
     * Get the value stored in the instance.
     *
     * @return value stored in the instance
     */
    public double getValue() {
        return value;
    }

    /**
     * Set the value stored in the instance.
     *
     * @param value value to store in the instance
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Get the dimension of the internal array.
     *
     * @return dimension of the array (always 1 for this class)
     */
    public int getStateDimension() {
        return 1;
    }

    /**
     * Reinitialize internal state from the specified array slice data.
     *
     * @param start start index in the array
     * @param array array holding the data to extract
     */
    public void mapStateFromArray(int start, double[] array) {
        value = array[start];
    }

    /**
     * Store internal state data into the specified array slice.
     *
     * @param start start index in the array
     * @param array array where data should be stored
     */
    public void mapStateToArray(int start, double[] array) {
        array[start] = value;
    }
}
