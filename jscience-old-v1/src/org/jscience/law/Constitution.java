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

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;


/**
 * A class representing the form, structure, activities, character, and
 * fundamental rules of a given organisation.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Constitution extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private Vector articles; //it is not guarantied that the elements of the vector are ordonned by number

    //must be a vector of Articles
    /**
     * Creates a new Constitution object.
     *
     * @param name DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param articles DOCUMENT ME!
     */
    public Constitution(String name, Date date, Vector articles) {
        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (date != null) &&
                (articles != null)) {
            iterator = articles.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Article;
            }

            if (valid) {
                this.name = name;
                this.date = date;
                this.articles = articles;
            } else {
                throw new IllegalArgumentException(
                    "The Vector can consist only of Articles.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Artwork constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getArticles() {
        return articles;
    }

    //each article should have a corresponding number that goes in a logical progression
    /**
     * DOCUMENT ME!
     *
     * @param article DOCUMENT ME!
     */
    public void addArticle(Article article) {
        this.articles.addElement(article);
    }

    //normally you don't remove articles in a constitution
    /**
     * DOCUMENT ME!
     *
     * @param article DOCUMENT ME!
     */
    public void removeArticle(Article article) {
        this.articles.remove(article);
    }

    //perhaps toString could be implemented
}
