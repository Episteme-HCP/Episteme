package org.jscience.history;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class HistoryDomainTest {

    @Test
    public void testTimeline() {
        Timeline timeline = new Timeline("World History");
        timeline.addEvent(HistoricalEvent.FRENCH_REVOLUTION);
        timeline.addEvent(HistoricalEvent.WORLD_WAR_I);
        timeline.addEvent(HistoricalEvent.MOON_LANDING);

        assertEquals(3, timeline.getEvents().size());

        Optional<HistoricalEvent> earliest = timeline.getEarliestEvent();
        assertTrue(earliest.isPresent());
        assertEquals("French Revolution", earliest.get().getName());

        Optional<HistoricalEvent> latest = timeline.getLatestEvent();
        assertTrue(latest.isPresent());
        assertEquals("Apollo 11 Moon Landing", latest.get().getName());
    }

    @Test
    public void testEventsBetween() {
        Timeline timeline = Timeline.worldHistory();
        // 1900 to 1950
        List<HistoricalEvent> events = timeline.getEventsBetween(
                FuzzyDate.of(1900, 1, 1),
                FuzzyDate.of(1950, 12, 31));

        // Should include WWI and WWII
        assertTrue(events.stream().anyMatch(e -> e.getName().equals("World War I")));
        assertTrue(events.stream().anyMatch(e -> e.getName().equals("World War II")));
        assertFalse(events.stream().anyMatch(e -> e.getName().equals("French Revolution")));
    }
}
