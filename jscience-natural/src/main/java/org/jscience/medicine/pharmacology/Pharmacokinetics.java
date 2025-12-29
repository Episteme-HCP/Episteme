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

package org.jscience.medicine.pharmacology;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Volume;

/**
 * Pharmacokinetics calculations for drug concentration modeling.
 * <p>
 * Provides one-compartment model calculations using type-safe Quantities:
 * <ul>
 * <li>First-order elimination</li>
 * <li>Half-life calculations</li>
 * <li>Steady-state concentrations</li>
 * <li>Loading/Maintenance dose calculations</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Pharmacokinetics {

    private final Quantity<Frequency> ke; // Elimination rate constant
    private final Quantity<Volume> vd; // Volume of distribution
    private final double bioavailability; // Fraction absorbed (0-1)

    /**
     * Creates pharmacokinetic model.
     * 
     * @param eliminationRateConstant ke (e.g., 0.1/h)
     * @param volumeOfDistribution    Vd (e.g., 10 L)
     * @param bioavailability         F (0-1, typically 1 for IV)
     */
    public Pharmacokinetics(Quantity<Frequency> eliminationRateConstant,
            Quantity<Volume> volumeOfDistribution,
            double bioavailability) {
        this.ke = eliminationRateConstant;
        this.vd = volumeOfDistribution;
        this.bioavailability = bioavailability;
    }

    /**
     * Creates model from half-life and volume.
     */
    public static Pharmacokinetics fromHalfLife(Quantity<Time> halfLife, Quantity<Volume> volume,
            double bioavailability) {
        // ke = ln(2) / t½
        // Calculate value in 1/s (Hertz) then create Quantity
        double keVal = Math.log(2) / halfLife.to(Units.SECOND).getValue().doubleValue();
        Quantity<Frequency> ke = Quantities.create(keVal, Units.HERTZ);
        return new Pharmacokinetics(ke, volume, bioavailability);
    }

    /**
     * Elimination half-life.
     * t½ = ln(2) / ke
     */
    public Quantity<Time> getHalfLife() {
        double keVal = ke.to(Units.HERTZ).getValue().doubleValue();
        return Quantities.create(Math.log(2) / keVal, Units.SECOND);
    }

    /**
     * Clearance.
     * CL = ke * Vd
     * Returns Volume/Time (e.g. L/h) - Generic Quantity as no specific
     * Dimension/Type for flow rate in standard set yet?
     * Actually VolumetricFlowRate exists in some extensions, but generic Quantity
     * is safe.
     */
    public Quantity<?> getClearance() {
        return vd.multiply(ke);
    }

    /**
     * Plasma concentration after single IV bolus dose.
     * C(t) = (D / Vd) * e^(-ke*t)
     */
    public Quantity<?> concentrationAfterBolus(Quantity<Mass> dose, Quantity<Time> time) {
        Quantity<?> c0 = dose.divide(vd); // Concentration
        double exponent = -getDimensionlessExponent(ke, time);
        return c0.multiply(Math.exp(exponent));
    }

    /**
     * Plasma concentration after oral dose.
     * C(t) = (F*D*ka / Vd*(ka-ke)) * (e^(-ke*t) - e^(-ka*t))
     */
    public Quantity<?> concentrationAfterOral(Quantity<Mass> dose, Quantity<Time> time,
            Quantity<Frequency> absorptionRate) {
        Quantity<Frequency> ka = absorptionRate;
        // factor = (F * D * ka) / (Vd * (ka - ke))
        // Dimensions: (1 * M * T^-1) / (L^3 * T^-1) = M/L^3 = Concentration

        Quantity<?> numerator = dose.multiply(ka).multiply(bioavailability);
        Quantity<?> denominator = vd.multiply(ka.subtract(ke));

        Quantity<?> factor = numerator.divide(denominator);

        double keT = getDimensionlessExponent(ke, time);
        double kaT = getDimensionlessExponent(ka, time);

        return factor.multiply(Math.exp(-keT) - Math.exp(-kaT));
    }

    /**
     * Time to peak concentration after oral dose.
     * tmax = ln(ka/ke) / (ka - ke)
     */
    public Quantity<Time> timeToPeak(Quantity<Frequency> absorptionRate) {
        Quantity<Frequency> ka = absorptionRate;
        double kaVal = ka.to(Units.HERTZ).getValue().doubleValue();
        double keVal = ke.to(Units.HERTZ).getValue().doubleValue();

        if (Math.abs(kaVal - keVal) < 1e-15)
            return Quantities.create(0, Units.SECOND); // Undefined if equal

        double tMaxSeconds = Math.log(kaVal / keVal) / (kaVal - keVal);
        return Quantities.create(tMaxSeconds, Units.SECOND);
    }

    /**
     * Steady-state average concentration with repeated dosing.
     * Css,avg = (F*D) / (Math.abs(CL) * τ)
     * CL * tau -> Volume
     * Dose / Volume -> Concentration
     */
    public Quantity<?> steadyStateAverage(Quantity<Mass> dose, Quantity<Time> dosingInterval) {
        Quantity<?> clearance = getClearance(); // L/s
        Quantity<?> volCleared = clearance.multiply(dosingInterval); // L
        return dose.multiply(bioavailability).divide(volCleared);
    }

    /**
     * Loading dose to achieve target concentration.
     * LD = Css * Vd / F
     * Css (Mass/Vol) * Vol -> Mass.
     */
    public Quantity<Mass> loadingDose(Quantity<?> targetConcentration) {
        // Check dimensions strictly? Or assume targetConcentration is M/L^3
        return targetConcentration.multiply(vd).divide(bioavailability).asType(Mass.class);
    }

    // Helper
    private double getDimensionlessExponent(Quantity<Frequency> rate, Quantity<Time> time) {
        return rate.to(Units.HERTZ).getValue().doubleValue() * time.to(Units.SECOND).getValue().doubleValue();
    }

    public Quantity<Frequency> getKe() {
        return ke;
    }

    public Quantity<Volume> getVd() {
        return vd;
    }

    public double getBioavailability() {
        return bioavailability;
    }

    @Override
    public String toString() {
        return String.format("Pharmacokinetics{ke=%s, Vd=%s, F=%.2f}", ke, vd, bioavailability);
    }
}
