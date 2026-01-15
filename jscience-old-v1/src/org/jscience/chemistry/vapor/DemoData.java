/*
 * Data class to store demo data.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor;

import java.util.HashMap;


/**
 * Data class to store demo data.
 */
public class DemoData {
    /**
     * DOCUMENT ME!
     */
    public int compID1 = 0;

    /**
     * DOCUMENT ME!
     */
    public int compID2 = 0;

    /**
     * DOCUMENT ME!
     */
    public HashMap actParamMap = new HashMap();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer sbfOut = new StringBuffer("");

        sbfOut.append("DemoData: ");
        sbfOut.append("compID1 = ").append(compID1).append(", ");
        sbfOut.append("compID2 = ").append(compID2).append(", ");
        sbfOut.append("actParamMap = ").append(actParamMap);

        return sbfOut.toString();
    }
}
