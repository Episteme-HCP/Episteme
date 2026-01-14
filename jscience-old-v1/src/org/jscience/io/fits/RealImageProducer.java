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

package org.jscience.io.fits;

/**
 * This interface is analogous to ImageProducer, except that it handles pixels
 * of type double. This paradigm for handling images may get scrapped.
 */
public interface RealImageProducer {
    /**
     * register a consumer to receive pixels from this producer.
     *
     * @param ic DOCUMENT ME!
     */
    public void addConsumer(RealImageConsumer ic);

    /**
     * returns true if the given consumer is registered with this
     * producer
     *
     * @param ic DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConsumer(RealImageConsumer ic);

    /**
     * unregister a consumer
     *
     * @param ic DOCUMENT ME!
     */
    public void removeConsumer(RealImageConsumer ic);

    /**
     * register the given consumer and begin sending pixels
     *
     * @param ic DOCUMENT ME!
     */
    public void startProduction(RealImageConsumer ic);

    /**
     * register the given consumer and request that this producer start
     * sending pixels in TDLR order. This method is not required to do
     * anything
     *
     * @param ic DOCUMENT ME!
     */
    public void requestTopDownLeftRightResend(RealImageConsumer ic);
} // end of RealImageProducer interface
