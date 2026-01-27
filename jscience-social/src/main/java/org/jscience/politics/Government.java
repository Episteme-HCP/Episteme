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

package org.jscience.politics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents the structure and executive configuration of a national government.
 * Groups administrative types, leadership roles, and ministerial departments.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class Government implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String countryName;
    private GovernmentForm type;
    private String headOfState;
    private String headOfGovernment;
    private String rulingParty;
    private int legislatureSeats;
    private int termYears;
    private final List<String> ministries = new ArrayList<>();

    /**
     * Initializes a new Government for a specific country.
     *
     * @param countryName name of the country
     * @param type        initial government archetype
     * @throws NullPointerException if any argument is null
     */
    public Government(String countryName, GovernmentForm type) {
        this.countryName = Objects.requireNonNull(countryName, "Country name cannot be null");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
    }

    public String getCountryName() {
        return countryName;
    }

    public GovernmentForm getType() {
        return type;
    }

    public void setType(GovernmentForm type) {
        this.type = Objects.requireNonNull(type, "Type cannot be null");
    }

    public String getHeadOfState() {
        return headOfState;
    }

    public void setHeadOfState(String head) {
        this.headOfState = head;
    }

    public String getHeadOfGovernment() {
        return headOfGovernment;
    }

    public void setHeadOfGovernment(String head) {
        this.headOfGovernment = head;
    }

    public String getRulingParty() {
        return rulingParty;
    }

    public void setRulingParty(String party) {
        this.rulingParty = party;
    }

    public int getLegislatureSeats() {
        return legislatureSeats;
    }

    public void setLegislatureSeats(int seats) {
        this.legislatureSeats = seats;
    }

    public int getTermYears() {
        return termYears;
    }

    public void setTermYears(int years) {
        this.termYears = years;
    }

    /**
     * Returns an unmodifiable list of government ministries or departments.
     * @return list of ministry names
     */
    public List<String> getMinistries() {
        return Collections.unmodifiableList(ministries);
    }

    /**
     * Registers a new ministry or executive department.
     * @param ministry the name of the department
     */
    public void addMinistry(String ministry) {
        if (ministry != null) {
            ministries.add(ministry);
        }
    }

    @Override
    public String toString() {
        return String.format("%s Government (%s)", countryName, type);
    }

    /**
     * Factory method for a standard US presidential government structure.
     * @return US Government template
     */
    public static Government usGovernment() {
        Government g = new Government("United States", GovernmentForm.PRESIDENTIAL_SYSTEM);
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
