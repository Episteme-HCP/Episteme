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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.resources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.*;

/**
 * Parser and accessor for the sciences taxonomy database.
 * <p>
 * <b>Data source</b>: Stephen Chrisomalis (2004), licensed for personal use
 * with attribution.
 * Database contains 600+ scientific disciplines from acarology to zoology.
 * </p>
 * 
 * <p>
 * <b>Usage example</b>:
 * </p>
 * 
 * <pre>{@code
 * // Find a science by name
 * Optional<Science> astro = SciencesDatabase.find("astronomy");
 * Science s = astro.get();
 * System.out.println(s.getName() + ": " + s.getDescription());
 * // Output: "Astronomy: study of celestial bodies"
 * 
 * // Search by keyword
 * List<Science> bio = SciencesDatabase.search("biology");
 * // Returns: biology, microbiology, astrobiology, etc.
 * }</pre>
 * 
 * <p>
 * <b>Performance</b>:
 * </p>
 * <ul>
 * <li>Lazy loading: Database loads on first access</li>
 * <li>Cached: Subsequent accesses are O(1)</li>
 * <li>Memory: ~70KB for full database</li>
 * </ul>
 * 
 * @see <a href="http://phrontistery.50megs.com">Phrontistery Word List</a>
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class SciencesDatabase {

    /**
     * Path to the embedded XML database.
     */
    private static final String RESOURCE_PATH = "/org/jscience/taxonomy/sciences.xml";
    private static Map<String, Science> sciences = null;
    private static final Object LOCK = new Object();

    /**
     * Represents a scientific discipline.
     */
    public static class Science {
        private final String name;
        private final String description;

        public Science(String name, String description) {
            this.name = name;
            this.description = description;
        }

        /**
         * Gets the name of the science (e.g., "astronomy").
         * 
         * @return lowercase science name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the description (e.g., "study of celestial bodies").
         * 
         * @return description
         */
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return name + ": " + description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Science))
                return false;
            Science science = (Science) o;
            return name.equals(science.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    /**
     * Finds a science by exact name match (case-insensitive).
     * <p>
     * Complexity: O(1) after initial load
     * </p>
     * 
     * @param name science name (case-insensitive)
     * @return Optional containing the science if found
     */
    public static Optional<Science> find(String name) {
        ensureLoaded();
        return Optional.ofNullable(sciences.get(name.toLowerCase()));
    }

    /**
     * Searches for sciences containing the keyword in name or description.
     * <p>
     * Complexity: O(n) where n is number of sciences (~600)
     * </p>
     * 
     * @param keyword search term (case-insensitive)
     * @return list of matching sciences (may be empty)
     */
    public static List<Science> search(String keyword) {
        ensureLoaded();
        String lowerKeyword = keyword.toLowerCase();
        List<Science> results = new ArrayList<>();

        for (Science science : sciences.values()) {
            if (science.getName().contains(lowerKeyword) ||
                    science.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(science);
            }
        }

        return results;
    }

    /**
     * Gets all sciences in the database.
     * <p>
     * Returns unmodifiable collection of all 600+ sciences.
     * </p>
     * 
     * @return all sciences
     */
    public static Collection<Science> getAll() {
        ensureLoaded();
        return Collections.unmodifiableCollection(sciences.values());
    }

    /**
     * Gets the number of sciences in the database.
     * 
     * @return count (typically 600+)
     */
    public static int count() {
        ensureLoaded();
        return sciences.size();
    }

    /**
     * Ensures database is loaded (thread-safe lazy initialization).
     */
    private static void ensureLoaded() {
        if (sciences == null) {
            synchronized (LOCK) {
                if (sciences == null) {
                    sciences = loadDatabase();
                }
            }
        }
    }

    /**
     * Loads and parses the sciences XML database.
     * <p>
     * Parses /org/jscience/sciences.xml from classpath.
     * </p>
     * 
     * @return map of science name to Science object
     * @throws RuntimeException if parsing fails
     */
    private static Map<String, Science> loadDatabase() {
        Map<String, Science> map = new HashMap<>();

        try {
            InputStream is = SciencesDatabase.class.getResourceAsStream(RESOURCE_PATH);
            if (is == null) {
                throw new IllegalStateException("Resource not found: " + RESOURCE_PATH);
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);

            NodeList scienceNodes = doc.getElementsByTagName("science");

            for (int i = 0; i < scienceNodes.getLength(); i++) {
                Element scienceElement = (Element) scienceNodes.item(i);

                String name = getTextContent(scienceElement, "name");
                String description = getTextContent(scienceElement, "description");

                if (name != null && description != null) {
                    map.put(name.toLowerCase(), new Science(name, description));
                }
            }

            is.close();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load sciences database", e);
        }

        return map;
    }

    /**
     * Extracts text content from XML element.
     */
    private static String getTextContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return null;
    }

    // Prevent instantiation
    private SciencesDatabase() {
    }
}

