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

package org.jscience.biology.loaders.phyloxml;

/**
 * Taxonomic information for a clade.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Taxonomy {
    
    private String id;
    private String idProvider;
    private String code;
    private String scientificName;
    private String commonName;
    private String rank;

    public Taxonomy() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdProvider() { return idProvider; }
    public void setIdProvider(String provider) { this.idProvider = provider; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String name) { this.scientificName = name; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String name) { this.commonName = name; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    @Override
    public String toString() {
        return "Taxonomy{" + scientificName + " (" + commonName + ")}";
    }
}
