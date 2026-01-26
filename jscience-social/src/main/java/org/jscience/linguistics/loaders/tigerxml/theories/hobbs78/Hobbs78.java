/*
 * Hobbs78.java
 *
 * Created on July 31, 2003, 8:47 PM
 */

package org.jscience.linguistics.loaders.tigerxml.theories.hobbs78;

import org.jscience.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.linguistics.loaders.tigerxml.NT;
import org.jscience.linguistics.loaders.tigerxml.Sentence;
import org.jscience.linguistics.loaders.tigerxml.T;
import org.jscience.linguistics.loaders.tigerxml.tools.IndexFeatures;
import org.jscience.linguistics.loaders.tigerxml.tools.SyntaxTools;

import java.util.List;

/**
 * Implementation of Hobbs' 1978 algorithm for finding an antecedent for a pronoun.
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
