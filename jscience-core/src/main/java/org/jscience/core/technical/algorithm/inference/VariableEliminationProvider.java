/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.inference;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.BayesianInferenceProvider;
import java.util.*;

/**
 * Implementation of the Variable Elimination algorithm for Bayesian Inference.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class VariableEliminationProvider implements BayesianInferenceProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public Real query(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes) {
        List<Factor> factors = new ArrayList<>();
        for (BayesNodeData node : nodes) {
            factors.add(toFactor(node, evidence));
        }

        Set<String> allVars = new HashSet<>();
        for (BayesNodeData node : nodes) {
            allVars.add(node.getName());
        }
        
        List<String> varsToEliminate = new ArrayList<>(allVars);
        varsToEliminate.remove(target);
        varsToEliminate.removeAll(evidence.keySet());

        for (String var : varsToEliminate) {
            List<Factor> toMultiply = new ArrayList<>();
            List<Factor> remaining = new ArrayList<>();
            for (Factor f : factors) {
                if (f.variables.contains(var)) {
                    toMultiply.add(f);
                } else {
                    remaining.add(f);
                }
            }
            if (!toMultiply.isEmpty()) {
                Factor product = Factor.multiply(toMultiply);
                Factor marginalized = product.marginalize(var);
                remaining.add(marginalized);
            }
            factors = remaining;
        }

        Factor finalFactor = Factor.multiply(factors);
        Factor normalized = finalFactor.normalize();
        
        Map<String, String> assignment = new HashMap<>();
        assignment.put(target, targetState);
        return Real.of(normalized.get(assignment));
    }

    private Factor toFactor(BayesNodeData node, Map<String, String> evidence) {
        String name = node.getName();
        Map<Map<String, String>, Map<String, Double>> cpt = node.getCPT();
        
        Set<String> vars = new HashSet<>();
        vars.add(name);
        if (!cpt.isEmpty()) {
            vars.addAll(cpt.keySet().iterator().next().keySet());
        }

        Set<String> resultVars = new HashSet<>(vars);
        resultVars.removeAll(evidence.keySet());

        Map<Map<String, String>, Double> table = new HashMap<>();
        for (Map.Entry<Map<String, String>, Map<String, Double>> entry : cpt.entrySet()) {
            Map<String, String> parentStates = entry.getKey();
            for (Map.Entry<String, Double> stateProb : entry.getValue().entrySet()) {
                Map<String, String> fullAssignment = new HashMap<>(parentStates);
                fullAssignment.put(name, stateProb.getKey());
                
                if (isConsistentWithEvidence(fullAssignment, evidence)) {
                    Map<String, String> reducedAssignment = new HashMap<>(fullAssignment);
                    reducedAssignment.keySet().removeAll(evidence.keySet());
                    table.put(reducedAssignment, stateProb.getValue());
                }
            }
        }
        return new Factor(resultVars, table);
    }

    private boolean isConsistentWithEvidence(Map<String, String> assignment, Map<String, String> evidence) {
        for (Map.Entry<String, String> e : evidence.entrySet()) {
            if (assignment.containsKey(e.getKey()) && !assignment.get(e.getKey()).equals(e.getValue())) {
                return false;
            }
        }
        return true;
    }

    private static class Factor {
        private final Set<String> variables;
        private final Map<Map<String, String>, Double> table;

        Factor(Set<String> variables, Map<Map<String, String>, Double> table) {
            this.variables = variables;
            this.table = table;
        }

        static Factor multiply(List<Factor> factors) {
            if (factors.isEmpty()) return new Factor(Collections.emptySet(), Collections.singletonMap(new HashMap<>(), 1.0));
            Factor result = factors.get(0);
            for (int i = 1; i < factors.size(); i++) {
                result = result.multiply(factors.get(i));
            }
            return result;
        }

        Factor multiply(Factor other) {
            Set<String> newVars = new HashSet<>(this.variables);
            newVars.addAll(other.variables);
            Map<Map<String, String>, Double> newTable = new HashMap<>();

            for (Map.Entry<Map<String, String>, Double> e1 : this.table.entrySet()) {
                for (Map.Entry<Map<String, String>, Double> e2 : other.table.entrySet()) {
                    if (isCompatible(e1.getKey(), e2.getKey())) {
                        Map<String, String> combined = new HashMap<>(e1.getKey());
                        combined.putAll(e2.getKey());
                        newTable.put(combined, e1.getValue() * e2.getValue());
                    }
                }
            }
            return new Factor(newVars, newTable);
        }

        boolean isCompatible(Map<String, String> a, Map<String, String> b) {
            for (String var : a.keySet()) {
                if (b.containsKey(var) && !a.get(var).equals(b.get(var))) return false;
            }
            return true;
        }

        Factor marginalize(String var) {
            Set<String> newVars = new HashSet<>(variables);
            newVars.remove(var);
            Map<Map<String, String>, Double> newTable = new HashMap<>();

            for (Map.Entry<Map<String, String>, Double> entry : table.entrySet()) {
                Map<String, String> assignment = new HashMap<>(entry.getKey());
                assignment.remove(var);
                newTable.put(assignment, newTable.getOrDefault(assignment, 0.0) + entry.getValue());
            }
            return new Factor(newVars, newTable);
        }

        Factor normalize() {
            double sum = 0;
            for (double val : table.values()) sum += val;
            Map<Map<String, String>, Double> newTable = new HashMap<>();
            for (Map.Entry<Map<String, String>, Double> entry : table.entrySet()) {
                newTable.put(entry.getKey(), entry.getValue() / (sum == 0 ? 1.0 : sum));
            }
            return new Factor(variables, newTable);
        }

        double get(Map<String, String> assignment) {
            return table.getOrDefault(assignment, 0.0);
        }
    }

    @Override
    public String getName() {
        return "Variable Elimination (Exact Inference)";
    }
}
