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

import java.util.Collections;
import java.util.Set;

/**
 * A no-frills implementation of Taxon.
 * @version 1.0
 * @author Silvere Martin-Michiellot
 */

/**
 * A no-frills implementatation of Taxon.
 * <p/>
 * <p>A TaxonFactory implementation will probably wish to sub-class
 * this and add package-private accessors for the parent and children
 * fields.</p>
 */

//strongly inspired and enhanced from Biojava, http://www.biojava.org, original code under Lesser GPL
//a enhanced version of this class could be build out of org.jscience.util.Tree
public class SimpleTaxon extends AbstractTaxon {
    protected Taxon parent;
    protected Set children;

    /**
     * Create a new instance with no parent, no children and given
     * scientific and common names.
     */
    public SimpleTaxon(String scientificName, String commonName) {
        super(scientificName, commonName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Taxon getParent() {
        return parent;
    }

    void setParent(Taxon parent) {
        this.parent = parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRoot() {
        return parent != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLeaf() {
        return children == null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChildren() {
        if (children != null) {
            return children;
        } else {
            return Collections.EMPTY_SET;
        }
    }
}
