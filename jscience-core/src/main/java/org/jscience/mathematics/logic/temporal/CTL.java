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
package org.jscience.mathematics.logic.temporal;

import java.util.*;
import java.util.function.Predicate;

/**
 * Computation Tree Logic (CTL).
 * <p>
 * CTL is a branching-time temporal logic used for model checking.
 * It extends propositional logic with temporal operators:
 * - AG φ: φ holds on All paths Globally
 * - EG φ: φ holds on some path Globally
 * - AF φ: φ holds on All paths Finally
 * - EF φ: φ holds on some path Finally
 * - A[φ U ψ]: φ Until ψ on All paths
 * - E[φ U ψ]: φ Until ψ on some path
 * </p>
 * 
 * @param <S> the type representing states
 * @param <P> the type representing atomic propositions
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class CTL<S, P> {

    private final Set<S> states;
    private final Map<S, Set<S>> transitions;
    private final Map<S, Set<P>> labeling;

    /**
     * Creates an empty CTL model.
     */
    public CTL() {
        this.states = new HashSet<>();
        this.transitions = new HashMap<>();
        this.labeling = new HashMap<>();
    }

    /**
     * Adds a state to the model.
     * 
     * @param state the state to add
     */
    public void addState(S state) {
        states.add(state);
        transitions.putIfAbsent(state, new HashSet<>());
        labeling.putIfAbsent(state, new HashSet<>());
    }

    /**
     * Adds a transition between states.
     * 
     * @param from the source state
     * @param to   the target state
     */
    public void addTransition(S from, S to) {
        if (!states.contains(from) || !states.contains(to)) {
            throw new IllegalArgumentException("Both states must exist");
        }
        transitions.get(from).add(to);
    }

    /**
     * Labels a state with an atomic proposition.
     * 
     * @param state       the state
     * @param proposition the proposition
     */
    public void label(S state, P proposition) {
        if (!states.contains(state)) {
            throw new IllegalArgumentException("State must exist");
        }
        labeling.get(state).add(proposition);
    }

    /**
     * Checks if a proposition holds in a state.
     * 
     * @param state       the state
     * @param proposition the proposition
     * @return true if the proposition holds
     */
    public boolean holds(S state, P proposition) {
        return labeling.getOrDefault(state, Collections.emptySet()).contains(proposition);
    }

    /**
     * AG φ: φ holds on All paths Globally.
     * 
     * @param phi the predicate
     * @return the set of states where AG φ holds
     */
    public Set<S> AG(Predicate<S> phi) {
        Set<S> result = new HashSet<>(states);
        Set<S> prev;

        do {
            prev = new HashSet<>(result);
            result.retainAll(states.stream()
                    .filter(phi::test)
                    .filter(s -> transitions.get(s).stream().allMatch(result::contains))
                    .toList());
        } while (!result.equals(prev));

        return result;
    }

    /**
     * EG φ: φ holds on some path Globally.
     * 
     * @param phi the predicate
     * @return the set of states where EG φ holds
     */
    public Set<S> EG(Predicate<S> phi) {
        Set<S> result = states.stream().filter(phi::test).collect(HashSet::new, Set::add, Set::addAll);
        Set<S> prev;

        do {
            prev = new HashSet<>(result);
            result.retainAll(states.stream()
                    .filter(s -> transitions.get(s).stream().anyMatch(result::contains))
                    .toList());
        } while (!result.equals(prev));

        return result;
    }

    /**
     * EF φ: φ holds on some path Finally.
     * 
     * @param phi the predicate
     * @return the set of states where EF φ holds
     */
    public Set<S> EF(Predicate<S> phi) {
        Set<S> result = states.stream().filter(phi::test).collect(HashSet::new, Set::add, Set::addAll);
        Set<S> prev;

        do {
            prev = new HashSet<>(result);
            for (S state : states) {
                if (transitions.get(state).stream().anyMatch(result::contains)) {
                    result.add(state);
                }
            }
        } while (!result.equals(prev));

        return result;
    }

    /**
     * AF φ: φ holds on All paths Finally.
     * 
     * @param phi the predicate
     * @return the set of states where AF φ holds
     */
    public Set<S> AF(Predicate<S> phi) {
        Set<S> result = states.stream().filter(phi::test).collect(HashSet::new, Set::add, Set::addAll);
        Set<S> prev;

        do {
            prev = new HashSet<>(result);
            for (S state : states) {
                if (!result.contains(state) && !transitions.get(state).isEmpty()
                        && transitions.get(state).stream().allMatch(result::contains)) {
                    result.add(state);
                }
            }
        } while (!result.equals(prev));

        return result;
    }

    /**
     * E[φ U ψ]: φ Until ψ on some path.
     * 
     * @param phi the first predicate
     * @param psi the second predicate
     * @return the set of states where E[φ U ψ] holds
     */
    public Set<S> EU(Predicate<S> phi, Predicate<S> psi) {
        Set<S> result = states.stream().filter(psi::test).collect(HashSet::new, Set::add, Set::addAll);
        Set<S> prev;

        do {
            prev = new HashSet<>(result);
            for (S state : states) {
                if (phi.test(state) && transitions.get(state).stream().anyMatch(result::contains)) {
                    result.add(state);
                }
            }
        } while (!result.equals(prev));

        return result;
    }

    /**
     * Returns all states in the model.
     * 
     * @return the set of all states
     */
    public Set<S> getStates() {
        return new HashSet<>(states);
    }

    @Override
    public String toString() {
        return "CTL(states=" + states.size() + ", transitions=" + countTransitions() + ")";
    }

    private int countTransitions() {
        return transitions.values().stream().mapToInt(Set::size).sum();
    }
}
