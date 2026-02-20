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

package org.jscience.social.sociology;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import org.jscience.social.economics.money.Money;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.social.politics.Ballot;
import org.jscience.social.politics.Country;
import org.jscience.social.politics.Election;
import org.jscience.social.politics.VotingMethod;

/**
 * A high-level simulator for sociological processes within a society.
 * Integrates social networks, opinion dynamics, and economic factors.
 * This class provides a framework for running agent-based simulations (ABS)
 * where persons interact, exchange opinions, and evolve socially.
 * Modernized to use Real for internal continuous values and provide double for output.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SocietySimulation implements Serializable {

    private static final long serialVersionUID = 2L;

    private final Society society;
    private final SocialNetwork network;
    private final Map<Person, Real> opinions = new HashMap<>();
    private final Random random = new Random();
    
    private long currentStep = 0;
    private Real globalStability = Real.ONE;

    /**
     * Configuration parameters for the simulation.
     */
    public static class Config implements Serializable {
        private static final long serialVersionUID = 1L;
        public Real opinionConvergenceRate = Real.of(0.1);
        public Real opinionThreshold = Real.of(0.5);
        public Real wealthVolatility = Real.of(10.0);
        public Real networkVolatility = Real.of(0.01);
        
        // Convenience double setters for UI/config
        public void setOpinionThreshold(double val) { this.opinionThreshold = Real.of(val); }
        public void setOpinionConvergenceRate(double val) { this.opinionConvergenceRate = Real.of(val); }
    }

    private final Config config;

    /**
     * Creates a new simulation for the given society.
     * Starts with a default social network and neutral opinions.
     *
     * @param society the society to simulate
     */
    public SocietySimulation(Society society) {
        this(society, new Config());
    }

    /**
     * Creates a simulation with specific configuration.
     *
     * @param society the society to simulate
     * @param config  simulation parameters
     */
    public SocietySimulation(Society society, Config config) {
        this.society = Objects.requireNonNull(society, "Society cannot be null");
        this.config = Objects.requireNonNull(config, "Config cannot be null");
        this.network = new SocialNetwork(society.getName() + " Network");
        
        // Initialize agents from society institutions
        for (SociologicalGroup SociologicalGroup : society.getInstitutions()) {
            for (Person member : SociologicalGroup.getMembers()) {
                network.addPerson(member);
                opinions.put(member, Real.of(random.nextDouble() * 2 - 1)); // Random -1 to 1
            }
        }
    }

    /**
     * Advances the simulation by one time step.
     */
    public void advance() {
        currentStep++;
        
        // 1. Opinion Dynamics (Bounded Confidence Model)
        simulateOpinionInteractions();
        
        // 2. Economic Dynamics
        simulateWealthEvolution();
        
        // 3. Social Network Evolution
        simulateNetworkDrift();
        
        // 4. Update Global Metrics
        updateMetrics();
    }

    private void simulateOpinionInteractions() {
        List<Person> actors = new ArrayList<>(network.getPersons());
        if (actors.size() < 2) return;

        for (int i = 0; i < actors.size(); i++) {
            Person p1 = actors.get(random.nextInt(actors.size()));
            Person p2 = actors.get(random.nextInt(actors.size()));
            if (p1.equals(p2)) continue;

            Real op1 = opinions.getOrDefault(p1, Real.ZERO);
            Real op2 = opinions.getOrDefault(p2, Real.ZERO);

            // Interaction based on distance and relationship strength
            Real distance = op1.subtract(op2).abs();
            double strength = network.getWeight(p1, p2).doubleValue();

            if (distance.compareTo(config.opinionThreshold) < 0) {
                Real influence = config.opinionConvergenceRate.multiply(Real.of(1.0 + strength));
                opinions.put(p1, op1.add(influence.multiply(op2.subtract(op1))));
                opinions.put(p2, op2.add(influence.multiply(op1.subtract(op2))));
            }
        }
    }

    private void simulateWealthEvolution() {
        for (Person p : network.getPersons()) {
            double change = (random.nextDouble() - 0.45) * config.wealthVolatility.doubleValue();
            p.earn(Money.usd(change));
        }
    }

    private void simulateNetworkDrift() {
        if (random.nextDouble() < config.networkVolatility.doubleValue()) {
            List<Person> actors = new ArrayList<>(network.getPersons());
            if (actors.size() < 2) return;
            
            Person p1 = actors.get(random.nextInt(actors.size()));
            Person p2 = actors.get(random.nextInt(actors.size()));
            
            if (!p1.equals(p2)) {
                if (network.areConnected(p1, p2)) {
                    // Strengthen or weaken
                    double w = network.getWeight(p1, p2).doubleValue();
                    network.addConnection(p1, p2, w + (random.nextDouble() - 0.5) * 0.1);
                } else if (opinions.get(p1).subtract(opinions.get(p2)).abs().compareTo(Real.of(0.2)) < 0) {
                    // New connection if opinions are close (input double check)
                    network.addConnection(p1, p2, 0.1);
                }
            }
        }
    }

    private void updateMetrics() {
        Real polar = OpinionDynamics.calculatePolarization(convertToIdMap(opinions));
        globalStability = Real.ONE.subtract(polar);
    }

    /**
     * Simulates an election based on the current opinions of the society members.
     * Each person votes for candidates based on ideological distance to their current opinion.
     *
     * @param title      the election title
     * @param candidates list of candidate names
     * @param method     the voting method to use
     * @return the list of winners
     */
    public List<String> holdElection(String title, List<String> candidates, VotingMethod method) {
        Election election = new Election(title, new Country("Democratic State", "DS"), LocalDate.now());
        
        // Define candidate positions (distributed across the opinion spectrum)
        Map<String, Real> candidatePositions = new HashMap<>();
        for (int i = 0; i < candidates.size(); i++) {
            // Distribute candidates between -1.0 and 1.0
            double pos = (candidates.size() > 1) ? -1.0 + (2.0 * i) / (candidates.size() - 1) : 0.0;
            candidatePositions.put(candidates.get(i), Real.of(pos));
        }

        for (Person p : opinions.keySet()) {
            Real pOpinion = opinions.get(p);
            
            // Generate a ranking based on distance to candidate positions
            List<String> ranking = new ArrayList<>(candidates);
            ranking.sort(Comparator.comparingDouble(c -> candidatePositions.get(c).subtract(pOpinion).abs().doubleValue()));
            
            Ballot b = Ballot.rankedChoice(p.getId().toString(), title, ranking);
            election.addBallot(b);
        }

        return election.calculateWinners(method, 1);
    }

    private Map<String, Real> convertToIdMap(Map<Person, Real> map) {
        Map<String, Real> idMap = new HashMap<>();
        for (Map.Entry<Person, Real> entry : map.entrySet()) {
            idMap.put(entry.getKey().getId().toString(), entry.getValue());
        }
        return idMap;
    }

    public long getCurrentStep() {
        return currentStep;
    }

    public double getGlobalStability() {
        return globalStability.doubleValue(); // Output double for display
    }

    public SocialNetwork getNetwork() {
        return network;
    }

    public Map<Person, Real> getOpinions() {
        return Collections.unmodifiableMap(opinions);
    }

    @Override
    public String toString() {
        return String.format("Simulation for %s, Step %d, Stability: %.2f", 
                society.getName(), currentStep, globalStability.doubleValue());
    }
}

