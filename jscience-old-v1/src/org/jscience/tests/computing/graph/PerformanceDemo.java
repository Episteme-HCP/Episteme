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

/* --------------------
 * PerformanceDemo.java
 * --------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: PerformanceDemo.java,v 1.2 2007-10-21 21:08:50 virtualcall Exp $
 *
 * Changes
 * -------
 * 10-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.tests.computing.graph.demo;

import org.jscience.computing.graph.Graph;
import org.jscience.computing.graph.graphs.Pseudograph;
import org.jscience.computing.graph.iterators.BreadthFirstIterator;
import org.jscience.computing.graph.iterators.DepthFirstIterator;

import java.io.IOException;
import java.util.Iterator;

/**
 * A simple demo to test memory and CPU consumption on a graph with 3 million
 * elements.
 * <p/>
 * <p/>
 * NOTE: To run this demo you may need to increase the JVM max mem size. In
 * Sun's JVM it is done using the "-Xmx" switch. Specify "-Xmx300M" to set it
 * to 300MB.
 * </p>
 * <p/>
 * <p/>
 * WARNING: Don't run this demo as-is on machines with less than 512MB memory.
 * Your machine will start paging severely. You need to first modify it to
 * have fewer graph elements. This is easily done by changing the loop
 * counters below.
 * </p>
 *
 * @author Barak Naveh
 * @since Aug 10, 2003
 */
public final class PerformanceDemo {
    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {
        long time = System.currentTimeMillis();

        reportPerformanceFor("starting at", time);

        Graph g = new Pseudograph();
        Object prev;
        Object curr;

        curr = prev = new Object();
        g.addVertex(prev);

        int numVertices = 10000;
        int numEdgesPerVertex = 200;
        int numElements = numVertices * (1 + numEdgesPerVertex);

        System.out.println("\n" + "allocating graph with " + numElements
                + " elements (may take a few tens of seconds)...");

        for (int i = 0; i < numVertices; i++) {
            curr = new Object();
            g.addVertex(curr);

            for (int j = 0; j < numEdgesPerVertex; j++) {
                g.addEdge(prev, curr);
            }

            prev = curr;
        }

        reportPerformanceFor("graph allocation", time);

        time = System.currentTimeMillis();

        for (Iterator i = new BreadthFirstIterator(g); i.hasNext();) {
            i.next();
        }

        reportPerformanceFor("breadth traversal", time);

        time = System.currentTimeMillis();

        for (Iterator i = new DepthFirstIterator(g); i.hasNext();) {
            i.next();
        }

        reportPerformanceFor("depth traversal", time);

        System.out.println("\n"
                + "Paused: graph is still in memory (to check mem consumption).");
        System.out.print("press any key to free memory and finish...");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done.");
    }

    private static void reportPerformanceFor(String msg, long refTime) {
        double time = (System.currentTimeMillis() - refTime) / 1000.0;
        double mem = usedMemory() / (1024.0 * 1024.0);
        mem = Math.round(mem * 100) / 100.0;
        System.out.println(msg + " (" + time + " sec, " + mem + "MB)");
    }

    private static long usedMemory() {
        Runtime rt = Runtime.getRuntime();

        return rt.totalMemory() - rt.freeMemory();
    }
}