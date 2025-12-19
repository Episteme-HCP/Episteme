package org.jscience.chemistry.electrochemistry;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.ElectricPotential;
import org.jscience.measure.quantity.Temperature;

/**
 * Calculations for Electrochemistry using the Nernst Equation.
 * <p>
 * Based on: W. Nernst, "Die elektromotorische Wirksamkeit der Ionen",
 * Zeitschrift für physikalische Chemie, Vol. 4, pp. 129-181, 1889.
 * </p>
 * <p>
 * The Nernst equation E = E° - (RT/nF)·ln(Q) relates the reduction potential
 * of a half-cell to the standard electrode potential, temperature, and
 * reaction quotient.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class NernstEquation {

    public static final double R = 8.314462618; // J/(mol·K)
    public static final double F = 96485.33212; // C/mol

    private NernstEquation() {
    }

    /**
     * Calculates the cell potential under non-standard conditions.
     * 
     * @param standardPotential E° (Volts)
     * @param temperature       Temperature (Kelvin)
     * @param electrons         Number of electrons transferred (n)
     * @param reactionQuotient  Reaction Quotient (Q)
     * @return Cell Potential (E)
     */
    public static Quantity<ElectricPotential> calculatePotential(
            Quantity<ElectricPotential> standardPotential,
            Quantity<Temperature> temperature,
            int electrons,
            double reactionQuotient) {

        double E0 = standardPotential.to(Units.VOLT).getValue().doubleValue();
        double T = temperature.to(Units.KELVIN).getValue().doubleValue();
        double lnQ = Math.log(reactionQuotient);

        double E = E0 - ((R * T) / (electrons * F)) * lnQ;

        return Quantities.create(E, Units.VOLT);
    }

    /**
     * Simplified Nernst equation at 25°C (298.15 K).
     * E = E° - (0.0592 / n) * log10(Q)
     */
    public static Quantity<ElectricPotential> calculatePotentialAt25C(
            Quantity<ElectricPotential> standardPotential,
            int electrons,
            double reactionQuotient) {

        double E0 = standardPotential.to(Units.VOLT).getValue().doubleValue();
        double logQ = Math.log10(reactionQuotient);

        // 0.05916 approx 0.0592
        double E = E0 - (0.05916 / electrons) * logQ;

        return Quantities.create(E, Units.VOLT);
    }
}
