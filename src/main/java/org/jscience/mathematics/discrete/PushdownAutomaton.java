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
 * Pushdown Automaton (PDA).
 * <p>
 * A PDA is defined by M = (Q, Σ, Γ, δ, q₀, Z₀, F) where:
 * <ul>
 * <li>Q is a finite set of states</li>
 * <li>Σ is the input alphabet</li>
 * <li>Γ is the stack alphabet</li>
 * <li>δ: Q × (Σ ∪ {ε}) × Γ → P(Q × Γ*) is the transition function</li>
 * <li>q₀ ∈ Q is the initial state</li>
 * <li>Z₀ ∈ Γ is the initial stack symbol</li>
 * <li>F ⊆ Q is the set of accepting states</li>
 * </ul>
 * </p>
 * 
 * @param <S> state type
 * @param <A> input alphabet symbol type
 * @param <G> stack alphabet symbol type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Pushdown_automaton">Pushdown
 *      Automaton (Wikipedia)</a>
 */
public class PushdownAutomaton<S, A, G> {

    private final Set<S> states;
    private final Set<A> inputAlphabet;
    private final Set<G> stackAlphabet;
    private final S initialState;
    private final G initialStackSymbol;
    private final Set<S> acceptingStates;

    // Transition: State -> (InputSymbol -> (StackTop -> Set<(NextState,
    // PushSymbols)>))
    // InputSymbol can be null for epsilon transition
    private final Map<S, Map<A, Map<G, Set<TransitionResult>>>> transitions;

    public class TransitionResult {
        public final S nextState;
        public final List<G> symbolsToPush; // Empty list for pop

        public TransitionResult(S nextState, List<G> symbolsToPush) {
            this.nextState = nextState;
            this.symbolsToPush = symbolsToPush;
        }
    }

    public PushdownAutomaton(S initialState, G initialStackSymbol) {
        this.states = new HashSet<>();
        this.inputAlphabet = new HashSet<>();
        this.stackAlphabet = new HashSet<>();
        this.initialState = initialState;
        this.initialStackSymbol = initialStackSymbol;
        this.acceptingStates = new HashSet<>();
        this.transitions = new HashMap<>();

        states.add(initialState);
        stackAlphabet.add(initialStackSymbol);
    }

    public void addState(S state, boolean isAccepting) {
        states.add(state);
        if (isAccepting) {
            acceptingStates.add(state);
        }
    }

    public void addTransition(S fromState, A input, G stackTop, S toState, List<G> push) {
        states.add(fromState);
        states.add(toState);
        if (input != null)
            inputAlphabet.add(input);
        stackAlphabet.add(stackTop);
        stackAlphabet.addAll(push);

        transitions.computeIfAbsent(fromState, k -> new HashMap<>())
                .computeIfAbsent(input, k -> new HashMap<>())
                .computeIfAbsent(stackTop, k -> new HashSet<>())
                .add(new TransitionResult(toState, push));
    }

    /**
     * Simulates the PDA on the given input.
     * Uses Depth-First Search to explore non-deterministic paths.
     * 
     * @param input sequence of symbols
     * @return true if accepted
     */
    public boolean accepts(List<A> input) {
        Stack<G> stack = new Stack<>();
        stack.push(initialStackSymbol);
        return explore(initialState, input, 0, stack);
    }

    private boolean explore(S currentState, List<A> input, int inputIndex, Stack<G> stack) {
        // Check if accepted: input consumed and (in accepting state OR stack empty
        // depending on acceptance criteria)
        // Here assuming acceptance by final state
        if (inputIndex == input.size() && acceptingStates.contains(currentState)) {
            return true;
        }

        if (stack.isEmpty()) {
            return false; // Cannot move if stack is empty
        }

        G stackTop = stack.peek();
        Map<A, Map<G, Set<TransitionResult>>> stateTrans = transitions.get(currentState);
        if (stateTrans == null)
            return false;

        // 1. Try epsilon transitions (input null)
        if (stateTrans.containsKey(null)) {
            Map<G, Set<TransitionResult>> epsTrans = stateTrans.get(null);
            if (epsTrans.containsKey(stackTop)) {
                for (TransitionResult res : epsTrans.get(stackTop)) {
                    Stack<G> nextStack = (Stack<G>) stack.clone();
                    nextStack.pop();
                    // Push symbols in reverse order so first in list is top of stack
                    for (int i = res.symbolsToPush.size() - 1; i >= 0; i--) {
                        nextStack.push(res.symbolsToPush.get(i));
                    }
                    if (explore(res.nextState, input, inputIndex, nextStack))
                        return true;
                }
            }
        }

        // 2. Try consuming input
        if (inputIndex < input.size()) {
            A symbol = input.get(inputIndex);
            if (stateTrans.containsKey(symbol)) {
                Map<G, Set<TransitionResult>> symTrans = stateTrans.get(symbol);
                if (symTrans.containsKey(stackTop)) {
                    for (TransitionResult res : symTrans.get(stackTop)) {
                        Stack<G> nextStack = (Stack<G>) stack.clone();
                        nextStack.pop();
                        for (int i = res.symbolsToPush.size() - 1; i >= 0; i--) {
                            nextStack.push(res.symbolsToPush.get(i));
                        }
                        if (explore(res.nextState, input, inputIndex + 1, nextStack))
                            return true;
                    }
                }
            }
        }

        return false;
    }
}

