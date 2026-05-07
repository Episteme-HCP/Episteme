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

package org.episteme.natural.biology.genealogy;

import java.util.*;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;

/**
 * Analyzes complex kinship networks, dynastic alliances, and house interactions.
 * Provides tools for quantifying relationships and identifying inter-dynastic links.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class GenealogyNetwork {

    private GenealogyNetwork() {
        // Prevent instantiation
    }

    /**
     * Data record representing a historical person and their dynastic house.
     */
    @Persistent
    public record Person(
        @Attribute String id,
        @Attribute String house
    ) implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        public Person {
            Objects.requireNonNull(id, "ID cannot be null");
        }
    }

    /**
     * Represents a directed or undirected relation between two individuals.
     */
    @Persistent
    public record Relation(
        @Attribute String p1,
        @Attribute String p2,
        @Attribute String type // "PARENT", "SPOUSE", etc.
    ) implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        public Relation {
            Objects.requireNonNull(p1, "Person 1 cannot be null");
            Objects.requireNonNull(p2, "Person 2 cannot be null");
            Objects.requireNonNull(type, "Relation type cannot be null");
        }
    }

    /**
     * Calculates the coefficient of relationship between two people based on known nodes.
     * Currently a placeholder for advanced graph-based genealogical algorithms.
     * 
     * @param p1 unique ID of first person
     * @param p2 unique ID of second person
     * @param relations list of all known relations
     * @return relationship coefficient
     */
    public static double coefficientOfRelationship(String p1, String p2, List<Relation> relations) {
        Objects.requireNonNull(p1, "Person 1 cannot be null");
        Objects.requireNonNull(p2, "Person 2 cannot be null");
        Objects.requireNonNull(relations, "Relations list cannot be null");
        // Logic for complex graph traversal to be implemented
        return 0.0;
    }

    /**
     * Identifies inter-house alliances formed through marriage relations.
     * 
     * @param relations list of all relations
     * @param personToHouse mapping from person ID to house name
     * @return map of alliance (house-house string) to frequency
     * @throws NullPointerException if any argument is null
     */
    public static Map<String, Integer> identifyAlliances(List<Relation> relations, 
            Map<String, String> personToHouse) {
        Objects.requireNonNull(relations, "Relations list cannot be null");
        Objects.requireNonNull(personToHouse, "Person-to-house map cannot be null");
        
        Map<String, Integer> alliances = new HashMap<>();
        for (Relation r : relations) {
            if ("SPOUSE".equalsIgnoreCase(r.type())) {
                String h1 = personToHouse.get(r.p1());
                String h2 = personToHouse.get(r.p2());
                if (h1 != null && h2 != null && !h1.equals(h2)) {
                    String key = h1.compareTo(h2) < 0 ? h1 + "-" + h2 : h2 + "-" + h1;
                    alliances.put(key, alliances.getOrDefault(key, 0) + 1);
                }
            }
        }
        return alliances;
    }
}

