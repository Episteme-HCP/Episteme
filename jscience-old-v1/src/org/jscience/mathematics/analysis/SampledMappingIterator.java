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
