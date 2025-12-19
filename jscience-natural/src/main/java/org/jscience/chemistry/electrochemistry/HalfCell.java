package org.jscience.chemistry.electrochemistry;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.ElectricPotential;

/**
 * Represents a half-cell in an electrochemical system.
 * Defined by its standard reduction potential.
 */
public class HalfCell {

    private final String reaction;
    private final Quantity<ElectricPotential> standardPotential; // E0

    public HalfCell(String reaction, Quantity<ElectricPotential> standardPotential) {
        this.reaction = reaction;
        this.standardPotential = standardPotential;
    }

    public HalfCell(String reaction, double potentialVolts) {
        this(reaction, Quantities.create(potentialVolts, Units.VOLT));
    }

    public String getReaction() {
        return reaction;
    }

    public Quantity<ElectricPotential> getStandardPotential() {
        return standardPotential;
    }

    @Override
    public String toString() {
        return String.format("%s (E° = %s)", reaction, standardPotential);
    }
}
