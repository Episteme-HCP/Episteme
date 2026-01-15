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
public interface NumberMapping extends AbstractMapping {
    /**
     * A user-defined function. We do not state anything about the
     * domain on which this function applies.
     *
     * @see See org.jscience.mathematics.analysis.Domain.
     */
    public Number map(Number x);
}
