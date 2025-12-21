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

import java.util.*;

/**
 * Represents a historical era or period.
 * <p>
 * Uses {@link FuzzyDate} to support dates with varying precision,
 * including approximate dates and BCE periods.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HistoricalEra {

    private final String name;
    private FuzzyDate startDate;
    private FuzzyDate endDate;
    private String description;
    private String region;
    private final List<Event> keyEvents = new ArrayList<>();

    public HistoricalEra(String name) {
        this.name = name;
    }

    public HistoricalEra(String name, FuzzyDate startDate, FuzzyDate endDate) {
        this(name);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public String getName() {
        return name;
    }

    public FuzzyDate getStartDate() {
        return startDate;
    }

    public FuzzyDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public List<Event> getKeyEvents() {
        return Collections.unmodifiableList(keyEvents);
    }

    // Setters
    public void setStartDate(FuzzyDate date) {
        this.startDate = date;
    }

    public void setEndDate(FuzzyDate date) {
        this.endDate = date;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void addKeyEvent(Event event) {
        keyEvents.add(event);
    }

    /**
     * Returns approximate duration in years.
     */
    public int getDurationYears() {
        if (startDate == null || endDate == null)
            return 0;
        Integer startYear = startDate.getYear();
        Integer endYear = endDate.getYear();
        if (startYear == null || endYear == null)
            return 0;

        int start = startDate.isBce() ? -startYear : startYear;
        int end = endDate.isBce() ? -endYear : endYear;
        return end - start;
    }

    @Override
    public String toString() {
        return String.format("Era '%s' (%s to %s)", name,
                startDate != null ? startDate : "unknown",
                endDate != null ? endDate : "unknown");
    }

    // Historical eras with FuzzyDate support
    public static HistoricalEra renaissance() {
        HistoricalEra e = new HistoricalEra("Renaissance",
                FuzzyDate.circa(1400), FuzzyDate.circa(1600));
        e.setRegion("Europe");
        e.setDescription("Cultural movement emphasizing humanism and arts");
        return e;
    }

    public static HistoricalEra industrialRevolution() {
        HistoricalEra e = new HistoricalEra("Industrial Revolution",
                FuzzyDate.circa(1760), FuzzyDate.circa(1840));
        e.setRegion("Britain, Europe");
        e.setDescription("Transition to machine manufacturing");
        return e;
    }

    public static HistoricalEra ancientGreece() {
        HistoricalEra e = new HistoricalEra("Ancient Greece",
                FuzzyDate.circaBce(800), FuzzyDate.circaBce(31));
        e.setRegion("Greece, Mediterranean");
        e.setDescription("Classical Greek civilization");
        return e;
    }

    public static HistoricalEra romanEmpire() {
        HistoricalEra e = new HistoricalEra("Roman Empire",
                FuzzyDate.bce(27), FuzzyDate.of(476));
        e.setRegion("Mediterranean, Europe");
        e.setDescription("Ancient Roman imperial period");
        return e;
    }

    public static HistoricalEra middleAges() {
        HistoricalEra e = new HistoricalEra("Middle Ages",
                FuzzyDate.circa(500), FuzzyDate.circa(1500));
        e.setRegion("Europe");
        e.setDescription("Medieval period in European history");
        return e;
    }
}
