package org.jscience.chemistry.kinetics;

/**
 * Arrhenius Equation and temperature dependence of reaction rates.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ArrheniusEquation {

    public static final double R = 8.314462618; // Gas constant

    /**
     * Calculates rate constant k.
     * k = A * exp(-Ea / RT)
     * 
     * @param A  Pre-exponential factor
     * @param Ea Activation energy (J/mol)
     * @param T  Temperature (K)
     * @return Rate constant k
     */
    public static double calculateK(double A, double Ea, double T) {
        return A * Math.exp(-Ea / (R * T));
    }

    /**
     * Calculates activation energy Ea given two rate constants.
     * ln(k2/k1) = (-Ea/R) * (1/T2 - 1/T1)
     * Ea = -R * ln(k2/k1) / (1/T2 - 1/T1)
     * 
     * @param k1 Rate at T1
     * @param k2 Rate at T2
     * @param t1 Temperature 1 (K)
     * @param t2 Temperature 2 (K)
     * @return Activation energy (J/mol)
     */
    public static double calculateActivationEnergy(double k1, double k2, double t1, double t2) {
        return -R * Math.log(k2 / k1) / (1.0 / t2 - 1.0 / t1);
    }
}
