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

package org.jscience.biology.taxonomy;

import org.jscience.util.CircularReferenceException;


/**
 * Encapsulate the mapping between Taxon and stringified representations of
 * taxa.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface TaxonParser {
    /**
     * Convert a stringified Taxon into a Taxon instance.
     *
     * @param taxonFactory the TaxonFactory used to instantiate taxa instances
     * @param taxonString the String to parse
     *
     * @return a Taxon instance created by the TaxonFactory from the
     *         taxonString
     *
     * @throws CircularReferenceException DOCUMENT ME!
     */
    public Taxon parse(TaxonFactory taxonFactory, String taxonString)
        throws CircularReferenceException;

    /**
     * Convert a Taxon into a stringified representation.
     *
     * @param taxon the Taxon to serialize
     *
     * @return the stringified version of Taxon
     */
    public String serialize(Taxon taxon);
}
