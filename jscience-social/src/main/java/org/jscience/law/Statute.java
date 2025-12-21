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
package org.jscience.law;

/**
 * Represents a legal statute or law.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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