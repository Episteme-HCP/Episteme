package org.jscience.mathematics.analysis;

/**
 * This class is a simple wrapper allowing to iterate over a
 * SampledFunction.
 * <p/>
 * <p>The basic implementation of the iteration interface does not
 * perform any transformation on the sample, it only handles a loop
 * over the underlying sampled function.</p>
 *
 * @author L. Maisonobe
 * @version $Id: SampledMappingIterator.java,v 1.3 2007-10-23 18:18:55 virtualcall Exp $
 * @see SampledMapping
 */

//Notice how close from Iterator this class is
//Java does not allow us to make a partial implementation of Iterator and still define this as an Interface
public interface SampledMappingIterator {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SampledMapping getSampledMapping();

    /**
     * Check if the iterator can provide another point.
     *
     * @return true if the iterator can provide another point.
     */
    public boolean hasNext();

    /**
     * Get the next point of a sampled function.
     *
     * @return the next point of the sampled function
     *
     * @throws ExhaustedSampleException if the sample has been exhausted
     * @throws MappingException if the underlying function throws one
     */
    public ValuedPair next() throws ExhaustedSampleException, MappingException;

    /**
     * DOCUMENT ME!
     */
    public void remove();
}
