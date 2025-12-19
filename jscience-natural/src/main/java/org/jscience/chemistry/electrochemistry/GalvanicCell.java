package org.jscience.chemistry.electrochemistry;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.ElectricPotential;

/**
 * Represents a Galvanic (Voltaic) Cell composed of two half-cells.
 */
public class GalvanicCell {

    private final HalfCell anode; // Oxidation occurs here
    private final HalfCell cathode; // Reduction occurs here

    public GalvanicCell(HalfCell anode, HalfCell cathode) {
        this.anode = anode;
        this.cathode = cathode;
    }

    /**
     * Calculates the standard cell potential.
     * E°cell = E°cathode - E°anode
     * 
     * @return standard cell potential
     */
    public Quantity<ElectricPotential> getStandardPotential() {
        double cat = cathode.getStandardPotential().to(Units.VOLT).getValue().doubleValue();
        double an = anode.getStandardPotential().to(Units.VOLT).getValue().doubleValue();
        return Quantities.create(cat - an, Units.VOLT);
    }

    public HalfCell getAnode() {
        return anode;
    }

    public HalfCell getCathode() {
        return cathode;
    }

    @Override
    public String toString() {
        return String.format("Anode: %s | Cathode: %s | E°cell = %s", anode, cathode, getStandardPotential());
    }
}
