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
 * @version $Id: RiemannNDIntegrator.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 * @see TrapezoidIntegrator
 */

public class RiemannNDIntegrator
        implements SampledMappingNDIntegrator {

    public double[] integrate(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        RiemannIntegratorSampler sampler = new RiemannIntegratorSampler(iter);
        double[] sum = null;

        try {
            while (true) {
                sum = getNumbersAsDouble(sampler.next().getY());
            }
        } catch (ExhaustedSampleException e) {
        }

        return sum;

    }

    private double[] getNumbersAsDouble(Number[] numbers) {
        double[] result;
        result = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            result[i] = numbers[i].doubleValue();
        }
        return result;
    }

}
