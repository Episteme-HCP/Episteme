package org.jscience.chemistry;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

/**
 * Represents a specific isotope of an element.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Isotope {

    private final Element element;
    private final int neutronCount;
    private final double massAMU; // Atomic Mass Units
    private final boolean radioactive;
    private final Double halfLifeSeconds; // null if stable

    public Isotope(Element element, int neutronCount, double massAMU, boolean radioactive, Double halfLifeSeconds) {
        this.element = element;
        this.neutronCount = neutronCount;
        this.massAMU = massAMU;
        this.radioactive = radioactive;
        this.halfLifeSeconds = halfLifeSeconds;
    }

    public Element getElement() {
        return element;
    }

    public int getProtonCount() {
        return element.getAtomicNumber();
    }

    public int getNeutronCount() {
        return neutronCount;
    }

    public int getMassNumber() {
        return getProtonCount() + neutronCount;
    }

    public Quantity<Mass> getMass() {
        // 1 u = 1.66053906660e-27 kg
        return Quantities.getQuantity(massAMU * 1.66053906660e-27, Units.KILOGRAM);
    }

    public boolean isStable() {
        return !radioactive;
    }

    public Double getHalfLife() {
        return halfLifeSeconds;
    }

    @Override
    public String toString() {
        return element.getSymbol() + "-" + getMassNumber();
    }
}
