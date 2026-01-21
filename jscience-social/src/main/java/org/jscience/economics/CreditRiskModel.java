/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models credit risk and probability of default.
 * Provides standard metrics like Altman Z-Score and Expected Loss.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public final class CreditRiskModel {

    private CreditRiskModel() {}

    /**
     * Calculates the Altman Z-Score for bankruptcy prediction.
     * <p>
     * Z = 1.2X1 + 1.4X2 + 3.3X3 + 0.6X4 + 1.0X5
     * <ul>
     * <li>X1 = Working Capital / Total Assets</li>
     * <li>X2 = Retained Earnings / Total Assets</li>
     * <li>X3 = EBIT / Total Assets</li>
     * <li>X4 = Market Value of Equity / Total Liabilities</li>
     * <li>X5 = Sales / Total Assets</li>
     * </ul>
     *
     * @param x1 Working Capital / Total Assets
     * @param x2 Retained Earnings / Total Assets
     * @param x3 Earnings Before Interest and Taxes / Total Assets
     * @param x4 Market Value of Equity / Book Value of Total Liabilities
     * @param x5 Sales / Total Assets
     * @return the calculated Z-Score
     */
    public static Real calculateZScore(double x1, double x2, double x3, double x4, double x5) {
        double z = 1.2*x1 + 1.4*x2 + 3.3*x3 + 0.6*x4 + 1.0*x5;
        return Real.of(z);
    }

    /**
     * Calculates Expected Loss (EL).
     * EL = EAD * PD * LGD
     *
     * @param ead Exposure at Default
     * @param pd Probability of Default (0-1)
     * @param lgd Loss Given Default (0-1)
     * @return the expected loss amount
     */
    public static Real expectedLoss(double ead, double pd, double lgd) {
        return Real.of(ead * pd * lgd);
    }
}
