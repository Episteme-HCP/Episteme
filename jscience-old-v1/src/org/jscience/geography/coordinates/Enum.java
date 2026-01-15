package org.jscience.geography.coordinates;

/**
 * The base enumeration from which all enumeration classes are inherited.
 *
 * @author David Shen
 */
public abstract class Enum {
    /** DOCUMENT ME! */
    private String _enumStr;

    /** DOCUMENT ME! */
    private int _enumInt;

/**
     * Creates a new Enum object.
     *
     * @param enumInt DOCUMENT ME!
     * @param enumStr DOCUMENT ME!
     */
    protected Enum(int enumInt, String enumStr) {
        _enumInt = enumInt;
        _enumStr = new String(enumStr);
    }

    /// Returns the string for the enumerant name
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new String(_enumStr);
    }

    /// returns the integer code value for the enumerant
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int toInt() {
        return _enumInt;
    }
}
