/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sociology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Simulates opinion dynamics in social networks using various theoretical models.
 * Includes implementations of the DeGroot model, Bounded Confidence (Deffuant-Weisbuch),
 * and media influence models.
 * Modernized to use Real for internal calculations and API.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class OpinionDynamics {

    private OpinionDynamics() {}

    /**
     * Represents a single agent in the network with a specific opinion and stubbornness.
     * @param id           unique identifier
     * @param opinion      current opinion value (-1.0 to 1.0)
     * @param stubbornness resistance to change (0.0 to 1.0)
     * @param neighbors    list of connected agent IDs
     */
    public record Agent(
        String id,
        Real opinion,
        Real stubbornness,
        List<String> neighbors
    ) implements Serializable {
        // Constructor with double for input convenience
        public Agent(String id, double opinion, double stubbornness, List<String> neighbors) {
            this(id, Real.of(opinion), Real.of(stubbornness), neighbors);
        }
    }

    /**
     * Captures the state of the simulation at a specific step.
     */
    public record SimulationState(
        int step,
        Map<String, Real> opinions,
        Real polarization,
        Real consensus,
        int clusters
    ) implements Serializable {
        // Double outputs for display
        public double getPolarizationValue() { return polarization.doubleValue(); }
        public double getConsensusValue() { return consensus.doubleValue(); }
    }

    /**
     * Runs the DeGroot opinion dynamics model.
     * In this model, agents update their opinions by taking the weighted average of their neighbors' opinions.
     * 
     * @param agents initial configuration of agents
     * @param steps  number of simulation steps
     * @return list of simulation states over time
     */
    public static List<SimulationState> simulateDeGroot(List<Agent> agents, int steps) {
        List<SimulationState> history = new ArrayList<>();
        Map<String, Real> opinions = new HashMap<>();
        
        for (Agent a : agents) {
            opinions.put(a.id(), a.opinion());
        }
        
        history.add(createState(0, opinions));
        
        for (int step = 1; step <= steps; step++) {
            Map<String, Real> newOpinions = new HashMap<>();
            final Map<String, Real> currentOpinions = opinions;
            
            for (Agent agent : agents) {
                Real neighborSum = Real.ZERO;
                int count = 0;
                for (String nId : agent.neighbors()) {
                    Real o = currentOpinions.get(nId);
                    if (o != null) {
                        neighborSum = neighborSum.add(o);
                        count++;
                    }
                }
                Real neighborAvg = count > 0 ? neighborSum.divide(Real.of(count)) : agent.opinion();
                
                // Aggregation: New = Stubbornness * Old + (1 - Stubbornness) * NeighborAvg
                Real s = agent.stubbornness();
                Real newOpinion = s.multiply(agent.opinion()).add(Real.ONE.subtract(s).multiply(neighborAvg));
                newOpinions.put(agent.id(), newOpinion);
            }
            
            opinions = newOpinions;
            history.add(createState(step, opinions));
        }
        
        return history;
    }

    /**
     * Runs the Bounded Confidence model (Deffuant-Weisbuch).
     * 
     * @param agents          initial configuration
     * @param steps           number of interaction steps
     * @param threshold       confidence threshold (epsilon) - Real
     * @param convergenceRate rate of convergence (mu) - Real
     * @return list of states (snapshot every 10 steps)
     */
    public static List<SimulationState> simulateBoundedConfidence(List<Agent> agents, 
            int steps, Real threshold, Real convergenceRate) {
        
        List<SimulationState> history = new ArrayList<>();
        Map<String, Real> opinions = new HashMap<>();
        Random random = new Random(42);
        
        for (Agent a : agents) {
            opinions.put(a.id(), a.opinion());
        }
        history.add(createState(0, opinions));
        
        List<String> ids = new ArrayList<>(opinions.keySet());
        
        for (int step = 1; step <= steps; step++) {
            for (int i = 0; i < agents.size(); i++) {
                String id1 = ids.get(random.nextInt(ids.size()));
                String id2 = ids.get(random.nextInt(ids.size()));
                if (id1.equals(id2)) continue;
                
                Real op1 = opinions.get(id1);
                Real op2 = opinions.get(id2);
                
                if (op1.subtract(op2).abs().compareTo(threshold) < 0) {
                    Real newOp1 = op1.add(convergenceRate.multiply(op2.subtract(op1)));
                    Real newOp2 = op2.add(convergenceRate.multiply(op1.subtract(op2)));
                    opinions.put(id1, newOp1);
                    opinions.put(id2, newOp2);
                }
            }
            
            if (step % 10 == 0) {
                history.add(createState(step, opinions));
            }
        }
        
        return history;
    }

    /**
     * Runs a social influence model incorporating external media effects.
     */
    public static List<SimulationState> simulateWithMedia(List<Agent> agents, int steps,
            Real mediaOpinion, Real mediaInfluence) {
        
        List<SimulationState> history = new ArrayList<>();
        Map<String, Real> opinions = new HashMap<>();
        
        for (Agent a : agents) {
            opinions.put(a.id(), a.opinion());
        }
        history.add(createState(0, opinions));
        
        for (int step = 1; step <= steps; step++) {
            Map<String, Real> newOpinions = new HashMap<>();
            final Map<String, Real> currentOpinions = opinions;
            
            for (Agent agent : agents) {
                Real neighborSum = Real.ZERO;
                int count = 0;
                for (String nId : agent.neighbors()) {
                    Real o = currentOpinions.get(nId);
                    if (o != null) {
                        neighborSum = neighborSum.add(o);
                        count++;
                    }
                }
                Real neighborAvg = count > 0 ? neighborSum.divide(Real.of(count)) : agent.opinion();
                
                Real socialInput = Real.ONE.subtract(mediaInfluence).multiply(neighborAvg).add(mediaInfluence.multiply(mediaOpinion));
                Real s = agent.stubbornness();
                Real socialInfluence = Real.ONE.subtract(s).multiply(socialInput);
                
                Real newOpinion = s.multiply(agent.opinion()).add(socialInfluence);
                // Clamp -1 to 1
                if (newOpinion.compareTo(Real.ONE) > 0) newOpinion = Real.ONE;
                if (newOpinion.compareTo(Real.of(-1)) < 0) newOpinion = Real.of(-1);
                
                newOpinions.put(agent.id(), newOpinion);
            }
            
            opinions = newOpinions;
            history.add(createState(step, opinions));
        }
        
        return history;
    }

    /**
     * Calculates a polarization index for the current opinion distribution.
     * 
     * @param opinions map of agent opinions (Real)
     * @return polarization score (Real)
     */
    public static Real calculatePolarization(Map<String, Real> opinions) {
        if (opinions == null || opinions.isEmpty()) return Real.ZERO;

        Real sumAbs = Real.ZERO;
        for (Real v : opinions.values()) {
            sumAbs = sumAbs.add(v.abs());
        }
        
        return sumAbs.divide(Real.of(opinions.size()));
    }

    private static SimulationState createState(int step, Map<String, Real> opinions) {
        if (opinions.isEmpty()) {
            return new SimulationState(step, new HashMap<>(), Real.ZERO, Real.ZERO, 0);
        }

        Real sum = Real.ZERO;
        for (Real v : opinions.values()) sum = sum.add(v);
        Real mean = sum.divide(Real.of(opinions.size()));
        
        Real sumVar = Real.ZERO;
        for (Real v : opinions.values()) {
            Real diff = v.subtract(mean);
            sumVar = sumVar.add(diff.multiply(diff));
        }
        Real variance = sumVar.divide(Real.of(opinions.size()));
        Real consensus = Real.ONE.subtract(variance.sqrt().min(Real.ONE)); // approximate
        
        Real polarization = calculatePolarization(opinions);
        int clusters = countClusters(opinions.values(), 0.2);
        
        return new SimulationState(step, new HashMap<>(opinions), polarization, consensus, clusters);
    }

    private static int countClusters(java.util.Collection<Real> values, double threshold) {
        if (values.isEmpty()) return 0;
        double[] sorted = values.stream().mapToDouble(Real::doubleValue).sorted().toArray();
        
        int clusters = 1;
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] - sorted[i - 1] > threshold) {
                clusters++;
            }
        }
        return Math.min(clusters, 5);
    }
}

