package org.jscience.computing.ai.casebasedreasoning;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class FilterEngine {
    /**
     * DOCUMENT ME!
     *
     * @param items DOCUMENT ME!
     * @param filterCriteria DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Items filterItems(Items items, FilterCriteria filterCriteria) {
        Items filteredItems = new Items(items.getTraitDescriptors());

        //--- Go through each item and see if violates any of our rules
        Iterator itemsCursor = items.iterator();

        while (itemsCursor.hasNext()) {
            Item item = (Item) itemsCursor.next();

            //--- Does this item violate any of our filter criteria?
            if (!(violatesCriteria(item, filterCriteria))) {
                //--- It didn't violate any criteria so let's keep this one
                filteredItems.add(item);
            }
        } //--- while hasNext

        return filteredItems;
    } //--- filterItems

    /**
     * DOCUMENT ME!
     *
     * @param item DOCUMENT ME!
     * @param criteria DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean violatesCriteria(Item item, FilterCriteria criteria) {
        Iterator listOfRules = criteria.iterator();

        //--- Look through each filter rule we have to see if any of them
        //---   disqualify this particular item
        while (listOfRules.hasNext()) {
            FilterCriterion criterion = (FilterCriterion) listOfRules.next();

            if (criterion.itemViolates(item)) {
                return true;
            }
        } //--- hasNext

        //--- We won't get this far in the method unless none of the criteria
        //---   were matched
        return false;
    } //--- violatesCriteria
} //--- FilterEngine
