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

package org.jscience.law;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a legal statute, law, or regulation.
 * <p>
 * A statute is a formal written enactment of a legislative authority.
 * This class provides a structured representation including hierarchical elements like articles.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @version 2.0 (Modernized)
 */
public class Statute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Types of legal acts.
     */
    public enum Type {
        CONSTITUTION, FEDERAL_LAW, STATE_LAW, REGULATION, ORDINANCE,
        TREATY, DIRECTIVE, DECREE, ACT
    }

    /**
     * Life cycle status of a legal act.
     */
    public enum Status {
        PROPOSED, ENACTED, AMENDED, REPEALED
    }

    private final String code;
    private final String title;
    private final Type type;
    private final String jurisdiction;
    private final int yearEnacted;
    private Status status;
    
    /** The hierarchical components (articles/clauses) of the statute. */
    private final List<Article> articles = new ArrayList<>();

    public Statute(String code, String title, Type type, String jurisdiction,
            int yearEnacted, Status status) {
        this.code = Objects.requireNonNull(code);
        this.title = Objects.requireNonNull(title);
        this.type = type;
        this.jurisdiction = jurisdiction;
        this.yearEnacted = yearEnacted;
        this.status = status;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public Type getType() { return type; }
    public String getJurisdiction() { return jurisdiction; }
    public int getYearEnacted() { return yearEnacted; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Article> getArticles() {
        return Collections.unmodifiableList(articles);
    }

    public void addArticle(Article article) {
        this.articles.add(Objects.requireNonNull(article));
    }

    public boolean isActive() {
        return status == Status.ENACTED || status == Status.AMENDED;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s, %d) - %s", code, title, type, yearEnacted, status);
    }
}
