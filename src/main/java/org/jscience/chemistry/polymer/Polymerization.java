package org.jscience.chemistry.polymer;

/**
 * Polymerization kinetics and statistics.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Polymerization {

    /**
     * Carothers Equation (Step-growth polymerization).
     * Degree of polymerization (X_n) vs extent of reaction (p).
     * X_n = 1 / (1 - p)
     * 
     * @param p Fraction of functional groups reacted (0 to 1)
     * @return Number average degree of polymerization
     */
    public static double carothersEquation(double p) {
        if (p >= 1.0)
            return Double.POSITIVE_INFINITY;
        return 1.0 / (1.0 - p);
    }

    /**
     * Carothers equation with stoichiometric imbalance r.
     * X_n = (1 + r) / (1 + r - 2rp)
     * 
     * @param p Extent of reaction
     * @param r Stoichiometric ratio (Na/Nb <= 1)
     * @return Degree of polymerization
     */
    public static double carothersEquationImbalance(double p, double r) {
        return (1.0 + r) / (1.0 + r - 2.0 * r * p);
    }
}
