package org.jscience.sports;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Human kinematics and sports performance analyzer.
 */
public final class KinematicAnalyzer {

    private KinematicAnalyzer() {}

    /**
     * Calculates the estimated energy expenditure using MET (Metabolic Equivalent of Task).
     * Energy (kcal) = MET * weight (kg) * duration (hours)
     * 
     * @param met The MET value for the activity (e.g., 8.0 for running).
     * @param mass Weight of the athlete.
     * @param duration Duration of the activity.
     * @return Total energy expended.
     */
    public static Quantity<Energy> calculateEnergyExpenditure(double met, Quantity<Mass> mass, Quantity<Time> duration) {
        double weightKg = mass.to(Units.KILOGRAM).getValue().doubleValue();
        double hours = duration.to(Units.HOUR).getValue().doubleValue();
        
        double kcal = met * weightKg * hours;
        // Convert kcal to Joules (1 kcal = 4184 J)
        return Quantities.create(kcal * 4184.0, Units.JOULE);
    }
}
