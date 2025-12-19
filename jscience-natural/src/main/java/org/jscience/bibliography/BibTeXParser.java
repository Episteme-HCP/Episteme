package org.jscience.bibliography;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BibTeX parser and generator.
 */
public class BibTeXParser {

    private BibTeXParser() {
    }

    /**
     * Parses a BibTeX entry.
     * 
     * @param bibtex BibTeX string like "@article{key, title={...}, ...}"
     * @return Map of field names to values
     */
    public static Map<String, String> parse(String bibtex) {
        Map<String, String> result = new LinkedHashMap<>();

        // Extract entry type and key
        Pattern headerPattern = Pattern.compile("@(\\w+)\\s*\\{\\s*([^,]+)\\s*,", Pattern.CASE_INSENSITIVE);
        Matcher headerMatcher = headerPattern.matcher(bibtex);
        if (headerMatcher.find()) {
            result.put("_type", headerMatcher.group(1).toLowerCase());
            result.put("_key", headerMatcher.group(2).trim());
        }

        // Extract fields
        Pattern fieldPattern = Pattern.compile("(\\w+)\\s*=\\s*[{\"](.*?)[}\"]", Pattern.DOTALL);
        Matcher fieldMatcher = fieldPattern.matcher(bibtex);
        while (fieldMatcher.find()) {
            result.put(fieldMatcher.group(1).toLowerCase(), fieldMatcher.group(2).trim());
        }

        return result;
    }

    /**
     * Generates a BibTeX entry from a map.
     */
    public static String generate(String entryType, String key, Map<String, String> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("@").append(entryType).append("{").append(key).append(",\n");

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (!entry.getKey().startsWith("_")) {
                sb.append("  ").append(entry.getKey()).append(" = {").append(entry.getValue()).append("},\n");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Creates a minimal article BibTeX entry.
     */
    public static String createArticle(String key, String author, String title,
            String journal, String year) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("author", author);
        fields.put("title", title);
        fields.put("journal", journal);
        fields.put("year", year);
        return generate("article", key, fields);
    }

    /**
     * Creates a book BibTeX entry.
     */
    public static String createBook(String key, String author, String title,
            String publisher, String year) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("author", author);
        fields.put("title", title);
        fields.put("publisher", publisher);
        fields.put("year", year);
        return generate("book", key, fields);
    }
}
