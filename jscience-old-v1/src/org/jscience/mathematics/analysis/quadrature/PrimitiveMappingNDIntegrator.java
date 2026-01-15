package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.PrimitiveMappingND;


/**
 * This interface represents an integrator for vectorial functions.
 * <p/>
 * <p>The classes which are devoted to integrate vectorial functions
 * should implement this interface. The functions which can be handled
 * should implement the {@link
 * org.jscience.mathematics.analysis.PrimitiveMappingND
 * ComputableFunction} interface.</p>
 * Warning: This is integration for functions of 1 variable into many variables, of course.
 *
 * @author L. Maisonobe
 * @version $Id: PrimitiveMappingNDIntegrator.java,v 1.3 2007-10-23 18:19:25 virtualcall Exp $
 * @see org.jscience.mathematics.analysis.PrimitiveMappingND
 */
public interface PrimitiveMappingNDIntegrator {
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
    public double[] integrate(PrimitiveMappingND f, double a, double b)
        throws MappingException;
}
