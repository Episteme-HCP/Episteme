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
