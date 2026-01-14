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

import java.util.Dictionary;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;


/* This algorithm should optimize waitingtimes considerably. Right now we are aware of some possible issues.
* Gains do rise too high, but only under certain specific very busy situations. If you lower the spawning-rates
* traffic will flow through righlty, and TC3 manages to get full control over the waiting times...
* TC3 does make some rather awkward decisions now and then, when a more likeley decision could be expected.
* Summing up, we have the feeling that there might be something wrong with this implementation
* of this algorithm.
* Having bugfixed this implementation for over many weeks and hours, we've come to a point where we have to say:
* This is TC3, the GLD way, as we see fit at the 29th of June 2001.
*/
/**
 * This algorithm works like TC1 with extra functionality. It outcome is
 * adjusted by reinforcement learning. The Q values are created overseeing the
 * whole environment of each traffic light.
 *
 * @author Group Algorithms
 * @version 0.1
 *
 * @see gld.algo.tlc.TC1TLC
 */
public class TC3TLCWorkInProgress extends TLController implements Colearning,
    InstantiationAssistant {
    /** DOCUMENT ME! */
    protected static final boolean red = false;

    /** DOCUMENT ME! */
    protected static final boolean green = true;

    /** DOCUMENT ME! */
    protected static final int green_index = 0;

    /** DOCUMENT ME! */
    protected static final int red_index = 1;

    /** DOCUMENT ME! */
    public static final String shortXMLName = "tlc-tc3-work-in-progress";

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

    // TC3 vars	
    /** DOCUMENT ME! */
    protected Vector[][][] count; //qa_table respresents the q'_table

    // TC3 vars	
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    protected Vector[][][] pTable; //qa_table respresents the q'_table

    // TC3 vars	
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    protected Vector[][][] pKtlTable; //qa_table respresents the q'_table

    /** DOCUMENT ME! */
    protected float[][][][] qTable; //sign, pos, des, color (red=0, green=1)

    /** DOCUMENT ME! */
    protected float[][][][] qaTable; //sign, pos, des, color (red=0, green=1)

    /** DOCUMENT ME! */
    protected float[][][] vTable;

    /** DOCUMENT ME! */
    protected float[][][] wTable;

    /** DOCUMENT ME! */
    protected float gamma = 0.95f; //Discount Factor; used to decrease the influence of previous V values, that's why: 0 < gamma < 1

    /** DOCUMENT ME! */
    private Random random_number;

/**
     * The constructor for TL controllers
     *
     * @param infra model being used.
     * @throws InfraException DOCUMENT ME!
     */
    public TC3TLCWorkInProgress(Infrastructure infra) throws InfraException {
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
            qaTable = new float[numSigns][][][];
            wTable = new float[numSigns][][];
            count = new Vector[numSigns][][];
            pTable = new Vector[numSigns][][];
            pKtlTable = new Vector[numSigns][][];

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
                    qaTable[id] = new float[num_pos_on_dl][][];
                    wTable[id] = new float[num_pos_on_dl][];
                    count[id] = new Vector[num_pos_on_dl][];
                    pTable[id] = new Vector[num_pos_on_dl][];
                    pKtlTable[id] = new Vector[num_pos_on_dl][];

                    for (int k = 0; k < num_pos_on_dl; k++) {
                        qTable[id][k] = new float[num_specialnodes][];
                        qaTable[id][k] = new float[num_specialnodes][];
                        vTable[id][k] = new float[num_specialnodes];
                        wTable[id][k] = new float[num_specialnodes];
                        count[id][k] = new Vector[num_specialnodes];
                        pTable[id][k] = new Vector[num_specialnodes];
                        pKtlTable[id][k] = new Vector[num_specialnodes];

                        for (int l = 0; l < num_specialnodes; l++) {
                            qTable[id][k][l] = new float[2];
                            qTable[id][k][l][0] = 0.0f;
                            qTable[id][k][l][1] = 0.0f;
                            qaTable[id][k][l] = new float[2];
                            qaTable[id][k][l][0] = 0.0f;
                            qaTable[id][k][l][1] = 0.0f;
                            vTable[id][k][l] = 0.0f;
                            wTable[id][k][l] = 0.0f;
                            count[id][k][l] = new Vector();
                            pTable[id][k][l] = new Vector();
                            pKtlTable[id][k][l] = new Vector();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        System.out.println("TC3TLCOpt2 datastructure created");
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

        for (int i = 0; i < num_nodes; i++) {
            num_dec = tld[i].length;

            for (int j = 0; j < num_dec; j++) {
                tl = tld[i][j].getTL();
                tlId = tl.getId();
                lane = tld[i][j].getTL().getLane();

                waitingsize = lane.getNumRoadusersWaiting();
                queue = lane.getQueue().listIterator();
                gain = 0;

                for (; waitingsize > 0; waitingsize--) {
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

                if ((gain > 50.0) || (gain < -50.0f)) {
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
        //When a roaduser leaves the city; this will 
        if ((dlanenow == null) || (signnow == null)) {
            return;
        }

        //This ordening is important for the execution of the algorithm!
        int Ktl = dlanenow.getNumRoadusersWaiting();

        if ((prevsign.getType() == Sign.TRAFFICLIGHT) &&
                ((signnow.getType() == Sign.TRAFFICLIGHT) ||
                (signnow.getType() == Sign.NO_SIGN))) {
            int tlId = prevsign.getId();
            int desId = ru.getDestNode().getId();
            recalcP(tlId, prevpos, desId, prevsign.mayDrive(), signnow.getId(),
                posnow, Ktl); // OK
            recalcW(tlId, prevpos, desId); // OK
            recalcV(tlId, prevpos, desId, prevsign.mayDrive(), Ktl, posMovs); // OK
            recalcQa(tlId, prevpos, desId, prevsign.mayDrive(),
                signnow.getId(), posnow, posMovs);
            recalcQ(tlId, prevpos, desId, prevsign.mayDrive(), signnow.getId(),
                posnow, posMovs, Ktl);
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
     * @param Ktl DOCUMENT ME!
     */
    protected void recalcP(int tlId, int pos, int desId, boolean light,
        int tlNewId, int posNew, int Ktl) { // Meneer Kaktus is tevree!
                                            // Is OK.
                                            // - First create a CountEntry, find if it exists, and if not add it.

        CountEntry thisSituation = new CountEntry(tlId, pos, desId, light,
                tlNewId, posNew, Ktl);
        int c_index = count[tlId][pos][desId].indexOf(thisSituation);

        if (c_index >= 0) {
            thisSituation = (CountEntry) count[tlId][pos][desId].elementAt(c_index);
            thisSituation.incrementValue();
        } else {
            count[tlId][pos][desId].addElement(thisSituation);
        }

        // We now know how often this exact situation has occurred
        // - Calculate the chance
        long sameSituationKtl = 0;
        long sameStartSituationKtl = 0;

        CountEntry curC;
        int num_c = count[tlId][pos][desId].size();

        for (int i = 0; i < num_c; i++) {
            curC = (CountEntry) count[tlId][pos][desId].elementAt(i);
            sameSituationKtl += curC.sameSituationWithKtl(thisSituation);
            sameStartSituationKtl += curC.sameStartSituationWithKtl(thisSituation);
        }

        // - Update this chance in the PKtlTable
        PKtlEntry thisChanceKtl = new PKtlEntry(tlId, pos, desId, light,
                tlNewId, posNew, Ktl);
        int pKtl_index = pKtlTable[tlId][pos][desId].indexOf(thisChanceKtl);

        if (pKtl_index >= 0) {
            thisChanceKtl = (PKtlEntry) pKtlTable[tlId][pos][desId].elementAt(pKtl_index);
        } else {
            pKtlTable[tlId][pos][desId].addElement(thisChanceKtl);
        }

        thisChanceKtl.setSameSituation(sameSituationKtl);
        thisChanceKtl.setSameStartSituation(sameStartSituationKtl);

        // - Update rest of the PKtl Table
        int num_pKtl = pKtlTable[tlId][pos][desId].size();
        PKtlEntry curPKtl;

        for (int i = 0; i < num_pKtl; i++) {
            curPKtl = (PKtlEntry) pKtlTable[tlId][pos][desId].elementAt(i);

            if (curPKtl.sameStartSituationWithKtl(thisSituation) &&
                    (i != pKtl_index)) {
                curPKtl.addSameStartSituation();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tlId DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param desId DOCUMENT ME!
     */
    protected void recalcW(int tlId, int pos, int desId) { // Meneer Kaktus is Tevree!
                                                           // W(tl,p,d) = Sum(L)[P(L|tl,p,d)Q'(tl,p,d,L)]

        float qaR = qaTable[tlId][pos][desId][red_index];
        float qaG = qaTable[tlId][pos][desId][green_index];

        float[] pGR = calcPGR(tlId, pos, desId);

        wTable[tlId][pos][desId] = (float) ((pGR[green_index] * qaG) +
            (pGR[red_index] * qaR));
    }

    /**
     * DOCUMENT ME!
     *
     * @param tlId DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param desId DOCUMENT ME!
     * @param light DOCUMENT ME!
     * @param Ktl DOCUMENT ME!
     * @param posMovs DOCUMENT ME!
     */
    protected void recalcV(int tlId, int pos, int desId, boolean light,
        int Ktl, PosMov[] posMovs) { // Meneer Kaktus is Tevree!
                                     // V(tl,p,d) = W(tl,p,d) + Sum(L)[P(L|tl,p,d) * Sum(tl',p')[P([tl,p,d],K,L,[tl',p'])yV(tl',p',d)]]
                                     // Calc Sum(tl',p') for Red and Green
                                     // Sum(tl',p') [P([tl,p,d],K,L,[tl',p'])yV(tl',p',d)]]

        float sumTLPG = 0;
        float sumTLPR = 0;
        float V = 0;

        PosMov curPosMov;
        int curPMTlId;
        int curPMPos;
        int num_posmovs = posMovs.length;

        for (int t = 0; t < num_posmovs; t++) {
            curPosMov = posMovs[t];
            curPMTlId = curPosMov.tlId;
            curPMPos = curPosMov.pos;

            V = vTable[curPMTlId][curPMPos][desId];

            PKtlEntry P = new PKtlEntry(tlId, pos, desId, green, curPMTlId,
                    curPMPos, Ktl);
            int p_index = pKtlTable[tlId][pos][desId].indexOf(P);

            if (p_index >= 0) {
                P = (PKtlEntry) pKtlTable[tlId][pos][desId].elementAt(p_index);
                sumTLPG += (P.getChance() * gamma * V);
            }

            P = new PKtlEntry(tlId, pos, desId, red, curPMTlId, curPMPos, Ktl);
            p_index = pKtlTable[tlId][pos][desId].indexOf(P);

            if (p_index >= 0) {
                P = (PKtlEntry) pKtlTable[tlId][pos][desId].elementAt(p_index);
                sumTLPR += (P.getChance() * gamma * V);
            }
        }

        // Calc P(tl,p,d,G) and P(tl,p,d,R)
        float[] pGR = calcPGR(tlId, pos, desId);
        float pG = pGR[green_index];
        float pR = pGR[red_index];

        // V = W + pG*sumTLPG + pR*sumTLPR
        float W = wTable[tlId][pos][desId];
        V = W + (pG * sumTLPG) + (pR * sumTLPR);
        vTable[tlId][pos][desId] = V;
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
    protected void recalcQa(int tlId, int pos, int desId, boolean light,
        int tlNewId, int posNew, PosMov[] posMovs) { // Meneer Kaktus is Tevree!
                                                     // Q'([tl,p,d],L) = Sum(p') [P([tl,p,d],L,*,[tl,p']) * (R([tl,p],[tl,p'])+yW([tl,p',d]))]
                                                     // p' = alle mogelijke posities voor me op deze lane

        float Qa = 0;
        float W = 0;
        float P = 0;
        float R = 0;

        /*                int num_posmovs = posMovs.length, pa;
                        for(int t=0; t<num_posmovs;t++) {
                                if (posMovs[t].tlId == tlId) {
                                        pa=posMovs[t].pos;
                                        long noem = 0, deel = 0;
                                        CountEntry C = new CountEntry(tlId,pos,desId,light,tlId,pa,-1);
                                        int numC = count[tlId][pos][desId].size()-1;
                                        for(;numC>=0;numC--) {
                                                CountEntry curC = (CountEntry) count[tlId][pos][desId].elementAt(numC);
                                                noem += curC.sameSituationDiffKtl(C);
                                                deel += curC.sameStartSituationDiffKtl(C);
                                        }
                                        PKtlEntry PKtl = new PKtlEntry(0,0,0,false,0,0,0);
                                        PKtl.setSameSituation(noem);
                                        PKtl.setSameStartSituation(deel);
                                        P   = PKtl.getChance();
                                        R   = calcReward(tlId,pos,tlId,pa);
                                        W   = wTable[tlId][pa][desId];
                                        Qa += P * (R+ (gamma * W));
                                }
                        }
        */
        if (tlNewId == tlId) {
            long noem = 0;
            long deel = 0;
            CountEntry C = new CountEntry(tlId, pos, desId, light, tlId,
                    posNew, -1);
            int numC = count[tlId][pos][desId].size() - 1;

            for (; numC >= 0; numC--) {
                CountEntry curC = (CountEntry) count[tlId][pos][desId].elementAt(numC);
                noem += curC.sameSituationDiffKtl(C);
                deel += curC.sameStartSituationDiffKtl(C);
            }

            PKtlEntry PKtl = new PKtlEntry(0, 0, 0, false, 0, 0, 0);
            PKtl.setSameSituation(noem);
            PKtl.setSameStartSituation(deel);
            P = PKtl.getChance();
            R = calcReward(tlId, pos, tlId, posNew);
            W = wTable[tlId][posNew][desId];
            Qa += (P * (R + (gamma * W)));
        }

        qaTable[tlId][pos][desId][light ? green_index : red_index] = Qa;
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
     * @param Ktl DOCUMENT ME!
     */
    protected void recalcQ(int tlId, int pos, int desId, boolean light,
        int tlNewId, int posNew, PosMov[] posMovs, int Ktl) { // Meneer Kaktus is Tevree!
                                                              // Q([tl,p,d],L) = Q'([tl,p,d],L) + Sum(tl',p') [P([tl,p,d],K,L,[tl',p']) * gamma * V([tl',p',d])]]

        float Qa = qaTable[tlId][pos][desId][light ? green_index : red_index];
        float V = 0;
        float sumTLP = 0;

        PosMov curPosMov;
        int curPMTlId;
        int curPMPos;
        int num_posmovs = posMovs.length;

        for (int t = 0; t < num_posmovs; t++) {
            curPosMov = posMovs[t];
            curPMTlId = curPosMov.tlId;
            curPMPos = curPosMov.pos;

            PKtlEntry P = new PKtlEntry(tlId, pos, desId, light, curPMTlId,
                    curPMPos, Ktl);
            int p_index = pKtlTable[tlId][pos][desId].indexOf(P);

            if (p_index >= 0) {
                P = (PKtlEntry) pKtlTable[tlId][pos][desId].elementAt(p_index);
                V = vTable[curPMTlId][curPMPos][desId];
                sumTLP += (P.getChance() * gamma * V);
            }
        }

        qTable[tlId][pos][desId][light ? green_index : red_index] = Qa +
            sumTLP;
    }

    /*
                            ==========================================================================
                                                    Additional methods, used by the recalc methods
                            ==========================================================================
    */
    protected float[] calcPGR(int tlId, int pos, int desId) {
        float[] counters = new float[2];

        int pKtlsize = pKtlTable[tlId][pos][desId].size() - 1;
        long pKtlGC2 = 0;
        long pKtlRC2 = 0;

        for (; pKtlsize >= 0; pKtlsize--) {
            PKtlEntry cur = (PKtlEntry) pKtlTable[tlId][pos][desId].elementAt(pKtlsize);

            if (cur.light == green) {
                pKtlGC2 += cur.getSameSituation();
            } else {
                pKtlRC2 += cur.getSameSituation();
            }
        }

        counters[green_index] = (float) (((double) pKtlGC2) / ((double) (pKtlGC2 +
            pKtlRC2)));
        counters[red_index] = (float) (((double) pKtlRC2) / ((double) (pKtlGC2 +
            pKtlRC2)));

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
    protected int calcReward(int tlId, int pos, int tlNewId, int posNew) {
        if ((pos == posNew) && (tlId == tlNewId)) {
            return 1;
        } else {
            return 0;
        }
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
     * @param sign_new DOCUMENT ME!
     * @param sign DOCUMENT ME!
     * @param destination DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getColearnValue(Sign sign_new, Sign sign, Node destination,
        int pos) {
        int Ktl = sign.getLane().getNumRoadusersWaiting();
        int tlId = sign.getId();
        int desId = destination.getId();

        // Calculate the colearning value
        float newCovalue = 0;
        int size = sign.getLane().getCompleteLength() - 1;

        for (; size >= 0; size--) {
            float V;
            PKtlEntry P = new PKtlEntry(sign.getId(), 0, destination.getId(),
                    green, sign_new.getId(), size, Ktl);
            int p_index = pKtlTable[tlId][pos][desId].indexOf(P);

            if (p_index >= 0) {
                try {
                    P = (PKtlEntry) pKtlTable[tlId][pos][desId].elementAt(p_index);
                    V = vTable[tlId][size][desId];
                    newCovalue += (P.getChance() * V);
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        }

        return newCovalue;
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
        super.load(myElement, loader);
        gamma = myElement.getAttribute("gamma").getFloatValue();
        random_chance = myElement.getAttribute("random-chance").getFloatValue();
        qTable = (float[][][][]) XMLArray.loadArray(this, loader);
        qaTable = (float[][][][]) XMLArray.loadArray(this, loader);
        vTable = (float[][][]) XMLArray.loadArray(this, loader);
        wTable = (float[][][]) XMLArray.loadArray(this, loader);
        count = (Vector[][][]) XMLArray.loadArray(this, loader, this);
        pTable = (Vector[][][]) XMLArray.loadArray(this, loader, this);
        pKtlTable = (Vector[][][]) XMLArray.loadArray(this, loader, this);
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
        XMLArray.saveArray(qaTable, this, saver, "qa-table");
        XMLArray.saveArray(vTable, this, saver, "v-table");
        XMLArray.saveArray(wTable, this, saver, "va-table");
        XMLArray.saveArray(count, this, saver, "count");
        XMLArray.saveArray(pTable, this, saver, "p-table");
        XMLArray.saveArray(pKtlTable, this, saver, "pKtlTable");
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
     * @param dictionaries DOCUMENT ME!
     *
     * @throws XMLInvalidInputException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void loadSecondStage(Dictionary dictionaries)
        throws XMLInvalidInputException, XMLTreeException {
        super.loadSecondStage(dictionaries);

        for (int i = 0; i < count.length; i++)
            for (int j = 0; j < count[i].length; j++)
                for (int k = 0; k < count[i][j].length; k++)
                    XMLUtils.loadSecondStage(count[i][j][k].elements(),
                        dictionaries);

        for (int i = 0; i < pTable.length; i++)
            for (int j = 0; j < pTable[i].length; j++)
                for (int k = 0; k < pTable[i][j].length; k++)
                    XMLUtils.loadSecondStage(pTable[i][j][k].elements(),
                        dictionaries);

        for (int i = 0; i < pKtlTable.length; i++)
            for (int j = 0; j < pKtlTable[i].length; j++)
                for (int k = 0; k < pKtlTable[i][j].length; k++)
                    XMLUtils.loadSecondStage(pKtlTable[i][j][k].elements(),
                        dictionaries);

        System.out.println("TC3 second stage load finished.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canCreateInstance(Class request) {
        return CountEntry.class.equals(request) ||
        PKtlEntry.class.equals(request);
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
        if (CountEntry.class.equals(request)) {
            return new CountEntry();
        } else if (PKtlEntry.class.equals(request)) {
            return new PKtlEntry();
        } else {
            throw new ClassNotFoundException(
                "TC3 IntstantiationAssistant cannot make instances of " +
                request);
        }
    }

    // Config dingetje
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

        // CountEntry vars
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        /** DOCUMENT ME! */
        int Ktl;

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
         * @param _Ktl     DOCUMENT ME!
         */
        CountEntry(int _tlId, int _pos, int _desId, boolean _light,
            int _tlNewId, int _posNew, int _Ktl) {
            tlId = _tlId; // The Sign the RU was at
            pos = _pos; // The position the RU was at
            desId = _desId; // The SpecialNode the RU is travelling to
            light = _light; // The colour of the Sign the RU is at now
            tlNewId = _tlNewId; // The Sign the RU is at now
            posNew = _posNew; // The position the RU is on now
            Ktl = _Ktl;
            value = 1; // How often this situation has occurred
        }

/**
         * Creates a new CountEntry object.
         */
        CountEntry() { // Empty constructor for loading
        }

        /**
         * DOCUMENT ME!
         */
        public void incrementValue() {
            value++;
        }

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

                if (countnew.Ktl != Ktl) {
                    return false;
                }

                return true;
            }

            return false;
        }

        // Retuns the count-value if the startingsituations match
        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public long sameStartSituationDiffKtl(CountEntry other) {
            if ((other.tlId == tlId) && (other.pos == pos) &&
                    (other.desId == desId) && (other.light == light)) {
                return value;
            } else {
                return 0;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public long sameStartSituationWithKtl(CountEntry other) {
            if ((other.tlId == tlId) && (other.pos == pos) &&
                    (other.desId == desId) && (other.light == light) &&
                    (other.Ktl == Ktl)) {
                return value;
            } else {
                return 0;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public long sameSituationDiffKtl(CountEntry other) {
            if ((other.tlId == tlId) && (other.pos == pos) &&
                    (other.light == light) && (other.desId == desId) &&
                    (other.tlNewId == tlNewId) && (other.posNew == posNew)) {
                return value;
            } else {
                return 0;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public long sameSituationWithKtl(CountEntry other) {
            if (equals(other)) {
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
            Ktl = myElement.getAttribute("ktl").getIntValue();
            value = myElement.getAttribute("value").getIntValue();
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
            result.addAttribute(new XMLAttribute("ktl", Ktl));
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
    public class PKtlEntry implements XMLSerializable { // PEntry vars

        /** DOCUMENT ME! */
        int pos;

        /** DOCUMENT ME! */
        int posNew;

        /** DOCUMENT ME! */
        int tlId;

        /** DOCUMENT ME! */
        int tlNewId;

        /** DOCUMENT ME! */
        int desId;

        /** DOCUMENT ME! */
        int Ktl;

        /** DOCUMENT ME! */
        long sameStartSituation;

        /** DOCUMENT ME! */
        long sameSituation;

        /** DOCUMENT ME! */
        boolean light;

        // XML vars
        /** DOCUMENT ME! */
        String parentName = "model.tlc";

/**
         * Creates a new PKtlEntry object.
         *
         * @param _tlId    DOCUMENT ME!
         * @param _pos     DOCUMENT ME!
         * @param _desId   DOCUMENT ME!
         * @param _light   DOCUMENT ME!
         * @param _tlNewId DOCUMENT ME!
         * @param _posNew  DOCUMENT ME!
         * @param _Ktl     DOCUMENT ME!
         */
        PKtlEntry(int _tlId, int _pos, int _desId, boolean _light,
            int _tlNewId, int _posNew, int _Ktl) {
            tlId = _tlId; // The Sign the RU was at
            pos = _pos; // The position the RU was at
            desId = _desId; // The SpecialNode the RU is travelling to
            light = _light; // The colour of the Sign the RU is at now
            tlNewId = _tlNewId; // The Sign the RU is at now
            posNew = _posNew; // The position the RU is on now
            Ktl = _Ktl;
            sameStartSituation = 0; // How often this situation has occurred
            sameSituation = 0;
        }

/**
         * Creates a new PKtlEntry object.
         */
        public PKtlEntry() {
            // Empty constructor for loading
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
        public float getSameStartSituation() {
            return sameStartSituation;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getSameSituation() {
            return sameSituation;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getChance() {
            if (getSameStartSituation() == 0) {
                return 0;
            } else {
                return (float) ((float) getSameSituation()) / ((float) getSameStartSituation());
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean sameStartSituationDiffKtl(CountEntry other) {
            if ((other.tlId == tlId) && (other.pos == pos) &&
                    (other.desId == desId) && (other.light == light)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean sameStartSituationWithKtl(CountEntry other) {
            if ((other.tlId == tlId) && (other.pos == pos) &&
                    (other.desId == desId) && (other.light == light) &&
                    (other.Ktl == Ktl)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean sameSituationDiffKtl(PKtlEntry other) {
            if ((other.tlId == tlId) && (other.pos == pos) &&
                    (other.desId == desId) && (other.light == light) &&
                    (other.tlNewId == tlNewId) && (other.posNew == posNew)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param other DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object other) {
            if ((other != null) && other instanceof PKtlEntry) {
                PKtlEntry pnew = (PKtlEntry) other;

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

                if (pnew.Ktl != Ktl) {
                    return false;
                }

                return true;
            }

            return false;
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
                                          .getIntValue();
            sameSituation = myElement.getAttribute("same-situation")
                                     .getIntValue();
            Ktl = myElement.getAttribute("ktl").getIntValue();
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
            result.addAttribute(new XMLAttribute("ktl", Ktl));

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
            return parentName + ".pktlval";
        }
    }
}
