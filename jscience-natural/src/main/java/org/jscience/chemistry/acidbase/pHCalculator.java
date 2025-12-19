package org.jscience.chemistry.acidbase;

/**
 * pH and buffer calculations.
 */
public class pHCalculator {

    private pHCalculator() {
    }

    /**
     * Calculates pH from hydrogen ion concentration.
     * pH = -log10[H+]
     */
    public static double pHFromConcentration(double hydrogenIonMolarity) {
        if (hydrogenIonMolarity <= 0)
            throw new IllegalArgumentException("Concentration must be positive");
        return -Math.log10(hydrogenIonMolarity);
    }

    /**
     * Calculates hydrogen ion concentration from pH.
     * [H+] = 10^(-pH)
     */
    public static double concentrationFromPH(double pH) {
        return Math.pow(10, -pH);
    }

    /**
     * Calculates pOH from pH.
     * pOH = 14 - pH (at 25°C)
     */
    public static double pOHFromPH(double pH) {
        return 14.0 - pH;
    }

    /**
     * Henderson-Hasselbalch equation for buffer pH.
     * pH = pKa + log10([A-]/[HA])
     * 
     * @param pKa                   Acid dissociation constant (-log Ka)
     * @param conjugateBaseMolarity [A-]
     * @param acidMolarity          [HA]
     */
    public static double bufferPH(double pKa, double conjugateBaseMolarity, double acidMolarity) {
        if (acidMolarity <= 0 || conjugateBaseMolarity <= 0) {
            throw new IllegalArgumentException("Concentrations must be positive");
        }
        return pKa + Math.log10(conjugateBaseMolarity / acidMolarity);
    }

    /**
     * Calculates buffer capacity (approximate).
     * β = 2.303 * C * Ka * [H+] / (Ka + [H+])^2
     */
    public static double bufferCapacity(double totalBufferConcentration, double Ka, double pH) {
        double H = Math.pow(10, -pH);
        return 2.303 * totalBufferConcentration * Ka * H / Math.pow(Ka + H, 2);
    }
}
