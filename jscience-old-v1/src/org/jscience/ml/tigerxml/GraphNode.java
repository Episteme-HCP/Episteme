/*
 * GraphNode.java
 *
 * Created on July 6, 2003, 1:42 AM
 *
 * Copyright (C) 2003 Oezguer Demir <oeze@coli.uni-sb.de>,
 *                    Vaclav Nemcik <vicky@coli.uni-sb.de>,
 *                    Hajo Keffer <hajokeffer@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.jscience.ml.tigerxml;

import org.jscience.ml.tigerxml.tools.GeneralTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a node in the syntax tree, either a terminal node or a
 * non-terminal node.<p>
 * <p/>
 * <code>GraphNode</code> is a generalization over <code>NT</code>
 * (non-terminal) and <code>T</code> (terminal)
 * nodes. <code>GraphNode</code> implements all methods and attributes that
 * are common to both, <code>NT</code> and <code>T</code> nodes.
 * <code>NT</code> and <code>T</code> are subclasses of this class and
 * inherit all methods. Furthermore, they implement methods which are
 * specific to non-terminal and terminal nodes respectively.<p>
 * <p/>
 * It is possible to cast one class to one of the others:
 * <ul>
 * <li><code>GraphNode</code> instances can be cast to <code>NT</code> <i>or</i>
 * <code>T</code>, depending on the <code>GraphNode</code> instance's
 * <code>.isTerminal()</code> value.
 * <li>All <code>NT</code> and <code>T</code> instances can be cast to
 * <code>GraphNode</code>.
 * </ul>
 * <p/>
 * Example:<p>
 * <code><pre>
 * Corpus corp = new Corpus("sampleTIGER");
 * GraphNode gn = corp.getGraphNodeBySpan("s1_1..s1_3");
 * if (gn.isTerminal()) {
 *   System.out.println( (T)gn.getWord());
 * } else {
 *   System.out.println( (NT)gn.toTreeString());
 * }
 * </pre></code>
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @author <a href="mailto:vicky@coli.uni-sb.de">Vaclav Nemcik</a>
 * @version 1.84
 *          $Id: GraphNode.java,v 1.2 2007-10-21 17:40:33 virtualcall Exp $
 * @see NT
 * @see T
 */
