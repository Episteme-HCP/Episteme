/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Heat transfer calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HeatTransfer {

    /** Stefan-Boltzmann constant (W/(mÃ‚Â²Ã‚Â·KÃ¢ÂÂ´)) */
    public static final Real STEFAN_BOLTZMANN = Real.of(5.670374419e-8);

    /**
     * Conductive heat transfer (Fourier's law).
     * Q = k * A * ÃŽâ€T / d
     */
    public static Real conduction(Real k, Real area, Real deltaT, Real thickness) {
        return k.multiply(area).multiply(deltaT).divide(thickness);
    }

    /**
     * Convective heat transfer (Newton's law of cooling).
     * Q = h * A * (T_s - T_Ã¢Ë†Å¾)
     */
    public static Real convection(Real h, Real area, Real deltaT) {
        return h.multiply(area).multiply(deltaT);
    }

    /**
     * Radiative heat transfer (Stefan-Boltzmann law).
     * Q = ÃŽÂµ * ÃÆ’ * A * (TÃ¢â€šÂÃ¢ÂÂ´ - TÃ¢â€šâ€šÃ¢ÂÂ´)
     */
    public static Real radiation(Real emissivity, Real area, Real T1, Real T2) {
        return emissivity.multiply(STEFAN_BOLTZMANN).multiply(area)
                .multiply(T1.pow(4).subtract(T2.pow(4)));
    }

    /**
     * Thermal resistance for conduction.
     * R = d / (k * A)
     */
    public static Real thermalResistanceConduction(Real thickness, Real k, Real area) {
        return thickness.divide(k.multiply(area));
    }

    /**
     * Thermal resistance for convection.
     * R = 1 / (h * A)
     */
    public static Real thermalResistanceConvection(Real h, Real area) {
        return Real.ONE.divide(h.multiply(area));
    }

    /**
     * Overall heat transfer coefficient for composite wall.
     * 1/U = 1/hÃ¢â€šÂ + ÃŽÂ£(d_i/k_i) + 1/hÃ¢â€šâ€š
     */
    public static Real overallHeatTransferCoefficient(Real h1, Real h2,
            Vector<Real> thicknesses, Vector<Real> conductivities) {
        Real resistance = Real.ONE.divide(h1).add(Real.ONE.divide(h2));
        for (int i = 0; i < thicknesses.dimension(); i++) {
            resistance = resistance.add(thicknesses.get(i).divide(conductivities.get(i)));
        }
        return Real.ONE.divide(resistance);
    }

    /**
     * Log Mean Temperature Difference (LMTD) for heat exchangers.
     */
    public static Real lmtd(Real deltaT1, Real deltaT2) {
        Real diff = deltaT1.subtract(deltaT2).abs();
        if (diff.compareTo(Real.of(0.01)) < 0) {
            return deltaT1.add(deltaT2).divide(Real.TWO);
        }
        return deltaT1.subtract(deltaT2).divide(deltaT1.divide(deltaT2).log());
    }

    /**
     * Heat exchanger effectiveness (NTU method).
     */
    public static Real heatExchangerEffectiveness(Real NTU, Real capacityRatio) {
        if (capacityRatio.compareTo(Real.of(0.01)) < 0) {
            return Real.ONE.subtract(NTU.negate().exp());
        }
        Real exp = NTU.negate().multiply(Real.ONE.subtract(capacityRatio)).exp();
        return Real.ONE.subtract(exp).divide(Real.ONE.subtract(capacityRatio.multiply(exp)));
    }

    /**
     * Biot number: ratio of convection to conduction.
     */
    public static Real biotNumber(Real h, Real characteristicLength, Real k) {
        return h.multiply(characteristicLength).divide(k);
    }

    /**
     * Fourier number: dimensionless time for transient conduction.
     */
    public static Real fourierNumber(Real thermalDiffusivity, Real time, Real length) {
        return thermalDiffusivity.multiply(time).divide(length.pow(2));
    }

    /**
     * Nusselt number for turbulent flow in a pipe (Dittus-Boelter).
     */
    public static Real nusseltDittusBoelter(Real Re, Real Pr, boolean heating) {
        Real n = heating ? Real.of(0.4) : Real.of(0.3);
        return Real.of(0.023).multiply(Re.pow(Real.of(0.8))).multiply(Pr.pow(n));
    }

    // --- Thermal conductivities (W/(mÃ‚Â·K)) ---
    public static final Real K_COPPER = Real.of(401);
    public static final Real K_ALUMINUM = Real.of(237);
    public static final Real K_STEEL = Real.of(50);
    public static final Real K_GLASS = Real.ONE;
    public static final Real K_CONCRETE = Real.of(1.4);
    public static final Real K_WOOD = Real.of(0.15);
    public static final Real K_AIR = Real.of(0.026);
    public static final Real K_WATER = Real.of(0.6);
}


