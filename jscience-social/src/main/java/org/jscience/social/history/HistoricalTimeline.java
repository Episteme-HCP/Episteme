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

package org.jscience.social.history;


import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jscience.core.util.persistence.Persistent;

/**
 * A timeline representing a sequence of historical events.
 * Provides analytical methods to filter and explore historical periods.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class HistoricalTimeline extends Timeline<HistoricalEvent> {

    private static final long serialVersionUID = 2L;

    public HistoricalTimeline(String name) {
        super(name);
    }

    /**
     * Returns a standard timeline of major world history events.
     * @return world history timeline
     */
    public static HistoricalTimeline worldHistory() {
        return HistoryConstants.WORLD_HISTORY;
    }

    @Override
    public HistoricalTimeline addEvent(HistoricalEvent event) {
        super.addEvent(event);
        return this;
    }

    /**
     * Returns events in a specific category.
     * 
     * @param category the category to filter by
     * @return filtered unmodifiable list of events
     */
    public List<HistoricalEvent> getEventsByCategory(EventCategory category) {
        Objects.requireNonNull(category, "Category cannot be null");
        return events.stream()
                .filter(e -> e.getCategory() == category)
                .sorted(Comparator.comparing(Event::getWhen))
                .toList();
    }

    /**
     * Returns events occurring in the BCE (Before Common Era) period.
     *
     * @return unmodifiable list of BCE events
     */
    public List<HistoricalEvent> getBceEvents() {
        // Year 1 AD start
        java.time.Instant yearOneAd = java.time.OffsetDateTime.of(1, 1, 1, 0, 0, 0, 0, java.time.ZoneOffset.UTC).toInstant();
        return events.stream()
                .filter(e -> e.getWhen().toInstant().isBefore(yearOneAd))
                .sorted(Comparator.comparing(Event::getWhen))
                .toList();
    }

    /**
     * Returns the approximate total time span of this timeline in years.
     * 
     * @return time span in years
     */
    public long getTimeSpanYears() {
        Optional<HistoricalEvent> earliest = getEarliestEvent();
        Optional<HistoricalEvent> latest = getLatestEvent();
        if (earliest.isPresent() && latest.isPresent()) {
            return java.time.Duration.between(
                    earliest.get().getWhen().toInstant(),
                    latest.get().getWhen().toInstant()).toDays() / 365;
        }
        return 0;
    }
}

