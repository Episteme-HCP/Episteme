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

import org.jscience.architecture.traffic.infrastructure.SpecialNode;
import org.jscience.architecture.traffic.simulation.SimModel;


/**
 * TrackingView that tracks the sum of the lengths of all waiting queues.
 *
 * @author Group GUI
 * @version 1.0
 */
public class AllQueuesTrackingView extends TrackingView {
    /** DOCUMENT ME! */
    SpecialNode[] specialNodes;

    /** DOCUMENT ME! */
    int numSpecialNodes;

/**
     * Creates a new AllQueuesTrackingView object.
     *
     * @param startCycle DOCUMENT ME!
     * @param model      DOCUMENT ME!
     */
    public AllQueuesTrackingView(int startCycle, SimModel model) {
        super(startCycle);
        specialNodes = model.getInfrastructure().getSpecialNodes();
        numSpecialNodes = specialNodes.length;
    }

    /**
     * Returns the next sample to be 'tracked'.
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected float nextSample(int src) {
        int sample = 0;

        for (int i = 0; i < numSpecialNodes; i++)
            sample += specialNodes[i].getWaitingQueueLength();

        return sample;
    }

    /**
     * Returns the description for this tracking window.
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return "total waiting queue length";
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSourceDesc(int i) {
        return "length";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getYLabel() {
        return "queue length (roadusers)";
    }
}
