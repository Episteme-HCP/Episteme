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

import org.jscience.architecture.traffic.infrastructure.EdgeNode;
import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.infrastructure.Node.NodeStatistics;


/**
 * TrackingView that tracks total roadusers arrived at a certain node.
 *
 * @author Group GUI
 * @version 1.0
 */
public class NodeRoadusersTrackingView extends ExtendedTrackingView {
    /** DOCUMENT ME! */
    NodeStatistics[] stats;

    /** DOCUMENT ME! */
    String desc;

/**
     * Creates a new NodeRoadusersTrackingView object.
     *
     * @param startCycle DOCUMENT ME!
     * @param node       DOCUMENT ME!
     */
    public NodeRoadusersTrackingView(int startCycle, Node node) {
        super(startCycle);
        stats = node.getStatistics();
        desc = "node " + node.getId() + " - roadusers ";

        if (node instanceof EdgeNode) {
            desc += "arrived";
        } else {
            desc += "crossed";
        }
    }

    /**
     * Returns the next sample to be 'tracked'.
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected float nextSample(int src) {
        return stats[src].getTotalRoadusers();
    }

    /**
     * Returns the description for this tracking window.
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return desc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getYLabel() {
        return "(roadusers)";
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
