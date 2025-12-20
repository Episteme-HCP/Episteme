/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.politics;

import java.time.LocalDate;

/**
 * Represents a political party.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Party {

    public enum Ideology {
        FAR_LEFT, LEFT, CENTER_LEFT, CENTER, CENTER_RIGHT, RIGHT, FAR_RIGHT,
        GREEN, LIBERTARIAN, NATIONALIST, SOCIALIST, LIBERAL, CONSERVATIVE
    }

    private final String name;
    private String abbreviation;
    private Ideology ideology;
    private LocalDate foundedDate;
    private String leader;
    private String headquarters;
    private long memberCount;
    private String color;

    public Party(String name, Ideology ideology) {
        this.name = name;
        this.ideology = ideology;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Ideology getIdeology() {
        return ideology;
    }

    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    public String getLeader() {
        return leader;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setAbbreviation(String abbr) {
        this.abbreviation = abbr;
    }

    public void setIdeology(Ideology ideology) {
        this.ideology = ideology;
    }

    public void setFoundedDate(LocalDate date) {
        this.foundedDate = date;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public void setHeadquarters(String hq) {
        this.headquarters = hq;
    }

    public void setMemberCount(long count) {
        this.memberCount = count;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, abbreviation, ideology);
    }
}
