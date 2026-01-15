package org.jscience.mathematics.analysis.quadrature;

import org.jscience.mathematics.analysis.*;

/**
 * This class implements a Riemann integrator as a sample.
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
 * @version $Id: RiemannNDIntegratorSampler.java,v 1.2 2007-10-21 17:45:52 virtualcall Exp $
 * @see RiemannIntegrator
 */

public class RiemannNDIntegratorSampler implements SampledMappingIterator {

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
    private double[] sum;

    /**
     * Constructor.
     * Build an integrator from an underlying sample iterator.
     *
     * @param iter iterator over the base function
     */
    public RiemannNDIntegratorSampler(SampledMappingIterator iter)
            throws ExhaustedSampleException, MappingException {

        this.iter = iter;

        // get the first point
        current = iter.next();

        // initialize the sum
        sum = new double[getDimension()];
        for (int i = 0; i < sum.length; ++i) {
            sum[i] = 0.0;
        }

    }

    public SampledMapping getSampledMapping() {
        return iter.getSampledMapping();
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public int getDimension() {
        return iter.getSampledMapping().numOutputDimensions();
    }

    public ValuedPair next()
            throws ExhaustedSampleException, MappingException {

        // performs one step of a Riemann scheme
        ValuedPair previous = current;
        current = iter.next();
        double step = (current.getX()[0].doubleValue() - previous.getX()[0].doubleValue());
        double[] pY = getNumbersAsDouble(previous.getY());
        for (int i = 0; i < sum.length; ++i) {
            sum[i] += step * pY[i];
        }

        double[] values = new double[sum.length];
        System.arraycopy(sum, 0, values, 0, sum.length);
        return new ValuedPair(current.getX(), getDoublesAsNumber(values));

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private double[] getNumbersAsDouble(Number[] numbers) {
        double[] result;
        result = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            result[i] = numbers[i].doubleValue();
        }
        return result;
    }

    private Number[] getDoublesAsNumber(double[] doubles) {
        Number[] result;
        result = new Number[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            result[i] = new Double(doubles[i]);
        }
        return result;
    }

}
