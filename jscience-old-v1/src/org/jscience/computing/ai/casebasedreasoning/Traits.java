package org.jscience.computing.ai.casebasedreasoning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * @author baylor
 */
public class Traits {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    private HashMap traits = new HashMap();

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------
    protected void add(Trait newTrait) {
        String key = newTrait.getName();

        traits.put(key, newTrait);
    }

    /**
     * DOCUMENT ME!
     *
     * @param traitName DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Trait get(String traitName) {
        return (Trait) traits.get(traitName);
    } //--- get

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return traits.isEmpty();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return traits.values().iterator();
    }

    /**
     * Load data from a string
     * <p/>
     * The string will have the data fields in the same
     * <p/>
     * order as the array of field definitions
     * <p/>
     * The string is | delimited
     */
    protected void loadFromDelimitedString(TraitDescriptors traitDescriptors,
                                           String encodedData) {
        try {
            int fieldNumber = 0;

            //--- Break the string into pieces
            StringTokenizer st = new StringTokenizer(encodedData, "|");

            //--- Get each field of data
            while (st.hasMoreTokens()) {
                String key = traitDescriptors.get(fieldNumber).toString();

                String value = st.nextToken().trim();

                Trait trait = new Trait(key, value);

                add(trait);

                fieldNumber++;
            } //--- while hasMoreTokens
        } //--- try
        catch (Exception e) {
            e.printStackTrace();
        } //--- catch
    } //--- loadFromDelimitedString
} //--- Traits
