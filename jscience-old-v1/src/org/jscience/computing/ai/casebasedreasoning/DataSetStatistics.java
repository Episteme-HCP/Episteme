package org.jscience.computing.ai.casebasedreasoning;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This is a collection of TraitStatistics objects
 * <p/>
 * There is one TraitStatistics object per Trait
 * <p/>
 * (the name is plural because each trait has multiple stats)
 * <p/>
 * <p/>
 * <p/>
 * DataSetStatistics builds the collection of TraitStatistics
 * <p/>
 * and is responsible for converting everything to floating
 * <p/>
 * points, which is what TraitStatistics wants
 * <p/>
 * Strings and booleans aren't really processed. Just set
 * <p/>
 * the max and min to 1 and 0
 *
 * @author <small>baylor</small>
 */
public class DataSetStatistics {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    HashMap dataSetStatistics; //--- collection of TraitStatistics

    //-----------------------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------------------
    protected DataSetStatistics(Items items) {
        createCollection(items.getTraitDescriptors());

        buildStatistics(items);
    } //--- constructor

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    private void add(TraitStatistics traitStatistics) {
        String key = traitStatistics.getTraitName();

        dataSetStatistics.put(key, traitStatistics);
    } //--- add

    private void buildStatistics(Items items) {
        Iterator itemList = items.iterator();

        while (itemList.hasNext()) {
            Item item = (Item) itemList.next();

            measureItemTraits(item);
        } //--- hasNext
    } //--- buildStatistics

    private void createCollection(TraitDescriptors descriptors) {
        this.dataSetStatistics = new HashMap();

        Iterator cursor = descriptors.iterator();

        while (cursor.hasNext()) {
            TraitDescriptor descriptor = (TraitDescriptor) cursor.next();

            String traitName = descriptor.getName();

            TraitStatistics traitStats = new TraitStatistics(traitName);

            //--- We can pretty much skip any traits that aren't numeric
            //--- We'll create a stats object but make it binary -
            //---   min=0, max=1, a value either matches exactly or it don't
            if ((descriptor.getDataType() != TraitDescriptor.TYPE_FLOAT) &&
                    (descriptor.getDataType() != TraitDescriptor.TYPE_INTEGER)) {
                traitStats.setMinimumValue(0);

                traitStats.setMaximumValue(1);
            }

            this.add(traitStats);
        } //--- while hasNext
    } //--- createCollection

    private void measureItemTraits(Item item) {
        Iterator traitList = item.iterator();

        while (traitList.hasNext()) {
            Trait trait = (Trait) traitList.next();

            String traitName = trait.getName();

            int dataType = item.getTraitDataType(traitName);

            //--- We're only going to measure stats for numbers
            //--- When we initialized the data set statistics collection,
            //---   we set the stats for all strings and booleans to
            //---   min=0 max=1, which is the only values we'd ever
            //---   recognize, so we can just ignore strings and bools here
            if ((dataType == TraitDescriptor.TYPE_FLOAT) ||
                    (dataType == TraitDescriptor.TYPE_INTEGER)) {
                TraitStatistics traitStats = this.get(traitName);

                float value = trait.getValue().toFloat();

                traitStats.addExample(value);
            }
        } //--- hasNext
    } //--- measureItemTraits

    //-----------------------------------------------------------------------------
    // Accessors
    //-----------------------------------------------------------------------------
    public TraitStatistics get(String traitName) {
        return (TraitStatistics) dataSetStatistics.get(traitName);
    } //--- get
} //--- DataSetStatistics
