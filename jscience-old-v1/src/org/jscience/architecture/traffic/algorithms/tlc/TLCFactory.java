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

package org.jscience.architecture.traffic.algorithms.tlc;

/**
 * This class can be used to create instances of Traffic Light Controllers for
 * a specific infrastructure.
 */
import org.jscience.architecture.traffic.infrastructure.InfraException;
import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.util.StringUtils;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class TLCFactory {
    /** DOCUMENT ME! */
    protected static final int RANDOM = 0;

    /** DOCUMENT ME! */
    protected static final int LONGEST_QUEUE = 1;

    /** DOCUMENT ME! */
    protected static final int MOST_CARS = 2;

    /** DOCUMENT ME! */
    protected static final int BEST_FIRST = 3;

    /** DOCUMENT ME! */
    protected static final int RELATIVE_LONGEST_QUEUE = 4;

    /** DOCUMENT ME! */
    protected static final int RLD = 5;

    /** DOCUMENT ME! */
    protected static final int RLD2 = 6;

    /** DOCUMENT ME! */
    protected static final int TC_1OPT = 7;

    /** DOCUMENT ME! */
    protected static final int TC_2OPT = 8;

    /** DOCUMENT ME! */
    protected static final int TC_3OPT = 9;

    /** DOCUMENT ME! */
    protected static final int TC1_B1 = 10;

    /** DOCUMENT ME! */
    protected static final int TC2_B1 = 11;

    /** DOCUMENT ME! */
    protected static final int TC3_B1 = 12;

    /** DOCUMENT ME! */
    protected static final int TC_1_DESTLESS = 13;

    /** DOCUMENT ME! */
    protected static final int TC_2_DESTLESS = 14;

    /** DOCUMENT ME! */
    protected static final int TC_3_WORKINPROGRESS = 15;

    /** DOCUMENT ME! */
    protected static final int ACGJ_1 = 16;

    /** DOCUMENT ME! */
    protected static final int ACGJ_2 = 17;

    /** DOCUMENT ME! */
    protected static final int ACGJ_3 = 18;

    /** DOCUMENT ME! */
    protected static final int ACGJ_3_FV = 19;

    /** DOCUMENT ME! */
    protected static final int ACGJ_4 = 20;

    /** DOCUMENT ME! */
    protected static final int ACGJ_5 = 21;

    /** DOCUMENT ME! */
    protected static final int LOCAL = 22;

    /** DOCUMENT ME! */
    protected static final int GENNEURAL = 23;

    /** DOCUMENT ME! */
    protected static final int TC_2FINAL = 24;

    /** DOCUMENT ME! */
    protected static final int TC1_FIX = 25;

    /** DOCUMENT ME! */
    protected static final int RLSARSA1 = 26;

    /** DOCUMENT ME! */
    protected static final int RLSARSA2 = 27;

    /** DOCUMENT ME! */
    protected static final int RLSARSA3 = 28;

    /** DOCUMENT ME! */
    protected static final int RLSARSA4 = 29;

    /** DOCUMENT ME! */
    protected static final int RLSARSA5 = 30;

    /** DOCUMENT ME! */
    protected static final int RLSARSA6 = 31;

    /** DOCUMENT ME! */
    protected static final String[] tlcDescs = {
            "Random", "Longest Queue", "Most Cars", "Best First",
            "Relative Longest Queue", "Red Light District",
            "Red Light District 2", "TC-1 Optimized 2.0",
            "TC-2 Optimized 2.0 (unfixed?)", "TC-3 Optimized 1.0 (unfixed?)",
            "TC-1 Bucket 2.0", "TC-2 Bucket 1.0", "TC-3 Bucket 1.0",
            "TC-1 Destinationless", "TC-2 Destinationless",
            "TC-3 Work In Progress", "ACGJ-1", "ACGJ-2", "ACGJ-3",
            "ACGJ-3 Stupidified", "ACGJ-4 : Gain Factoring, 2xDNA",
            "ACGJ-4 : Gain Factoring, 1xDNA", "Local Hillclimbing", "GenNeural",
            "TC-2 Final Version", "TC-1 version (not ok?)", "RL Sarsa 1",
            "RL Sarsa 2", "RL Sarsa 3", "RL Sarsa 4", "RL Sarsa 5", "RL Sarsa 6",
        };

    /** DOCUMENT ME! */
    protected static final String[] xmlNames = {
            RandomTLC.shortXMLName, LongestQueueTLC.shortXMLName,
            MostCarsTLC.shortXMLName, BestFirstTLC.shortXMLName,
            RelativeLongestQueueTLC.shortXMLName, RLDTLC.shortXMLName,
            RLD2TLC.shortXMLName, TC1TLCOpt.shortXMLName, TC2TLCOpt.shortXMLName,
            TC3Opt.shortXMLName, TC1B1.shortXMLName, TC2B1.shortXMLName,
            TC3B1.shortXMLName, TC1TLCDestless.shortXMLName,
            TC2TLCDestless.shortXMLName, TC3TLCWorkInProgress.shortXMLName,
            ACGJ1.shortXMLName, ACGJ2.shortXMLName, ACGJ3.shortXMLName,
            ACGJ3FixedValue.shortXMLName, ACGJ4.shortXMLName, ACGJ5.shortXMLName,
            LocalHillTLC.shortXMLName, GenNeuralTLC.shortXMLName,
            TC2Final.shortXMLName, TC1TLCFix.shortXMLName, SL1TLC.shortXMLName,
            SL2TLC.shortXMLName, SL3TLC.shortXMLName, SL4TLC.shortXMLName,
            SL5TLC.shortXMLName, SL6TLC.shortXMLName,
        };

    /** DOCUMENT ME! */
    protected static final String[] categoryDescs = {
            "Simple Maths", "Complex Maths", "Longest Q-variants",
            "Reinforcement Learning", "RL Sarsa TLCs", "Genetic",
            "Neural Network"
        };

    /** DOCUMENT ME! */
    protected static final int[][] categoryTLCs = {
            { RANDOM, MOST_CARS, RLD, RLD2 },
            { LOCAL, ACGJ_2 },
            { LONGEST_QUEUE, RELATIVE_LONGEST_QUEUE, BEST_FIRST },
            //		{TC1_FIX, TC_1OPT, TC_2OPT, TC_3OPT, TC1_B1, TC2_B1, TC3_B1, TC_1_DESTLESS, TC_2_DESTLESS, TC_3_WORKINPROGRESS, TC_2FINAL},        
            {TC1_FIX, TC_1OPT, TC_2OPT, TC_3OPT },
            { RLSARSA1, RLSARSA2, RLSARSA3, RLSARSA4, RLSARSA5, RLSARSA6 },
            { ACGJ_1, ACGJ_3, ACGJ_3_FV, ACGJ_4, ACGJ_5 },
            { GENNEURAL }
        };

    /** DOCUMENT ME! */
    protected Infrastructure infra;

    /** DOCUMENT ME! */
    protected Random random;

/**
     * Makes a new TLCFactory for a specific infrastructure with a new random
     * number generator.
     *
     * @param infra The infrastructure
     */
    public TLCFactory(Infrastructure infra) {
        this.infra = infra;
        random = new Random();
    }

/**
     * Makes a new TLCFactory for a specific infrastructure
     *
     * @param infra  The infrastructure
     * @param random The random number generator which some algorithms use
     */
    public TLCFactory(Infrastructure infra, Random random) {
        this.infra = infra;
        this.random = random;
    }

    /**
     * Looks up the id of a TLC algorithm by its description
     *
     * @param algoDesc The description of the algorithm
     *
     * @return The id of the algorithm
     */
    public static int getId(String algoDesc) {
        return StringUtils.getIndexObject(tlcDescs, algoDesc);
    }

    /**
     * Returns an array of TLC descriptions
     *
     * @return DOCUMENT ME!
     */
    public static String[] getTLCDescriptions() {
        return tlcDescs;
    }

    /**
     * Look up the description of a TLC algorithm by its id
     *
     * @param algoId The id of the algorithm
     *
     * @return The description
     */
    public static String getDescription(int algoId) {
        return (String) (StringUtils.lookUpNumber(tlcDescs, algoId));
    }

    /**
     * Returns an array containing the TLC category descriptions.
     *
     * @return DOCUMENT ME!
     */
    public static String[] getCategoryDescs() {
        return categoryDescs;
    }

    /**
     * Returns an array of TLC numbers for each TLC category.
     *
     * @return DOCUMENT ME!
     */
    public static int[][] getCategoryTLCs() {
        return categoryTLCs;
    }

    /**
     * Returns the total number of TLCs currently available.
     *
     * @return DOCUMENT ME!
     */
    public static int getNumberOfTLCs() {
        return tlcDescs.length;
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
     * Returns an instance of a TLC by its description.
     *
     * @param tlcDesc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public TLController genTLC(String tlcDesc) throws InfraException {
        return getInstanceForLoad(getId(tlcDesc));
    }

    /**
     * DOCUMENT ME!
     *
     * @param cat DOCUMENT ME!
     * @param tlc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public TLController genTLC(int cat, int tlc) throws InfraException {
        return getInstanceForLoad(categoryTLCs[cat][tlc]);
    }

    /**
     * Gets a new instance of an algorithm by its number. This method
     * is meant to be used for loading.
     *
     * @param algoId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public TLController getInstanceForLoad(int algoId)
        throws InfraException {
        switch (algoId) {
        case RANDOM:
            return ((random != null) ? new RandomTLC(infra, random)
                                     : new RandomTLC(infra));

        case LONGEST_QUEUE:
            return new LongestQueueTLC(infra);

        case MOST_CARS:
            return new MostCarsTLC(infra);

        case BEST_FIRST:
            return new BestFirstTLC(infra);

        case RELATIVE_LONGEST_QUEUE:
            return new RelativeLongestQueueTLC(infra);

        case RLD:
            return new RLDTLC(infra);

        case RLD2:
            return new RLD2TLC(infra);

        case TC_1OPT:
            return new TC1TLCOpt(infra);

        case TC_2OPT:
            return new TC2TLCOpt(infra);

        case TC_3OPT:
            return new TC3Opt(infra);

        case TC1_B1:
            return new TC1B1(infra);

        case TC2_B1:
            return new TC2B1(infra);

        case TC3_B1:
            return new TC3B1(infra);

        case TC_1_DESTLESS:
            return new TC1TLCDestless(infra);

        case TC_2_DESTLESS:
            return new TC2TLCDestless(infra);

        case TC_3_WORKINPROGRESS:
            return new TC3TLCWorkInProgress(infra);

        case ACGJ_1:
            return new ACGJ1(infra);

        case ACGJ_2:
            return new ACGJ2(infra);

        case ACGJ_3:
            return new ACGJ3(infra);

        case ACGJ_3_FV:
            return new ACGJ3FixedValue(infra);

        case ACGJ_4:
            return new ACGJ4(infra);

        case ACGJ_5:
            return new ACGJ4(infra);

        case LOCAL:
            return new LocalHillTLC(infra);

        case GENNEURAL:
            return new GenNeuralTLC(infra);

        case TC_2FINAL:
            return new TC2Final(infra);

        case TC1_FIX:
            return new TC1TLCFix(infra);

        case RLSARSA1:
            return new SL1TLC(infra);

        case RLSARSA2:
            return new SL2TLC(infra);

        case RLSARSA3:
            return new SL3TLC(infra);

        case RLSARSA4:
            return new SL4TLC(infra);

        case RLSARSA5:
            return new SL5TLC(infra);

        case RLSARSA6:
            return new SL6TLC(infra);
        }

        throw new InfraException("The TLCFactory can't make TLC's of type " +
            algoId);
    }
}
