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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.identity.SimpleIdentification;

/**
 * Represents a detailed bibliographical notice for a work (book, article, etc.).
 * <p>
 * This record provides a rich set of metadata according to modern library standards.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public record BibliographicalNotice(
    Identification id,
    String title,
    List<String> authors,
    String publisher,
    String publicationYear,
    String type, // "BOOK", "ARTICLE", "PROCEEDINGS", etc.
    String source, // Journal name or Book series
    String volume,
    String issue,
    String pages,
    String doi,
    String isbn,
    String issn,
    String abstractText,
    List<String> keywords,
    Map<String, String> additionalMetadata
) implements Identified<Identification>, Serializable {

    @Override
    public Identification getId() {
        return id;
    }

    /**
     * Helper constructor for String IDs.
     */
    public BibliographicalNotice(String id, String title, List<String> authors, String publisher, String publicationYear, String type) {
        this(new SimpleIdentification(id), title, authors, publisher, publicationYear, type, 
             null, null, null, null, null, null, null, null, null, Map.of());
    }
}
