/*
 * BreadthFirstSearcher.java
 *
 * Created in September, 2003
 *
 * Copyright (C) 2003 Hajo Keffer <hajokeffer@coli.uni-sb.de>
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
package org.jscience.ml.tigerxml.theories.hobbs78;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.T;
import org.jscience.ml.tigerxml.tools.GeneralTools;
import org.jscience.ml.tigerxml.tools.IndexFeatures;
import org.jscience.ml.tigerxml.tools.SyncMMAX;
import org.jscience.ml.tigerxml.tools.SyntaxTools;

import java.util.ArrayList;


/**
 * This class is a helper class for Hobbs78. A BreadthFirstSearcher
 * searches for a GraphNode that is the possible antecedent of a pronoun. It
 * searches recursively through a list of Graphnodes and through all children
 * it encounters. It does so in a breadth-first and subject-first then
 * left-to-right fashion. Parameters: my_pronoun is the pronoun whose
 * antecedent is searched my_mother is an NT that is supposed to be the mother
 * of nodes nodes are the nodes to be searched np_or_s_between if set to true
 * then there must be a np or s-like node between a node and mother for the
 * node to be returned go_below specifies whether it will be searched below np
 * or s-like nodes encountered
 *
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84 $Id: BreadthFirstSearcher.java,v 1.3 2007-10-23 18:21:43 virtualcall Exp $
 */
public class BreadthFirstSearcher {
    /** DOCUMENT ME! */
    private T pronoun;

    /** DOCUMENT ME! */
    private NT mother;

    /** DOCUMENT ME! */
    private ArrayList agenda;

    /** DOCUMENT ME! */
    private int pointer;

    /** DOCUMENT ME! */
    private boolean np_or_s_between;

    /** DOCUMENT ME! */
    private boolean go_below;

/**
     * Creates a new BreadthFirstSearcher object.
     *
     * @param my_pronoun         DOCUMENT ME!
     * @param my_mother          DOCUMENT ME!
     * @param nodes              DOCUMENT ME!
     * @param my_np_or_s_between DOCUMENT ME!
     * @param my_go_below        DOCUMENT ME!
     */
    public BreadthFirstSearcher(T my_pronoun, NT my_mother, ArrayList nodes,
        boolean my_np_or_s_between, boolean my_go_below) {
        this.mother = my_mother;
        this.agenda = new ArrayList();

        for (int i = 0; i < nodes.size(); i++) {
            Object next_item = nodes.get(i);
            agenda.add(next_item);
        }

        agenda = GeneralTools.sortNodes(agenda);
        this.pointer = 0;
        this.go_below = my_go_below;
        this.np_or_s_between = my_np_or_s_between;
        this.pronoun = my_pronoun;
    } // constructor BreadthFirstSeacher

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private GraphNode getNext() {
        GraphNode next_node = (GraphNode) agenda.get(pointer);
        pointer++;

        if (!(next_node.isTerminal())) {
            NT non_terminal = (NT) next_node;
            boolean neither_np_nor_s = ((!(SyntaxTools.isNpLikeNode(non_terminal))) &&
                (!(SyntaxTools.isSLikeNode(non_terminal))));
            ArrayList daughters = non_terminal.getDaughters();
            daughters = GeneralTools.sortNodes(daughters);
            orderSubjectFirst(daughters);

            if ((go_below) || (neither_np_nor_s)) {
                for (int i = 0; i < daughters.size(); i++) {
                    Object next_daughter = daughters.get(i);
                    agenda.add(next_daughter);
                } // for i
            } // if
        } // if

        return next_node;
    } // method getNext

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean hasNext() {
        return pointer < agenda.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param list DOCUMENT ME!
     */
    private static void orderSubjectFirst(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            GraphNode next_node = (GraphNode) list.get(i);
            String edge = next_node.getEdge2Mother();

            if (edge.equals("SB")) {
                list.remove(i);
                list.add(0, next_node);
            }
        } // for i
    } // method orderSubjectFirst

    /**
     * Returns the GraphNode which is likely to be the antecedent
     * according to the setting of the BreadthFirstSearcher object, null if no
     * such node could be found
     *
     * @return The GraphNode which is likely to be the antecedent or null.
     */
    public GraphNode searchAntecedent() {
        GraphNode return_value = null;
        boolean cont = this.hasNext();

        while (cont) {
            GraphNode next = this.getNext();

            if (SyncMMAX.isMarkable(next)) {
                boolean match = IndexFeatures.indexFeaturesMatch(next,
                        this.pronoun);

                if (match) {
                    boolean between_ok = true;

                    if (this.np_or_s_between) {
                        between_ok = npOrSLikeBetween(next, this.mother);
                    }

                    if (between_ok) {
                        return_value = next;
                    } // if between_ok
                } // if match
            } // if is Markable

            if ((!(this.hasNext())) || (!(return_value == null))) {
                cont = false;
            }
        } //while

        return return_value;
    } // method SearchAntecedent

    /**
     * DOCUMENT ME!
     *
     * @param lower DOCUMENT ME!
     * @param higher DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean npOrSLikeBetween(GraphNode lower, NT higher) {
        NT mother = lower.getMother();
        boolean return_value = false;
        boolean cont;
        cont = true;

        while (cont) {
            if (mother == null) {
                System.err.println(
                    "org.jscience.ml.tigerxml.theories.hobbs78.BreadthFirstSearcher: " +
                    "WARNING: didn't meet higher node when calling NpOrSLikeBetween" +
                    " for Sentence(s) " + lower.getSentence() + " and " +
                    higher.getSentence());
                cont = false;
            } else if (mother.equals(higher)) {
                cont = false;
            } else if (org.jscience.ml.tigerxml.tools.SyntaxTools.isNpLikeNode(
                        mother) ||
                    org.jscience.ml.tigerxml.tools.SyntaxTools.isSLikeNode(
                        mother)) {
                cont = false;
                return_value = true;
            }

            if (cont) {
                mother = mother.getMother();
            }
        } // while

        return return_value;
    } // npOrSLikeBetween
} // class BreadthFirstSearcher
