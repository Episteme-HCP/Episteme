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
 * Context-Free Grammar implementation.
 * <p>
 * A context-free grammar is a formal grammar consisting of:
 * <ul>
 * <li>A set of terminal symbols (the alphabet)</li>
 * <li>A set of non-terminal symbols (variables)</li>
 * <li>A set of production rules</li>
 * <li>A start symbol</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Grammar {

    private final String startSymbol;
    private final Set<String> terminals = new HashSet<>();
    private final Set<String> nonTerminals = new HashSet<>();
    private final Map<String, List<List<String>>> productions = new HashMap<>();

    /**
     * Creates a new grammar with the given start symbol.
     *
     * @param startSymbol the start symbol (non-terminal)
     */
    public Grammar(String startSymbol) {
        this.startSymbol = startSymbol;
        this.nonTerminals.add(startSymbol);
    }

    /**
     * Adds a terminal symbol.
     *
     * @param terminal the terminal symbol
     * @return this grammar for chaining
     */
    public Grammar addTerminal(String terminal) {
        terminals.add(terminal);
        return this;
    }

    /**
     * Adds multiple terminal symbols.
     *
     * @param terminals the terminal symbols
     * @return this grammar for chaining
     */
    public Grammar addTerminals(String... terminals) {
        this.terminals.addAll(Arrays.asList(terminals));
        return this;
    }

    /**
     * Adds a non-terminal symbol.
     *
     * @param nonTerminal the non-terminal symbol
     * @return this grammar for chaining
     */
    public Grammar addNonTerminal(String nonTerminal) {
        nonTerminals.add(nonTerminal);
        return this;
    }

    /**
     * Adds a production rule.
     * <p>
     * Example: addProduction("S", "a", "B") means S → aB
     * </p>
     *
     * @param lhs        left-hand side (non-terminal)
     * @param rhsSymbols right-hand side symbols
     * @return this grammar for chaining
     */
    public Grammar addProduction(String lhs, String... rhsSymbols) {
        nonTerminals.add(lhs);
        productions.computeIfAbsent(lhs, k -> new ArrayList<>())
                .add(Arrays.asList(rhsSymbols));
        return this;
    }

    /**
     * Adds an epsilon (empty) production.
     *
     * @param lhs left-hand side
     * @return this grammar for chaining
     */
    public Grammar addEpsilonProduction(String lhs) {
        nonTerminals.add(lhs);
        productions.computeIfAbsent(lhs, k -> new ArrayList<>())
                .add(Collections.emptyList());
        return this;
    }

    /**
     * Returns the start symbol.
     *
     * @return start symbol
     */
    public String getStartSymbol() {
        return startSymbol;
    }

    /**
     * Returns all terminal symbols.
     *
     * @return unmodifiable set of terminals
     */
    public Set<String> getTerminals() {
        return Collections.unmodifiableSet(terminals);
    }

    /**
     * Returns all non-terminal symbols.
     *
     * @return unmodifiable set of non-terminals
     */
    public Set<String> getNonTerminals() {
        return Collections.unmodifiableSet(nonTerminals);
    }

    /**
     * Returns all productions for a given non-terminal.
     *
     * @param nonTerminal the non-terminal
     * @return list of productions (each production is a list of symbols)
     */
    public List<List<String>> getProductions(String nonTerminal) {
        return productions.getOrDefault(nonTerminal, Collections.emptyList());
    }

    /**
     * Checks if a symbol is a terminal.
     *
     * @param symbol the symbol
     * @return true if terminal
     */
    public boolean isTerminal(String symbol) {
        return terminals.contains(symbol);
    }

    /**
     * Checks if a symbol is a non-terminal.
     *
     * @param symbol the symbol
     * @return true if non-terminal
     */
    public boolean isNonTerminal(String symbol) {
        return nonTerminals.contains(symbol);
    }

    /**
     * Generates a random derivation from the start symbol.
     * <p>
     * Useful for testing and generating sample strings.
     * May not terminate for grammars with left recursion.
     * </p>
     *
     * @param maxSteps maximum derivation steps
     * @return the derived string, or null if max steps exceeded
     */
    public String generateRandom(int maxSteps) {
        Random random = new Random();
        List<String> sententialForm = new ArrayList<>();
        sententialForm.add(startSymbol);

        for (int step = 0; step < maxSteps; step++) {
            // Find first non-terminal
            int ntIndex = -1;
            for (int i = 0; i < sententialForm.size(); i++) {
                if (isNonTerminal(sententialForm.get(i))) {
                    ntIndex = i;
                    break;
                }
            }

            if (ntIndex == -1) {
                // All terminals, done
                return String.join("", sententialForm);
            }

            String nt = sententialForm.get(ntIndex);
            List<List<String>> prods = getProductions(nt);
            if (prods.isEmpty()) {
                return null; // No production for non-terminal
            }

            List<String> chosen = prods.get(random.nextInt(prods.size()));
            sententialForm.remove(ntIndex);
            sententialForm.addAll(ntIndex, chosen);
        }

        return null; // Max steps exceeded
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grammar (start: ").append(startSymbol).append(")\n");
        sb.append("Terminals: ").append(terminals).append("\n");
        sb.append("Non-terminals: ").append(nonTerminals).append("\n");
        sb.append("Productions:\n");
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            for (List<String> rhs : entry.getValue()) {
                sb.append("  ").append(entry.getKey()).append(" → ");
                sb.append(rhs.isEmpty() ? "ε" : String.join(" ", rhs));
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
