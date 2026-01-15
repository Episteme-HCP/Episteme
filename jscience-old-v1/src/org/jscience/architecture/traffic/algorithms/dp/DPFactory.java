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

/**
 * This class can be used to create instances of Driving Policies for a
 * specific SimModel and TrafficLightController
 */
import org.jscience.architecture.traffic.algorithms.tlc.TLController;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.util.StringUtils;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class DPFactory {
    /** DOCUMENT ME! */
    public static final int SHORTEST_PATH = 0;

    /** DOCUMENT ME! */
    public static final int SMARTER_SHORTEST_PATH = 1;

    /** DOCUMENT ME! */
    public static final int AGGRESSIVE = 2;

    /** DOCUMENT ME! */
    public static final int COLEARNING = 3;

    /** DOCUMENT ME! */
    protected static final String[] dpDescs = {
            "Normal shortest path", "Least busy shortest path", "Aggressive",
            "Colearning"
        };

    /** DOCUMENT ME! */
    protected static final String[] xmlNames = {
            ShortestPathDP.shortXMLName, SmarterShortestPathDP.shortXMLName,
            AggressiveDP.shortXMLName, ColearnPolicy.shortXMLName
        };

    /** DOCUMENT ME! */
    protected SimModel model;

    /** DOCUMENT ME! */
    protected TLController tlc;

/**
     * Makes a new DPFactory for a specific SimModel and TLC
     *
     * @param model The SimModel to create the algorithm for
     * @param tlc   The traffic light controller to co-operate with
     */
    public DPFactory(SimModel model, TLController tlc) {
        this.model = model;
        this.tlc = tlc;
    }

    /**
     * Looks up the id of a DP algorithm by its description
     *
     * @param algoDesc The description of the algorithm
     *
     * @return The id of the algorithm
     */
    public static int getId(String algoDesc) {
        return StringUtils.getIndexObject(dpDescs, algoDesc);
    }

    /**
     * Returns an array of driving policy descriptions
     *
     * @return DOCUMENT ME!
     */
    public static String[] getDescriptions() {
        return dpDescs;
    }

    /**
     * Returns a new DrivingPolicy of the requested ID
     *
     * @param dp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public DrivingPolicy genDP(int dp) throws ClassNotFoundException {
        return getInstance(dp);
    }

    /**
     * Look up the description of a DP algorithm by its id
     *
     * @param algoId The id of the algorithm
     *
     * @return The description
     */
    public static String getDescription(int algoId) {
        return (String) (StringUtils.lookUpNumber(dpDescs, algoId));
    }

    /**
     * Gets the number of an algorithm from its XML tag name
     *
     * @param tagName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getNumberByXMLTagName(String tagName) {
        return StringUtils.getIndexObject(xmlNames, tagName);
    }

    /**
     * Gets a new instance of an algorithm by its number. This method
     * is meant to be used for loading.
     *
     * @param algoId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public DrivingPolicy getInstance(int algoId) throws ClassNotFoundException {
        switch (algoId) {
        case SHORTEST_PATH:
            return new ShortestPathDP(model, tlc);

        case SMARTER_SHORTEST_PATH:
            return new ShortestPathDP(model, tlc);

        case AGGRESSIVE:
            return new AggressiveDP(model, tlc);

        case COLEARNING:
            return new ColearnPolicy(model, tlc);
        }

        throw new ClassNotFoundException(
            "The DPFactory can't make DP's of type " + algoId);
    }
}
