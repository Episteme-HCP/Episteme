/*
 * Data class to store component data.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor;

/**
 * Data class to store component data.
 */
public class ComponentData {
    /**
     * DOCUMENT ME!
     */
    public int compId = -1;

    /**
     * DOCUMENT ME!
     */
    public String name = null;

    /**
     * DOCUMENT ME!
     */
    public double Tc = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double Pc = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double Zc = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double Vc = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double omega = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double antA = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double antB = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public double antC = Constants.DOUBLE_NULL;

    /**
     * DOCUMENT ME!
     */
    public boolean isCriticalDataAvailable = true;

    /**
     * DOCUMENT ME!
     */
    public boolean isAntoineDataAvailable = true;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        if (Tc == Constants.DOUBLE_NULL) {
            isCriticalDataAvailable = false;
        }

        if (antA == Constants.DOUBLE_NULL) {
            isAntoineDataAvailable = false;
        }

        Vc = (Constants.R_J_PER_KMOL_K * Zc * Tc) / Pc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name;
    }
}
