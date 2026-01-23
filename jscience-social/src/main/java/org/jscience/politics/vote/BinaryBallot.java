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
 * A ballot where each option for a given choice can be either selected (true) or not (false).
 * This is suitable for approval voting or simple multiple-choice elections.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class BinaryBallot extends Ballot {

    private static final long serialVersionUID = 1L;
    
    // Choice Title -> (Option Name -> Selected)
    private final Map<String, Map<String, Boolean>> choices;

    /**
     * Creates a new BinaryBallot.
     */
    public BinaryBallot() {
        this.choices = new HashMap<>();
    }

    @Override
    public void addChoice(String title) {
        choices.computeIfAbsent(title, k -> new HashMap<>());
    }

    @Override
    public void addOptionToChoice(String title, String option) {
        Map<String, Boolean> options = choices.computeIfAbsent(title, k -> new HashMap<>());
        options.putIfAbsent(option, false);
    }

    @Override
    public Set<String> getChoices() {
        return choices.keySet();
    }

    @Override
    public Set<String> getOptionsForChoice(String title) {
        Map<String, Boolean> options = choices.get(title);
        return options != null ? options.keySet() : new HashSet<>();
    }

    @Override
    public boolean isOptionSelected(String title, String option) {
        Map<String, Boolean> options = choices.get(title);
        return options != null && options.getOrDefault(option, false);
    }

    /**
     * Sets the selection status for a specific option.
     *
     * @param title    the choice title
     * @param option   the option name
     * @param selected true to select, false to deselect
     */
    public void setSelectionForOption(String title, String option, boolean selected) {
        Map<String, Boolean> options = choices.computeIfAbsent(title, k -> new HashMap<>());
        options.put(option, selected);
    }

    @Override
    public BinaryBallot clone() {
        BinaryBallot copy = new BinaryBallot();
        for (Map.Entry<String, Map<String, Boolean>> entry : choices.entrySet()) {
            Map<String, Boolean> optionsCopy = new HashMap<>(entry.getValue());
            copy.choices.put(entry.getKey(), optionsCopy);
        }
        return copy;
    }
}

