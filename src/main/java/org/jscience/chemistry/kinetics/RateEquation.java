package org.jscience.chemistry.kinetics;

/**
 * Chemical kinetics calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class RateEquation {

    /**
     * Rate law order enumeration.
     */
    public enum Order {
        ZERO(0), FIRST(1), SECOND(2);

        private final int value;

        Order(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Calculates reaction rate from rate constant and concentrations.
     * rate = k * [A]^a * [B]^b * ...
     * 
     * @param k              Rate constant
     * @param concentrations Reactant concentrations
     * @param orders         Reaction orders for each reactant
     * @return Reaction rate
     */
    public static double rate(double k, double[] concentrations, double[] orders) {
        double rate = k;
        for (int i = 0; i < concentrations.length; i++) {
            rate *= Math.pow(concentrations[i], orders[i]);
        }
        return rate;
    }

    /**
     * Zero-order integrated rate law.
     * [A] = [A]₀ - k·t
     */
    public static double zeroOrderConcentration(double A0, double k, double t) {
        return Math.max(0, A0 - k * t);
    }

    /**
     * First-order integrated rate law.
     * [A] = [A]₀ · exp(-k·t)
     */
    public static double firstOrderConcentration(double A0, double k, double t) {
        return A0 * Math.exp(-k * t);
    }

    /**
     * Second-order integrated rate law (one reactant: 2A → products).
     * 1/[A] = 1/[A]₀ + k·t
     */
    public static double secondOrderConcentration(double A0, double k, double t) {
        return 1.0 / (1.0 / A0 + k * t);
    }

    /**
     * Calculates half-life for different reaction orders.
     */
    public static double halfLife(double A0, double k, Order order) {
        switch (order) {
            case ZERO:
                return A0 / (2 * k);
            case FIRST:
                return Math.log(2) / k;
            case SECOND:
                return 1.0 / (k * A0);
            default:
                throw new IllegalArgumentException("Unknown order");
        }
    }

    /**
     * Arrhenius equation: temperature dependence of rate constant.
     * k = A · exp(-Ea/(R·T))
     * 
     * @param A  Pre-exponential (frequency) factor
     * @param Ea Activation energy (kJ/mol)
     * @param T  Temperature (K)
     * @return Rate constant k
     */
    public static double arrhenius(double A, double Ea, double T) {
        double R = 0.008314; // kJ/(mol·K)
        return A * Math.exp(-Ea / (R * T));
    }

    /**
     * Calculates activation energy from two rate constants at different
     * temperatures.
     * Ea = R · ln(k2/k1) / (1/T1 - 1/T2)
     */
    public static double activationEnergy(double k1, double T1, double k2, double T2) {
        double R = 0.008314; // kJ/(mol·K)
        return R * Math.log(k2 / k1) / (1.0 / T1 - 1.0 / T2);
    }

    /**
     * Eyring equation (transition state theory).
     * k = (k_B·T/h) · exp(-ΔG‡/(R·T))
     * 
     * @param T          Temperature (K)
     * @param deltaG_act Activation Gibbs energy (kJ/mol)
     * @return Rate constant
     */
    public static double eyring(double T, double deltaG_act) {
        double kB = 1.380649e-23; // Boltzmann constant (J/K)
        double h = 6.62607015e-34; // Planck constant (J·s)
        double R = 8.314; // J/(mol·K)
        double prefactor = kB * T / h;
        return prefactor * Math.exp(-deltaG_act * 1000 / (R * T));
    }
}
