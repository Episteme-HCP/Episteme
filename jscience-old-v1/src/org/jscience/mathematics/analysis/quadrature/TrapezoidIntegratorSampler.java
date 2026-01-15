package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.*;

/**
 * This class implements a trapezoid integrator as a sample.
 * <p/>
 * <p>A trapezoid integrator is a very simple one that assumes the
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
 * @version $Id: TrapezoidIntegratorSampler.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 * @see TrapezoidIntegrator
 */

public class TrapezoidIntegratorSampler implements SampledMappingIterator {

    /**
     * Underlying sample iterator.
     */
    private SampledMappingIterator iter;

    /**
     * Current point.
     */
    private ValuedPair current;

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
    public TrapezoidIntegratorSampler(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        this.iter = iter;

        // get the first point
        current = iter.next();

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
        // performs one step of a trapezoid scheme
        ValuedPair previous = current;
        current = iter.next();
        sum += 0.5
                * (current.getX()[0].doubleValue() - previous.getX()[0].doubleValue())
                * (previous.getY()[0].doubleValue() + current.getY()[0].doubleValue());

        return new ValuedPair(current.getX()[0].doubleValue(), sum);

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
