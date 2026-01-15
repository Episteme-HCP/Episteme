/*
 * Data class to store ID-value pairs.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.util;

/**
 * Data class to store ID-value pairs.
 */
public class IDValue {
    /**
     * DOCUMENT ME!
     */
    public int ID = -1;

    /**
     * DOCUMENT ME!
     */
    public String value = null;

    /**
     * Creates a new IDValue object.
     */
    public IDValue() {
    }

    /**
     * Creates a new IDValue object.
     *
     * @param ID DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public IDValue(int ID, String value) {
        this.ID = ID;
        this.value = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return value;
    }
}
