/*
 * Interface for Fugacity Calculator. To add user-defined fugacity calculation
 * method, implement this interface.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.calculators;

import org.jscience.chemistry.vapor.VLEContext;


/**
 * Interface for Fugacity Calculator. To add user-defined fugacity calculation
 * method, implement this interface.
 */
public interface IFugacityCalculator {
    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void setContext(VLEContext context);

    /**
     * DOCUMENT ME!
     *
     * @param T DOCUMENT ME!
     * @param P DOCUMENT ME!
     * @param Psat DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] calculateFugacity(double T, double P, double[] Psat);
}
