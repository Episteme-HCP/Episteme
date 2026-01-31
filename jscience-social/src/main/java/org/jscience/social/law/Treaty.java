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

package org.jscience.social.law;

import org.jscience.social.psychology.social.HumanGroup;
import org.jscience.core.util.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a formal agreement between multiple human groups (nations, organizations, etc.).
 * A treaty consists of participants (groups) and a collection of articles.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Treaty implements Named {

    /** The formal name of the treaty. */
    @Attribute
    private String name;

    /** The date the treaty was signed or ratified. */
    @Attribute
    private Date date;

    /** The groups that are party to this treaty. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<HumanGroup> groups;

    /** The specific clauses or articles of the treaty. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private List<Article> articles;

    /**
     * Creates a new Treaty.
     *
     * @param name     the name (cannot be empty)
     * @param date     the signing date
     * @param groups   the participating groups
     * @param articles the treaty articles
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if name is empty
     */
    public Treaty(String name, Date date, Set<HumanGroup> groups, List<Article> articles) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.groups = new HashSet<>(Objects.requireNonNull(groups, "Groups set cannot be null"));
        this.articles = new ArrayList<>(Objects.requireNonNull(articles, "Articles list cannot be null"));

        // Validate types if they were passed as raw collections in legacy code
        for (Object g : groups) {
            if (!(g instanceof HumanGroup)) throw new IllegalArgumentException("Groups must contain only HumanGroup instances");
        }
        for (Object a : articles) {
            if (!(a instanceof Article)) throw new IllegalArgumentException("Articles must contain only Article instances");
        }
    }

    /**
     * Returns the name of the treaty.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the signing date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns an unmodifiable view of the participating groups.
     *
     * @return the groups
     */
    public Set<HumanGroup> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    /**
     * Adds a group to the treaty.
     *
     * @param group the group to add
     * @throws NullPointerException if group is null
     */
    public void addGroup(HumanGroup group) {
        groups.add(Objects.requireNonNull(group, "HumanGroup cannot be null"));
    }

    /**
     * Removes a group from the treaty.
     *
     * @param group the group to remove
     */
    public void removeGroup(HumanGroup group) {
        this.groups.remove(group);
    }

    /**
     * Returns an unmodifiable view of the treaty articles.
     *
     * @return the articles
     */
    public List<Article> getArticles() {
        return Collections.unmodifiableList(articles);
    }

    /**
     * Adds an article to the treaty.
     *
     * @param article the article to add
     * @throws NullPointerException if article is null
     */
    public void addArticle(Article article) {
        articles.add(Objects.requireNonNull(article, "Article cannot be null"));
    }

    /**
     * Removes an article from the treaty.
     *
     * @param article the article to remove
     */
    public void removeArticle(Article article) {
        this.articles.remove(article);
    }
}

