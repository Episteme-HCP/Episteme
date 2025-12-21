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
package org.jscience.biology.cell;

/**
 * Modeling the eukaryotic cell cycle.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CellCycle {

    /**
     * Phases of the cell cycle.
     */
    public enum Phase {
        G1("Gap 1", "Growth, protein synthesis"),
        S("Synthesis", "DNA replication"),
        G2("Gap 2", "Growth, preparation for mitosis"),
        M("Mitosis", "Cell division"),
        G0("Resting", "Quiescence");

        private final String name;
        private final String description;

        Phase(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private Phase currentPhase;
    private double timeInPhase; // hours

    // Duration of phases in hours (typical mammalian cell, ~24h total)
    private static final double DURATION_G1 = 11.0;
    private static final double DURATION_S = 8.0;
    private static final double DURATION_G2 = 4.0;
    private static final double DURATION_M = 1.0;

    public CellCycle() {
        this.currentPhase = Phase.G1;
        this.timeInPhase = 0;
    }

    /**
     * Progresses the cell cycle by a given time step.
     * 
     * @param deltaTime Hours to advance
     */
    public void advance(double deltaTime) {
        if (currentPhase == Phase.G0)
            return;

        timeInPhase += deltaTime;
        checkTransition();
    }

    private void checkTransition() {
        switch (currentPhase) {
            case G1:
                if (timeInPhase >= DURATION_G1) {
                    transition(Phase.S);
                }
                break;
            case S:
                if (timeInPhase >= DURATION_S) {
                    transition(Phase.G2);
                }
                break;
            case G2:
                if (timeInPhase >= DURATION_G2) {
                    transition(Phase.M);
                }
                break;
            case M:
                if (timeInPhase >= DURATION_M) {
                    transition(Phase.G1); // Divide and restart
                    // In a simulation, this would trigger cell division event
                }
                break;
            default:
                break;
        }
    }

    private void transition(Phase next) {
        this.currentPhase = next;
        this.timeInPhase = 0; // Reset timer for new phase
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public double getProgressInPhase() {
        switch (currentPhase) {
            case G1:
                return Math.min(1.0, timeInPhase / DURATION_G1);
            case S:
                return Math.min(1.0, timeInPhase / DURATION_S);
            case G2:
                return Math.min(1.0, timeInPhase / DURATION_G2);
            case M:
                return Math.min(1.0, timeInPhase / DURATION_M);
            default:
                return 0.0;
        }
    }

    public void enterG0() {
        this.currentPhase = Phase.G0;
        this.timeInPhase = 0;
    }

    public void exitG0() {
        if (currentPhase == Phase.G0) {
            transition(Phase.G1);
        }
    }
}
