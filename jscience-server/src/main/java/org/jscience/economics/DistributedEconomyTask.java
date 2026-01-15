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

package org.jscience.economics;

import java.io.Serializable;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.distributed.TaskRegistry;


/**
 * Distributed Economic Simulation Task.
 * 
 * Simulates macro-economic indicators (GDP, Inflation) based on stochastic
 * shocks
 * and policy interventions.
 */
public class DistributedEconomyTask implements Serializable {

    private String economyName;
    private Real gdp;
    private Real inflation;
    private double gdpD;
    private double inflationD;
    private double dt; // years
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.REAL;

    public DistributedEconomyTask(String name, Real gdp, Real inflation) {
        this.economyName = name;
        this.gdp = gdp;
        this.inflation = inflation;
        this.gdpD = gdp.doubleValue();
        this.inflationD = inflation.doubleValue();
        this.dt = 1.0;
    }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        if (this.mode != mode) {
            if (mode == TaskRegistry.PrecisionMode.PRIMITIVE) {
                gdpD = gdp.doubleValue();
                inflationD = inflation.doubleValue();
            } else {
                gdp = Real.of(gdpD);
                inflation = Real.of(inflationD);
            }
            this.mode = mode;
        }
    }

    public void run() {
        // Solow-Swan inspired growth with stochastic shocks
        double growthRate = 0.02 + 0.05 * (Math.random() - 0.5);
        double inflationShock = 0.01 * (Math.random() - 0.5);

        if (mode == TaskRegistry.PrecisionMode.REAL) {
            double gValue = gdp.doubleValue();
            double iValue = inflation.doubleValue();

            gValue *= (1 + growthRate * dt);
            iValue += inflationShock;

            this.gdp = Real.of(gValue);
            this.inflation = Real.of(iValue);
        } else {
            gdpD *= (1 + growthRate * dt);
            inflationD += inflationShock;
        }
    }

    public Real getGdp() {
        return mode == TaskRegistry.PrecisionMode.REAL ? gdp : Real.of(gdpD);
    }

    public Real getInflation() {
        return mode == TaskRegistry.PrecisionMode.REAL ? inflation : Real.of(inflationD);
    }

    public String getEconomyName() {
        return economyName;
    }
}
