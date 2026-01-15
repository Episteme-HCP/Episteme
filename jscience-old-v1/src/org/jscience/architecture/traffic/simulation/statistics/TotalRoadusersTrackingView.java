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

import org.jscience.architecture.traffic.infrastructure.Node.NodeStatistics;
import org.jscience.architecture.traffic.simulation.SimModel;


/**
 * TrackingView that tracks the total number of Roadusers that has arrived.
 *
 * @author Group GUI
 * @version 1.0
 */
public class TotalRoadusersTrackingView extends ExtendedTrackingView {
    /** DOCUMENT ME! */
    NodeStatistics[][] stats;

/**
     * Creates a new TotalRoadusersTrackingView object.
     *
     * @param startCycle DOCUMENT ME!
     * @param model      DOCUMENT ME!
     */
    public TotalRoadusersTrackingView(int startCycle, SimModel model) {
        super(startCycle);
        stats = model.getInfrastructure().getEdgeNodeStatistics();
    }

    /**
     * Returns the next sample to be 'tracked'.
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected float nextSample(int index) {
        int sample = 0;

        for (int i = 0; i < stats.length; i++)
            sample += stats[i][index].getTotalRoadusers();

        return (float) sample;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getYLabel() {
        return "arrived (roadusers)";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return "total arrived roadusers";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean useModes() {
        return false;
    }
}
