package org.jscience.medicine;

import java.util.*;

/**
 * Checks for potential drug-drug interactions.
 */
public final class DrugInteractionChecker {

    private DrugInteractionChecker() {}

    private static final Map<String, List<String>> INTERACTIONS = Map.of(
        "Warfarin", List.of("Aspirin", "Ibuprofen"),
        "Aspirin", List.of("Warfarin"),
        "Sildenafil", List.of("Nitroglycerin")
    );

    /**
     * Returns a list of potential interactions for a set of medications.
     */
    public static List<String> check(List<String> meds) {
        List<String> alerts = new ArrayList<>();
        for (int i = 0; i < meds.size(); i++) {
            for (int j = i + 1; j < meds.size(); j++) {
                String m1 = meds.get(i);
                String m2 = meds.get(j);
                if (INTERACTIONS.getOrDefault(m1, Collections.emptyList()).contains(m2)) {
                    alerts.add("ALERT: Interaction between " + m1 + " and " + m2);
                }
            }
        }
        return alerts;
    }
}
