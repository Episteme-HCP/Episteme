package org.jscience.chemistry.kinetics;

/**
 * Arrhenius Equation and temperature dependence of reaction rates.
 * <p>
 * Based on: S. Arrhenius, "Über die Reaktionsgeschwindigkeit bei der Inversion
 * von Rohrzucker durch Säuren", Zeitschrift für physikalische Chemie,
 * Vol. 4, pp. 226-248, 1889.
 * </p>
 * <p>
 * The equation k = A·exp(-Ea/RT) describes how reaction rates depend on
 * temperature, where A is the pre-exponential factor, Ea is the activation
 * energy, R is the gas constant, and T is the absolute temperature.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 * @see <a href="https://doi.org/10.1515/zpch-1889-0416">Original Paper</a>
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

    // === Quantity Overloads ===

    /**
     * Calculates rate constant k using Quantity types.
     * 
     * @param A           Pre-exponential factor (1/s or appropriate unit)
     * @param Ea          Activation energy
     * @param temperature Temperature
     * @return Rate constant k
     */
    public static double calculateK(double A,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Energy> Ea,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Temperature> temperature) {
        double eaJoules = Ea.to(org.jscience.measure.Units.JOULE).getValue().doubleValue();
        double tempKelvin = temperature.to(org.jscience.measure.Units.KELVIN).getValue().doubleValue();
        return calculateK(A, eaJoules, tempKelvin);
    }

    /**
     * Calculates activation energy using Quantity types.
     * 
     * @param k1 Rate at T1
     * @param k2 Rate at T2
     * @param t1 Temperature 1
     * @param t2 Temperature 2
     * @return Activation energy as Quantity
     */
    public static org.jscience.measure.Quantity<org.jscience.measure.quantity.Energy> calculateActivationEnergy(
            double k1, double k2,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Temperature> t1,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Temperature> t2) {
        double t1K = t1.to(org.jscience.measure.Units.KELVIN).getValue().doubleValue();
        double t2K = t2.to(org.jscience.measure.Units.KELVIN).getValue().doubleValue();
        double ea = calculateActivationEnergy(k1, k2, t1K, t2K);
        return org.jscience.measure.Quantities.create(ea, org.jscience.measure.Units.JOULE);
    }
}
