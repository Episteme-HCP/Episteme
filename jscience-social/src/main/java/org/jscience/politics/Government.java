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
package org.jscience.politics;

import java.util.*;

/**
 * Represents a government or administration.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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