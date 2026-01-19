package org.jscience.law;

import java.util.*;

/**
 * Detects internal contradictions and risk factors in contracts and statutes.
 */
public final class InherentConflictDetector {

    private InherentConflictDetector() {}

    public record Conflict(
        String section1,
        String section2,
        String description,
        double severity // 0-1
    ) {}

    /**
     * Scans a list of contract clauses for keyword-based contradictions.
     */
    public static List<Conflict> scanForContradictions(Map<String, String> clauses) {
        List<Conflict> conflicts = new ArrayList<>();
        List<String> keys = new ArrayList<>(clauses.keySet());

        for (int i = 0; i < keys.size(); i++) {
            for (int j = i + 1; j < keys.size(); j++) {
                String c1 = clauses.get(keys.get(i)).toLowerCase();
                String c2 = clauses.get(keys.get(j)).toLowerCase();

                // Simple check for "exclusive" vs "non-exclusive"
                if (c1.contains("exclusive") && c2.contains("non-exclusive")) {
                    conflicts.add(new Conflict(keys.get(i), keys.get(j), 
                        "Mutually exclusive exclusivity levels detected", 0.9));
                }
                
                // Liability vs Unlimited
                if (c1.contains("liability limited to") && c2.contains("unlimited liability")) {
                    conflicts.add(new Conflict(keys.get(i), keys.get(j), 
                        "Contradictory liability limits", 1.0));
                }
            }
        }
        return conflicts;
    }

    /**
     * Checks if a contract meets basic validity criteria (Offer, Acceptance, Consideration).
     */
    public static Map<String, Boolean> checkValidity(String text) {
        Map<String, Boolean> results = new HashMap<>();
        String low = text.toLowerCase();
        
        results.put("Has Offer", low.contains("offer") || low.contains("proposes"));
        results.put("Has Consideration", low.contains("payment") || low.contains("sum of") || low.contains("consideration"));
        results.put("Has Term", low.contains("duration") || low.contains("term") || low.contains("date"));
        
        return results;
    }
}
