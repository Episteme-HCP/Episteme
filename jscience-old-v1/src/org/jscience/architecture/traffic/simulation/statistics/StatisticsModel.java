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

package org.jscience.architecture.traffic.simulation.statistics;

import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.infrastructure.Node.NodeStatistics;
import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;
import org.jscience.architecture.traffic.simulation.SimModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;

/**
 * Class to maintain statistics as shown in extensions of StatisticsView.
 *
 * @author Group GUI
 * @version 1.0
 */

public class StatisticsModel extends Observable {
    /**
     * Separator used when saving data.
     */
    public static String SEP = "\t";
    /**
     * The length of the delay tables maintained in each Node.
     */
    protected final static int statNumData = Node.STAT_NUM_DATA;
    /**
     * Determines whether to use all time averages or 'last <statNumData> averages'
     */
    protected boolean allTimeAvg;
    /**
     * The SimModel to get the statistical data from.
     */
    protected SimModel model;

    // All variables used to maintain the statistics.
    protected String infraName, infraAuthor, simName;
    protected int numNodes, numSpecial, numJunctions, cycle;
    protected NodeStatistics[][] nodeStats;
    protected float allTimeTripWT, lastXTripWT, allTimeJunctionWT, lastXJunctionWT;
    protected int roadusersArrived, junctionCrossings, lastXTripCount, lastXJunctionCount;


    /**
     * Creates a <code>StatisticsModel</code>.
     *
     * @param _model The <code>SimModel</code> statistics should be read from.
     */
    public StatisticsModel(SimModel _model) {
        model = _model;
        refresh();
    }

    /*============================================*/
    /* GET AND SET                                */
    /*============================================*/

    /**
     * Sets the SimModel to be used.
     */
    public void setSimModel(SimModel _model) {
        model = _model;
        refresh();
    }

    /**
     * Returns the SimModel to be shown.
     */
    public SimModel getSimModel() {
        return model;
    }

