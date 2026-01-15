/*
 * Data class to store Unifac group data.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor;

/**
 * Data class to store Unifac group data.
 */
public class GroupData {
    /**
     * DOCUMENT ME!
     */
    public String groupName = null;

    /**
     * DOCUMENT ME!
     */
    public int uniGroupNo = 0;

    /**
     * DOCUMENT ME!
     */
    public int k = 0;

    /**
     * DOCUMENT ME!
     */
    public double qk = 0;

    /**
     * DOCUMENT ME!
     */
    public double rk = 0;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return groupName;
    }
}
