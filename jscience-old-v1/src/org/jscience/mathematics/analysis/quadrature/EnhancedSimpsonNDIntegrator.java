package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.ExhaustedSampleException;
import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.SampledMappingIterator;

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
 * @version $Id: EnhancedSimpsonNDIntegrator.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 */

public class EnhancedSimpsonNDIntegrator
        implements SampledMappingNDIntegrator {
    public double[] integrate(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        EnhancedSimpsonIntegratorSampler sampler =
                new EnhancedSimpsonIntegratorSampler(iter);
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
