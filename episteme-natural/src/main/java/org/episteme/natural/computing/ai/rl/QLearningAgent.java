/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.computing.ai.rl;

import java.util.*;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * A generic Q-Learning agent implementation.
 * Learns a policy mapping states to actions to maximize cumulative reward.
 *
 * @param <S> The type of the state (must properly implement equals/hashCode).
 * @param <A> The type of the action.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QLearningAgent<S, A> {

    private final Map<S, Map<A, Double>> qTable = new HashMap<>();
    private final List<A> possibleActions;
    
    private double alpha = 0.1; // Learning rate
    private double gamma = 0.9; // Discount factor
    private double epsilon = 0.1; // Exploration rate
    private final Random random = new Random();

    public QLearningAgent(List<A> possibleActions) {
        this.possibleActions = new ArrayList<>(Objects.requireNonNull(possibleActions));
    }

    /**
     * Chooses an action based on the current state using an epsilon-greedy policy.
     * @param state The current state.
     * @return The chosen action.
     */
    public A chooseAction(S state) {
        if (random.nextDouble() < epsilon) {
            return possibleActions.get(random.nextInt(possibleActions.size()));
        } else {
            return getBestAction(state);
        }
    }

    /**
     * Updates the Q-value for a state-action pair based on the received reward and next state.
     * Q(s, a) = Q(s, a) + alpha * (reward + gamma * max(Q(s', a')) - Q(s, a))
     *
     * @param state The previous state.
     * @param action The action taken.
     * @param reward The reward received.
     * @param nextState The new state arrived at.
     */
    public void learn(S state, A action, double reward, S nextState) {
        Map<A, Double> stateActions = qTable.computeIfAbsent(state, k -> new HashMap<>());
        double oldQ = stateActions.getOrDefault(action, 0.0);
        double maxQNext = getMaxQ(nextState);
        
        double newQ = oldQ + alpha * (reward + gamma * maxQNext - oldQ);
        stateActions.put(action, newQ);
    }
    
    public void learn(S state, A action, Real reward, S nextState) {
        learn(state, action, reward.doubleValue(), nextState);
    }

    /**
     * Returns the best action for a given state (exploitation).
     */
    public A getBestAction(S state) {
        Map<A, Double> actions = qTable.get(state);
        if (actions == null || actions.isEmpty()) {
            return possibleActions.get(random.nextInt(possibleActions.size()));
        }
        
        A bestAction = null;
        double maxVal = -Double.MAX_VALUE;
        
        for (A action : possibleActions) {
            double val = actions.getOrDefault(action, 0.0);
            if (val > maxVal) {
                maxVal = val;
                bestAction = action;
            }
        }
        return bestAction != null ? bestAction : possibleActions.get(0);
    }

    /**
     * Gets the maximum Q-value for a given state.
     */
    public double getMaxQ(S state) {
        Map<A, Double> actions = qTable.get(state);
        if (actions == null || actions.isEmpty()) return 0.0;
        
        return actions.values().stream().mapToDouble(v -> v).max().orElse(0.0);
    }

    // Setters for hyperparameters
    public void setLearningRate(double alpha) { this.alpha = alpha; }
    public void setDiscountFactor(double gamma) { this.gamma = gamma; }
    public void setExplorationRate(double epsilon) { this.epsilon = epsilon; }
}
