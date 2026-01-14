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

package org.jscience.architecture.traffic.simulation.statistics;

import org.jscience.architecture.traffic.infrastructure.Junction;
import org.jscience.architecture.traffic.infrastructure.Node.NodeStatistics;


/**
 * TrackingView that tracks the waiting queue length of one EdgeNode.
 *
 * @author Group GUI
 * @version 1.0
 */
public class JunctionWaitTrackingView extends ExtendedTrackingView {
    /** DOCUMENT ME! */
    NodeStatistics[] stats;

    /** DOCUMENT ME! */
    int id;

/**
     * Creates a new JunctionWaitTrackingView object.
     *
     * @param startCycle DOCUMENT ME!
     * @param junction   DOCUMENT ME!
     */
    public JunctionWaitTrackingView(int startCycle, Junction junction) {
        super(startCycle);
        stats = junction.getStatistics();
        id = junction.getId();
    }

    /**
     * Returns the next sample to be 'tracked'.
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected float nextSample(int src) {
        return stats[src].getAvgWaitingTime(allTime);
    }

    /**
     * Returns the description for this tracking window.
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return "junction " + id + " - average junction waiting time";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getYLabel() {
        return "delay (cycles)";
    }
}
