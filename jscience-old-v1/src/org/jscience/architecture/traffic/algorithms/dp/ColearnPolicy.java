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
package org.jscience.architecture.traffic.algorithms.dp;

import org.jscience.architecture.traffic.algorithms.tlc.Colearning;
import org.jscience.architecture.traffic.algorithms.tlc.TLController;
import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.infrastructure.Roaduser;
import org.jscience.architecture.traffic.infrastructure.Sign;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author Group Algorithms
 * @version 1.0 This is the class for driving policy using colearning.
 */
public class ColearnPolicy extends DrivingPolicy {
    /** DOCUMENT ME! */
    public static final String shortXMLName = "dp-cl";

    /** DOCUMENT ME! */
    protected Colearning tlc;

/**
     * The constructor for a colearning TC-1 driving policy.
     *
     * @param _m   The model which is used
     * @param _tlc DOCUMENT ME!
     */
    public ColearnPolicy(SimModel _m, TLController _tlc) {
        super(_m, _tlc);
        tlc = (Colearning) tlc;

        if (tlc == null) {
            tlc = (Colearning) _m.getTLController();
        }
    }

    /**
     * The lane to which a car continues his trip.
     *
     * @param r The road user being asked.
     * @param now_here DOCUMENT ME!
     * @param allOutgoing All the possible outgoing lanes
     * @param shortest All the lanes which are in a shortest path to the car's
     *        destination
     *
     * @return The chosen lane.
     */
    public Drivelane getDirectionLane(Roaduser r, Drivelane now_here,
        Drivelane[] allOutgoing, Drivelane[] shortest) {
        int num_sp = shortest.length;
        int num_out = allOutgoing.length;
        int laneI;
        Drivelane best = null;
        Drivelane lane;
        Node dest = r.getDestNode();
        float bestV = Float.MAX_VALUE;
        float laneV;

        for (int i = 0; i < num_out; i++) {
            for (int j = 0; j < num_sp; j++) {
                if (allOutgoing[i].getId() == shortest[j].getId()) {
                    lane = shortest[j];

                    if (lane.getSign().getType() == Sign.TRAFFICLIGHT) {
                        laneV = tlc.getColearnValue(now_here.getSign(),
                                lane.getSign(), dest, 0);

                        if (laneV < bestV) {
                            bestV = laneV;
                            best = lane;
                        }
                    }

                    if (best == null) {
                        best = lane;
                    }
                }
            }
        }

        return best;
    }

    /**
     * DOCUMENT ME!
     *
     * @param myElement DOCUMENT ME!
     * @param loader DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        System.out.println("DP-CL loaded");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        return new XMLElement(shortXMLName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return "model." + shortXMLName;
    }
}
