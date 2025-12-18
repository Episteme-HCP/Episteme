package org.jscience.chemistry.electrochemistry;

import javax.measure.Quantity;
import javax.measure.quantity.ElectricCharge;
import javax.measure.quantity.Mass;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

/**
 * Faraday's laws of electrolysis.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class FaradayLaws {

    public static final double F = 96485.33212; // Faraday constant (sample value, C/mol)

    /**
     * Calculates mass of substance deposited/liberated.
     * m = (Q * M) / (n * F)
     * 
     * @param charge    Total electric charge (Coulombs)
     * @param molarMass Molar mass (g/mol) - Note: Input as simple double for
     *                  calculation
     * @param valence   n (electrons per ion)
     * @return Mass deposited (grams)
     */
    public static double calculateMassDeposited(double charge, double molarMass, int valence) {
        return (charge * molarMass) / (valence * F);
    }

    /**
     * Calculates electric charge required to deposit a mass.
     * Q = (m * n * F) / M
     * 
     * @param mass      Mass (grams)
     * @param molarMass Molar mass (g/mol)
     * @param valence   n
     * @return Charge (Coulombs)
     */
    public static double calculateChargeRequired(double mass, double molarMass, int valence) {
        return (mass * valence * F) / molarMass;
    }

    /**
     * Type-safe version using Units.
     */
    public static Quantity<Mass> calculateMass(Quantity<ElectricCharge> charge, double molarMassGramPerMol,
            int valence) {
        double q = charge.to(Units.COULOMB).getValue().doubleValue();
        double m = calculateMassDeposited(q, molarMassGramPerMol, valence);
        // m is in grams
        return Quantities.getQuantity(m / 1000.0, Units.KILOGRAM);
    }
}
