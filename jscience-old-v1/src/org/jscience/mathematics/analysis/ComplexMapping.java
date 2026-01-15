package org.jscience.mathematics.analysis;

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * This interface defines a map or function.  It is a convenience interface for
 * Complex only mappings. It is used to pass user-defined functions to some of
 * the other maths classes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @see org.jscience.mathematics.analysis.NumericalMathUtils
 * @see AbstractMapping
 */
public interface ComplexMapping extends NumberMapping {
    /**
     * A user-defined function. We do not state anything about the
     * domain on which this function applies.
     *
     * @see See org.jscience.mathematics.analysis.Domain.
     */
    public Complex map(Complex x);
}
