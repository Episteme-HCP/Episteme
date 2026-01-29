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

package org.jscience.economics.models;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.UniversalDataModel;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.io.Serializable;
import java.util.*;

/**
 * Universal data model for strategic decision making and game theory.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class StrategicModel implements UniversalDataModel, ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

    @Persistent
    public static class Payoff implements Serializable {
        private static final long serialVersionUID = 1L;
        @Attribute
        private String label;
        @Attribute
        private Real player1;
        @Attribute
        private Real player2;

        public Payoff() {}
        public Payoff(String label, Real player1, Real player2) {
            this.label = label;
            this.player1 = player1;
            this.player2 = player2;
        }
        public String getLabel() { return label; }
        public Real getPlayer1() { return player1; }
        public Real getPlayer2() { return player2; }
    }

    @Persistent
    public static class DecisionNode implements Serializable {
        private static final long serialVersionUID = 1L;
        @Attribute
        private String label;
        @Attribute
        private String action;
        @Relation(type = Relation.Type.ONE_TO_MANY)
        private List<DecisionNode> branches = new ArrayList<>();

        public DecisionNode() {}
        public DecisionNode(String label, String action, List<DecisionNode> branches) {
            this.label = label;
            this.action = action;
            this.branches = branches != null ? branches : new ArrayList<>();
        }
        public String getLabel() { return label; }
        public String getAction() { return action; }
        public List<DecisionNode> getBranches() { return branches; }
    }

    @Attribute
    private final Map<String, Payoff> payoffMatrix = new LinkedHashMap<>();
    
    @Attribute
    private final List<Real[]> paretoFrontier = new ArrayList<>();

    public StrategicModel() {
        this(new SimpleIdentification("Strategy-" + UUID.randomUUID()));
    }

    public StrategicModel(Identification id) {
        this.id = Objects.requireNonNull(id);
    }

    public void addPayoff(String key, Real p1, Real p2) {
        payoffMatrix.put(key, new Payoff(key, p1, p2));
    }

    public void addParetoPoint(Real x, Real y) {
        paretoFrontier.add(new Real[]{x, y});
    }

    public Map<String, Payoff> getPayoffMatrix() { return Collections.unmodifiableMap(payoffMatrix); }
    public List<Real[]> getParetoFrontier() { return Collections.unmodifiableList(paretoFrontier); }

    @Override public String getModelType() { return "Game Theory / Strategic Model"; }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }
}
