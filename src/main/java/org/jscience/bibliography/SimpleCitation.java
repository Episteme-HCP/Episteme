package org.jscience.bibliography;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple immutable implementation of Citation.
 */
public class SimpleCitation implements Citation, Serializable {
    private final String key;
    private final String title;
    private final String author;
    private final String year;
    private final String doi;

    public SimpleCitation(String key, String title, String author, String year, String doi) {
        this.key = Objects.requireNonNull(key, "Key cannot be null");
        this.title = title;
        this.author = author;
        this.year = year;
        this.doi = doi;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public String getDOI() {
        return doi;
    }

    @Override
    public String toBibTeX() {
        StringBuilder sb = new StringBuilder();
        sb.append("@misc{").append(key).append(",\n");
        if (author != null)
            sb.append("  author = {").append(author).append("},\n");
        if (title != null)
            sb.append("  title = {").append(title).append("},\n");
        if (year != null)
            sb.append("  year = {").append(year).append("},\n");
        if (doi != null)
            sb.append("  doi = {").append(doi).append("},\n");
        sb.append("}");
        return sb.toString();
    }
}
