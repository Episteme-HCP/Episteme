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
package org.jscience.medicine.pharmacology;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Time;

/**
 * Pharmacokinetics calculations for drug concentration modeling.
 * <p>
 * Provides one-compartment model calculations including:
 * <ul>
 * <li>First-order elimination</li>
 * <li>Half-life calculations</li>
 * <li>Steady-state concentrations</li>
 * <li>Loading dose calculations</li>
 * <li>Clearance and volume of distribution</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class Pharmacokinetics {

    private final double ke; // Elimination rate constant (1/h)
    private final double vd; // Volume of distribution (L)
    private final double bioavailability; // Fraction absorbed (0-1)

    /**
     * Creates pharmacokinetic model.
     * 
     * @param eliminationRateConstant ke in 1/hour
     * @param volumeOfDistribution    Vd in liters
     * @param bioavailability         F (0-1, typically 1 for IV)
     */
    public Pharmacokinetics(double eliminationRateConstant, double volumeOfDistribution, double bioavailability) {
        this.ke = eliminationRateConstant;
        this.vd = volumeOfDistribution;
        this.bioavailability = bioavailability;
    }

    /**
     * Creates model from half-life and volume.
     */
    public static Pharmacokinetics fromHalfLife(double halfLifeHours, double volumeL, double bioavailability) {
        double ke = Math.log(2) / halfLifeHours;
        return new Pharmacokinetics(ke, volumeL, bioavailability);
    }

    /**
     * Elimination half-life.
     * t½ = ln(2) / ke
     */
    public Quantity<Time> getHalfLife() {
        double hours = Math.log(2) / ke;
        return Quantities.create(hours * 3600, Units.SECOND);
    }

    /**
     * Clearance.
     * CL = ke * Vd
     * 
     * @return clearance in L/h
     */
    public double getClearance() {
        return ke * vd;
    }

    /**
     * Plasma concentration after single IV bolus dose.
     * C(t) = (D / Vd) * e^(-ke*t)
     * 
     * @param doseAmount dose in mg
     * @param timeHours  time since dose in hours
     * @return concentration in mg/L
     */
    public double concentrationAfterBolus(double doseAmount, double timeHours) {
        double c0 = doseAmount / vd;
        return c0 * Math.exp(-ke * timeHours);
    }

    /**
     * Plasma concentration after oral dose.
     * Includes absorption phase with ka.
     * C(t) = (F*D*ka / Vd*(ka-ke)) * (e^(-ke*t) - e^(-ka*t))
     * 
     * @param doseAmount     dose in mg
     * @param timeHours      time since dose
     * @param absorptionRate ka in 1/h
     * @return concentration in mg/L
     */
    public double concentrationAfterOral(double doseAmount, double timeHours, double absorptionRate) {
        double ka = absorptionRate;
        double factor = (bioavailability * doseAmount * ka) / (vd * (ka - ke));
        return factor * (Math.exp(-ke * timeHours) - Math.exp(-ka * timeHours));
    }

    /**
     * Time to peak concentration after oral dose.
     * tmax = ln(ka/ke) / (ka - ke)
     */
    public double timeToPeak(double absorptionRate) {
        double ka = absorptionRate;
        return Math.log(ka / ke) / (ka - ke);
    }

    /**
     * Steady-state average concentration with repeated dosing.
     * Css,avg = (F*D) / (CL * τ)
     * 
     * @param doseAmount          dose per administration
     * @param dosingIntervalHours τ (tau) - time between doses
     * @return average steady-state concentration
     */
    public double steadyStateAverage(double doseAmount, double dosingIntervalHours) {
        return (bioavailability * doseAmount) / (getClearance() * dosingIntervalHours);
    }

    /**
     * Steady-state peak concentration.
     * Css,max = (F*D/Vd) / (1 - e^(-ke*τ))
     */
    public double steadyStatePeak(double doseAmount, double dosingIntervalHours) {
        double numerator = bioavailability * doseAmount / vd;
        double denominator = 1 - Math.exp(-ke * dosingIntervalHours);
        return numerator / denominator;
    }

    /**
     * Steady-state trough concentration.
     * Css,min = Css,max * e^(-ke*τ)
     */
    public double steadyStateTrough(double doseAmount, double dosingIntervalHours) {
        return steadyStatePeak(doseAmount, dosingIntervalHours) * Math.exp(-ke * dosingIntervalHours);
    }

    /**
     * Loading dose to achieve target concentration immediately.
     * LD = Css * Vd / F
     * 
     * @param targetConcentration desired concentration (mg/L)
     * @return loading dose in mg
     */
    public double loadingDose(double targetConcentration) {
        return targetConcentration * vd / bioavailability;
    }

    /**
     * Maintenance dose to maintain steady state.
     * MD = Css * CL * τ / F
     */
    public double maintenanceDose(double targetConcentration, double dosingIntervalHours) {
        return targetConcentration * getClearance() * dosingIntervalHours / bioavailability;
    }

    /**
     * Time to reach fraction of steady state.
     * t = -ln(1-f) / ke
     * 
     * @param fraction fraction of steady state (e.g., 0.9 for 90%)
     * @return time in hours
     */
    public double timeToSteadyStateFraction(double fraction) {
        return -Math.log(1 - fraction) / ke;
    }

    /**
     * Number of half-lives to reach steady state (≈ 4-5).
     */
    public double halfLivesToSteadyState() {
        return 4.32; // ~97% of steady state
    }

    /**
     * Drug accumulation factor.
     * R = 1 / (1 - e^(-ke*τ))
     */
    public double accumulationFactor(double dosingIntervalHours) {
        return 1.0 / (1 - Math.exp(-ke * dosingIntervalHours));
    }

    public double getKe() {
        return ke;
    }

    public double getVd() {
        return vd;
    }

    public double getBioavailability() {
        return bioavailability;
    }

    @Override
    public String toString() {
        return String.format("Pharmacokinetics{ke=%.4f/h, Vd=%.1fL, F=%.2f, t½=%.1fh, CL=%.2fL/h}",
                ke, vd, bioavailability, Math.log(2) / ke, getClearance());
    }
}
