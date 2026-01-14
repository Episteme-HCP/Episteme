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

package org.jscience.ml.tigerxml.theories.hobbs78;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.Sentence;
import org.jscience.ml.tigerxml.T;
import org.jscience.ml.tigerxml.tools.IndexFeatures;
import org.jscience.ml.tigerxml.tools.SyntaxTools;

import java.util.ArrayList;

/**
 * This class provides a set of static functions as defined in Hobbs 1978.
 *
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84
 *          $Id: Hobbs78.java,v 1.2 2007-10-21 17:40:33 virtualcall Exp $
 */
public class Hobbs78 {

    /**
     * This method implements Hobbs'
     * algorithm for finding an antecedent for a
     * pronoun.<p>
     * <p/>
     * Hobbs' Algorithm (adapted to Negra Syntax)<p>
     * <p/>
     * (1) Begin at any terminal tagged PPER.<p>
     * (2) Go up the tree to the first NP-like or S-like
     * node encountered. Call this node X, and the path
     * used to reach it p.<p>
     * (3) Traverse all branches below node X to the
     * left of path p in a
     * subject-first-then-left-to-right and
     * breadth-first fashion. Take into account any
     * NP-like item encountered which has an NP-like
     * or S-like node between it and X.
     * "To take into account" here means that the
     * algorithm will return the item in question
     * as the antecedent if it matches the
     * pronoun in its index features.<p>
     * (4) If node X is the highest S-like node in the
     * sentence, traverse the surface parse trees
     * of previous sentences in the text in order
     * of recency, the most recent first; each tree is
     * traversed in a subject-first-then-left-to-right,
     * breadth-first manner, and when an NP-like item is
     * encountered, it is taken into account. If X is
     * not the highest S-like node in the sentence,
     * continue to step 5.<p>
     * (5) From node X, go up the tree to the first
     * NP-like or S-like node encountered. Call this
     * new node X, and call the path traversed to reach
     * it p.<p>
     * (6) If X is an NP-like node, take X into account.<p>
     * (7) Traverse all branches below node X to the
     * left of path p in a
     * subject-first-then-left-to-right,
     * breadth-first manner. Take into account any
     * NP-like item encountered.<p>
     * (8) If X is an S-like node, traverse all branches
     * of node X to the right of path p in a
     * subject-first-then-left-to-right and breath-first
     * manner, but do not go below any NP-like
     * or S-like node encountered. Propose any NP-like
     * item encountered as the antecedent.<p>
     * (9) Go to step 4.<p>
     *
     * @param poss_pron The GraphNode for which to find the antecedent.
     * @return The GraphNode which is considered the antecedent.
     */
    public static final GraphNode hobbsSearch
            (GraphNode poss_pron) {
        GraphNode result = null;
        boolean is_third_person =
                (new IndexFeatures(poss_pron))
                        .isThirdPerson();
        if ((poss_pron.isTerminal()) &&
                (is_third_person)) {
            T pronoun = (T) poss_pron;
            if (SyntaxTools.isPronoun(pronoun)) { //step 1
                if (!(isBelowNpOrSLike(pronoun))) {
                    result = searchPreviousSentences(pronoun);
                } else {
                    MotherAndPath m_p =
                            MotherAndPath.getFirstNpOrSLike
                                    (pronoun); // step 2
                    NT mother = m_p.getMother();
                    boolean left = true;
                    ArrayList left_daughters =
                            m_p.getOneSide(left);
                    boolean np_or_s_between = true;
                    boolean go_below = true;
                    BreadthFirstSearcher searcher = // step 3
                            new BreadthFirstSearcher(pronoun,
                                    mother,
                                    left_daughters,
                                    np_or_s_between,
                                    go_below);
                    result = searcher.searchAntecedent();
                    boolean cont = (result == null);
                    while (cont) {
                        if (!isBelowNpOrSLike(mother)) { // step 4
                            result =
                                    searchPreviousSentences(pronoun);
                            cont = false;
                        } else { // mother is located
                            // below an np or s like
                            // node
                            MotherAndPath m_p2 =
                                    MotherAndPath.getFirstNpOrSLike
                                            (mother); //step 5
                            mother = m_p2.getMother();
                            GraphNode my_next_path = m_p2.getPath();
                            boolean np_like =
                                    SyntaxTools.isNpLikeNode(mother);
                            boolean s_like =
                                    SyntaxTools.isSLikeNode(mother);
                            if ((np_like) &&
                                    (IndexFeatures.
                                            indexFeaturesMatch
                                            (mother, pronoun))) {
                                result = mother; //step 6
                            } else {
                                boolean left3 = true;
                                ArrayList daughters3 =
                                        m_p2.getOneSide(left3);
                                boolean np_or_s_between3 = false;
                                boolean go_below3 = true;
                                BreadthFirstSearcher searcher3 =
                                        new BreadthFirstSearcher
                                                (pronoun, // step 7
                                                        mother,
                                                        daughters3,
                                                        np_or_s_between3,
                                                        go_below3);
                                result = searcher3.searchAntecedent();
                            }
                            if (s_like && (result == null)) {
                                boolean left4 = false;
                                ArrayList daughters4 =
                                        m_p2.getOneSide(left4);
                                boolean np_or_s_between4 = false;
                                boolean go_below4 = true;
                                BreadthFirstSearcher searcher4 =
                                        new BreadthFirstSearcher
                                                (pronoun,
                                                        mother, //step 8
                                                        daughters4,
                                                        np_or_s_between4,
                                                        go_below4);
                                result = searcher4.searchAntecedent();
                            } // if s like and result == null
                            cont = (result == null);
                        } // else mother below an np or s like
                    } // while
                } // else pronoun below np or s lik
            } // if it is a pronoun
        } // if it is a terminal
        return result;
    } // method hobbsSearch

    private static GraphNode searchPreviousSentences
            (T pronoun) {
        boolean cont = true;
        GraphNode result = null;
        Sentence sent = pronoun.getSentence();
        while (cont) {
            Sentence prev_sent =
                    sent.getPrevSentence();
            if (!(prev_sent == null)) {
                if (prev_sent.hasRootNT()) {
                    NT mother = prev_sent.getRootNT();
                    ArrayList daughters =
                            mother.getDaughters();
                    boolean np_or_s_between = false;
                    boolean go_below = true;
                    BreadthFirstSearcher searcher =
                            new BreadthFirstSearcher
                                    (pronoun,
                                            mother,
                                            daughters,
                                            np_or_s_between,
                                            go_below);
                    result = searcher.searchAntecedent();
                    cont = (result == null);
                } //
                else { // prev sent has no root
                    cont = true;
                }
            } else { // previous sent is null
                cont = false;
            }
            sent = prev_sent;
        } // while loop
        return result;
    }

    private static boolean isBelowNpOrSLike
            (GraphNode node) {
        if (!(node.hasMother())) {
            return false;
        } else {
            NT mother = node.getMother();
            while (mother != null) {
                if (org.jscience.ml.tigerxml.tools.SyntaxTools.
                        isNpLikeNode(mother) ||
                        org.jscience.ml.tigerxml.tools.SyntaxTools.
                                isSLikeNode(mother)) {
                    return true;
                }
                mother = mother.getMother();
            } //while
            return false;
        } // else
    } // method isBelowNpOrSLike
} // class
