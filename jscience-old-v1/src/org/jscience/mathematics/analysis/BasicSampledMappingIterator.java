/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
