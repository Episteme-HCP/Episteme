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

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.infrastructure.*;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;


/**
 * This controller will decide it's Q values for the traffic lights
 * according to the traffic situation on the lane connected to the
 * TrafficLight. It will learn how to alter it's outcome by reinforcement
 * learning. Now Optimized 2.0 Now Long-Fixed, so run run run, Forrest!
 *
 * @author Arne K, Jilles V
 * @version 2.0
 */
public class TC1TLCOpt extends TLController implements Colearning,
    InstantiationAssistant {
    /** DOCUMENT ME! */
    protected static float gamma = 0.90f; //Discount Factor; used to decrease the influence of previous V values, that's why: 0 < gamma < 1

    /** DOCUMENT ME! */
    protected final static boolean red = false;

    /** DOCUMENT ME! */
    protected final static boolean green = true;

    /** DOCUMENT ME! */
    protected final static int green_index = 0;

    /** DOCUMENT ME! */
    protected final static int red_index = 1;

    /** DOCUMENT ME! */
    protected final static String shortXMLName = "tlc-tc1o1";

    /** DOCUMENT ME! */
    protected static float random_chance = 0.01f; //A random gain setting is chosen instead of the on the TLC dictates with this chance

    // TLC vars
    /** DOCUMENT ME! */
    protected Infrastructure infrastructure;

    /** DOCUMENT ME! */
    protected TrafficLight[][] tls;

    /** DOCUMENT ME! */
    protected Node[] allnodes;

    /** DOCUMENT ME! */
    protected int num_nodes;

    // TC1 vars
    /** DOCUMENT ME! */
    protected Vector[][][] count;

    // TC1 vars
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    protected Vector[][][] pTable;

    /** DOCUMENT ME! */
    protected float[][][][] qTable; //sign, pos, des, color (red=0, green=1)

    /** DOCUMENT ME! */
    protected float[][][] vTable;

    /** DOCUMENT ME! */
    private Random random_number;

/**
     * The constructor for TL controllers
     *
     * @param infra model being used.
     * @throws InfraException DOCUMENT ME!
     */
    public TC1TLCOpt(Infrastructure infra) throws InfraException {
        super(infra);
    }

    /**
     * DOCUMENT ME!
     *
     * @param infra DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure infra) {
        super.setInfrastructure(infra);

        try {
            Node[] nodes = infra.getAllNodes();
            num_nodes = nodes.length;

            int numSigns = infra.getAllInboundLanes().size();
            qTable = new float[numSigns][][][];
            vTable = new float[numSigns][][];
            count = new Vector[numSigns][][];
            pTable = new Vector[numSigns][][];

            int num_specialnodes = infra.getNumSpecialNodes();

            for (int i = 0; i < num_nodes; i++) {
                Node n = nodes[i];
                Drivelane[] dls = n.getInboundLanes();

                for (int j = 0; j < dls.length; j++) {
                    Drivelane d = dls[j];
                    Sign s = d.getSign();
                    int id = s.getId();
                    int num_pos_on_dl = d.getCompleteLength();

                    qTable[id] = new float[num_pos_on_dl][][];
                    vTable[id] = new float[num_pos_on_dl][];
                    count[id] = new Vector[num_pos_on_dl][];
                    pTable[id] = new Vector[num_pos_on_dl][];

                    for (int k = 0; k < num_pos_on_dl; k++) {
                        qTable[id][k] = new float[num_specialnodes][];
                        vTable[id][k] = new float[num_specialnodes];
                        count[id][k] = new Vector[num_specialnodes];
                        pTable[id][k] = new Vector[num_specialnodes];

                        for (int l = 0; l < num_specialnodes; l++) {
                            qTable[id][k][l] = new float[2];
                            qTable[id][k][l][0] = 0.0f;
                            qTable[id][k][l][1] = 0.0f;
                            vTable[id][k][l] = 0.0f;
                            count[id][k][l] = new Vector();
                            pTable[id][k][l] = new Vector();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        random_number = new Random();
    }

    /**
     * Calculates how every traffic light should be switched Per node,
     * per sign the waiting roadusers are passed and per each roaduser the
     * gain is calculated.
     *
     * @return DOCUMENT ME!
     *
     * @see gld.algo.tlc.TLDecision
     */
    public TLDecision[][] decideTLs() {
        /* gain = 0
         * For each TL
         *  For each Roaduser waiting
         *   gain = gain + pf*(Q([tl,pos,des],red) - Q([tl,pos,des],green))
         */
        int num_dec;

        /* gain = 0
         * For each TL
         *  For each Roaduser waiting
         *   gain = gain + pf*(Q([tl,pos,des],red) - Q([tl,pos,des],green))
         */
        int waitingsize;

        /* gain = 0
         * For each TL
         *  For each Roaduser waiting
         *   gain = gain + pf*(Q([tl,pos,des],red) - Q([tl,pos,des],green))
         */
        int pos;

        /* gain = 0
         * For each TL
         *  For each Roaduser waiting
         *   gain = gain + pf*(Q([tl,pos,des],red) - Q([tl,pos,des],green))
         */
        int tlId;

        /* gain = 0
         * For each TL
         *  For each Roaduser waiting
         *   gain = gain + pf*(Q([tl,pos,des],red) - Q([tl,pos,des],green))
         */
        int desId;
        float gain;
        float passenger_factor;
        Sign tl;
        Drivelane lane;
        Roaduser ru;
        ListIterator queue;
        Node destination;

        //Determine wheter it should be random or not
        boolean randomrun = false;

        if (random_number.nextFloat() < random_chance) {
            randomrun = true;
        }

        // For all Nodes
        for (int i = 0; i < num_nodes; i++) {
            num_dec = tld[i].length;

            // For all Trafficlights
            for (int j = 0; j < num_dec; j++) {
                tl = tld[i][j].getTL();
                tlId = tl.getId();
                lane = tld[i][j].getTL().getLane();

                waitingsize = lane.getNumRoadusersWaiting();
                queue = lane.getQueue().listIterator();
                gain = 0;

                // For each waiting Roaduser
                for (int k = 0; k < waitingsize; k++) {
                    ru = (Roaduser) queue.next();
                    pos = ru.getPosition();
                    desId = ru.getDestNode().getId();
                    passenger_factor = ru.getNumPassengers();

                    // Add the pf*(Q([tl,pos,des],red)-Q([tl,pos,des],green))
                    gain += (passenger_factor * (qTable[tlId][pos][desId][red_index] -
                    qTable[tlId][pos][desId][green_index])); //red - green
                }

                // Debug info generator
                if ((trackNode != -1) && (i == trackNode)) {
                    Drivelane currentlane2 = tld[i][j].getTL().getLane();
                    boolean[] targets = currentlane2.getTargets();
                    System.out.println("node: " + i + " light: " + j +
                        " gain: " + gain + " " + targets[0] + " " + targets[1] +
                        " " + targets[2] + " " +
                        currentlane2.getNumRoadusersWaiting());
                }

                // If this is a random run, set all gains randomly
                if (randomrun) {
                    gain = random_number.nextFloat();
                }

                if ((gain > 1000.0) || (gain < -1000.0f)) {
                    System.out.println("Gain might be too high? : " + gain);
                }

                tld[i][j].setGain(gain);
            }
        }

        return tld;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ru DOCUMENT ME!
     * @param prevlane DOCUMENT ME!
     * @param prevsign DOCUMENT ME!
     * @param prevpos DOCUMENT ME!
     * @param dlanenow DOCUMENT ME!
     * @param signnow DOCUMENT ME!
     * @param posnow DOCUMENT ME!
     * @param posMovs DOCUMENT ME!
     * @param desired DOCUMENT ME!
     */
    public void updateRoaduserMove(Roaduser ru, Drivelane prevlane,
        Sign prevsign, int prevpos, Drivelane dlanenow, Sign signnow,
        int posnow, PosMov[] posMovs, Drivelane desired) {
        // Roaduser has just left the building!
        if ((dlanenow == null) || (signnow == null)) {
            return;
        }

        //This ordening is important for the execution of the algorithm!
        if ((prevsign.getType() == Sign.TRAFFICLIGHT) &&
                ((signnow.getType() == Sign.TRAFFICLIGHT) ||
                (signnow.getType() == Sign.NO_SIGN))) {
            int tlId = prevsign.getId();
            int desId = ru.getDestNode().getId();
            recalcP(tlId, prevpos, desId, prevsign.mayDrive(), signnow.getId(),
                posnow);
            recalcQ(tlId, prevpos, desId, prevsign.mayDrive(), signnow.getId(),
                posnow, posMovs);
            recalcV(tlId, prevpos, desId);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tlId DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param desId DOCUMENT ME!
     * @param light DOCUMENT ME!
     * @param tlNewId DOCUMENT ME!
     * @param posNew DOCUMENT ME!
     */
    protected void recalcP(int tlId, int pos, int desId, boolean light,
        int tlNewId, int posNew) {
        // - First create a CountEntry, find if it exists, and if not add it.
        CountEntry thisSituation = new CountEntry(tlId, pos, desId, light,
                tlNewId, posNew);
        int c_index = count[tlId][pos][desId].indexOf(thisSituation);

        if (c_index >= 0) {
            // Entry found
            thisSituation = (CountEntry) count[tlId][pos][desId].elementAt(c_index);
            thisSituation.incrementValue();
        } else {
            // Entry not found
            count[tlId][pos][desId].addElement(thisSituation);
        }

        // We now know how often this exact situation has occurred
        // - Calculate the chance
        long sameSituation = thisSituation.getValue();
        long sameStartSituation = 0;

        CountEntry curC;
        int num_c = count[tlId][pos][desId].size();

        for (int i = 0; i < num_c; i++) {
            curC = (CountEntry) count[tlId][pos][desId].elementAt(i);
            sameStartSituation += curC.sameStartSituation(thisSituation);
        }

        // - Update this chance
        // Calculate the new P(L|(tl,pos,des))
        // P(L|(tl,pos,des))	= P([tl,pos,des],L)/P([tl,pos,des])
        //						= #([tl,pos,des],L)/#([tl,pos,des])
        // Niet duidelijk of dit P([tl,p,d],L,[*,*]) of P([tl,p,d],L,[tl,d]) moet zijn
        // Oftewel, kans op deze transitie of kans om te wachten!
        PEntry thisChance = new PEntry(tlId, pos, desId, light, tlNewId, posNew);
        int p_index = pTable[tlId][pos][desId].indexOf(thisChance);

        if (p_index >= 0) {
            thisChance = (PEntry) pTable[tlId][pos][desId].elementAt(p_index);
        } else {
            pTable[tlId][pos][desId].addElement(thisChance);
            p_index = pTable[tlId][pos][desId].indexOf(thisChance);
        }

        thisChance.setSameSituation(sameSituation);
        thisChance.setSameStartSituation(sameStartSituation);

        // - Update rest of the Chance Table
        int num_p = pTable[tlId][pos][desId].size();
        PEntry curP;

        for (int i = 0; i < num_p; i++) {
            curP = (PEntry) pTable[tlId][pos][desId].elementAt(i);

            if (curP.sameStartSituation(thisSituation) && (i != p_index)) {
                curP.addSameStartSituation();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tlId DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param desId DOCUMENT ME!
     * @param light DOCUMENT ME!
     * @param tlNewId DOCUMENT ME!
     * @param posNew DOCUMENT ME!
     * @param posMovs DOCUMENT ME!
     */
    protected void recalcQ(int tlId, int pos, int desId, boolean light,
        int tlNewId, int posNew, PosMov[] posMovs) { // Meneer Kaktus zegt: OK!
                                                     // Q([tl,p,d],L)	= Sum(tl', p') [P([tl,p,d],L,[tl',p'])(R([tl,p],[tl',p'])+ yV([tl',p',d]))
                                                     // First gather All tl' and p' in one array

        int num_posmovs = posMovs.length;

        PosMov curPosMov;
        int curPMTlId;
        int curPMPos;
        float R = 0;
        float V = 0;
        float Q = 0;

        for (int t = 0; t < num_posmovs; t++) { // For All tl', pos'
            curPosMov = posMovs[t];
            curPMTlId = curPosMov.tlId;
            curPMPos = curPosMov.pos;

            PEntry P = new PEntry(tlId, pos, desId, light, curPMTlId, curPMPos);
            int p_index = pTable[tlId][pos][desId].indexOf(P);

            if (p_index >= 0) {
                P = (PEntry) pTable[tlId][pos][desId].elementAt(p_index);
                R = rewardFunction(tlId, pos, curPMTlId, curPMPos);
                V = vTable[curPMTlId][curPMPos][desId];
                Q += (P.getChance() * (R + (gamma * V)));
            }

            // Else P(..)=0, thus will not add anything in the summation
        }

        qTable[tlId][pos][desId][light ? green_index : red_index] = Q;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tlId DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param desId DOCUMENT ME!
     */
    protected void recalcV(int tlId, int pos, int desId) { //  V([tl,p,d]) = Sum (L) [P(L|(tl,p,d))Q([tl,p,d],L)]

        float qRed = qTable[tlId][pos][desId][red_index];
        float qGreen = qTable[tlId][pos][desId][green_index];
        float[] pGR = calcPGR(tlId, pos, desId);
        float pGreen = pGR[green_index];
        float pRed = pGR[red_index];

        vTable[tlId][pos][desId] = (pGreen * qGreen) + (pRed * qRed);
    }

    /*
                            ==========================================================================
                                                    Additional methods, used by the recalc methods
                            ==========================================================================
    */
    protected float[] calcPGR(int tlId, int pos, int desId) {
        float[] counters = new float[2];
        double countR = 0;
        double countG = 0;

        int psize = pTable[tlId][pos][desId].size() - 1;

        for (; psize >= 0; psize--) {
            PEntry cur = (PEntry) pTable[tlId][pos][desId].elementAt(psize);

            if (cur.light == green) {
                countG += cur.getSameSituation();
            } else {
                countR += cur.getSameSituation();
            }
        }

        counters[green_index] = (float) (countG / (countG + countR));
        counters[red_index] = (float) (countR / (countG + countR));

        return counters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tlId DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param tlNewId DOCUMENT ME!
     * @param posNew DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int rewardFunction(int tlId, int pos, int tlNewId, int posNew) {
        if ((tlId != tlNewId) || (pos != posNew)) {
            return 0;
        }

        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sign DOCUMENT ME!
     * @param des DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getVValue(Sign sign, Node des, int pos) {
        return vTable[sign.getId()][pos][des.getId()];
    }

    /**
     * DOCUMENT ME!
     *
     * @param now DOCUMENT ME!
     * @param sign DOCUMENT ME!
     * @param des DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getColearnValue(Sign now, Sign sign, Node des, int pos) {
        return getVValue(sign, des, pos);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void showSettings(Controller c) {
        String[] descs = { "Gamma (discount factor)", "Random decision chance" };
        float[] floats = { gamma, random_chance };
        TLCSettings settings = new TLCSettings(descs, null, floats);

        settings = doSettingsDialog(c, settings);
        gamma = settings.floats[0];
        random_chance = settings.floats[1];
    }

    // XMLSerializable, SecondStageLoader and InstantiationAssistant implementation
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
        super.load(myElement, loader);
        gamma = myElement.getAttribute("gamma").getFloatValue();
        random_chance = myElement.getAttribute("random-chance").getFloatValue();
        qTable = (float[][][][]) XMLArray.loadArray(this, loader);
        vTable = (float[][][]) XMLArray.loadArray(this, loader);
        count = (Vector[][][]) XMLArray.loadArray(this, loader, this);
        pTable = (Vector[][][]) XMLArray.loadArray(this, loader, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param saver DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException {
        super.saveChilds(saver);
        XMLArray.saveArray(qTable, this, saver, "q-table");
        XMLArray.saveArray(vTable, this, saver, "v-table");
        XMLArray.saveArray(count, this, saver, "counts");
        XMLArray.saveArray(pTable, this, saver, "p-table");
    }

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
        result.addAttribute(new XMLAttribute("random-chance", random_chance));
        result.addAttribute(new XMLAttribute("gamma", gamma));

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

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canCreateInstance(Class request) {
        System.out.println("Called TC1TLC-opt instantiation assistant ??");

        return CountEntry.class.equals(request) ||
        PEntry.class.equals(request);
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     * @throws InstantiationException DOCUMENT ME!
     * @throws IllegalAccessException DOCUMENT ME!
     */
    public Object createInstance(Class request)
        throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        System.out.println("Called TC1TLC-opt instantiation assistant");

        if (CountEntry.class.equals(request)) {
            return new CountEntry();
        } else if (PEntry.class.equals(request)) {
            return new PEntry();
        } else {
            throw new ClassNotFoundException(
                "TC1 IntstantiationAssistant cannot make instances of " +
                request);
        }
    }

    /*
                            ==========================================================================
                                    Internal Classes to provide a way to put entries into the tables
                            ==========================================================================
    */
    public class CountEntry implements XMLSerializable {
        // CountEntry vars
        /** DOCUMENT ME! */
        int tlId;

        // CountEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int pos;

        // CountEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int desId;

        // CountEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int tlNewId;

        // CountEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int posNew;

        /** DOCUMENT ME! */
        long value;

        /** DOCUMENT ME! */
        boolean light;

        // XML vars
        /** DOCUMENT ME! */
        String parentName = "model.tlc";

/**
         * Creates a new CountEntry object.
         *
         * @param _tlId    DOCUMENT ME!
         * @param _pos     DOCUMENT ME!
         * @param _desId   DOCUMENT ME!
         * @param _light   DOCUMENT ME!
         * @param _tlNewId DOCUMENT ME!
         * @param _posNew  DOCUMENT ME!
         */
        CountEntry(int _tlId, int _pos, int _desId, boolean _light,
            int _tlNewId, int _posNew) {
            tlId = _tlId; // The Sign the RU was at
            pos = _pos; // The position the RU was at
            desId = _desId; // The SpecialNode the RU is travelling to
            light = _light; // The colour of the Sign the RU is at now
            tlNewId = _tlNewId; // The Sign the RU is at now
            posNew = _posNew; // The position the RU is on now
            value = 1; // How often this situation has occurred
        }

/**
         * Creates a new CountEntry object.
         */
        public CountEntry() { // Empty constructor for loading
        }

        /**
         * DOCUMENT ME!
         */
        public void incrementValue() {
            value++;
        }

        // Returns how often this situation has occurred
        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public long getValue() {
            return value;
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object other) {
            if ((other != null) && other instanceof CountEntry) {
                CountEntry countnew = (CountEntry) other;

                if (countnew.tlId != tlId) {
                    return false;
                }

                if (countnew.pos != pos) {
                    return false;
                }

                if (countnew.desId != desId) {
                    return false;
                }

                if (countnew.light != light) {
                    return false;
                }

                if (countnew.tlNewId != tlNewId) {
                    return false;
                }

                if (countnew.posNew != posNew) {
                    return false;
                }

                return true;
            }

            return false;
        }

        // Retuns the count-value if the situations match
        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public long sameSituation(CountEntry other) {
            if (equals(other)) {
                return value;
            } else {
                return 0;
            }
        }

        // Retuns the count-value if the startingsituations match
        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public long sameStartSituation(CountEntry other) {
            if ((other.tlId == tlId) && (other.pos == pos) &&
                    (other.desId == desId) && (other.light == light)) {
                return value;
            } else {
                return 0;
            }
        }

        // XMLSerializable implementation of CountEntry
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
            pos = myElement.getAttribute("pos").getIntValue();
            tlId = myElement.getAttribute("tl-id").getIntValue();
            desId = myElement.getAttribute("des-id").getIntValue();
            light = myElement.getAttribute("light").getBoolValue();
            tlNewId = myElement.getAttribute("new-tl-id").getIntValue();
            posNew = myElement.getAttribute("new-pos").getIntValue();
            value = myElement.getAttribute("value").getLongValue();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("count");
            result.addAttribute(new XMLAttribute("pos", pos));
            result.addAttribute(new XMLAttribute("tl-id", tlId));
            result.addAttribute(new XMLAttribute("des-id", desId));
            result.addAttribute(new XMLAttribute("light", light));
            result.addAttribute(new XMLAttribute("new-tl-id", tlNewId));
            result.addAttribute(new XMLAttribute("new-pos", posNew));
            result.addAttribute(new XMLAttribute("value", value));

            return result;
        }

        /**
         * DOCUMENT ME!
         *
         * @param saver DOCUMENT ME!
         *
         * @throws XMLTreeException DOCUMENT ME!
         * @throws IOException DOCUMENT ME!
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public void saveChilds(XMLSaver saver)
            throws XMLTreeException, IOException, XMLCannotSaveException { // A count entry has no child objects
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return parentName + ".count";
        }

        /**
         * DOCUMENT ME!
         *
         * @param parentName DOCUMENT ME!
         */
        public void setParentName(String parentName) {
            this.parentName = parentName;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class PEntry implements XMLSerializable {
        // PEntry vars
        /** DOCUMENT ME! */
        int pos;

        // PEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int posNew;

        // PEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int tlId;

        // PEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int tlNewId;

        // PEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int desId;

        /** DOCUMENT ME! */
        double sameStartSituation;

        /** DOCUMENT ME! */
        double sameSituation;

        /** DOCUMENT ME! */
        boolean light;

        // XML vars
        /** DOCUMENT ME! */
        String parentName = "model.tlc";

/**
         * Creates a new PEntry object.
         *
         * @param _tlId    DOCUMENT ME!
         * @param _pos     DOCUMENT ME!
         * @param _desId   DOCUMENT ME!
         * @param _light   DOCUMENT ME!
         * @param _tlNewId DOCUMENT ME!
         * @param _posNew  DOCUMENT ME!
         */
        PEntry(int _tlId, int _pos, int _desId, boolean _light, int _tlNewId,
            int _posNew) {
            tlId = _tlId; // The Sign the RU was at
            pos = _pos; // The position the RU was at
            desId = _desId; // The SpecialNode the RU is travelling to
            light = _light; // The colour of the Sign the RU is at now
            tlNewId = _tlNewId; // The Sign the RU is at now
            posNew = _posNew; // The position the RU is on now
            sameStartSituation = 0; // How often this situation has occurred
            sameSituation = 0;
        }

/**
         * Creates a new PEntry object.
         */
        public PEntry() { // Empty constructor for loading
        }

        /**
         * DOCUMENT ME!
         */
        public void addSameStartSituation() {
            sameStartSituation++;
        }

        /**
         * DOCUMENT ME!
         *
         * @param s DOCUMENT ME!
         */
        public void setSameStartSituation(long s) {
            sameStartSituation = s;
        }

        /**
         * DOCUMENT ME!
         *
         * @param s DOCUMENT ME!
         */
        public void setSameSituation(long s) {
            sameSituation = s;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getSameStartSituation() {
            return sameStartSituation;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getSameSituation() {
            return sameSituation;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getChance() {
            return getSameSituation() / getSameStartSituation();
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object other) {
            if ((other != null) && other instanceof PEntry) {
                PEntry pnew = (PEntry) other;

                if (pnew.tlId != tlId) {
                    return false;
                }

                if (pnew.pos != pos) {
                    return false;
                }

                if (pnew.desId != desId) {
                    return false;
                }

                if (pnew.light != light) {
                    return false;
                }

                if (pnew.tlNewId != tlNewId) {
                    return false;
                }

                if (pnew.posNew != posNew) {
                    return false;
                }

                return true;
            }

            return false;
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean sameSituation(CountEntry other) {
            return equals(other);
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean sameStartSituation(CountEntry other) {
            if ((other.tlId == tlId) && (other.pos == pos) &&
                    (other.desId == desId) && (other.light == light)) {
                return true;
            } else {
                return false;
            }
        }

        // XMLSerializable implementation of PEntry
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
            pos = myElement.getAttribute("pos").getIntValue();
            tlId = myElement.getAttribute("tl-id").getIntValue();
            desId = myElement.getAttribute("des-id").getIntValue();
            light = myElement.getAttribute("light").getBoolValue();
            tlNewId = myElement.getAttribute("new-tl-id").getIntValue();
            posNew = myElement.getAttribute("new-pos").getIntValue();
            sameStartSituation = myElement.getAttribute("same-startsituation")
                                          .getLongValue();
            sameSituation = myElement.getAttribute("same-situation")
                                     .getLongValue();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("pval");
            result.addAttribute(new XMLAttribute("pos", pos));
            result.addAttribute(new XMLAttribute("tl-id", tlId));
            result.addAttribute(new XMLAttribute("des-id", desId));
            result.addAttribute(new XMLAttribute("light", light));
            result.addAttribute(new XMLAttribute("new-tl-id", tlNewId));
            result.addAttribute(new XMLAttribute("new-pos", posNew));
            result.addAttribute(new XMLAttribute("same-startsituation",
                    sameStartSituation));
            result.addAttribute(new XMLAttribute("same-situation", sameSituation));

            return result;
        }

        /**
         * DOCUMENT ME!
         *
         * @param saver DOCUMENT ME!
         *
         * @throws XMLTreeException DOCUMENT ME!
         * @throws IOException DOCUMENT ME!
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public void saveChilds(XMLSaver saver)
            throws XMLTreeException, IOException, XMLCannotSaveException { // A PEntry has no child objects
        }

        /**
         * DOCUMENT ME!
         *
         * @param parentName DOCUMENT ME!
         */
        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return parentName + ".pval";
        }
    }
}
