package org.jscience.physics.classical.thermodynamics;

/**
 * Equilibrium Constant calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class EquilibriumConstant {

    /**
     * Calculates K_eq from Gibbs Free Energy change.
     * K = exp(-ΔG° / RT)
     * 
     * @param deltaG      Standard Gibbs free energy change (J/mol)
     * @param temperature Temperature (Kelvin)
     * @return Equilibrium constant (dimensionless)
     */
    public static double calculateK(double deltaG, double temperature) {
        double R = 8.314462618; // Gas constant J/(mol·K)
        return Math.exp(-deltaG / (R * temperature));
    }

    /**
     * Calculates Gibbs Free Energy from K_eq.
     * ΔG° = -RT ln K
     */
    public static double calculateDeltaG(double K, double temperature) {
        double R = 8.314462618;
        return -R * temperature * Math.log(K);
    }

    /**
     * Reaction Quotient Q.
     * Q = product concentrations / reactant concentrations
     * 
     * @param productConcs  Array of product concentrations
     * @param reactantConcs Array of reactant concentrations
     * @return Q
     */
    public static double calculateQ(double[] productConcs, double[] reactantConcs) {
        double num = 1.0;
        for (double c : productConcs)
            num *= c;

        double den = 1.0;
        for (double c : reactantConcs)
            den *= c;

        return den == 0 ? Double.POSITIVE_INFINITY : num / den;
    }
}
