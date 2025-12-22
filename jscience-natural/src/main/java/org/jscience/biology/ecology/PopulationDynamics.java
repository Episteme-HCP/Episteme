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
package org.jscience.biology.ecology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Dimensionless;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;

/**
 * Population dynamics models for ecology.
 * <p>
 * Provides:
 * <ul>
 * <li>Exponential growth</li>
 * <li>Logistic growth (Verhulst equation)</li>
 * <li>Lotka-Volterra predator-prey model</li>
 * <li>Competition models</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PopulationDynamics {

    private PopulationDynamics() {
    }

    // === Exponential Growth ===

    /**
     * Exponential growth model using Quantities.
     * P(t) = P0 * e^(rt)
     */
    public static Quantity<Dimensionless> exponentialGrowth(Quantity<Dimensionless> p0, Quantity<Frequency> r,
            Quantity<Time> t) {
        Real p0Val = Real.of(p0.to(Units.ONE).getValue().doubleValue());
        Real rVal = Real.of(r.to(Units.HERTZ).getValue().doubleValue());
        Real tVal = Real.of(t.to(Units.SECOND).getValue().doubleValue());
        Real result = exponentialGrowth(p0Val, rVal, tVal);
        return Quantities.create(result.doubleValue(), Units.ONE);
    }

    /**
     * Exponential growth model (Malthusian).
     * P(t) = P0 * e^(rt)
     */
    public static Real exponentialGrowth(Real p0, Real r, Real t) {
        return p0.multiply(r.multiply(t).exp());
    }

    /**
     * Exponential growth rate calculation.
     * r = ln(Nt/N0) / t
     */
    public static Real calculateGrowthRate(Real n0, Real nt, Real time) {
        return nt.divide(n0).log().divide(time);
    }

    /**
     * Doubling time for exponential growth.
     * td = ln(2) / r
     */
    public static Real doublingTime(Real growthRate) {
        return Real.LN2.divide(growthRate);
    }

    // === Logistic Growth ===

    /**
     * Logistic growth model using Quantities.
     * P(t) = K / (1 + ((K - P0)/P0) * e^(-rt))
     */
    public static Quantity<Dimensionless> logisticGrowth(Quantity<Dimensionless> p0, Quantity<Frequency> r,
            Quantity<Dimensionless> K, Quantity<Time> t) {
        Real p0Val = Real.of(p0.to(Units.ONE).getValue().doubleValue());
        Real rVal = Real.of(r.to(Units.HERTZ).getValue().doubleValue());
        Real kVal = Real.of(K.to(Units.ONE).getValue().doubleValue());
        Real tVal = Real.of(t.to(Units.SECOND).getValue().doubleValue());
        Real result = logisticGrowth(p0Val, rVal, kVal, tVal);
        return Quantities.create(result.doubleValue(), Units.ONE);
    }

    /**
     * Logistic growth model.
     * P(t) = K / (1 + ((K - P0)/P0) * e^(-rt))
     */
    public static Real logisticGrowth(Real p0, Real r, Real K, Real t) {
        Real factor = K.subtract(p0).divide(p0).multiply(r.negate().multiply(t).exp());
        return K.divide(Real.ONE.add(factor));
    }

    /**
     * Logistic growth rate at population N.
     * dN/dt = rN(1 - N/K)
     */
    public static Real logisticGrowthRate(Real n, Real k, Real r) {
        return r.multiply(n).multiply(Real.ONE.subtract(n.divide(k)));
    }

    /**
     * Inflection point of logistic curve (maximum growth rate).
     * Occurs at N = K/2
     */
    public static Real inflectionPoint(Real k) {
        return k.divide(Real.TWO);
    }

    // === Lotka-Volterra Predator-Prey ===

    /**
     * Solving Lotka-Volterra predator-prey equations using Euler method.
     * dx/dt = alpha*x - beta*x*y (Prey)
     * dy/dt = delta*x*y - gamma*y (Predator)
     * 
     * @param prey     Initial prey population
     * @param predator Initial predator population
     * @param alpha    Prey growth rate
     * @param beta     Predation rate
     * @param delta    Predator reproduction rate
     * @param gamma    Predator death rate
     * @param timeStep dt
     * @param steps    Number of steps to simulate
     * @return Real[][] where column 0 is prey, column 1 is predator (rows = steps)
     */
    public static Real[][] lotkaVolterra(Real prey, Real predator,
            Real alpha, Real beta,
            Real delta, Real gamma,
            Real timeStep, int steps) {
        Real[][] result = new Real[steps][2];
        Real x = prey;
        Real y = predator;

        for (int i = 0; i < steps; i++) {
            result[i][0] = x;
            result[i][1] = y;

            Real dx = alpha.multiply(x).subtract(beta.multiply(x).multiply(y)).multiply(timeStep);
            Real dy = delta.multiply(x).multiply(y).subtract(gamma.multiply(y)).multiply(timeStep);

            x = x.add(dx);
            y = y.add(dy);

            // Prevent negative populations
            if (x.compareTo(Real.ZERO) < 0)
                x = Real.ZERO;
            if (y.compareTo(Real.ZERO) < 0)
                y = Real.ZERO;
        }

        return result;
    }

    /**
     * Lotka-Volterra equilibrium - prey population.
     * N* = gamma/delta
     */
    public static Real preyEquilibrium(Real gamma, Real delta) {
        return gamma.divide(delta);
    }

    /**
     * Lotka-Volterra equilibrium - predator population.
     * P* = alpha/beta
     */
    public static Real predatorEquilibrium(Real alpha, Real beta) {
        return alpha.divide(beta);
    }

    // === Competition ===

    /**
     * Competitive exclusion - Lotka-Volterra competition.
     * dN1/dt = r1*N1 * (K1 - N1 - alpha12*N2) / K1
     */
    public static Real competitionGrowthRate(Real n1, Real n2, Real r1, Real k1, Real alpha12) {
        Real numerator = k1.subtract(n1).subtract(alpha12.multiply(n2));
        return r1.multiply(n1).multiply(numerator).divide(k1);
    }

    /**
     * Allee effect - reduced growth at low density.
     * dN/dt = rN(N/A - 1)(1 - N/K)
     */
    public static Real alleeEffectGrowthRate(Real n, Real r, Real a, Real k) {
        return r.multiply(n).multiply(n.divide(a).subtract(Real.ONE)).multiply(Real.ONE.subtract(n.divide(k)));
    }
    // === Epidemiology ===

    /**
     * SEIR Model (Susceptible, Exposed, Infectious, Recovered).
     * 
     * dS/dt = -beta * S * I / N
     * dE/dt = beta * S * I / N - sigma * E
     * dI/dt = sigma * E - gamma * I
     * dR/dt = gamma * I
     * 
     * @param initialPop [S, E, I, R]
     * @param beta       rate of spread
     * @param sigma      rate of progression (1/incubation period)
     * @param gamma      rate of recovery (1/infectious period)
     * @param dt         time step
     * @param steps      number of steps
     * @return Real[steps][4] containing S, E, I, R for each step
     */
    public static Real[][] seirModel(Real[] initialPop, Real beta, Real sigma, Real gamma, Real dt, int steps) {
        Real[][] result = new Real[steps][4];
        Real S = initialPop[0];
        Real E = initialPop[1];
        Real I = initialPop[2];
        Real R = initialPop[3];
        Real N = S.add(E).add(I).add(R); // Total population assumed constant

        for (int t = 0; t < steps; t++) {
            result[t][0] = S;
            result[t][1] = E;
            result[t][2] = I;
            result[t][3] = R;

            // dS = -beta * S * I / N
            Real dS = beta.multiply(S).multiply(I).divide(N).negate().multiply(dt);

            // dE = beta * S * I / N - sigma * E
            Real transmission = beta.multiply(S).multiply(I).divide(N);
            Real progression = sigma.multiply(E);
            Real dE = transmission.subtract(progression).multiply(dt);

            // dI = sigma * E - gamma * I
            Real recovery = gamma.multiply(I);
            Real dI = progression.subtract(recovery).multiply(dt);

            // dR = gamma * I
            Real dR = recovery.multiply(dt);

            S = S.add(dS);
            E = E.add(dE);
            I = I.add(dI);
            R = R.add(dR);

            // Safety clamp
            if (S.doubleValue() < 0)
                S = Real.ZERO;
            if (E.doubleValue() < 0)
                E = Real.ZERO;
            if (I.doubleValue() < 0)
                I = Real.ZERO;
            if (R.doubleValue() < 0)
                R = Real.ZERO;
        }
        return result;
    }
}
