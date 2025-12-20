/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.politics;

import java.util.*;

/**
 * Represents a government or administration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Government {

    public enum Type {
        DEMOCRACY, REPUBLIC, MONARCHY, CONSTITUTIONAL_MONARCHY,
        PARLIAMENTARY, PRESIDENTIAL, FEDERAL, UNITARY,
        THEOCRACY, AUTHORITARIAN, TOTALITARIAN, OLIGARCHY
    }

    private final String countryName;
    private Type type;
    private String headOfState;
    private String headOfGovernment;
    private String rulingParty;
    private int legislatureSeats;
    private int termYears;
    private final List<String> ministries = new ArrayList<>();

    public Government(String countryName, Type type) {
        this.countryName = countryName;
        this.type = type;
    }

    // Getters
    public String getCountryName() {
        return countryName;
    }

    public Type getType() {
        return type;
    }

    public String getHeadOfState() {
        return headOfState;
    }

    public String getHeadOfGovernment() {
        return headOfGovernment;
    }

    public String getRulingParty() {
        return rulingParty;
    }

    public int getLegislatureSeats() {
        return legislatureSeats;
    }

    public int getTermYears() {
        return termYears;
    }

    public List<String> getMinistries() {
        return Collections.unmodifiableList(ministries);
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setHeadOfState(String head) {
        this.headOfState = head;
    }

    public void setHeadOfGovernment(String head) {
        this.headOfGovernment = head;
    }

    public void setRulingParty(String party) {
        this.rulingParty = party;
    }

    public void setLegislatureSeats(int seats) {
        this.legislatureSeats = seats;
    }

    public void setTermYears(int years) {
        this.termYears = years;
    }

    public void addMinistry(String ministry) {
        ministries.add(ministry);
    }

    @Override
    public String toString() {
        return String.format("%s Government (%s)", countryName, type);
    }

    // Example governments
    public static Government usGovernment() {
        Government g = new Government("United States", Type.PRESIDENTIAL);
        g.setHeadOfState("President");
        g.setHeadOfGovernment("President");
        g.setLegislatureSeats(535);
        g.setTermYears(4);
        g.addMinistry("State Department");
        g.addMinistry("Defense Department");
        g.addMinistry("Treasury Department");
        return g;
    }
}
