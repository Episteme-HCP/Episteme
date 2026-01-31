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

package org.jscience.social.arts;

import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents a book or publication within the literary arts.
 * Captures bibliographic data according to standard library metadata formats.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Book extends Artwork {

    private static final long serialVersionUID = 3L;

    /**
     * Litery and publication genres.
     */
    public enum Genre {
        FICTION, NON_FICTION, SCIENCE, HISTORY, BIOGRAPHY,
        FANTASY, MYSTERY, ROMANCE, HORROR, SCIENCE_FICTION,
        POETRY, DRAMA, PHILOSOPHY, RELIGION, REFERENCE
    }

    @Attribute
    private String isbn;
    @Attribute
    private String publisher;
    @Attribute
    private int pages;
    @Attribute
    private Genre genre;
    @Attribute
    private String language;
    @Attribute
    private String synopsis;

    /**
     * Creates a new Book with just a title.
     * @param title the title of the book
     */
    public Book(String title) {
        super(title, "", null, null, ArtForm.LITERATURE);
    }

    /**
     * Creates a new Book with title and author.
     * @param title the title
     * @param author the author
     */
    public Book(String title, String author) {
        this(title);
        if (author != null) {
            addAuthor(author);
        }
    }

    /**
     * Creates a new Book with basic bibliographic info.
     * @param title the title
     * @param isbn the ISBN
     * @param publicationDate the date of publication
     */
    public Book(String title, String isbn, TimeCoordinate publicationDate) {
        super(title, "", publicationDate, null, ArtForm.LITERATURE);
        this.isbn = isbn;
    }

    public String getTitle() {
        return getName();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPages() {
        return pages;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * Returns a formatted academic citation in a simplified APA/MLA style.
     * @return citation string
     */
    public String getCitation() {
        String authorStr = getAuthors().isEmpty() ? "Unknown Author" : String.join(", ", getAuthors());
        String yearStr = getProductionDate() != null ? getProductionDate().toString() : "n.d.";
        return String.format("%s (%s). %s. %s.", authorStr, yearStr, getTitle(), publisher != null ? publisher : "n.p.");
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by %s", getTitle(), getAuthors().isEmpty() ? "Unknown Author" : String.join(", ", getAuthors()));
    }
}

