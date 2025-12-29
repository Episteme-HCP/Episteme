/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.bibliography.loaders;

/**
 * Citation metadata from CrossRef or other bibliography sources.
 * <p>
 * Supports multiple citation formats (APA, MLA, Chicago, BibTeX).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CitationInfo {

    private final String doi;
    private final String title;
    private final String authors;
    private final String journal;
    private final int year;
    private final String volume;
    private final String pages;

    private CitationInfo(Builder builder) {
        this.doi = builder.doi;
        this.title = builder.title;
        this.authors = builder.authors;
        this.journal = builder.journal;
        this.year = builder.year;
        this.volume = builder.volume;
        this.pages = builder.pages;
    }

    public String getDoi() {
        return doi;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getJournal() {
        return journal;
    }

    public int getYear() {
        return year;
    }

    public String getVolume() {
        return volume;
    }

    public String getPages() {
        return pages;
    }

    /**
     * Format citation in APA style.
     * 
     * @return APA formatted citation
     */
    public String formatAPA() {
        StringBuilder sb = new StringBuilder();
        if (authors != null && !authors.isEmpty()) {
            sb.append(authors);
        }
        sb.append(" (").append(year > 0 ? year : "n.d.").append("). ");
        if (title != null)
            sb.append(title).append(". ");
        if (journal != null)
            sb.append("*").append(journal).append("*");
        if (volume != null)
            sb.append(", ").append(volume);
        if (pages != null)
            sb.append(", ").append(pages);
        sb.append(".");
        if (doi != null)
            sb.append(" https://doi.org/").append(doi);
        return sb.toString();
    }

    /**
     * Format citation in MLA style.
     * 
     * @return MLA formatted citation
     */
    public String formatMLA() {
        StringBuilder sb = new StringBuilder();
        if (authors != null && !authors.isEmpty()) {
            sb.append(authors).append(". ");
        }
        if (title != null)
            sb.append("\"").append(title).append(".\" ");
        if (journal != null)
            sb.append("*").append(journal).append("*");
        if (volume != null)
            sb.append(", vol. ").append(volume);
        if (year > 0)
            sb.append(", ").append(year);
        if (pages != null)
            sb.append(", pp. ").append(pages);
        sb.append(".");
        return sb.toString();
    }

    /**
     * Format as BibTeX entry.
     * 
     * @param key citation key
     * @return BibTeX format
     */
    public String formatBibTeX(String key) {
        StringBuilder sb = new StringBuilder();
        sb.append("@article{").append(key).append(",\n");
        if (authors != null)
            sb.append("  author = {").append(authors).append("},\n");
        if (title != null)
            sb.append("  title = {").append(title).append("},\n");
        if (journal != null)
            sb.append("  journal = {").append(journal).append("},\n");
        if (year > 0)
            sb.append("  year = {").append(year).append("},\n");
        if (volume != null)
            sb.append("  volume = {").append(volume).append("},\n");
        if (pages != null)
            sb.append("  pages = {").append(pages).append("},\n");
        if (doi != null)
            sb.append("  doi = {").append(doi).append("},\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return formatAPA();
    }

    public static class Builder {
        private String doi = "";
        private String title = "";
        private String authors = "";
        private String journal = "";
        private int year;
        private String volume = "";
        private String pages = "";

        public Builder doi(String doi) {
            this.doi = doi;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder authors(String authors) {
            this.authors = authors;
            return this;
        }

        public Builder journal(String journal) {
            this.journal = journal;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder volume(String volume) {
            this.volume = volume;
            return this;
        }

        public Builder pages(String pages) {
            this.pages = pages;
            return this;
        }

        public CitationInfo build() {
            return new CitationInfo(this);
        }
    }
}