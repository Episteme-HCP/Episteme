/*
 * NTBuilder.java
 *
 * Created on February 13, 2003, 4:51 PM
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

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.Sentence;
import org.jscience.ml.tigerxml.tools.DomTools;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class NTBuilder {

    /**
     * Builds up a given (ideally empty) <code>NT</code> by getting
     * all information from the given DOM </code>Element</code>
     * <code>ntElement</code>.
     *
     * @param nt        The NT to be built (can be empty).
     * @param sent      The <code>Sentence</code> to hold the new <code>NT</code>.
     * @param ntElement The DOM <code>Element</code> containing
     *                  a sentence element of a TIGER XML <code>Document</code>.
     */
    public static final void buildNT(NT nt, Sentence sent, Element ntElement) {
        nt.setVerbosity(sent.getVerbosity());
        if (nt.getVerbosity() > 0) {
            DomTools.checkElementName(ntElement, "nt");
        }
        if (nt.getVerbosity() >= 4) {
            System.err.println("org.jscience.ml.tigerxml.core.NTBuilder: Building and adding NT " +
                    ntElement.getAttribute("id"));
        }
        if (ntElement.hasAttribute("id") && ntElement.hasAttribute("cat")) {
            nt.setId(ntElement.getAttribute("id"));
            nt.setCat(ntElement.getAttribute("cat"));
            nt.setSentence(sent);
            nt.setIndex(sent.getNTCount());
            NodeList edgeNodes = ntElement.getElementsByTagName("edge");
            for (int i = 0; i < edgeNodes.getLength(); i++) {
                Element currentEdgeElement = (Element) edgeNodes.item(i);
                if (currentEdgeElement.hasAttribute("label") &&
                        currentEdgeElement.hasAttribute("idref")) {
                    String idref = currentEdgeElement.getAttribute("idref");
                    GraphNode refGraphNode = sent.getById(idref);
                    if (refGraphNode == null) {
                        // no graph node with a fitting id
                        // means that it hasn't been created
                        // yet
                        // Mother info has to be temporarily
                        // stored in a virtual node
                        // It will be added later when the node
                        // in question is created
                        VNode vnode = null;
                        if (sent.hasVNode(idref)) {
                            vnode = sent.getVNode(idref);
                        } else {
                            vnode = new VNode(idref, nt.getCorpus().getVerbosity());
                            sent.addVNode(vnode);
                        }
                        vnode.setMother(nt);
                        vnode.setEdge2Mother
                                (currentEdgeElement.getAttribute("label"));
                    } else {
                        //ADDING MOTHER INFORMATION TO DESCENDANTS ...
                        refGraphNode.setMother(nt);
                        refGraphNode.setEdge2Mother(currentEdgeElement.getAttribute("label"));
                        nt.addDaughter(refGraphNode);
                    }
                } // if elem2 has attribute label and idref
                else {
                    if (currentEdgeElement.hasAttribute("idref")) {
                        System.err.println("org.jscience.ml.tigerxml.NT: "
                                + "An edge with no LABEL value reached. "
                                + "Skipping ...");
                    } else {
                        System.err.println("org.jscience.ml.tigerxml.NT: "
                                + "An edge with no IDREF value reached. "
                                + "Skipping ...\n");
                    }
                }
            } // for i
            org.w3c.dom.NodeList list_of_nodes2 =
                    ntElement.getElementsByTagName("secedge");
            for (int j = 0;
                 j < list_of_nodes2.getLength();
                 j++) {
                org.w3c.dom.Element elem2 =
                        (org.w3c.dom.Element)
                                list_of_nodes2.item(j);
                if (elem2.hasAttribute("label") &&
                        elem2.hasAttribute("idref")) {
                    String idref = elem2.getAttribute("idref");
                    GraphNode graph_node1 =
                            sent.getById(idref);
                    if (graph_node1 == null) {
                        // no graph node with a fitting id
                        // means that it hasn't been created
                        // yet
                        // SMother info has to be temporarily
                        // stored in a virtual node
                        // It will be added later when the node
                        // in question is created
                        VNode v_node = null;
                        if (sent.hasVNode(idref)) {
                            v_node = sent.getVNode(idref);
                        } else {
                            v_node = new VNode(idref, nt.getCorpus().getVerbosity());
                            sent.addVNode(v_node);
                        }
                        v_node.addSecDaughter(nt, elem2.getAttribute("label"));
                    } else {
                        //ADDING S-MOTHER INFORMATION TO DESCENDANTS ...
                        if ((graph_node1.isTerminal()) && (nt.getVerbosity() > 0)) {
                            System.err.println
                                    ("org.jscience.ml.tigerxml.NT: WARNING: " +
                                            " cannot add " +
                                            graph_node1.getId() +
                                            " as secondary mother because it's a terminal. Skipping ...");
                        } else {
                            NT nt1 = (NT) graph_node1;
                            nt.setSecMother
                                    (nt1, elem2.getAttribute("label"));
                            nt1.addSecDaughter(nt);
                        }
                    }
                } // if elem2 has attribute label and idref
                else {
                    if (elem2.hasAttribute("idref")) {
                        System.err.println("org.jscience.ml.tigerxml.NT: "
                                + "An edge with no LABEL value reached. "
                                + "Skipping ...");
                    } else {
                        System.err.println("org.jscience.ml.tigerxml.NT: "
                                + "An edge with no IDREF value reached. "
                                + "Skipping ...\n");
                    }
                }
            } // for j
        } // if elem has attribute cat and id
        else {
            if (ntElement.hasAttribute("id")) {
                System.err.println
                        ("org.jscience.ml.tigerxml.NT: WARNING: The non-terminal node " +
                                ntElement.getAttribute("id") +
                                "has no CAT value. Skipping ...");
            } else {
                System.err.println
                        ("org.jscience.ml.tigerxml.NT: A non-terminal node with no ID value reached. "
                                + "Skipping ...");
            }
        } // else
    } // buildNT()

}
