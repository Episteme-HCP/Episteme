package org.jscience.computing.ai.casebasedreasoning;

import java.util.HashMap;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class SimilarityCriterionScores {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private HashMap scores = new HashMap();

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param score DOCUMENT ME!
     */
    public void add(SimilarityCriterionScore score) {
        this.scores.put(score.getID(), score);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @param ID DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarityCriterionScore get(String ID) {
        return (SimilarityCriterionScore) scores.get(ID);
    } //--- get

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return scores.values().iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return scores.size();
    } //--- size
}
