package org.jscience.physics.classical.thermodynamics;

/**
 * Van't Hoff equation for temperature dependence of K.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class VantHoff {

    /**
     * Calculates K at T2 given K at T1 and ΔH°.
     * ln(K2/K1) = (-ΔH°/R) * (1/T2 - 1/T1)
     * 
     * @param k1     Equilibrium constant at T1
     * @param t1     Initial temperature (K)
     * @param t2     Final temperature (K)
     * @param deltaH Enthalpy of reaction (J/mol), assumed constant
     * @return Equilibrium constant at T2
     */
    public static double calculateK2(double k1, double t1, double t2, double deltaH) {
        double R = 8.314462618;
        double exponent = (-deltaH / R) * (1.0 / t2 - 1.0 / t1);
        return k1 * Math.exp(exponent);
    }
}
