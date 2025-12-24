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
package org.jscience.medicine.epidemiology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;

/**
 * SEIRS (Susceptible-Exposed-Infected-Recovered-Susceptible) epidemic model.
 * <p>
 * Extends SEIR model by allowing loss of immunity, causing recovered
 * individuals to return to the susceptible pool over time.
 * </p>
 * 
 * <pre>
 * S → E → I → R → S
 * 
 * dS/dt = -βSI/N + ξR
 * dE/dt = βSI/N - σE
 * dI/dt = σE - γI
 * dR/dt = γI - ξR
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SEIRSModel {

    private final Quantity<Frequency> beta; // Transmission rate
    private final Quantity<Frequency> sigma; // Incubation rate
    private final Quantity<Frequency> gamma; // Recovery rate
    private final Quantity<Frequency> xi; // Immunity loss rate
    private final int population;

    private Real S;
    private Real E;
    private Real I;
    private Real R;
    private Quantity<Time> time;

    /**
     * Creates a SEIRS model.
     *
     * @param population       total population
     * @param initialExposed   initial exposed individuals
     * @param initialInfected  initial infectious individuals
     * @param transmissionRate β
     * @param incubationRate   σ (1/latent period)
     * @param recoveryRate     γ (1/infectious period)
     * @param immunityLossRate ξ (1/immunity duration)
     */
    public SEIRSModel(int population, int initialExposed, int initialInfected,
            Quantity<Frequency> transmissionRate,
            Quantity<Frequency> incubationRate,
            Quantity<Frequency> recoveryRate,
            Quantity<Frequency> immunityLossRate) {
        this.population = population;
        this.beta = transmissionRate;
        this.sigma = incubationRate;
        this.gamma = recoveryRate;
        this.xi = immunityLossRate;

        this.E = Real.of(initialExposed);
        this.I = Real.of(initialInfected);
        this.S = Real.of(population - initialExposed - initialInfected);
        this.R = Real.ZERO;
        this.time = Quantities.create(0, Units.SECOND);
    }

    /** Basic reproduction number R₀ = β/γ */
    public Real getR0() {
        return beta.divide(gamma).getValue();
    }

    /** Average immunity duration in days */
    public double getImmunityDuration() {
        return 1.0 / (xi.to(Units.HERTZ).getValue().doubleValue() * 86400);
    }

    /** Advance simulation by dt */
    public void step(Quantity<Time> dt) {
        Real N = Real.of(population);
        Real dtSec = Real.of(dt.to(Units.SECOND).getValue().doubleValue());
        Real betaVal = beta.to(Units.HERTZ).getValue();
        Real sigmaVal = sigma.to(Units.HERTZ).getValue();
        Real gammaVal = gamma.to(Units.HERTZ).getValue();
        Real xiVal = xi.to(Units.HERTZ).getValue();

        Real lambda = betaVal.multiply(I).divide(N);

        Real dS = lambda.negate().multiply(S).add(xiVal.multiply(R));
        Real dE = lambda.multiply(S).subtract(sigmaVal.multiply(E));
        Real dI = sigmaVal.multiply(E).subtract(gammaVal.multiply(I));
        Real dR = gammaVal.multiply(I).subtract(xiVal.multiply(R));

        S = S.add(dS.multiply(dtSec)).max(Real.ZERO);
        E = E.add(dE.multiply(dtSec)).max(Real.ZERO);
        I = I.add(dI.multiply(dtSec)).max(Real.ZERO);
        R = R.add(dR.multiply(dtSec)).max(Real.ZERO);
        time = time.add(dt);
    }

    /** Run simulation for duration */
    public Real[][] simulate(Quantity<Time> duration, Quantity<Time> dt) {
        double totalSeconds = duration.to(Units.SECOND).getValue().doubleValue();
        double dtSeconds = dt.to(Units.SECOND).getValue().doubleValue();
        int steps = (int) (totalSeconds / dtSeconds) + 1;
        Real[][] results = new Real[steps][5];

        reset();

        for (int i = 0; i < steps; i++) {
            results[i] = new Real[] {
                    Real.of(time.to(Units.DAY).getValue().doubleValue()),
                    S, E, I, R
            };
            step(dt);
        }
        return results;
    }

    public void reset() {
        this.I = Real.of(1);
        this.E = Real.ZERO;
        this.S = Real.of(population).subtract(I);
        this.R = Real.ZERO;
        this.time = Quantities.create(0, Units.SECOND);
    }

    // Getters
    public Real getSusceptible() {
        return S;
    }

    public Real getExposed() {
        return E;
    }

    public Real getInfected() {
        return I;
    }

    public Real getRecovered() {
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

    public Quantity<Frequency> getSigma() {
        return sigma;
    }

    public Quantity<Frequency> getGamma() {
        return gamma;
    }

    public Quantity<Frequency> getXi() {
        return xi;
    }

    @Override
    public String toString() {
        return String.format("SEIRSModel{N=%d, R0=%.2f, immunity=%.0fd, S=%.0f, E=%.0f, I=%.0f, R=%.0f}",
                population, getR0().doubleValue(), getImmunityDuration(),
                S.doubleValue(), E.doubleValue(), I.doubleValue(), R.doubleValue());
    }

    // Factory methods

    /** Seasonal flu: ~6 month immunity */
    public static SEIRSModel seasonalFlu(int population, int initialCases) {
        return new SEIRSModel(population, 0, initialCases,
                Quantities.create(0.3 / 86400.0, Units.HERTZ), // β
                Quantities.create(0.5 / 86400.0, Units.HERTZ), // σ (2d latent)
                Quantities.create(0.2 / 86400.0, Units.HERTZ), // γ (5d infectious)
                Quantities.create(1.0 / (180 * 86400.0), Units.HERTZ)); // ξ (180d immunity)
    }

    /** Common cold: ~2 week immunity */
    public static SEIRSModel commonCold(int population, int initialCases) {
        return new SEIRSModel(population, 0, initialCases,
                Quantities.create(0.5 / 86400.0, Units.HERTZ),
                Quantities.create(1.0 / 86400.0, Units.HERTZ), // 1d latent
                Quantities.create(0.25 / 86400.0, Units.HERTZ), // 4d infectious
                Quantities.create(1.0 / (14 * 86400.0), Units.HERTZ)); // 14d immunity
    }

    /** COVID-19 variant with waning immunity (~1 year) */
    public static SEIRSModel covidWaning(int population, int initialCases) {
        return new SEIRSModel(population, 0, initialCases,
                Quantities.create(0.25 / 86400.0, Units.HERTZ),
                Quantities.create(0.2 / 86400.0, Units.HERTZ),
                Quantities.create(0.1 / 86400.0, Units.HERTZ),
                Quantities.create(1.0 / (365 * 86400.0), Units.HERTZ)); // 1 year immunity
    }
}
