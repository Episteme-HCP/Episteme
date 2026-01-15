package org.jscience.computing.ai.casebasedreasoning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class SimilarItems {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    //--- Sorted list of SimilarityDescription objects
    /** DOCUMENT ME! */
    private ArrayList descriptions = new ArrayList();

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param descriptor DOCUMENT ME!
     */
    protected void add(SimilarityDescription descriptor) {
        descriptions.add(descriptor);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarityDescription getBestMatch() {
        SimilarityDescription descriptor = (SimilarityDescription) descriptions.get(0);

        return descriptor;
    } //--- getBestMatch

    /**
     * DOCUMENT ME!
     *
     * @param numberOfMatches DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarItems getBestMatches(int numberOfMatches) {
        SimilarItems subset = new SimilarItems();

        if (numberOfMatches > descriptions.size()) {
            numberOfMatches = descriptions.size();
        }

        for (int i = 0; i < numberOfMatches; i++) {
            SimilarityDescription description = (SimilarityDescription) descriptions.get(i);

            subset.add(description);
        }

        return subset;
    } //--- getBestMatches

    /**
     * DOCUMENT ME!
     *
     * @param percentSimilar DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarItems getByPercentSimilarity(float percentSimilar) {
        SimilarItems subset = new SimilarItems();

        for (int i = 0; i < descriptions.size(); i++) {
            SimilarityDescription description = (SimilarityDescription) descriptions.get(i);

            if (description.getPercentSimilarity() == percentSimilar) {
                subset.add(description);
            }
        }

        return subset;
    } //--- getByPercentSimilarity

    /**
     * DOCUMENT ME!
     *
     * @param rank DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarItems getByRank(int rank) {
        SimilarItems subset = new SimilarItems();

        for (int i = 0; i < descriptions.size(); i++) {
            SimilarityDescription description = (SimilarityDescription) descriptions.get(i);

            if (description.getRank() == rank) {
                subset.add(description);
            }
        }

        return subset;
    } //--- getByRank

    /**
     * DOCUMENT ME!
     *
     * @param rank DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarityDescription getByRelativeRank(int rank) {
        int index = rank - 1; //--- 0 based array

        SimilarityDescription descriptor = (SimilarityDescription) descriptions.get(index);

        return descriptor;
    } //--- getByRelativeRank

    /**
     * DOCUMENT ME!
     *
     * @param minimumPercentSimilar DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarItems getByThreshold(float minimumPercentSimilar) {
        SimilarItems subset = new SimilarItems();

        for (int i = 0; i < descriptions.size(); i++) {
            SimilarityDescription description = (SimilarityDescription) descriptions.get(i);

            if (description.getPercentSimilarity() >= minimumPercentSimilar) {
                subset.add(description);
            }
        }

        return subset;
    } //--- getByThreshold

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return descriptions.iterator();
    }

    /**
     * DOCUMENT ME!
     */
    public void rankItems() {
        try {
            Collections.sort(this.descriptions);

            //--- Hopefully now the index is the also the sort order of the item
            //---   so we'll use it for our ranking
            for (int i = 0; i < descriptions.size(); i++) {
                SimilarityDescription description = (SimilarityDescription) descriptions.get(i);

                int rank = i + 1; //--- +1 to compensate for 0-based arrays

                description.setRank(rank);
            } //--- for i=0 to fields.size
        } catch (Exception e) {
            System.out.println("exception: " + e);

            e.printStackTrace();
        } //--- catch
    } //--- rankItems

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return descriptions.size();
    } //--- size
} //--- SimilarItems
