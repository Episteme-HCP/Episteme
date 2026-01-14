/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.chemistry;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.MassDensity;
import org.jscience.measure.quantity.SpecificHeatCapacity;
import org.jscience.measure.quantity.ThermalConductivity;
import org.jscience.measure.quantity.Energy;
import org.jscience.mathematics.numbers.real.Real;

/**
 * A chemical element.
 * Modernized to use JScience V5 Quantity system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
    private Real electronegativity;

    // Properties
    private Quantity<Length> covalentRadius;
    private Quantity<Length> atomicRadius;
    private Quantity<Temperature> meltingPoint;
    private Quantity<Temperature> boilingPoint;
    private Quantity<MassDensity> density;
    private Quantity<SpecificHeatCapacity> specificHeat;
    private Quantity<ThermalConductivity> thermalConductivity;
    private Quantity<Energy> ionizationEnergy;
    private Quantity<Energy> electronAffinity;
    private String standardState;
    private String electronConfiguration; // e.g. [Ar]4s2
    private String oxidationStates; // e.g. +4, +2
    private int yearDiscovered;

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

    public Real getElectronegativity() {
        return electronegativity;
    }

    public void setElectronegativity(Real electronegativity) {
        this.electronegativity = electronegativity;
    }

    // Legacy support for loading tools (optional but helpful if JSON loader uses
    // double)
    public void setElectronegativity(double val) {
        this.electronegativity = Real.of(val);
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

    public Quantity<Energy> getIonizationEnergy() {
        return ionizationEnergy;
    }

    public void setIonizationEnergy(Quantity<Energy> ionizationEnergy) {
        this.ionizationEnergy = ionizationEnergy;
    }

    public Quantity<Energy> getElectronAffinity() {
        return electronAffinity;
    }

    public void setElectronAffinity(Quantity<Energy> electronAffinity) {
        this.electronAffinity = electronAffinity;
    }

    public String getStandardState() {
        return standardState;
    }

    public void setStandardState(String standardState) {
        this.standardState = standardState;
    }

    public String getElectronConfiguration() {
        return electronConfiguration;
    }

    public void setElectronConfiguration(String electronConfiguration) {
        this.electronConfiguration = electronConfiguration;
    }

    public String getOxidationStates() {
        return oxidationStates;
    }

    public void setOxidationStates(String oxidationStates) {
        this.oxidationStates = oxidationStates;
    }

    public int getYearDiscovered() {
        return yearDiscovered;
    }

    public void setYearDiscovered(int yearDiscovered) {
        this.yearDiscovered = yearDiscovered;
    }

    @Override
    public String toString() {
        return symbol;
    }
}


