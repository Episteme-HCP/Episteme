package org.jscience.arts;

import org.jscience.history.time.UncertainDate;
import java.util.*;

/**
 * Represents a book or publication.
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
    private UncertainDate publicationDate;
    private int pages;
    private Genre genre;
    private String language;
    private String synopsis;

    public Book(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
    }

    public Book(String title, String author) {
        this(title);
        if (author != null) {
            this.authors.add(author);
        }
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

    public UncertainDate getPublicationDate() {
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

    public void setPublicationDate(UncertainDate date) {
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
        if (author != null) {
            authors.add(author);
        }
    }

    public void addAuthor(Artist artist) {
        if (artist != null) {
            authors.add(artist.getName());
        }
    }

    /**
     * Returns formatted citation.
     */
    public String getCitation() {
        String authorStr = authors.isEmpty() ? "Unknown Author" : String.join(", ", authors);
        String yearStr = publicationDate != null ? publicationDate.toString() : "n.d.";
        return String.format("%s (%s). %s. %s.", authorStr, yearStr, title, publisher != null ? publisher : "n.p.");
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by %s", title, authors.isEmpty() ? "Unknown Author" : String.join(", ", authors));
    }
}


