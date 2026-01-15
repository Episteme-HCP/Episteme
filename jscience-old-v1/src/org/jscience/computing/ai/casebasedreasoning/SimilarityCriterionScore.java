package org.jscience.computing.ai.casebasedreasoning;

/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class SimilarityCriterionScore {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private String criterionID;

    /** DOCUMENT ME! */
    private float normalizedValue;

    /** DOCUMENT ME! */
    private float weightedValue;

    //-----------------------------------------------------------------------------
    /**
     * Creates a new SimilarityCriterionScore object.
     *
     * @param criterionID DOCUMENT ME!
     */
    protected SimilarityCriterionScore(String criterionID) {
        setID(criterionID);
    }

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getNormalizedValue() {
        return normalizedValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    protected void setNormalizedValue(float value) {
        this.normalizedValue = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getID() {
        return criterionID;
    }

    /**
     * DOCUMENT ME!
     *
     * @param criterionID DOCUMENT ME!
     */
    protected void setID(String criterionID) {
        this.criterionID = criterionID;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWeightedValue() {
        return weightedValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    protected void setWeightedValue(float value) {
        this.weightedValue = value;
    }
} //--- SimilarityCriterionScore
