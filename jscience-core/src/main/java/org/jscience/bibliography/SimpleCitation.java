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

package org.jscience.bibliography;

import java.io.Serializable;
import java.util.Objects;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
