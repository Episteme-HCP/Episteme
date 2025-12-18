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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.engineering.electrical;

/**
 * Basic DC circuit calculations using Ohm's law and Kirchhoff's rules.
 * <p>
 * Provides:
 * <ul>
 * <li>Ohm's law calculations</li>
 * <li>Series/parallel resistance</li>
 * <li>Power calculations</li>
 * <li>Voltage divider</li>
 * <li>RC/RL time constants</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class CircuitAnalysis {

    private CircuitAnalysis() {
    }

    // === Ohm's Law ===

    /**
     * Voltage from current and resistance.
     * V = IR
     * 
     * @param currentAmps    current in Amperes
     * @param resistanceOhms resistance in Ohms
     * @return voltage in Volts
     */
    public static double voltage(double currentAmps, double resistanceOhms) {
        return currentAmps * resistanceOhms;
    }

    /**
     * Current from voltage and resistance.
     * I = V/R
     */
    public static double current(double voltageVolts, double resistanceOhms) {
        return voltageVolts / resistanceOhms;
    }

    /**
     * Resistance from voltage and current.
     * R = V/I
     */
    public static double resistance(double voltageVolts, double currentAmps) {
        return voltageVolts / currentAmps;
    }

    // === Power ===

    /**
     * Electrical power.
     * P = VI = I²R = V²/R
     */
    public static double power(double voltageVolts, double currentAmps) {
        return voltageVolts * currentAmps;
    }

    /**
     * Power from current and resistance.
     * P = I²R
     */
    public static double powerFromCurrent(double currentAmps, double resistanceOhms) {
        return currentAmps * currentAmps * resistanceOhms;
    }

    /**
     * Power from voltage and resistance.
     * P = V²/R
     */
    public static double powerFromVoltage(double voltageVolts, double resistanceOhms) {
        return voltageVolts * voltageVolts / resistanceOhms;
    }

    // === Series/Parallel ===

    /**
     * Total resistance in series.
     * Rtotal = R1 + R2 + ... + Rn
     */
    public static double resistanceSeries(double... resistances) {
        double total = 0;
        for (double r : resistances) {
            total += r;
        }
        return total;
    }

    /**
     * Total resistance in parallel.
     * 1/Rtotal = 1/R1 + 1/R2 + ... + 1/Rn
     */
    public static double resistanceParallel(double... resistances) {
        double sum = 0;
        for (double r : resistances) {
            sum += 1.0 / r;
        }
        return 1.0 / sum;
    }

    /**
     * Two resistors in parallel (simplified).
     * R = (R1 * R2) / (R1 + R2)
     */
    public static double resistanceParallel2(double r1, double r2) {
        return (r1 * r2) / (r1 + r2);
    }

    /**
     * Total capacitance in parallel.
     * Ctotal = C1 + C2 + ... + Cn
     */
    public static double capacitanceParallel(double... capacitances) {
        double total = 0;
        for (double c : capacitances) {
            total += c;
        }
        return total;
    }

    /**
     * Total capacitance in series.
     * 1/Ctotal = 1/C1 + 1/C2 + ... + 1/Cn
     */
    public static double capacitanceSeries(double... capacitances) {
        double sum = 0;
        for (double c : capacitances) {
            sum += 1.0 / c;
        }
        return 1.0 / sum;
    }

    // === Voltage Divider ===

    /**
     * Output voltage of a voltage divider.
     * Vout = Vin * R2 / (R1 + R2)
     */
    public static double voltageDivider(double vin, double r1, double r2) {
        return vin * r2 / (r1 + r2);
    }

    /**
     * Current divider for parallel resistors.
     * I2 = Itotal * R1 / (R1 + R2)
     */
    public static double currentDivider(double iTotalAmps, double r1, double r2) {
        return iTotalAmps * r1 / (r1 + r2);
    }

    // === Time Constants ===

    /**
     * RC time constant.
     * τ = RC (seconds)
     */
    public static double rcTimeConstant(double resistanceOhms, double capacitanceFarads) {
        return resistanceOhms * capacitanceFarads;
    }

    /**
     * RL time constant.
     * τ = L/R (seconds)
     */
    public static double rlTimeConstant(double inductanceHenries, double resistanceOhms) {
        return inductanceHenries / resistanceOhms;
    }

    /**
     * Capacitor voltage during charging.
     * V(t) = Vmax * (1 - e^(-t/RC))
     */
    public static double capacitorChargingVoltage(double vmax, double timeS, double tau) {
        return vmax * (1 - Math.exp(-timeS / tau));
    }

    /**
     * Capacitor voltage during discharging.
     * V(t) = V0 * e^(-t/RC)
     */
    public static double capacitorDischargingVoltage(double v0, double timeS, double tau) {
        return v0 * Math.exp(-timeS / tau);
    }

    // === AC Impedance ===

    /**
     * Capacitive reactance.
     * Xc = 1 / (2πfC)
     */
    public static double capacitiveReactance(double frequencyHz, double capacitanceFarads) {
        return 1.0 / (2 * Math.PI * frequencyHz * capacitanceFarads);
    }

    /**
     * Inductive reactance.
     * Xl = 2πfL
     */
    public static double inductiveReactance(double frequencyHz, double inductanceHenries) {
        return 2 * Math.PI * frequencyHz * inductanceHenries;
    }

    /**
     * Impedance magnitude for RLC series circuit.
     * |Z| = √(R² + (Xl - Xc)²)
     */
    public static double impedanceMagnitude(double resistance, double inductiveReactance, double capacitiveReactance) {
        double x = inductiveReactance - capacitiveReactance;
        return Math.sqrt(resistance * resistance + x * x);
    }

    /**
     * Resonant frequency for LC circuit.
     * f0 = 1 / (2π√LC)
     */
    public static double resonantFrequency(double inductanceHenries, double capacitanceFarads) {
        return 1.0 / (2 * Math.PI * Math.sqrt(inductanceHenries * capacitanceFarads));
    }

    /**
     * Quality factor for RLC circuit.
     * Q = (1/R) * √(L/C)
     */
    public static double qualityFactor(double resistance, double inductance, double capacitance) {
        return (1.0 / resistance) * Math.sqrt(inductance / capacitance);
    }
}
