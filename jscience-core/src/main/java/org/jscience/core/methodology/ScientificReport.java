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

package org.jscience.core.methodology;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jscience.core.measure.Quantity;
import org.jscience.core.bibliography.Citation;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents a formal scientific or technical report.
 * <p>
 * This class serves as a comprehensive container for scientific findings,
 * methodologies, and results. It supports persistence, multiple authors,
 * quantitative data storage, and bibliographic references.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class ScientificReport implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L; // Updated version

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private String title;

    @Attribute
    private final List<String> authors;

    @Attribute
    private LocalDate publicationDate;

    @Attribute
    private String abstractText;

    @Attribute
    private final List<String> keywords;

    @Attribute
    private final Map<String, Quantity<?>> quantitativeResults;

    @Attribute
    private final List<Citation> references;
    
    // Additional structured content
    @Attribute
    private final Map<String, String> sections;

    /**
     * Creates a new Scientific Report with a generated ID.
     * 
     * @param title The title of the report.
     */
    public ScientificReport(String title) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(Objects.requireNonNull(title, "Title cannot be null"));
        this.authors = new ArrayList<>();
        this.keywords = new ArrayList<>();
        this.quantitativeResults = new HashMap<>();
        this.references = new ArrayList<>();
        this.sections = new HashMap<>();
        this.publicationDate = LocalDate.now();
    }
    
    /**
     * Creates a new Scientific Report with specific ID (reconstitution).
     */
    public ScientificReport(Identification id, String title) {
        this.id = Objects.requireNonNull(id);
        this.title = title;
        this.authors = new ArrayList<>();
        this.keywords = new ArrayList<>();
        this.quantitativeResults = new HashMap<>();
        this.references = new ArrayList<>();
        this.sections = new HashMap<>();
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public Identification getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    // Using trait pattern for name to satisfy typical Named interface without explicitly implementing if not required
    public String getName() {
        return title;
    }
    
    public void setName(String name) {
        this.title = name;
    }

    public List<String> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(String authorName) {
        if (authorName != null && !authorName.isBlank()) {
            authors.add(authorName);
        }
    }
    
    public void setAuthors(List<String> authors) {
        this.authors.clear();
        if (authors != null) {
            this.authors.addAll(authors);
        }
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public List<String> getKeywords() {
        return Collections.unmodifiableList(keywords);
    }

    public void addKeyword(String keyword) {
        if (keyword != null) {
            keywords.add(keyword);
        }
    }

    /**
     * Stores a quantitative result.
     * @param key Identifier for the result (e.g., "Mean Velocity")
     * @param quantity The measured quantity
     */
    public void addResult(String key, Quantity<?> quantity) {
        if (key != null && quantity != null) {
            quantitativeResults.put(key, quantity);
        }
    }

    public Map<String, Quantity<?>> getQuantitativeResults() {
        return Collections.unmodifiableMap(quantitativeResults);
    }

    public void addReference(Citation citation) {
        if (citation != null) {
            references.add(citation);
        }
    }

    public List<Citation> getReferences() {
        return Collections.unmodifiableList(references);
    }
    
    public void addSection(String title, String content) {
        if (title != null) {
            sections.put(title, content);
        }
    }
    
    public Map<String, String> getSections() {
        return Collections.unmodifiableMap(sections);
    }

    @Override
    public String toString() {
        return String.format("Report: %s (%s) - %d Authors", title, 
            publicationDate != null ? publicationDate.format(DateTimeFormatter.ISO_DATE) : "Undated",
            authors.size());
    }
}

