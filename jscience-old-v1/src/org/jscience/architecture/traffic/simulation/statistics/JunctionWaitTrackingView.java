/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
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
