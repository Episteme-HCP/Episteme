/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.law;

/**
 * Represents a legal statute or law.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Statute {

    public enum Type {
        CONSTITUTION, FEDERAL_LAW, STATE_LAW, REGULATION, ORDINANCE,
        TREATY, DIRECTIVE, DECREE
    }

    public enum Status {
        PROPOSED, ENACTED, AMENDED, REPEALED
    }

    private final String code;
    private final String title;
    private final Type type;
    private final String jurisdiction;
    private final int yearEnacted;
    private final Status status;

    public Statute(String code, String title, Type type, String jurisdiction,
            int yearEnacted, Status status) {
        this.code = code;
        this.title = title;
        this.type = type;
        this.jurisdiction = jurisdiction;
        this.yearEnacted = yearEnacted;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public Type getType() {
        return type;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public int getYearEnacted() {
        return yearEnacted;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isActive() {
        return status == Status.ENACTED || status == Status.AMENDED;
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s, %d) - %s", code, title, type, yearEnacted, status);
    }
}
