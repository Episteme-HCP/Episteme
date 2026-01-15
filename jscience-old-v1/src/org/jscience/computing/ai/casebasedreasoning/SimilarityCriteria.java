package org.jscience.computing.ai.casebasedreasoning;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class SimilarityCriteria {
    /** DOCUMENT ME! */
    private ArrayList criteria = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param criterion DOCUMENT ME!
     */
    protected void add(SimilarityCriterion criterion) {
        criteria.add(criterion);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @param fieldName DOCUMENT ME!
     * @param operator DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    protected void add(String fieldName, String operator, String value) {
        SimilarityCriterion criterion = new SimilarityCriterion(fieldName,
                operator, value);

        this.add(criterion);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        SimilarityCriteria newItems = new SimilarityCriteria();

        newItems.setValues(criteria);

        return (Object) newItems;
    } //--- clone

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return criteria.iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @param newValues DOCUMENT ME!
     */
    private void setValues(ArrayList newValues) {
        criteria = (ArrayList) newValues.clone();
    } //--- setValues

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return criteria.size();
    } //--- size
} //--- SimilarityCriteria
