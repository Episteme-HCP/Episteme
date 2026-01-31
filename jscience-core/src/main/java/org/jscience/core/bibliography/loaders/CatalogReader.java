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

package org.jscience.core.bibliography.loaders;

import org.jscience.core.bibliography.BibliographicalNotice;
import org.jscience.core.io.ResourceReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for loading bibliographical catalogs of books or articles.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface CatalogReader extends ResourceReader<List<BibliographicalNotice>> {

    /**
     * Searches the catalog for works matching the given query string.
     * 
     * @param query the search query
     * @return a list of matching notices
     */
    CompletableFuture<List<BibliographicalNotice>> search(String query);

    /**
     * Loads a specific notice by its identifier (e.g., ISBN, DOI).
     * 
     * @param id the identifier
     * @return the notice, or null if not found
     */
    CompletableFuture<BibliographicalNotice> loadNotice(String id);
}

