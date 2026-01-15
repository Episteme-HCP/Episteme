package org.jscience.mathematics.analysis;

/**
 * This interface defines an N dimensional map.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @see AbstractMapping
 */
public interface NumberMappingND extends AbstractMapping {
    /**
     * A user-defined map. We do not state anything about the domain on
     * which this function applies. x[] size should always be equal to
     * numInputDimensions().
     *
     * @see See org.jscience.mathematics.analysis.Domain.
     */
    public Number[] map(Number[] x);

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
