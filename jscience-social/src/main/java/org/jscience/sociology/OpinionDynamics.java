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

package org.jscience.sociology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulates opinion dynamics in social networks using various theoretical models.
 * Includes implementations of the DeGroot model, Bounded Confidence (Deffuant-Weisbuch),
 * and media influence models.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
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
        double opinion,
        double stubbornness,
        List<String> neighbors
    ) implements Serializable {}

    /**
     * Captures the state of the simulation at a specific step.
     */
    public record SimulationState(
        int step,
        Map<String, Double> opinions,
        double polarization,
        double consensus,
        int clusters
    ) implements Serializable {}

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
        Map<String, Double> opinions = new HashMap<>();
        
        for (Agent a : agents) {
            opinions.put(a.id(), a.opinion());
        }
        
        history.add(createState(0, opinions));
        
        for (int step = 1; step <= steps; step++) {
            Map<String, Double> newOpinions = new HashMap<>();
            final Map<String, Double> currentOpinions = opinions;
            
            for (Agent agent : agents) {
                double neighborAvg = agent.neighbors().stream()
                    .mapToDouble(n -> currentOpinions.getOrDefault(n, 0.0))
                    .average()
                    .orElse(agent.opinion());
                
                // Aggregation: New = Stubbornness * Old + (1 - Stubbornness) * NeighborAvg
                double newOpinion = agent.stubbornness() * agent.opinion() + 
                                   (1 - agent.stubbornness()) * neighborAvg;
                newOpinions.put(agent.id(), newOpinion);
            }
            
            opinions = newOpinions;
            history.add(createState(step, opinions));
        }
        
        return history;
    }

    /**
     * Runs the Bounded Confidence model (Deffuant-Weisbuch).
     * Agents only interact and adjust opinions if their opinion difference is within a specified threshold.
     * 
     * @param agents          initial configuration
     * @param steps           number of interaction steps
     * @param threshold       confidence threshold (epsilon)
     * @param convergenceRate rate of convergence (mu)
     * @return list of states (snapshot every 10 steps)
     */
    public static List<SimulationState> simulateBoundedConfidence(List<Agent> agents, 
            int steps, double threshold, double convergenceRate) {
        
        List<SimulationState> history = new ArrayList<>();
        Map<String, Double> opinions = new HashMap<>();
        Random random = new Random(42);
        
        for (Agent a : agents) {
            opinions.put(a.id(), a.opinion());
        }
        history.add(createState(0, opinions));
        
        List<String> ids = new ArrayList<>(opinions.keySet());
        
        for (int step = 1; step <= steps; step++) {
            // Pairwise random interactions
            // Trying roughly N interactions per step to approximate one time unit
            for (int i = 0; i < agents.size(); i++) {
                String id1 = ids.get(random.nextInt(ids.size()));
                String id2 = ids.get(random.nextInt(ids.size()));
                if (id1.equals(id2)) continue;
                
                double op1 = opinions.get(id1);
                double op2 = opinions.get(id2);
                
                // Interaction condition: |op1 - op2| < threshold
                if (Math.abs(op1 - op2) < threshold) {
                    double newOp1 = op1 + convergenceRate * (op2 - op1);
                    double newOp2 = op2 + convergenceRate * (op1 - op2);
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
     * 
     * @param agents         initial agents
     * @param steps          simulation steps
     * @param mediaOpinion   the opinion value broadcast by media
     * @param mediaInfluence strength of media influence (0.0 to 1.0)
     * @return simulation history
     */
    public static List<SimulationState> simulateWithMedia(List<Agent> agents, int steps,
            double mediaOpinion, double mediaInfluence) {
        
        List<SimulationState> history = new ArrayList<>();
        Map<String, Double> opinions = new HashMap<>();
        
        for (Agent a : agents) {
            opinions.put(a.id(), a.opinion());
        }
        history.add(createState(0, opinions));
        
        for (int step = 1; step <= steps; step++) {
            Map<String, Double> newOpinions = new HashMap<>();
            final Map<String, Double> currentOpinions = opinions;
            
            for (Agent agent : agents) {
                double neighborAvg = agent.neighbors().stream()
                    .mapToDouble(n -> currentOpinions.getOrDefault(n, 0.0))
                    .average()
                    .orElse(agent.opinion());
                
                // Combined influence: (1 - media) * neighbors + media * mediaOpinion
                double socialInput = (1 - mediaInfluence) * neighborAvg + mediaInfluence * mediaOpinion;
                double socialInfluence = (1 - agent.stubbornness()) * socialInput;
                
                double newOpinion = agent.stubbornness() * agent.opinion() + socialInfluence;
                newOpinion = Math.max(-1, Math.min(1, newOpinion)); // Clamp to valid range
                
                newOpinions.put(agent.id(), newOpinion);
            }
            
            opinions = newOpinions;
            history.add(createState(step, opinions));
        }
        
        return history;
    }

    /**
     * Creates a random agent network for testing.
     * 
     * @param n              number of agents
     * @param avgConnections average number of neighbors per agent
     * @param random         random number generator
     * @return list of generated agents
     */
    public static List<Agent> createRandomNetwork(int n, double avgConnections, Random random) {
        List<Agent> agents = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            ids.add("agent_" + i);
        }
        
        for (int i = 0; i < n; i++) {
            List<String> neighbors = new ArrayList<>();
            // approximate number of connections
            int numConnections = (int) (avgConnections * (0.5 + random.nextDouble()));
            
            for (int j = 0; j < numConnections; j++) {
                String neighbor = ids.get(random.nextInt(n));
                if (!neighbor.equals(ids.get(i)) && !neighbors.contains(neighbor)) {
                    neighbors.add(neighbor);
                }
            }
            
            double opinion = random.nextDouble() * 2 - 1;  // -1.0 to +1.0
            double stubbornness = random.nextDouble() * 0.5; // 0.0 to 0.5
            
            agents.add(new Agent(ids.get(i), opinion, stubbornness, neighbors));
        }
        
        return agents;
    }

    /**
     * Calculates a polarization index for the current opinion distribution.
     * Uses average distance from the center (simple metric).
     * 
     * @param opinions map of agent opinions
     * @return polarization score
     */
    public static Real calculatePolarization(Map<String, Double> opinions) {
        double[] values = opinions.values().stream().mapToDouble(Double::doubleValue).toArray();
        if (values.length == 0) return Real.ZERO;

        double distFromCenter = Arrays.stream(values)
            .map(Math::abs)
            .average().orElse(0);
        
        return Real.of(distFromCenter);
    }

    private static SimulationState createState(int step, Map<String, Double> opinions) {
        double[] values = opinions.values().stream().mapToDouble(Double::doubleValue).toArray();
        if (values.length == 0) {
            return new SimulationState(step, new HashMap<>(), 0, 0, 0);
        }

        double mean = Arrays.stream(values).average().orElse(0);
        double variance = Arrays.stream(values).map(v -> (v - mean) * (v - mean)).average().orElse(0);
        double consensus = 1.0 - Math.min(1.0, Math.sqrt(variance)); // approximate 0..1
        
        double polarization = calculatePolarization(opinions).doubleValue();
        int clusters = countClusters(values, 0.2);
        
        return new SimulationState(step, new HashMap<>(opinions), polarization, consensus, clusters);
    }

    private static int countClusters(double[] values, double threshold) {
        if (values.length == 0) return 0;
        double[] sorted = values.clone();
        Arrays.sort(sorted);
        
        int clusters = 1;
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] - sorted[i - 1] > threshold) {
                clusters++;
            }
        }
        return Math.min(clusters, 5); // Cap for summary simplicity
    }
}
