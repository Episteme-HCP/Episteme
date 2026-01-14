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

import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;

import java.awt.*;


/**
 * An <code>ExtendedTrackingView</code> shows a certain tracking graph. To
 * be used in combination with ExtendedTrackingController. Makes it  possible
 * to draw a graph for each concrete roaduser type and all roadusers.
 *
 * @author Group GUI
 * @version 1.0
 */
public abstract class ExtendedTrackingView extends TrackingView {
    /** DOCUMENT ME! */
    protected boolean allTime;

/**
     * Creates a new <code>ExtendedTrackingView</code>. A
     * <code>TrackingView</code> is a component displaying a tracking graph.
     *
     * @param _startCycle The cycle this view starts tracking.
     */
    public ExtendedTrackingView(int _startCycle) {
        super(_startCycle);
    }

    /*============================================*/
    /* Basic GET and SET methods                  */
    /*============================================*/
    public void setAllTime(boolean b) {
        allTime = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getAllTime() {
        return allTime;
    }

    /**
     * Returns the colors to be used when drawing the tracking graphs.
     *
     * @return DOCUMENT ME!
     */
    protected Color[] getColors() {
        Color[] ruColors = new Color[MAX_TRACK];
        ruColors[0] = Color.black;

        for (int i = 1; i < MAX_TRACK; i++)
            ruColors[i] = RoaduserFactory.getColorByType(RoaduserFactory.statIndexToRuType(
                        i));

        return ruColors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int getMaxTrack() {
        return RoaduserFactory.statArrayLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSourceDesc(int i) {
        return RoaduserFactory.getDescByStatIndex(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean useModes() {
        return true;
    }
}
