package org.jscience.bibliography;

import java.util.Map;

/**
 * Citation formatter supporting APA, MLA, and Chicago styles.
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
