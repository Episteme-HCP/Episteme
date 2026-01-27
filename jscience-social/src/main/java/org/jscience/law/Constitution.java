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

import org.jscience.util.Named;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A class representing the fundamental rules, form, structure, and activities 
 * of a given organization or nation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Constitution implements Named {
    
    private String name;
    private Date date;
    private List<Article> articles;

    /**
     * Creates a new Constitution object.
     *
     * @param name the name of the constitution
     * @param date the date of adoption or proclamation
     * @param articles the set of articles defining the constitution
     * @throws IllegalArgumentException if any argument is null or if the list contains non-Article entities
     */
    public Constitution(String name, Date date, List<Article> articles) {
        if (name == null || name.isEmpty() || date == null || articles == null) {
            throw new IllegalArgumentException("Constitution arguments cannot be null or empty.");
        }

        for (Object article : articles) {
            if (!(article instanceof Article)) {
                throw new IllegalArgumentException("The list must contain only Articles.");
            }
        }

        this.name = name;
        this.date = date;
        this.articles = new ArrayList<>(articles);
    }

    /**
     * Returns the name of the organization or constitution.
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the date when the constitution was established.
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the list of articles in the constitution.
     * @return the list of articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Adds an article to the constitution.
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
     * Removes an article from the constitution. 
     * Note: Articles are rarely removed from a constitution; usually, they are amended.
     * @param article the article to remove
     */
    public void removeArticle(Article article) {
        this.articles.remove(article);
    }
}
