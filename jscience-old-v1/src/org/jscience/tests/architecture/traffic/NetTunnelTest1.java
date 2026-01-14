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

package org.jscience.architecture.traffic.infrastructure;

import org.jscience.architecture.traffic.simulation.SimulationRunningException;

import java.awt.*;


/**
 * Simple infrastructure used for testing NetTunnel
 *
 * @author Group Datastructures
 * @version 1.0
 */
public class NetTunnelTest1 extends Infrastructure {
/**
     * Creates a new NetTunnelTest1 object.
     */
    public NetTunnelTest1() {
        super(new Dimension(1000, 800));

        try {
            EdgeNode edge0 = new EdgeNode(new Point(-100, -100));
            EdgeNode edge1 = new EdgeNode(new Point(100, -100));
            NetTunnel tunnel = new NetTunnel(new Point(0, 0));
            Junction node3 = new Junction(new Point(0, -100));

            edge0.setId(0);
            edge1.setId(1);
            tunnel.setId(2);
            node3.setId(3);

            Road road1 = new Road(node3, edge0, 10);
            Road road2 = new Road(edge1, node3, 10);
            Road road3 = new Road(node3, tunnel, 10);

            Drivelane d11 = new Drivelane(road1);
            d11.setType(1);

            boolean[] d11targets = { false, true, true };
            d11.setTargets(d11targets);

            TrafficLight s11 = new TrafficLight(node3, d11);
            d11.setSign(s11);

            Drivelane d12 = new Drivelane(road1);
            d12.setType(1);

            boolean[] d12targets = { false, true, false };
            d12.setTargets(d12targets);

            TrafficLight s12 = new TrafficLight(edge0, d12);
            d12.setSign(s12);

            Drivelane d21 = new Drivelane(road2);
            d21.setType(1);

            boolean[] d21targets = { false, true, false };
            d21.setTargets(d21targets);

            TrafficLight s21 = new TrafficLight(edge1, d21);
            d21.setSign(s21);

            Drivelane d22 = new Drivelane(road2);
            d22.setType(1);

            boolean[] d22targets = { true, true, false };
            d22.setTargets(d22targets);

            TrafficLight s22 = new TrafficLight(node3, d22);
            d22.setSign(s22);

            Drivelane d41 = new Drivelane(road3);
            d41.setType(1);

            boolean[] d41targets = { false, false, false };
            d41.setTargets(d41targets);

            TrafficLight s41 = new TrafficLight(tunnel, d41);
            d41.setSign(s41);

            Drivelane d44 = new Drivelane(road3);
            d44.setType(1);

            boolean[] d44targets = { true, false, true };
            d44.setTargets(d44targets);

            TrafficLight s44 = new TrafficLight(node3, d44);
            d44.setSign(s44);

            road1.addAlphaLane(d11);
            road1.addBetaLane(d12);
            road2.addAlphaLane(d21);
            road2.addBetaLane(d22);
            road3.addAlphaLane(d44);
            road3.addBetaLane(d41);

            edge0.setRoad(road1);
            edge0.setRoadPos(1);
            edge0.setAlpha(true);
            edge1.setRoad(road2);
            edge1.setRoadPos(3);
            edge1.setAlpha(true);
            tunnel.setRoad(road3);
            tunnel.setRoadPos(0);
            tunnel.setAlpha(true);

            Road[] roads = { null, road2, road3, road1 };
            node3.setAllRoads(roads);

            Sign[] signs = { d11.getSign(), d22.getSign(), d44.getSign() };
            node3.setSigns(signs);

            Node[] nodes = { edge0, edge1, tunnel, node3 };
            SpecialNode[] specialNodesCopy = { edge0, edge1, tunnel };

            try {
                tunnel.setLocalPort(10041);
                tunnel.setRemoteHostname("localhost");
                tunnel.setRemotePort(10040);
            } catch (SimulationRunningException exc) { // We can't get this exception since the exception isn't initialized yet
            }

            allNodes = nodes;
            specialNodes = specialNodesCopy;

            SpawnFrequency sf1a = new SpawnFrequency(1, 0.25f);
            SpawnFrequency sf1b = new SpawnFrequency(2, 0.0f);
            SpawnFrequency sf1c = new SpawnFrequency(4, 0.0f);
            SpawnFrequency[] sfa1 = { sf1a, sf1b, sf1c };
            edge0.setSpawnFrequencies(sfa1);

            SpawnFrequency sf2a = new SpawnFrequency(1, 0.25f);
            SpawnFrequency sf2b = new SpawnFrequency(2, 0.0f);
            SpawnFrequency sf2c = new SpawnFrequency(4, 0.0f);
            SpawnFrequency[] sfa2 = { sf2a, sf2b, sf2c };
            edge1.setSpawnFrequencies(sfa2);

            title = "Simple NetTunnel test 1";
            author = "GLD project team";
        } catch (InfraException e) {
            e.printStackTrace();
        }
    }
}
