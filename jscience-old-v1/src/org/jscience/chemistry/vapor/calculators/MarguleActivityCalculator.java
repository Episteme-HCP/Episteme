/*
 * Margule's equation implementation for activity calculation.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.calculators;

import org.jscience.chemistry.vapor.VLEContext;

import java.util.ArrayList;


/**
 * Margule's equation implementation for activity calculation.
 */
public class MarguleActivityCalculator implements IActivityCalculator {
    /**
     * DOCUMENT ME!
     */
    private VLEContext context = null;

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void setContext(VLEContext context) {
        this.context = context;
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] calculateActivity(Object[] params) {
        double A12 = ((Double) params[0]).doubleValue();
        double A21 = ((Double) params[1]).doubleValue();

        double[] gamma = new double[2];
        double[] x = context.getLiquidMoleFractions();

        gamma[0] = Math.exp(x[1] * x[1] * (A12 + (2 * (A21 - A12) * x[0])));
        gamma[1] = Math.exp(x[0] * x[0] * (A21 + (2 * (A12 - A21) * x[1])));

        return gamma;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numOfComps DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getParamList(int numOfComps) {
        ArrayList paramList = new ArrayList();

        if (numOfComps != 2) {
            return paramList;
        }

        paramList.add("Margule-a12");
        paramList.add("Margule-a21");

        return paramList;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNonBinarySystemAllowed() {
        return false;
    }
}
