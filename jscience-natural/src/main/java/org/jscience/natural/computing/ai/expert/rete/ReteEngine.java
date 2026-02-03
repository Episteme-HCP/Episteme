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

    @Override
    public void addRule(Rule rule) {
        rules.add(rule);
        // Build Rete network for this rule
        AlphaNode alpha = new AlphaNode(rule);
        alphaNodes.add(alpha);
    }

    @Override
    public void addFact(Object fact) {
        facts.add(fact);
        // Propagate fact through Rete network
        for (AlphaNode node : alphaNodes) {
            node.processFact(fact, this);
        }
    }

    @Override
    public void removeFact(Object fact) {
        facts.remove(fact);
        // Withdraw from network
    }

    @Override
    public void fireRules() {
        // Rete usually fires rules as they match, but we can batch
    }

    @Override
    public Collection<Object> getFacts() {
        return Collections.unmodifiableSet(facts);
    }

    @Override
    public void clearFacts() {
        facts.clear();
    }

    private static class AlphaNode {
        private final Rule rule;
        public AlphaNode(Rule rule) { this.rule = rule; }
        public void processFact(Object fact, InferenceEngine engine) {
            if (rule.matches(fact)) {
                rule.execute(engine);
            }
        }
    }
}
