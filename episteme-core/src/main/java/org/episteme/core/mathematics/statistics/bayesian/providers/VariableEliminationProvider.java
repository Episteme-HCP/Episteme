/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.statistics.bayesian.providers;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.statistics.bayesian.BayesianInferenceProvider;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.util.*;

/**
 * Implementation of the Variable Elimination algorithm for Bayesian Inference.
 * Supports multiple precisions (float, double, Real).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class VariableEliminationProvider implements BayesianInferenceProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public Real query(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes) {
        List<RealFactor> factors = new ArrayList<>();
        for (BayesNodeData node : nodes) {
            factors.add(toRealFactor(node, evidence));
        }

        Set<String> allVars = new HashSet<>();
        for (BayesNodeData node : nodes) {
            allVars.add(node.getName());
        }
        
        List<String> varsToEliminate = new ArrayList<>(allVars);
        varsToEliminate.remove(target);
        varsToEliminate.removeAll(evidence.keySet());

        for (String var : varsToEliminate) {
            List<RealFactor> toMultiply = new ArrayList<>();
            List<RealFactor> remaining = new ArrayList<>();
            for (RealFactor f : factors) {
                if (f.variables.contains(var)) {
                    toMultiply.add(f);
                } else {
                    remaining.add(f);
                }
            }
            if (!toMultiply.isEmpty()) {
                RealFactor product = RealFactor.multiply(toMultiply);
                RealFactor marginalized = product.marginalize(var);
                remaining.add(marginalized);
            }
            factors = remaining;
        }

        RealFactor finalFactor = RealFactor.multiply(factors);
        RealFactor normalized = finalFactor.normalize();
        
        Map<String, String> assignment = new HashMap<>();
        assignment.put(target, targetState);
        return normalized.get(assignment);
    }

    @Override
    public float queryFloat(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes) {
        List<FloatFactor> factors = new ArrayList<>();
        for (BayesNodeData node : nodes) {
            factors.add(toFloatFactor(node, evidence));
        }

        Set<String> allVars = new HashSet<>();
        for (BayesNodeData node : nodes) {
            allVars.add(node.getName());
        }
        
        List<String> varsToEliminate = new ArrayList<>(allVars);
        varsToEliminate.remove(target);
        varsToEliminate.removeAll(evidence.keySet());

        for (String var : varsToEliminate) {
            List<FloatFactor> toMultiply = new ArrayList<>();
            List<FloatFactor> remaining = new ArrayList<>();
            for (FloatFactor f : factors) {
                if (f.variables.contains(var)) toMultiply.add(f);
                else remaining.add(f);
            }
            if (!toMultiply.isEmpty()) {
                remaining.add(FloatFactor.multiply(toMultiply).marginalize(var));
            }
            factors = remaining;
        }

        FloatFactor finalFactor = FloatFactor.multiply(factors);
        FloatFactor normalized = finalFactor.normalize();
        
        Map<String, String> assignment = new HashMap<>();
        assignment.put(target, targetState);
        return normalized.get(assignment);
    }

    @Override
    public double queryDouble(String target, String targetState, Map<String, String> evidence, List<BayesNodeData> nodes) {
        List<DoubleFactor> factors = new ArrayList<>();
        for (BayesNodeData node : nodes) {
            factors.add(toDoubleFactor(node, evidence));
        }

        Set<String> allVars = new HashSet<>();
        for (BayesNodeData node : nodes) {
            allVars.add(node.getName());
        }
        
        List<String> varsToEliminate = new ArrayList<>(allVars);
        varsToEliminate.remove(target);
        varsToEliminate.removeAll(evidence.keySet());

        for (String var : varsToEliminate) {
            List<DoubleFactor> toMultiply = new ArrayList<>();
            List<DoubleFactor> remaining = new ArrayList<>();
            for (DoubleFactor f : factors) {
                if (f.variables.contains(var)) toMultiply.add(f);
                else remaining.add(f);
            }
            if (!toMultiply.isEmpty()) {
                remaining.add(DoubleFactor.multiply(toMultiply).marginalize(var));
            }
            factors = remaining;
        }

        DoubleFactor finalFactor = DoubleFactor.multiply(factors);
        DoubleFactor normalized = finalFactor.normalize();
        
        Map<String, String> assignment = new HashMap<>();
        assignment.put(target, targetState);
        return normalized.get(assignment);
    }

    private RealFactor toRealFactor(BayesNodeData node, Map<String, String> evidence) {
        String name = node.getName();
        Map<Map<String, String>, Map<String, Real>> cpt = node.getCPTReal();
        Set<String> vars = new HashSet<>();
        vars.add(name);
        if (!cpt.isEmpty()) vars.addAll(cpt.keySet().iterator().next().keySet());
        Set<String> resultVars = new HashSet<>(vars);
        resultVars.removeAll(evidence.keySet());

        Map<Map<String, String>, Real> table = new HashMap<>();
        for (var entry : cpt.entrySet()) {
            for (var stateProb : entry.getValue().entrySet()) {
                Map<String, String> full = new HashMap<>(entry.getKey());
                full.put(name, stateProb.getKey());
                if (isConsistentWithEvidence(full, evidence)) {
                    Map<String, String> reduced = new HashMap<>(full);
                    reduced.keySet().removeAll(evidence.keySet());
                    table.put(reduced, stateProb.getValue());
                }
            }
        }
        return new RealFactor(resultVars, table);
    }

    private FloatFactor toFloatFactor(BayesNodeData node, Map<String, String> evidence) {
        String name = node.getName();
        Map<Map<String, String>, Map<String, Float>> cpt = node.getCPTFloat();
        Set<String> vars = new HashSet<>();
        vars.add(name);
        if (!cpt.isEmpty()) vars.addAll(cpt.keySet().iterator().next().keySet());
        Set<String> resultVars = new HashSet<>(vars);
        resultVars.removeAll(evidence.keySet());

        Map<Map<String, String>, Float> table = new HashMap<>();
        for (var entry : cpt.entrySet()) {
            for (var stateProb : entry.getValue().entrySet()) {
                Map<String, String> full = new HashMap<>(entry.getKey());
                full.put(name, stateProb.getKey());
                if (isConsistentWithEvidence(full, evidence)) {
                    Map<String, String> reduced = new HashMap<>(full);
                    reduced.keySet().removeAll(evidence.keySet());
                    table.put(reduced, stateProb.getValue());
                }
            }
        }
        return new FloatFactor(resultVars, table);
    }

    private DoubleFactor toDoubleFactor(BayesNodeData node, Map<String, String> evidence) {
        String name = node.getName();
        Map<Map<String, String>, Map<String, Double>> cpt = node.getCPT();
        Set<String> vars = new HashSet<>();
        vars.add(name);
        if (!cpt.isEmpty()) vars.addAll(cpt.keySet().iterator().next().keySet());
        Set<String> resultVars = new HashSet<>(vars);
        resultVars.removeAll(evidence.keySet());

        Map<Map<String, String>, Double> table = new HashMap<>();
        for (var entry : cpt.entrySet()) {
            for (var stateProb : entry.getValue().entrySet()) {
                Map<String, String> full = new HashMap<>(entry.getKey());
                full.put(name, stateProb.getKey());
                if (isConsistentWithEvidence(full, evidence)) {
                    Map<String, String> reduced = new HashMap<>(full);
                    reduced.keySet().removeAll(evidence.keySet());
                    table.put(reduced, stateProb.getValue());
                }
            }
        }
        return new DoubleFactor(resultVars, table);
    }

    private boolean isConsistentWithEvidence(Map<String, String> assignment, Map<String, String> evidence) {
        for (Map.Entry<String, String> e : evidence.entrySet()) {
            if (assignment.containsKey(e.getKey()) && !assignment.get(e.getKey()).equals(e.getValue())) {
                return false;
            }
        }
        return true;
    }

    // --- Specialized Factor Classes ---

    private static class RealFactor {
        private final Set<String> variables;
        private final Map<Map<String, String>, Real> table;

        RealFactor(Set<String> variables, Map<Map<String, String>, Real> table) {
            this.variables = variables;
            this.table = table;
        }

        static RealFactor multiply(List<RealFactor> factors) {
            if (factors.isEmpty()) return new RealFactor(Collections.emptySet(), Collections.singletonMap(new HashMap<>(), Real.ONE));
            RealFactor result = factors.get(0);
            for (int i = 1; i < factors.size(); i++) result = result.multiply(factors.get(i));
            return result;
        }

        RealFactor multiply(RealFactor other) {
            Set<String> newVars = new HashSet<>(this.variables);
            newVars.addAll(other.variables);
            Map<Map<String, String>, Real> newTable = new HashMap<>();
            for (var e1 : this.table.entrySet()) {
                for (var e2 : other.table.entrySet()) {
                    if (isCompatible(e1.getKey(), e2.getKey())) {
                        Map<String, String> combined = new HashMap<>(e1.getKey());
                        combined.putAll(e2.getKey());
                        newTable.put(combined, e1.getValue().multiply(e2.getValue()));
                    }
                }
            }
            return new RealFactor(newVars, newTable);
        }

        static boolean isCompatible(Map<String, String> a, Map<String, String> b) {
            for (String var : a.keySet()) {
                if (b.containsKey(var) && !a.get(var).equals(b.get(var))) return false;
            }
            return true;
        }

        RealFactor marginalize(String var) {
            Set<String> newVars = new HashSet<>(variables);
            newVars.remove(var);
            Map<Map<String, String>, Real> newTable = new HashMap<>();
            for (var entry : table.entrySet()) {
                Map<String, String> assignment = new HashMap<>(entry.getKey());
                assignment.remove(var);
                newTable.put(assignment, newTable.getOrDefault(assignment, Real.ZERO).add(entry.getValue()));
            }
            return new RealFactor(newVars, newTable);
        }

        RealFactor normalize() {
            Real sum = Real.ZERO;
            for (Real val : table.values()) sum = sum.add(val);
            Map<Map<String, String>, Real> newTable = new HashMap<>();
            Real scale = sum.isZero() ? Real.ONE : sum.inverse();
            for (var entry : table.entrySet()) {
                newTable.put(entry.getKey(), entry.getValue().multiply(scale));
            }
            return new RealFactor(variables, newTable);
        }

        Real get(Map<String, String> assignment) {
            return table.getOrDefault(assignment, Real.ZERO);
        }
    }

    private static class FloatFactor {
        private final Set<String> variables;
        private final Map<Map<String, String>, Float> table;

        FloatFactor(Set<String> variables, Map<Map<String, String>, Float> table) {
            this.variables = variables;
            this.table = table;
        }

        static FloatFactor multiply(List<FloatFactor> factors) {
            if (factors.isEmpty()) return new FloatFactor(Collections.emptySet(), Collections.singletonMap(new HashMap<>(), 1.0f));
            FloatFactor result = factors.get(0);
            for (int i = 1; i < factors.size(); i++) result = result.multiply(factors.get(i));
            return result;
        }

        FloatFactor multiply(FloatFactor other) {
            Set<String> newVars = new HashSet<>(this.variables);
            newVars.addAll(other.variables);
            Map<Map<String, String>, Float> newTable = new HashMap<>();
            for (var e1 : this.table.entrySet()) {
                for (var e2 : other.table.entrySet()) {
                    if (RealFactor.isCompatible(e1.getKey(), e2.getKey())) {
                        Map<String, String> combined = new HashMap<>(e1.getKey());
                        combined.putAll(e2.getKey());
                        newTable.put(combined, e1.getValue() * e2.getValue());
                    }
                }
            }
            return new FloatFactor(newVars, newTable);
        }

        FloatFactor marginalize(String var) {
            Set<String> newVars = new HashSet<>(variables);
            newVars.remove(var);
            Map<Map<String, String>, Float> newTable = new HashMap<>();
            for (var entry : table.entrySet()) {
                Map<String, String> assignment = new HashMap<>(entry.getKey());
                assignment.remove(var);
                newTable.put(assignment, newTable.getOrDefault(assignment, 0.0f) + entry.getValue());
            }
            return new FloatFactor(newVars, newTable);
        }

        FloatFactor normalize() {
            float sum = 0;
            for (float val : table.values()) sum += val;
            Map<Map<String, String>, Float> newTable = new HashMap<>();
            float scale = sum == 0 ? 1.0f : 1.0f / sum;
            for (var entry : table.entrySet()) newTable.put(entry.getKey(), entry.getValue() * scale);
            return new FloatFactor(variables, newTable);
        }

        float get(Map<String, String> assignment) {
            return table.getOrDefault(assignment, 0.0f);
        }
    }

    private static class DoubleFactor {
        private final Set<String> variables;
        private final Map<Map<String, String>, Double> table;

        DoubleFactor(Set<String> variables, Map<Map<String, String>, Double> table) {
            this.variables = variables;
            this.table = table;
        }

        static DoubleFactor multiply(List<DoubleFactor> factors) {
            if (factors.isEmpty()) return new DoubleFactor(Collections.emptySet(), Collections.singletonMap(new HashMap<>(), 1.0));
            DoubleFactor result = factors.get(0);
            for (int i = 1; i < factors.size(); i++) result = result.multiply(factors.get(i));
            return result;
        }

        DoubleFactor multiply(DoubleFactor other) {
            Set<String> newVars = new HashSet<>(this.variables);
            newVars.addAll(other.variables);
            Map<Map<String, String>, Double> newTable = new HashMap<>();
            for (var e1 : this.table.entrySet()) {
                for (var e2 : other.table.entrySet()) {
                    if (RealFactor.isCompatible(e1.getKey(), e2.getKey())) {
                        Map<String, String> combined = new HashMap<>(e1.getKey());
                        combined.putAll(e2.getKey());
                        newTable.put(combined, e1.getValue() * e2.getValue());
                    }
                }
            }
            return new DoubleFactor(newVars, newTable);
        }

        DoubleFactor marginalize(String var) {
            Set<String> newVars = new HashSet<>(variables);
            newVars.remove(var);
            Map<Map<String, String>, Double> newTable = new HashMap<>();
            for (var entry : table.entrySet()) {
                Map<String, String> assignment = new HashMap<>(entry.getKey());
                assignment.remove(var);
                newTable.put(assignment, newTable.getOrDefault(assignment, 0.0) + entry.getValue());
            }
            return new DoubleFactor(newVars, newTable);
        }

        DoubleFactor normalize() {
            double sum = 0;
            for (double val : table.values()) sum += val;
            Map<Map<String, String>, Double> newTable = new HashMap<>();
            double scale = sum == 0 ? 1.0 : 1.0 / sum;
            for (var entry : table.entrySet()) newTable.put(entry.getKey(), entry.getValue() * scale);
            return new DoubleFactor(variables, newTable);
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
