package org.jscience.computing.ai.casebasedreasoning;

/**
 * @author <small>baylor</small>
 */
public class SimilarityDescription implements Comparable {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    private Item itemBeingDescribed;
    private int rank;
    private float percentMatch;

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------

    /**
     * Implements the compareTo method required by the Comparable interface
     * <p/>
     * We're gonna compare items based on their percent similarity
     * <p/>
     * Used by SimilarItems.rankItems()
     * <p/>
     * The stupid argument name (o) is Sun's fault, not mine
     * <p/>
     * <p/>
     * <p/>
     * Return codes:
     * <p/>
     * me > arg = -1  (better % match means we go first)
     * <p/>
     * me = arg =  0
     * <p/>
     * me < arg = +1  (it's a better match, it goes first)
     */
    public int compareTo(Object o) {
        //--- If this fails, it'll throw a ClassCastException, which we
        //---   expect the idiot who pass us this argument to handle
        SimilarityDescription arg = (SimilarityDescription) o;

        int result = 0; //--- default to being equal

        if (this.getPercentSimilarity() < arg.getPercentSimilarity()) {
            result = 1;
        }

        if (this.getPercentSimilarity() > arg.getPercentSimilarity()) {
            result = -1;
        }

        return result;
    } //--- compareTo

    //-----------------------------------------------------------------------------
    // Accessors
    //-----------------------------------------------------------------------------
    public Item getItem() {
        return itemBeingDescribed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRank() {
        return rank;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPercentSimilarity() {
        return percentMatch;
    }

    protected void setItem(Item newItem) {
        itemBeingDescribed = newItem;
    }

    protected void setPercentSimilarity(float newPercentMatch) {
        percentMatch = newPercentMatch;
    }

    protected void setRank(int newRank) {
        rank = newRank;
    }
} //--- SimilarityDescription
