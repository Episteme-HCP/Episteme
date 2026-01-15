package org.jscience.util;

/**
 * A class representing an element of a relation. This is also named a
 * tuple in the computer world.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class NAry extends Object {
    /** DOCUMENT ME! */
    private Object[] elems;

    //not null or empty although each of the Objects in elem can be null
/**
     * Creates a new NAry object.
     *
     * @param elems DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public NAry(Object[] elems) {
        if ((elems != null) && (elems.length > 0)) {
            this.elems = elems;
        } else {
            throw new IllegalArgumentException(
                "NAry doesn't accept null values or empty arrays.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return elems.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getValue() {
        return elems;
    }

    //wil throw an error if i<0 or i>(getDimension()-1)
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(int i) {
        return elems[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        NAry nAry;
        boolean result;
        int i;

        if (o instanceof NAry) {
            nAry = (NAry) o;

            if (nAry.getDimension() == elems.length) {
                result = true;
                i = 0;

                while ((i < elems.length) && result) {
                    result = elems[i].equals(nAry.getValue(i));
                }

                return result;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
