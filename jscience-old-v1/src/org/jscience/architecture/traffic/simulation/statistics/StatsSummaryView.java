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

import java.awt.*;


/**
 * Extension of StatisticsView showing a summary of all data.
 *
 * @author Group GUI
 * @version 1.0
 */
public class StatsSummaryView extends StatisticsView {
    /** DOCUMENT ME! */
    protected final static int LINE_HEIGHT = 20;

    /** DOCUMENT ME! */
    protected int x;

    /** DOCUMENT ME! */
    protected int y;

/**
     * Creates a new StatsSummaryView object.
     *
     * @param parent DOCUMENT ME!
     * @param stats  DOCUMENT ME!
     */
    public StatsSummaryView(StatisticsController parent, StatisticsModel stats) {
        super(parent, stats);
        parent.setScrollMax(0, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintStats(Graphics g) {
        g.setColor(Color.black);
        g.setFont(infoFont);

        y = paintArea.y + LINE_HEIGHT;

        infoLine(g,
            "Nodes: " + stats.getNumSpecialNodes() + " special nodes, " +
            stats.getNumJunctions() + " junctions");
        emptyLine();
        infoLine(g,
            "Total number of roadusers that has arrived at its destination: " +
            stats.getRoadusersArrived());
        infoLine(g,
            "Total number of junction crossings: " +
            stats.getJunctionCrossings());
        emptyLine();
        infoLine(g,
            "Average trip waiting time (based on all roadusers arrived): " +
            stats.getAllTimeTripWT());

        if (stats.getLastXTripCount() != stats.getRoadusersArrived()) {
            infoLine(g,
                "Average trip waiting time (based on last " +
                stats.getLastXTripCount() + " roadusers arrived): " +
                stats.getLastXTripWT());
        }

        infoLine(g,
            "Average junction waiting time (based on all junction crossings): " +
            stats.getAllTimeJunctionWT());

        if (stats.getLastXJunctionCount() != stats.getJunctionCrossings()) {
            infoLine(g,
                "Average junction waiting time (based on last " +
                stats.getLastXJunctionCount() + " junction crossings): " +
                stats.getLastXJunctionWT());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param s DOCUMENT ME!
     */
    protected void infoLine(Graphics g, String s) {
        g.drawString(s, x, y);
        y += LINE_HEIGHT;
    }

    /**
     * DOCUMENT ME!
     */
    protected void emptyLine() {
        y += LINE_HEIGHT;
    }

    /**
     * DOCUMENT ME!
     */
    protected void paintAreaChanged() {
        x = paintArea.x;
    }
}
