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

package org.jscience.bibliography;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ReferenceManager {

    private final List<Map<String, String>> references = new ArrayList<>();
    private String defaultStyle = "APA";

    /**
     * Adds a reference from BibTeX entry.
     */
    public void addFromBibTeX(String bibtex) {
        Map<String, String> entry = BibTeXParser.parse(bibtex);
        if (entry != null && !entry.isEmpty()) {
            references.add(entry);
        }
    }

    /**
     * Adds a reference manually.
     */
    public void addReference(Map<String, String> entry) {
        references.add(new LinkedHashMap<>(entry));
    }

    /**
     * Gets all references.
     */
    public List<Map<String, String>> getAllReferences() {
        return new ArrayList<>(references);
    }

    /**
     * Finds references by author name.
     */
    public List<Map<String, String>> findByAuthor(String authorPattern) {
        List<Map<String, String>> results = new ArrayList<>();
        for (Map<String, String> ref : references) {
            String author = ref.get("author");
            if (author != null && author.toLowerCase().contains(authorPattern.toLowerCase())) {
                results.add(ref);
            }
        }
        return results;
    }

    /**
     * Finds references by year.
     */
    public List<Map<String, String>> findByYear(String year) {
        List<Map<String, String>> results = new ArrayList<>();
        for (Map<String, String> ref : references) {
            if (year.equals(ref.get("year"))) {
                results.add(ref);
            }
        }
        return results;
    }

    /**
     * Formats all references in the specified style.
     */
    public List<String> formatAll(String style) {
        List<String> formatted = new ArrayList<>();
        for (Map<String, String> ref : references) {
            formatted.add(formatReference(ref, style));
        }
        return formatted;
    }

    /**
     * Formats a single reference.
     */
    public String formatReference(Map<String, String> entry, String style) {
        return switch (style.toUpperCase()) {
            case "APA" -> CitationFormatter.formatAPA(entry);
            case "MLA" -> CitationFormatter.formatMLA(entry);
            case "CHICAGO" -> CitationFormatter.formatChicago(entry);
            default -> CitationFormatter.formatAPA(entry);
        };
    }

    /**
     * Exports all references as BibTeX.
     */
    public String exportAsBibTeX() {
        StringBuilder sb = new StringBuilder();
        for (Map<String, String> ref : references) {
            String type = ref.getOrDefault("_type", "article");
            String key = ref.getOrDefault("_key", "ref" + references.indexOf(ref));
            sb.append(BibTeXParser.generate(type, key, ref));
            sb.append("\n\n");
        }
        return sb.toString();
    }

    /**
     * Gets reference count.
     */
    public int size() {
        return references.size();
    }

    /**
     * Clears all references.
     */
    public void clear() {
        references.clear();
    }

    /**
     * Sets default citation style.
     */
    public void setDefaultStyle(String style) {
        this.defaultStyle = style;
    }

    /**
     * Gets default citation style.
     */
    public String getDefaultStyle() {
        return defaultStyle;
    }

    /**
     * Sorts references alphabetically by author.
     */
    public void sortByAuthor() {
        references.sort((a, b) -> {
            String authorA = a.getOrDefault("author", "");
            String authorB = b.getOrDefault("author", "");
            return authorA.compareToIgnoreCase(authorB);
        });
    }

    /**
     * Sorts references by year (newest first).
     */
    public void sortByYear() {
        references.sort((a, b) -> {
            String yearA = a.getOrDefault("year", "0");
            String yearB = b.getOrDefault("year", "0");
            return yearB.compareTo(yearA);
        });
    }
}


