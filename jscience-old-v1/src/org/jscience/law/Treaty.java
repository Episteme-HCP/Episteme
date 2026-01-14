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

import org.jscience.psychology.social.HumanGroup;

import org.jscience.util.Named;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing a Set of agreements between two or more parties.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//also named declaration
public class Treaty extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private Set groups; //the set of HumanGroups

    /** DOCUMENT ME! */
    private Vector articles; //it is not guarantied that the elements of the vector are ordonned by number

    //must be a vector of Articles
    /**
     * Creates a new Treaty object.
     *
     * @param name DOCUMENT ME!
     * @param date DOCUMENT ME!
     * @param groups DOCUMENT ME!
     * @param articles DOCUMENT ME!
     */
    public Treaty(String name, Date date, Set groups, Vector articles) {
        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (date != null) &&
                (groups != null) && (articles != null)) {
            iterator = articles.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Article;
            }

            if (valid) {
                iterator = groups.iterator();

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof HumanGroup;
                }

                if (valid) {
                    this.name = name;
                    this.date = date;
                    this.groups = groups;
                    this.articles = articles;
                } else {
                    throw new IllegalArgumentException(
                        "The Set should contain only HumanGroups.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The Vector can consist only of Articles.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Treaty constructor can't have null arguments (and name can't be empty).");
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
    public Set getGroups() {
        return groups;
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addGroup(HumanGroup group) {
        if (group != null) {
            this.groups.add(group);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null HumanGroup.");
        }
    }

    //this normally does not happen
    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    public void removeGroup(HumanGroup group) {
        this.groups.remove(group);
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
        if (article != null) {
            this.articles.add(article);
        } else {
            throw new IllegalArgumentException("You can't add a null Article.");
        }
    }

    //be cautious when removing an article as there still may be some other articles refering to this article
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
