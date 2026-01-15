package org.jscience.mathematics.analysis;

/**
 * This interface defines a map or function. It is used to pass user-defined
 * functions to some of the other maths classes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @see org.jscience.mathematics.analysis.NumericalMathUtils
 * @see AbstractMapping
 */

//you should implement map(double) and provide converters for the other methods
//OR you implement a mapping for, say, int, and throw errors for the others methods meaning that this is a map only defined on ints.
//this class is a wrapping class to provide support for mappings with minimal object initialization
public interface PrimitiveMapping extends AbstractMapping {
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(int x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(long x);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(float x);

    /**
     * A user-defined function. We do not state anything about the
     * domain on which this function applies.
     *
     * @see See org.jscience.mathematics.analysis.IntervalList.
     */
    public double map(double x);
}
