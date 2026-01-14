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

package org.jscience.medicine.epidemiology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;

/**
 * SIR (Susceptible-Infected-Recovered) epidemic model.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SIRModel {

    private final Quantity<Frequency> beta;
    private final Quantity<Frequency> gamma;
    private final int population;

    private Real S;
    private Real I;
    private Real R;
    private Quantity<Time> time;

    public SIRModel(int population, int initialInfected, Quantity<Frequency> transmissionRate,
            Quantity<Frequency> recoveryRate) {
        this.population = population;
        this.beta = transmissionRate;
        this.gamma = recoveryRate;

        this.I = Real.of(initialInfected);
        this.S = Real.of(population - initialInfected);
        this.R = Real.ZERO;
        this.time = Quantities.create(0, Units.SECOND);
    }

    /** Basic reproduction number RÃ¢â€šâ‚¬ = ÃŽÂ²/ÃŽÂ³ */
    public Real getR0() {
        return beta.divide(gamma).getValue();
    }

    /** Herd immunity threshold (1 - 1/RÃ¢â€šâ‚¬) */
    public Real getHerdImmunityThreshold() {
        Real r0 = getR0();
        return r0.compareTo(Real.ONE) > 0 ? Real.ONE.subtract(Real.ONE.divide(r0)) : Real.ZERO;
    }

    /** Advance simulation by dt using Euler method */
    public void step(Quantity<Time> dt) {
        Real N = Real.of(population);
        Real dtSec = Real.of(dt.to(Units.SECOND).getValue().doubleValue());
        Real betaVal = beta.to(Units.HERTZ).getValue();
        Real gammaVal = gamma.to(Units.HERTZ).getValue();

        Real dS = betaVal.negate().multiply(S).multiply(I).divide(N);
        Real dI = betaVal.multiply(S).multiply(I).divide(N).subtract(gammaVal.multiply(I));
        Real dR = gammaVal.multiply(I);

        S = S.add(dS.multiply(dtSec)).max(Real.ZERO);
        I = I.add(dI.multiply(dtSec)).max(Real.ZERO);
        R = R.add(dR.multiply(dtSec)).max(Real.ZERO);
        time = time.add(dt);
    }

    /** Run simulation for duration */
    public Real[][] simulate(Quantity<Time> duration, Quantity<Time> dt) {
        double totalSeconds = duration.to(Units.SECOND).getValue().doubleValue();
        double dtSeconds = dt.to(Units.SECOND).getValue().doubleValue();
        int steps = (int) (totalSeconds / dtSeconds) + 1;
        Real[][] results = new Real[steps][4];

        reset();

        for (int i = 0; i < steps; i++) {
            results[i] = new Real[] { Real.of(time.to(Units.DAY).getValue().doubleValue()), S, I, R };
            step(dt);
        }
        return results;
    }

    public void reset() {
        this.I = Real.of(population > 0 ? 1 : 0);
        this.S = Real.of(population).subtract(I);
        this.R = Real.ZERO;
        this.time = Quantities.create(0, Units.SECOND);
    }

    /** Peak time estimation */
    public Quantity<Time> getPeakTime() {
        Real r0 = getR0();
        if (r0.compareTo(Real.ONE) <= 0)
            return Quantities.create(0, Units.SECOND);

        Real betaVal = beta.to(Units.HERTZ).getValue();
        Real gammaVal = gamma.to(Units.HERTZ).getValue();
        Real val = Real.of(population).multiply(r0.subtract(Real.ONE)).log().divide(betaVal.subtract(gammaVal));
        return Quantities.create(val, Units.SECOND);
    }

    /** Final epidemic size (fraction infected) */
    public Real getFinalSize() {
        Real r0 = getR0();
        if (r0.compareTo(Real.ONE) <= 0)
            return Real.ZERO;
        Real r = Real.of(0.9);
        for (int i = 0; i < 100; i++) {
            r = Real.ONE.subtract(r0.negate().multiply(r).exp());
        }
        return r;
    }

    // Getters
    public Real getSusceptible() {
        return S;
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

    public Quantity<Frequency> getGamma() {
        return gamma;
    }

    @Override
    public String toString() {
        return String.format("SIRModel{N=%d, R0=%.2f, S=%.0f, I=%.0f, R=%.0f, t=%s}",
                population, getR0().doubleValue(), S.doubleValue(), I.doubleValue(), R.doubleValue(), time);
    }

    // Factory methods
    public static SIRModel covid19Like(int population, int initialCases) {
        return new SIRModel(population, initialCases,
                Quantities.create(0.3 / 86400.0, Units.HERTZ),
                Quantities.create(0.1 / 86400.0, Units.HERTZ));
    }

    public static SIRModel influenzaLike(int population, int initialCases) {
        return new SIRModel(population, initialCases,
                Quantities.create(0.2 / 86400.0, Units.HERTZ),
                Quantities.create(0.14 / 86400.0, Units.HERTZ));
    }

    public static SIRModel measlesLike(int population, int initialCases) {
        return new SIRModel(population, initialCases,
                Quantities.create(1.8 / 86400.0, Units.HERTZ),
                Quantities.create(0.125 / 86400.0, Units.HERTZ));
    }
}


