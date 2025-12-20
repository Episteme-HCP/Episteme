package org.jscience.chemistry;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Dimensionless;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.MassDensity;
import org.jscience.measure.quantity.SpecificHeatCapacity;
import org.jscience.measure.quantity.ThermalConductivity;

/**
 * A chemical element.
 * Modernized to use JScience V5 Quantity system.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Element {

    public enum ElementCategory {
        ALKALI_METAL, ALKALINE_EARTH_METAL, TRANSITION_METAL, POST_TRANSITION_METAL, CHEMICALLY_UNKNOWN, METALLOID,
        NONMETAL, HALOGEN, NOBLE_GAS, LANTHANIDE, ACTINIDE, UNKNOWN
    }

    private String name;
    private String symbol;
    private int atomicNumber;
    private int massNumber;
    private Quantity<Mass> atomicMass;
    private int group;
    private int period;
    private ElementCategory category;
    private double electronegativity;

    // Properties
    private Quantity<Length> covalentRadius;
    private Quantity<Length> atomicRadius;
    private Quantity<Temperature> meltingPoint;
    private Quantity<Temperature> boilingPoint;
    private Quantity<MassDensity> density;
    private Quantity<SpecificHeatCapacity> specificHeat;
    private Quantity<ThermalConductivity> thermalConductivity;
    private Quantity<Dimensionless> ionizationEnergy;

    public Element(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    // --- Getters and Setters ---

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    public void setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
    }

    public int getMassNumber() {
        return massNumber;
    }

    public void setMassNumber(int massNumber) {
        this.massNumber = massNumber;
    }

    public Quantity<Mass> getAtomicMass() {
        return atomicMass;
    }

    public void setAtomicMass(Quantity<Mass> atomicMass) {
        this.atomicMass = atomicMass;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public ElementCategory getCategory() {
        return category;
    }

    public void setCategory(ElementCategory category) {
        this.category = category;
    }

    public double getElectronegativity() {
        return electronegativity;
    }

    public void setElectronegativity(double electronegativity) {
        this.electronegativity = electronegativity;
    }

    public Quantity<Length> getCovalentRadius() {
        return covalentRadius;
    }

    public void setCovalentRadius(Quantity<Length> covalentRadius) {
        this.covalentRadius = covalentRadius;
    }

    public Quantity<Length> getAtomicRadius() {
        return atomicRadius;
    }

    public void setAtomicRadius(Quantity<Length> atomicRadius) {
        this.atomicRadius = atomicRadius;
    }

    public Quantity<Temperature> getMeltingPoint() {
        return meltingPoint;
    }

    public void setMeltingPoint(Quantity<Temperature> meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

    public Quantity<Temperature> getBoilingPoint() {
        return boilingPoint;
    }

    public void setBoilingPoint(Quantity<Temperature> boilingPoint) {
        this.boilingPoint = boilingPoint;
    }

    public Quantity<MassDensity> getDensity() {
        return density;
    }

    public void setDensity(Quantity<MassDensity> density) {
        this.density = density;
    }

    public Quantity<SpecificHeatCapacity> getSpecificHeat() {
        return specificHeat;
    }

    public void setSpecificHeat(Quantity<SpecificHeatCapacity> specificHeat) {
        this.specificHeat = specificHeat;
    }

    public Quantity<ThermalConductivity> getThermalConductivity() {
        return thermalConductivity;
    }

    public void setThermalConductivity(Quantity<ThermalConductivity> thermalConductivity) {
        this.thermalConductivity = thermalConductivity;
    }

    public Quantity<Dimensionless> getIonizationEnergy() {
        return ionizationEnergy;
    }

    public void setIonizationEnergy(Quantity<Dimensionless> ionizationEnergy) {
        this.ionizationEnergy = ionizationEnergy;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
