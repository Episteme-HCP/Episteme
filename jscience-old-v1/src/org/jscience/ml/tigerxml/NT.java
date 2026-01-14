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

package org.jscience.ml.tigerxml;

import org.jscience.ml.tigerxml.core.NTBuilder;
import org.jscience.ml.tigerxml.tools.GeneralTools;
import org.jscience.ml.tigerxml.tools.SyncMMAX;
import org.w3c.dom.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a non-terminal node a syntax tree.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @author <a href="mailto:vicky@coli.uni-sb.de">Vaclav Nemcik</a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84
 *          $Id: NT.java,v 1.2 2007-10-21 17:40:33 virtualcall Exp $
 * @see GraphNode
 */
public class NT
        extends GraphNode {

    /* Class variables */

    /**
     * The CAT attribute (non-terminal category) of this node
     */
    private String cat;

    /**
     * All terminal nodes subordinated to this node
     */
    private ArrayList terminals;

    /**
     * All children of this node
     */
    private ArrayList daughters;

    /**
     * All secondary children of this node
     */
    private ArrayList secDaughters;

    /**
     * The span (list of terminal node IDs) (sorted)
     */
    private String span;

    /**
     * Creates an empty NT setting all member variables to <code>null<code>
     * or empty.
     */
    public NT() {
        super();
        init();
    }

    public NT(Element ntElement, Sentence sent) {
        super();
        init();
        NTBuilder.buildNT(this, sent, ntElement);
    } // NT()

    public NT(Element ntElement, Sentence sent, int verbosity) {
        super();
        init();
        this.verbosity = verbosity;
        NTBuilder.buildNT(this, sent, ntElement);
    } // NT()

    private void init() {
        this.cat = "";
        this.terminals = null;
        this.span = null;
        this.daughters = new ArrayList();
        this.setTerminal(false);
    }

    /**
     * Helps getTerminals() to expand this instance in order to return an
     * ArrayList of terminal node. Method runs recursively.
     *
     * @param nodes An ArrayList containing possibly only one node.
     * @return The expanded ArrayList containing terminal nodes only.
     */
    private static ArrayList expandNodes(ArrayList nodes) {
        // is any of the nodes is an NT?
        boolean stillToDo = false;
        for (int i = 0; i < nodes.size(); i++) {
            if (((GraphNode) nodes.get(i)).isTerminal() == false) {
                stillToDo = true;
            } // if
        } // for i

        // if not, abort the recursion
        if (stillToDo == false) {
            return nodes;
        }
        // if yes, continue recursively
        else {
            // search through the nodes
            for (int i = 0; i < nodes.size(); i++) {
                // expand the first NT encounterd by replacing it by its daughters
                if (((GraphNode) nodes.get(i)).isTerminal() == false) {
                    NT nt2expand = (NT) nodes.get(i);
                    nodes.remove(i);
                    ArrayList daughters2add = nt2expand.getDaughters();
                    for (int j = daughters2add.size() - 1; j >= 0; j--) {
                        nodes.add(i, daughters2add.get(j));
                    } // for j
                } // if
            } // for i
            return expandNodes(nodes);
        } // else
    } // expandNodes()

    /**
     * Adds a daughter. Can by a <code>NT</code>, a <code>T</code>,
     * or a <code>GraphNode</code>.
     */
    public void addDaughter(GraphNode newDaughter) {
        daughters.add(newDaughter);
    }

    /**
     * Adds a secondary daughter. Can be a <code>NT</code>, a <code>T</code>,
     * or a <code>GraphNode</code>.
     */
    public void addSecDaughter(GraphNode newDaughter) {
        if (secDaughters == null) {
            secDaughters = new ArrayList();
        }
        secDaughters.add(newDaughter);
        /////FOO
        /////System.err.println("DEBUG: YEAHEA: in sentence " + this.getSentence());
    }

    /**
     * Returns true if this node has secondary
     * daughters.
     */
    public boolean hasSecDaughters() {
        if (secDaughters == null) {
            return false;
        } else {
            return (!secDaughters.isEmpty());
        }
    }

    /**
     * Sets the category of this NT
     */
    public void setCat(String newcat) {
        cat = newcat;
    }

    /**
     * Returns the category of this NT
     */
    public String getCat() {
        return cat;
    }

    /**
     * Returns the span (the list of terminal node IDs).<p>
     * The concept "span" is taken from MMAX
     * ({@link <a href=http://www.eml.org/nlp>}http://www.eml.org/nlp</a>).
     * The idea is to have a compact string representation of all terminal IDs
     * underneath this <code>NT</code>.<p>
     * For more methods related to MMAX concepts
     *
     * @see org.jscience.ml.tigerxml.tools.SyncMMAX
     */
    public String getSpan() {
        // if span has not been computed yet for this instance, compute
        if (this.span == null) {
            StringBuffer spanBuffer = new StringBuffer();
            for (int i = 0; i < this.getTerminals().size() - 1; i++) {
                T currentTerminal = (T) this.terminals.get(i);
                spanBuffer.append(currentTerminal.getId() + ",");
            }
            spanBuffer.append(((T) this.terminals.get(this.terminals.size() - 1)).getId());
            this.span = SyncMMAX.condenseSpan(new String(spanBuffer));
        } // if
        return this.span;
    } // getSpan()

    /**
     * Returns an ArrayList of the daughter nodes ordered left
     * to right according to linear precedence. Note that if you
     * want to get
     * information like POS, MORPH, or CAT you have to look up the type of each
     * child like this:
     * <code>
     * GraphNode n = someNode.getChildren().get(1);
     * if (n.isTerminal) {
     * fooPos = n.getPos();
     * }
     * </code>
     */
    public ArrayList getDaughters() {
        return GeneralTools.sortNodes(daughters);
    }

    /**
     * Returns an ArrayList of the secondary daughter nodes ordered left
     * to right according to linear precedence. Note that if you
     * want to get
     * information like POS, MORPH, or CAT you have to look up the type of each
     * child like this:
     * <code>
     * GraphNode n = (GraphNode) someNode.getSDaughters().get(1);<p>
     * if (n.isTerminal()) {
     * String fooPos = ((T)n).getPos();
     * }
     * </code>
     */
    public ArrayList getSecDaughters() {
        if (secDaughters == null) {
            secDaughters = new ArrayList();
        }
        secDaughters = GeneralTools.sortNodes(secDaughters);
        return secDaughters;
    }

    /**
     * Returns an ArrayList of all daughters, primary as well as secondary,
     * ordered left
     * to right according to linear precedence. Note that if you
     * want to get
     * information like POS, MORPH, or CAT you have to look up the type of each
     * child like this:
     * <code>
     * GraphNode n = (GraphNode) someNode.getAllDaughters().get(1);<p>
     * if (n.isTerminal()) {
     * String fooPos = ((T)n).getPos();
     * }
     * </code>
     */
    public ArrayList getAllDaughters() {
        ArrayList return_daughters =
                new ArrayList(daughters);
        return_daughters.addAll(this.getSecDaughters());
        return_daughters =
                GeneralTools.sortNodes(return_daughters);
        return return_daughters;
    }

    /**
     * Returns all terminal daughters recursively. The returned terminals are
     * in the order of the sentence (linear precedence).
     */
    public ArrayList getTerminals() {
        // If not already generated, generate the terminal nodes list
        if (this.terminals == null) {
            this.terminals = new ArrayList();
            this.terminals.add(this);
            expandNodes(this.terminals);
            this.terminals =
                    org.jscience.ml.tigerxml.tools.GeneralTools.sortTerminals(this.terminals);
        } // if
        return this.terminals;
    } // getTerminals()

    /**
     * Returns the surface of this NT - the String consisting of all terminals
     * underneath this node.
     *
     * @return The String representation (all terminal nodes' word strings).
     */
    public String getWords() {
        StringBuffer strBuffer = new StringBuffer();
        ArrayList terms = this.getTerminals();
        for (int i = 0; i < terms.size(); i++) {
            strBuffer.append(((T) terms.get(i)).getWord() + " ");
        } // for i
        // Get rid of that last space
        strBuffer.deleteCharAt(strBuffer.length() - 1);
        return strBuffer.toString();
    } // getWords()

    /**
     * Returns the surface of this NT - the String consisting of all terminals
     * underneath this node.
     *
     * @return The String representation (all terminal nodes' word strings).
     */
    public String getText() {
        return this.getWords();
    }

    /**
     * Returns a list with all those nodes that
     * have a given edge label.
     */

    private static ArrayList filterByLabel
            (ArrayList nodes,
             String label) {
        ArrayList return_nodes = new ArrayList();
        for (int i = 0;
             i < nodes.size();
             i++) {
            GraphNode next_node =
                    (GraphNode) nodes.get(i);
            if ((next_node.getEdge2Mother()).equals
                    (label)) {
                return_nodes.add(next_node);
            }
        } // for i
        return return_nodes;
    }

    /**
     * Returns an ArrayList of the descendant nodes of this node.
     * The descendants are ordered breadth first
     * then left to right.
     * Note that you have to look
     * up the type of each child like this, if you want to get information like
     * POS, MORPH, or CAT:
     * <code>
     * GraphNode n = (GraphNode) someNode.getDescendants().get(1);<p>
     * if (n.isTerminal()) {
     * String fooPos = ((T)n).getPos();
     * }
     * </code>
     */
    public ArrayList getDescendants() {
        ArrayList return_daughters =
                new ArrayList(this.getDaughters());
        ArrayList agenda = new ArrayList(this.daughters);
        while (!(agenda.isEmpty())) {
            GraphNode next_node =
                    (GraphNode) agenda.remove(0);
            if (!(next_node.isTerminal())) {
                NT nt = (NT) next_node;
                return_daughters.addAll
                        (nt.getDaughters());
                agenda.addAll(nt.getDaughters());
            }
        }
        return return_daughters;
    }

    /**
     * Returns an ArrayList of all descendant nodes,
     * primary as well as secondary ones, of this node.
     * The descendants are ordered breadth first
     * then left to right.
     * Note that you have to look
     * up the type of each child like this, if you want to get information like
     * POS, MORPH, or CAT:
     * <code>
     * GraphNode n = (GraphNode) someNode.getAllDescendants().get(1);<p>
     * if (n.isTerminal()) {
     * String fooPos = ((T)n).getPos();
     * }
     * </code>
     */
    public ArrayList getAllDescendants() {
        ArrayList return_daughters =
                new ArrayList(this.getAllDaughters());
        ArrayList agenda = new ArrayList(this.getAllDaughters());
        while (!(agenda.isEmpty())) {
            GraphNode next_node =
                    (GraphNode) agenda.remove(0);
            if (!(next_node.isTerminal())) {
                NT nt = (NT) next_node;
                return_daughters.addAll
                        (nt.getAllDaughters());
                agenda.addAll(nt.getAllDaughters());
            }
        }
        return return_daughters;
    }

    /**
     * Returns an ArrayList of the daughter nodes with
     * a given edge label.
     * The list is ordered left to right according to word order
     * Note that you have to look
     * up the type of each child like this, if you want to get information like
     * POS, MORPH, or CAT:
     * <code>
     * GraphNode n = (GraphNode) someNode.getDaughtersByLabel().get(1);<p>
     * if (n.isTerminal()) {
     * String fooPos = ((T)n).getPos();
     * }
     * </code>
     */
    public ArrayList
    getDaughtersByLabel(String edgelabel) {
        ArrayList return_daughters =
                new ArrayList(this.getDaughters());
        return_daughters =
                filterByLabel(return_daughters, edgelabel);
        return return_daughters;
    }

    /**
     * Returns an ArrayList of all the daughter nodes, primary
     * as well as secondary ones, with
     * a given edge label.
     * The list is ordered left to right according to word order
     * Note that you have to look
     * up the type of each child like this, if you want to get information like
     * POS, MORPH, or CAT:
     * <code>
     * GraphNode n = (GraphNode) someNode.getAllDaughtersByLabel().get(1);<p>
     * if (n.isTerminal()) {
     * String fooPos = ((T)n).getPos();
     * }
     * </code>
     */
    public ArrayList
    getAllDaughtersByLabel(String edgelabel) {
        ArrayList return_daughters =
                new ArrayList(this.getAllDaughters());
        return_daughters =
                filterByLabel(return_daughters, edgelabel);
        return return_daughters;
    }

    /**
     * Returns an ArrayList of the descendant nodes with
     * a given edge label.
     * The descendants are ordered breadth first
     * then left to right.
     * Note that you have to look
     * up the type of each child like this, if you want to get information like
     * POS, MORPH, or CAT:
     * <code>
     * GraphNode n = (GraphNode) someNode.getDescendantsByLabel().get(1);<p>
     * if (n.isTerminal()) {
     * String fooPos = ((T)n).getPos();
     * }
     * </code>
     */
    public ArrayList
    getDescendantsByLabel(String edgelabel) {
        ArrayList return_daughters =
                this.getDescendants();
        return_daughters =
                filterByLabel(return_daughters, edgelabel);
        return return_daughters;
    }

    /**
     * Returns an ArrayList of all descendant nodes,
     * primary as well as secondary ones, with
     * a given edge label.
     * The descendants are ordered breadth first
     * then left to right.
     * Note that you have to look
     * up the type of each child like this, if you want to get information like
     * POS, MORPH, or CAT:
     * <code>
     * GraphNode n = (GraphNode) someNode.getAllDescendantsByLabel().get(1);<p>
     * if (n.isTerminal()) {
     * String fooPos = ((T)n).getPos();
     * }
     * </code>
     */
    public ArrayList
    getAllDescendantsByLabel(String edgelabel) {
        ArrayList return_daughters =
                this.getAllDescendants();
        return_daughters =
                filterByLabel(return_daughters, edgelabel);
        return return_daughters;
    }

    protected void print2Xml(FileWriter out_xml) {
        try {
            out_xml.write(" <nt ");
            out_xml.write
                    ("id=\"" + this.getId() + "\" ");
            out_xml.write
                    ("cat=\"" + this.getCat() + "\"");
            out_xml.write(">\n");
            ArrayList daughters = this.getDaughters();
            for (int i = 0;
                 i < daughters.size();
                 i++) {
                GraphNode next_daughter =
                        (GraphNode) daughters.get(i);
                out_xml.write("  <edge ");
                out_xml.write
                        ("label=\""
                                + next_daughter.getEdge2Mother()
                                + "\" ");
                out_xml.write
                        ("idref=\"" +
                                next_daughter.getId()
                                + "\"");
                out_xml.write("/>\n");
            } // for i
            out_xml.write(" </nt>\n");
        } catch (IOException e) {
            System.err.println
                    ("Error occurred while writing: " +
                            e.toString());
        }
    } // method print2Xml

} // class
