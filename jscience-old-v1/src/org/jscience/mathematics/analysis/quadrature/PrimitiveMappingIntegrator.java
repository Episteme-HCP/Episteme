package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.PrimitiveMapping;


/**
 * This interface represents an integrator for scalar functions.
 * <p/>
 * <p>The classes which are devoted to integrate scalar functions
 * should implement this interface. The functions which can be handled
 * should implement the {@link
 * org.jscience.mathematics.analysis.functions.scalar.ComputableFunction
 * ComputableFunction} interface.</p>
 *
 * @author L. Maisonobe
 * @version $Id: PrimitiveMappingIntegrator.java,v 1.3 2007-10-23 18:19:25 virtualcall Exp $
 * @see org.jscience.mathematics.analysis.PrimitiveMapping
 */
public interface PrimitiveMappingIntegrator {
    /**
     * Integrate a function over a defined range.
     *
     * @param f function to integrate
     * @param a first bound of the range (can be lesser or greater than b)
     * @param b second bound of the range (can be lesser or greater than a)
     *
     * @return value of the integral over the range
     *
     * @throws MappingException if the underlying function throws one
     */
    public double integrate(PrimitiveMapping f, double a, double b)
        throws MappingException;
}
