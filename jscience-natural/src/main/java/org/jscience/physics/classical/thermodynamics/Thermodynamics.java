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
package org.jscience.physics.classical.thermodynamics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.*;
import org.jscience.physics.PhysicalConstants;

/**
 * Thermodynamics equations and models.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Thermodynamics {

    /** Ideal gas law: PV = nRT */
    public static Quantity<Pressure> idealGasPressure(
            Quantity<AmountOfSubstance> n,
            Quantity<Temperature> T,
            Quantity<Volume> V) {
        Quantity<?> R = PhysicalConstants.GAS_CONSTANT;
        return n.multiply(R).multiply(T).divide(V).asType(Pressure.class);
    }

    /** Internal energy change: ΔU = nCvΔT */
    public static Quantity<Energy> internalEnergyChange(
            Quantity<AmountOfSubstance> n,
            Real Cv,
            Quantity<Temperature> deltaT) {
        return n.multiply(Cv).multiply(deltaT).asType(Energy.class);
    }

    /** Carnot efficiency: η = 1 - Tc/Th */
    public static Real carnotEfficiency(Quantity<Temperature> Tcold, Quantity<Temperature> Thot) {
        Real tc = Tcold.getValue();
        Real th = Thot.getValue();
        return Real.ONE.subtract(tc.divide(th));
    }

    /** Entropy change for ideal gas */
    public static Real entropyChange(
            Quantity<AmountOfSubstance> n,
            Real Cv,
            Quantity<Temperature> T1,
            Quantity<Temperature> T2,
            Quantity<Volume> V1,
            Quantity<Volume> V2) {
        Real nVal = n.getValue();
        Real t1 = T1.getValue();
        Real t2 = T2.getValue();
        Real v1 = V1.getValue();
        Real v2 = V2.getValue();
        Real R = PhysicalConstants.GAS_CONSTANT.getValue();

        Real term1 = nVal.multiply(Cv).multiply(t2.divide(t1).log());
        Real term2 = nVal.multiply(R).multiply(v2.divide(v1).log());

        return term1.add(term2);
    }

    /** Ideal gas law (solve for unknown): PV = nRT */
    public static Real idealGasLaw(Real P, Real V, Real n, Real T) {
        Real R = PhysicalConstants.GAS_CONSTANT.getValue();
        if (P.isZero())
            return n.multiply(R).multiply(T).divide(V);
        if (V.isZero())
            return n.multiply(R).multiply(T).divide(P);
        if (n.isZero())
            return P.multiply(V).divide(R.multiply(T));
        if (T.isZero())
            return P.multiply(V).divide(n.multiply(R));
        return n.multiply(R).multiply(T).divide(V);
    }

    /** Otto cycle efficiency: η = 1 - 1/r^(γ-1) */
    public static Real ottoEfficiency(Real compressionRatio, Real gamma) {
        return Real.ONE.subtract(Real.ONE.divide(compressionRatio.pow(gamma.subtract(Real.ONE))));
    }

    /** Diesel cycle efficiency */
    public static Real dieselEfficiency(Real compressionRatio, Real cutoffRatio, Real gamma) {
        Real term1 = Real.ONE.divide(compressionRatio.pow(gamma.subtract(Real.ONE)));
        Real term2 = cutoffRatio.pow(gamma).subtract(Real.ONE)
                .divide(gamma.multiply(cutoffRatio.subtract(Real.ONE)));
        return Real.ONE.subtract(term1.multiply(term2));
    }
}