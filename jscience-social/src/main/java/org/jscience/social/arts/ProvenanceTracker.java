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

package org.jscience.social.arts;

import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.natural.earth.Place;
import java.util.*;

/**
 * Tracks the provenance (ownership history) of artworks through time.
 */
public final class ProvenanceTracker {

    private ProvenanceTracker() {}

    public enum TransferType {
        CREATION, COMMISSION, SALE, AUCTION, GIFT, INHERITANCE, 
        PLUNDER, RESTITUTION, MUSEUM_ACQUISITION, LOAN
    }

    public record ProvenanceEvent(
        TimeCoordinate date,
        String fromOwner,
        String toOwner,
        TransferType type,
        Place location,
        String notes
    ) {}

    /**
     * Complete provenance record for an artwork.
     */
    public static class ProvenanceRecord {
        private final List<ProvenanceEvent> history = new ArrayList<>();

        public ProvenanceRecord(Artwork artwork) {
            Objects.requireNonNull(artwork);
        }

        public void addEvent(ProvenanceEvent event) {
            history.add(event);
            history.sort((a, b) -> {
                if (a.date() == null) return -1;
                if (b.date() == null) return 1;
                return a.date().compareTo(b.date());
            });
        }

        public List<ProvenanceEvent> getHistory() {
            return Collections.unmodifiableList(history);
        }

        public String getCurrentOwner() {
            return history.isEmpty() ? null : history.get(history.size() - 1).toOwner();
        }

        public List<ProvenanceEvent> getEventsByType(TransferType type) {
            return history.stream().filter(e -> e.type() == type).toList();
        }

        public boolean hasGapInProvenance() {
            // Check for unknown periods
            return history.stream().anyMatch(e -> 
                e.fromOwner() == null || e.fromOwner().equals("Unknown"));
        }

        public List<Place> getLocationHistory() {
            return history.stream()
                .map(ProvenanceEvent::location)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        }
    }

    /**
     * Analyzes provenance for potential issues (looted art, gaps).
     */
    public static List<String> analyzeProvenance(ProvenanceRecord record) {
        List<String> issues = new ArrayList<>();
        
        if (record.hasGapInProvenance()) {
            issues.add("WARNING: Provenance has unknown ownership periods");
        }
        
        List<ProvenanceEvent> plunder = record.getEventsByType(TransferType.PLUNDER);
        if (!plunder.isEmpty()) {
            issues.add("ALERT: Artwork was plundered at least once");
            for (ProvenanceEvent e : plunder) {
                issues.add("  - Plundered circa " + e.date() + " at " + e.location());
            }
        }
        
        // Check for restitution after plunder
        boolean hasRestitution = !record.getEventsByType(TransferType.RESTITUTION).isEmpty();
        if (!plunder.isEmpty() && !hasRestitution) {
            issues.add("NOTE: No restitution recorded after plunder event(s)");
        }
        
        return issues;
    }
}

