package org.jscience.computing.ai.casebasedreasoning;

import java.util.Iterator;

/**
 * @author <small>baylor</small>
 */
public class Item {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    Traits traits = new Traits();
    TraitDescriptors traitDescriptors;

    //-----------------------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------------------
    protected Item(TraitDescriptors traitDescriptors) {
        this.traitDescriptors = traitDescriptors;
    }

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    public int getTraitDataType(String traitName) {
        TraitDescriptor traitDescriptor = traitDescriptors.get(traitName);

        return traitDescriptor.getDataType();
    } //--- getTraitValue

    /**
     * DOCUMENT ME!
     *
     * @param traitName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public TraitValue getTraitValue(String traitName) {
        Trait trait = traits.get(traitName);

        return trait.getValue();
    } //--- getTraitValue

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return traits.iterator();
    }

    /**
     * Assumption: good data is passed in.
     * <p/>
     * This method does not currently do any error checking like
     * <p/>
     * making sure the number of fields matches the number of
     * <p/>
     * | delimited items in the data
     * <p/>
     * Hey, it's prototype code, what do you expect?
     */
    protected void loadFromDelimitedString(TraitDescriptors traitDescriptors,
                                           String encodedData) {
        try {
            traits.loadFromDelimitedString(traitDescriptors, encodedData);
        } //--- try
        catch (Exception e) {
            e.printStackTrace();
        } //--- catch
    } //--- loadFromDelimitedString
} //--- Item
