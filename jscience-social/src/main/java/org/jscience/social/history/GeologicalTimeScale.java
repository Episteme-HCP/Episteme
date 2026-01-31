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


import java.util.List;
import org.jscience.social.history.time.FuzzyTimePoint;
import org.jscience.core.util.persistence.Persistent;

/**
 * Official divisions of geological time according to the International Commission on Stratigraphy (ICS).
 * Rewritten as a timeline of GeologicalEra events.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class GeologicalTimeScale extends Timeline<Event> {

    private static final long serialVersionUID = 2L;

    private static final GeologicalTimeScale INSTANCE = new GeologicalTimeScale();

    private GeologicalTimeScale() {
        super("Geological Time Scale");
        addEvent(HistoryConstants.HADEAN);
        addEvent(HistoryConstants.ARCHEAN);
        addEvent(HistoryConstants.PROTEROZOIC);
        addEvent(HistoryConstants.PHANEROZOIC);
        addEvent(HistoryConstants.PALEOZOIC);
        addEvent(HistoryConstants.MESOZOIC);
        addEvent(HistoryConstants.CENOZOIC);
        addEvent(HistoryConstants.PALEOGENE);
        addEvent(HistoryConstants.NEOGENE);
        addEvent(HistoryConstants.QUATERNARY);
        addEvent(HistoryConstants.PLEISTOCENE);
        addEvent(HistoryConstants.HOLOCENE);
    }

    public static GeologicalTimeScale getInstance() {
        return INSTANCE;
    }

    /**
     * Finds the era(s) covering a specific time.
     * 
     * @param point the time coordinate
     * @return list of eras
     */
    public List<GeologicalEra> findEras(FuzzyTimePoint point) {
        return getEvents().stream()
                .filter(e -> e instanceof GeologicalEra)
                .map(e -> (GeologicalEra) e)
                .filter(e -> e.getWhen().compareTo(point) < 0) // Simplistic check
                .toList();
    }
}

