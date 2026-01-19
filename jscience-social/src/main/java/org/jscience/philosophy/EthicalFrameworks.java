package org.jscience.philosophy;

import java.util.*;

/**
 * Compares outcomes of ethical dilemmas across different frameworks.
 */
public final class EthicalFrameworks {

    private EthicalFrameworks() {}

    public record Action(String description, double netHappiness, boolean violatesDuty, boolean isVirtuous) {}

    /**
     * Utilitarian evaluation: Pick action with highest net happiness.
     */
    public static Action utilitarianChoice(List<Action> actions) {
        return actions.stream().max(Comparator.comparingDouble(Action::netHappiness)).orElse(null);
    }

    /**
     * Deontological evaluation: Filter out actions that violate duty.
     */
    public static List<Action> deontologicalPermissible(List<Action> actions) {
        return actions.stream().filter(a -> !a.violatesDuty()).toList();
    }

    /**
     * Virtue Ethics evaluation: Prioritize virtuous actions.
     */
    public static List<Action> virtueOriented(List<Action> actions) {
        return actions.stream().filter(Action::isVirtuous).toList();
    }
}
