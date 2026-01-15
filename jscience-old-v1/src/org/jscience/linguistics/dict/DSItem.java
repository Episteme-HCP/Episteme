package org.jscience.linguistics.dict;

/**
 * Informace o databazi nebo strategii vyhledavani na dict serveru
 *
 * @author Stepan Bechynsky
 * @author bechynsky@bdflow.com
 * @author BD Flow, a. s.
 * @version 0.1
 */
public class DSItem {
    /**
     * DOCUMENT ME!
     */
    private String name = new String(); // jmeno

    /**
     * DOCUMENT ME!
     */
    private String info = new String(); // popis

/**
     * Creates a new DSItem object.
     *
     * @param name DOCUMENT ME!
     * @param info DOCUMENT ME!
     */
    public DSItem(String name, String info) {
        this.name = name;
        this.info = info;
    } // END: DSItem(String name, String info)

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return info;
    } // END: toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    } // END: getName()
}
