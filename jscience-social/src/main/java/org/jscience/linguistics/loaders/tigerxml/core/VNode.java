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

package org.jscience.linguistics.loaders.tigerxml.core;

import org.jscience.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.linguistics.loaders.tigerxml.NT;

import java.io.Serializable;

import java.util.ArrayList;


/**
 * The purpose of the class VNode (= virtual node) is to temporarily store
 * mother (secondary daughter) information concerning daughters (secondary
 * mothers) that have not been created yet. The necessity of creating a VNode
 * arises when the dom parser parses an NT but has not parsed one of the
 * daughters (secondary mothers) belongig to it.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VNode implements Serializable {
    /** The identifier of the virtual node. */
    private String id;

    /** The mother node. */
    private NT mother;

    /** The edge label to the mother. */
    private String edge;

    /** The list of secondary daughters. */
    private ArrayList<GraphNode> secDaughters;
    /** The list of edge labels for secondary daughters. */
    private ArrayList<String> secEdges;

    /** The verbosity level for debugging. */
    private int verbosity = 0;

    /**
     * Creates a new VNode object.
     *
     * @param new_id the identifier for the new node
     */
    public VNode(String new_id) {
        id = new_id;
        mother = null;
        edge = null;
        secDaughters = new ArrayList<>();
        secEdges = new ArrayList<>();
    }

    /**
     * Creates a new VNode object with verbosity.
     *
     * @param new_id the identifier for the new node
     * @param verbosity the level of verbosity
     */
    public VNode(String new_id, int verbosity) {
        id = new_id;
        mother = null;
        edge = null;
        secDaughters = new ArrayList<>();
        secEdges = new ArrayList<>();
        this.verbosity = verbosity;
    }

    /**
     * Adds a secondary daughter and its edge label.
     *
     * @param secDaughter the secondary daughter node
     * @param secEdge the edge label
     */
    public void addSecDaughter(GraphNode secDaughter, String secEdge) {
        secDaughters.add(secDaughter);
        secEdges.add(secEdge);
    }

    /**
     * Sets the mother node.
     *
     * @param newMother the mother node
     */
    public void setMother(NT newMother) {
        mother = newMother;
    }

    /**
     * Gets the identifier of this virtual node.
     *
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns true if this virtual node has an assigned mother.
     *
     * @return true if mother is set
     */
    private boolean hasMother() {
        return (mother != null);
    }

    /**
     * Sets the edge label to the mother node.
     *
     * @param newEdge the edge label
     */
    public void setEdge2Mother(String newEdge) {
        edge = newEdge;
    }

    /**
     * Transfers the stored information to the newly created graph node.
     *
     * @param node the graph node to associate with this virtual information
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
                GraphNode next_s_daughter = secDaughters.get(i);
                String next_s_edge = secEdges.get(i);
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
