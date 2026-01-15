package org.jscience.computing.ai.casebasedreasoning;

import java.util.HashMap;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class SimilarityWeights {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private HashMap weights = new HashMap();

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param fieldName DOCUMENT ME!
     * @param weight DOCUMENT ME!
     */
    protected void add(String fieldName, int weight) {
        //--- HashMaps require objects, no intrinsic data types
        //--- So we gotta convert int to Integer to store it
        Integer weightAsObject = new Integer(weight);

        weights.put(fieldName, weightAsObject);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        SimilarityWeights newItems = new SimilarityWeights();

        newItems.setValues(weights);

        return (Object) newItems;
    } //--- clone

    /**
     * DOCUMENT ME!
     *
     * @param traitName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int get(String traitName) {
        Integer value = (Integer) weights.get(traitName);

        return value.intValue();
    } //--- get

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return weights.values().iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator mapIterator() {
        return weights.entrySet().iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @param newValues DOCUMENT ME!
     */
    private void setValues(HashMap newValues) {
        weights = (HashMap) newValues.clone();
    } //--- setValues

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return weights.size();
    } //--- size
} //--- SimilarityWeights
