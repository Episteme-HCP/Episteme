/*
 * Sentence.java
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

import org.jscience.ml.tigerxml.core.SentenceBuilder;
import org.jscience.ml.tigerxml.core.VNode;
import org.jscience.ml.tigerxml.core.VRoot;
import org.w3c.dom.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a sentence in a corpus.
 * <code>Sentence</code> objects hold a list of NTs and a list of Ts. They also
 * provide methods for traversing the syntax tree, accessing nodes directly,
 * and getting other structural information.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @author <a href="mailto:vicky@coli.uni-sb.de">Vaclav Nemcik</a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84
 *          $Id: Sentence.java,v 1.2 2007-10-21 17:40:33 virtualcall Exp $
 * @see GraphNode
 * @see NT
 * @see T
 */
public class Sentence
        implements Serializable {

    /**
     * The ID String of this sentence
     */
    private String id;
    /**
     * The position index of this sentence in the corpus holding it.
     */
    private int index;
    /**
     * The corpus this sentence is part of
     */
    private Corpus corpus;
    /**
     * The non-terminal nodes of this sentence
     */
    private ArrayList nts;
    /**
     * The terminal nodes of this sentence
     */
    private ArrayList ts;
    /**
     * The String representation of this sentence: the text
     */
    private String text;
    /**
     * The terminals in sentence order (linear precedence)
     */
    private ArrayList terminals;
    /**
     * The virtual root node - this node is imaginary and the only node that
     * holds all other nodes.
     */
    private NT vroot;
    /**
     * The real graph root.
     */
    private NT root;
    /**
     * The hash code number.
     */
    private int hashCode;
    /**
     * A hash storing possible virtual node with their ids.
     */
    private HashMap id2vnode;
    /**
     * The higher this value the more process and debug information will
     * written to stderr.
     */
    private int verbosity = 0;

    /**
     * Creates a new Sentence instance setting all member variables
     * to <code>null</code> or empty.
     */
    public Sentence() {
        init();
    }

    /**
     * Creates a new Sentence instance extracting all necessary information
     * from the passed DOM Element.
     *
     * @param sElement The DOM Sentence Element.
     * @param corpus   The Corpus this Sentence is part of.
     */
    public Sentence(Element sElement, Corpus corpus) {
        init();
        SentenceBuilder.buildSentence(this, corpus, sElement);
    } // Sentence()

    /**
     * Creates a new Sentence instance extracting all necessary information
     * from the passed DOM Element.
     *
     * @param sElement The DOM Sentence Element.
     * @param corpus   The Corpus this Sentence is part of.
     */
    public Sentence(Element sElement, Corpus corpus, int verbosity) {
        init();
        this.verbosity = verbosity;
        SentenceBuilder.buildSentence(this, corpus, sElement);
    } // Sentence()

    private void init() {
        this.nts = new ArrayList();
        this.ts = new ArrayList();
        this.id = "";
        this.index = -1;
        this.id2vnode = new HashMap();
        this.hashCode = 0;
        this.text = "";
        this.terminals = new ArrayList();
        this.vroot = null;
        this.root = null;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String newid) {
        id = newid;
    }

    /**
     * Returns the position index of this sentence in the corpus holding it.
     *
     * @return An integer denoting the index in the corpus holding this sentence.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the position index in the corpus holding this sentence.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns the corpus this sentence is part of.
     *
     * @return The corpus that holds this sentence in its sentences ArrayList.
     */
    public Corpus getCorpus() {
        return corpus;
    }

    /**
     * Sets the corpus this sentence is part of.
     */
    public void setCorpus(Corpus passCorpus) {
        this.corpus = passCorpus;
    }

    /**
     * Returns the sentence object succeeding this sentence in the sentences
     * ArrayList of the corpus containing this sentence.
     *
     * @return The sentence succeeding this sentence.
     */
    public Sentence getNextSentence() {
        if (this.corpus.getSentenceCount() <= this.index + 1) {
            return null;
        }
        return this.getCorpus().getSentence(this.index + 1);
    }

    /**
     * Returns the sentence object predecessing this sentence in the sentences
     * ArrayList of the corpus containing this sentence.
     *
     * @return The sentence predecessing this sentence.
     */
    public Sentence getPrevSentence() {
        if (this.index == 0) {
            return null;
        }
        return this.getCorpus().getSentence(this.index - 1);
    }

    /**
     * Returns the sentence object succeeding this sentence in the sentences
     * ArrayList of the corpus containing this sentence by n.
     *
     * @return The sentence succeeding this sentence by n.
     */
    public Sentence getNextSentence(int n) {
        if (this.corpus.getSentenceCount() <= this.index + n) {
            return null;
        }
        return this.getCorpus().getSentence(this.index + n);
    }

    /**
     * Returns the sentence object predecessing this sentence in the sentences
     * ArrayList of the corpus containing this sentence by n.
     *
     * @return The sentence predecessing this sentence by n.
     */
    public Sentence getPrevSentence(int n) {
        if (this.index - n < 0) {
            return null;
        }
        return this.getCorpus().getSentence(this.index - n);
    }

    /**
     * Returns the MMAX-style span of the sentence.
     *
     * @return A String representing the span (e.g. "s1_1..s1_14")
     */
    public String getSpan() {
        T firstT = (T) ts.get(0);
        T lastT = (T) ts.get(ts.size() - 1);
        return new String(firstT.getId() + ".." + lastT.getId());
    }

    /**
     * Returns the number of NT objects in the sentence.
     *
     * @return An integer denoting the number of NTs in this sentence.
     */
    public int getNTCount() {
        return nts.size();
    }

    /**
     * Returns the number of NT objects in the sentence.
     *
     * @return An <code>integer</code> denoting the number of non-terminals in
     *         this sentence.
     * @deprecated As of org.jscience.ml.tigerxml 1.1 - use {@link #getNTCount()} instead.
     */
    public int getNoOfNTs() {
        return nts.size();
    }

    public NT getNT(int i) {
        if (nts.size() == 0) {
            return null;
        }
        return (NT) nts.get(i);
    }

    /**
     * Returns the <code>NT</code> which has the given ID. Returns null if the
     * search fails. If the sentence of the <code>NT</code> is not known,
     * {@link Corpus#getNT(Stringid)} can be used to retrieve the
     * wanted <code>NT</code>.
     *
     * @param id The ID of the NT to be found.
     * @return The NT that is identified by ID or <code>null</code> if the search
     *         fails.
     */
    public NT getNT(String id) {
        for (int i = 0; i < this.nts.size(); i++) {
            NT currentNT = (NT) this.nts.get(i);
            if (currentNT.getId().equalsIgnoreCase(id)) {
                return currentNT;
            } // if
        } // for i
        // maybe it is the VROOT?
        if (this.getVROOT().getId().equalsIgnoreCase(id)) {
            return this.getVROOT();
        }
        return null;
    } // getNT

    /**
     * Returns the <code>T</code> which has the given ID. Returns null if the
     * search fails. If the sentence of the <code>T</code> is not known,
     * {@link Corpus#getT(Stringid)} can be used to retrieve the
     * wanted <code>T</code>.
     *
     * @param id The ID of the T to be found.
     * @return The T that is identified by ID or <code>null</code> if the search
     *         fails.
     */
    public T getT(String id) {
        for (int i = 0; i < this.ts.size(); i++) {
            T currentT = (T) this.ts.get(i);
            if (currentT.getId().equalsIgnoreCase(id)) {
                return currentT;
            } // if
        } // for i
        return null;
    }

    /**
     * Returns the <code>GraphNode</code> which has the given ID. Returns null if
     * the search fails. If the sentence of the <code>GraphNode</code> is
     * not known, {@link Corpus#getGraphNode(Stringid)} can be used to retrieve
     * the wanted <code>GraphNode</code>.
     *
     * @param id The ID of the <code>GraphNode</code> to be found.
     * @return The <code>GraphNode</code> that is identified by ID or
     *         <code>null</code> if the search fails.
     */
    public GraphNode getGraphNode(String id) {
        GraphNode out;
        out = this.getT(id);
        if (out != null) {
            return out;
        }
        out = this.getNT(id);
        if (out != null) {
            return out;
        }
        return null;
    }

    /**
     * Returns all <code>GraphNode</code> objects contained in this sentence. The
     * returned GraphNodes are in the order of the XML corpus file. In order
     * to have the list ordered by linear precedence, use
     * {@link org.jscience.ml.tigerxml.tools.GeneralTools#sortNodes(ArrayListnodes)}.
     * <p/>
     * The returned list does not contain the VROOT.<p>
     * Ordering by class:<br>
     * All <code>NT</code> objects of the sentence are followed by all
     * <code>T</code> object of the corpus.
     *
     * @return All graph nodes contained in this <code>Sentence</code>.
     */
    public ArrayList getAllGraphNodes() {
        ArrayList allGraphNodes = new ArrayList();
        allGraphNodes.addAll(this.nts);
        allGraphNodes.addAll(this.ts);
        return allGraphNodes;
    }

    /**
     * Returns the <code>T</code> which has the given ID. Returns null if the
     * search fails. If the sentence of the <code>T</code> is not known,
     * {@link Corpus#getTerminal(Stringid)} can be used to retrieve the
     * wanted <code>T</code>.
     *
     * @param id The ID of the T to be found.
     * @return The T that is identified by ID or <code>null</code> if the search
     *         fails.
     */
    public T getTerminal(String id) {
        return this.getT(id);
    }

    public ArrayList getNTs() {
        return this.nts;
    }

    public ArrayList getTs() {
        return this.ts;
    }

    public void addNT(NT newnt) {
        nts.add(newnt);
    }

    /**
     * Returns the root node of the sentence (always an NT).
     * Note that this method returns null if there is no sentence root but
     * the VROOT at the top of the tree.
     * That's the way NEGRA Syntax analyses simple
     * sentences (headlines) like
     * "Darmstadt-Braunschweig 1:0"
     *
     * @return The root NT, <code>null</code> if sth. goes wrong.
     */
    public NT getRootNT() {
        if (this.root != null) {
            return this.root;
        }
        // Strategy: start from the end, look for the first NT node in the list of
        // NT nodes that has the mother 'null'
        for (int i = this.nts.size() - 1; i >= 0; i--) {
            NT n = (NT) this.nts.get(i);
            if (n.getMother() == null) {
                this.root = n;
                return n;
            } // if
        } // for
        // This means the method failed
        return null;
    }

    /**
     * Returns the VROOT of this sentence. If necessary creates it first,
     * linking all of its daughter nodes to it.
     * <p/>
     * The virtual root node is imaginary and the only node that
     * holds all other nodes.
     *
     * @return The virtual root node of this sentence - VROOT.
     */
    public NT getVROOT() {
        // if already generated, return the VROOT
        if (this.vroot != null) {
            return (NT) this.vroot;
        }
        // else create the VROOT
        else {
            this.vroot = new VRoot(this);
            return (NT) this.vroot;
        } // else
    } // getVROOT()

    public boolean hasRootNT() {
        return (!(this.getRootNT() == null));
    }

    /**
     * Returns the number of T objects in the sentence.
     *
     * @return An integer denoting the number of terminals in this sentence.
     */
    public int getTCount() {
        return this.ts.size();
    }

    /**
     * Returns the number of T objects in the sentence.
     *
     * @return An integer denoting the number of terminals in this sentence.
     * @deprecated As of org.jscience.ml.tigerxml 1.1 - use {@link #getTCount()} instead.
     */
    public int getNoOfTs() {
        return ts.size();
    }

    public T getT(int i) {
        return (T) ts.get(i);
    }

    public void addT(T newt) {
        ts.add(newt);
    }

    public String getText() {
        if (this.text == null) {
            StringBuffer textBuffer = new StringBuffer();
            for (int i = 0; i < this.ts.size(); i++) {
                textBuffer.append(((T) this.ts.get(i)).getWord());
                textBuffer.append(" ");
            } // for i
            this.text = textBuffer.toString();
        } // if
        return this.text;
    }

    /**
     * Returns all terminal daughters. The returned terminals are
     * in the order of the sentence (linear precedence).
     */
    public ArrayList getTerminals() {
        // If not already generated, generate the terminal nodes list
        if (this.terminals == null) {
            this.terminals = this.getRootNT().getTerminals();
        } // if
        return this.terminals;
    } // getTerminals()

    /**
     * Returns the syntax tree structure of this sentence as as string.
     */
    public String toTreeString() {
        return this.getVROOT().toTreeString();
        ////DEBUGreturn this.getRootNT().toTreeString();
    }

    /**
     * Prints the syntax tree structure of this sentence to stout.
     */
    public void printTree() {
        System.out.println(this.toTreeString());
    }

    /**
     * Prints the syntax tree structure of this sentence to stderr.
     */
    public void printTreeErr() {
        System.err.println(this.toTreeString());
    }

    /**
     * Returns the String representation of this Sentence - its ID.
     *
     * @return The String representation of this Sentence - its ID.
     */
    public String toString() {
        return this.id;
    }

    /**
     * Returns true if the object is identical to this <code>Sentence</code>
     * object. Identity is determined by comparing the sentence IDs.
     *
     * @param obj The Java <code>Object</code> to which this is to be
     *            compared to.
     * @return True if the object is a sentence and
     *         the sentences are identical.
     * @see #getId()
     */
    public boolean equals(Object obj) {
        try {
            Sentence your_sentence = (Sentence) obj;
            String my_id = this.id;
            String your_id = your_sentence.getId();
            return my_id.equals(your_id);
        } catch (ClassCastException e) {
            return false;
        }
    } // method equals

    public int hashCode() {
        if (hashCode == 0) {
            hashCode = (this.toString()).hashCode();
        }
        return hashCode;
    }

    /**
     * Finds and returns a <code>GraphNode</code> in this sentence or returns
     * <code>null</code> if there is no <code>GraphNode</code> with the given ID.
     *
     * @param pass_id The ID of the <code>GraphNode</code> to be found.
     * @return The <code>GraphNode</code> with the given ID or <code>null</code>.
     */
    public GraphNode getById(String pass_id) {
        for (int i = 0; i < ts.size(); i++) {
            T tmpT = (T) ts.get(i);
            if (tmpT.getId().equalsIgnoreCase(pass_id)) {
                return (GraphNode) tmpT;
            }
        } // for i
        for (int i = 0; i < nts.size(); i++) {
            NT tmpNT = (NT) nts.get(i);
            if (tmpNT.getId().equalsIgnoreCase(pass_id)) {
                return (GraphNode) tmpNT;
            }
        } // for i
        // maybe it is the VROOT?
        if (this.getVROOT().getId().equalsIgnoreCase(pass_id)) {
            return this.getVROOT();
        }
        return null;
    } // getById()

    public void addVNode(VNode v_node) {
        id2vnode.put(v_node.getId(), v_node);
    }

    public boolean hasVNode(String v_node_id) {
        return (id2vnode.containsKey(v_node_id));
    }

    public VNode getVNode(String v_node_id) {
        return ((VNode) id2vnode.get(v_node_id));
    }

    protected void print2Xml(FileWriter out_xml) {
        try {
            out_xml.write("<s ");
            out_xml.write
                    ("id=\"" + this.getId() + "\"");
            out_xml.write(">\n");
            String root_id = "";
            // look for an root id
            NT root_nt = this.getRootNT();
            if (root_nt != null) {
                root_id = root_nt.getId();
            } else {
                if (this.getTCount() > 0) {
                    T first_t = this.getT(0);
                    root_id = first_t.getId();
                }
            }
            out_xml.write("<graph ");
            out_xml.write
                    ("root=\"" + root_id + "\"");
            out_xml.write(">\n");
            out_xml.write("<terminals>\n");
            for (int i = 0;
                 i < this.getTCount();
                 i++) {
                T next_t = this.getT(i);
                next_t.print2Xml(out_xml);
            }
            out_xml.write("</terminals>\n");
            out_xml.write("<nonterminals>\n");
            for (int i = 0;
                 i < this.getNTCount();
                 i++) {
                NT next_nt = this.getNT(i);
                next_nt.print2Xml(out_xml);
            }
            out_xml.write("</nonterminals>\n");
            out_xml.write("</graph>\n");
            out_xml.write("</s>\n");
        } catch (IOException e) {
            System.err.println
                    ("Error occurred while writing: " +
                            e.toString());
        }
    } // method print2Xml

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
