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
