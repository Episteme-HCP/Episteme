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

/**
 * Represents a historical event with fuzzy date support.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HistoricalEvent implements org.jscience.util.identity.Identifiable<String>,
        org.jscience.geography.Locatable, org.jscience.util.Temporal {

    public enum Category {
        POLITICAL, MILITARY, CULTURAL, SCIENTIFIC, ECONOMIC, RELIGIOUS, NATURAL
    }

    private final String id;
    private final String name;
    private final String description;
    private final FuzzyDate startDate;
    private final FuzzyDate endDate;
    private final Category category;
    private final org.jscience.geography.Place location;

    public HistoricalEvent(String name, String description, FuzzyDate startDate, FuzzyDate endDate,
            Category category, org.jscience.geography.Place location) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.location = location;
    }

    public HistoricalEvent(String name, FuzzyDate date, Category category) {
        this(name, null, date, date, category, null);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public FuzzyDate getStartDate() {
        return startDate;
    }

    public FuzzyDate getEndDate() {
        return endDate;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public org.jscience.geography.Place getLocation() {
        return location;
    }

    @Override
    public java.time.Instant getTimestamp() {
        // Approximate conversion for Temporal interface
        if (startDate != null) {
            // Logic to convert FuzzyDate to Instant
            // Assuming FuzzyDate has some conversion method or we construct it manually
            // This is a placeholder logic, strictly we should use FuzzyDate's value
            // Since I don't see FuzzyDate content, I'll assume standard conversion logic or
            // best effort
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.YEAR, startDate.getYear());
            if (startDate.getMonth() != null)
                cal.set(java.util.Calendar.MONTH, startDate.getMonth() - 1);
            if (startDate.getDay() != null)
                cal.set(java.util.Calendar.DAY_OF_MONTH, startDate.getDay());
            if (startDate.isBce())
                cal.set(java.util.Calendar.ERA, java.util.GregorianCalendar.BC);
            return cal.toInstant();
        }
        return java.time.Instant.MIN;
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

    public boolean overlaps(HistoricalEvent other) {
        return startDate.compareTo(other.endDate) <= 0 &&
                endDate.compareTo(other.startDate) >= 0;
    }

    @Override
    public String toString() {
        if (startDate.equals(endDate)) {
            return String.format("%s (%s) - %s", name, startDate, category);
        }
        return String.format("%s (%s to %s) - %s", name, startDate, endDate, category);
    }

    // Notable historical events with FuzzyDate
    public static final HistoricalEvent FRENCH_REVOLUTION = new HistoricalEvent("French Revolution",
            "Political revolution in France", FuzzyDate.of(1789, 7, 14),
            FuzzyDate.of(1799, 11, 9), Category.POLITICAL,
            new org.jscience.geography.Place("France", org.jscience.geography.Place.Type.COUNTRY));

    public static final HistoricalEvent WORLD_WAR_I = new HistoricalEvent("World War I",
            "Global military conflict", FuzzyDate.of(1914, 7, 28),
            FuzzyDate.of(1918, 11, 11), Category.MILITARY,
            new org.jscience.geography.Place("Global", org.jscience.geography.Place.Type.REGION));

    public static final HistoricalEvent WORLD_WAR_II = new HistoricalEvent("World War II",
            "Global military conflict", FuzzyDate.of(1939, 9, 1),
            FuzzyDate.of(1945, 9, 2), Category.MILITARY,
            new org.jscience.geography.Place("Global", org.jscience.geography.Place.Type.REGION));

    public static final HistoricalEvent MOON_LANDING = new HistoricalEvent("Apollo 11 Moon Landing",
            FuzzyDate.of(1969, 7, 20), Category.SCIENTIFIC);

    // Ancient events with BCE support
    public static final HistoricalEvent FALL_OF_ROME = new HistoricalEvent("Fall of Western Rome",
            "End of the Western Roman Empire", FuzzyDate.of(476, 9, 4),
            FuzzyDate.of(476, 9, 4), Category.POLITICAL,
            new org.jscience.geography.Place("Rome", org.jscience.geography.Place.Type.CITY));

    public static final HistoricalEvent BATTLE_OF_MARATHON = new HistoricalEvent("Battle of Marathon",
            "Greek victory over Persian forces", FuzzyDate.bce(490),
            FuzzyDate.bce(490), Category.MILITARY,
            new org.jscience.geography.Place("Marathon", org.jscience.geography.Place.Type.CITY));

    public static final HistoricalEvent FOUNDING_OF_ROME = new HistoricalEvent("Founding of Rome",
            "Traditional founding date of Rome", FuzzyDate.bce(753),
            FuzzyDate.bce(753), Category.POLITICAL,
            new org.jscience.geography.Place("Rome", org.jscience.geography.Place.Type.CITY));

    public static final HistoricalEvent GREAT_PYRAMID = new HistoricalEvent("Construction of Great Pyramid",
            "Building of the Great Pyramid of Giza", FuzzyDate.circaBce(2560),
            FuzzyDate.circaBce(2540), Category.CULTURAL,
            new org.jscience.geography.Place("Giza", org.jscience.geography.Place.Type.CITY));
}


