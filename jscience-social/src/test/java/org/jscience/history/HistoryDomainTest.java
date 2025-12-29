/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
