package org.jscience.chemistry.gui.extended.beans;

import java.util.Hashtable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CentralLookup {
    /** DOCUMENT ME! */
    private static CentralLookup lookup = null;

    static {
    }

    /** DOCUMENT ME! */
    private Hashtable objectTable;

/**
     * Creates a new CentralLookup object.
     */
    CentralLookup() {
        objectTable = new Hashtable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CentralLookup getLookup() {
        if (lookup == null) {
            init();
        }

        return lookup;
    }

    /**
     * DOCUMENT ME!
     */
    private static void init() {
        lookup = new CentralLookup();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param object DOCUMENT ME!
     */
    public void addObject(String name, Object object) {
        objectTable.put(name, object);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getObject(String name) {
        return objectTable.get(name);
    }
}
