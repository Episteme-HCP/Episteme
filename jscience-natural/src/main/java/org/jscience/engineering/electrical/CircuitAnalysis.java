/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.engineering.electrical;

import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.ElectricPotential;
import org.jscience.measure.quantity.ElectricCurrent;
import org.jscience.measure.quantity.ElectricResistance;
import org.jscience.measure.quantity.Power;
import org.jscience.measure.quantity.ElectricCapacitance;
import org.jscience.measure.quantity.Inductance;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Frequency;

/**
 * Basic DC circuit calculations using Ohm's law and Kirchhoff's rules.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CircuitAnalysis {

    private CircuitAnalysis() {
    }

    // === Quantity Overloads ===

    public static Quantity<ElectricPotential> voltageQ(Quantity<ElectricCurrent> i, Quantity<ElectricResistance> r) {
        Real result = voltage(Real.of(i.to(Units.AMPERE).getValue().doubleValue()),
                Real.of(r.to(Units.OHM).getValue().doubleValue()));
        return Quantities.create(result.doubleValue(), Units.VOLT);
    }

    public static Quantity<ElectricCurrent> currentQ(Quantity<ElectricPotential> v, Quantity<ElectricResistance> r) {
        Real result = current(Real.of(v.to(Units.VOLT).getValue().doubleValue()),
                Real.of(r.to(Units.OHM).getValue().doubleValue()));
        return Quantities.create(result.doubleValue(), Units.AMPERE);
    }

    public static Quantity<ElectricResistance> resistanceQ(Quantity<ElectricPotential> v, Quantity<ElectricCurrent> i) {
        Real result = resistance(Real.of(v.to(Units.VOLT).getValue().doubleValue()),
                Real.of(i.to(Units.AMPERE).getValue().doubleValue()));
        return Quantities.create(result.doubleValue(), Units.OHM);
    }

    public static Quantity<Power> powerQ(Quantity<ElectricPotential> v, Quantity<ElectricCurrent> i) {
        Real result = power(Real.of(v.to(Units.VOLT).getValue().doubleValue()),
                Real.of(i.to(Units.AMPERE).getValue().doubleValue()));
        return Quantities.create(result.doubleValue(), Units.WATT);
    }

    public static Quantity<Time> rcTimeConstantQ(Quantity<ElectricResistance> r, Quantity<ElectricCapacitance> c) {
        Real result = rcTimeConstant(Real.of(r.to(Units.OHM).getValue().doubleValue()),
                Real.of(c.to(Units.FARAD).getValue().doubleValue()));
        return Quantities.create(result.doubleValue(), Units.SECOND);
    }

    public static Quantity<Frequency> resonantFrequencyQ(Quantity<Inductance> l, Quantity<ElectricCapacitance> c) {
        Real result = resonantFrequency(Real.of(l.to(Units.HENRY).getValue().doubleValue()),
                Real.of(c.to(Units.FARAD).getValue().doubleValue()));
        return Quantities.create(result.doubleValue(), Units.HERTZ);
    }

    // === Ohm's Law ===

    /**
     * Voltage from current and resistance. V = IR
     */
    public static Real voltage(Real currentAmps, Real resistanceOhms) {
        return currentAmps.multiply(resistanceOhms);
    }

    /**
     * Current from voltage and resistance. I = V/R
     */
    public static Real current(Real voltageVolts, Real resistanceOhms) {
        return voltageVolts.divide(resistanceOhms);
    }

    /**
     * Resistance from voltage and current. R = V/I
     */
    public static Real resistance(Real voltageVolts, Real currentAmps) {
        return voltageVolts.divide(currentAmps);
    }

    // === Power ===

    /**
     * Electrical power. P = VI
     */
    public static Real power(Real voltageVolts, Real currentAmps) {
        return voltageVolts.multiply(currentAmps);
    }

    /**
     * Power from current and resistance. P = I²R
     */
    public static Real powerFromCurrent(Real currentAmps, Real resistanceOhms) {
        return currentAmps.pow(2).multiply(resistanceOhms);
    }

    /**
     * Power from voltage and resistance. P = V²/R
     */
    public static Real powerFromVoltage(Real voltageVolts, Real resistanceOhms) {
        return voltageVolts.pow(2).divide(resistanceOhms);
    }

    // === Series/Parallel ===

    /**
     * Total resistance in series.
     */
    public static Real resistanceSeries(Vector<Real> resistances) {
        Real total = Real.ZERO;
        for (int i = 0; i < resistances.dimension(); i++) {
            total = total.add(resistances.get(i));
        }
        return total;
    }

    /**
     * Total resistance in parallel.
     */
    public static Real resistanceParallel(Vector<Real> resistances) {
        Real sum = Real.ZERO;
        for (int i = 0; i < resistances.dimension(); i++) {
            sum = sum.add(Real.ONE.divide(resistances.get(i)));
        }
        return Real.ONE.divide(sum);
    }

    /**
     * Two resistors in parallel (simplified).
     */
    public static Real resistanceParallel2(Real r1, Real r2) {
        return r1.multiply(r2).divide(r1.add(r2));
    }

    /**
     * Total capacitance in parallel.
     */
    public static Real capacitanceParallel(Vector<Real> capacitances) {
        Real total = Real.ZERO;
        for (int i = 0; i < capacitances.dimension(); i++) {
            total = total.add(capacitances.get(i));
        }
        return total;
    }

    /**
     * Total capacitance in series.
     */
    public static Real capacitanceSeries(Vector<Real> capacitances) {
        Real sum = Real.ZERO;
        for (int i = 0; i < capacitances.dimension(); i++) {
            sum = sum.add(Real.ONE.divide(capacitances.get(i)));
        }
        return Real.ONE.divide(sum);
    }

    // === Voltage Divider ===

    /**
     * Output voltage of a voltage divider.
     */
    public static Real voltageDivider(Real vin, Real r1, Real r2) {
        return vin.multiply(r2).divide(r1.add(r2));
    }

    /**
     * Current divider for parallel resistors.
     */
    public static Real currentDivider(Real iTotalAmps, Real r1, Real r2) {
        return iTotalAmps.multiply(r1).divide(r1.add(r2));
    }

    // === Time Constants ===

    /**
     * RC time constant. τ = RC
     */
    public static Real rcTimeConstant(Real resistanceOhms, Real capacitanceFarads) {
        return resistanceOhms.multiply(capacitanceFarads);
    }

    /**
     * RL time constant. τ = L/R
     */
    public static Real rlTimeConstant(Real inductanceHenries, Real resistanceOhms) {
        return inductanceHenries.divide(resistanceOhms);
    }

    /**
     * Capacitor voltage during charging.
     */
    public static Real capacitorChargingVoltage(Real vmax, Real timeS, Real tau) {
        return vmax.multiply(Real.ONE.subtract(timeS.negate().divide(tau).exp()));
    }

    /**
     * Capacitor voltage during discharging.
     */
    public static Real capacitorDischargingVoltage(Real v0, Real timeS, Real tau) {
        return v0.multiply(timeS.negate().divide(tau).exp());
    }

    // === AC Impedance ===

    /**
     * Capacitive reactance. Xc = 1 / (2πfC)
     */
    public static Real capacitiveReactance(Real frequencyHz, Real capacitanceFarads) {
        return Real.ONE.divide(Real.TWO_PI.multiply(frequencyHz).multiply(capacitanceFarads));
    }

    /**
     * Inductive reactance. Xl = 2πfL
     */
    public static Real inductiveReactance(Real frequencyHz, Real inductanceHenries) {
        return Real.TWO_PI.multiply(frequencyHz).multiply(inductanceHenries);
    }

    /**
     * Impedance magnitude for RLC series circuit.
     */
    public static Real impedanceMagnitude(Real resistance, Real inductiveReactance, Real capacitiveReactance) {
        Real x = inductiveReactance.subtract(capacitiveReactance);
        return resistance.pow(2).add(x.pow(2)).sqrt();
    }

    /**
     * Resonant frequency for LC circuit.
     */
    public static Real resonantFrequency(Real inductanceHenries, Real capacitanceFarads) {
        return Real.ONE.divide(Real.TWO_PI.multiply(
                inductanceHenries.multiply(capacitanceFarads).sqrt()));
    }

    /**
     * Quality factor for RLC circuit.
     */
    public static Real qualityFactor(Real resistance, Real inductance, Real capacitance) {
        return Real.ONE.divide(resistance).multiply(inductance.divide(capacitance).sqrt());
    }
}
