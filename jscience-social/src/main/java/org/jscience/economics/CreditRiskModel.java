package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models credit risk and probability of default.
 */
public final class CreditRiskModel {

    private CreditRiskModel() {}

    /**
     * Altman Z-Score for bankruptcy prediction.
     */
    public static Real calculateZScore(double x1, double x2, double x3, double x4, double x5) {
        double z = 1.2*x1 + 1.4*x2 + 3.3*x3 + 0.6*x4 + 1.0*x5;
        return Real.of(z);
    }

    /**
     * Expected Loss = EAD * PD * LGD
     */
    public static Real expectedLoss(double ead, double pd, double lgd) {
        return Real.of(ead * pd * lgd);
    }
}
