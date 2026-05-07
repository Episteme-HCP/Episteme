/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.law;

import org.episteme.core.util.Named;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A class representing a systematic collection of laws or regulations 
 * (e.g., Civil Code, Penal Code) applicable in a specific jurisdiction.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Code implements Named {
    
    private String name;
    private String category;
    private Date date;
    private List<Article> articles;

    /**
     * Minimal constructor for the legal code.
     * @param name the name of the code
     */
    public Code(String name) {
        this(name, "General Law", new Date(), new ArrayList<>());
    }

    /**
     * Creates a new Code object.
     *
     * @param name the name of the code (e.g., "Code Civil")
     * @param category the legal category (e.g., "Civil Law")
     * @param date the date of promulgation or publication
     * @param articles the initial list of articles
     * @throws IllegalArgumentException if any argument is null or if the list contains non-Article entities
     */
    public Code(String name, String category, Date date, List<Article> articles) {
        if (name == null || name.isEmpty() || category == null || 
            category.isEmpty() || date == null || articles == null) {
            throw new IllegalArgumentException("Code arguments cannot be null or empty.");
        }

        for (Object article : articles) {
            if (!(article instanceof Article)) {
                throw new IllegalArgumentException("The list must contain only Articles.");
            }
        }

        this.name = name;
        this.category = category;
        this.date = date;
        this.articles = new ArrayList<>(articles);
    }

    /**
     * Returns the name of the legal code.
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the category of the code.
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the publication or effective date of the code.
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the list of articles contained in this code.
     * @return the list of articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Adds an article to the code.
     * @param article the article to add
     * @throws IllegalArgumentException if article is null
     */
    public void addArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Cannot add a null article.");
        }
        this.articles.add(article);
    }

    /**
     * Removes an article from the code.
     * @param article the article to remove
     */
    public void removeArticle(Article article) {
        this.articles.remove(article);
    }

    /** Legacy getter for ID. */
    public String getId() {
        return name;
    }

    /** Retrieves an article by its numbering. */
    public Article getArticle(String num) {
        return articles.stream()
                .filter(a -> a.getNumbering() != null && a.getNumbering().toString().equals(num))
                .findFirst()
                .orElse(null);
    }
}

