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

import org.jscience.architecture.traffic.algorithms.tlc.TLController;
import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.Roaduser;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;
import java.util.Random;

/**
 * This extension of {@see gld.DrivingPolicy} selects the next lane
 * by finding one which on the shortest path to road user's destination.
 *
 * @author Group Algorithms
 * @version 1.0
 */
public class ShortestPathDP extends DrivingPolicy {
    public static final String shortXMLName = "dp-sp";

    /**
     * The constructor for a shortest driving policy.
     *
     * @param m The model which is used
     */

    public ShortestPathDP(SimModel sim, TLController _tlc) {
        super(sim, _tlc);
    }

    /**
     * The lane to which a car continues his trip.
     *
     * @param r           The road user being asked.
     * @param allOutgoing All the possible outgoing lanes
     * @param shortest    All the lanes which are in a shortest path to the car's destination
     * @return The chosen lane.
     */
    public Drivelane getDirectionLane(Roaduser r, Drivelane lane_now, Drivelane[] allOutgoing, Drivelane[] shortest) {
        //Create a subset from the 2 sets allOutgoing and shortest
        Drivelane[] subset;
        int index = 0;
        int num_outgoing = allOutgoing.length;
        int num_shortest = shortest.length;

        if (num_shortest < num_outgoing)
            subset = new Drivelane[num_shortest];
        else
            subset = new Drivelane[num_outgoing];

        for (int i = 0; i < num_outgoing; i++) {
            for (int j = 0; j < num_shortest; j++) {
                if (allOutgoing[i].getId() == shortest[j].getId()) {
                    subset[index] = allOutgoing[i];
                    index++;
                }
            }
        }

        if (index > 0) {
            Random generator = model.getRNGen();
            int i = (int) Math.floor(generator.nextFloat() * index);
            while (i == index)
                i = (int) Math.floor(generator.nextFloat() * index);
            return subset[i];
        } else {
            //System.out.println ("Couldnt't get direction in DP");
            return null;  //Something very funny is going on now
        }
    }

    // Trivial XMLSerializable implementation
    public void load(XMLElement myElement, XMLLoader loader) throws XMLTreeException, IOException, XMLInvalidInputException {
        System.out.println("DP SP loaded");
    }

    public XMLElement saveSelf() throws XMLCannotSaveException {
        return new XMLElement(shortXMLName);
    }

    public String getXMLName() {
        return "model." + shortXMLName;
    }
}
