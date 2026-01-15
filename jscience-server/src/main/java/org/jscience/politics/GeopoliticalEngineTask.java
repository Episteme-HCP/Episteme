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

package org.jscience.politics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.distributed.TaskRegistry;


/**
 * Geopolitical Engine Distributed Task.
 * 
 * Simulates international relations, trade, and stability between nations.
 * Uses a modified Correlates of War (COW) inspired model for conflict
 * simulation.
 */
public class GeopoliticalEngineTask implements Serializable {

    public static class NationState implements Serializable {
        public String name;
        public double stability; // 0.0 to 1.0
        public Real stabilityReal;
        public double militaryPower;
        public Real militaryPowerReal;
        public List<String> allies = new ArrayList<>();

        public NationState(String name, double stability, double mil) {
            this.name = name;
            this.stability = stability;
            this.stabilityReal = Real.of(stability);
            this.militaryPower = mil;
            this.militaryPowerReal = Real.of(mil);
        }
    }

    private List<NationState> nations;
    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.PRIMITIVE;

    public GeopoliticalEngineTask(List<NationState> nations) {
        this.nations = nations;
    }

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
    }

    public void run() {
        if (mode == TaskRegistry.PrecisionMode.REAL) {
            runReal();
        } else {
            runPrimitive();
        }
    }

    private void runReal() {
        for (NationState n : nations) {
            // Internal stability fluctuation
            n.stabilityReal = n.stabilityReal.add(Real.of(0.1 * (Math.random() - 0.5)));
            if (n.stabilityReal.doubleValue() < 0)
                n.stabilityReal = Real.of(0.0);
            if (n.stabilityReal.doubleValue() > 1)
                n.stabilityReal = Real.of(1.0);

            // Interaction: if stability is very low, chance of conflict
            if (n.stabilityReal.doubleValue() < 0.2) {
                for (NationState other : nations) {
                    if (other != n && !n.allies.contains(other.name)) {
                        // Potential tension
                        militaryInteractionReal(n, other);
                    }
                }
            }
        }
    }

    private void runPrimitive() {
        for (NationState n : nations) {
            // Internal stability fluctuation
            n.stability += 0.1 * (Math.random() - 0.5);
            n.stability = Math.max(0, Math.min(1.0, n.stability));

            // Interaction: if stability is very low, chance of conflict
            if (n.stability < 0.2) {
                for (NationState other : nations) {
                    if (other != n && !n.allies.contains(other.name)) {
                        // Potential tension
                        militaryInteraction(n, other);
                    }
                }
            }
        }
    }

    private void militaryInteractionReal(NationState a, NationState b) {
        // Simple balance of power check
        Real tension = a.militaryPowerReal.divide(b.militaryPowerReal.add(Real.of(1e-6)))
                .multiply(Real.of(1.0).subtract(a.stabilityReal));
        if (tension.doubleValue() > 2.0 && Math.random() > 0.95) {
            // Conflict event reduce stability for both
            a.stabilityReal = a.stabilityReal.subtract(Real.of(0.1));
            b.stabilityReal = b.stabilityReal.subtract(Real.of(0.05));
        }
    }

    private void militaryInteraction(NationState a, NationState b) {
        // Simple balance of power check
        double tension = (a.militaryPower / (b.militaryPower + 1e-6)) * (1.0 - a.stability);
        if (tension > 2.0 && Math.random() > 0.95) {
            // Conflict event reduce stability for both
            a.stability -= 0.1;
            b.stability -= 0.05;
        }
    }

    public List<NationState> getNations() {
        return nations;
    }
}
