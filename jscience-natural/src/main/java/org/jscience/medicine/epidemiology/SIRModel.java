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
package org.jscience.medicine.epidemiology;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;

/**
 * SIR (Susceptible-Infected-Recovered) epidemic model.
 * <p>
 * Classic compartmental model for infectious disease spread:
 * <ul>
 * <li>S (Susceptible) - can catch the disease</li>
 * <li>I (Infected) - currently infected and infectious</li>
 * <li>R (Recovered/Removed) - immune or dead</li>
 * </ul>
 * </p>
 * <p>
 * Differential equations:
 * dS/dt = -βSI/N, dI/dt = βSI/N - γI, dR/dt = γI
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class SIRModel {

    private final Quantity<Frequency> beta; // Transmission rate
    private final Quantity<Frequency> gamma; // Recovery rate
    private final int population;

    private double S; // Susceptible
    private double I; // Infected
    private double R; // Recovered
    private Quantity<Time> time;

    /**
     * Creates SIR model with specified parameters.
     * 
     * @param population       total population
     * @param initialInfected  initial number of infected
     * @param transmissionRate β - probability of transmission per contact
     * @param recoveryRate     γ - 1/average infection duration
     */
    public SIRModel(int population, int initialInfected, Quantity<Frequency> transmissionRate,
            Quantity<Frequency> recoveryRate) {
        this.population = population;
        this.beta = transmissionRate;
        this.gamma = recoveryRate;

        this.I = initialInfected;
        this.S = population - initialInfected;
        this.R = 0;
        this.time = Quantities.create(0, Units.SECOND);
    }

    /**
     * Returns the basic reproduction number R₀ = β/γ.
     * If R₀ > 1, epidemic spreads; if R₀ < 1, epidemic dies out.
     */
    public double getR0() {
        return beta.divide(gamma).getValue().doubleValue();
    }

    /**
     * Returns herd immunity threshold (1 - 1/R₀).
     */
    public double getHerdImmunityThreshold() {
        double r0 = getR0();
        return r0 > 1 ? 1 - 1 / r0 : 0;
    }

    /**
     * Advances simulation by dt using Euler method.
     * 
     * @param dt time step
     */
    public void step(Quantity<Time> dt) {
        double N = population;
        double dtSec = dt.to(Units.SECOND).getValue().doubleValue();
        double betaVal = beta.to(Units.HERTZ).getValue().doubleValue();
        double gammaVal = gamma.to(Units.HERTZ).getValue().doubleValue();

        double dS = -betaVal * S * I / N;
        double dI = betaVal * S * I / N - gammaVal * I;
        double dR = gammaVal * I;

        S += dS * dtSec;
        I += dI * dtSec;
        R += dR * dtSec;
        time = time.add(dt);

        // Ensure non-negative
        S = Math.max(0, S);
        I = Math.max(0, I);
        R = Math.max(0, R);
    }

    /**
     * Runs simulation for specified duration.
     * 
     * @param duration total simulation time
     * @param dt       time step
     * @return array of [time(days), S, I, R] rows. Time is normalized to days for
     *         output convenience/legacy.
     */
    public double[][] simulate(Quantity<Time> duration, Quantity<Time> dt) {
        double totalSeconds = duration.to(Units.SECOND).getValue().doubleValue();
        double dtSeconds = dt.to(Units.SECOND).getValue().doubleValue();

        int steps = (int) (totalSeconds / dtSeconds) + 1;
        double[][] results = new double[steps][4];

        // Reset
        reset();

        for (int i = 0; i < steps; i++) {
            results[i] = new double[] { time.to(Units.DAY).getValue().doubleValue(), S, I, R };
            step(dt);
        }

        return results;
    }

    /**
     * Resets model to initial state.
     */
    public void reset() {
        this.I = population > 0 ? 1 : 0;
        this.S = population - I;
        this.R = 0;
        this.time = Quantities.create(0, Units.SECOND);
    }

    /**
     * Returns peak infection day (approximate).
     */
    public Quantity<Time> getPeakTime() {
        double r0 = getR0();
        if (r0 <= 1)
            return Quantities.create(0, Units.SECOND);

        double betaVal = beta.to(Units.HERTZ).getValue().doubleValue();
        double gammaVal = gamma.to(Units.HERTZ).getValue().doubleValue();

        // Approximate: peak occurs when S = N/R0
        // Analytical time to peak is complex, but approximation t_peak ~ (ln(N) -
        // ln(I0)) / (beta - gamma) often used for initial growth
        // For general SIR peak formula: t = integral... usually solved numerically.
        // Using the linearized growth approximation from previous code:
        // Math.log(population * (r0 - 1)) / (beta - gamma);
        // Beware units. (beta-gamma) is in Hz (1/s). Result is in Seconds.

        double val = Math.log(population * (r0 - 1)) / (betaVal - gammaVal);
        return Quantities.create(val, Units.SECOND);
    }

    /**
     * Returns expected final size of epidemic (fraction infected).
     */
    public double getFinalSize() {
        double r0 = getR0();
        if (r0 <= 1)
            return 0;

        // Solve R_∞ = 1 - exp(-R0 * R_∞) numerically
        double r = 0.9;
        for (int i = 0; i < 100; i++) {
            r = 1 - Math.exp(-r0 * r);
        }
        return r;
    }

    // Getters
    public double getSusceptible() {
        return S;
    }

    public double getInfected() {
        return I;
    }

    public double getRecovered() {
        return R;
    }

    public Quantity<Time> getTime() {
        return time;
    }

    public int getPopulation() {
        return population;
    }

    public Quantity<Frequency> getBeta() {
        return beta;
    }

    public Quantity<Frequency> getGamma() {
        return gamma;
    }

    @Override
    public String toString() {
        return String.format("SIRModel{N=%d, R0=%.2f, S=%.0f, I=%.0f, R=%.0f, t=%s}",
                population, getR0(), S, I, R, time);
    }

    /**
     * Creates model for COVID-19 like parameters.
     */
    public static SIRModel covid19Like(int population, int initialCases) {
        // R0 approx 3. gamma approx 0.1/day. beta = 0.3/day.
        return new SIRModel(population, initialCases,
                Quantities.create(0.3 / 86400.0, Units.HERTZ),
                Quantities.create(0.1 / 86400.0, Units.HERTZ));
    }

    /**
     * Creates model for influenza like parameters.
     */
    public static SIRModel influenzaLike(int population, int initialCases) {
        // R0 ~ 1.4. Beta ~ 0.2/day, Gamma ~ 0.14/day
        return new SIRModel(population, initialCases,
                Quantities.create(0.2 / 86400.0, Units.HERTZ),
                Quantities.create(0.14 / 86400.0, Units.HERTZ));
    }

    /**
     * Creates model for measles like parameters.
     */
    public static SIRModel measlesLike(int population, int initialCases) {
        // R0 ~ 14. Beta ~ 1.8/day, Gamma ~ 0.125/day
        return new SIRModel(population, initialCases,
                Quantities.create(1.8 / 86400.0, Units.HERTZ),
                Quantities.create(0.125 / 86400.0, Units.HERTZ));
    }
}
