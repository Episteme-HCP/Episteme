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
