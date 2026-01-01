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

package org.jscience.medicine.epidemiology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;

/**
 * SEIR (Susceptible-Exposed-Infected-Recovered) epidemic model.
 * <p>
 * Extends SIR model by adding an Exposed compartment representing
 * individuals who are infected but not yet infectious (latent period).
 * </p>
 *
 * <pre>
 * S Ã¢â€ â€™ E Ã¢â€ â€™ I Ã¢â€ â€™ R
 *
 * dS/dt = -ÃŽÂ²SI/N
 * dE/dt = ÃŽÂ²SI/N - ÃÆ’E
 * dI/dt = ÃÆ’E - ÃŽÂ³I
 * dR/dt = ÃŽÂ³I
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SEIRModel {

    private final Quantity<Frequency> beta; // Transmission rate
    private final Quantity<Frequency> sigma; // Incubation rate (1/latent period)
    private final Quantity<Frequency> gamma; // Recovery rate
    private final int population;

    private Real S; // Susceptible
    private Real E; // Exposed (latent)
    private Real I; // Infectious
    private Real R; // Recovered
    private Quantity<Time> time;

    /**
     * Creates a SEIR model.
     *
     * @param population       total population
     * @param initialExposed   initial exposed individuals
     * @param initialInfected  initial infectious individuals
     * @param transmissionRate ÃŽÂ² (contacts/time)
     * @param incubationRate   ÃÆ’ (1/latent period)
     * @param recoveryRate     ÃŽÂ³ (1/infectious period)
     */
    public SEIRModel(int population, int initialExposed, int initialInfected,
            Quantity<Frequency> transmissionRate,
            Quantity<Frequency> incubationRate,
            Quantity<Frequency> recoveryRate) {
        this.population = population;
        this.beta = transmissionRate;
        this.sigma = incubationRate;
        this.gamma = recoveryRate;

        this.E = Real.of(initialExposed);
        this.I = Real.of(initialInfected);
        this.S = Real.of(population - initialExposed - initialInfected);
        this.R = Real.ZERO;
        this.time = Quantities.create(0, Units.SECOND);
    }

    /** Basic reproduction number RÃ¢â€šâ‚¬ = ÃŽÂ²/ÃŽÂ³ */
    public Real getR0() {
        return beta.divide(gamma).getValue();
    }

    /** Effective reproduction number RÃ¢â€šâ€˜ = RÃ¢â€šâ‚¬ Ãƒâ€” S/N */
    public Real getEffectiveR() {
        return getR0().multiply(S).divide(Real.of(population));
    }

    /** Latent period (1/ÃÆ’) in days */
    public double getLatentPeriod() {
        return 1.0 / (sigma.to(Units.HERTZ).getValue().doubleValue() * 86400);
    }

    /** Infectious period (1/ÃŽÂ³) in days */
    public double getInfectiousPeriod() {
        return 1.0 / (gamma.to(Units.HERTZ).getValue().doubleValue() * 86400);
    }

    /** Advance simulation by dt using Euler method */
    public void step(Quantity<Time> dt) {
        Real N = Real.of(population);
        Real dtSec = Real.of(dt.to(Units.SECOND).getValue().doubleValue());
        Real betaVal = beta.to(Units.HERTZ).getValue();
        Real sigmaVal = sigma.to(Units.HERTZ).getValue();
        Real gammaVal = gamma.to(Units.HERTZ).getValue();

        // Force of infection
        Real lambda = betaVal.multiply(I).divide(N);

        // Derivatives
        Real dS = lambda.negate().multiply(S);
        Real dE = lambda.multiply(S).subtract(sigmaVal.multiply(E));
        Real dI = sigmaVal.multiply(E).subtract(gammaVal.multiply(I));
        Real dR = gammaVal.multiply(I);

        // Update compartments
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
        Real[][] results = new Real[steps][5]; // time, S, E, I, R

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

    @Override
    public String toString() {
        return String.format("SEIRModel{N=%d, R0=%.2f, S=%.0f, E=%.0f, I=%.0f, R=%.0f}",
                population, getR0().doubleValue(),
                S.doubleValue(), E.doubleValue(), I.doubleValue(), R.doubleValue());
    }

    // Factory methods for common diseases

    /** COVID-19 like: R0Ã¢â€°Ë†2.5, latent ~5d, infectious ~10d */
    public static SEIRModel covid19Like(int population, int initialCases) {
        return new SEIRModel(population, 0, initialCases,
                Quantities.create(0.25 / 86400.0, Units.HERTZ), // ÃŽÂ²
                Quantities.create(0.2 / 86400.0, Units.HERTZ), // ÃÆ’ (5d latent)
                Quantities.create(0.1 / 86400.0, Units.HERTZ)); // ÃŽÂ³ (10d infectious)
    }

    /** Influenza like: R0Ã¢â€°Ë†1.5, latent ~2d, infectious ~5d */
    public static SEIRModel influenzaLike(int population, int initialCases) {
        return new SEIRModel(population, 0, initialCases,
                Quantities.create(0.3 / 86400.0, Units.HERTZ),
                Quantities.create(0.5 / 86400.0, Units.HERTZ),
                Quantities.create(0.2 / 86400.0, Units.HERTZ));
    }

    /** Measles like: R0Ã¢â€°Ë†15, latent ~10d, infectious ~8d */
    public static SEIRModel measlesLike(int population, int initialCases) {
        return new SEIRModel(population, 0, initialCases,
                Quantities.create(1.875 / 86400.0, Units.HERTZ),
                Quantities.create(0.1 / 86400.0, Units.HERTZ),
                Quantities.create(0.125 / 86400.0, Units.HERTZ));
    }
}


