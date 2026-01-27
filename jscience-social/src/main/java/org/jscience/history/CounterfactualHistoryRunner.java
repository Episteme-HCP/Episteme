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
package org.jscience.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.sociology.Society;
import org.jscience.sociology.SocietySimulation;

/**
 * Orchestrates multi-domain counterfactual simulations by combining 
 * historical contingencies with agent-based social simulations.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class CounterfactualHistoryRunner implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Society society;
    private final List<CounterfactualSimulator.CounterfactualScenario> scenarios = new ArrayList<>();

    /**
     * Initializes the runner for a specific society.
     * @param society the society to experiment on
     */
    public CounterfactualHistoryRunner(Society society) {
        this.society = Objects.requireNonNull(society, "Society cannot be null");
    }

    /**
     * Adds a historical divergence scenario to the simulation queue.
     * @param scenario the scenario to add
     */
    public void addScenario(CounterfactualSimulator.CounterfactualScenario scenario) {
        scenarios.add(Objects.requireNonNull(scenario));
    }

    /**
     * Executes the simulation on the society and returns a combined report.
     * 
     * @param electionTitle title of the simulated benchmark election
     * @param candidates    candidates for the election
     * @param method        voting method
     * @return Multi-domain simulation report
     */
    public String runDivergenceAnalysis(String electionTitle, List<String> candidates, org.jscience.politics.VotingMethod method) {
        StringBuilder report = new StringBuilder();
        report.append("--- CROSS-DOMAIN COUNTERFACTUAL REPORT ---\n\n");

        for (CounterfactualSimulator.CounterfactualScenario scenario : scenarios) {
            report.append("SCENARIO: ").append(scenario.name()).append("\n");
            
            // 1. Historical Analysis
            CounterfactualSimulator.SimulationResult histResult = CounterfactualSimulator.simulate(scenario);
            report.append("Historical Divergence: ").append(String.format("%.2f", histResult.historicalDivergenceValue())).append("\n");
            
            // 2. Social Simulation (Agent-Based)
            SocietySimulation.Config config = new SocietySimulation.Config();
            // Adjust simulation volatility based on historical importance
            config.opinionThreshold = Real.of(0.5 - (scenario.divergencePoint().importance().doubleValue() * 0.2));
            SocietySimulation sim = new SocietySimulation(society, config);
            
            // Warm up the simulation for 10 steps
            for (int i = 0; i < 10; i++) sim.advance();
            
            // 3. Political Outcome Benchmark
            List<String> winners = sim.holdElection(electionTitle, candidates, method);
            report.append("Post-Divergence Election Winner: ").append(winners.isEmpty() ? "None" : winners.get(0)).append("\n");
            report.append("Societal Stability: ").append(String.format("%.2f", sim.getGlobalStability())).append("\n");
            report.append("\n");
        }

        return report.toString();
    }
}
