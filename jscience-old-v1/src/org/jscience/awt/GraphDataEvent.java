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

import java.util.EventObject;


/**
 * Defines an event that encapsulates changes to a graph.
 *
 * @author Mark Hale
 * @version 1.1
 */
public final class GraphDataEvent extends EventObject {
    /** Specifies all series. */
    public static final int ALL_SERIES = -1;

    /** DOCUMENT ME! */
    private final int series;

    /** DOCUMENT ME! */
    private final boolean isIncremental;

/**
     * All series data in the graph has changed.
     *
     * @param src DOCUMENT ME!
     */
    public GraphDataEvent(Object src) {
        this(src, ALL_SERIES, false);
    }

/**
     * This series has changed.
     *
     * @param src           DOCUMENT ME!
     * @param seriesChanged The index of the series that may have changed.
     */
    public GraphDataEvent(Object src, int seriesChanged) {
        this(src, seriesChanged, false);
    }

/**
     * This series has changed incrementally. Useful for streaming data to a
     * graph.
     *
     * @param src                 DOCUMENT ME!
     * @param seriesChanged       The index of the series that may have changed.
     * @param isIncrementalChange True indicates an extra data point has been
     *                            added.
     */
    public GraphDataEvent(Object src, int seriesChanged,
        boolean isIncrementalChange) {
        super(src);
        series = seriesChanged;
        isIncremental = isIncrementalChange;
    }

    /**
     * Returns the series that has changed.
     *
     * @return The index of the series that may have changed or ALL_SERIES.
     */
    public int getSeries() {
        return series;
    }

    /**
     * Returns whether the change was incremental.
     *
     * @return True if an extra data point has been added.
     */
    public boolean isIncremental() {
        return isIncremental;
    }
}
