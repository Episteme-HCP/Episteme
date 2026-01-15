package org.jscience.mathematics.analysis;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * This class is a simple wrapper allowing to iterate over a
 * SampledFunction.
 * <p/>
 * <p>The basic implementation of the iteration interface does not
 * perform any transformation on the sample, it only handles a loop
 * over the underlying sampled function.</p>
 *
 * @author L. Maisonobe
 * @version $Id: BasicSampledMappingIterator.java,v 1.2 2007-10-21 17:45:32 virtualcall Exp $
 * @see SampledMapping
 */
public class BasicSampledMappingIterator implements SampledMappingIterator, Serializable {

    /**
     * Underlying sampled function.
     */
    private final SampledMapping function;

    /**
     * Next sample element.
     */
    private int next;

    /**
     * Simple constructor.
     * Build an instance from a SampledFunction
     *
     * @param function smapled function over which we want to iterate
     */
    public BasicSampledMappingIterator(SampledMapping function) {
        this.function = function;
        next = 0;
    }

    public SampledMapping getSampledMapping() {
        return function;
    }

    public boolean hasNext() {
        return next < function.size();
    }

    public ValuedPair next() {
        if (next >= function.size()) {
            throw new NoSuchElementException();
        }

        int current = next++;

        return function.samplePointAt(current);

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
