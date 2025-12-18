/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.discrete;

import java.util.*;

/**
 * Turing Machine (TM).
 * <p>
 * A TM is defined by M = (Q, Γ, b, Σ, δ, q₀, F) where:
 * <ul>
 * <li>Q is a finite set of states</li>
 * <li>Γ is the tape alphabet</li>
 * <li>b ∈ Γ is the blank symbol</li>
 * <li>Σ ⊆ Γ \ {b} is the input alphabet</li>
 * <li>δ: Q × Γ → Q × Γ × {L, R} is the transition function</li>
 * <li>q₀ ∈ Q is the initial state</li>
 * <li>F ⊆ Q is the set of accepting states</li>
 * </ul>
 * </p>
 * 
 * @param <S> state type
 * @param <T> tape symbol type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Turing_machine">Turing Machine
 *      (Wikipedia)</a>
 */
public class TuringMachine<S, T> {

    public enum Direction {
        LEFT, RIGHT, STAY
    }

    private final Set<S> states;
    private final Set<T> tapeAlphabet;
    private final T blankSymbol;
    private final S initialState;
    private final Set<S> acceptingStates;

    // Transition: State -> (TapeSymbol -> (NextState, WriteSymbol, MoveDirection))
    private final Map<S, Map<T, TransitionResult>> transitions;

    public class TransitionResult {
        public final S nextState;
        public final T writeSymbol;
        public final Direction move;

        public TransitionResult(S nextState, T writeSymbol, Direction move) {
            this.nextState = nextState;
            this.writeSymbol = writeSymbol;
            this.move = move;
        }
    }

    public TuringMachine(S initialState, T blankSymbol) {
        this.states = new HashSet<>();
        this.tapeAlphabet = new HashSet<>();
        this.blankSymbol = blankSymbol;
        this.initialState = initialState;
        this.acceptingStates = new HashSet<>();
        this.transitions = new HashMap<>();

        states.add(initialState);
        tapeAlphabet.add(blankSymbol);
    }

    public void addState(S state, boolean isAccepting) {
        states.add(state);
        if (isAccepting) {
            acceptingStates.add(state);
        }
    }

    public void addTransition(S fromState, T readSymbol, S toState, T writeSymbol, Direction move) {
        states.add(fromState);
        states.add(toState);
        tapeAlphabet.add(readSymbol);
        tapeAlphabet.add(writeSymbol);

        transitions.computeIfAbsent(fromState, k -> new HashMap<>())
                .put(readSymbol, new TransitionResult(toState, writeSymbol, move));
    }

    /**
     * Simulates the Turing Machine on the given input.
     * Warning: May not terminate (halting problem).
     * 
     * @param input    initial tape content
     * @param maxSteps maximum steps to prevent infinite loops
     * @return true if accepted, false if rejected or max steps reached
     */
    public boolean run(List<T> input, int maxSteps) {
        // Tape representation: Map<Integer, T>
        // Index 0 is start, positive to right, negative to left
        Map<Integer, T> tape = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            tape.put(i, input.get(i));
        }

        S currentState = initialState;
        int headPosition = 0;
        int steps = 0;

        while (steps < maxSteps) {
            if (acceptingStates.contains(currentState)) {
                return true;
            }

            T currentSymbol = tape.getOrDefault(headPosition, blankSymbol);
            Map<T, TransitionResult> stateTrans = transitions.get(currentState);

            if (stateTrans == null || !stateTrans.containsKey(currentSymbol)) {
                return false; // Halted in non-accepting state
            }

            TransitionResult res = stateTrans.get(currentSymbol);

            // Write
            tape.put(headPosition, res.writeSymbol);

            // Move
            if (res.move == Direction.LEFT)
                headPosition--;
            else if (res.move == Direction.RIGHT)
                headPosition++;

            // State change
            currentState = res.nextState;
            steps++;
        }

        return false; // Max steps reached
    }
}

