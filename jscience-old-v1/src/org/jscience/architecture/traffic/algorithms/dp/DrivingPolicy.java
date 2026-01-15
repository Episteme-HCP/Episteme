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

import org.jscience.architecture.traffic.algorithms.tlc.TLController;
import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.InfraException;
import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.infrastructure.Roaduser;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Dictionary;


/**
 * This is the abstract class for each driving policy.
 *
 * @author Group Algorithms
 * @version 1.0
 */
public abstract class DrivingPolicy implements XMLSerializable, TwoStageLoader {
    /** DOCUMENT ME! */
    protected SimModel model;

    /** DOCUMENT ME! */
    protected TLController tlc;

/**
     * Creates a new DrivingPolicy object.
     *
     * @param m    DOCUMENT ME!
     * @param _tlc DOCUMENT ME!
     */
    DrivingPolicy(SimModel m, TLController _tlc) {
        model = m;
        tlc = _tlc;
    }

    /**
     * The lane to which a car continues his trip.
     *
     * @param r The road user being asked.
     * @param lane_now All the possible outgoing lanes
     * @param node_now All the lanes which are in a shortest path to the car's
     *        destination
     *
     * @return The chosen lane.
     *
     * @throws InfraException DOCUMENT ME!
     */
    public Drivelane getDirection(Roaduser r, Drivelane lane_now, Node node_now)
        throws InfraException {
        Drivelane[] lanesleadingfrom = node_now.getLanesLeadingFrom(lane_now,
                r.getType());
        Drivelane[] shortestpaths = node_now.getShortestPaths(r.getDestNode()
                                                               .getId(),
                r.getType());

        return getDirectionLane(r, lane_now, lanesleadingfrom, shortestpaths);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param lane_now DOCUMENT ME!
     * @param allOutgoing DOCUMENT ME!
     * @param shortest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Drivelane getDirectionLane(Roaduser r, Drivelane lane_now,
        Drivelane[] allOutgoing, Drivelane[] shortest);

    // Generic XMLSerializable implementation
    /**
     * Empty for Drivingpolicy
     *
     * @param myElement DOCUMENT ME!
     * @param loader DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException { // Empty
    }

    /**
     * Empty for Drivingpolicy
     *
     * @param saver DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException { // Empty
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void setParentName(String parentName) throws XMLTreeException {
        throw new XMLTreeException(
            "Attempt to change fixed parentName of a DP class.");
    }

    // Empty TwoStageLoader (standard)
    /**
     * DOCUMENT ME!
     *
     * @param dictionaries DOCUMENT ME!
     *
     * @throws XMLInvalidInputException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void loadSecondStage(Dictionary dictionaries)
        throws XMLInvalidInputException, XMLTreeException {
    }
}
