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

package org.jscience.tests.computing.aidemos;

import org.jscience.computing.ai.ca.CellularAutomata;
import org.jscience.computing.game.PrisonersDilemmaAgent;

import java.awt.*;


/**
 * Implements a spatial version of the iterated prisoner's dilemma. Five
 * different strategies are placed randomly within the world and are pitted
 * against each other. The five strategies and their corresponding colours
 * are:
 * <p/>
 * <ul>
 * <li>
 * all cooperate - red.
 * </li>
 * <li>
 * all defect - yellow.
 * </li>
 * <li>
 * random - green.
 * </li>
 * <li>
 * tit-for-tat - blue.
 * </li>
 * <li>
 * pavlov - orange.
 * </li>
 * </ul>
 *
 * @author James Matthews
 */
public class SpatialIPD extends CellularAutomata {
    /**
     * The number of trail per agent, per neighbour, per iteration.
     */
    protected int trialRuns = 5;

    /**
     * The noise rate.
     */
    protected double noiseRate = 0.0005;

    /**
     * The agents used in the world.
     */
    protected PrisonersDilemmaAgent[][] ipdAgents;

    /**
     * Creates a new instance of SpatialIPD
     */
    public SpatialIPD() {
        this(0, 0);
    }

    /**
     * Creates a new instance with world size information.
     *
     * @param size_x the x-size of the world.
     * @param size_y the y-size of the world.
     */
    public SpatialIPD(int size_x, int size_y) {
        super(size_x, size_y, DOUBLE_BUFFERING);
    }

    /**
     * Set the noise rate to be used within the world. The noise rate causes
     * strategies to mutate every now and again.
     *
     * @param nr the noise rate.
     */
    public void setNoiseRate(double nr) {
        noiseRate = nr;
    }

    /**
     * Retrieve the noise rate.
     *
     * @return the noise rate.
     */
    public double getNoiseRate() {
        return noiseRate;
    }

    /**
     * Set the world size.
     *
     * @param size_x the x-size of the world.
     * @param size_y the y-size of the world.
     */
    public void setWorldSize(int size_x, int size_y) {
        super.setWorldSize(size_x, size_y);

        // Set up the array of ipd agents
        ipdAgents = new PrisonersDilemmaAgent[size_x][size_y];

        for (int i = 0; i < size_x; i++) {
            for (int j = 0; j < size_y; j++) {
                ipdAgents[i][j] = new PrisonersDilemmaAgent();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments passed to iterateCA.
     */
    public static void main(String[] args) {
        SpatialIPD sipd = new SpatialIPD(100, 100);

        sipd.init();
        sipd.setCASize(2);

        String[] defaultArgs = {"500", "50", "sipd", "2"};
        iterateCA(sipd, defaultArgs);
    }

    /**
     * Advance the Spatial IPD one timestep.
     */
    public void doStep() {
        int sx = getSizeX();
        int gx;
        int sy = getSizeY();
        int gy;

        // for each agent in the world
        for (int i = 0; i < sx; i++) {
            for (int j = 0; j < sy; j++) {
                // for each of the 8-neighbours
                for (int x = i - 1; x < (i + 2); x++) {
                    gx = translateGeometry(x, 0);

                    for (int y = j - 1; y < (j + 2); y++) {
                        if ((x == i) && (y == j)) {
                            continue;
                        }

                        gy = translateGeometry(y, 1);

                        // for each trial
                        for (int r = 0; r < trialRuns; r++) {
                            ipdAgents[i][j].run(ipdAgents[gx][gy]);
                        }
                    }
                }
            }
        }

        int topScore = 0;
        int topStrategy = 0;

        // for each agent in the world
        for (int i = 0; i < sx; i++) {
            for (int j = 0; j < sy; j++) {
                topScore = ipdAgents[i][j].getPoints();
                topStrategy = ipdAgents[i][j].getStrategy();

                // for each of the 8-neighbours
                for (int x = i - 1; x < (i + 2); x++) {
                    gx = translateGeometry(x, 0);

                    for (int y = j - 1; y < (j + 2); y++) {
                        if ((x == i) && (y == j)) {
                            continue;
                        }

                        gy = translateGeometry(y, 1);

                        if (ipdAgents[gx][gy].getPoints() > topScore) {
                            topScore = ipdAgents[gx][gy].getPoints();
                            topStrategy = ipdAgents[gx][gy].getStrategy();
                        } /* if (ipdAgents[gx][gy].getPoints() == topScore && Math.random() < 0.33) {
                         topScore = ipdAgents[gx][gy].getPoints();
                         topStrategy = ipdAgents[gx][gy].getStrategy();
                        }*/
                    }
                }

                // Re-assign top strategy (with mutation)
                if (Math.random() < noiseRate) {
                    topStrategy = (int) (Math.random() * PrisonersDilemmaAgent.STRATEGIES);
                }

                ipdAgents[i][j].setStrategy(topStrategy);
                setWorldAt(i, j, topStrategy);
            }
        }

        // for each agent, reset the score
        for (int i = 0; i < sx; i++) {
            for (int j = 0; j < sy; j++) {
                ipdAgents[i][j].reset();
            }
        }

        flipBuffer();
    }

    /**
     * Initialize the world to a random collection of strategies.
     */
    public void init() {
        int strategy = (int) (Math.random() * PrisonersDilemmaAgent.STRATEGIES);

        for (int i = 0; i < getSizeX(); i++) {
            for (int j = 0; j < getSizeY(); j++) {
                strategy = (int) (Math.random() * PrisonersDilemmaAgent.STRATEGIES);
                ipdAgents[i][j].setStrategy(strategy);
                setWorldAt(i, j, strategy);
            }
        }

        flipBuffer();

        setWorldColour(PrisonersDilemmaAgent.ALL_C, Color.RED);
        setWorldColour(PrisonersDilemmaAgent.ALL_D, Color.YELLOW);
        setWorldColour(PrisonersDilemmaAgent.RANDOM, Color.GREEN);
        setWorldColour(PrisonersDilemmaAgent.TFT, Color.BLUE);
        setWorldColour(PrisonersDilemmaAgent.PAVLOV, Color.ORANGE);
    }
}
