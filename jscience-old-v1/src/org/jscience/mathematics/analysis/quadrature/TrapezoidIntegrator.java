package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.ExhaustedSampleException;
import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.SampledMappingIterator;

/**
 * This class implements a trapezoid integrator.
 * <p/>
 * <p>A trapezoid integrator is a very simple one that assumes the
 * function is linear over the integration step.</p>
 *
 * @author L. Maisonobe
 * @version $Id: TrapezoidIntegrator.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 */

public class TrapezoidIntegrator
        implements SampledMappingIntegrator {
    public double integrate(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        TrapezoidIntegratorSampler sampler = new TrapezoidIntegratorSampler(iter);
        double sum = 0.0;

        try {
            while (true) {
                sum = sampler.next().getY()[0].doubleValue();
            }
        } catch (ExhaustedSampleException e) {
        }

        return sum;

    }

}
