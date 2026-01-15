package org.jscience.computing.ai.casebasedreasoning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author baylor
 */
public class TraitDescriptors {
    //--- Should probably have the trait and trait descriptors
    //---   act the same for get's and iterator
    /** DOCUMENT ME! */
    ArrayList traitDescriptors = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param descriptor DOCUMENT ME!
     */
    private void add(int index, TraitDescriptor descriptor) {
        traitDescriptors.add(index, descriptor);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return traitDescriptors.isEmpty();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        //--- Returns an iterator to an array list containing
        //---   TraitDescriptor objects
        return traitDescriptors.iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TraitDescriptor get(int index) {
        return (TraitDescriptor) traitDescriptors.get(index);
    } //--- get

    /**
     * DOCUMENT ME!
     *
     * @param traitName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TraitDescriptor get(String traitName) {
        TraitDescriptor matchingDescriptor = null;

        Iterator cursor = traitDescriptors.iterator();

        while (cursor.hasNext()) {
            TraitDescriptor traitDescriptor = (TraitDescriptor) cursor.next();

            if (traitDescriptor.getName().equals(traitName)) {
                matchingDescriptor = traitDescriptor;
            }
        } //--- hasNext

        return matchingDescriptor;
    } //--- getTraitDescriptor

    /**
     * DOCUMENT ME!
     *
     * @param traitName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDataType(String traitName) {
        TraitDescriptor traitDescriptor = get(traitName);

        return traitDescriptor.getDataType();
    } //--- getDataType

    /**
     * DOCUMENT ME!
     *
     * @param encodedData DOCUMENT ME!
     */
    protected void loadFromDelimitedString(String encodedData) {
        try {
            int entryNumber = 0;

            //--- Capture all the fields that the user wants to define
            StringTokenizer st = new StringTokenizer(encodedData, "|");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();

                TraitDescriptor traitDescriptor = new TraitDescriptor(token);

                add(entryNumber, traitDescriptor);

                entryNumber++;
            } //--- while st.hasMoreTokens
        } //--- try
        catch (Exception e) {
            String methodName = "TraitDescriptors::loadFromDelimitedString";

            System.out.println(methodName + " error: " + e);
        } //--- catch
    } //--- loadFromDelimitedString

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return traitDescriptors.size();
    } //--- size
} //--- TraitDescriptors