    /**
     * Sets whether all time averages should be used or not.
     */
    public void setAllTimeAvg(boolean b) {
        allTimeAvg = b;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns whether all time averages should be used or not.
     */
    public boolean getAllTimeAvg() {
        return allTimeAvg;
    }

    /// GET ///
    public String getInfraName() {
        return infraName;
    }

    public String getInfraAuthor() {
        return infraAuthor;
    }

    public String getSimName() {
        return simName;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public int getNumSpecialNodes() {
        return numSpecial;
    }

    public int getNumJunctions() {
        return numJunctions;
    }

    public int getCycle() {
        return cycle;
    }

    public NodeStatistics[][] getNodeStatistics() {
        return nodeStats;
    }

    public float getAllTimeTripWT() {
        return allTimeTripWT;
    }

    public float getLastXTripWT() {
        return lastXTripWT;
    }

    public float getAllTimeJunctionWT() {
        return allTimeJunctionWT;
    }

    public float getLastXJunctionWT() {
        return lastXJunctionWT;
    }

    public int getRoadusersArrived() {
        return roadusersArrived;
    }

    public int getJunctionCrossings() {
        return junctionCrossings;
    }

    public int getLastXTripCount() {
        return lastXTripCount;
    }

    public int getLastXJunctionCount() {
        return lastXJunctionCount;
    }

    /*============================================*/
    /* REFRESHING                                 */
    /*============================================*/

    /**
     * Refresh the statistical data from the model and repaint.
     */
    public void refresh() {
        Infrastructure infra = model.getInfrastructure();
        infraName = infra.getTitle();
        infraAuthor = infra.getAuthor();
        simName = model.getSimName();
        cycle = model.getCurCycle();

        numNodes = infra.getNumNodes();
        numSpecial = infra.getNumSpecialNodes();
        numJunctions = infra.getNumJunctions();
        NodeStatistics[][] ns = infra.getNodeStatistics();
        nodeStats = new NodeStatistics[ns.length][];
        for (int i = 0; i < ns.length; i++) {
            nodeStats[i] = new NodeStatistics[ns[i].length];
            for (int j = 0; j < ns[i].length; j++)
                nodeStats[i][j] = (NodeStatistics) ns[i][j].getClone();
        }

        roadusersArrived = junctionCrossings = lastXTripCount = lastXJunctionCount = 0;
        allTimeTripWT = lastXTripWT = allTimeJunctionWT = lastXJunctionWT = 0;

        for (int i = 0; i < numSpecial; i++) {
            int ru = nodeStats[i][0].getTotalRoadusers();
            roadusersArrived += ru;
            allTimeTripWT += nodeStats[i][0].getAvgWaitingTime(true) * ru;
            int tmp = Math.min(ru, statNumData);
            lastXTripWT += nodeStats[i][0].getAvgWaitingTime(false) * tmp;
            lastXTripCount += tmp;
        }
        allTimeTripWT = roadusersArrived > 0 ? allTimeTripWT / roadusersArrived : 0;
        lastXTripWT = lastXTripCount > 0 ? lastXTripWT / lastXTripCount : 0;

        for (int i = numSpecial; i < numNodes; i++) {
            int ru = nodeStats[i][0].getTotalRoadusers();
            junctionCrossings += ru;
            allTimeJunctionWT += nodeStats[i][0].getAvgWaitingTime(true) * ru;
            int tmp = Math.min(ru, statNumData);
            lastXJunctionWT += nodeStats[i][0].getAvgWaitingTime(false) * tmp;
            lastXJunctionCount += tmp;
        }
        allTimeJunctionWT = junctionCrossings > 0 ? allTimeJunctionWT / junctionCrossings : 0;
        lastXJunctionWT = lastXJunctionCount > 0 ? lastXJunctionWT / lastXJunctionCount : 0;

        setChanged();
        notifyObservers();
    }

    /*============================================*/
    /* SAVING                                     */
    /*============================================*/

    /**
     * Save data to a CSV file.
     */
    protected void saveData(String filename) throws IOException {
        int[] ruTypes = new int[nodeStats[0].length];
        for (int i = 0; i < ruTypes.length; i++)
            ruTypes[i] = RoaduserFactory.statIndexToRuType(i);

        PrintWriter out = new PrintWriter(new FileWriter(new File(filename)));
        out.println("# Data exported by Green Light District");
        out.println("#");
        Infrastructure infra = model.getInfrastructure();
        out.println("# Infrastructure: \"" + infra.getTitle() + "\" by " + infra.getAuthor());
        out.println("# Simulation: \"" + model.getSimName() + "\"");
        out.println("#");
        out.println("# Data at cycle: " + cycle);
        out.println("# #nodes = " + numNodes + ", #specialnodes = " + numSpecial + ", #junctions = " + numJunctions);
        out.println("#");
        out.println("# Total number of roadusers that has arrived at its destination: " + roadusersArrived);
        out.println("# Total number of junction crossings: " + junctionCrossings);
        out.println("# Average trip waiting time (based on all roadusers arrived): " + allTimeTripWT);
        if (roadusersArrived != lastXTripCount)
            out.println("# Average trip waiting time (based on last " + lastXTripCount + " roadusers arrived): " + lastXTripWT);
        out.println("# Average junction waiting time (based on all junction crossings): " + allTimeJunctionWT);
        if (junctionCrossings != lastXJunctionCount)
            out.println("# Average junction waiting time (based on last " + lastXJunctionCount + " junction crossings): " + lastXJunctionWT);
        out.println("#");
        out.println("#");
        out.println("# EdgeNodes");
        out.println("# Data format: " + "<id" + SEP + "ruType" + SEP + "roadusersArrived" + SEP + "avgTripWaitingTimeAllTime" + SEP + "avgTripWaitingTimeLast" + Node.STAT_NUM_DATA + ">");
        out.println("#");
        for (int id = 0; id < numNodes; id++) {
            if (id == numSpecial) {
                out.println("#");
                out.println("# Junctions");
                out.println("#");
            }
            for (int statIndex = 0; statIndex < nodeStats[id].length; statIndex++) {
                NodeStatistics ns = nodeStats[id][statIndex];
                out.println(id + SEP + ruTypes[statIndex] + SEP + ns.getTotalRoadusers() + SEP +
                        ns.getAvgWaitingTime(true) + SEP + ns.getAvgWaitingTime(false));
            }
        }
        out.close();
    }
}
