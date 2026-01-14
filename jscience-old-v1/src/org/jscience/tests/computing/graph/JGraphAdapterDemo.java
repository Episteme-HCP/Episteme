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

/* ----------------------
 * JGraphAdapterDemo.java
 * ----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: JGraphAdapterDemo.java,v 1.3 2007-10-23 18:23:34 virtualcall Exp $
 *
 * Changes
 * -------
 * 03-Aug-2003 : Initial revision (BN);
 * 07-Nov-2003 : Adaptation to JGraph 3.0 (BN);
 *
 */
package org.jscience.tests.computing.graph.demo;

import org.jgraph.JGraph;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import org.jscience.computing.graph.DirectedGraph;
import org.jscience.computing.graph.ListenableGraph;
import org.jscience.computing.graph.external.JGraphModelAdapter;
import org.jscience.computing.graph.graphs.DefaultListenableGraph;
import org.jscience.computing.graph.graphs.DirectedMultigraph;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;


/**
 * A demo applet that shows how to use JGraph to visualize JGraphT graphs.
 *
 * @author Barak Naveh
 *
 * @since Aug 3, 2003
 */
public class JGraphAdapterDemo extends JApplet {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3256444702936019250L;

    /** DOCUMENT ME! */
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");

    /** DOCUMENT ME! */
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    //
    /** DOCUMENT ME! */
    private JGraphModelAdapter m_jgAdapter;

    /**
     * An alternative starting point for this demo, to also allow
     * running this applet as an application.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {
        JGraphAdapterDemo applet = new JGraphAdapterDemo();
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("JGraphT Adapter to JGraph Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        // create a JGraphT graph
        ListenableGraph g = new ListenableDirectedMultigraph();

        // create a visualization using JGraph, via an adapter
        m_jgAdapter = new JGraphModelAdapter(g);

        JGraph jgraph = new JGraph(m_jgAdapter);

        adjustDisplaySettings(jgraph);
        getContentPane().add(jgraph);
        resize(DEFAULT_SIZE);

        Object v1 = "v1";
        Object v2 = "v2";
        Object v3 = "v3";
        Object v4 = "v4";

        // add some sample data (graph manipulated via JGraphT)
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v1);
        g.addEdge(v4, v3);

        // position vertices nicely within JGraph component
        positionVertexAt(v1, 130, 40);
        positionVertexAt(v2, 60, 200);
        positionVertexAt(v3, 310, 230);
        positionVertexAt(v4, 380, 70);

        // that's all there is to it!...
    }

    /**
     * DOCUMENT ME!
     *
     * @param jg DOCUMENT ME!
     */
    private void adjustDisplaySettings(JGraph jg) {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param vertex DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    private void positionVertexAt(Object vertex, int x, int y) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds = new Rectangle2D.Double(x, y, bounds.getWidth(),
                bounds.getHeight());

        GraphConstants.setBounds(attr, newBounds);

        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        m_jgAdapter.edit(cellAttr, null, null, null);
    }

    /**
     * a listenable directed multigraph that allows loops and parallel
     * edges.
     */
    private static class ListenableDirectedMultigraph
        extends DefaultListenableGraph implements DirectedGraph {
        /** DOCUMENT ME! */
        private static final long serialVersionUID = 1L;

/**
         * Creates a new ListenableDirectedMultigraph object.
         */
        ListenableDirectedMultigraph() {
            super(new DirectedMultigraph());
        }
    }
}
