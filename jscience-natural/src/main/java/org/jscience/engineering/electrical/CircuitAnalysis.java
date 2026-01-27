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

package org.jscience.engineering.electrical;

import java.util.List;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.ElectricCapacitance;
import org.jscience.measure.quantity.ElectricCurrent;
import org.jscience.measure.quantity.ElectricPotential;
import org.jscience.measure.quantity.ElectricResistance;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Inductance;
import org.jscience.measure.quantity.Power;
import org.jscience.measure.quantity.Time;

/**
 * Basic DC/AC circuit calculations using Ohm's law and Kirchhoff's rules.
 * Modernized to use typed Quantities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CircuitAnalysis {

    private CircuitAnalysis() {
    }

    // === Ohm's Law ===

    /**
     * Voltage from current and resistance. V = IR
     */
    public static Quantity<ElectricPotential> voltage(Quantity<ElectricCurrent> current, Quantity<ElectricResistance> resistance) {
        Real i = current.to(Units.AMPERE).getValue();
        Real r = resistance.to(Units.OHM).getValue();
        return Quantities.create(i.multiply(r), Units.VOLT);
    }

    /**
     * Current from voltage and resistance. I = V/R
     */
    public static Quantity<ElectricCurrent> current(Quantity<ElectricPotential> voltage, Quantity<ElectricResistance> resistance) {
        Real v = voltage.to(Units.VOLT).getValue();
        Real r = resistance.to(Units.OHM).getValue();
        return Quantities.create(v.divide(r), Units.AMPERE);
    }

    /**
     * Resistance from voltage and current. R = V/I
     */
    public static Quantity<ElectricResistance> resistance(Quantity<ElectricPotential> voltage, Quantity<ElectricCurrent> current) {
        Real v = voltage.to(Units.VOLT).getValue();
        Real i = current.to(Units.AMPERE).getValue();
        return Quantities.create(v.divide(i), Units.OHM);
    }

    // === Power ===

    /**
     * Electrical power. P = VI
     */
    public static Quantity<Power> power(Quantity<ElectricPotential> voltage, Quantity<ElectricCurrent> current) {
        Real v = voltage.to(Units.VOLT).getValue();
        Real i = current.to(Units.AMPERE).getValue();
        return Quantities.create(v.multiply(i), Units.WATT);
    }

    /**
     * Power from current and resistance. P = I²R
     */
    public static Quantity<Power> powerFromCurrent(Quantity<ElectricCurrent> current, Quantity<ElectricResistance> resistance) {
        Real i = current.to(Units.AMPERE).getValue();
        Real r = resistance.to(Units.OHM).getValue();
        return Quantities.create(i.pow(2).multiply(r), Units.WATT);
    }

    /**
     * Power from voltage and resistance. P = V²/R
     */
    public static Quantity<Power> powerFromVoltage(Quantity<ElectricPotential> voltage, Quantity<ElectricResistance> resistance) {
        Real v = voltage.to(Units.VOLT).getValue();
        Real r = resistance.to(Units.OHM).getValue();
        return Quantities.create(v.pow(2).divide(r), Units.WATT);
    }

    // === Series/Parallel ===

    /**
     * Total resistance in series.
     */
    public static Quantity<ElectricResistance> resistanceSeries(List<Quantity<ElectricResistance>> resistances) {
        Real total = Real.ZERO;
        for (Quantity<ElectricResistance> r : resistances) {
            total = total.add(r.to(Units.OHM).getValue());
        }
        return Quantities.create(total, Units.OHM);
    }

    /**
     * Total resistance in parallel.
     */
    public static Quantity<ElectricResistance> resistanceParallel(List<Quantity<ElectricResistance>> resistances) {
        Real sumReciprocal = Real.ZERO;
        for (Quantity<ElectricResistance> r : resistances) {
            sumReciprocal = sumReciprocal.add(Real.ONE.divide(r.to(Units.OHM).getValue()));
        }
        return Quantities.create(Real.ONE.divide(sumReciprocal), Units.OHM);
    }

    /**
     * Two resistors in parallel.
     */
    public static Quantity<ElectricResistance> resistanceParallel2(Quantity<ElectricResistance> r1, Quantity<ElectricResistance> r2) {
        Real v1 = r1.to(Units.OHM).getValue();
        Real v2 = r2.to(Units.OHM).getValue();
        return Quantities.create(v1.multiply(v2).divide(v1.add(v2)), Units.OHM);
    }

    /**
     * Total capacitance in parallel.
     */
    public static Quantity<ElectricCapacitance> capacitanceParallel(List<Quantity<ElectricCapacitance>> capacitances) {
        Real total = Real.ZERO;
        for (Quantity<ElectricCapacitance> c : capacitances) {
            total = total.add(c.to(Units.FARAD).getValue());
        }
        return Quantities.create(total, Units.FARAD);
    }

    /**
     * Total capacitance in series.
     */
    public static Quantity<ElectricCapacitance> capacitanceSeries(List<Quantity<ElectricCapacitance>> capacitances) {
        Real sumReciprocal = Real.ZERO;
        for (Quantity<ElectricCapacitance> c : capacitances) {
            sumReciprocal = sumReciprocal.add(Real.ONE.divide(c.to(Units.FARAD).getValue()));
        }
        return Quantities.create(Real.ONE.divide(sumReciprocal), Units.FARAD);
    }

    // === Voltage Divider ===

    /**
     * Output voltage of a voltage divider.
     * Vout = Vin * R2 / (R1 + R2)
     */
    public static Quantity<ElectricPotential> voltageDivider(Quantity<ElectricPotential> vin, Quantity<ElectricResistance> r1, Quantity<ElectricResistance> r2) {
        Real v = vin.to(Units.VOLT).getValue();
        Real val1 = r1.to(Units.OHM).getValue();
        Real val2 = r2.to(Units.OHM).getValue();
        return Quantities.create(v.multiply(val2).divide(val1.add(val2)), Units.VOLT);
    }

    /**
     * Current divider for 2 parallel resistors.
     * I1 = Itotal * R2 / (R1 + R2)
     */
    public static Quantity<ElectricCurrent> currentDivider(Quantity<ElectricCurrent> iTotal, Quantity<ElectricResistance> r1, Quantity<ElectricResistance> r2) {
        Real i = iTotal.to(Units.AMPERE).getValue();
        Real val1 = r1.to(Units.OHM).getValue();
        Real val2 = r2.to(Units.OHM).getValue();
        // Current through R1
        return Quantities.create(i.multiply(val2).divide(val1.add(val2)), Units.AMPERE);
    }

    // === Time Constants ===

    /**
     * RC time constant. τ = RC
     */
    public static Quantity<Time> rcTimeConstant(Quantity<ElectricResistance> r, Quantity<ElectricCapacitance> c) {
        Real res = r.to(Units.OHM).getValue();
        Real cap = c.to(Units.FARAD).getValue();
        return Quantities.create(res.multiply(cap), Units.SECOND);
    }

    /**
     * RL time constant. τ = L/R
     */
    public static Quantity<Time> rlTimeConstant(Quantity<Inductance> l, Quantity<ElectricResistance> r) {
        Real ind = l.to(Units.HENRY).getValue();
        Real res = r.to(Units.OHM).getValue();
        return Quantities.create(ind.divide(res), Units.SECOND);
    }

    /**
     * Capacitor voltage during charging: V(t) = Vmax * (1 - e^(-t/τ))
     */
    public static Quantity<ElectricPotential> capacitorChargingVoltage(Quantity<ElectricPotential> vmax, Quantity<Time> time, Quantity<Time> tau) {
        Real v = vmax.to(Units.VOLT).getValue();
        Real t = time.to(Units.SECOND).getValue();
        Real tc = tau.to(Units.SECOND).getValue();
        return Quantities.create(v.multiply(Real.ONE.subtract(t.negate().divide(tc).exp())), Units.VOLT);
    }

    /**
     * Capacitor voltage during discharging: V(t) = V0 * e^(-t/τ)
     */
    public static Quantity<ElectricPotential> capacitorDischargingVoltage(Quantity<ElectricPotential> v0, Quantity<Time> time, Quantity<Time> tau) {
        Real v = v0.to(Units.VOLT).getValue();
        Real t = time.to(Units.SECOND).getValue();
        Real tc = tau.to(Units.SECOND).getValue();
        return Quantities.create(v.multiply(t.negate().divide(tc).exp()), Units.VOLT);
    }

    // === AC Impedance ===

    /**
     * Capacitive reactance. Xc = 1 / (2πfC)
     */
    public static Quantity<ElectricResistance> capacitiveReactance(Quantity<Frequency> frequency, Quantity<ElectricCapacitance> capacitance) {
        Real f = frequency.to(Units.HERTZ).getValue();
        Real c = capacitance.to(Units.FARAD).getValue();
        Real xc = Real.ONE.divide(Real.TWO_PI.multiply(f).multiply(c));
        return Quantities.create(xc, Units.OHM);
    }

    /**
     * Inductive reactance. Xl = 2πfL
     */
    public static Quantity<ElectricResistance> inductiveReactance(Quantity<Frequency> frequency, Quantity<Inductance> inductance) {
        Real f = frequency.to(Units.HERTZ).getValue();
        Real l = inductance.to(Units.HENRY).getValue();
        Real xl = Real.TWO_PI.multiply(f).multiply(l);
        return Quantities.create(xl, Units.OHM);
    }

    /**
     * Impedance magnitude for RLC series circuit.
     * Z = sqrt(R² + (Xl - Xc)²)
     */
    public static Quantity<ElectricResistance> impedanceMagnitude(Quantity<ElectricResistance> r, Quantity<ElectricResistance> xl, Quantity<ElectricResistance> xc) {
        Real res = r.to(Units.OHM).getValue();
        Real xL = xl.to(Units.OHM).getValue();
        Real xC = xc.to(Units.OHM).getValue();
        Real x = xL.subtract(xC);
        return Quantities.create(res.pow(2).add(x.pow(2)).sqrt(), Units.OHM);
    }

    /**
     * Resonant frequency for LC circuit.
     * f = 1 / (2π * sqrt(LC))
     */
    public static Quantity<Frequency> resonantFrequency(Quantity<Inductance> inductance, Quantity<ElectricCapacitance> capacitance) {
        Real l = inductance.to(Units.HENRY).getValue();
        Real c = capacitance.to(Units.FARAD).getValue();
        Real f = Real.ONE.divide(Real.TWO_PI.multiply(l.multiply(c).sqrt()));
        return Quantities.create(f, Units.HERTZ);
    }

    /**
     * Quality factor for RLC circuit.
     * Q = (1/R) * sqrt(L/C)
     */
    public static Real qualityFactor(Quantity<ElectricResistance> resistance, Quantity<Inductance> inductance, Quantity<ElectricCapacitance> capacitance) {
        Real r = resistance.to(Units.OHM).getValue();
        Real l = inductance.to(Units.HENRY).getValue();
        Real c = capacitance.to(Units.FARAD).getValue();
        return Real.ONE.divide(r).multiply(l.divide(c).sqrt());
    }
}


