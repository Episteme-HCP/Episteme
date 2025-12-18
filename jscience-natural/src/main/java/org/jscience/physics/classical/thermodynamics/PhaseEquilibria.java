package org.jscience.physics.classical.thermodynamics;

/**
 * Phase Equilibria calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class PhaseEquilibria {

    /**
     * Clausius-Clapeyron equation.
     * Relates vapor pressure and temperature.
     * ln(P2/P1) = (-ΔH_vap / R) * (1/T2 - 1/T1)
     * 
     * @param p1        Pressure at T1 (Pa)
     * @param t1        Temperature 1 (K)
     * @param t2        Temperature 2 (K)
     * @param deltaHvap Enthalpy of vaporization (J/mol)
     * @return Pressure at T2 (Pa)
     */
    public static double clausiusClapeyron(double p1, double t1, double t2, double deltaHvap) {
        double R = 8.314462618;
        double exponent = (-deltaHvap / R) * (1.0 / t2 - 1.0 / t1);
        return p1 * Math.exp(exponent);
    }

    /**
     * Gibbs Phase Rule.
     * F = C - P + 2
     * 
     * @param components Number of chemically independent components
     * @param phases     Number of phases
     * @return Degrees of freedom
     */
    public static int gibbsPhaseRule(int components, int phases) {
        return components - phases + 2;
    }
}
