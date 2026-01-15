/*
 * CorpusBuilder.java
 *
 * Created on February 13, 2003, 3:19 PM
 *
 * Copyright (C) 2003,2004 Oezguer Demir <oeze@coli.uni-sb.de>,
 *                         Vaclav Nemcik <vicky@coli.uni-sb.de>,
 *                         Hajo Keffer <hajokeffer@coli.uni-sb.de>
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

package org.jscience.ml.tigerxml.core;

import org.jscience.ml.tigerxml.Corpus;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.Sentence;
import org.jscience.ml.tigerxml.T;
import org.jscience.ml.tigerxml.tools.DomTools;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SentenceBuilder {

    /**
     * Builds up a given (ideally empty) <code>Sentence</code> by getting
     * all information from the given DOM </code>Element</code>
     * <code>sElement</code>.
     *
     * @param sentence The <code>Sentence</code> instance to be built
     *                 (can be empty).
     * @param corpus   The <code>Corpus</corpus> to hold the new
     *                 <code>sentence</code>.
     * @param sElement The DOM <code>Element</code> containing
     *                 a sentence element of a TIGER XML <code>Document</code>.
     */
    public static final void buildSentence(Sentence sentence, Corpus corpus,
                                           Element sElement) {
        if (sentence.getVerbosity() > 0) {
            // Print a warning if the root node is not "sentence"
            DomTools.checkElementName(sElement, "s");
        }
        // Init members
        sentence.setCorpus(corpus);
        sentence.setIndex(corpus.getSentenceCount());
        sentence.setId(sElement.getAttribute("id"));
        sentence.setVerbosity(corpus.getVerbosity());
        // Process the sentence graph
        String rootNodeID;
        Element graph = DomTools.getElement(sElement, "graph");
        if (graph.hasAttribute("root")) {
            rootNodeID = graph.getAttribute("root");
        } // if root
        else {
            rootNodeID = null;
            if (sentence.getVerbosity() >= 2) {
                System.err.println("org.jscience.ml.tigerxml.Sentence: No root specified for "
                        + "Sentence "
                        + sElement.getAttribute("id")
                        + ". Will infer root node from structure.");
            } // if
        } // else
        // TERMINALS PROCESSING
        Element terminals = DomTools.getElement(graph, "terminals");
        NodeList domNodeList = terminals.getElementsByTagName("t");
        for (int i = 0; i < domNodeList.getLength(); i++) {
            Element domNode = (Element) domNodeList.item(i);
            T my_term = new T(domNode, sentence, i);
            my_term.setVerbosity(sentence.getVerbosity());
            sentence.addT(my_term);
            // look whether there is already a virtual
            // node with the same id
            // if so, add virtual node info to this
            // terminal
            if (sentence.hasVNode(my_term.getId())) {
                VNode v_node = sentence.getVNode(my_term.getId());
                v_node.addInfo(my_term);
            } // if
        } // for i
        //  NONTERMINALS PROCESSING
        Element non_terminals = DomTools.getElement(graph, "nonterminals");
        NodeList domNodeList2 = non_terminals.getElementsByTagName("nt");
        for (int n = 0; n < domNodeList2.getLength(); n++) {
            Element domNode = (Element) domNodeList2.item(n);
            //ADDING THE NON-TERMINAL
            NT my_non_term = new NT(domNode, sentence);
            my_non_term.setVerbosity(sentence.getVerbosity());
            sentence.addNT(my_non_term);
            // look whether there is already a virtual
            // node with the same id
            // if so, add virtual node info to this
            // non-terminal
            if (sentence.hasVNode(my_non_term.getId())) {
                VNode v_node = sentence.getVNode(my_non_term.getId());
                v_node.addInfo(my_non_term);
            }
        } // for n
        /// commented out by oeze: in use: Sentence.getRootNT
        /// root NT annotation in source is unreliable, method getRootNT failsafe
        //              ROOT ELEMENT PROCESSING
        //GraphNode graphNode = this.getById(rootNodeID);
        //if (graphNode == null) {
        //  System.err.println("org.jscience.ml.tigerxml.Sentence: WARNING: Root node in Sentence "
        //                     + this + "not found!");
        //}
        //else {
        //graphNode.setMother(null);
        //graphNode.setEdge2Mother(null);
        //if (!graphNode.isTerminal()) {
        //  this.root = (NT) graphNode;
        //}
        //else {
        //  System.err.println(
        //    "org.jscience.ml.tigerxml.Sentence: WARNING: Specified root node in Sentence " + this
        //    + " is terminal!");
        //}
        //} // else
    }

}
