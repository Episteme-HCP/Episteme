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

package org.jscience.social.linguistics.loaders.tigerxml.theories.hobbs78;

import org.jscience.social.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.social.linguistics.loaders.tigerxml.NT;
import org.jscience.social.linguistics.loaders.tigerxml.Sentence;
import org.jscience.social.linguistics.loaders.tigerxml.T;
import org.jscience.social.linguistics.loaders.tigerxml.tools.IndexFeatures;
import org.jscience.social.linguistics.loaders.tigerxml.tools.SyntaxTools;

import java.util.List;

/**
 * Implementation of Hobbs' 1978 algorithm for finding an antecedent for a pronoun.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Hobbs78 {

    /**
     * Hobbs' Algorithm (adapted to Tiger/Negra Syntax)
     */
    public static final GraphNode hobbsSearch(GraphNode poss_pron) {
        if (poss_pron == null) return null;
        
        boolean is_third_person = new IndexFeatures(poss_pron).isThirdPerson();
        if (poss_pron.isTerminal() && is_third_person) {
            T pronoun = (T) poss_pron;
            if (SyntaxTools.isPronoun(pronoun)) {
                if (!isBelowNpOrSLike(pronoun)) {
                    return searchPreviousSentences(pronoun);
                } else {
                    MotherAndPath m_p = MotherAndPath.getFirstNpOrSLike(pronoun);
                    NT mother = m_p.getMother();
                    if (mother == null) return searchPreviousSentences(pronoun);
                    
                    List<GraphNode> left_daughters = m_p.getOneSide(true);
                    BreadthFirstSearcher searcher = new BreadthFirstSearcher(pronoun, mother, left_daughters, true, true);
                    GraphNode result = searcher.searchAntecedent();
                    
                    while (result == null) {
                        if (!isBelowNpOrSLike(mother)) {
                            return searchPreviousSentences(pronoun);
                        } else {
                            MotherAndPath m_p2 = MotherAndPath.getFirstNpOrSLike(mother);
                            mother = m_p2.getMother();
                            if (mother == null) return searchPreviousSentences(pronoun);
                            
                            if (SyntaxTools.isNpLikeNode(mother) && IndexFeatures.indexFeaturesMatch(mother, pronoun)) {
                                return mother;
                            } else {
                                List<GraphNode> daughters3 = m_p2.getOneSide(true);
                                BreadthFirstSearcher searcher3 = new BreadthFirstSearcher(pronoun, mother, daughters3, false, true);
                                result = searcher3.searchAntecedent();
                            }
                            
                            if (SyntaxTools.isSLikeNode(mother) && result == null) {
                                List<GraphNode> daughters4 = m_p2.getOneSide(false);
                                BreadthFirstSearcher searcher4 = new BreadthFirstSearcher(pronoun, mother, daughters4, false, true);
                                result = searcher4.searchAntecedent();
                            }
                        }
                    }
                    return result;
                }
            }
        }
        return null;
    }

    private static GraphNode searchPreviousSentences(T pronoun) {
        Sentence currentSent = pronoun.getSentence();
        if (currentSent == null) return null;
        
        Sentence prev_sent = currentSent.getPrevSentence();
        while (prev_sent != null) {
            if (prev_sent.hasRootNT()) {
                NT root = prev_sent.getRootNT();
                List<GraphNode> daughters = root.getDaughters();
                BreadthFirstSearcher searcher = new BreadthFirstSearcher(pronoun, root, daughters, false, true);
                GraphNode result = searcher.searchAntecedent();
                if (result != null) return result;
            }
            prev_sent = prev_sent.getPrevSentence();
        }
        return null;
    }

    private static boolean isBelowNpOrSLike(GraphNode node) {
        if (node == null) return false;
        NT mother = node.getMother();
        while (mother != null) {
            if (SyntaxTools.isNpLikeNode(mother) || SyntaxTools.isSLikeNode(mother)) {
                return true;
            }
            mother = mother.getMother();
        }
        return false;
    }
}

