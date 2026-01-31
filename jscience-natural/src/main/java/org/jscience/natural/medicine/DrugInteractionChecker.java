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

package org.jscience.natural.medicine;

import java.util.*;

/**
 * Utility for checking potential drug-drug interactions.
 * */
public final class DrugInteractionChecker {

    private DrugInteractionChecker() {}

    /** Severity levels for drug interactions.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Severity {
        CONTRAINDICATED, MAJOR, MODERATE, MINOR, UNKNOWN
    }

    /** Represents a specific interaction between two drugs. */
    public record InteractionAlert(Medication drugA, Medication drugB, Severity severity, String reason) {
        @Override
        public String toString() {
            return String.format("[%s] Interaction between %s and %s: %s", severity, drugA.getName(), drugB.getName(), reason);
        }
    }

    // This would typically be populated from a database (e.g., RxNorm, DrugBank)
    private static final Map<String, List<String>> MAJOR_INTERACTIONS = Map.of(
        "Warfarin", List.of("Aspirin", "Ibuprofen"),
        "Aspirin", List.of("Warfarin"),
        "Sildenafil", List.of("Nitroglycerin"),
        "Nitroglycerin", List.of("Sildenafil")
    );

    /**
     * Checks for potential interactions within a list of medications.
     * 
     * @param medications the list of medications to check
     * @return a list of identified interaction alerts
     */
    public static List<InteractionAlert> checkInteractions(Collection<Medication> medications) {
        List<InteractionAlert> alerts = new ArrayList<>();
        List<Medication> medList = new ArrayList<>(medications);

        for (int i = 0; i < medList.size(); i++) {
            for (int j = i + 1; j < medList.size(); j++) {
                Medication m1 = medList.get(i);
                Medication m2 = medList.get(j);
                
                if (isInteracting(m1, m2)) {
                    alerts.add(new InteractionAlert(m1, m2, Severity.MAJOR, "Clinical danger reported"));
                }
            }
        }
        return alerts;
    }

    private static boolean isInteracting(Medication m1, Medication m2) {
        return MAJOR_INTERACTIONS.getOrDefault(m1.getName(), Collections.emptyList()).contains(m2.getName()) ||
               MAJOR_INTERACTIONS.getOrDefault(m1.getGenericName(), Collections.emptyList()).contains(m2.getGenericName());
    }
}

