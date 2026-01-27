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

package org.jscience.philosophy;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class for evaluating outcomes of ethical dilemmas using different 
 * philosophical frameworks.
 * 
 * <p> Supports analysis from Utilitarian (maximizing utility/happiness), 
 *     Deontological (adherence to duties and rules), and Virtue Ethics 
 *     (alignment with virtuous character) perspectives.</p>
 *
 * @author <a href="mailto:silvere.martin-michiellot@jscience.org">Silvere Martin-Michiellot</a>
 * @author Gemini AI (Google DeepMind)
 * @version 6.0, July 21, 2014
 */
public final class EthicalFrameworks {


    private EthicalFrameworks() {}

    /**
     * Represents a potential action in an ethical decision-making scenario.
     *
     * @param description   Textual description of the action
     * @param netHappiness  projected utility (happiness/benefit) of the outcome
     * @param violatesDuty  whether the action violates a fundamental moral duty or rule
     * @param isVirtuous    whether the action aligns with virtuous character traits
     */
    public record Action(
        String description,
        double netHappiness,
        boolean violatesDuty,
        boolean isVirtuous
    ) implements Serializable {}

    /**
     * Evaluates actions based on Utilitarianism.
     * Selects the action that maximizes net happiness (utility).
     * 
     * @param actions list of possible actions
     * @return the action with the highest net happiness, or null if list is empty
     */
    public static Action utilitarianChoice(List<Action> actions) {
        if (actions == null) return null;
        return actions.stream()
            .max(Comparator.comparingDouble(Action::netHappiness))
            .orElse(null);
    }

    /**
     * Evaluates actions based on Deontology.
     * Filters out any actions that violate moral duties or rules, regardless of consequences.
     * 
     * @param actions list of possible actions
     * @return list of permissible actions (non-violating)
     */
    public static List<Action> deontologicalPermissible(List<Action> actions) {
        if (actions == null) return List.of();
        return actions.stream()
            .filter(a -> !a.violatesDuty())
            .collect(Collectors.toList());
    }

    /**
     * Evaluates actions based on Virtue Ethics.
     * Selects actions that a virtuous agent would perform (aligned with character virtues).
     * 
     * @param actions list of possible actions
     * @return list of virtuous actions
     */
    public static List<Action> virtueOriented(List<Action> actions) {
        if (actions == null) return List.of();
        return actions.stream()
            .filter(Action::isVirtuous)
            .collect(Collectors.toList());
    }
}
