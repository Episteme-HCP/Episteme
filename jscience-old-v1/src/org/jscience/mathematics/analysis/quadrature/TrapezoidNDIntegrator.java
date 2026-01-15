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
 * @version $Id: TrapezoidNDIntegrator.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 */

public class TrapezoidNDIntegrator
        implements SampledMappingNDIntegrator {
    public double[] integrate(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        TrapezoidIntegratorSampler sampler =
                new TrapezoidIntegratorSampler(iter);
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
