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

package org.jscience.awt;

/**
 * This is a generic interface for sending data to 2D category graphs.
 *
 * @author Mark Hale
 * @version 1.0
 */
public interface CategoryGraph2DModel {
    /**
     * Add a listener.
     *
     * @param l DOCUMENT ME!
     */
    void addGraphDataListener(GraphDataListener l);

    /**
     * Remove a listener.
     *
     * @param l DOCUMENT ME!
     */
    void removeGraphDataListener(GraphDataListener l);

    /**
     * Returns the ith category.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String getCategory(int i);

    /**
     * Returns the value for the ith category.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    float getValue(int i);

    /**
     * Returns the number of data points in the current series.
     *
     * @return DOCUMENT ME!
     */
    int seriesLength();

    /**
     * Selects the first data series.
     */
    void firstSeries();

    /**
     * Selects the next data series. Returns false if there is no next
     * series.
     *
     * @return DOCUMENT ME!
     */
    boolean nextSeries();
}
