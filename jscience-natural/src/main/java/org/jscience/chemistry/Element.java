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

import org.jscience.util.identity.AbstractIdentifiedEntity;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * A chemical element.
 * Modernized to use JScience V5 Quantity system and Identification system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Element extends AbstractIdentifiedEntity {

    private static final long serialVersionUID = 1L;

    public enum ElementCategory {
        ALKALI_METAL, ALKALINE_EARTH_METAL, TRANSITION_METAL, POST_TRANSITION_METAL, CHEMICALLY_UNKNOWN, METALLOID,
        NONMETAL, HALOGEN, NOBLE_GAS, LANTHANIDE, ACTINIDE, UNKNOWN
    }

    @Attribute
    private int atomicNumber;
    @Attribute
    private int massNumber;
    @Attribute
    private Quantity<Mass> atomicMass;
    @Attribute
    private int group;
    @Attribute
    private int period;
    @Attribute
    private ElementCategory category;
    @Attribute
    private Real electronegativity;

    // Properties
    @Attribute
    private Quantity<Length> covalentRadius;
    @Attribute
    private Quantity<Length> atomicRadius;
    @Attribute
    private Quantity<Temperature> meltingPoint;
    @Attribute
    private Quantity<Temperature> boilingPoint;
    @Attribute
    private Quantity<MassDensity> density;
    @Attribute
    private Quantity<SpecificHeatCapacity> specificHeat;
    @Attribute
    private Quantity<ThermalConductivity> thermalConductivity;
    @Attribute
    private Quantity<Energy> ionizationEnergy;
    @Attribute
    private Quantity<Energy> electronAffinity;
    @Attribute
    private String standardState;
    @Attribute
    private String electronConfiguration;
    @Attribute
    private String oxidationStates;
    @Attribute
    private int yearDiscovered;

    public Element(String name, String symbol) {
        super(new SimpleIdentification(symbol));
        setName(name);
    }

    public String getSymbol() {
        return getId().toString();
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
        return getSymbol();
    }
}
