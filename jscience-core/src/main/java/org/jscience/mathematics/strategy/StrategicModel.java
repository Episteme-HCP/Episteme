/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.strategy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.data.UniversalDataModel;
import java.util.*;

/**
 * Universal data model for strategic decision making and game theory.
 */
public class StrategicModel implements UniversalDataModel {

    public record Payoff(String label, Real player1, Real player2) {}
    public record DecisionNode(String label, String action, List<DecisionNode> branches) {}

    private final Map<String, Payoff> payoffMatrix = new LinkedHashMap<>();
    private final List<Real[]> paretoFrontier = new ArrayList<>();

    public void addPayoff(String key, Real p1, Real p2) {
        payoffMatrix.put(key, new Payoff(key, p1, p2));
    }

    public void addParetoPoint(Real x, Real y) {
        paretoFrontier.add(new Real[]{x, y});
    }

    public Map<String, Payoff> getPayoffMatrix() { return Collections.unmodifiableMap(payoffMatrix); }
    public List<Real[]> getParetoFrontier() { return Collections.unmodifiableList(paretoFrontier); }

    @Override public String getModelType() { return "Game Theory / Strategic Model"; }
}
