package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Simulates opinion dynamics in social networks.
 */
public final class OpinionDynamics {

    private OpinionDynamics() {}

    public record Agent(
        String id,
        double opinion,        // -1 to +1
        double stubbornness,   // 0 to 1 (0 = easily influenced)
        List<String> neighbors
    ) {}

    public record SimulationState(
        int step,
        Map<String, Double> opinions,
        double polarization,
        double consensus,
        int clusters
    ) {}

    /**
     * Runs DeGroot opinion dynamics model (averaging).
     */
    public static List<SimulationState> simulateDeGroot(List<Agent> agents, int steps) {
        List<SimulationState> history = new ArrayList<>();
        Map<String, Double> opinions = new HashMap<>();
        Map<String, Agent> agentMap = new HashMap<>();
        
        for (Agent a : agents) {
            opinions.put(a.id(), a.opinion());
            agentMap.put(a.id(), a);
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
                
                // Weighted average with stubbornness
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
     * Runs Bounded Confidence model (Deffuant-Weisbuch).
     * Agents only interact if opinions are within threshold.
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
            // Random pairwise interactions
            for (int i = 0; i < agents.size(); i++) {
                String id1 = ids.get(random.nextInt(ids.size()));
                String id2 = ids.get(random.nextInt(ids.size()));
                if (id1.equals(id2)) continue;
                
                double op1 = opinions.get(id1);
                double op2 = opinions.get(id2);
                
                // Only interact if within threshold
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
     * Runs social influence model with external media effects.
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
                
                // Include media influence
                double socialInfluence = (1 - agent.stubbornness()) * 
                    ((1 - mediaInfluence) * neighborAvg + mediaInfluence * mediaOpinion);
                
                double newOpinion = agent.stubbornness() * agent.opinion() + socialInfluence;
                newOpinion = Math.max(-1, Math.min(1, newOpinion)); // Bound
                
                newOpinions.put(agent.id(), newOpinion);
            }
            
            opinions = newOpinions;
            history.add(createState(step, opinions));
        }
        
        return history;
    }

    /**
     * Creates a random network of agents.
     */
    public static List<Agent> createRandomNetwork(int n, double avgConnections, Random random) {
        List<Agent> agents = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            ids.add("agent_" + i);
        }
        
        for (int i = 0; i < n; i++) {
            List<String> neighbors = new ArrayList<>();
            int numConnections = (int) (avgConnections * (0.5 + random.nextDouble()));
            
            for (int j = 0; j < numConnections; j++) {
                String neighbor = ids.get(random.nextInt(n));
                if (!neighbor.equals(ids.get(i)) && !neighbors.contains(neighbor)) {
                    neighbors.add(neighbor);
                }
            }
            
            double opinion = random.nextDouble() * 2 - 1;  // -1 to +1
            double stubbornness = random.nextDouble() * 0.5; // 0 to 0.5
            
            agents.add(new Agent(ids.get(i), opinion, stubbornness, neighbors));
        }
        
        return agents;
    }

    /**
     * Calculates polarization index.
     */
    public static Real calculatePolarization(Map<String, Double> opinions) {
        double[] values = opinions.values().stream().mapToDouble(Double::doubleValue).toArray();
        
        // Bimodality check: variance from both extremes
        // Bimodality check: variance from both extremes

        double distFromCenter = Arrays.stream(values)
            .map(v -> Math.abs(v))
            .average().orElse(0);
        
        return Real.of(distFromCenter);
    }

    private static SimulationState createState(int step, Map<String, Double> opinions) {
        double[] values = opinions.values().stream().mapToDouble(Double::doubleValue).toArray();
        
        // Consensus: how similar are opinions
        double mean = Arrays.stream(values).average().orElse(0);
        double variance = Arrays.stream(values).map(v -> (v - mean) * (v - mean)).average().orElse(0);
        double consensus = 1 - Math.sqrt(variance); // 0 = dispersed, 1 = consensus
        
        // Polarization: bimodal distribution
        double polarization = calculatePolarization(opinions).doubleValue();
        
        // Clusters (simplified: count distinct opinion groups)
        int clusters = countClusters(values, 0.2);
        
        return new SimulationState(step, new HashMap<>(opinions), polarization, consensus, clusters);
    }

    private static int countClusters(double[] values, double threshold) {
        Arrays.sort(values);
        int clusters = 1;
        for (int i = 1; i < values.length; i++) {
            if (values[i] - values[i-1] > threshold) {
                clusters++;
            }
        }
        return Math.min(clusters, 5); // Cap at 5
    }
}
