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

/* -----------------
 * HelloJGraphT.java
 * -----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: HelloJGraphT.java,v 1.3 2007-10-23 18:23:33 virtualcall Exp $
 *
 * Changes
 * -------
 * 27-Jul-2003 : Initial revision (BN);
 *
 */
package org.jscience.tests.computing.graph.demo;

import org.jscience.computing.graph.DirectedGraph;
import org.jscience.computing.graph.UndirectedGraph;
import org.jscience.computing.graph.graphs.DefaultDirectedGraph;
import org.jscience.computing.graph.graphs.SimpleGraph;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple introduction to using JGraphT.
 *
 * @author Barak Naveh
 *
 * @since Jul 27, 2003
 */
public final class HelloJGraphT {
/**
     * Creates a new HelloJGraphT object.
     */
    private HelloJGraphT() {
    } // ensure non-instantiability.

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {
        UndirectedGraph stringGraph = createStringGraph();

        // note undirected edges are printed as: {<v1>,<v2>}
        System.out.println(stringGraph.toString());

        // create a graph based on URL objects
        DirectedGraph hrefGraph = createHrefGraph();

        // note directed edges are printed as: (<v1>,<v2>)
        System.out.println(hrefGraph.toString());
    }

    /**
     * Creates a toy directed graph based on URL objects that
     * represents link structure.
     *
     * @return a graph based on URL objects.
     */
    private static DirectedGraph createHrefGraph() {
        DirectedGraph g = new DefaultDirectedGraph();

        try {
            URL amazon = new URL("http://www.amazon.com");
            URL yahoo = new URL("http://www.yahoo.com");
            URL ebay = new URL("http://www.ebay.com");

            // add the vertices
            g.addVertex(amazon);
            g.addVertex(yahoo);
            g.addVertex(ebay);

            // add edges to create linking structure
            g.addEdge(yahoo, amazon);
            g.addEdge(yahoo, ebay);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return g;
    }

    /**
     * Craete a toy graph based on String objects.
     *
     * @return a graph based on String objects.
     */
    private static UndirectedGraph createStringGraph() {
        UndirectedGraph g = new SimpleGraph();

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v4);
        g.addEdge(v4, v1);

        return g;
    }
}
