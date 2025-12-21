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
package org.jscience.arts;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a book or publication.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Book {

    public enum Genre {
        FICTION, NON_FICTION, SCIENCE, HISTORY, BIOGRAPHY,
        FANTASY, MYSTERY, ROMANCE, HORROR, SCIENCE_FICTION,
        POETRY, DRAMA, PHILOSOPHY, RELIGION, REFERENCE
    }

    private final String title;
    private final List<String> authors = new ArrayList<>();
    private String isbn;
    private String publisher;
    private LocalDate publicationDate;
    private int pages;
    private Genre genre;
    private String language;
    private String synopsis;

    public Book(String title) {
        this.title = title;
    }

    public Book(String title, String author) {
        this(title);
        this.authors.add(author);
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
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

    // Setters
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublicationDate(LocalDate date) {
        this.publicationDate = date;
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

    public void addAuthor(String author) {
        authors.add(author);
    }

    /**
     * Returns formatted citation.
     */
    public String getCitation() {
        String authorStr = String.join(", ", authors);
        int year = publicationDate != null ? publicationDate.getYear() : 0;
        return String.format("%s (%d). %s. %s.", authorStr, year, title, publisher);
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by %s", title, String.join(", ", authors));
    }

    // Classic books
    public static Book originOfSpecies() {
        Book b = new Book("On the Origin of Species", "Charles Darwin");
        b.setPublicationDate(LocalDate.of(1859, 11, 24));
        b.setGenre(Genre.SCIENCE);
        b.setPages(502);
        return b;
    }

    public static Book principia() {
        Book b = new Book("Principia Mathematica", "Isaac Newton");
        b.setPublicationDate(LocalDate.of(1687, 7, 5));
        b.setGenre(Genre.SCIENCE);
        return b;
    }
}