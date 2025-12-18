package org.jscience.chemistry.polymer;

/**
 * Physical properties of polymers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class PolymerProperties {

    /**
     * Mark-Houwink Equation.
     * Intrinsic viscosity [eta] = K * M^a
     * 
     * @param K Constant dependent on polymer/solvent/temp
     * @param M Molecular weight
     * @param a Scalar exponent (usually 0.5 - 0.8)
     * @return Intrinsic viscosity
     */
    public static double calculateIntrinsicViscosity(double K, double M, double a) {
        return K * Math.pow(M, a);
    }

    /**
     * Flory-Fox equation for Glass Transition Temperature (Tg).
     * Tg = Tg_inf - K / Mn
     * 
     * @param Tg_inf Tg of infinite molecular weight polymer
     * @param K      Constant
     * @param Mn     Number average molecular weight
     * @return Tg for the given molecular weight
     */
    public static double calculateTg(double Tg_inf, double K, double Mn) {
        return Tg_inf - (K / Mn);
    }
}
