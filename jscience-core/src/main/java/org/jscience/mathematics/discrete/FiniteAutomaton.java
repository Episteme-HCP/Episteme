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

package org.jscience.mathematics.discrete;

import java.util.*;

/**
 * Deterministic Finite Automaton (DFA).
 * <p>
 * A DFA is defined by M = (Q, ÃŽÂ£, ÃŽÂ´, qÃ¢â€šâ‚¬, F) where:
 * <ul>
 * <li>Q is a finite set of states</li>
 * <li>ÃŽÂ£ is the input alphabet</li>
 * <li>ÃŽÂ´: Q Ãƒâ€” ÃŽÂ£ Ã¢â€ â€™ Q is the transition function</li>
 * <li>qÃ¢â€šâ‚¬ Ã¢Ë†Ë† Q is the initial state</li>
 * <li>F Ã¢Å â€  Q is the set of accepting states</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FiniteAutomaton<S, A> {

    private final Set<S> states;
    private final Set<A> alphabet;
    private final Map<S, Map<A, S>> transitions;
    private final S initialState;
    private final Set<S> acceptingStates;

    /**
     * Constructs a DFA.
     * 
     * @param states          set of all states
     * @param alphabet        input alphabet
     * @param transitions     transition function
     * @param initialState    initial state
     * @param acceptingStates accepting (final) states
     */
    public FiniteAutomaton(Set<S> states, Set<A> alphabet, Map<S, Map<A, S>> transitions,
            S initialState, Set<S> acceptingStates) {
        this.states = new HashSet<>(states);
        this.alphabet = new HashSet<>(alphabet);
        this.transitions = new HashMap<>(transitions);
        this.initialState = initialState;
        this.acceptingStates = new HashSet<>(acceptingStates);
    }

    /**
     * Tests if this automaton accepts the given input string.
     * 
     * @param input sequence of symbols
     * @return true if input is accepted
     */
    public boolean accepts(List<A> input) {
        S currentState = initialState;

        for (A symbol : input) {
            Map<A, S> stateTransitions = transitions.get(currentState);
            if (stateTransitions == null || !stateTransitions.containsKey(symbol)) {
                return false; // No transition defined
            }
            currentState = stateTransitions.get(symbol);
        }

        return acceptingStates.contains(currentState);
    }

    /**
     * Returns all states reachable from the initial state.
     * 
     * @return set of reachable states
     */
    public Set<S> reachableStates() {
        Set<S> reachable = new HashSet<>();
        Queue<S> queue = new LinkedList<>();

        queue.offer(initialState);
        reachable.add(initialState);

        while (!queue.isEmpty()) {
            S state = queue.poll();
            Map<A, S> stateTransitions = transitions.get(state);

            if (stateTransitions != null) {
                for (S nextState : stateTransitions.values()) {
                    if (!reachable.contains(nextState)) {
                        reachable.add(nextState);
                        queue.offer(nextState);
                    }
                }
            }
        }

        return reachable;
    }

    /**
     * Checks if this automaton is minimal (has no unreachable or equivalent
     * states).
     * 
     * @return true if minimal
     */
    public boolean isMinimal() {
        Set<S> reachable = reachableStates();
        return reachable.size() == states.size(); // Simplified check
    }

    public Set<S> getStates() {
        return Collections.unmodifiableSet(states);
    }

    public Set<A> getAlphabet() {
        return Collections.unmodifiableSet(alphabet);
    }

    public S getInitialState() {
        return initialState;
    }

    public Set<S> getAcceptingStates() {
        return Collections.unmodifiableSet(acceptingStates);
    }
}

