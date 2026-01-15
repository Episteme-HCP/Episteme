package org.jscience.computing.ai.casebasedreasoning;

/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class QueryManager {
    /** DOCUMENT ME! */
    FilterCriteria filterCriteria;

    /** DOCUMENT ME! */
    SimilarityCriteria similarityCriteria;

    /** DOCUMENT ME! */
    SimilarityWeights similarityWeights;

/**
     * Creates a new QueryManager object.
     */
    public QueryManager() {
        QueryReader queryReader = new QueryReader();

        filterCriteria = queryReader.getFilterCriteria();

        similarityCriteria = queryReader.getSimilarityCriteria();

        similarityWeights = queryReader.getSimilarityWeights();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FilterCriteria getFilterCriteria() {
        return filterCriteria;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarityCriteria getSimilarityCriteria() {
        return similarityCriteria;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarityWeights getSimilarityWeights() {
        return similarityWeights;
    }
} //--- QueryManager
