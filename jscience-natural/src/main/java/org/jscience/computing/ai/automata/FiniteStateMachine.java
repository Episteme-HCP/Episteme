/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.computing.ai.automata;

import java.util.*;

/**
 * Finite State Machine (FSM) implementation.
 * <p>
 * A finite state machine is a mathematical model of computation. It consists
 * of a finite number of states, transitions between states triggered by
 * input symbols, and optionally produces output.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FiniteStateMachine<S, I> {

    private S currentState;
    private final S initialState;
    private final Set<S> states = new HashSet<>();
    private final Set<S> acceptingStates = new HashSet<>();
    private final Map<S, Map<I, S>> transitions = new HashMap<>();
    private final List<StateChangeListener<S, I>> listeners = new ArrayList<>();

    /**
     * Creates a new FSM with the given initial state.
     *
     * @param initialState the starting state
     */
    public FiniteStateMachine(S initialState) {
        this.initialState = initialState;
        this.currentState = initialState;
        this.states.add(initialState);
    }

    /**
     * Adds a state to the FSM.
     *
     * @param state     the state to add
     * @param accepting whether this is an accepting/final state
     * @return this FSM for chaining
     */
    public FiniteStateMachine<S, I> addState(S state, boolean accepting) {
        states.add(state);
        if (accepting) {
            acceptingStates.add(state);
        }
        return this;
    }

    /**
     * Adds a transition from one state to another on a given input.
     *
     * @param from  source state
     * @param input input symbol
     * @param to    destination state
     * @return this FSM for chaining
     */
    public FiniteStateMachine<S, I> addTransition(S from, I input, S to) {
        states.add(from);
        states.add(to);
        transitions.computeIfAbsent(from, k -> new HashMap<>()).put(input, to);
        return this;
    }

    /**
     * Processes an input symbol, transitioning to the next state.
     *
     * @param input the input symbol
     * @return true if a transition was made
     * @throws IllegalStateException if no transition exists for the input
     */
    public boolean process(I input) {
        Map<I, S> stateTransitions = transitions.get(currentState);
        if (stateTransitions == null || !stateTransitions.containsKey(input)) {
            return false;
        }

        S previousState = currentState;
        currentState = stateTransitions.get(input);

        for (StateChangeListener<S, I> listener : listeners) {
            listener.onStateChange(previousState, input, currentState);
        }

        return true;
    }

    /**
     * Processes a sequence of inputs.
     *
     * @param inputs the input sequence
     * @return true if all inputs were processed successfully
     */
    public boolean processAll(Iterable<I> inputs) {
        for (I input : inputs) {
            if (!process(input)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the current state.
     *
     * @return current state
     */
    public S getCurrentState() {
        return currentState;
    }

    /**
     * Checks if the FSM is in an accepting state.
     *
     * @return true if current state is accepting
     */
    public boolean isAccepting() {
        return acceptingStates.contains(currentState);
    }

    /**
     * Resets the FSM to its initial state.
     */
    public void reset() {
        this.currentState = initialState;
    }

    /**
     * Returns all states.
     *
     * @return unmodifiable set of states
     */
    public Set<S> getStates() {
        return Collections.unmodifiableSet(states);
    }

    /**
     * Adds a state change listener.
     *
     * @param listener the listener
     */
    public void addStateChangeListener(StateChangeListener<S, I> listener) {
        listeners.add(listener);
    }

    /**
     * Listener interface for state changes.
     */
    @FunctionalInterface
    public interface StateChangeListener<S, I> {
        void onStateChange(S from, I input, S to);
    }
}
