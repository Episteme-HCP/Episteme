/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.history;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a historical era or period.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Era {

    private final String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String region;
    private final List<Event> keyEvents = new ArrayList<>();

    public Era(String name) {
        this.name = name;
    }

    public Era(String name, LocalDate startDate, LocalDate endDate) {
        this(name);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
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
    public void setStartDate(LocalDate date) {
        this.startDate = date;
    }

    public void setEndDate(LocalDate date) {
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
     * Returns duration in years.
     */
    public int getDurationYears() {
        if (startDate == null || endDate == null)
            return 0;
        return java.time.Period.between(startDate, endDate).getYears();
    }

    @Override
    public String toString() {
        return String.format("Era '%s' (%s)", name,
                startDate != null && endDate != null ? startDate.getYear() + "-" + endDate.getYear() : "dates unknown");
    }

    // Historical eras
    public static Era renaissance() {
        Era e = new Era("Renaissance", LocalDate.of(1400, 1, 1), LocalDate.of(1600, 1, 1));
        e.setRegion("Europe");
        e.setDescription("Cultural movement emphasizing humanism and arts");
        return e;
    }

    public static Era industrialRevolution() {
        Era e = new Era("Industrial Revolution", LocalDate.of(1760, 1, 1), LocalDate.of(1840, 1, 1));
        e.setRegion("Britain, Europe");
        e.setDescription("Transition to machine manufacturing");
        return e;
    }
}
