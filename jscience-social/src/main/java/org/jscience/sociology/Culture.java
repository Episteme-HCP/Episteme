/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sociology;

import java.util.*;

/**
 * Represents a culture or cultural group.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Culture {

    private final String name;
    private String description;
    private String region;
    private String primaryLanguage;
    private final List<String> traditions = new ArrayList<>();
    private final List<String> values = new ArrayList<>();
    private final List<String> taboos = new ArrayList<>();
    private String religion;

    public Culture(String name) {
        this.name = name;
    }

    public Culture(String name, String region) {
        this(name);
        this.region = region;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public String getReligion() {
        return religion;
    }

    public List<String> getTraditions() {
        return Collections.unmodifiableList(traditions);
    }

    public List<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    public List<String> getTaboos() {
        return Collections.unmodifiableList(taboos);
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setPrimaryLanguage(String language) {
        this.primaryLanguage = language;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public void addTradition(String tradition) {
        traditions.add(tradition);
    }

    public void addValue(String value) {
        values.add(value);
    }

    public void addTaboo(String taboo) {
        taboos.add(taboo);
    }

    @Override
    public String toString() {
        return String.format("Culture '%s' (%s)", name, region);
    }
}
