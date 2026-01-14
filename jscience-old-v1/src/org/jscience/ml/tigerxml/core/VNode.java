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

package org.jscience.ml.tigerxml.core;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;

import java.io.Serializable;

import java.util.ArrayList;


/**
 * The purpose of the class VNode (= virtual node) is to temporarily store
 * mother (secondary daughter) information concerning daughters (secondary
 * mothers) that have not been created yet. The necessity of creating a VNode
 * arises when the dom parser parses an NT but has not parsed one of the
 * daughters (secondary mothers) belongig to it.
 *
 * @author <a href="mailto:hajo_keffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84 $Id: VNode.java,v 1.3 2007-10-23 18:21:42 virtualcall Exp $
 */
public class VNode implements Serializable {
    /** DOCUMENT ME! */
    private String id;

    /** DOCUMENT ME! */
    private NT mother;

    /** DOCUMENT ME! */
    private String edge;

    /** DOCUMENT ME! */
    private ArrayList secDaughters;

    /** DOCUMENT ME! */
    private ArrayList secEdges;

    /** DOCUMENT ME! */
    private int verbosity = 0;

/**
     * Creates a new VNode object.
     *
     * @param new_id DOCUMENT ME!
     */
    public VNode(String new_id) {
        id = new_id;
        mother = null;
        edge = null;
        secDaughters = new ArrayList();
        secEdges = new ArrayList();
    }

/**
     * Creates a new VNode object.
     *
     * @param new_id    DOCUMENT ME!
     * @param verbosity DOCUMENT ME!
     */
    public VNode(String new_id, int verbosity) {
        id = new_id;
        mother = null;
        edge = null;
        secDaughters = new ArrayList();
        secEdges = new ArrayList();
        this.verbosity = verbosity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param secDaughter DOCUMENT ME!
     * @param secEdge DOCUMENT ME!
     */
    public void addSecDaughter(GraphNode secDaughter, String secEdge) {
        secDaughters.add(secDaughter);
        secEdges.add(secEdge);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newMother DOCUMENT ME!
     */
    public void setMother(NT newMother) {
        mother = newMother;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean hasMother() {
        return (mother != null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newEdge DOCUMENT ME!
     */
    public void setEdge2Mother(String newEdge) {
        edge = newEdge;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void addInfo(GraphNode node) {
        for (int i = 0; i < secDaughters.size(); i++) {
            if (node.isTerminal()) {
                if (this.verbosity > 0) {
                    System.err.println(
                        "org.jscience.ml.tigerxml.VNode: WARNING: " +
                        "cannot add " + this.getId() + " to " + node.getId() +
                        " as secondary daughter " +
                        "because it is a terminal.");
                }
            } else {
                NT nt_node = (NT) node;
                GraphNode next_s_daughter = (GraphNode) secDaughters.get(i);
                String next_s_edge = (String) secEdges.get(i);
                nt_node.addSecDaughter(next_s_daughter);
                next_s_daughter.setSecMother(nt_node, next_s_edge);
            }
        }

        if (this.hasMother()) {
            node.setMother(this.mother);
            (this.mother).addDaughter(node);
            node.setEdge2Mother(this.edge);
        }
    }

    /**
     * Gets the currently set level of verbosity of this instance. The
     * higher the value the more information is written to stderr.
     *
     * @return The level of verbosity.
     */
    public int getVerbosity() {
        return this.verbosity;
    }

    /**
     * Sets the currently set level of verbosity of this instance. The
     * higher the value the more information is written to stderr.
     *
     * @param verbosity The level of verbosity.
     */
    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }
}
