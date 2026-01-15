/*
 * Interface for Activity Calculator. To add user-defined activity calculation
 * method, implement this interface.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.calculators;

import org.jscience.chemistry.vapor.VLEContext;

import java.util.ArrayList;


/**
 * Interface for Activity Calculator. To add user-defined activity calculation
 * method, implement this interface.
 */
public interface IActivityCalculator {
    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void setContext(VLEContext context);

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] calculateActivity(Object[] params);

    /**
     * DOCUMENT ME!
     *
     * @param numOfComps DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getParamList(int numOfComps);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNonBinarySystemAllowed();
}
