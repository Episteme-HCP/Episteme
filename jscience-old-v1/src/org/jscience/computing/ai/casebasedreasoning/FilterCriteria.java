package org.jscience.computing.ai.casebasedreasoning;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <small>baylor</small>
 */
public class FilterCriteria {
    //--- Here's where we hold all our data
    private ArrayList data = new ArrayList();

    /**

     *

     */
    protected void add(String field, String operator, String value) {
        FilterCriterion criterion = new FilterCriterion(field, operator, value);

        data.add(criterion);
    } //--- add

    /**
     * This has to return Object because that's how
     * <p/>
     * it's defined in the super class
     */
    public Object clone() {
        FilterCriteria newFilterCriteria = new FilterCriteria();

        newFilterCriteria.setValues(data);

        return (Object) newFilterCriteria;
    } //--- clone

    /**

     *

     */
    public Iterator iterator() {
        return data.iterator();
    }

    private void setValues(ArrayList newValues) {
        data = (ArrayList) newValues.clone();
    }

    /**

     *

     */
    public int size() {
        return data.size();
    }
} //--- FilterCriteria
