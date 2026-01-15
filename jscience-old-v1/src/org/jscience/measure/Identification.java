package org.jscience.measure;

/**
 * A class representing an abstract way to identify something
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//You can spot that many classes require an identification, which is defined as String (see StringIdentificationFactory).
//We do not offer better support because... there isn't any.
//Usually each country or company provides its own internal schema to decide what is a valid or invalid identification. 
//Books (ISBN), other goods (ASIN), banks accounts, companies have some sort of international conventions but which have many exceptions.
//should be renamed Identifiable
public abstract class Identification extends Object implements java.io.Serializable {
    //a String that describes the common name for that identification scheme
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String getDescription();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String toString();

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean equals(Object object);
}
