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

package org.jscience.sociology;

import java.util.*;

/**
 * Represents a religion or faith tradition.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Religion {

    public enum Type {
        MONOTHEISTIC, POLYTHEISTIC, PANTHEISTIC, ATHEISTIC,
        ANIMISTIC, SHAMANISTIC, PHILOSOPHICAL
    }

    private final String name;
    private Type type;
    private long followers;
    private String founder;
    private int foundedYear; // Negative for BCE
    private String originRegion;
    private String holyText;
    private final List<String> beliefs = new ArrayList<>();
    private final List<String> practices = new ArrayList<>();
    private final List<String> holidays = new ArrayList<>();

    public Religion(String name) {
        this.name = name;
    }

    public Religion(String name, Type type) {
        this(name);
        this.type = type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public long getFollowers() {
        return followers;
    }

    public String getFounder() {
        return founder;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public String getOriginRegion() {
        return originRegion;
    }

    public String getHolyText() {
        return holyText;
    }

    public List<String> getBeliefs() {
        return Collections.unmodifiableList(beliefs);
    }

    public List<String> getPractices() {
        return Collections.unmodifiableList(practices);
    }

    public List<String> getHolidays() {
        return Collections.unmodifiableList(holidays);
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public void setFoundedYear(int year) {
        this.foundedYear = year;
    }

    public void setOriginRegion(String region) {
        this.originRegion = region;
    }

    public void setHolyText(String text) {
        this.holyText = text;
    }

    public void addBelief(String belief) {
        beliefs.add(belief);
    }

    public void addPractice(String practice) {
        practices.add(practice);
    }

    public void addHoliday(String holiday) {
        holidays.add(holiday);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d followers)", name, type, followers);
    }

    // Major religions
    public static Religion christianity() {
        Religion r = new Religion("Christianity", Type.MONOTHEISTIC);
        r.setFollowers(2_400_000_000L);
        r.setFounder("Jesus Christ");
        r.setFoundedYear(33);
        r.setHolyText("Bible");
        r.addHoliday("Christmas");
        r.addHoliday("Easter");
        return r;
    }

    public static Religion islam() {
        Religion r = new Religion("Islam", Type.MONOTHEISTIC);
        r.setFollowers(1_900_000_000L);
        r.setFounder("Muhammad");
        r.setFoundedYear(622);
        r.setHolyText("Quran");
        r.addHoliday("Eid al-Fitr");
        r.addHoliday("Eid al-Adha");
        return r;
    }

    public static Religion buddhism() {
        Religion r = new Religion("Buddhism", Type.PHILOSOPHICAL);
        r.setFollowers(500_000_000L);
        r.setFounder("Siddhartha Gautama");
        r.setFoundedYear(-500);
        r.setOriginRegion("India");
        r.addHoliday("Vesak");
        return r;
    }
}


