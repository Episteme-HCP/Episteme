/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import java.time.LocalDate;
import java.util.List;

public class HistoryDomainTest {

    @Test
    public void testEventComparisonAndTimeline() {
        Timeline worldHistory = new Timeline("World History");

        LocalDate d1 = LocalDate.of(1900, 1, 1);
        LocalDate d2 = LocalDate.of(1950, 1, 1);
        LocalDate d3 = LocalDate.of(2000, 1, 1);

        Event e3 = new Event("Event 3", d3, Event.Category.CULTURAL);
        Event e1 = new Event("Event 1", d1, Event.Category.POLITICAL);
        Event e2 = new Event("Event 2", d2, Event.Category.MILITARY);

        // Add out of order
        worldHistory.addEvent(e3);
        worldHistory.addEvent(e1);
        worldHistory.addEvent(e2);

        List<Event> events = worldHistory.getEvents();
        assertEquals(3, events.size());
        assertEquals(e1, events.get(0), "Events should be sorted chronologically");
        assertEquals(e2, events.get(1));
        assertEquals(e3, events.get(2));

        // Test filtering
        List<Event> range = worldHistory.getEventsBetween(
                LocalDate.of(1920, 1, 1),
                LocalDate.of(1980, 1, 1));
        assertEquals(1, range.size());
        assertEquals(e2, range.get(0));
    }
}
