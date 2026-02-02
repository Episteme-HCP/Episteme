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

package org.jscience.social.psychology.ai;

import java.util.HashMap;
import java.util.Map;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.engineering.eventdriven.Event;

/**
 * A basic Q-Learning implementation.
 * Q(s, a) <- Q(s, a) + alpha * (reward + gamma * max(Q(s', a')) - Q(s, a))
 */
public class QLearningAgent implements ReinforcementLearningAgent {

    private Map<String, Double> qTable = new HashMap<>();
    private double alpha = 0.1; // Learning rate
    private double gamma = 0.9; // Discount factor
    private double epsilon = 0.1; // Exploration rate
    
    private String lastState;
    private String lastAction;

    public QLearningAgent() {
        // Init
    }

    @Override
    public void observeReward(Real reward) {
        if (lastState != null && lastAction != null) {
            String stateAction = lastState + ":" + lastAction;
            double oldQ = qTable.getOrDefault(stateAction, 0.0);
            
            // Simplified: assuming next state max Q is 0 for now (or need explicit state transition input)
            // Ideally observeReward comes with observeNewState
            double target = reward.doubleValue(); // + gamma * maxQ(newState)
            
            double newQ = oldQ + alpha * (target - oldQ);
            qTable.put(stateAction, newQ);
        }
    }

    @Override
    public void learn() {
        // No-op in simple tabular Q-learning; happens in observeReward
    }

    @Override
    public Event decideAction() {
        // Placeholder logic
        return null;
    }
}
