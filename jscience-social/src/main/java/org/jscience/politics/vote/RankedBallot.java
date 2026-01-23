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
package org.jscience.politics.vote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * A ballot where each option for a given choice can be ranked or assigned a score (integer).
 * This is suitable for Borda count, Condorcet methods, or range voting.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class RankedBallot extends Ballot {

    private static final long serialVersionUID = 1L;
    
    // Choice Title -> (Option Name -> Rank/Score)
    private final Map<String, Map<String, Integer>> choices;

    /**
     * Creates a new RankedBallot.
     */
    public RankedBallot() {
        this.choices = new HashMap<>();
    }

    @Override
    public void addChoice(String title) {
        choices.computeIfAbsent(title, k -> new HashMap<>());
    }

    @Override
    public void addOptionToChoice(String title, String option) {
        Map<String, Integer> options = choices.computeIfAbsent(title, k -> new HashMap<>());
        options.putIfAbsent(option, 0);
    }

    @Override
    public Set<String> getChoices() {
        return choices.keySet();
    }

    @Override
    public Set<String> getOptionsForChoice(String title) {
        Map<String, Integer> options = choices.get(title);
        return options != null ? options.keySet() : new HashSet<>();
    }

    @Override
    public boolean isOptionSelected(String title, String option) {
        return getOptionSelection(title, option) > 0;
    }

    /**
     * Returns the rank or score assigned to a specific option.
     *
     * @param title  the choice title
     * @param option the option name
     * @return the integer value assigned, or -1 if option not found
     */
    public int getOptionSelection(String title, String option) {
        Map<String, Integer> options = choices.get(title);
        if (options != null && options.containsKey(option)) {
            return options.get(option);
        }
        return -1;
    }

    /**
     * Sets the rank or score for a specific option.
     *
     * @param title  the choice title
     * @param option the option name
     * @param value  the integer value (rank or score)
     */
    public void setOptionSelection(String title, String option, int value) {
        Map<String, Integer> options = choices.computeIfAbsent(title, k -> new HashMap<>());
        options.put(option, value);
    }

    @Override
    public RankedBallot clone() {
        RankedBallot copy = new RankedBallot();
        for (Map.Entry<String, Map<String, Integer>> entry : choices.entrySet()) {
            Map<String, Integer> optionsCopy = new HashMap<>(entry.getValue());
            copy.choices.put(entry.getKey(), optionsCopy);
        }
        return copy;
    }
}

