package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.ExhaustedSampleException;
import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.SampledMappingIterator;

/**
 * This class implements a Riemann integrator.
 * <p/>
 * <p>A Riemann integrator is a very simple one that assumes the
 * function is constant over the integration step. Since it is very
 * simple, this algorithm needs very small steps to achieve high
 * accuracy, and small steps lead to numerical errors and
 * instabilities.</p>
 * <p/>
 * <p>This algorithm is almost never used and has been included in
 * this package only as a simple template for more useful
 * integrators.</p>
 *
 * @author L. Maisonobe
 * @version $Id: RiemannIntegrator.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 * @see TrapezoidIntegrator
 */

public class RiemannIntegrator
        implements SampledMappingIntegrator {
    public double integrate(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        RiemannIntegratorSampler sampler = new RiemannIntegratorSampler(iter);
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
