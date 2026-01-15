package org.jscience.computing.ai.casebasedreasoning;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class SimilarityWeight {
    /** DOCUMENT ME! */
    private String fieldName;

    /** DOCUMENT ME! */
    private Integer weight;

/**
     * Creates a new SimilarityWeight object.
     *
     * @param fieldName DOCUMENT ME!
     * @param weight    DOCUMENT ME!
     */
    protected SimilarityWeight(String fieldName, Integer weight) {
        this.fieldName = fieldName;

        this.weight = weight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer getWeight() {
        return weight;
    }
} //--- SimilarityWeight
