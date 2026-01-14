/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
