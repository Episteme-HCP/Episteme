package org.jscience.natural.computing.ai.expert.rete;

import org.jscience.natural.computing.ai.expert.InferenceEngine;
import org.jscience.natural.computing.ai.expert.Rule;
import java.util.*;

/**
 * High-performance Rete engine implementation for expert systems.
 */
public class ReteEngine implements InferenceEngine {
    private final List<Rule> rules = new ArrayList<>();
    private final Set<Object> facts = new HashSet<>();
    private final List<AlphaNode> alphaNodes = new ArrayList<>();
    private final List<BetaNode> betaNodes = new ArrayList<>();

    @Override
    public void addRule(Rule rule) {
        rules.add(rule);
        // Build Rete network for this rule
        AlphaNode alpha = new AlphaNode(rule);
        alphaNodes.add(alpha);
        
        // For complex rules, we would chain BetaNodes here
        // Currently supporting simple alpha networks, but prepared for Beta joins
    }

    @Override
    public void addFact(Object fact) {
        if (facts.add(fact)) {
            // Propagate fact through Rete network
            for (AlphaNode node : alphaNodes) {
                node.processFact(fact, this);
            }
        }
    }

    @Override
    public void removeFact(Object fact) {
        if (facts.remove(fact)) {
             // In a full Rete, we would propagate retract signals
        }
    }

    @Override
    public void fireRules() {
        // Rules fire immediately on fact addition in this simple version
        // In complex Rete, we'd process the agenda here
    }

    @Override
    public Collection<Object> getFacts() {
        return Collections.unmodifiableSet(facts);
    }

    @Override
    public void clearFacts() {
        facts.clear();
    }

}
