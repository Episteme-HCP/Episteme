package org.jscience.biology.cell;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Cellular metabolism models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Metabolism {

    /** Gibbs free energy of ATP hydrolysis (kJ/mol) */
    public static final double ATP_HYDROLYSIS_G = -30.5;

    /**
     * Calculates energy released from ATP hydrolysis.
     * ATP + H2O -> ADP + Pi
     * 
     * @param moles Moles of ATP hydrolyzed
     * @return Energy released (J)
     */
    public static Quantity<Energy> atpHydrolysisEnergy(double moles) {
        double energyKj = Math.abs(ATP_HYDROLYSIS_G) * moles;
        return Quantities.create(energyKj * 1000, Units.JOULE);
    }

    /**
     * Simplified Glycolysis net reaction energy.
     * Glucose + 2 NAD+ + 2 ADP + 2 Pi -> 2 Pyruvate + 2 NADH + 2 H+ + 2 ATP
     * Net yield: 2 ATP per Glucose.
     */
    public static double glycolysisAtpYield(double glucoseMoles) {
        return 2 * glucoseMoles;
    }

    /**
     * Cellular Respiration (Aerobic) total ATP yield.
     * Theoretical max: ~38 ATP per Glucose (~30-32 practical).
     */
    public static double aerobicRespirationAtpYield(double glucoseMoles) {
        return 32 * glucoseMoles;
    }

    /**
     * Energy charge of the cell.
     * EC = ([ATP] + 0.5[ADP]) / ([ATP] + [ADP] + [AMP])
     * Healthy cell: 0.8 - 0.95
     */
    public static double energyCharge(double atpConc, double adpConc, double ampConc) {
        return (atpConc + 0.5 * adpConc) / (atpConc + adpConc + ampConc);
    }
}
