package org.jscience.mathematics.analysis;

/**
 * This interface defines an N dimensional map.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @see AbstractMapping
 */

//you should implement map(double[]) and provide converters for the other methods
//this class is a wrapping class to provide support for mappings with minimal object initialization
public interface PrimitiveMappingND extends AbstractMapping {
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(int[] x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(long[] x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(float[] x);

    /**
     * A user-defined function. We do not state anything about the
     * domain on which this function applies.
     *
     * @see See org.jscience.mathematics.analysis.Domain.
     */
    public double[] map(double[] x);

    /**
     * The dimension of variable parameter. Should be a strictly
     * positive integer.
     *
     * @return DOCUMENT ME!
     */
    public int numInputDimensions();

    /**
     * The dimension of the result values. Should be inferior or equal
     * to numInputDimensions(). Should be a strictly positive integer.
     *
     * @return DOCUMENT ME!
     */
    public int numOutputDimensions();
}
