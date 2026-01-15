package org.jscience.mathematics.analysis.fitting;

import org.jscience.mathematics.analysis.SampledMapping;
import org.jscience.mathematics.analysis.SampledMappingIterator;
import org.jscience.mathematics.analysis.ValuedPair;

import java.io.Serializable;

/**
 * This class provides sampled values of the function t -> [f(t)^2, f'(t)^2].
 * <p/>
 * This class is a helper class used to compute a first guess of the
 * harmonic coefficients of a function <code>f (t) = a cos (omega t +
 * phi)</code>.
 *
 * @author L. Maisonobe
 * @version $Id: F2FP2Iterator.java,v 1.2 2007-10-21 17:38:16 virtualcall Exp $
 * @see FFPIterator
 * @see HarmonicCoefficientsGuesser
 */

class F2FP2Iterator
        implements SampledMappingIterator, Serializable {

    private FFPIterator ffpIterator;

    public F2FP2Iterator(AbstractCurveFitter.FitMeasurement[] measurements) {
        ffpIterator = new FFPIterator(measurements);
    }

    public int getDimension() {
        return 2;
    }

    public SampledMapping getSampledMapping() {
        return null;
    }

    public boolean hasNext() {
        return ffpIterator.hasNext();
    }

    public ValuedPair next() {

        // get the raw values from the underlying FFPIterator
        ValuedPair point = (ValuedPair) ffpIterator.next();

        // hack the values (to avoid building a new object)
        double[] y = getNumbersAsDouble(point.getY());
        y[0] *= y[0];
        y[1] *= y[1];
        return point;

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

}
