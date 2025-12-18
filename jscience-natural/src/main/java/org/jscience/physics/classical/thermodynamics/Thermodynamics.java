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
package org.jscience.physics.classical.thermodynamics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.*;
import org.jscience.physics.PhysicalConstants;

/**
 * Thermodynamics equations and models.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class Thermodynamics {

    /**
     * Ideal gas law: PV = nRT
     */
    public static Quantity<Pressure> idealGasPressure(
            Quantity<AmountOfSubstance> n,
            Quantity<Temperature> T,
            Quantity<Volume> V) {
        // P = nRT/V
        Quantity<?> R = PhysicalConstants.GAS_CONSTANT;
        return n.multiply(R).multiply(T).divide(V).asType(Pressure.class);
    }

    /**
     * Internal energy change: ΔU = nCvΔT
     */
    public static Quantity<Energy> internalEnergyChange(
            Quantity<AmountOfSubstance> n,
            Real Cv, // Molar heat capacity at constant volume
            Quantity<Temperature> deltaT) {
        return n.multiply(Cv).multiply(deltaT).asType(Energy.class);
    }

    /**
     * Carnot efficiency: η = 1 - Tc/Th
     */
    public static Real carnotEfficiency(
            Quantity<Temperature> Tcold,
            Quantity<Temperature> Thot) {
        Real tc = Real.of(Tcold.getValue().doubleValue());
        Real th = Real.of(Thot.getValue().doubleValue());
        return Real.ONE.subtract(tc.divide(th));
    }

    /**
     * Entropy change for ideal gas: ΔS = nCv ln(T2/T1) + nR ln(V2/V1)
     */
    public static Real entropyChange(
            Quantity<AmountOfSubstance> n,
            Real Cv,
            Quantity<Temperature> T1,
            Quantity<Temperature> T2,
            Quantity<Volume> V1,
            Quantity<Volume> V2) {
        Real nVal = Real.of(n.getValue().doubleValue());
        Real t1 = Real.of(T1.getValue().doubleValue());
        Real t2 = Real.of(T2.getValue().doubleValue());
        Real v1 = Real.of(V1.getValue().doubleValue());
        Real v2 = Real.of(V2.getValue().doubleValue());
        Real R = PhysicalConstants.GAS_CONSTANT.getValue();

        Real term1 = nVal.multiply(Cv).multiply(Real.of(Math.log(t2.divide(t1).doubleValue())));
        Real term2 = nVal.multiply(R).multiply(Real.of(Math.log(v2.divide(v1).doubleValue())));

        return term1.add(term2);
    }
    // --- Methods merged from IdealGas2 ---

    /**
     * Ideal gas law: PV = nRT (Double precision)
     */
    public static double idealGasLaw(double P, double V, double n, double T) {
        double R = PhysicalConstants.GAS_CONSTANT.getValue().doubleValue();
        if (P == 0)
            return n * R * T / V;
        if (V == 0)
            return n * R * T / P;
        if (n == 0)
            return P * V / (R * T);
        if (T == 0)
            return P * V / (n * R);
        return n * R * T / V;
    }

    /**
     * Otto cycle efficiency.
     */
    public static double ottoEfficiency(double compressionRatio, double gamma) {
        return 1.0 - 1.0 / Math.pow(compressionRatio, gamma - 1.0);
    }

    /**
     * Diesel cycle efficiency.
     */
    public static double dieselEfficiency(double compressionRatio, double cutoffRatio, double gamma) {
        double term1 = 1.0 / Math.pow(compressionRatio, gamma - 1);
        double term2 = (Math.pow(cutoffRatio, gamma) - 1) / (gamma * (cutoffRatio - 1));
        return 1 - term1 * term2;
    }

    /**
     * Carnot efficiency (double).
     */
    public static double carnotEfficiency(double Thot, double Tcold) {
        return 1.0 - Tcold / Thot;
    }
}
