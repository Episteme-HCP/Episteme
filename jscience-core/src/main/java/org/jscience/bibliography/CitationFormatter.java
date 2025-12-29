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

import java.util.Map;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CitationFormatter {

    private CitationFormatter() {
    }

    /**
     * Formats citation in APA style.
     * Author, A. A. (Year). Title. Journal, Volume(Issue), Pages.
     */
    public static String formatAPA(Map<String, String> entry) {
        String author = entry.getOrDefault("author", "Unknown");
        String year = entry.getOrDefault("year", "n.d.");
        String title = entry.getOrDefault("title", "Untitled");
        String journal = entry.getOrDefault("journal", "");
        String volume = entry.getOrDefault("volume", "");
        String pages = entry.getOrDefault("pages", "");

        StringBuilder sb = new StringBuilder();
        sb.append(formatAuthorAPA(author));
        sb.append(" (").append(year).append("). ");
        sb.append(title).append(". ");

        if (!journal.isEmpty()) {
            sb.append("*").append(journal).append("*");
            if (!volume.isEmpty()) {
                sb.append(", ").append(volume);
            }
            if (!pages.isEmpty()) {
                sb.append(", ").append(pages);
            }
            sb.append(".");
        }

        return sb.toString();
    }

    /**
     * Formats citation in MLA style.
     * Author. "Title." Journal, vol. Volume, Year, pp. Pages.
     */
    public static String formatMLA(Map<String, String> entry) {
        String author = entry.getOrDefault("author", "Unknown");
        String title = entry.getOrDefault("title", "Untitled");
        String journal = entry.getOrDefault("journal", "");
        String volume = entry.getOrDefault("volume", "");
        String year = entry.getOrDefault("year", "n.d.");
        String pages = entry.getOrDefault("pages", "");

        StringBuilder sb = new StringBuilder();
        sb.append(author).append(". ");
        sb.append("\"").append(title).append(".\" ");

        if (!journal.isEmpty()) {
            sb.append("*").append(journal).append("*");
            if (!volume.isEmpty()) {
                sb.append(", vol. ").append(volume);
            }
            sb.append(", ").append(year);
            if (!pages.isEmpty()) {
                sb.append(", pp. ").append(pages);
            }
            sb.append(".");
        }

        return sb.toString();
    }

    /**
     * Formats citation in Chicago style.
     * Author. "Title." Journal Volume (Year): Pages.
     */
    public static String formatChicago(Map<String, String> entry) {
        String author = entry.getOrDefault("author", "Unknown");
        String title = entry.getOrDefault("title", "Untitled");
        String journal = entry.getOrDefault("journal", "");
        String volume = entry.getOrDefault("volume", "");
        String year = entry.getOrDefault("year", "n.d.");
        String pages = entry.getOrDefault("pages", "");

        StringBuilder sb = new StringBuilder();
        sb.append(author).append(". ");
        sb.append("\"").append(title).append(".\" ");

        if (!journal.isEmpty()) {
            sb.append("*").append(journal).append("* ");
            if (!volume.isEmpty()) {
                sb.append(volume).append(" ");
            }
            sb.append("(").append(year).append(")");
            if (!pages.isEmpty()) {
                sb.append(": ").append(pages);
            }
            sb.append(".");
        }

        return sb.toString();
    }

    private static String formatAuthorAPA(String author) {
        // Simple formatting: First M. Last -> Last, F. M.
        String[] parts = author.split("\\s+");
        if (parts.length >= 2) {
            StringBuilder sb = new StringBuilder();
            sb.append(parts[parts.length - 1]).append(", ");
            for (int i = 0; i < parts.length - 1; i++) {
                sb.append(parts[i].charAt(0)).append(". ");
            }
            return sb.toString().trim();
        }
        return author;
    }
}
