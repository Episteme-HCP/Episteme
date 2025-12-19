package org.jscience.chemistry.electrochemistry;

import org.jscience.chemistry.ChemicalReaction;

/**
 * Represents a half-cell electrode reaction.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ElectrodeReaction {

    private final ChemicalReaction reaction;
    private final double standardPotential; // E° (Volts) vs SHE

    public ElectrodeReaction(ChemicalReaction reaction, double e0) {
        this.reaction = reaction;
        this.standardPotential = e0;
    }

    public double getStandardPotential() {
        return standardPotential;
    }

    public ChemicalReaction getReaction() {
        return reaction;
    }

    /**
     * Calculates potential at non-standard conditions using Nernst equation.
     * E = E° - (RT/nF) * ln(Q)
     */
    public double calculatePotential(double temperature, int n, double reactionQuotientQ) {
        org.jscience.measure.Quantity<org.jscience.measure.quantity.ElectricPotential> e0 = org.jscience.measure.Quantities
                .create(standardPotential, org.jscience.measure.Units.VOLT);
        org.jscience.measure.Quantity<org.jscience.measure.quantity.Temperature> t = org.jscience.measure.Quantities
                .create(temperature, org.jscience.measure.Units.KELVIN);

        return NernstEquation.calculatePotential(e0, t, n, reactionQuotientQ)
                .to(org.jscience.measure.Units.VOLT).getValue().doubleValue();
    }
}
