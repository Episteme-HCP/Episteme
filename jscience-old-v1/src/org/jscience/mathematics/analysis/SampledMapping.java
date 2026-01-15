package org.jscience.mathematics.analysis;

/**
 * This interface represent sampled scalar functions.
 * <p/>
 * <p>A function sample is an ordered set of points of the form (x, y)
 * where x is the abscissa of the point and y is the function value at
 * x. It is typically a function that has been computed by external
 * means or the result of measurements.</p>
 * <p/>
 * <p>The {@link PrimitiveMappingSampler} class can be used to
 * transform classes implementing the {@link PrimitiveMapping}
 * interface into classes implementing this interface.</p>
 * <p/>
 * <p>Sampled functions cannot be directly handled by integrators
 * implementing the {@link
 * org.jscience.mathematics.analysis.quadrature.SampledFunctionIntegrator
 * SampledFunctionIntegrator}. These integrators need a {@link
 * SampledFunctionIterator} object to iterate over the
 * sample.</p>
 *
 * @author L. Maisonobe
 * @version $Id: SampledMapping.java,v 1.3 2007-10-23 18:18:55 virtualcall Exp $
 * @see SampledMappingIterator
 * @see PrimitiveMappingSampler
 * @see AbstractMapping
 */
public interface SampledMapping {
    /**
     * Get the number of points in the sample.
     *
     * @return number of points in the sample
     */
    public int size();

    /**
     * Get the dimension of the input values of the function.
     *
     * @return dimension
     */
    public int numInputDimensions();

    /**
     * Get the dimension of the output values of the function.
     *
     * @return dimension
     */
    public int numOutputDimensions();

    /**
     * Get the abscissa and value of the sample at the specified index.
     *
     * @param index index in the sample, should be between 0 and {@link #size}
     *        - 1
     *
     * @return abscissa and value of the sample at the specified index
     *
     * @throws ArrayIndexOutOfBoundsException if the index is wrong
     */
    public ValuedPair samplePointAt(int index)
        throws ArrayIndexOutOfBoundsException;
}
