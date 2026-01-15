package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.*;

/**
 * This class implements an enhanced Simpson integrator as a sample.
 * <p/>
 * <p>A traditional Simpson integrator is based on a quadratic
 * approximation of the function on three equally spaced points. This
 * integrator does the same thing but can handle non-equally spaced
 * points. If it is used on a regular sample, it behaves exactly as a
 * traditional Simpson integrator.</p>
 *
 * @author L. Maisonobe
 * @version $Id: EnhancedSimpsonIntegratorSampler.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 * @see EnhancedSimpsonIntegrator
 */

public class EnhancedSimpsonIntegratorSampler implements SampledMappingIterator {

    /**
     * Underlying sampled function iterator.
     */
    private SampledMappingIterator iter;

    /**
     * Next point.
     */
    private ValuedPair next;

    /**
     * Current running sum.
     */
    private double sum;

    /**
     * Constructor.
     * Build an integrator from an underlying sample iterator.
     *
     * @param iter iterator over the base function
     */
    public EnhancedSimpsonIntegratorSampler(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        this.iter = iter;

        // get the first point
        next = iter.next();

        // initialize the sum
        sum = 0.0;

    }

    public SampledMapping getSampledMapping() {
        return iter.getSampledMapping();
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public ValuedPair next() throws ExhaustedSampleException, MappingException {
        // performs one step of an enhanced Simpson scheme
        ValuedPair previous = next;
        ValuedPair current = iter.next();

        try {
            next = iter.next();

            double h1 = current.getX()[0].doubleValue() - previous.getX()[0].doubleValue();
            double h2 = next.getX()[0].doubleValue() - current.getX()[0].doubleValue();
            double cP = (h1 + h2) * (2 * h1 - h2) / (6 * h1);
            double cC = (h1 + h2) * (h1 + h2) * (h1 + h2) / (6 * h1 * h2);
            double cN = (h1 + h2) * (2 * h2 - h1) / (6 * h2);

            sum += cP * previous.getY()[0].doubleValue() + cC * current.getY()[0].doubleValue() + cN * next.getY()[0].doubleValue();

        } catch (ExhaustedSampleException e) {
            // we have an incomplete step at the end of the sample
            // we use a trapezoid scheme for this last step
            sum += 0.5 * (current.getX()[0].doubleValue() - previous.getX()[0].doubleValue()) * (previous.getY()[0].doubleValue() + current.getY()[0].doubleValue());
            return new ValuedPair(current.getX()[0].doubleValue(), sum);
        }

        return new ValuedPair(next.getX()[0].doubleValue(), sum);

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
