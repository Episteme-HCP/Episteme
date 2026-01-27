package org.jscience.history;


import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jscience.util.persistence.Persistent;

/**
 * A timeline representing a sequence of historical events.
 * Provides analytical methods to filter and explore historical periods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
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
        return events.stream()
                .filter(e -> e.getWhen().toInstant().isBefore(java.time.Instant.EPOCH)) // Simplistic check for BCE
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
