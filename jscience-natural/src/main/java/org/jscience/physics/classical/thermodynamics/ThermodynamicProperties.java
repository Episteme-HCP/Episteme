package org.jscience.physics.classical.thermodynamics;

/**
 * Thermodynamic property calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ThermodynamicProperties {

    /** Gas constant R in J/(mol·K) */
    public static final double R_J = 8.314462618;

    /** Gas constant R in kJ/(mol·K) */
    public static final double R_KJ = 0.008314462618;

    /** Gas constant R in cal/(mol·K) */
    public static final double R_CAL = 1.98720425864;

    /**
     * Calculates Gibbs free energy change.
     * ΔG = ΔH - T·ΔS
     * 
     * @param deltaH Enthalpy change (kJ/mol)
     * @param deltaS Entropy change (J/mol·K)
     * @param T      Temperature (K)
     * @return ΔG in kJ/mol
     */
    public static double gibbsEnergy(double deltaH, double deltaS, double T) {
        return deltaH - T * deltaS / 1000.0; // Convert J to kJ
    }

    /**
     * Calculates equilibrium constant from Gibbs energy.
     * K = exp(-ΔG°/(R·T))
     * 
     * @param deltaG Standard Gibbs energy change (kJ/mol)
     * @param T      Temperature (K)
     * @return Equilibrium constant K
     */
    public static double equilibriumConstant(double deltaG, double T) {
        return Math.exp(-deltaG / (R_KJ * T));
    }

    /**
     * Calculates Gibbs energy from equilibrium constant.
     * ΔG° = -R·T·ln(K)
     */
    public static double gibbsFromK(double K, double T) {
        return -R_KJ * T * Math.log(K);
    }

    /**
     * Reaction quotient Q calculation.
     * Q = [products]^ν / [reactants]^ν
     * 
     * @param productConcentrations  Product concentrations
     * @param productCoefficients    Stoichiometric coefficients (positive)
     * @param reactantConcentrations Reactant concentrations
     * @param reactantCoefficients   Stoichiometric coefficients (positive)
     * @return Reaction quotient Q
     */
    public static double reactionQuotient(double[] productConcentrations, int[] productCoefficients,
            double[] reactantConcentrations, int[] reactantCoefficients) {
        double numerator = 1.0;
        for (int i = 0; i < productConcentrations.length; i++) {
            numerator *= Math.pow(productConcentrations[i], productCoefficients[i]);
        }

        double denominator = 1.0;
        for (int i = 0; i < reactantConcentrations.length; i++) {
            denominator *= Math.pow(reactantConcentrations[i], reactantCoefficients[i]);
        }

        return numerator / denominator;
    }

    /**
     * Determines reaction spontaneity.
     * 
     * @param deltaG Gibbs energy change
     * @return true if spontaneous (ΔG < 0)
     */
    public static boolean isSpontaneous(double deltaG) {
        return deltaG < 0;
    }

    /**
     * Calculates temperature at which ΔG = 0 (equilibrium temperature).
     * T_eq = ΔH / ΔS
     * 
     * @param deltaH Enthalpy change (kJ/mol)
     * @param deltaS Entropy change (J/mol·K)
     * @return Equilibrium temperature (K)
     */
    public static double equilibriumTemperature(double deltaH, double deltaS) {
        return deltaH * 1000.0 / deltaS;
    }

    /**
     * Van't Hoff equation: temperature dependence of K.
     * ln(K2/K1) = -ΔH°/R * (1/T2 - 1/T1)
     * 
     * @param K1     Equilibrium constant at T1
     * @param T1     Temperature 1 (K)
     * @param T2     Temperature 2 (K)
     * @param deltaH Standard enthalpy change (kJ/mol)
     * @return K2 at temperature T2
     */
    public static double vantHoff(double K1, double T1, double T2, double deltaH) {
        double exponent = (-deltaH / R_KJ) * (1.0 / T2 - 1.0 / T1);
        return K1 * Math.exp(exponent);
    }

    /**
     * Heat capacity at constant pressure.
     * q = n·Cp·ΔT
     * 
     * @param n      Amount (mol)
     * @param Cp     Molar heat capacity (J/mol·K)
     * @param deltaT Temperature change (K)
     * @return Heat transferred (J)
     */
    public static double heatTransfer(double n, double Cp, double deltaT) {
        return n * Cp * deltaT;
    }

    /**
     * Standard enthalpy of reaction from formation enthalpies.
     * ΔH°_rxn = Σ ΔH°_f(products) - Σ ΔH°_f(reactants)
     */
    public static double reactionEnthalpy(double[] productFormationEnthalpies, int[] productCoeff,
            double[] reactantFormationEnthalpies, int[] reactantCoeff) {
        double sum = 0;
        for (int i = 0; i < productFormationEnthalpies.length; i++) {
            sum += productCoeff[i] * productFormationEnthalpies[i];
        }
        for (int i = 0; i < reactantFormationEnthalpies.length; i++) {
            sum -= reactantCoeff[i] * reactantFormationEnthalpies[i];
        }
        return sum;
    }
}
