package org.episteme.mathematics.analysis.quadrature;

import org.episteme.mathematics.analysis.ExhaustedSampleException;
import org.episteme.mathematics.analysis.MappingException;
import org.episteme.mathematics.analysis.SampledMappingIterator;

/**
 * This class implements an enhanced Simpson-like integrator.
 * <p/>
 * <p>A traditional Simpson integrator is based on a quadratic
 * approximation of the function on three equally spaced points. This
 * integrator does the same thing but can handle non-equally spaced
 * points. If it is used on a regular sample, it behaves exactly as a
 * traditional Simpson integrator.</p>
 *
 * @author L. Maisonobe
 * @version $Id: EnhancedSimpsonIntegrator.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 */

public class EnhancedSimpsonIntegrator implements SampledMappingIntegrator {

    public double integrate(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        EnhancedSimpsonIntegratorSampler sampler
                = new EnhancedSimpsonIntegratorSampler(iter);
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
