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
package org.jscience.linguistics.loaders.tigerxml.theories.hobbs78;

import org.jscience.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.linguistics.loaders.tigerxml.NT;
import org.jscience.linguistics.loaders.tigerxml.T;
import org.jscience.linguistics.loaders.tigerxml.tools.GeneralTools;
import org.jscience.linguistics.loaders.tigerxml.tools.IndexFeatures;
import org.jscience.linguistics.loaders.tigerxml.tools.SyncMMAX;
import org.jscience.linguistics.loaders.tigerxml.tools.SyntaxTools;

import java.util.ArrayList;
import java.util.List;


/**
 * Helper class for Hobbs78 pronoun resolution algorithm.
 * Searches breadth-first, subject-first, then left-to-right.
 */
public class BreadthFirstSearcher {
    private final T pronoun;
    private final NT mother;
    private final List<GraphNode> agenda;
    private int pointer;
    private final boolean np_or_s_between;
    private final boolean go_below;

    /**
     * Creates a new BreadthFirstSearcher object.
     */
    public BreadthFirstSearcher(T my_pronoun, NT my_mother, List<GraphNode> nodes,
        boolean my_np_or_s_between, boolean my_go_below) {
        this.mother = my_mother;
        this.agenda = new ArrayList<>();

        if (nodes != null) {
            this.agenda.addAll(nodes);
        }

        List<GraphNode> sorted = GeneralTools.sortNodes(agenda);
        this.agenda.clear();
        this.agenda.addAll(sorted);
        
        this.pointer = 0;
        this.go_below = my_go_below;
        this.np_or_s_between = my_np_or_s_between;
        this.pronoun = my_pronoun;
    }

    private GraphNode getNext() {
        GraphNode next_node = agenda.get(pointer);
        pointer++;

        if (!next_node.isTerminal()) {
            NT non_terminal = (NT) next_node;
            boolean neither_np_nor_s = !SyntaxTools.isNpLikeNode(non_terminal) &&
                                       !SyntaxTools.isSLikeNode(non_terminal);
            
            List<GraphNode> daughters = new ArrayList<>(non_terminal.getDaughters());
            daughters = GeneralTools.sortNodes(daughters);
            orderSubjectFirst(daughters);

            if (go_below || neither_np_nor_s) {
                agenda.addAll(daughters);
            }
        }

        return next_node;
    }

    private boolean hasNext() {
        return pointer < agenda.size();
    }

    private static void orderSubjectFirst(List<GraphNode> list) {
        for (int i = 0; i < list.size(); i++) {
            GraphNode next_node = list.get(i);
            String edge = next_node.getEdge2Mother();

            if ("SB".equals(edge)) {
                list.remove(i);
                list.add(0, next_node);
            }
        }
    }

    public GraphNode searchAntecedent() {
        while (hasNext()) {
            GraphNode next = getNext();

            if (SyncMMAX.isMarkable(next)) {
                if (IndexFeatures.indexFeaturesMatch(next, pronoun)) {
                    boolean between_ok = true;
                    if (np_or_s_between) {
                        between_ok = npOrSLikeBetween(next, mother);
                    }
                    if (between_ok) {
                        return next;
                    }
                }
            }
        }
        return null;
    }

    private static boolean npOrSLikeBetween(GraphNode lower, NT higher) {
        if (lower == null || higher == null) return false;
        NT currentMother = lower.getMother();

        while (currentMother != null) {
            if (currentMother.equals(higher)) {
                return false;
            }
            if (SyntaxTools.isNpLikeNode(currentMother) || SyntaxTools.isSLikeNode(currentMother)) {
                return true;
            }
            currentMother = currentMother.getMother();
        }
        return false;
    }
}