public class GraphNode
        implements Serializable {

    /* Class variables */

    /**
     * The String ID of the node.
     */
    private String id;

    /**
     * The sentence this GraphNode is part of.
     */
    private Sentence sentence;

    /**
     * The mother node
     */
    private NT mother;

    /**
     * The label String of the edge going to the mother node
     */
    private String edge2mother;

    /**
     * The secondary mothers of this node.
     */
    private ArrayList secMothers;

    /**
     * The edge labels of the secondary mothers.
     */
    private ArrayList secLabels;

    /**
     * The higher this value the more process and debug information will
     * written to stderr.
     */
    protected int verbosity = 0;

    /**
     * If true, this node is a terminal node and can be casted to
     * <code>T</code>. If false, to <code>NT</code>.
     */
    private boolean isTerminal;

    /**
     * The position index of this node in the ArrayList holding it.
     */
    private int index;

    /**
     * A hash map that stores the paths through the
     * tree leading to the other nodes
     */
    private HashMap node2path;

    /**
     * The buffer used to build the ascii representation of the syntax tree of
     * this node recursively.
     */
    private StringBuffer treeStringBuffer;

    /**
     * Creates an empty instance of <tt>GraphNode</tt> with all values set to
     * <tt>null</tt> or emty.
     */
    public GraphNode() {
        id = "";
        edge2mother = "";
        isTerminal = false;
        index = -1;
        treeStringBuffer = new StringBuffer();
        node2path = new HashMap();
    }

    /**
     * Creates an instance of GraphNode with alls values set as given.
     *
     * @param id          The ID-String of the node.
     * @param sentence    The sentence this instance of GraphNode is part of.
     * @param mother      The mother node (can only be a NT).
     * @param edge2mother The label of the edge going to the mother node.
     * @param isTerminal  True/false for whether this instance is an instance of T.
     * @param index       The position index of this node in the ArrayList holding it.
     */
    public GraphNode(String id, Sentence sentence, NT mother,
                     String edge2mother,
                     boolean isTerminal, int index) {
        this.id = id;
        this.sentence = sentence;
        this.mother = mother;
        this.edge2mother = edge2mother;
        this.isTerminal = isTerminal;
        this.index = index;
        this.node2path = new HashMap();
    }

    /* ACCESS METHODS */

    /**
     * Returns true if this node is a terminal node (T), false if not.
     * If this is a terminal node, it can be casted to <code>T</code>.
     *
     * @return True if this is an instance of <code>T</code>.
     * @see T
     */
    public boolean isTerminal() {
        return this.isTerminal;
    }

    /**
     * Sets whether this node is a terminal.
     *
     * @param isTerminal True if this node is a terminal.
     */
    public void setTerminal(boolean isTerminal) {
        this.isTerminal = isTerminal;
    }

    /**
     * Returns the ID String of this node.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the ID String of this node.
     */
    public void setId(String newid) {
        id = newid;
    }

    /**
     * Returns the sentence this GraphNode is part of.
     */
    public Sentence getSentence() {
        return this.sentence;
    }

    /**
     * Sets the sentence this GraphNode is part of.
     */
    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    /**
     * Returns the corpus this GraphNode is part of.
     */
    public Corpus getCorpus() {
        return this.sentence.getCorpus();
    }

    /**
     * Returns the mother node. You might have to use a cast like this:
     * <code>
     * NT myNonTerminal = (NT) someGraphNode.getMother();
     * </code>
     */
    public NT getMother() {
        return this.mother;
    }

    /**
     * Sets the mother of this node.
     */
    public void setMother(NT newmother) {
        this.mother = newmother;
    }

    /**
     * Returns the label of the edge going to the mother node.
     */
    public String getEdge2Mother() {
        return this.edge2mother;
    }

    /**
     * Sets the label String of the edge going to the mother node.
     */
    public void setEdge2Mother(String newedge2mother) {
        edge2mother = newedge2mother;
    }

    /**
     * Sets a secondary mother of this node and the label of the edge going to it.
     */
    public void setSecMother
            (NT mother, String label) {
        if (secMothers == null) {
            secMothers = new ArrayList();
        }
        if (secLabels == null) {
            secLabels = new ArrayList();
        }
        secMothers.add(mother);
        secLabels.add(label);
    }

    /**
     * Returns true if this node has any
     * secondary mothers.
     */
    public boolean hasSecMothers() {
        if (secMothers != null) {
            return (!secMothers.isEmpty());
        } else {
            return false;
        }
    }

    /**
     * Returns an ArrayList of the secondary mothers
     * of this node. Note that it returns null
     * if there aren't any secondary mothers.
     */

    public ArrayList getSecMothers() {
        return this.secMothers;
    }

    /**
     * Returns the label that belongs to the edge
     * going to the secondary mother s_mother.
     */
    public String getEdge2SecMother(NT s_mother) {
        if (this.hasSecMothers()) {
            for (int i = 0;
                 i < secMothers.size();
                 i++) {
                NT next_nt = (NT) secMothers.get(i);
                if (next_nt.equals(s_mother)) {
                    return (String) secLabels.get(i);
                }
            } // for i
            if (this.verbosity > 0) {
                System.err.println
                        ("org.jscience.ml.tigerxml.GraphNode: WARNING: " +
                                "could not retrieve secondary mother "
                                + s_mother.getId());
            }
            return "";
        } else {
            if (this.verbosity > 0) {
                System.err.println
                        ("org.jscience.ml.tigerxml.GraphNode: WARNING: " +
                                " attempt to retrieve secondary edgelabel "
                                + " of node " + this.getId() +
                                " which has not got any secondary mother ");
            }
            return "";
        }
    }

    /**
     * Returns the position index of this node in the ArrayList holding it.
     *
     * @return An integer denoting the index of the ArrayList holding this node.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Sets the position index in the ArrayList holding this node.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns the String representation of this node.
     *
     * @return The String representation (all terminal nodes' word strings).
     */
    public String toString() {
        return this.id;
    } // toString()

    // Author: Hajo ---------------------------------------------------------

    /**
     * Returns true if the two nodes are identical.
     */
    public boolean equals(GraphNode otherNode) {
        if (otherNode == null) {
            return false;
        }
        String thisID = this.getId();
        String otherID = otherNode.getId();
        return thisID.equals(otherID);
    } // method equals

    /**
     * Returns true if this node has a mother node. Use this method to catch
     * cases where a returned <code>null</code> from {@link #getMother()}
     * would cause trouble.
     */
    public boolean hasMother() {
        return (this.getMother() != null);
    }

    /**
     * Prints identifying information on this node to stderr.
     */
    public void printNodeErr() {
        System.err.println(this.toNodeInfoString());
        System.err.flush();
    } // method printNodeErr()

    /**
     * Prints a comment and identifying information on this node to stderr.
     *
     * @param comment A comment precding the printed information.
     */
    public void printNodeErr(String comment) {
        System.err.println(comment + " " + this.toNodeInfoString());
        System.err.flush();
    } // method printNodeErr(String)

    /**
     * Returns the surface string of this node. All contained terminal strings
     * sorted by linear precedence and put into a single string.
     *
     * @return The surface string of this node.
     */
    public String getText() {
        if (this.isTerminal()) {
            return ((T) this).getWord();
        } else {
            return ((NT) this).getWords();
        }
    }

    /**
     * Returns true if one of the leaves of the
     * subtree whose root this node is
     * has the input word as its word
     */
    public boolean hasSurfaceWord(String word) {
        if (this.isTerminal()) {
            return (((T) this).getWord()).equals(word);
        } else {
            ArrayList terminals =
                    ((NT) this).getTerminals();
            for (int i = 0;
                 i < terminals.size();
                 i++) {
                T nextTerminal = (T) terminals.get(i);
                if ((nextTerminal.getWord()).equals
                        (word)) {
                    return true;
                }
            } // for i
            return false;
        }
    } // method hasSurfaceWord

    /**
     * Returns true if there is a dominating node
     * that has the category cat.
     * or if this node has the category cat itself
     *
     * @return A truth value indicating wheter there is
     *         a dominating cat node.
     */
    public boolean isDominatedBy(String cat) {
        return (getDominatingNodeByCat(cat) != null);
    }

    /**
     * Returns the nearest dominating node with category cat,
     * null if there is no such node
     * If this node has the category cat it will be returned itself.
     *
     * @return The nearest node whith cat that dominates this node.
     */
    public NT getDominatingNodeByCat
            (String cat) {
        if ((!(this.isTerminal())) &&
                ((((NT) this).getCat()).equals(cat))) {
            return (NT) this;
        } else if (!(this.hasMother())) {
            return null;
        } else {
            NT mother = this.getMother();
            while (mother != null) {
                if ((mother.getCat()).equals(cat)) {
                    return mother;
                }
                mother = mother.getMother();
            } //while
            return null;
        }
    } // method getDominatingNodeByCat()

    /**
     * Returns a <code>Path</code> object representing
     * the path through the syntax tree which connects
     * this node and the input node.
     *
     * @return a <code>Path</code> object representing
     *         the path which connects this and the input node.
     */
    public Path getPath(GraphNode toNode) {
        if (node2path.containsKey(toNode)) {
            return (Path) node2path.get(toNode);
        } else {
            Path newPath =
                    Path.makePath(this, toNode);
            node2path.put(toNode, newPath);
            return newPath;
        }
    } // method getPath

    /**
     * Returns true if this node is dominated by
     * the input node in the syntax tree.
     *
     * @return a truth value indicating whether this
     *         node is dominated by the input node.
     */
    public boolean isDominatedBy(GraphNode node) {
        return ((getPath(node)).start_below_end);
    }

    /**
     * Returns true if this node dominates
     * the input node in the syntax tree.
     *
     * @return a truth value indicating whether this
     *         node dominates the input node.
     */
    public boolean dominates(GraphNode node) {
        return ((getPath(node)).end_below_start);
    }

    /**
     * Returns the tree structure of this GraphNode as a string.
     *
     * @return The tree structure of this GraphNode as a string.
     */
    public String toTreeString() {
        this.treeStringBuffer = new StringBuffer();
        this.buildTreeStringBuffer(0, this.treeStringBuffer);
        return this.treeStringBuffer.toString();
    }

    /**
     * Returns the tree structure of this node and its daughters as a string.
     * This method is primarily for class-internal usage.
     *
     * @param depth The current level of indendation (recursion).
     * @param tree  The tree string built so far (recursion).
     * @see Sentence#printTree()
     */
    private void buildTreeStringBuffer(int depth, StringBuffer tree) {
        for (int i = 0; i < depth; i++) {
            tree.append("|   ");
        }
        tree.append("|--" + this.getEdge2Mother() + "--");
        if (this.isTerminal) {
            tree.append("(" + ((T) this).getPos() + ") "
                    + "\"" + this.getText() + "\""
                    + " (" + ((T) this).getMorph() + ") "
                    + "<" + this.getId() + ">"
                    + System.getProperties().getProperty("line.separator", "\n"));
        } else {
            tree.append("[" + ((NT) this).getCat() + "] "
                    + "\"" + this.getText() + "\""
                    + " <" + this.getId() + ">"
                    + System.getProperties().getProperty("line.separator", "\n"));
            ArrayList daughters = ((NT) this).getDaughters();
            daughters = GeneralTools.sortNodes(daughters);
            for (int i = 0; i < daughters.size(); i++) {
                GraphNode currentDaughter = (GraphNode) daughters.get(i);
                currentDaughter.buildTreeStringBuffer(depth + 1, tree);
            } // for i
        } // else
    } // buildTreeString()

    /**
     * Returns identifying information about this node as a string.
     *
     * @return Identifying information about this node as a string.
     */
    public String toNodeInfoString() {
        StringBuffer infoStrBuffer = new StringBuffer();
        infoStrBuffer.append("ID: <" + this.id + "> CorpusID: <" + this.getCorpus()
                + "> SentenceID: <" + this.sentence.getId()
                + "> Terminal: \'" + this.isTerminal + "\' Text: \"");
        if (this.isTerminal()) {
            infoStrBuffer.append(((T) this).getWord());
        } else {
            infoStrBuffer.append(((NT) this).getWords());
        }
        infoStrBuffer.append("\"" + System.getProperty("line.separator", "\n"));
        return infoStrBuffer.toString();
    } // method toNodeInfoString()

    /**
     * Prints all daughters recursively in a tree structure to stdout.
     */
    public void printTree() {
        System.out.println(this.toTreeString());
    } // printTreeErr()

    /**
     * Prints all daughters recursively in a tree structure to stderr.
     */
    public void printTreeErr() {
        System.err.println(this.toTreeString());
    } // printTreeErr()

    /**
     * Prints identifying information about this node.
     */
    public void printNode() {
        System.out.println(this.toNodeInfoString());
        System.out.flush();
    } // method printNode()

    /**
     * Prints a comment and identifying information about this node.
     *
     * @param comment A comment precding the printed information.
     */
    public void printNode(String comment) {
        System.out.println(comment + " " + this.toNodeInfoString());
        System.out.flush();
    } // method printNode(String)

    /**
     * Returns true if the leftmost terminal of the
     * tree whose root node this node is linearly
     * preceedes the leftmost terminal of the tree
     * whose root node the input node is.
     */
    public boolean before(GraphNode node) {
        if (this.getLeftmostTerminal().getPosition()
                < node.getLeftmostTerminal().getPosition()) {
            return true;
        }
        return false;
    } // method before

    /**
     * Returns the leftmost terminal of the
     * tree whose root node this node is.
     */
    public T getLeftmostTerminal() {
        if (this.isTerminal) {
            return (T) this;
        }
        return (T) ((NT) this).getTerminals().get(0);
    } // method getLeftmostTerminal()

    /**
     * Gets the currently set level of verbosity of this instance. The higher
     * the value the more information is written to stderr.
     *
     * @return The level of verbosity.
     */
    public int getVerbosity() {
        return this.verbosity;
    }

    /**
     * Sets the currently set level of verbosity of this instance. The higher
     * the value the more information is written to stderr.
     *
     * @param verbosity The level of verbosity.
     */
    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

} // class
