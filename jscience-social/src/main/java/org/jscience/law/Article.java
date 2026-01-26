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

import org.jscience.util.Commented;
import org.jscience.util.Named;
import org.jscience.util.Numbering;
import org.jscience.util.SimpleNumbering;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a specific element or clause of a law, code, or constitution.
 * Articles are the building blocks of legal frameworks.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public class Article implements Named, Commented {
    
    private String name; // rarely used
    private Numbering numbering;
    private String contents;
    private Date date; // date of adoption, publication, or application
    
    private final Map<String, Object> traits = new HashMap<>();

    /**
     * Minimal constructor for legacy support.
     */
    public Article(String name, String contents) {
        this(name, new SimpleNumbering(name), contents, "", new Date());
    }

    /**
     * Creates a new Article object with numbering and contents.
     *
     * @param numbering the numbering/index of the article
     * @param contents the textual content of the article
     * @throws IllegalArgumentException if numbering or contents is null
     */
    public Article(Numbering numbering, String contents) {
        this("", numbering, contents, "", new Date());
    }

    /**
     * Creates a new Article object with numbering, contents, and date.
     *
     * @param numbering the numbering/index of the article
     * @param contents the textual content of the article
     * @param date the date associated with the article
     * @throws IllegalArgumentException if numbering or contents is null
     */
    public Article(Numbering numbering, String contents, Date date) {
        this("", numbering, contents, "", date);
    }

    /**
     * Creates a full Article object.
     *
     * @param name the name of the article
     * @param numbering the numbering/index of the article
     * @param contents the textual content of the article
     * @param comments interpretative comments or notes
     * @param date the date of the article
     * @throws IllegalArgumentException if any argument is null
     */
    public Article(String name, Numbering numbering, String contents,
        String comments, Date date) {
        if (name == null || numbering == null || contents == null || 
            contents.isEmpty() || comments == null || date == null) {
            throw new IllegalArgumentException("Article arguments cannot be null or empty.");
        }
        this.name = name;
        this.numbering = numbering;
        this.contents = contents;
        setComments(comments);
        this.date = date;
    }

    /**
     * Returns the name of the article.
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the numbering or index of the article.
     * @return the numbering
     */
    public Numbering getNumbering() {
        return numbering;
    }

    /**
     * Returns the text contents of the article.
     * @return the contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * Returns the date associated with this article.
     * @return the date
     */
    public Date getDate() {
        return date;
    }
    
    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }
}
