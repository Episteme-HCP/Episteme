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
package org.jscience.architecture.traffic.algorithms.tlc;

import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.Roaduser;
import org.jscience.architecture.traffic.infrastructure.Sign;
import org.jscience.architecture.traffic.xml.XMLCannotSaveException;
import org.jscience.architecture.traffic.xml.XMLElement;


/**
 * Red Light District TLC... This controller will switch TrafficLights so
 * that the Trafficlight with the shortest queue of  waiting Roadusers will
 * get set to green.
 *
 * @author Group Algorithms
 * @version 1.0
 */
public class RLDTLC extends TLController {
    /** DOCUMENT ME! */
    protected final static String shortXMLName = "tlc-rld";

    /** DOCUMENT ME! */
    protected int num_nodes;

/**
     * The constructor for TL controllers
     *
     * @param i model being used.
     */
    public RLDTLC(Infrastructure i) {
        super(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure i) {
        super.setInfrastructure(i);
        num_nodes = tld.length;
    }

    /**
     * This implementation sets the Q-values according to the length of
     * the waiting queue. The longer the queue, the higher the Q-value.
     *
     * @return DOCUMENT ME!
     */
    public TLDecision[][] decideTLs() {
        int minLength;
        int num_lanes;
        int minId;
        int temp_len;
        int ru_pos;

        for (int i = 0; i < num_nodes; i++) {
            minLength = Integer.MAX_VALUE;
            minId = -1;
            num_lanes = tld[i].length;

            for (int j = 0; j < num_lanes; j++) {
                temp_len = tld[i][j].getTL().getLane().getNumRoadusersWaiting();

                if ((temp_len < minLength) && (temp_len > 0)) {
                    minLength = temp_len;
                    minId = j;
                }

                tld[i][j].setGain(0);
            }

            if (minId != -1) {
                tld[i][minId].setGain(1);
            }

            //System.out.println("Node:"+i+" LongestQueue at Sign:"+minId+" with length "+minLength);
        }

        return tld;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _ru DOCUMENT ME!
     * @param _prevlane DOCUMENT ME!
     * @param _prevsign DOCUMENT ME!
     * @param _prevpos DOCUMENT ME!
     * @param _dlanenow DOCUMENT ME!
     * @param _signnow DOCUMENT ME!
     * @param _posnow DOCUMENT ME!
     * @param posMovs DOCUMENT ME!
     * @param desired DOCUMENT ME!
     */
    public void updateRoaduserMove(Roaduser _ru, Drivelane _prevlane,
        Sign _prevsign, int _prevpos, Drivelane _dlanenow, Sign _signnow,
        int _posnow, PosMov[] posMovs, Drivelane desired) { // No needed
    }

    // XMLSerializable implementation
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = super.saveSelf();
        result.setName(shortXMLName);

        return result;
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
