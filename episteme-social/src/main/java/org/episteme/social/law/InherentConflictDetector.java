/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.law;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Detects internal contradictions and risk factors in contracts and statutes.
 * Uses keyword-based analysis to identify potential legal conflicts.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 */
public final class InherentConflictDetector {

    private InherentConflictDetector() {
        // Utility class
    }

    /**
     * Represents a detected conflict between two sections of a legal document.
     */
    public record Conflict(
        String section1,
        String section2,
        String description,
        double severity // 0.0 to 1.0
    ) {}

    /**
     * Scans a map of legal clauses for keyword-based contradictions.
     *
     * @param clauses a map where keys are section identifiers and values are clause texts
     * @return a list of detected conflicts
     */
    public static List<Conflict> scanForContradictions(Map<String, String> clauses) {
        if (clauses == null) {
            return List.of();
        }
        
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
     * Checks if a contract text meets basic legal validity criteria using simple heuristics.
     * Criteria include Offer, Consideration, and Term.
     *
     * @param text the contract text
     * @return a map showing which criteria are present
     */
    public static Map<String, Boolean> checkValidity(String text) {
        if (text == null) {
            return Map.of();
        }
        
        Map<String, Boolean> results = new HashMap<>();
        String low = text.toLowerCase();
        
        results.put("Has Offer", low.contains("offer") || low.contains("proposes"));
        results.put("Has Consideration", low.contains("payment") || low.contains("sum of") || low.contains("consideration"));
        results.put("Has Term", low.contains("duration") || low.contains("term") || low.contains("date"));
        
        return results;
    }
}

