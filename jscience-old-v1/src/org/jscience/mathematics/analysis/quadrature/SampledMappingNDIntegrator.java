package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.ExhaustedSampleException;
import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.SampledMappingIterator;


/**
 * This interface represents an integrator for vectorial samples.
 * <p/>
 * <p>The classes which are devoted to integrate vectorial samples
 * should implement this interface.</p>
 *
 * @author L. Maisonobe
 * @version $Id: SampledMappingNDIntegrator.java,v 1.3 2007-10-23 18:19:26 virtualcall Exp $
 * @see org.jscience.mathematics.analysis.SampledMappingIterator
 * @see PrimitiveMappingNDIntegrator
 */
public interface SampledMappingNDIntegrator {
    /**
     * Integrate a sample over its overall range
     *
     * @param iter iterator over the sample to integrate
     *
     * @return value of the integral over the sample range
     *
     * @throws ExhaustedSampleException if the sample does not have enough
     *         points for the integration scheme
     * @throws MappingException if the underlying sampled function throws one
     */
    public double[] integrate(SampledMappingIterator iter)
        throws ExhaustedSampleException, MappingException;
}
