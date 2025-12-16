package org.jscience.chemistry;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Temperature;

/**
 * Chemical element from the periodic table.
 * <p>
 * Immutable representation of an element with all key properties.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Element {

    private final int atomicNumber; // Z
    private final String symbol; // e.g., "H", "He"
    private final String name; // e.g., "Hydrogen"
    private final Quantity<Mass> atomicMass;
    private final int group; // 1-18 (0 for lanthanides/actinides)
    private final int period; // 1-7
    private final ElementCategory category;
    private final double electronegativity; // Pauling scale (0 if unknown)
    private final Quantity<Length> atomicRadius; // Optional
    private final Quantity<Temperature> meltingPoint; // Optional
    private final Quantity<Temperature> boilingPoint; // Optional

    public enum ElementCategory {
        ALKALI_METAL,
        ALKALINE_EARTH_METAL,
        TRANSITION_METAL,
        POST_TRANSITION_METAL,
        METALLOID,
        NONMETAL, // Generic for diatomic/polyatomic if not specific
        DIATOMIC_NONMETAL,
        POLYATOMIC_NONMETAL,
        HALOGEN,
        NOBLE_GAS,
        LANTHANIDE,
        ACTINIDE,
        UNKNOWN
    }

    public Element(int atomicNumber, String symbol, String name, Quantity<Mass> atomicMass,
            int group, int period, ElementCategory category, double electronegativity,
            Quantity<Temperature> meltingPoint, Quantity<Temperature> boilingPoint) {
        this.atomicNumber = atomicNumber;
        this.symbol = symbol;
        this.name = name;
        this.atomicMass = atomicMass;
        this.group = group;
        this.period = period;
        this.category = category;
        this.electronegativity = electronegativity;
        this.atomicRadius = null; // Default to null if not provided
        this.meltingPoint = meltingPoint;
        this.boilingPoint = boilingPoint;
    }

    // --- Properties ---

    public int getAtomicNumber() {
        return atomicNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public Quantity<Mass> getAtomicMass() {
        return atomicMass;
    }

    public int getGroup() {
        return group;
    }

    public int getPeriod() {
        return period;
    }

    public ElementCategory getCategory() {
        return category;
    }

    public double getElectronegativity() {
        return electronegativity;
    }

    public Quantity<Length> getAtomicRadius() {
        return atomicRadius;
    }

    public Quantity<Temperature> getMeltingPoint() {
        return meltingPoint;
    }

    public Quantity<Temperature> getBoilingPoint() {
        return boilingPoint;
    }

    /**
     * Number of valence electrons (simplified for main group elements).
     */
    public int getValenceElectrons() {
        if (group >= 1 && group <= 2)
            return group;
        if (group >= 13 && group <= 18)
            return group - 10;
        return -1; // Transition metals are complex
    }

    /**
     * Is this a metal?
     */
    public boolean isMetal() {
        return category == ElementCategory.ALKALI_METAL ||
                category == ElementCategory.ALKALINE_EARTH_METAL ||
                category == ElementCategory.TRANSITION_METAL ||
                category == ElementCategory.POST_TRANSITION_METAL ||
                category == ElementCategory.LANTHANIDE ||
                category == ElementCategory.ACTINIDE;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, Z=%d, A=%s)", name, symbol, atomicNumber, atomicMass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Element))
            return false;
        return atomicNumber == ((Element) o).atomicNumber;
    }

    @Override
    public int hashCode() {
        return atomicNumber;
    }
}
